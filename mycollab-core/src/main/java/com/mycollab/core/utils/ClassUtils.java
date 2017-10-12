/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.core.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility class for processing class meta data.
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ClassUtils {
    /**
     * Check whether object <code>o</code> is one of instance in class list
     * <code>classes</code>
     *
     * @param o
     * @param classes
     * @return
     */
    public static boolean instanceOf(Object o, Class<?>... classes) {
        for (Class<?> cls : classes) {
            if (cls.isInstance(o)) {
                return true;
            }
        }
        return false;
    }

    public static Class<?> getInterfaceInstanceOf(Class cls, Class superCls) {
        Class[] interfaces = cls.getInterfaces();
        for (Class inter : interfaces) {
            if (superCls.isAssignableFrom(inter)) {
                return inter;
            }
        }

        if (cls.getSuperclass() != null) {
            return getInterfaceInstanceOf(cls.getSuperclass(), superCls);
        }
        return null;
    }


    private static Map<Class, Field[]> mapFields = new ConcurrentHashMap<>();

    /**
     * Get all fields of class <code>type</code> includes its super classes
     *
     * @param type
     * @return
     */
    public static Field[] getAllFields(Class<?> type) {
        if (mapFields.containsKey(type)) {
            return mapFields.get(type);
        } else {
            List<Field> fields = new ArrayList<>();
            populateFields(type, fields);
            Field[] arr = fields.toArray(new Field[0]);
            mapFields.put(type, arr);
            return arr;
        }
    }

    private static void populateFields(Class<?> type, List<Field> fields) {
        if (type != null) {
            Field[] declaredFields = type.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                if (!Modifier.isStatic(declaredField.getModifiers())) {
                    fields.add(declaredField);
                }
            }

            populateFields(type.getSuperclass(), fields);
        }
    }

    public static Method findAnnotatedMethod(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        for (Method method : clazz.getMethods())
            if (method.isAnnotationPresent(annotationClass))
                return method;
        return null;
    }
}
