package com.amituofo.common.kit.remote;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.amituofo.common.util.StringUtils;
import com.amituofo.common.util.ValidUtils;

public class RMIRegistry {
	public static final int DEFAULT_REGISTRY_PORT = 1099;

	private int port = DEFAULT_REGISTRY_PORT;
	private String host = null;

	private Registry registry = null;

	public RMIRegistry() {
		super();
	}

	public RMIRegistry(int port) {
		super();
		if (port <= 0) {
			port = DEFAULT_REGISTRY_PORT;
		} else {
			this.port = port;
		}
	}

	public int getPort() {
		return port;
	}

	public RMIRegistry withPort(int port) {
		this.port = port;
		return this;
	}

	public String getHost() {
		return host;
	}

	public RMIRegistry withHost(String host) {
		this.host = host;
		return this;
	}

	public void bind(String name, Remote obj) throws AccessException, RemoteException, AlreadyBoundException {
		if (obj == null || StringUtils.isEmpty(name)) {
			throw new AccessException("name or remote object required!");
		}

		getRegistry().bind(name, obj);
	}

	public void rebind(String name, Remote obj) throws AccessException, RemoteException, AlreadyBoundException {
		if (obj == null || StringUtils.isEmpty(name)) {
			throw new AccessException("name or remote object required!");
		}

		getRegistry().rebind(name, obj);
	}

	public void unbind(String name) throws AccessException, RemoteException, NotBoundException {
		if (StringUtils.isEmpty(name)) {
			return;
		}

		getRegistry().unbind(name);
	}

	public String[] list() throws AccessException, RemoteException {
		return getRegistry().list();
	}

	public Remote lookup(String name) throws AccessException, RemoteException, NotBoundException {
		if (StringUtils.isEmpty(name)) {
			return null;
		}

		return getRegistry().lookup(name);
	}

	public synchronized Registry getRegistry() throws RemoteException {
		if (registry != null) {
			return registry;
		}

		boolean notexist = true;
		String connhost = StringUtils.isEmpty(host) ? "localhost" : host;
		try {
			ValidUtils.invalidHostConnection(connhost, port, 5000);
			notexist = false;
		} catch (Exception e) {
			notexist = true;
		}

		if (notexist) {
			return createRegistry();
		} else {
			if (StringUtils.isNotEmpty(host)) {
				registry = LocateRegistry.getRegistry(host, port);
			} else {
				registry = LocateRegistry.getRegistry(host);
			}
		}

		return registry;
	}

//	public static void main(String[] args) throws RemoteException {
//		Registry r = LocateRegistry.createRegistry(1234);
//		System.out.println(r);
//		r = LocateRegistry.getRegistry(1233);
//		System.out.println(r);
//	}

	public void unbindAll() {
		if (registry == null) {
			return;
		}

		String[] binded;
		try {
			binded = registry.list();
			if (binded != null)
				for (String name : binded) {
					try {
						registry.unbind(name);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

	private  Registry createRegistry() throws RemoteException {
		if (StringUtils.isNotEmpty(host)) {
			System.setProperty("java.rmi.server.hostname", host);
		}

		registry = LocateRegistry.createRegistry(port);
		return registry;
	}
}
