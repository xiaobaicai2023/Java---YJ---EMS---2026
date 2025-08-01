package com.yunpower.collect.storage.listener;

import cn.hutool.core.date.DateUtil;
import com.yunpower.collect.storage.service.StorageCommonService;
import com.yunpower.common.core.constant.Constants;
import com.yunpower.common.core.entity.amqp.DeviceStateMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class RedisMessageListener extends KeyExpirationEventMessageListener {

	@Value("${spring.redis.database:*}")
	private String redisDatabase;

	/**
	 * Creates new {@link MessageListener} for {@code __keyevent@*__:expired} messages.
	 *
	 * @param listenerContainer must not be {@literal null}.
	 */
	public RedisMessageListener(RedisMessageListenerContainer listenerContainer) {
		super(listenerContainer);
	}


	/**
	 * 监听指定数据库的 key (默认监听所有key)
	 * */
	@Override
	protected void doRegister(RedisMessageListenerContainer listenerContainer) {
		Topic topic = new PatternTopic("__keyevent@" + this.redisDatabase + "__:expired");
		listenerContainer.addMessageListener(this, topic);
	}

	/**
	 * 针对 redis 数据失效事件，进行数据处理
	 */
	@Override
	public void onMessage(Message message, byte[] pattern) {
		String expiredKey = new String(message.getBody(), StandardCharsets.UTF_8);
		log.debug("redis key {} 过期了", expiredKey);

		//监控设备状态key过期
		if (expiredKey.contains(Constants.DEVICE_STATE_KEY)) {
			String[] keyArr = expiredKey.split(":");
			String type =keyArr[1];
			String deviceSn = keyArr[2];
            log.debug("设备 {} 状态过期了", deviceSn);
			DeviceStateMessage  stateMessage =DeviceStateMessage.
					builder()
					.deviceSn(deviceSn)
					.type(type)
					.state(0)
					.changeTime(DateUtil.now())
					.build();
			StorageCommonService.changeState(stateMessage, false);
		}
	}
}
