package com.amituofo.common.kit.config;

import java.io.File;

public interface ConfigLoadEvent<CONFIG> {

	boolean validate(CONFIG setting);
	
	void failedLoading(File file, Throwable e);

}
