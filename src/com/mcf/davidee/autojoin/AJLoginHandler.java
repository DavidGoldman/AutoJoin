package com.mcf.davidee.autojoin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.network.NetworkManager;
import net.minecraft.util.IChatComponent;

import com.mcf.davidee.autojoin.gui.AutoJoinScreen;

public class AJLoginHandler extends NetHandlerLoginClient {

	public AJLoginHandler(NetworkManager manager, Minecraft mc, GuiScreen screen) {
		super(manager, mc, screen);
	}
	
	public void onDisconnect(IChatComponent message) {
		Minecraft mc = Minecraft.getMinecraft();
		
		if (mc.currentScreen instanceof AutoJoinScreen)
			((AutoJoinScreen)mc.currentScreen).connectError(message.getFormattedText());
		else
			super.onDisconnect(message);
    }

}
