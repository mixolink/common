package com.amituofo.common.ui.swingexts.component;

import javax.swing.Icon;

import com.amituofo.common.ex.InvalidConfigException;
import com.amituofo.common.kit.config.Configuration;

public abstract class JEWizardPanel extends JECardPanel {
	protected final int index;
	protected final Configuration conf;
	private JEWizardPanelContainer wizardContainer;

	public JEWizardPanel() {
		this(-1, null);
//		this(-1, new Configuration());
	}

	public JEWizardPanel(Integer index, Configuration conf) {
		super();
		this.index = index;
		this.conf = conf;
	}

	public int getIndex() {
		return index;
	}

	public Configuration getConfiguration() {
		return conf;
	}

	public JEWizardPanelContainer getWizardContainer() {
		return wizardContainer;
	}

	public void setWizardContainer(JEWizardPanelContainer wizardContainer) {
		this.wizardContainer = wizardContainer;
	}

	protected abstract String getDescription();

	protected abstract String getTitle();

//	public Configuration getConfig() throws InvalidConfigException {
//		updateConfig(config);
//		return config;
//	}

	protected abstract boolean updateConfig(Configuration conf) throws InvalidConfigException;

	public boolean finish(Configuration conf) throws InvalidConfigException {
		return false;
	}

	public void setPreviewConfig(Configuration conf) {
		this.conf.resetTo(conf);
	}

	public String getID() {
		return "ID" + this.hashCode();
	}

	protected abstract Icon getDescriptionIcon();

	protected abstract String getGroupName();

//	protected abstract void validateConfig() throws InvalidConfigException;
}
