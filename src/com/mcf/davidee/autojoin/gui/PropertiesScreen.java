package com.mcf.davidee.autojoin.gui;

import java.util.Arrays;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.MathHelper;

import org.lwjgl.input.Keyboard;

import com.mcf.davidee.autojoin.AutoJoin;
import com.mcf.davidee.autojoin.ServerSetting;
import com.mcf.davidee.guilib.basic.BasicScreen;
import com.mcf.davidee.guilib.basic.Label;
import com.mcf.davidee.guilib.basic.MultiTooltip;
import com.mcf.davidee.guilib.basic.Tooltip;
import com.mcf.davidee.guilib.core.Button;
import com.mcf.davidee.guilib.core.Container;
import com.mcf.davidee.guilib.core.TextField;
import com.mcf.davidee.guilib.vanilla.ButtonVanilla;
import com.mcf.davidee.guilib.vanilla.TextFieldVanilla;
import com.mcf.davidee.guilib.vanilla.TextFieldVanilla.VanillaFilter;

public class PropertiesScreen extends BasicScreen {
	
	public final String ip;
	private final ServerSetting setting;
	
	private Container container;
	private Label title, ping, error, join;
	private TextField pingDelay, errorDelay, joinOffset;
	private Button close;

	public PropertiesScreen(GuiScreen parent, String ip) {
		super(parent);
		
		this.ip = "" + ip;
		setting = AutoJoin.instance.getConfig().loadSetting(this.ip);
	}
	
	@Override
	protected void unhandledKeyTyped(char c, int code) {
		if (c == '\r')
			close();
	}
	
	@Override
	public void close() {
		setting.pingDelay = MathHelper.parseDoubleWithDefault(pingDelay.getText(), setting.pingDelay);
		setting.errorDelay = MathHelper.parseDoubleWithDefault(errorDelay.getText(), setting.errorDelay);
		setting.joinOffset = MathHelper.parseIntWithDefault(joinOffset.getText(), setting.joinOffset);
		AutoJoin.instance.getConfig().saveSetting(ip, setting);
		super.close();
	}

	@Override
	protected void revalidateGui() {
		title.setPosition(width/2, 20);
		ping.setPosition(width/2, 54);
		error.setPosition(width/2, 95);
		join.setPosition(width/2, 136);
		close.setPosition(width / 2 - 100, height / 4 + 120 + 12);
		
		pingDelay.setPosition(width/2 - 50, 67);
		errorDelay.setPosition(width/2 - 50, 107);
		joinOffset.setPosition(width/2 - 50, 147);
		container.revalidate(0, 0, width, height);
	}

	@Override
	protected void createGui() {
		title = new Label("Properties for \"" + ip + "\"");
		ping = new Label("Ping Delay (seconds)", new Tooltip("Delay between pings"));
		error = new Label("Error Delay (seconds)", new Tooltip("Delay after an error"));
		join = new Label("Join Offset", new MultiTooltip(Arrays.asList("Connect when", "#Players < #MaxPlayers + JoinOffset")));
		close = new ButtonVanilla("Done", new CloseHandler());
		
		pingDelay = new TextFieldVanilla(100, 20, new VanillaFilter());
		pingDelay.setMaxLength(4);
		pingDelay.setText("" + setting.pingDelay);
		errorDelay = new TextFieldVanilla(100, 20, new VanillaFilter());
		errorDelay.setMaxLength(4);
		errorDelay.setText("" + setting.errorDelay);
		joinOffset = new TextFieldVanilla(100, 20, new VanillaFilter());
		joinOffset.setMaxLength(4);
		joinOffset.setText("" + setting.joinOffset);
		
		container = new Container();
		container.addWidgets(title, ping, error, join, pingDelay, errorDelay, joinOffset, close);
		
		containers.add(container);
		selectedContainer = container;
	}

	@Override
	protected void reopenedGui() { }

}
