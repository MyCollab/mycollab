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
package com.mycollab.vaadin.web.ui.chart;

/**
 * @author MyCollab Ltd
 * @since 5.2.0
 */
public class Key implements Comparable {
    private String key;
    private String displayName;

    public Key(String key, String displayName) {
        this.key = key;
        this.displayName = displayName;
    }

    public String getKey() {
        return key;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof Key)) {
            throw new IllegalArgumentException("Invalid param");
        } else {
            Key tmp = (Key) o;
            int result = key.compareTo(tmp.key);
            if (result != 0) {
                return displayName.compareTo(tmp.displayName);
            }
            return result;
        }
    }
}
