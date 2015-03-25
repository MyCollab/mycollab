/**
 * This file is part of mycollab-ui.
 *
 * mycollab-ui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-ui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-ui.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.vaadin.mvp;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.form.domain.FormCustomFieldValueWithBLOBs;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.NestedMethodProperty;

/**
 * 
 * @author MyCollab Ltd
 * @since 3.0
 *
 * @param <T>
 */
public class BeanItemCustomExt<T> extends BeanItem<T> {
	private static final long serialVersionUID = 1L;

	private static List<String> excludeFields = Arrays.asList("id", "module",
			"typeid", "SerialVersionUID", "serialVersionUID");
	private static Set<String> customFieldNames;

	static {
		customFieldNames = new HashSet<>();
		Class<FormCustomFieldValueWithBLOBs> customFormCls = FormCustomFieldValueWithBLOBs.class;
		for (Class cls = customFormCls; cls != null; cls = cls.getSuperclass()) {
			Field[] declaredFields = cls.getDeclaredFields();
			for (Field field : declaredFields) {
				if (!excludeFields.contains(field.getName())) {
					customFieldNames.add(field.getName());
				}
			}
		}
	}

	public static void init() {
	}

	public BeanItemCustomExt(T bean) {
		super(bean);

		try {
			for (String customFieldName : customFieldNames) {
				String propName = "customfield." + customFieldName;
				this.addItemProperty(propName, new NestedMethodProperty(
						PropertyUtils.getProperty(bean, "customfield"),
						customFieldName));
			}

		} catch (Exception e) {
			throw new MyCollabException(e);
		}

	}

}
