package com.amituofo.common.kit.remote;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import com.amituofo.common.kit.remote.RMIRegistry;

public class LocalRMIRegistry {
	private static RMIRegistry instance = null;

	public LocalRMIRegistry() {
	}

	public static RMIRegistry getInstance() {
		if (instance == null) {
			instance = new RMIRegistry();
		}
		return instance;
	}

	public String getHost() {
		return instance.getHost();
	}

	public RMIRegistry withHost(String host) {
		return instance.withHost(host);
	}

	public int getPort() {
		return instance.getPort();
	}

	public void getRegistry() throws RemoteException {
		instance.getRegistry();
	}

	public RMIRegistry withPort(int port) {
		return instance.withPort(port);
	}

	public void bind(String name, Remote obj) throws AccessException, RemoteException, AlreadyBoundException {
		instance.bind(name, obj);
	}

	public void rebind(String name, Remote obj) throws AccessException, RemoteException, AlreadyBoundException {
		instance.rebind(name, obj);
	}

	public void unbind(String name) throws AccessException, RemoteException, NotBoundException {
		instance.unbind(name);
	}

	public String[] list() throws AccessException, RemoteException {
		return instance.list();
	}

	public Remote lookup(String name) throws AccessException, RemoteException, NotBoundException {
		return instance.lookup(name);
	}

}
