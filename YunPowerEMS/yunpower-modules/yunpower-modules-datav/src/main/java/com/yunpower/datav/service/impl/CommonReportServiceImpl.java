package com.yunpower.datav.service.impl;

import com.yunpower.common.core.DoubleUtils;
import com.yunpower.common.core.constant.SecurityConstants;
import com.yunpower.common.core.exception.ServiceException;
import com.yunpower.common.core.utils.DateUtils;
import com.yunpower.common.core.utils.StringUtils;
import com.yunpower.datav.domain.ShardingMonth;
import com.yunpower.datav.domain.ShardingQuery;
import com.yunpower.datav.domain.dto.ReportConfigDto;
import com.yunpower.datav.domain.vo.ChartQueryVo;
import com.yunpower.datav.domain.vo.ReportQueryVo;
import com.yunpower.datav.domain.vo.report.TableHeader;
import com.yunpower.datav.domain.vo.report.TableResult;
import com.yunpower.datav.enums.DatavDateDimEnum;
import com.yunpower.datav.service.CommonReportService;
import com.yunpower.datav.service.ShardingCommonService;
import com.yunpower.datav.utils.GenerateTimeUtils;
import com.yunpower.system.api.RemoteMonitorDeviceVarService;
import com.yunpower.system.api.domain.FeignMonitorDeviceVar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * 通用报表服务
 *
 * @Author: Jiajiaglam
 * @Date: 2024/6/20 7:51
 */
@Service
public class CommonReportServiceImpl implements CommonReportService {
	@Autowired
	private ShardingCommonService shardingCommonService;

	@Autowired
	private RemoteMonitorDeviceVarService remoteMonitorDeviceVarService;

	/**
	 * 处理报表数据
	 *
	 * @param reportQueryVo 查询参数
	 */
	@Override
	public Object handleReportData(ReportQueryVo reportQueryVo) {
		//组装时间
		packageTime(reportQueryVo);

		//组装表头和数据
		return packageHeader(reportQueryVo);
	}

	//region 组装表头

