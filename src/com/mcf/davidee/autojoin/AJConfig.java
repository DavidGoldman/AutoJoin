package com.mcf.davidee.autojoin;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

public class AJConfig {
	
	private final Configuration config;
	private final ServerSetting _default;
	
	public AJConfig(Configuration config) {
		this.config = config;
		config.load();
		this._default = loadDefault();
		config.save();
	}

	private ServerSetting loadDefault() {
		Property ping = config.get("default", "pingDelay", 1.0D, "Default delay between pings (seconds)");
		Property error = config.get("default", "errorDelay", 4.0D, "Default delay after an error (seconds)");
		Property offset = config.get("default", "joinOffset", 0, "Default offset, connect when #Players < #MaxPlayers + joinOffset");
		ServerSetting s = new ServerSetting(ping, error, offset);
		s.save(ping, error, offset);
		return s;
	}
	
	public ServerSetting loadSetting(String ip) {
		ip = ip.replace('.', '_');
		Property ping = config.get(ip, "pingDelay", 1.0D);
		Property error = config.get(ip, "errorDelay", 4.0D);
		Property offset = config.get(ip, "joinOffset", 0);
		ServerSetting s = new ServerSetting(ping, error, offset);
		s.save(ping, error, offset);
		config.save();
		return s;
	}
	
	public void saveSetting(String ip, ServerSetting s) {
		ip = ip.replace('.', '_');
		Property ping = config.get(ip, "pingDelay", 1.0D);
		Property error = config.get(ip, "errorDelay", 4.0D);
		Property offset = config.get(ip, "joinOffset", 0);
		s.save(ping, error, offset);
		config.save();
	}
}
