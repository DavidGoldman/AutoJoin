package com.mcf.davidee.autojoin.forge;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiMainMenu;

import com.mcf.davidee.autojoin.AutoJoin;
import com.mcf.davidee.autojoin.ServerInfo;
import com.mcf.davidee.autojoin.gui.DisconnectedScreen;

import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.TickType;

public class GuiListener implements IScheduledTickHandler {

	private Minecraft mc = Minecraft.getMinecraft();

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) { }

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		if (mc.currentScreen instanceof GuiDisconnected) {
			ServerInfo info = AutoJoin.instance.lastServer;
			DisconnectedScreen dc = new DisconnectedScreen(info, (GuiDisconnected)mc.currentScreen);
			
			if (AutoJoin.instance.screen != null) {
				AutoJoin.instance.screen.connectError(dc.errorMessage);
				mc.displayGuiScreen(AutoJoin.instance.screen);
			}
			else if (info != null)
				mc.displayGuiScreen(dc);
		}
		if (mc.currentScreen instanceof GuiMainMenu) {
			AutoJoin.instance.resetCache();
		}
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}

	@Override
	public String getLabel() {
		return "autojoin.tick";
	}

	@Override
	public int nextTickSpacing() {
		return 1;
	}

}
