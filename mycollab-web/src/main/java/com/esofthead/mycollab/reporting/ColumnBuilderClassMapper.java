package com.esofthead.mycollab.reporting;

import java.util.HashMap;
import java.util.Map;

import net.sf.dynamicreports.report.builder.component.ComponentBuilder;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1.2
 * 
 */
public class ColumnBuilderClassMapper {
	private static Map<Class, Map<String, ComponentBuilder>> mapInjection = new HashMap<Class, Map<String, ComponentBuilder>>();

	public static void put(Class cls, Map<String, ComponentBuilder> columns) {
		mapInjection.put(cls, columns);
	}

	public static Map<String, ComponentBuilder> getListFieldBuilder(Class cls) {
		return mapInjection.get(cls);
	}
}
