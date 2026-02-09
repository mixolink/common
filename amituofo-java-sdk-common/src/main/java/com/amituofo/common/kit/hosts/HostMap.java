package com.amituofo.common.kit.hosts;

public class HostMap {
	private String ip;
	private String hostname;
	
	public HostMap() {
	}

	public HostMap(String ip, String hostname) {
		super();
		this.ip = ip;
		this.hostname = hostname;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

}
