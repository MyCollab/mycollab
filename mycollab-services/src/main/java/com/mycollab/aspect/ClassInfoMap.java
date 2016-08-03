/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.aspect;

import java.util.HashMap;
import java.util.Map;

/**
 * @author MyCollab Ltd.
 * @since 5.0.1
 */
public class ClassInfoMap {
    private static Map<Class, ClassInfo> mapWrapper = new HashMap<>();

    public static void put(Class cls, ClassInfo classInfo) {
        mapWrapper.put(cls, classInfo);
    }

    public static ClassInfo getClassInfo(Class cls) {
        return mapWrapper.get(cls);
    }

    public static String getModule(Class cls) {
        return mapWrapper.get(cls).getModule();
    }

    public static String getType(Class cls) {
        return mapWrapper.get(cls).getType();
    }
}
