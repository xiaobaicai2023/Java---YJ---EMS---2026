package com.yunpower.collect.storage.executor;

import com.yunpower.common.core.entity.amqp.DeviceVarSupplementMessage;

/**
 * 数据补充执行器
 * */
public class DeviceVarSupplementExecutor implements Runnable {

	private DeviceVarSupplementMessage message;

    public DeviceVarSupplementExecutor(DeviceVarSupplementMessage message) {
        this.message = message;
    }

	@Override
	public void run() {

	}
}
