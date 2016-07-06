/**
 * This file is part of mycollab-dao.
 *
 * mycollab-dao is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-dao is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-dao.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.db.query;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author MyCollab Ltd
 * @since 5.3.1
 */
public class CacheParamMapper {
    private static Map<String, Map<String, ValueParam>> map = new ConcurrentHashMap<>();

    public static final <P extends Param> P register(String type, Enum displayName, P param) {
        Map<String, ValueParam> valueParamMap = map.get(type);
        if (valueParamMap == null) {
            valueParamMap = new ConcurrentHashMap<>();
            map.put(type, valueParamMap);
        }
        valueParamMap.put(param.getId(), new ValueParam(displayName, param));
        return param;
    }

    public static final ValueParam getValueParam(String type, String id) {
        Map<String, ValueParam> valueParamMap = map.get(type);
        return (valueParamMap != null) ? valueParamMap.get(id) : null;
    }

    public static class ValueParam {
        private Enum displayName;
        private Param param;

        ValueParam(Enum displayName, Param param) {
            this.displayName = displayName;
            this.param = param;
        }

        public Enum getDisplayName() {
            return displayName;
        }

        public Param getParam() {
            return param;
        }
    }
}
