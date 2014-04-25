package com.github.mcbotty.network;

import java.io.*;
import java.net.*;

import com.github.mcbotty.network.NetworkStatic.PacketID;
import com.github.mcbotty.network.packet.Status.StatusResponse;
import com.google.gson.Gson;

public class Client {
	Thread clientListenerThread;
	InetSocketAddress host;
	MCDataInputStream in;
	MCDataOutputStream out;
	Socket s;

	public long Ping = 0;

	public Client() {
		clientListenerThread = new Thread(null, clientListenerRunnable, "ClientListener");
	}
	
	public Client(InetSocketAddress host) {
		this.host = host;
		clientListenerThread = new Thread(null, clientListenerRunnable, "ClientListener");
	}
	
	public Client(String hostname, int port) {
		host = new InetSocketAddress(hostname, port);
		clientListenerThread = new Thread(null, clientListenerRunnable, "ClientListener");
	}
	
	void serverStartConnection() throws Exception {
		s = new Socket();
		s.setSoTimeout(10000);
		s.connect(host, 10000);
		in = new MCDataInputStream(s.getInputStream());
		out = new MCDataOutputStream(s.getOutputStream());
		
		// Handshake
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		MCDataOutputStream hs = new MCDataOutputStream(b);
		hs.write(PacketID.HANDSHAKE); // Handshake PacketID 0x00
		hs.writeVarInt(5); // Protocol Version
		hs.writeVarInt(host.getHostString().length());
		hs.writeBytes(host.getHostString());
		hs.writeShort(host.getPort());
		hs.writeVarInt(1); // State
		hs.close();

		out.writeVarInt(b.size());
		out.write(b.toByteArray());
	}

	public void pingServer() {
		try {
			serverStartConnection();
			
			long now = System.currentTimeMillis();
			out.writeByte(0x09); // Packet Size = 9
			out.writeByte(PacketID.PING); // Ping PacketID 0x01
			out.writeLong(now);
			
			in.readVarInt(); // Discard Packet Size
			in.readVarInt(); // Discard Packet ID
			in.readLong();
			Ping = System.currentTimeMillis() - now;
			
			
			out.close();
			in.close();
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	public void getServerStatus() {
		s = new Socket();
		try {
			serverStartConnection();

			out.writeByte(0x01); // Packet Size
			out.writeByte(PacketID.STATUS); // Status PacketID 0x00

			in.readVarInt(); // Discard Packet Size
			int id = in.readVarInt();

			if (id == -1 || id != 0x00) throw new Exception("Error (Client.java:46): ID = " + id);

			int length = in.readVarInt();

			byte[] inBuf = new byte[length];
			in.readFully(inBuf);
			String json = new String(inBuf);
			Gson g = new Gson();
			System.out.println(json);
			StatusResponse sr = g.fromJson(json, StatusResponse.class);
			System.out.println(sr);

			out.close();
			in.close();
			s.close();

		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	public InetSocketAddress getHost() {
		return this.host;
	}

	public void setHost(InetSocketAddress host) {
		this.host = host;
	}

	public void setHost(String hostname, int port) {
		this.host = new InetSocketAddress(hostname, port);
	}

	void clientListenerLoop() throws Exception {
		
	}

	Runnable clientListenerRunnable = new Runnable() {

		@Override
		public void run() {
			while(true) {
				try {
					clientListenerLoop();
					Thread.sleep(10);
				} catch (Exception e) {}
			}
		}

	};

}
