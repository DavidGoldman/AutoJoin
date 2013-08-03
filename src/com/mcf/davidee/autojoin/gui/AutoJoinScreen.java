package com.mcf.davidee.autojoin.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.client.resources.I18n;

import com.mcf.davidee.autojoin.AutoJoin;
import com.mcf.davidee.autojoin.ServerInfo;
import com.mcf.davidee.autojoin.ServerSetting;
import com.mcf.davidee.autojoin.thread.ThreadConnectToServer;
import com.mcf.davidee.autojoin.thread.ThreadPingServer;
import com.mcf.davidee.guilib.basic.BasicScreen;
import com.mcf.davidee.guilib.basic.Label;
import com.mcf.davidee.guilib.core.Button;
import com.mcf.davidee.guilib.core.Container;
import com.mcf.davidee.guilib.vanilla.ButtonVanilla;

public class AutoJoinScreen extends BasicScreen {

	public enum State {
		PING_WAIT,
		PINGING,
		CONNECTING,
		WAITING,
		VER_ERR;
	}

	private final ServerInfo server;
	private final ServerSetting setting;

	private Container container;
	private Label title, status, info;
	private Button close;

	//Synchronize? Don't think it's needed...
	private NetClientHandler handler;
	private State state;

	private long startTime;
	private long curTime;

	private int connectCounter;
	private int pingCounter;
	private boolean cancelled;

	public AutoJoinScreen(GuiScreen parent, ServerInfo server) {
		super(parent);

		this.server = server;
		this.setting = AutoJoin.instance.getConfig().loadSetting(server.ip);
		setState(State.PING_WAIT);
	}

	public void setState(State state) {
		this.state = state;
		this.startTime = System.currentTimeMillis();

		switch(state) {
		case PING_WAIT:
			break;
		case PINGING:
			status.setText("Ping #" + (++pingCounter));
			info.setText("");
			new ThreadPingServer(this, server).start();
			break;
		case CONNECTING:
			status.setText("Connect #" + (++connectCounter));
			new ThreadConnectToServer(this, server).start();
			break;
		case WAITING:
			status.setText("Error - Waiting");
			break;
		case VER_ERR:
			status.setText("Unable to connect!");
			break;
		}
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		if (handler != null)
			handler.processReadPackets();
		curTime = System.currentTimeMillis();
		switch(state) {
		case PING_WAIT:
			if ((curTime - startTime) / 1000.0D >= setting.pingDelay)
				setState(State.PINGING);
			break;
		case WAITING:
			if ((curTime - startTime) / 1000.0D >= setting.errorDelay)
				setState(State.PING_WAIT); //Possibly change to PINGING? Why wait more?
			break;
		default:
			break;
		}
	}

	@Override
	public void close() {
		this.cancelled = true;
		if (handler != null)
			handler.disconnect();
		AutoJoin.instance.resetCache();
		super.close();
	}

	@Override
	protected void revalidateGui() {
		title.setPosition(width/2, height/2 - 50);
		status.setPosition(width/2, height/2 - 10);
		info.setPosition(width/2, height/2 + 10);
		close.setPosition(width/2 - 100, height/4 + 132);
		
		container.revalidate(0, 0, width, height);
	}

	public void setNetClientHandler(NetClientHandler handler) {
		this.handler = handler;
	}
	
	public NetClientHandler getNetClientHandler() {
		return handler;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	protected void createGui() {
		title = new Label("Auto-Joining Server \"" + server.ip + "\"");
		status = new Label("");
		info = new Label("");
		close = new ButtonVanilla(I18n.func_135053_a("gui.done"), new CloseHandler());

		container = new Container();
		container.addWidgets(title, status, info, close);
		containers.add(container);
		selectedContainer = container;
	}

	@Override
	protected void reopenedGui() { }

	public void pingSuccess(int cur, int max) {
		info.setText("Ping Result : " + cur + "/" + max);
		if (cur < max + setting.joinOffset) 
			setState(State.CONNECTING);
		else
			setState(State.PING_WAIT);
	}

	public void pingFail(String message) {
		setState(State.WAITING);
		info.setText(message);
	}
	
	public void connectError(String message) {
		setState(State.WAITING);
		info.setText(message);
	}

	public void versionErrror(String message) {
		setState(State.VER_ERR);
		info.setText(message);
	}


}
