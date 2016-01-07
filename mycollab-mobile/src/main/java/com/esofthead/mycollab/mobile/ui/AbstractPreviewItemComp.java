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
package com.esofthead.mycollab.mobile.ui;

import com.esofthead.mycollab.vaadin.mvp.IPreviewView;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.vaadin.navigationbarquickmenu.NavigationBarQuickMenu;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;

/**
 * @param <B>
 * @author MyCollab Ltd.
 * @since 3.0
 */
public abstract class AbstractPreviewItemComp<B> extends AbstractMobilePageView implements IPreviewView<B> {
    private static final long serialVersionUID = 1L;

    protected B beanItem;
    protected AdvancedPreviewBeanForm<B> previewForm;
    private NavigationBarQuickMenu editBtn;
    private CssLayout content;

    public AbstractPreviewItemComp() {
        content = new CssLayout();
        previewForm = initPreviewForm();
        content.addComponent(previewForm);

        editBtn = new NavigationBarQuickMenu();
        editBtn.setButtonCaption("...");
        editBtn.setContent(createButtonControls());
        this.setRightComponent(editBtn);

        initRelatedComponents();

        ComponentContainer toolbarContent = createBottomPanel();
        if (toolbarContent != null) {
            content.addComponent(toolbarContent);
        }
        this.setContent(content);
    }

    public void previewItem(final B item) {
        this.beanItem = item;
        this.setCaption(initFormTitle());

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

    abstract protected String initFormTitle();

    abstract protected AdvancedPreviewBeanForm<B> initPreviewForm();

    abstract protected void initRelatedComponents();

    abstract protected IFormLayoutFactory initFormLayoutFactory();

    abstract protected AbstractBeanFieldGroupViewFieldFactory<B> initBeanFormFieldFactory();

    abstract protected ComponentContainer createButtonControls();

    abstract protected ComponentContainer createBottomPanel();

}
