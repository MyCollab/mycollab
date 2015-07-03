/**
 * This file is part of mycollab-core.
 *
 * mycollab-core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-core.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.core.utils;

import com.esofthead.mycollab.core.MyCollabException;
import org.apache.commons.beanutils.PropertyUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.0.9
 */
public class ArrayUtils {
    public static <T> boolean isNotEmpty(T[] array) {
        return array != null && array.length != 0;
    }

    public static List<Integer> extractIds(List items) {
        try {
            List<Integer> keys = new ArrayList<>(items.size());
            for (Object item : items) {
                Integer key = (Integer) PropertyUtils.getProperty(item, "id");
                keys.add(key);
            }
            return keys;
        } catch (Exception e) {
            throw new MyCollabException(e);
        }
    }
}
