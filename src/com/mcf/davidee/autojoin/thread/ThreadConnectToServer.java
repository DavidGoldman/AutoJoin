package com.mcf.davidee.autojoin.thread;

import java.net.InetAddress;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.C00PacketLoginStart;

import com.mcf.davidee.autojoin.AJLoginHandler;
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
			if (screen.isCancelled())
				return;
			NetworkManager manager = NetworkManager.provideLanClient(InetAddress.getByName(info.ip), info.port);
			//TODO change this back to the AJ screen?
			manager.setNetHandler(new AJLoginHandler(manager, mc, new GuiMainMenu()));
			manager.scheduleOutboundPacket(new C00Handshake(AutoJoin.PROTOCOL_VER, info.ip, info.port, EnumConnectionState.LOGIN));
            manager.scheduleOutboundPacket(new C00PacketLoginStart(mc.getSession().func_148256_e()));
            screen.setManager(manager);
		}
		catch(Exception e) {
			if (!screen.isCancelled())
				screen.connectError(e.getMessage());
		}
	}
}
