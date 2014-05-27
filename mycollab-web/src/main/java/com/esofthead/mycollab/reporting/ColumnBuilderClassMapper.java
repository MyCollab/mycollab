package com.esofthead.mycollab.reporting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1.2
 * 
 */
public class ColumnBuilderClassMapper {
	private static Map<Class, List<? extends AbstractColumnFieldComponentBuilder>> mapInjection = new HashMap<Class, List<? extends AbstractColumnFieldComponentBuilder>>();

	public static void put(Class cls,
			List<? extends AbstractColumnFieldComponentBuilder> columns) {
		mapInjection.put(cls, columns);
	}

	public static List<? extends AbstractColumnFieldComponentBuilder> getListFieldBuilder(
			Class cls) {
		return mapInjection.get(cls);
	}
}
