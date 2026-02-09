package com.amituofo.common.kit.event;

import com.amituofo.common.api.Callback;

public class FireListeners {

	public FireListeners() {
	}

	public static <V> void fire(Listeners<Callback<V>> listeners, V data) {
		Callback<V>[] ls = listeners.listeners();
		for (Callback<V> l : ls) {
			l.callback(data);
		}
	}

}