	/**
	 * 组装表头
	 *
	 * @param configVo 查询参数
	 */
	private TableResult packageHeader(ReportQueryVo configVo) {

		//region ***表头结构***
		//|--【第1列】
		//|----title:日期
		//|----dataIndex:date
		//|----children
		//|--【第2列】
		//|----title:设备名称
		//|----dataIndex:设备名称
		//|----children:
		//|------【第2-1列】
		//|--------title:变量名称
		//|--------dataIndex:变量名称
		//|--------children:
		//|----------【第2-1-1列】
		//|------------name:峰谷平名称
		//|------------dataIndex:匹配数据的唯一标识（只有最后一级时才存在）
		//|------【第2-2...列（重复第2-1列）】
		//|--------...
		//|--【第3...N列（重复第2列）】
		//|----title:设备名称
		//|----dataIndex:设备名称
		//endregion

		//region 组成第一部分Header -- start
		List<TableHeader> headerList = new ArrayList<>();

		TableHeader dateHeader = new TableHeader("日期", "date", null);
		headerList.add(dateHeader);

		for (ReportConfigDto dto : configVo.getHeadConfigList()) {
			packageTree(headerList, dto);
		}

		//如果是峰谷报表，再封装一层头
		if (configVo.getIsFengGuData()) {
			packageFengGuTree(headerList);
		}

		//endregion 组成第一部分Header -- end

		//region ***数据结构***
		//tableData: [
		//  {
		//    date: "2024-05-17",
		//    "匹配数据的唯一标识": 10,
		//    "匹配数据的唯一标识": 20,
		//    "匹配数据的唯一标识": 30
		//  }
		//]
		//endregion

		//region 组成第二部分 Rows -- Start
		// 数据集
		List<Map<String, String>> tableData = new ArrayList<>();
		Map<String, Map<String, String>> tempData = new HashMap<>();

		// 日期参数
		Map<String, Object> dateToParamForDay = DateUtils.dateToParamForDay(configVo.getBeginTime(), configVo.getEndTime());

		//region 获取变量系数
		//变量SN列表-去重
		Set<String> setVarSn = configVo.getHeadConfigList().stream().map(ReportConfigDto::getDeviceVarId).collect(Collectors.toSet());

		//远程调用 ---> System服务 查询出变量系数
		FeignMonitorDeviceVar varParam = new FeignMonitorDeviceVar();
		varParam.getParams().put("varSns", setVarSn);
		List<FeignMonitorDeviceVar> deviceVarList = remoteMonitorDeviceVarService.getListInner(varParam, SecurityConstants.INNER);
		if (StringUtils.isEmpty(deviceVarList)) {
			throw new ServiceException("未找到对应变量");
		}

		//转成 map 内存型变量系统给设置成1
		Map<String, Double> coefficientMap = deviceVarList.stream()
				.collect(Collectors.toMap(
						FeignMonitorDeviceVar::getVarSn,
						item -> item.getCoefficient() == null ? 1L : DoubleUtils.floatToDouble(item.getCoefficient())
				));
		//endrgion

		for (ReportConfigDto item : configVo.getHeadConfigList()) {
			List<ShardingMonth> shardingMonthList = new ArrayList<>();

			// 日维度数据（日维度只有 实时值）
			if (Objects.equals(configVo.getDateDim(), DatavDateDimEnum.DAY.getKey())) {
				ShardingQuery query = new ShardingQuery(item.getDeviceVarId(), 1, 1, dateToParamForDay, coefficientMap.get(item.getDeviceVarId()));
				query.getParams().put("hour", "0");
				shardingMonthList = shardingCommonService.continuityChangeDayDay(query);
			}

			// 月维度数据（月维度有 累积值、平均值、最小值、最大值）（如果是峰谷平报表，只有累积值）
			if (Objects.equals(configVo.getDateDim(), DatavDateDimEnum.MONTH.getKey())) {
				if (configVo.getIsFengGuData()) {
					//峰谷数据
					ShardingQuery query = new ShardingQuery(item.getDeviceVarId(), 2, 5, dateToParamForDay, coefficientMap.get(item.getDeviceVarId()));
					query.setIsCharge(configVo.getIsCharge());
					shardingMonthList = shardingCommonService.continuityFengGuMonthDay(query);
				} else {
					//基础数据
					ShardingQuery query = new ShardingQuery(item.getDeviceVarId(), (configVo.getChangeType() < 5 ? 1 : 2), configVo.getChangeType(), dateToParamForDay, coefficientMap.get(item.getDeviceVarId()));
					shardingMonthList = shardingCommonService.continuityChangeMonthDay(query);
				}
			}

			// 年维度数据（年维度有 累积值、平均值、最小值、最大值）（如果是峰谷平报表，只有累积值）
			if (Objects.equals(configVo.getDateDim(), DatavDateDimEnum.YEAR.getKey())) {
				if (configVo.getIsFengGuData()) {
					//峰谷数据
					ShardingQuery query = new ShardingQuery(item.getDeviceVarId(), 2, 5, dateToParamForDay, coefficientMap.get(item.getDeviceVarId()));
					query.setIsCharge(configVo.getIsCharge());
					shardingMonthList = shardingCommonService.continuityFengGuYearDay(query);
				} else {
					//基础数据
					ShardingQuery query = new ShardingQuery(item.getDeviceVarId(), (configVo.getChangeType() < 5 ? 1 : 2), configVo.getChangeType(), dateToParamForDay, coefficientMap.get(item.getDeviceVarId()));
					shardingMonthList = shardingCommonService.continuityChangeYearDay(query);
				}
			}

			// 将数据反向补充到结果中
			if (shardingMonthList.isEmpty()) {
				continue;
			}

			if (configVo.getIsFengGuData()) {
				// 峰谷数据
				for (int i = 0; i < configVo.getTimeList().size(); i++) {
					ChartQueryVo.TimeInfo timeInfo = configVo.getTimeList().get(i);
					if (timeInfo.getValue() == null) {
						continue;
					}

					// 筛选出指定时间的记录
					List<ShardingMonth> filterList = shardingMonthList.stream().filter(shardingMonth -> Objects.equals(shardingMonth.getFormattedDatetime(), timeInfo.getKey())).collect(Collectors.toList());

					// 判断是筛选出的记录中是否全包含了“峰谷平尖”等
					List<String> cloneList = fengPingGuList();

					for (ShardingMonth sm : filterList) {
						packageResult(timeInfo.getKey(), item.getDeviceVarId() + "-" + sm.getSeasonalTypeName(), sm.getDataValue(), tempData);
						cloneList.remove(sm.getSeasonalTypeName());
					}

					// 将没有的记录以0值插入
					for (String legend : cloneList) {
						packageResult(timeInfo.getKey(), item.getDeviceVarId() + "-" + legend, 0D, tempData);
					}
				}

			} else {
				// 基础数据
				Map<String, Double> shardingMonthMap = shardingMonthList.stream().collect(Collectors.toMap(ShardingMonth::getFormattedDatetime, ShardingMonth::getDataValue));
				for (int i = 0; i < configVo.getTimeList().size(); i++) {
					ChartQueryVo.TimeInfo timeInfo = configVo.getTimeList().get(i);
					if (timeInfo.getValue() == null) {
						continue;
					}

					packageResult(timeInfo.getKey(), item.getDeviceVarId(), shardingMonthMap.getOrDefault(timeInfo.getKey(), 0d), tempData);
				}
			}
		}

		// 重新组装结果数据
		for (int i = 0; i < configVo.getTimeList().size(); i++) {
			ChartQueryVo.TimeInfo timeInfo = configVo.getTimeList().get(i);
			if (tempData.containsKey(timeInfo.getKey())) {
				tableData.add(tempData.get(timeInfo.getKey()));
			}
		}

		//endregion 组成第二部分 Rows -- End

		return new TableResult(headerList, tableData);
	}
	//endregion

