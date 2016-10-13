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

import com.mycollab.vaadin.mvp.IPreviewView;
import com.mycollab.vaadin.touchkit.NavigationBarQuickMenu;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.IFormLayoutFactory;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @param <B>
 * @author MyCollab Ltd.
 * @since 3.0
 */
public abstract class AbstractPreviewItemComp<B> extends AbstractMobilePageView implements IPreviewView<B> {
    private static final long serialVersionUID = 1L;

    protected B beanItem;
    protected AdvancedPreviewBeanForm<B> previewForm;

    public AbstractPreviewItemComp() {
        previewForm = initPreviewForm();
    }

    public void previewItem(final B item) {
        this.beanItem = item;

        CssLayout content = new CssLayout();
        content.addComponent(new MHorizontalLayout(ELabel.h2(initFormHeader())).withMargin(true).withStyleName("border-bottom").withFullWidth());
        content.addComponent(previewForm);

        ComponentContainer buttonControls = createButtonControls();
        if (buttonControls instanceof VerticalLayout) {
            NavigationBarQuickMenu editBtn = new NavigationBarQuickMenu();
            editBtn.setContent(buttonControls);
            this.setRightComponent(editBtn);
        } else {
            this.setRightComponent(buttonControls);
        }

        initRelatedComponents();

        ComponentContainer toolbarContent = createBottomPanel();
        if (toolbarContent != null) {
            content.addComponent(toolbarContent);
        }
        this.setContent(content);

        previewForm.setFormLayoutFactory(initFormLayoutFactory());
        previewForm.setBeanFormFieldFactory(initBeanFormFieldFactory());
    }

    public B getItem() {
        return beanItem;
    }

    public AdvancedPreviewBeanForm<B> getPreviewForm() {
        return previewForm;
    }

    @Override
    protected void onBecomingVisible() {
        super.onBecomingVisible();
        if (beanItem != null)
            previewForm.setBean(beanItem);
        afterPreviewItem();
    }

    abstract protected void afterPreviewItem();

    abstract protected String initFormHeader();

    abstract protected AdvancedPreviewBeanForm<B> initPreviewForm();

    protected void initRelatedComponents() {
    }

    abstract protected IFormLayoutFactory initFormLayoutFactory();

    abstract protected AbstractBeanFieldGroupViewFieldFactory<B> initBeanFormFieldFactory();

    abstract protected ComponentContainer createButtonControls();

    abstract protected ComponentContainer createBottomPanel();

}
