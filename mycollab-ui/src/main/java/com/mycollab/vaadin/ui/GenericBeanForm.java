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
package com.mycollab.vaadin.ui;

import com.mycollab.core.MyCollabException;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Field;

import static com.mycollab.core.utils.BeanUtility.deepClone;

/**
 * @param <B>
 * @author MyCollab Ltd.
 * @since 3.0
 */
public class GenericBeanForm<B> extends CssLayout {
    private static final long serialVersionUID = 1L;

    private IFormLayoutFactory layoutFactory;
    protected IBeanFieldGroupFieldFactory<B> fieldFactory;

    private boolean isValid;
    protected B bean;

    public GenericBeanForm() {
        super();
        this.setWidth("100%");
    }

    /**
     * Disable form validation bean. This is used to switch views of forms and keep the previous values of bean
     * without validation. You should be careful to use this method
     *
     * @param isBuffered
     */
    public void setFormBuffered(boolean isBuffered) {
        if (fieldFactory != null) {
            fieldFactory.setBuffered(isBuffered);
        }
    }

    public void setFormLayoutFactory(IFormLayoutFactory layoutFactory) {
        this.layoutFactory = layoutFactory;
    }

    public IFormLayoutFactory getLayoutFactory() {
        return layoutFactory;
    }

    public void setBeanFormFieldFactory(IBeanFieldGroupFieldFactory<B> fieldFactory) {
        this.fieldFactory = fieldFactory;
    }

    public IBeanFieldGroupFieldFactory<B> getFieldFactory() {
        return fieldFactory;
    }

    public B getBean() {
        return bean;
    }

    public void setBean(B beanVal) {
        this.bean = beanVal;

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
