package com.yunpower.collect.storage.executor;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.yunpower.collect.protocols.websocket.WebSocketPushService;
import com.yunpower.collect.storage.StorageVariables;
import com.yunpower.collect.storage.domain.MonitorDeviceVar;
import com.yunpower.collect.storage.domain.ShardingDay;
import com.yunpower.collect.storage.domain.ShardingQuery;
import com.yunpower.collect.storage.service.StorageCommonService;
import com.yunpower.common.core.constant.CacheConstants;
import com.yunpower.common.core.constant.Constants;
import com.yunpower.common.core.entity.amqp.DeviceStateMessage;
import com.yunpower.common.core.entity.amqp.DeviceVarCollectMessage;
import com.yunpower.common.core.enums.WebSocketMessageEnum;
import com.yunpower.common.core.utils.AviatorUtils;
import com.yunpower.common.core.utils.DoubleUtils;
import com.yunpower.common.core.utils.EltDateUtils;
import com.yunpower.common.core.utils.TimeRoundingUtils;
import com.yunpower.common.core.vo.EnumSOTVO;
import com.yunpower.common.core.vo.WebSocketBaseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.yunpower.common.core.constant.Constants.CHANNEL_SN;

/**
 * 计算内存型变量-执行器
 */
public class CalculateMemoryVariablesExecutor implements Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(CalculateMemoryVariablesExecutor.class);

	@Override
	public void run() {
		// 获取上下文属性中的 channelSn
		MDC.put(CHANNEL_SN, Constants.CALCULATE_MEMORY_VARIABLES);

		LOGGER.info("计算内存型变量开始");
		AtomicInteger size = new AtomicInteger();
		TimeInterval timer = DateUtil.timer();

		try {
			//获取内存型变量数据
			MonitorDeviceVar deviceVar = new MonitorDeviceVar();
			deviceVar.setOrigin(2);
			deviceVar.setDeleteFlag(0);
			deviceVar.getParams().put("hasComputeFormula", 1);
			List<MonitorDeviceVar> deviceVarList = StorageVariables.monitorDeviceVarService.selectMonitorDeviceVarList(deviceVar);
			LOGGER.info("获取到内存型变量数据，共{}条", deviceVarList.size());
			size.set(0);

			Map<String, Long> deviceSnMap = new HashMap<>();
			deviceVarList.forEach(item -> {
				boolean result = handleVariables(item);
				if (result) {
					size.addAndGet(1);
					// 设备上线，先写入map，相同的deviceSn 可以覆盖，循环结束后再写入 MQ
					deviceSnMap.put(deviceVar.getDeviceSn(), deviceVar.getDeviceId());
				}
			});

			//发送设备状态
			sendMonitorDeviceState(deviceSnMap);

		} catch (Exception ex) {
			LOGGER.error("计算内存型变量-执行器异常", ex);
		} finally {
			LOGGER.info("计算内存型变量结束，共处理{}条数据 耗时：{} 毫秒", size.get(), timer.interval());
			MDC.remove(CHANNEL_SN);
		}
	}

	private boolean handleVariables(MonitorDeviceVar deviceVar) {
		boolean result = false;
		LOGGER.info("设备变量编码：{} 开始计算", deviceVar.getVarSn());
		try {

			//检查是否配置 缺失值处理（1不计算 2使用最近值 3使用0值 4使用1值）
			if (deviceVar.getDeletionHandle() == null) {
				LOGGER.info("设备变量编码：{} 缺失值处理为空,执行结束", deviceVar.getVarSn());
				return result;
			}

			//检查变量系数是否设置
			double coefficient = 1;
			if (deviceVar.getCoefficient() != null) {
				coefficient = DoubleUtils.parse(deviceVar.getCoefficient());
			}

			//1、检查公式内是否有变量
			List<String> varSnList = AviatorUtils.extractVariables(deviceVar.getComputeFormula());
			if (varSnList == null || varSnList.isEmpty()) {
				LOGGER.info("设备变量编码：{} 公式内没有提取到变量", deviceVar.getVarSn());
				return result;
			}
			LOGGER.info("设备变量编码：{} 公式内的变量 {}", deviceVar.getVarSn(),varSnList);

			//2、获取公式内的变量数据  从数据库中过滤一遍 过滤出无效的变量
			MonitorDeviceVar varQuery = new MonitorDeviceVar();
			varQuery.getParams().put("varSns", varSnList);
			varQuery.setDeleteFlag(0);
			List<MonitorDeviceVar> varList = StorageVariables.monitorDeviceVarService.selectMonitorDeviceVarList(varQuery);
			if (ObjectUtil.isEmpty(varList)) {
				LOGGER.info("设备变量编码：{} 公式内变量不存在", deviceVar.getVarSn());
				return result;
			}

			//3、判断变量数量是否一致
			List<String> varSnListNew = varList.stream().map(MonitorDeviceVar::getVarSn).collect(Collectors.toList());
			if (varSnListNew.size() != varList.size()) {
				LOGGER.info("设备变量编码：{} 公式内变量数量不一致 公式内变量:{},数据库筛选后:{}", deviceVar.getVarSn(), varSnList, varSnListNew);
				return result;
			}
			Map<String, MonitorDeviceVar> varSnMap = varList.stream()
					.collect(Collectors.toMap(MonitorDeviceVar::getVarSn, i -> i));

			//获取saveTime 保证公式内所有变量都有数据 故往前推移变量的存盘时间
			String lastScaleTime = TimeRoundingUtils.getNextScaleTime(DateUtil.now(), - deviceVar.getSaveCycle());
			Date saveTime = EltDateUtils.dateTime(EltDateUtils.YYYY_MM_DD_HH_MM_SS, lastScaleTime);
			String saveTimeStr = DateUtil.formatDateTime(saveTime);

			//设置查询条件
			ShardingQuery query = new ShardingQuery();
			//存储变量信息
			Map<String, Object> env = new HashMap<>();
			//是否需要计算
			boolean calculate = true;

			//遍历每一项获取数据
			for (Map.Entry<String, MonitorDeviceVar> entry : varSnMap.entrySet()) {
				query = new ShardingQuery();
				query.setVariableName(entry.getKey());

				//获取公式内变量系数
				Float _coefficient = ObjectUtil.isNull(entry.getValue().getCoefficient()) || entry.getValue().getCoefficient() <= 0 ? 1F : entry.getValue().getCoefficient();

				//获取存盘时间
				if (ObjectUtil.isNotNull(entry.getValue().getSaveCycle())) {
					if (entry.getValue().getSaveCycle() < 60) {
						query.getParams().put("minute", entry.getValue().getSaveCycle());
					} else if (entry.getValue().getSaveCycle() == 60) {
						query.getParams().put("hour", entry.getValue().getSaveCycle());
					}
				}
				query.setSaveTime(saveTime);
				ShardingDay dataEntity = StorageVariables.shardingCommonService.selectDayValue(query);

				//如果获取到数据 则说明没有确实，继续查询一下个
				if (dataEntity != null) {
					env.put(entry.getKey(), DoubleUtils.parse(dataEntity.getDataValue() * _coefficient));
					continue;
				}
				if (deviceVar.getDeletionHandle() == 1) {
					LOGGER.info("设备变量编码：{} 缺失值处理为1，不计算", deviceVar.getVarSn());
					calculate = false;
					break;
				}
				if (deviceVar.getDeletionHandle() == 2) {
					//近似值
					//获取近24小时数据
					dataEntity = StorageVariables.shardingCommonService.selectLastDayValue24H(entry.getKey());
					if (dataEntity != null) {
						env.put(entry.getKey(), DoubleUtils.parse(dataEntity.getDataValue() * _coefficient));
					}
				} else if (deviceVar.getDeletionHandle() == 3) {
					//使用0值
					env.put(entry.getKey(), 0D);
				} else if (deviceVar.getDeletionHandle() == 4) {
					//使用1值
					env.put(entry.getKey(), 1);
				}
			}
			LOGGER.info("设备变量编码：{} 公式内变量值获取完成：{}", deviceVar.getVarSn(), env);
			//需要计算
			if (calculate) {

				if (env.size() != varSnMap.size()) {
					LOGGER.info("设备变量编码：{} 公式内变量值获取完成：{}，与公式内变量数量不一致", deviceVar.getVarSn(), env);
					return false;
				}

				//计算后的总值
				double _total = DoubleUtils.parse(AviatorUtils.execute(deviceVar.getComputeFormula(), env));

				//计算系数
				double total = DoubleUtils.parse(_total * coefficient);
				LOGGER.info("设备变量编码：{} 公式内变量值计算完成：{} 变量数据系数：{},最终结果：{}", deviceVar.getVarSn(), _total,coefficient, total);

				//0值计算处理
				// 如果逆变器停止发电后上传0值时，需要选择：零值计算（计算），补当前数据（补前一条）其它情况下不选
				//if(total == 0){
				//	//是否需要零值计算
				//	boolean isZeroCompute = ObjectUtil.equal(deviceVar.getZeroCompute(),1) && ObjectUtil.equal(deviceVar.getRepairData(),1);
				//	LOGGER.debug("设备变量编码：{} 零值计算配置：{} 补充当前数据配置：{},结果：{}", deviceVar.getVarSn(), deviceVar.getZeroCompute(),deviceVar.getRepairData(),isZeroCompute);
				//	if(isZeroCompute){
				//		EnumSOTVO redisVo = StorageCommonService.lastSaveRedisData(deviceVar.getVarSn(),true);
				//		if(redisVo != null){
				//			LOGGER.debug("设备变量编码：{} 上次存盘值：{}", deviceVar.getVarSn(), redisVo.getValue());
				//			total = DoubleUtils.stringToDouble(redisVo.getValue().toString());
				//		}
				//	}
				//}

				// 保存到 redis 中（实时数据）
				EnumSOTVO redisVO = new EnumSOTVO(deviceVar.getVarSn(), DoubleUtils.parse2(total), DateUtil.now());
				String valueStr = JSON.toJSONString(redisVO);
				StorageVariables.redisService.setCacheObject(Constants.MONITOR_DEVICE_VAR_SN_KEY + deviceVar.getVarSn(), valueStr, CacheConstants.EXPIRATION, TimeUnit.MINUTES);

				//发送消息到webSocket
				WebSocketBaseMessage baseMessage = new WebSocketBaseMessage(WebSocketMessageEnum.DEVICE_RUNTIME_MESSAGE, redisVO);
				WebSocketPushService.pushMsgToOne(deviceVar.getVarSn(), JSON.toJSONString(baseMessage));

				//数据存储
				DeviceVarCollectMessage message = DeviceVarCollectMessage.builder()
						.deviceVarSn(deviceVar.getVarSn())
						.saveTime(saveTimeStr)
						.value(total)
						.collectTime(DateUtil.now())
						.build();
				StorageCommonService.doStorage(message, true);
			}
			result = true;
		} catch (Exception ex) {
			LOGGER.error("处理内存型变量 设备变量编码：{} 异常信息", deviceVar.getVarSn(), ex);
		}
		return result;
	}


	/**
	 * 发送监控设备状态
	 */
	private void sendMonitorDeviceState(Map<String, Long> deviceSnMap) {
		for (Map.Entry<String, Long> item : deviceSnMap.entrySet()) {
			//设备状态变更
			DeviceStateMessage stateMessage = DeviceStateMessage.builder()
					.deviceSn(item.getKey())
					.state(1)
					.type(Constants.MONITOR_DEVICE_SUFFIX)
					.changeTime(DateUtil.now())
					.build();
			StorageCommonService.changeState(stateMessage, true);
		}
	}
}
