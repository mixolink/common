package com.amituofo.common.bean;

import java.util.Objects;

public class RemoteClient<AUTH> {
	private String protocol;
	private String host;
	private int port;
	private String authVersion;
	private AUTH authorization;

	public RemoteClient() {
	}

	public RemoteClient(String host, int port) {
		super();
		this.host = host;
		this.port = port;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public AUTH getAuthorization() {
		return authorization;
	}

	public void setAuthorization(AUTH authorization) {
		this.authorization = authorization;
	}

	public String getAuthVersion() {
		return authVersion;
	}

	public void setAuthVersion(String authVersion) {
		this.authVersion = authVersion;
	}

	@Override
	public int hashCode() {
		return Objects.hash(protocol, authVersion, authorization, host, port);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RemoteClient other = (RemoteClient) obj;
		return Objects.equals(protocol, other.protocol) && Objects.equals(authVersion, other.authVersion) && Objects.equals(authorization, other.authorization) && Objects.equals(host, other.host) && port == other.port;
	}

}
