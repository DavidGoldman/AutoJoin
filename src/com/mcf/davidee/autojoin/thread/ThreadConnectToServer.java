package com.mcf.davidee.autojoin.thread;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.network.packet.Packet2ClientProtocol;

import com.mcf.davidee.autojoin.AutoJoin;
import com.mcf.davidee.autojoin.ServerInfo;
import com.mcf.davidee.autojoin.gui.AutoJoinScreen;

public class ThreadConnectToServer extends Thread {
	
	private final AutoJoinScreen screen;
	private final Minecraft mc;
	private final ServerInfo info;
	
	public ThreadConnectToServer(AutoJoinScreen screen, ServerInfo info) {
		this.screen = screen;
		this.mc = Minecraft.getMinecraft();
		this.info = info;
	}
	
	public void run() {
		try {
			screen.setNetClientHandler(new NetClientHandler(mc, info.ip, info.port));
			if (screen.isCancelled())
				return;
			screen.getNetClientHandler().addToSendQueue(new Packet2ClientProtocol(AutoJoin.PROTOCOL_VER,
					mc.getSession().getUsername(), info.ip, info.port));
		}
		catch(Exception e) {
			if (!screen.isCancelled())
				screen.connectError(e.getMessage());
		}
	}
}
