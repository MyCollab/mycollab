/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.ui.components;

import com.mycollab.vaadin.events.HasEditFormHandlers;
import com.mycollab.vaadin.mvp.AbstractVerticalPageView;
import com.mycollab.vaadin.mvp.IFormAddView;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.mycollab.vaadin.ui.AdvancedEditBeanForm;
import com.mycollab.vaadin.ui.IFormLayoutFactory;
import com.mycollab.vaadin.ui.WrappedFormLayoutFactory;
import com.mycollab.vaadin.web.ui.AddViewLayout;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.ComponentContainer;

/**
 * @param <B>
 * @author MyCollab Ltd.
 * @since 3.0
 */
public abstract class AbstractEditItemComp<B> extends AbstractVerticalPageView implements IFormAddView<B> {
    private static final long serialVersionUID = 1L;

    protected B beanItem;
    protected AdvancedEditBeanForm<B> editForm;

    public AbstractEditItemComp() {
        super();
        editForm = new AdvancedEditBeanForm<>();
        addComponent(editForm);
    }

    @Override
    public void editItem(final B item) {
        beanItem = item;
        editForm.setFormLayoutFactory(new FormLayoutFactory());
        editForm.setBeanFormFieldFactory(initBeanFormFieldFactory());
        editForm.setBean(item);
    }

    @Override
    public HasEditFormHandlers<B> getEditFormHandlers() {
        return this.editForm;
    }

    private class FormLayoutFactory extends WrappedFormLayoutFactory {

        @Override
        public AbstractComponent getLayout() {
            final AddViewLayout formAddLayout = new AddViewLayout(initFormHeader(), initFormIconResource());

            final ComponentContainer buttonControls = createButtonControls();
            if (buttonControls != null) {
                formAddLayout.addHeaderRight(buttonControls);
            }

            formAddLayout.setTitle(initFormTitle());
            wrappedLayoutFactory = initFormLayoutFactory();
            formAddLayout.addBody(wrappedLayoutFactory.getLayout());

            final ComponentContainer bottomPanel = createBottomPanel();
            if (bottomPanel != null) {
                formAddLayout.addBottom(bottomPanel);
            }

            return formAddLayout;
        }
    }

    protected ComponentContainer createBottomPanel() {
        return null;
    }

    abstract protected String initFormHeader();

    abstract protected String initFormTitle();

    abstract protected FontAwesome initFormIconResource();

    abstract protected ComponentContainer createButtonControls();

    abstract protected AdvancedEditBeanForm<B> initPreviewForm();

    abstract protected IFormLayoutFactory initFormLayoutFactory();

    abstract protected AbstractBeanFieldGroupEditFieldFactory<B> initBeanFormFieldFactory();
}
