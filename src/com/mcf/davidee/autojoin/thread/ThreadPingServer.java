package com.mcf.davidee.autojoin.thread;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet254ServerPing;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.MathHelper;

import com.mcf.davidee.autojoin.AutoJoin;
import com.mcf.davidee.autojoin.ServerInfo;
import com.mcf.davidee.autojoin.gui.AutoJoinScreen;

public class ThreadPingServer extends Thread {

	private final AutoJoinScreen screen;
	private final ServerInfo info;

	public ThreadPingServer(AutoJoinScreen screen, ServerInfo info) {
		this.screen = screen;
		this.info = info;
	}

	/**
	 * Ping the server (well attempt to)!
	 */
	public void run() {
		Socket socket = null;
		DataInputStream in = null;
		DataOutputStream out = null;
		try {
			socket = new Socket();
			socket.setSoTimeout(3000);
			socket.setTcpNoDelay(true);
			socket.setTrafficClass(18);
			socket.connect(new InetSocketAddress(info.ip, info.port), 3000);

			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			Packet254ServerPing pkt = new Packet254ServerPing(AutoJoin.PROTOCOL_VER, info.ip, info.port);
			out.writeByte(pkt.getPacketId());
			pkt.writePacketData(out);

			if (in.read() != 255) {
				screen.pingFail("Invalid response from server");
				return;
			}
			String str = Packet.readString(in, 256);
			char[] arr = str.toCharArray();

			for (int i = 0; i < arr.length; ++i)
				if (arr[i] != 167 && arr[i] != 0 && ChatAllowedCharacters.allowedCharacters.indexOf(arr[i]) < 0)
					arr[i] = 63;
			str = new String(arr);

			String[] data = null;
			int curPlayers = -1;
			int maxPlayers = -1;

			if (str.startsWith("\u00a7") && str.length() > 1) {
				data = str.substring(1).split("\u0000");
				
				if (MathHelper.parseIntWithDefault(data[0], 0) == 1) {
					curPlayers = Integer.parseInt(data[4]);
					maxPlayers = Integer.parseInt(data[5]);
					int internalVersion = MathHelper.parseIntWithDefault(data[1], AutoJoin.PROTOCOL_VER);
					String gameVersion = data[2];
					if (internalVersion == AutoJoin.PROTOCOL_VER)
						screen.pingSuccess(curPlayers, maxPlayers);
					else
						screen.versionErrror("Outdated Server (" + gameVersion + ")");
				}
				else 
					screen.versionErrror("Outdated Server (???)");
			}
			else 
				screen.versionErrror("Outdated Server (1.3)");

		}
		catch (SocketException e) {
			screen.pingFail("Socket error: " + e.getMessage());
		} catch (Exception e) {
			screen.pingFail("Ping failed: " + e.getMessage());
		} 
		finally {
			if (socket != null) {
				try { socket.close(); }
				catch(IOException e) { }
			}
		}
	}

}
