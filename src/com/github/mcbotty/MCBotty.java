package com.github.mcbotty;

import com.github.mcbotty.network.Authentication;
import com.github.mcbotty.network.Client;

public class MCBotty {
	
	public MCBotty() {
		Authentication.loadCredentials();
		Authentication.refresh();
		/*Client c = new Client("localhost", 25565);
		c.getServerStatus();
		while(true) {
			c.pingServer();
			System.out.println("Server Ping: " + c.Ping);
			try {Thread.sleep(500);}catch(Exception e) {}
		}//*/
	}

	public static void main(String[] args) {
		new MCBotty();
	}

}
