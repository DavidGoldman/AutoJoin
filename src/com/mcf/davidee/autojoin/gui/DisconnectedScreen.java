package com.mcf.davidee.autojoin.gui;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.IChatComponent;

import com.mcf.davidee.autojoin.AutoJoin;
import com.mcf.davidee.autojoin.ServerInfo;

public class DisconnectedScreen extends GuiScreen {

	private final ServerInfo info;

	public String errorMessage;
	public IChatComponent errorDetail;
	
	@SuppressWarnings("rawtypes")
	public List list;
	
	public final GuiScreen parent;


	@SuppressWarnings("rawtypes")
	public DisconnectedScreen(ServerInfo info, GuiDisconnected disconnected) throws RuntimeException {
		this.info = info;
		try {
			Field[] fields = GuiDisconnected.class.getDeclaredFields();
			for (Field f : fields)
				f.setAccessible(true);
			errorMessage = (String) fields[0].get(disconnected);
			errorDetail = (IChatComponent) fields[1].get(disconnected);
			list = (List)  fields[2].get(disconnected);
			parent = (GuiScreen) fields[3].get(disconnected);
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected void keyTyped(char par1, int par2) { }

	@SuppressWarnings("unchecked")
	public void initGui() {
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 132, I18n.format("gui.toMenu")));
		this.buttonList.add(new GuiButton(1, width /2 - 100, height/ 4 + 80, "Auto Join"));
		this.buttonList.add(new GuiButton(2, width / 2 - 100, height / 4 + 106, "Auto Join Properties"));

		this.list = this.fontRendererObj.listFormattedStringToWidth(errorDetail.getFormattedText(), this.width - 50);
	}

	protected void actionPerformed(GuiButton button) {
		if (button.id == 0) {
			this.mc.displayGuiScreen(parent);
			AutoJoin.instance.resetCache();
		}
		if (button.id == 1) {
			AutoJoin.instance.screen = new AutoJoinScreen(parent, info);
			mc.displayGuiScreen(AutoJoin.instance.screen);
		}
		if (button.id == 2)
			mc.displayGuiScreen(new PropertiesScreen(this, info.ip));
	}

	@SuppressWarnings("rawtypes")
	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRendererObj, this.errorMessage, this.width / 2, this.height / 2 - 50, 11184810);
		int k = this.height / 2 - 30;

		if (list != null) {
			for (Iterator iterator = list.iterator(); iterator.hasNext(); k += this.fontRendererObj.FONT_HEIGHT) {
				String s = (String)iterator.next();
				this.drawCenteredString(this.fontRendererObj, s, this.width / 2, k, 16777215);
			}
		}

		super.drawScreen(par1, par2, par3);
	}
}
