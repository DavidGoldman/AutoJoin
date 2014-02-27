package com.mcf.davidee.autojoin;

import java.net.InetSocketAddress;

public class ServerInfo {
	
	public final String ip;
	public final int port;
	
	public ServerInfo(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}
	
	public static ServerInfo from(InetSocketAddress address) {
		return new ServerInfo(address.getHostString(), address.getPort());
	}
	
}
