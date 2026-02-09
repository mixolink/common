package com.amituofo.common.ui.swingexts.component;

import com.amituofo.common.ui.action.TitleAction;

public class JEScrollTabPane extends JECardScrollPanel implements TitleAction {
	private JETabPanel panel;

	public JEScrollTabPane(JETabPanel panel) {
		this.panel = panel;

		getVerticalScrollBar().setUnitIncrement(16); // 每次滚动16像素，可改大
		getHorizontalScrollBar().setUnitIncrement(16);

		this.setViewportView(panel);
	}

	public String getTitle() {
		return panel.getTitle();
	}

	protected void _Deactiving_() {
		panel._Deactiving_();
	}

	protected void _Activing_() {
		panel._Activing_();
	}

	@Override
	public void refresh() {
		panel.refresh();
	}

	public JETabPanel getPanel() {
		return panel;
	}

	@Override
	public synchronized void destroy() {
		if (panel != null) {
			panel.destroy();
			panel = null;
		}
		super.destroy();
	}

}
