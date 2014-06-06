/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
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

	static {
		ReportTemplateFactory.getTemplate();
	}

	public static void put(Class cls, Map<String, ComponentBuilder> columns) {
		mapInjection.put(cls, columns);
	}

	public static Map<String, ComponentBuilder> getListFieldBuilder(Class cls) {
		return mapInjection.get(cls);
	}
}
