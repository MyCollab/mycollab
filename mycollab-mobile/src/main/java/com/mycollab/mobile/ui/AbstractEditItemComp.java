/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.mobile.ui;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.events.HasEditFormHandlers;
import com.mycollab.vaadin.mvp.IFormAddView;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.mycollab.vaadin.ui.AdvancedEditBeanForm;
import com.mycollab.vaadin.ui.IFormLayoutFactory;
import com.mycollab.vaadin.ui.WrappedFormLayoutFactory;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;

/**
 * @param <B>
 * @author MyCollab Ltd.
 * @since 3.0
 */
public abstract class AbstractEditItemComp<B> extends AbstractMobilePageView implements IFormAddView<B> {
    private static final long serialVersionUID = 1L;

    protected B beanItem;
    protected AdvancedEditBeanForm<B> editForm;

    public AbstractEditItemComp() {
        super();
        editForm = new AdvancedEditBeanForm<>();
        this.setContent(editForm);

        Button saveBtn = new Button(UserUIContext.getMessage(GenericI18Enum.BUTTON_SAVE), clickEvent -> {
            if (editForm.validateForm())
                editForm.fireSaveForm();
        });
        this.setRightComponent(saveBtn);
    }

    @Override
    public void editItem(final B item) {
        this.beanItem = item;
        editForm.setFormLayoutFactory(new FormLayoutFactory());
        editForm.setBeanFormFieldFactory(initBeanFormFieldFactory());
        editForm.setBean(item);

        this.setCaption(initFormTitle());
    }

    @Override
    public HasEditFormHandlers<B> getEditFormHandlers() {
        return editForm;
    }

    class FormLayoutFactory extends WrappedFormLayoutFactory {
        private static final long serialVersionUID = 1L;

        @Override
        public AbstractComponent getLayout() {
            VerticalLayout formAddLayout = new VerticalLayout();
            wrappedLayoutFactory = initFormLayoutFactory();
            formAddLayout.addComponent(wrappedLayoutFactory.getLayout());
            return formAddLayout;
        }
    }

    abstract protected String initFormTitle();

    abstract protected IFormLayoutFactory initFormLayoutFactory();

    abstract protected AbstractBeanFieldGroupEditFieldFactory<B> initBeanFormFieldFactory();
}
