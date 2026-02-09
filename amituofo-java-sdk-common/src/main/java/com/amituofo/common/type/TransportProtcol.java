package com.amituofo.common.type;

public enum TransportProtcol {
	// (Transmission Control Protocol)：面向连接的、可靠的流协议，提供数据包的排序、确认和重传机制；
	TCP,
	// (User Datagram Protocol)：无连接、不可靠的数据报协议，没有数据包的排序、确认和重传机制；
	UDP,
	// (Stream Control Transmission Protocol)：面向连接的、可靠的消息传输协议，支持多条流并行传输；
	SCTP,
	// (Datagram Congestion Control Protocol)：面向连接、带拥塞控制和可靠性保证的协议。
	DCCP
}
