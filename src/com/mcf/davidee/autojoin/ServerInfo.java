package com.mcf.davidee.autojoin;

import java.net.InetSocketAddress;

import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;

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
	
	public static ServerInfo from(ServerData data) {
		ServerAddress address = ServerAddress.func_78860_a(data.serverIP); /*getServerAddress*/
		return new ServerInfo(address.getIP(), address.getPort());
	}
	
}
