package com.amituofo.common.kit.remote;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amituofo.common.kit.counter.Counter;

public class ClosableServicePoint extends UnicastRemoteObject {
	private static class ServicePoint {
		List<ClosableServicePoint> points = new ArrayList<>();
		int refersCount = 0;
	}

	private static Map<Integer, ServicePoint> refersServices = new HashMap<>();

	public static int DEFAULT_SERVICE_PORT = 1399;

	private final int port;

	public ClosableServicePoint(int port) throws RemoteException {
		super(port);
		this.port = port;

		synchronized (refersServices) {
			ServicePoint refers = refersServices.get(port);
			if (refers == null) {
				refers = new ServicePoint();
				refersServices.put(port, refers);
			}
			refers.points.add(this);
			refers.refersCount++;
		}
	}

	public void shutdown() {
		synchronized (refersServices) {
			ServicePoint refers = refersServices.get(port);
			refers.refersCount--;
			if (refers.refersCount <= 0) {
				refersServices.remove(port);

				for (ClosableServicePoint sp : refers.points) {
					try {
						unexportObject(sp, true);
						break;
					} catch (NoSuchObjectException e) {
						e.printStackTrace();
					}
				}

				refers.points.clear();
			}
		}
	}

}
