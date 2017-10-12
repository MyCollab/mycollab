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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.1.1
 */
public class SortedArrayMap<K extends Comparable, V> {
    private List<Entry<K, V>> entries = new ArrayList<>();

    public void put(K key, V value) {
        entries.add(new Entry<>(key, value));
        Collections.sort(entries);
    }

    public boolean containsKey(K key) {
        return getKeyIndex(key) > -1;
    }

    public V get(K key) {
        int index = getKeyIndex(key);
        if (index > -1) {
            Entry<K, V> entry = entries.get(index);
            return entry.value;
        }
        return null;
    }

    public int getKeyIndex(K key) {
        for (int i = 0; i < entries.size(); i++) {
            Entry<K, V> entry = entries.get(i);
            if (entry.key.equals(key)) {
                return i;
            }
        }

        return -1;
    }

    private static class Entry<K extends Comparable, V> implements Comparable {
        K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Entry)) return false;

            Entry<?, ?> entry = (Entry<?, ?>) o;
            return !(key != null ? !key.equals(entry.key) : entry.key != null);

        }

        @Override
        public int hashCode() {
            int result = key != null ? key.hashCode() : 0;
            result = 31 * result + (value != null ? value.hashCode() : 0);
            return result;
        }

        @Override
        public int compareTo(Object o) {
            if (o instanceof Entry) {
                Entry<?, ?> entry = (Entry<?, ?>) o;
                if (key == null || entry.key == null) {
                    throw new MyCollabException("Invalid comparable");
                } else {
                    return entry.key.compareTo(key);
                }
            } else {
                throw new MyCollabException("Invalid comparable");
            }
        }
    }
}
