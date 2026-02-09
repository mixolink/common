package com.amituofo.common.ui.swingexts.component;

import java.awt.CardLayout;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import com.amituofo.common.ui.lang.CardComponent;

public class JETabbedCardContainerPanel<D> extends JEPanel {
	private CardComponent<JECloseableTabbedPanel, D> topDataComponent;
	private final Map<String, CardComponent<JECloseableTabbedPanel, D>> components = new HashMap<String, CardComponent<JECloseableTabbedPanel, D>>();

	public JETabbedCardContainerPanel() {
		setLayout(new CardLayout(0, 0));

		// 占位panel，解决空白card，add第一个后无法自动显示的bug
		JPanel panel = new JPanel();
		add(panel, "name_786413097376940");
	}

	public void addComponent(String name, JECloseableTabbedPanel comp, D data) {
		CardComponent<JECloseableTabbedPanel, D> cd = new CardComponent<JECloseableTabbedPanel, D>(name, comp, data);
		components.put(name, cd);
		super.add(name, comp);
		// switchTo(name);
	}

	public CardComponent<JECloseableTabbedPanel, D> switchTo(String name) {
		if (components.containsKey(name)) {
			CardComponent<JECloseableTabbedPanel, D> dataComponent = components.get(name);

			if (dataComponent == topDataComponent) {
				return topDataComponent;
			}

//			try {
//				topDataComponent.getComponent().deactiving();
//			} finally {
				((CardLayout) this.getLayout()).show(this, name);
				topDataComponent = dataComponent;

//				topDataComponent.getComponent().activing();
//			}

			return topDataComponent;
		}

		return null;
	}

	public Collection<CardComponent<JECloseableTabbedPanel, D>> getDataComponents() {
		return components.values();
	}

	public boolean hasComponent(String name) {
		return components.containsKey(name);
	}

	public CardComponent<JECloseableTabbedPanel, D> getTopComponent() {
		return topDataComponent;
	}

	public void remove(String name) {
		CardComponent<JECloseableTabbedPanel, D> cd = components.remove(name);
		if (cd != null) {
			super.remove(cd.getComponent());
		}
	}
}
