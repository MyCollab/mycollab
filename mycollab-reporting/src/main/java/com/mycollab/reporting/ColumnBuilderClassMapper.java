package com.mycollab.reporting;

import com.mycollab.reporting.generator.ComponentBuilderGenerator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author MyCollab Ltd.
 * @since 4.1.2
 */
public class ColumnBuilderClassMapper {
    private static Map<Class, Map<String, ComponentBuilderGenerator>> mapInjection = new ConcurrentHashMap<>();

    public static void put(Class cls, Map<String, ComponentBuilderGenerator> columns) {
        mapInjection.put(cls, columns);
    }

    public static Map<String, ComponentBuilderGenerator> getListFieldBuilder(Class cls) {
        return mapInjection.get(cls);
    }
}
