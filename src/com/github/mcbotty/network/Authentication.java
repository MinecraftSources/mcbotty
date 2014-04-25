package com.github.mcbotty.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

import com.github.mcbotty.network.packet.Authenticate.AuthenticateRequest;
import com.github.mcbotty.network.packet.Authenticate.AuthenticateResponse;
import com.github.mcbotty.network.packet.Authenticate.AuthenticateResponse.Profile;
import com.github.mcbotty.network.packet.Authenticate.InvalidatePacket;
import com.github.mcbotty.network.packet.Authenticate.RefreshPacket;
import com.google.gson.Gson;

public class Authentication {
	private static String ClientToken = "";
	private static String AccessToken = "";
	private static Profile SelectedProfile = null;
	private static Gson g = new Gson();

	public static void saveCredentials() {
		try {
			RefreshPacket rp = new RefreshPacket();
			rp.setAccessToken(AccessToken);
			rp.setClientToken(ClientToken);
			rp.setSelectedProfile(SelectedProfile);
			String json = g.toJson(rp);
			File f = new File("./credentials.json");
			DataOutputStream out = new DataOutputStream(new FileOutputStream(f));
			out.writeBytes(json);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

	}

	public static void loadCredentials() {
		try {
			File f = new File("./credentials.json");
			FileInputStream in = new FileInputStream(f);
			String json = "";
			int b;
			if (in.available() > 0) while ((b = in.read()) != -1)
				json += (char) b;
			in.close();
			RefreshPacket rp = g.fromJson(json, RefreshPacket.class);
			AccessToken = rp.getAccessToken();
			ClientToken = rp.getClientToken();
			SelectedProfile = rp.getSelectedProfile();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	public static void authenticate(String username, String password) {
		AuthenticateRequest req = new AuthenticateRequest();
		req.getAgent().setName("Minecraft");
		req.getAgent().setVersion(1);
		req.setUsername(username);
		req.setPassword(password);
		req.setClientToken(ClientToken = UUID.randomUUID().toString());
		String json = g.toJson(req);
		json = send(NetworkStatic.MOJANG_AUTH_AUTHENTICATE, json);
		AuthenticateResponse res = g.fromJson(json, AuthenticateResponse.class);
		ClientToken = res.getClientToken();
		AccessToken = res.getAccessToken();
		SelectedProfile = res.getSelectedProfile();

	}

	public static void refresh() {
		RefreshPacket rp = new RefreshPacket();
		rp.setAccessToken(AccessToken);
		rp.setClientToken(ClientToken);
		rp.setSelectedProfile(SelectedProfile);
		String json = g.toJson(rp);
		json = send(NetworkStatic.MOJANG_AUTH_REFRESH, json);
		rp = g.fromJson(json, RefreshPacket.class);
		AccessToken = rp.getAccessToken();
	}

	public static void invalidate() {
		InvalidatePacket ip = new InvalidatePacket();
		ip.setAccessToken(AccessToken);
		ip.setClientToken(ClientToken);
		String json = g.toJson(ip);
		send(NetworkStatic.MOJANG_AUTH_INVALIDATE, json);
	}

	private static String send(String mode, String json) {
		try {
			URL mojangAuth = new URL(mode);
			HttpsURLConnection con = (HttpsURLConnection) mojangAuth.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setDoInput(true);
			con.setDoOutput(true);
			DataOutputStream out = new DataOutputStream(con.getOutputStream());
			out.writeBytes(json);
			out.close();
			DataInputStream in = new DataInputStream(con.getInputStream());
			json = "";
			int b;
			if (in.available() > 0) while ((b = in.read()) != -1)
				json += (char) b;
			in.close();
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
