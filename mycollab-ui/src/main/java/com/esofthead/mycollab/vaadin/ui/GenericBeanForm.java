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
package com.esofthead.mycollab.vaadin.ui;

import com.esofthead.mycollab.core.MyCollabException;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Field;

/**
 * 
 * @author MyCollab Ltd.
 * @since 3.0
 * 
 * @param <B>
 */
public class GenericBeanForm<B> extends CssLayout {

	private static final long serialVersionUID = 1L;
	public static String SAVE_ACTION = "Save";
	public static String SAVE_AND_NEW_ACTION = "Save & New";
	public static String ASSIGN_ACTION = "Assign";
	public static String EDIT_ACTION = "Edit";
	public static String CANCEL_ACTION = "Cancel";
	public static String DELETE_ACTION = "Delete";
	public static String CLONE_ACTION = "Clone";

	protected IFormLayoutFactory layoutFactory;
	protected IBeanFieldGroupFieldFactory<B> fieldFactory;

	private boolean isValid;

	private B bean;

	public GenericBeanForm() {
		super();
	}

	public GenericBeanForm(IFormLayoutFactory layoutFactory) {
		setFormLayoutFactory(layoutFactory);
	}

	public void setFormLayoutFactory(IFormLayoutFactory layoutFactory) {
		this.layoutFactory = layoutFactory;
	}

	public void setBeanFormFieldFactory(
			IBeanFieldGroupFieldFactory<B> fieldFactory) {
		this.fieldFactory = fieldFactory;
	}

	public B getBean() {
		return bean;
	}

	public void setBean(B bean) {
		this.bean = bean;

		this.removeAllComponents();
		this.addComponent(layoutFactory.getLayout());

		if (fieldFactory == null) {
			throw new MyCollabException("Field factory must be set");
		}

		fieldFactory.setBean(bean);
	}

	public void commit() {
		fieldFactory.commit();
	}

	public void attachField(Object propertyId, Field<?> field) {
		layoutFactory.attachField(propertyId, field);
	}

	public void setValid(boolean value) {
		isValid = value;
	}

	protected boolean isValid() {
		return this.isValid;
	}
}
