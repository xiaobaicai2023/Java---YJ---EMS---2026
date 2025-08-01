import {ref, onUnmounted} from 'vue';

interface SocketOptions {
	heartbeatInterval?: number; //心跳间隔
	reconnectInterval?: number; //重连时间
	maxReconnectAttempts?: number; //最大重连次数
}

/**
 * socket 返的数据格式
 */
export interface SocketData {
	data: SocketMessage | DeviceVarAlarmMessage;
	sendData?: Function
}

export interface WebSocketBaseMessage {
	bizType:string,
	bizData: SocketMessage | DeviceVarAlarmMessage
}

/**
 * 基础格式
 */
export interface SocketMessage {
	key?: string,
	time?: string,
	value: number
}

/**
 * 报警数据格式
 */
export interface DeviceVarAlarmMessage {
	stationName: string,
	deviceName: string,
	deviceVarName: string
	categoryName: string
	content: string
	happenTime: string
	alarmId: number
	stationSn: string,
	stationType: number,
	triggerStatus: number,
	triggerStatusName: string,
	triggerLevelName: string,
	triggerLevel: number
}

class Socket {
	url: string; //websocket 服务器的 URL
	ws: WebSocket | null = null; //websocket 实例
	opts: SocketOptions; //websocket 配置项
	reconnectAttempts: number = 0; //重连次数
	listeners: { [key: string]: Function[] } = {}; //监听器
	heartbeatInterval: number | null = null; //心跳间隔

	constructor(url: string, opts: SocketOptions = {}) {
		this.url = url;
		this.opts = {
			heartbeatInterval: 30000, //心跳间隔
			reconnectInterval: 5000, // 重连时间
			maxReconnectAttempts: 50, //重新连接次数
			...opts
		};

		this.init();
	}

	// 初始化websocket连接
	init() {
		this.ws = new WebSocket(this.url);
		this.ws.onopen = this.onOpen.bind(this);
		this.ws.onmessage = this.onMessage.bind(this);
		this.ws.onerror = this.onError.bind(this);
		this.ws.onclose = this.onClose.bind(this);
		// console.log('WebSocket初始化完成')
	}

	// websocket 打开的成功回调
	onOpen(event: Event) {
		// console.log(`WebSocket连接到${this.url}已建立：`, event);
		this.reconnectAttempts = 0;
		this.startHeartbeat();
		this.emit('open', event);
	}

	// websocket 接收到消息的回调
	onMessage(event: MessageEvent) {
		// console.log(`${this.url}收到消息:`, event.data);
		if (event.data !== 'pong') {
			this.emit('message', JSON.parse(event.data));
		}
	}

	// websocket 发生错误的回调
	onError(event: Event) {
		console.error(`WebSocket错误(${this.url}):`, event);
		this.emit('error', event);
	}

	// websocket 关闭的回调
	onClose(event: CloseEvent) {
		// console.log(`WebSocket连接到${this.url}已关闭`, event);
		this.stopHeartbeat();// 停止心跳
		this.emit('close', event);// 触发关闭事件

		if (this.reconnectAttempts < this.opts.maxReconnectAttempts!) {
			setTimeout(() => {
				this.reconnectAttempts++;
				this.init();
			}, this.opts.reconnectInterval);
		}
	}

	// 启动心跳机制
	startHeartbeat() {
		if (!this.opts.heartbeatInterval) return;
		this.heartbeatInterval = window.setInterval(() => {
			if (this.ws?.readyState === WebSocket.OPEN) {
				this.ws.send('ping');
			}
		}, this.opts.heartbeatInterval);
	}

	// 停止心跳机制
	stopHeartbeat() {
		if (this.heartbeatInterval) {
			clearInterval(this.heartbeatInterval);
			this.heartbeatInterval = null;
		}
	}

	// 向服务端发送数据
	send(message: string) {
		if (this.ws?.readyState === WebSocket.OPEN) {
			// console.log('给socket发送消息============>', message)
			this.ws.send(message);
		} else {
			console.error('WebSocket is not open. Cannot send:', message);
		}
	}

	// 监听事件
	on(event: string, callback: Function) {
		if (!this.listeners[event]) {
			this.listeners[event] = [];
		}
		this.listeners[event].push(callback);
	}

	// 移除事件
	off(event: string) {
		if (this.listeners[event]) {
			delete this.listeners[event];
		}
	}

	// 触发事件
	emit(event: string, data: any) {
		this.listeners[event]?.forEach(callback => callback(data));
	}
}

export function useSocket(url: string, opts?: SocketOptions) {
	// 创建一个websocket连接
	const socket = new Socket(url, opts);

	// 移除事件监听
	onUnmounted(() => {
		socket.off('open');
		socket.off('message');
		socket.off('error');
		socket.off('close');
	});

	// 返回一个对象，包含WebSocket实例和一些方法，分别是，实例 发送 注册事件 解绑事件
	return {
		socket,
		send: socket.send.bind(socket),
		on: socket.on.bind(socket),
		off: socket.off.bind(socket)
	};
}
