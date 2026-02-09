package com.amituofo.common.ui.swingexts.model;

public class ModelIndexData<T> {
	private int modelIndex;
	private T userData;

	public ModelIndexData(int modelIndex, T userData) {
		super();
		this.modelIndex = modelIndex;
		this.userData = userData;
	}

	public T getUserData() {
		return userData;
	}

	public int getModelIndex() {
		return modelIndex;
	}

}
