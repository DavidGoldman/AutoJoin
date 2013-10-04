package com.mcf.davidee.autojoin.gui;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

import com.mcf.davidee.autojoin.AutoJoin;
import com.mcf.davidee.autojoin.ServerInfo;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class DisconnectedScreen extends GuiScreen {

	private final ServerInfo info;

	public String errorMessage;
	public String errorDetail;
	public Object[] field_74247_c;
	public List list;
	public final GuiScreen parent;


	public DisconnectedScreen(ServerInfo info, GuiDisconnected disconnected) throws RuntimeException {
		this.info = info;
		try {
			Field[] fields = GuiDisconnected.class.getDeclaredFields();
			for (Field f : fields)
				f.setAccessible(true);
			errorMessage = (String) fields[0].get(disconnected);
			errorDetail = (String) fields[1].get(disconnected);
			field_74247_c = (Object[])  fields[2].get(disconnected);
			parent = (GuiScreen) fields[4].get(disconnected);
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected void keyTyped(char par1, int par2) { }

	public void initGui() {
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 132, I18n.getString("gui.toMenu")));
		this.buttonList.add(new GuiButton(1, width /2 - 100, height/ 4 + 80, "Auto Join"));
		this.buttonList.add(new GuiButton(2, width / 2 - 100, height / 4 + 106, "Auto Join Properties"));

		if (this.field_74247_c != null)
			this.list = this.fontRenderer.listFormattedStringToWidth(I18n.getStringParams(this.errorDetail, this.field_74247_c), this.width - 50);
		else
			this.list = this.fontRenderer.listFormattedStringToWidth(I18n.getString(this.errorDetail), this.width - 50);
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

	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, this.errorMessage, this.width / 2, this.height / 2 - 50, 11184810);
		int k = this.height / 2 - 30;

		if (list != null)
			for (Iterator iterator = list.iterator(); iterator.hasNext(); k += this.fontRenderer.FONT_HEIGHT)
			{
				String s = (String)iterator.next();
				this.drawCenteredString(this.fontRenderer, s, this.width / 2, k, 16777215);
			}

		super.drawScreen(par1, par2, par3);
	}
}