	//region 【表头】将平行结构转成树状结构

	/**
	 * 【表头】将平行结构转成树状结构
	 *
	 * @param packageList 要组装的结构
	 * @param originDto   插入的数据
	 */
	private List<TableHeader> packageTree(List<TableHeader> packageList, ReportConfigDto originDto) {
		//多级表头用“/”分隔。例如：公寓/单元一
		String[] names = originDto.getDeviceVarName().split("/");
		AtomicBoolean isExist = new AtomicBoolean(false);
		for (TableHeader item : packageList) {
			if (item.getTitle().equals(names[0])) {
				if (names.length > 1) {
					originDto.setDeviceVarName(originDto.getDeviceVarName().substring(originDto.getDeviceVarName().indexOf("/") + 1));
					if (item.getChildren() == null) {
						item.setChildren(new ArrayList<>());
					}
					item.setChildren(packageTree(item.getChildren(), originDto));
				}

				isExist.set(true);
				break;
			}
		}

		if (!isExist.get()) {
			TableHeader dto = new TableHeader(names[0], originDto.getDeviceVarId(), null);
			if (names.length > 1) {
				originDto.setDeviceVarName(originDto.getDeviceVarName().substring(originDto.getDeviceVarName().indexOf("/") + 1));
				dto.setChildren(new ArrayList<>());
				dto.setChildren(packageTree(dto.getChildren(), originDto));
			}
			packageList.add(dto);
		}

		return packageList;
	}

	/**
	 * 【表头】给表头添加“峰谷平”标签
	 *
	 * @param packageList 要组装的结构
	 */
	private void packageFengGuTree(List<TableHeader> packageList) {
		//峰谷平维度
		String[] legends = "尖,峰,平,谷,深谷".split(",");

		packageList.forEach(item -> {
			if (Objects.equals(item.getDataIndex(), "date")) {
				return;
			}

			if (item.getChildren() == null) {
				List<TableHeader> children = new ArrayList<>();
				for (String leg : legends) {
					TableHeader dto = new TableHeader(leg, item.getDataIndex() + "-" + leg, null);
					children.add(dto);
				}
				item.setChildren(children);
				return;
			}

			packageFengGuTree(item.getChildren());
		});
	}
	//endregion

	//region 组装时间数据

	/**
	 * 组装时间数据
	 * <pre>
	 *     beginTime 开始时间
	 *     endTime   结束时间
	 * </pre>
	 *
	 * @param configVo 查询参数
	 **/
	private void packageTime(ReportQueryVo configVo) {
		String beginTime = "";
		String endTime = "";

		//格式化【日】时间 00:05～24:00 00:10～24:00 00:15～24:00 00:30～24:00 01:00～24:00
		if (StringUtils.isNotEmpty(configVo.getDayTime())) {
			configVo.setDateDim(DatavDateDimEnum.DAY.getKey());
			beginTime = DateUtils.completionDayTime(configVo.getDayTime(), true);
			endTime = DateUtils.completionDayTime(configVo.getDayTime(), false);
			//二级维度只有：时
			configVo.setTimeList(GenerateTimeUtils.generateMinuteHourSlots(beginTime, endTime, 60, false));
		}
		// 格式化【月】时间 1号～30号/31号
		if (StringUtils.isNotEmpty(configVo.getMonthTime())) {
			configVo.setDateDim(DatavDateDimEnum.MONTH.getKey());
			beginTime = DateUtils.completionMonthTime(configVo.getMonthTime(), true);
			endTime = DateUtils.completionMonthTime(configVo.getMonthTime(), false);
			configVo.setTimeList(GenerateTimeUtils.generateMonthlySlots(beginTime, endTime, false));
		}

		// 格式化【年】时间 1月～12月
		if (StringUtils.isNotEmpty(configVo.getYearTime())) {
			configVo.setDateDim(DatavDateDimEnum.YEAR.getKey());
			beginTime = DateUtils.completionYearTime(configVo.getYearTime(), true);
			endTime = DateUtils.completionYearTime(configVo.getYearTime(), false);
			configVo.setTimeList(GenerateTimeUtils.generateYearlySlots(beginTime, endTime, false));
		}

		configVo.setBeginTime(beginTime);
		configVo.setEndTime(endTime);
	}

	//endregion

	//region 组装结果数据
	private void packageResult(String date, String dataKey, Double dataValue, Map<String, Map<String, String>> resultMap) {
		if (resultMap.get(date) == null) {
			Map<String, String> map = new HashMap<>();
			map.put("date", date);
			map.put(dataKey, DoubleUtils.toPlainString(dataValue));
			resultMap.put(date, map);
		} else {
			resultMap.get(date).put(dataKey, DoubleUtils.toPlainString(dataValue));
		}
	}
	//endregion

	//region 工具类
	private List<String> fengPingGuList() {
		List<String> list = new ArrayList<>();
		list.add("尖");
		list.add("峰");
		list.add("平");
		list.add("谷");
		list.add("深谷");
		return list;
	}
	//endregion
}
