package com.amituofo.common.kit.config;

import java.net.Proxy.Type;

public interface RemoteConfig extends Config {

	int getConnectionTimeout();

	int getPort();

	String getHost();

	void setPort(int port);

	void setHost(String domain);

	String getUser();

	String getPassword();

	void setUser(String username);

	void setPassword(String password);

	void setConnectionTimeout(int timeout);

	void setProxy(String host, int port);

	void setProxyType(Type type);

	void setProxyAuth(String username, String password);

	Type getProxyType();

	String getProxyHost();

	int getProxyPort();

	String getProxyUsername();

	String getProxyPassword();

	Boolean isProxyAuthenticationRequired();

	void setProxyAuthenticationRequired(boolean enabled);

	ProxyStatus getProxyStatus();

	void setProxyStatus(ProxyStatus status);

}