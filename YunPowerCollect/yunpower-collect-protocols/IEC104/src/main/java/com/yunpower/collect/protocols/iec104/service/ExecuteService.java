package com.yunpower.collect.protocols.iec104.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.yunpower.collect.protocols.iec104.IEC104Variables;
import com.yunpower.collect.protocols.iec104.container.IEC104Link;
import com.yunpower.collect.protocols.iec104.container.LinkContainer;
import com.yunpower.collect.protocols.iec104.enums.TypeIdentifierEnum;
import com.yunpower.collect.protocols.iec104.future.FutureNameEnum;
import com.yunpower.collect.protocols.iec104.future.FutureUtils;
import com.yunpower.collect.protocols.iec104.message.MessageBody;
import com.yunpower.collect.protocols.iec104.message.MessageInfo;
import com.yunpower.collect.protocols.iec104.task.StartFrameExecutor;
import com.yunpower.collect.protocols.websocket.WebSocketPushService;
import com.yunpower.collect.storage.StorageVariables;
import com.yunpower.collect.storage.domain.CommunicationChannel;
import com.yunpower.collect.storage.domain.CommunicationDevice;
import com.yunpower.collect.storage.domain.MonitorDeviceVar;
import com.yunpower.common.core.constant.CacheConstants;
import com.yunpower.common.core.constant.Constants;
import com.yunpower.common.core.constant.RabbitConstants;
import com.yunpower.common.core.entity.amqp.DeviceStateMessage;
import com.yunpower.common.core.entity.amqp.DeviceVarCollectMessage;
import com.yunpower.common.core.enums.WebSocketMessageEnum;
import com.yunpower.common.core.service.ScheduledService;
import com.yunpower.common.core.utils.*;
import com.yunpower.common.core.vo.EnumSOTVO;
import com.yunpower.common.core.vo.WebSocketBaseMessage;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @title:
 * @Author: Jiajiaglam
 * @date: 2023-12-21 16:38
 * @description:
 */
