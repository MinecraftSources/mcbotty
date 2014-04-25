package com.github.mcbotty.network.packet;

import java.util.Collection;

import com.github.mcbotty.network.packet.Authenticate.AuthenticateResponse.Profile;

public class Authenticate {

	public static class InvalidatePacket {
		private String accessToken;
		private String clientToken;

		public String getClientToken() {
			return clientToken;
		}

		public void setClientToken(String clientToken) {
			this.clientToken = clientToken;
		}

		public String getAccessToken() {
			return accessToken;
		}

		public void setAccessToken(String accessToken) {
			this.accessToken = accessToken;
		}
	}

	public static class RefreshPacket {
		private String accessToken;
		private String clientToken;
		private Profile selectedProfile;

		public Profile getSelectedProfile() {
			return selectedProfile;
		}

		public void setSelectedProfile(Profile selectedProfile) {
			this.selectedProfile = selectedProfile;
		}

		public String getClientToken() {
			return clientToken;
		}

		public void setClientToken(String clientToken) {
			this.clientToken = clientToken;
		}

		public String getAccessToken() {
			return accessToken;
		}

		public void setAccessToken(String accessToken) {
			this.accessToken = accessToken;
		}
	}

	public static class AuthenticateResponse {

		public AuthenticateResponse() {
			selectedProfile = new Profile();
		}

		public class Profile {
			private String id;
			private String name;
			private String legacy;

			public String getId() {
				return id;
			}

			public void setId(String id) {
				this.id = id;
			}

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public String getLegacy() {
				return legacy;
			}

			public void setLegacy(String legacy) {
				this.legacy = legacy;
			}
		}

		private String accessToken;
		private String clientToken;
		private Collection<Profile> availableProfiles;
		private Profile selectedProfile;

		public String getAccessToken() {
			return accessToken;
		}

		public void setAccessToken(String accessToken) {
			this.accessToken = accessToken;
		}

		public String getClientToken() {
			return clientToken;
		}

		public void setClientToken(String clientToken) {
			this.clientToken = clientToken;
		}

		public Collection<Profile> getAvailableProfiles() {
			return availableProfiles;
		}

		public void setAvailableProfiles(Collection<Profile> availableProfiles) {
			this.availableProfiles = availableProfiles;
		}

		public Profile getSelectedProfile() {
			return selectedProfile;
		}

		public void setSelectedProfile(Profile selectedProfile) {
			this.selectedProfile = selectedProfile;
		}

	}

	public static class AuthenticateRequest {
		public AuthenticateRequest() {
			agent = new Agent();
		}

		public class Agent {
			private String name;
			private int version;

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public int getVersion() {
				return version;
			}

			public void setVersion(int version) {
				this.version = version;
			}
		}

		private Agent agent;
		private String username;
		private String password;
		private String clientToken;

		public Agent getAgent() {
			return agent;
		}

		public void setAgent(Agent agent) {
			this.agent = agent;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getClientToken() {
			return clientToken;
		}

		public void setClientToken(String clientToken) {
			this.clientToken = clientToken;
		}
	}

}
