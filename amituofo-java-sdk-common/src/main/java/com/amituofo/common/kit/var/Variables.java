package com.amituofo.common.kit.var;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amituofo.common.api.StringValueReplacer;
import com.amituofo.common.ex.InvalidParameterException;
import com.amituofo.common.util.SystemUtils;
import com.amituofo.common.util.URLUtils;

public class Variables {

//	public final static String ENVVAR_TENANT_ID = "%TENANT_ID%";
	public final static String ENVVAR_JAVA_PATH = "%JAVA%";
//	public final static String ENVVAR_PATH1 = "%PATH1%";
//	public final static String ENVVAR_PATH2 = "%PATH2%";

	public final static String STANDARD = "STANDARD";
	private static Map<String, Variables> vars = new HashMap<>();

	private final List<Variable> evs = new ArrayList<>();

	public Variables() {
		super();
//		init(null);
	}

	private void init(Variables vars) {
		add(ENVVAR_JAVA_PATH, new StringValueReplacer() {

			@Override
			public String replace(String value, Object... params) {
				String javapath = URLUtils.catFilePath(System.getProperty("java.home"), "bin" + File.separator + "java");
				if (SystemUtils.isWindows()) {
					javapath += ".exe";
				}
				value = value.replace(ENVVAR_JAVA_PATH, javapath);
				return value;
			}
		});

		if (vars != null) {
			evs.addAll(vars.evs);
		}
	}

	public Variables clone() {
		Variables standard = new Variables();
		standard.init(this);
		return standard;
	}

	public static Variables get(String name) {
		Variables standard = vars.get(name);
		if (standard == null) {
			standard = new Variables();
			standard.init(standard());
			vars.put(name, standard);
		}
		return standard;
	}

	public static Variables standard() {
		Variables standard = vars.get(STANDARD);
		if (standard == null) {
			standard = new Variables();
			standard.init(null);
			vars.put(STANDARD, standard);
		}
		return standard;
	}

	public Variables add(String name, StringValueReplacer vr) {
		evs.add(new Variable(name, vr));
		return this;
	}

	public String[] replace(String[] value) throws InvalidParameterException {
		if (value != null) {
			for (int i = 0; i < value.length; i++) {
				value[i] = replace(value[i]);
			}
		}

		return value;
	}

	public String replace(String value) throws InvalidParameterException {
		if (value == null || value.length() < 4 || value.indexOf('%') == -1) {
			return value;
		}

		String newvalue = value;

		for (Variable ev : evs) {
			if (newvalue.contains(ev.getName())) {
				newvalue = ev.getValueReplacer().replace(newvalue);
			}

			if (newvalue.indexOf('%') == -1) {
				return newvalue;
			}
		}

		return newvalue;
	}

	public String replace(String value, Object... params) throws InvalidParameterException {
		if (value == null || value.length() < 4 || value.indexOf('%') == -1) {
			return value;
		}

		String newvalue = value;

		for (Variable ev : evs) {
			if (newvalue.contains(ev.getName())) {
				newvalue = ev.getValueReplacer().replace(newvalue, params);
			}

			if (newvalue.indexOf('%') == -1) {
				return newvalue;
			}
		}

		return newvalue;
	}

}