public class ExecuteService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExecuteService.class);

	/**
	 * 检查通道
	 */
	private static CommunicationChannel checkChannel(ChannelHandlerContext ctx, String connectKey) {
		CommunicationChannel channel = StorageVariables.REGISTER_LIST.get(connectKey);
		if (channel == null) {
			LOGGER.error("未查询到通道：{}", connectKey);

			// 如果没有查询到通道，取消所有线程
			FutureUtils.cancleAllFuture(ctx);

			// 同时主动断开连接，重新注册
			ctx.channel().close();
			return null;
		}
		return channel;
	}

	/**
	 * 通道注册服务
	 */
	public void doRegister(ChannelHandlerContext ctx, String connectKey, String registerCode) {
		List<CommunicationChannel> channelList = StorageVariables.GLOBAL_CHANNEL_LIST.stream()
				.filter(s -> ObjectUtil.isNotNull(s.getRegistrationCode()) && s.getRegistrationCode().equals(registerCode)).collect(Collectors.toList());
		if (channelList.isEmpty()) {
			LOGGER.info("未查询到注册码为（{}）的设备！", registerCode);
			return;
		}
		CommunicationChannel channel = channelList.get(0);
		LOGGER.info("接收到注册码：{}", registerCode);
		StorageVariables.REGISTER_LIST.put(connectKey, channel);

		// 加入容器，用于发送总召唤和回复帧（方案2）
		String localIpAddress = ((InetSocketAddress) ctx.channel().localAddress()).getHostString();
		int localPort = ((InetSocketAddress) ctx.channel().localAddress()).getPort();
		LinkContainer.getInstance().getLinks().put(ctx.channel().id(), new IEC104Link(ctx.channel(), localIpAddress, localPort, IEC104Link.Role.SLAVER));

		LOGGER.info("注册成功，发送链路启动命令！");
		ScheduledFuture<?> startFrameFuture = ScheduledService.getInstance().scheduleAtFixedRate(new StartFrameExecutor(ctx), 0, 5, TimeUnit.SECONDS);
		IEC104Variables.GLOBAL_SCHEDULED_FUTURE_MAP.put(FutureNameEnum.START_FRAME_NAME.getValue() + ctx.channel().id().asShortText(), startFrameFuture);

		//发送上线消息到MQ
		sendCommunicationState(channel);
	}

	/**
	 * 数据存盘
	 */
	public void doStorage(ChannelHandlerContext ctx, String connectKey, MessageBody messageBody, String dateTime) {

		// 时间要放到这里？还是外面？因为有锁，需要等待......
		if (messageBody.getMessages() == null) {
			return;
		}
		//标准时间
		String standardDateTime = EltDateUtils.formatDateLong(dateTime);

		//检查通道
		CommunicationChannel channel = checkChannel(ctx, connectKey);
		if (channel == null) return;

		//发送通道数据包
		sendChannelData(connectKey, messageBody.getHexString(), standardDateTime);

		// 通过 channel_id 和 ggdz 获取通讯管理机ID
		Long communicationDeviceId = StorageVariables.GLOBAL_CHANNEL_GGDZ_DEVICE_MAP.get(channel.getId() + "-" + messageBody.getTerminalAddress());
		if (communicationDeviceId == null) {
			LOGGER.error("未查询到通讯管理机！");
			return;
		}
		//通讯管理机上线 --> MQ
		sendCommunicationDeviceState(communicationDeviceId);

		// 取出当前设备（通讯管理机）的变量列表 格式==> 通讯设备ID:<信息体地址：变量编码>
		Map<Integer, MonitorDeviceVar> storageVarMap = StorageVariables.GLOBAL_CHANNEL_DEVICE_STORAGE_VARIABLE.get(communicationDeviceId);
		if (ObjectUtil.isEmpty(storageVarMap)) {
			LOGGER.error("未查询到电力设备！");
			return;
		}
		//endregion
		Map<String, Long> deviceSnMap = new HashMap<>();
		for (MessageInfo item : messageBody.getMessages()) {

			if (item.getMessageInfos().length == 0) {
				continue;
			}

			//region // 取出信息体中的变量

			// 信息体地址
			int storageAddress = item.getMessageAddress();

			// 通过寄存区地址取出变量编码
			MonitorDeviceVar deviceVar = storageVarMap.get(storageAddress);
			if (deviceVar == null) {
				LOGGER.error("未查询到当前通讯设备：{} 下信息体地址为：{}的变量！", communicationDeviceId, storageAddress);
				continue;
			}

			// 设备上线，先写入map，相同的deviceSn 可以覆盖，循环结束后再写入 MQ
			deviceSnMap.put(deviceVar.getDeviceSn(), deviceVar.getDeviceId());

			// 数据值
			double value = 0;

			// 遥测数据（短浮点数）
			if (TypeIdentifierEnum.shortFloatingPointTelemetry.equals(messageBody.getTypeIdentifier())) {
				//低前高后：需要反转数组
				value = Telemetry.Bytes2Double_IEEE754(ByteUtils.reByte(item.getMessageInfos()));
			}

			// 单点遥信
			if (TypeIdentifierEnum.onePointTeleindication.equals(messageBody.getTypeIdentifier())) {
				value = item.getMessageInfos()[0];
			}
			//endregion

			// 保存到 redis 中（实时数据）
			double _value = DoubleUtils.parse2(value);
			EnumSOTVO redisVO = new EnumSOTVO(deviceVar.getVarSn(), _value, standardDateTime);
			//状态量 double to int
			if (deviceVar.getVarType() == 2) {
				redisVO.setValue((int) _value);
			}
			String valueStr = JSON.toJSONString(redisVO);
			LOGGER.debug("set to redis varSn={},value={}", deviceVar.getVarSn(), valueStr);
			StorageVariables.redisService.setCacheObject(getVariableCacheKey(deviceVar.getVarSn()), valueStr, CacheConstants.EXPIRATION, TimeUnit.MINUTES);

			//发送消息到webSocket
			WebSocketBaseMessage baseMessage = new WebSocketBaseMessage(WebSocketMessageEnum.DEVICE_RUNTIME_MESSAGE, redisVO);
			WebSocketPushService.pushMsgToOne(deviceVar.getVarSn(), JSON.toJSONString(baseMessage));

			// 存盘周期：0-不存盘
			if (deviceVar.getSaveCycle() <= 0) {
				LOGGER.debug("不存盘！");
				continue;
			}

			/*
			  以前这种方式有点问题，就是会形成时间断层，比如5分钟的整点正好没传上数据来。
			  换种思路：
			  拿   12:05 分来举例
			  例如 12:04 上来过数据，但是 12:05 没有数据，这说明 12:05 的时候数据没有发生变化
			  那么 12:05 的数据就应该和 12:04 的数据是一样的
			  所以，每一次上传数据都要保存，向后保存
			 */

			// 数据存储时间（时间后移）实际存储时间
			String nextScaleTime = TimeRoundingUtils.getNextScaleTime(standardDateTime, deviceVar.getSaveCycle());

			//发送到MQ、数据存盘
			DeviceVarCollectMessage message = DeviceVarCollectMessage.builder()
					.channelSn(channel.getChannelSn())
					.deviceVarSn(deviceVar.getVarSn())
					.saveTime(nextScaleTime)
					.value(value)
					.collectTime(standardDateTime)
					.build();
			IEC104Variables.publisherService.sendMessage(RabbitConstants.COLLECT_CONSUMER_KEY + storageAddress % 10, message);
		}

		//监控上线 --> MQ
		sendMonitorDeviceState(deviceSnMap,channel.getChannelSn());
	}

	/**
	 * 发送通道数据包
	 */
	private void sendChannelData(String connectKey, String value, String standardDateTime) {
		CommunicationChannel channel = StorageVariables.REGISTER_LIST.get(connectKey);
		if (channel != null) {
			// 发送通道数据
			// 保存到 REDIS 中（实时数据）
			EnumSOTVO channelVo = new EnumSOTVO(channel.getChannelSn(), value, standardDateTime);
			LOGGER.info("send websocket channelSn={} , value = {}", channel.getChannelSn(), value);
			WebSocketBaseMessage baseMessage = new WebSocketBaseMessage(WebSocketMessageEnum.DEVICE_RUNTIME_MESSAGE, channelVo);
			WebSocketPushService.pushMsgToOne(channel.getChannelSn(), JSON.toJSONString(baseMessage));
		}
	}

	/**
	 * 发送通道设备状态
	 */
	private void sendCommunicationState(CommunicationChannel channel) {
		//记录通道状态-上线-MQ
		DeviceStateMessage message = DeviceStateMessage.builder()
				.channelSn(channel.getChannelSn())
				.deviceSn(channel.getChannelSn())
				.state(1)
				.type(Constants.CHANNEL_SUFFIX)
				.changeTime(DateUtil.now())
				.build();
		IEC104Variables.publisherService.sendMessage(RabbitConstants.STATECHANGE_CONSUMER_KEY + channel.getId() % 4, message);
	}

	/**
	 * 发送通道设备状态
	 */
	private void sendCommunicationDeviceState(Long communicationDeviceId) {
		CommunicationDevice communicationDevice = StorageVariables.GLOBAL_CHANNEL_GGDZ_DEVICE_INFO_MAP.get(communicationDeviceId);
		//记录通道设备状态-上线-MQ
		DeviceStateMessage message = DeviceStateMessage.builder()
				.channelSn(communicationDevice.getChannelSn())
				.deviceSn(communicationDevice.getDeviceSn())
				.state(1)
				.type(Constants.CHANNEL_DEVICE_SUFFIX)
				.changeTime(DateUtil.now())
				.build();
		IEC104Variables.publisherService.sendMessage(RabbitConstants.STATECHANGE_CONSUMER_KEY + communicationDevice.getId() % 4, message);
	}

	/**
	 * 发送监控设备状态
	 */
	private void sendMonitorDeviceState(Map<String, Long> deviceSnMap,String channelSn) {
		for (Map.Entry<String, Long> item : deviceSnMap.entrySet()) {
			//记录监控设备状态-上线-MQ
			DeviceStateMessage message = DeviceStateMessage.builder()
					.channelSn(channelSn)
					.deviceSn(item.getKey())
					.state(1)
					.type(Constants.MONITOR_DEVICE_SUFFIX)
					.changeTime(DateUtil.now())
					.build();
			IEC104Variables.publisherService.sendMessage(RabbitConstants.STATECHANGE_CONSUMER_KEY + item.getValue() % 4, message);
		}
	}

	//region // 变量数据存储KEY
	private String getVariableCacheKey(String varSn) {
		return Constants.MONITOR_DEVICE_VAR_SN_KEY + varSn;
	}
	//endregion
}
