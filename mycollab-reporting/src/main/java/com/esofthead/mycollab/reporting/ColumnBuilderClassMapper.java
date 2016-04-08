/**
 * This file is part of mycollab-reporting.
 *
 * mycollab-reporting is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-reporting is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-reporting.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.reporting;

import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.reporting.generator.ComponentBuilderGenerator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author MyCollab Ltd.
 * @since 4.1.2
 */
public class ColumnBuilderClassMapper {
    private static Map<Class, Map<String, ComponentBuilderGenerator>> mapInjection = new ConcurrentHashMap<>();

    static {
        ReportTemplateFactory.getTemplate(SiteConfiguration.getDefaultLocale());
    }

    public static void put(Class cls, Map<String, ComponentBuilderGenerator> columns) {
        mapInjection.put(cls, columns);
    }

    public static Map<String, ComponentBuilderGenerator> getListFieldBuilder(Class cls) {
        return mapInjection.get(cls);
    }
}
