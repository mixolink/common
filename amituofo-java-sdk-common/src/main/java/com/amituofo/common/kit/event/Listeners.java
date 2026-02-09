package com.amituofo.common.kit.event;

import java.util.Arrays;

public class Listeners<T> {
	private T[] listeners;
	private int size;

	public Listeners() {
		listeners = (T[]) new Object[0];
	}

	public void add(T l) {
		if (l == null) {
			return;
		}

		T[] listeners = this.listeners;
		final int size = this.size;
		if (size == listeners.length) {
			this.listeners = listeners = Arrays.copyOf(listeners, size << 1);
		}
		listeners[size] = l;
		this.size = size + 1;
	}

	public void remove(T l) {
		if (l == null) {
			return;
		}

		final T[] listeners = this.listeners;
		int size = this.size;
		for (int i = 0; i < size; i++) {
			if (listeners[i] == l) {
				int listenersToMove = size - i - 1;
				if (listenersToMove > 0) {
					System.arraycopy(listeners, i + 1, listeners, i, listenersToMove);
				}
				listeners[--size] = null;
				this.size = size;

				return;
			}
		}
	}

	public T[] listeners() {
		return listeners;
	}

	public int count() {
		return size;
	}
}
