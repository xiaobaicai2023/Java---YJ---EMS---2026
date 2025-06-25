package com.yunpower.controller;

import com.yunpower.collect.protocols.websocket.WebSocketPushService;
import com.yunpower.collect.storage.StorageVariables;
import com.yunpower.collect.storage.domain.ShardingDay;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeController {

	@GetMapping("/")
	public String index() {
		return "欢迎使用yunpower";
	}

	@GetMapping("/sendMsg")
	public String sendMsg(@RequestParam("userId") String userId, @RequestParam("msg") String msg) {
		WebSocketPushService.pushMsgToOne(userId, msg);
		return "send success";
	}

}
