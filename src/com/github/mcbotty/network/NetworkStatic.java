package com.github.mcbotty.network;

import java.security.MessageDigest;

public class NetworkStatic {

	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	
	public final static String MOJANG_AUTH = "https://authserver.mojang.com";
	public final static String MOJANG_AUTH_AUTHENTICATE = MOJANG_AUTH + "/authenticate";
	public final static String MOJANG_AUTH_REFRESH = MOJANG_AUTH + "/refresh";
	public final static String MOJANG_AUTH_INVALIDATE = MOJANG_AUTH + "/invalidate";

	public class PacketID {
		public final static byte HANDSHAKE = 0x00;
		public final static byte KEEPALIVE = 0x00;
		public final static byte PING = 0x01;
		public final static byte STATUS = 0x00;
		
	}

	public static String sha1Hex(String data) {
		int i = 0, ii = 0;
		String digest = "";
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
			md.reset();
			md.update(data.getBytes("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		byte[] hash = md.digest();
		boolean neg = (hash[0] & 0x80) == 0x80;
		if (neg) {
			boolean carry = true;
			for (i = hash.length - 1; i >= 0; i--) {
				hash[i] = (byte) ~hash[i];
				if (carry) {
					carry = hash[i] == 0xFF;
					hash[i]++;
				}
			}
		}

		char[] hex = new char[hash.length * 2];
		for (i = 0; i < hash.length; i++) {
			ii = hash[i] & 0xFF;
			hex[i * 2] = hexArray[ii >>> 4];
			hex[i * 2 + 1] = hexArray[ii & 0x0F];
		}

		digest = new String(hex);
		if (digest.startsWith("0")) digest = digest.replaceFirst("0", digest);

		if (neg) digest = "-" + digest;
		return digest.toLowerCase();
	}

}
