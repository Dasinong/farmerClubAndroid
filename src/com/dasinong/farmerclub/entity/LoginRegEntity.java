package com.dasinong.farmerclub.entity;

public class LoginRegEntity extends BaseEntity {

	private User data;
	
	private String accessToken;
	
	private ClientConfig clientConfig;

	public ClientConfig getClientConfig() {
		return clientConfig;
	}

	public void setClientConfig(ClientConfig clientConfig) {
		this.clientConfig = clientConfig;
	}

	public User getData() {
		return data;
	}

	public void setData(User data) {
		this.data = data;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	public class ClientConfig{
		private boolean enableWelfare;
		private boolean isDaren;
		
		public boolean isEnableWelfare() {
			return enableWelfare;
		}
		
		public void setEnableWelfare(boolean enableWelfare) {
			this.enableWelfare = enableWelfare;
		}
		
		public boolean isDaren() {
			return isDaren;
		}
		
		public void setDaren(boolean isDaren) {
			this.isDaren = isDaren;
		}
	}
}
