package com.mcf.davidee.autojoin;

import net.minecraftforge.common.Property;

public class ServerSetting {
	public double pingDelay;
	public double errorDelay;
	public int joinOffset;
	
	public ServerSetting(double pingDelay, double errorDelay, int joinOffset) {
		this.pingDelay = clampDouble(pingDelay, .1D, 30D);
		this.errorDelay = clampDouble(errorDelay, 0, 30D);
		this.joinOffset = joinOffset;
	}
	
	public ServerSetting(ServerSetting s) {
		this(s.pingDelay, s.errorDelay, s.joinOffset);
	}
	
	public ServerSetting(Property ping, Property error, Property offset) {
		this(ping.getDouble(1.0D), error.getDouble(4.0D), offset.getInt(0));
	}
	
	public void save(Property ping, Property error, Property offset) {
		ping.set(pingDelay);
		error.set(errorDelay);
		offset.set(joinOffset);
	}
	
	private static double clampDouble(double d, double min, double max) {
		if (d < min)
			return min;
		if (d > max)
			return max;
		return d;
	}
}
