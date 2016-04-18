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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Type;

import com.esofthead.mycollab.core.MyCollabException;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Utility to serialize and deserialize java object to json data format and vice
 * versa.
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class JsonDeSerializer {
	private static final Gson gson;

	static {
		gson = new GsonBuilder().setExclusionStrategies(new MyExclusionStrategy()).create();
	}

	/**
	 * Convert object <code>o</code> to json format
	 * 
	 * @param o
	 * @return
	 */
	public static String toJson(Object o) {
		return gson.toJson(o);
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
		T ins = gson.fromJson(value, type);
		if (ins == null) {
			try {
				return type.newInstance();
			} catch (Exception e) {
				throw new MyCollabException(e);
			}
		}

		return ins;
	}

	/**
	 * Convert json value <code>value</code> to java object has class type
	 * <code>type</code>
	 * 
	 * @param value
	 * @param type
	 *            The specific genericized type of src. You can obtain this type
	 *            by using the {@link com.google.gson.reflect.TypeToken} class.
	 *            For example, to get the type for {@code Collection<Foo>}, you
	 *            should use:
	 * 
	 *            <pre>
	 * Type typeOfT = new TypeToken&lt;Collection&lt;Foo&gt;&gt;() {
	 * }.getType();
	 * </pre>
	 * @return
	 */
	public static <T> T fromJson(String value, Type type) {
		T ins = gson.fromJson(value, type);
		if (ins == null) {
			try {
				return null;
			} catch (Exception e) {
				throw new MyCollabException(e);
			}
		}

		return ins;
	}

	/**
	 * Annotation to exclude class parameter in serialization process
	 * 
	 * @author MyCollab Ltd.
	 * 
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.FIELD })
	public @interface Exclude {
		// Field tag only annotation
	}

	private static class MyExclusionStrategy implements ExclusionStrategy {

		public boolean shouldSkipClass(Class<?> clazz) {
			return false;
		}

		public boolean shouldSkipField(FieldAttributes f) {
			return f.getAnnotation(Exclude.class) != null;
		}
	}
}
