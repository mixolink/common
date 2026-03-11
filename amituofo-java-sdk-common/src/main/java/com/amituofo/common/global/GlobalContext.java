package com.amituofo.common.global;

import java.util.ArrayList;
import java.util.List;

import com.amituofo.common.api.SystemExitHook;
import com.amituofo.common.bean.Session;

public class GlobalContext {
	private final static List<SystemExitHook> exitCallbacks = new ArrayList<SystemExitHook>();
	private final static Session session = new Session();

	static {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			for (SystemExitHook systemExitHook : exitCallbacks) {
				try {
					systemExitHook.exec();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}));
	}

	public GlobalContext() {
	}

	public static Session getSession() {
		return session;
	}

	public static void addSystemExitHook(SystemExitHook hook) {
		// Runtime.getRuntime().addShutdownHook(new Thread(hook));
		exitCallbacks.add(hook);
	}


}
