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
package com.esofthead.mycollab.module.project.ui.components;

import com.esofthead.mycollab.vaadin.events.HasEditFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.IFormAddView;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.esofthead.mycollab.vaadin.ui.AdvancedEditBeanForm;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.WrappedFormLayoutFactory;
import com.esofthead.mycollab.vaadin.web.ui.AddViewLayout;
import com.vaadin.server.Resource;
import com.vaadin.ui.ComponentContainer;

/**
 * @param <B>
 * @author MyCollab Ltd.
 * @since 3.0
 */
public abstract class AbstractEditItemComp<B> extends AbstractPageView implements IFormAddView<B> {
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
        private static final long serialVersionUID = 1L;

        @Override
        public ComponentContainer getLayout() {
            final AddViewLayout formAddLayout = new AddViewLayout(initFormHeader(), initFormIconResource());

            final ComponentContainer topLayout = createButtonControls();
            if (topLayout != null) {
                formAddLayout.addHeaderRight(topLayout);
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

    abstract protected Resource initFormIconResource();

    abstract protected ComponentContainer createButtonControls();

    abstract protected AdvancedEditBeanForm<B> initPreviewForm();

    abstract protected IFormLayoutFactory initFormLayoutFactory();

    abstract protected AbstractBeanFieldGroupEditFieldFactory<B> initBeanFormFieldFactory();
}
