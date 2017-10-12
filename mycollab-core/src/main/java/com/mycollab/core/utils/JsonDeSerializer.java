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

import com.mycollab.core.MyCollabException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Utility to serialize and deserialize java object to json data format and vice
 * versa.
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class JsonDeSerializer {
    private static final ObjectMapper objMapper;

    static {
        objMapper = new ObjectMapper();
    }

    /**
     * Convert object <code>o</code> to json format
     *
     * @param o
     * @return
     */
    public static String toJson(Object o) {
        try {
            return objMapper.writeValueAsString(o);
        } catch (Exception e) {
            throw new MyCollabException(e);
        }
    }

    /**
     * Convert json value <code>value</code> to java object has class type
     * <code>type</code>
     *
     * @param value
     * @param type
     * @return
     */
    public static <T> T fromJson(String value, Class<T> type) {
        try {
            T ins = objMapper.readValue(value, type);
            if (ins == null) {
                try {
                    return type.newInstance();
                } catch (Exception e) {
                    throw new MyCollabException(e);
                }
            }

            return ins;
        } catch (Exception e) {
            throw new MyCollabException(e);
        }
    }

    /**
     * Convert json value <code>value</code> to java object has class type
     * <code>type</code>
     *
     * @param value
     * @param type
     * @return
     */
    public static <T> T fromJson(String value, TypeReference type) {
        try {
            T ins = objMapper.readValue(value, type);
            if (ins == null) {
                try {
                    return null;
                } catch (Exception e) {
                    throw new MyCollabException(e);
                }
            }

            return ins;
        } catch (Exception e) {
            throw new MyCollabException(e);
        }
    }
}
