/**
 * This file is part of mycollab-caching.
 *
 * mycollab-caching is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-caching is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-caching.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.cache;

import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
public class CacheObject<K, V> {
    private Map<K, V> objects = new WeakHashMap<>();

    public V get(K key) {
        return objects.get(key);
    }

    public Set<K> keySet() {
        return objects.keySet();
    }

    public void remove(K key) {
        objects.remove(key);
    }

    public void put(K key, V value) {
        objects.put(key, value);
    }
}
