package com.amituofo.common.ui.swingexts.model;

import javax.swing.SwingWorker;

import com.amituofo.common.api.Callback;
import com.amituofo.common.api.ObjectHandler;
import com.amituofo.common.type.HandleFeedback;

public abstract class ItemRecordModelDataProviderBase<ITEM> implements ItemRecordModelDataProvider<ITEM> {

	private ObjectHandler<Integer, ITEM> DEFAULT_LIST_HANDLER = new ObjectHandler<Integer, ITEM>() {

		@Override
		public void exceptionCaught(ITEM data, Throwable e) {
			e.printStackTrace();
		}

		@Override
		public HandleFeedback handle(Integer meta, ITEM obj) {
			if (obj != null) {
				recmodel.addItem(obj);
			}
			return null;
		}
	};

	protected ItemRecordModel<ITEM> recmodel;

	public ItemRecordModelDataProviderBase(ItemRecordModel<ITEM> model) {
		super();
		this.recmodel = model;
	}

	@Override
	public void destroy() {
		recmodel = null;
	}

	public ItemModelDataInjector<ITEM> buildModelHelper() {
		return new ItemModelDataInjector<ITEM>(recmodel);
	}

	public void refresh() {
		refresh(null);
	}

	public synchronized void refresh(Callback<Void> callback) {
		recmodel.removeRecords();

		SwingWorker<Void, Void> sw = new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {
				list(DEFAULT_LIST_HANDLER);
				return null;
			}

			@Override
			protected void done() {
				if (recmodel.autoFireRecordsBatchInserted == false || recmodel.autoFireRecordsInserted == false) {
					recmodel.fireRecordsRefreshed();
				}

				if (callback != null) {
					callback.callback(null);
				}
			}
		};
		sw.execute();
	}

}
