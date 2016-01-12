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
package com.esofthead.mycollab.module.crm.ui.components;

import com.esofthead.mycollab.module.crm.view.CrmVerticalTabsheet;
import com.esofthead.mycollab.vaadin.mvp.AbstractCssPageView;
import com.esofthead.mycollab.vaadin.ui.*;
import com.esofthead.mycollab.vaadin.web.ui.AddViewLayout2;
import com.esofthead.mycollab.vaadin.web.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.vaadin.web.ui.VerticalTabsheet;
import com.esofthead.mycollab.vaadin.web.ui.VerticalTabsheet.TabImpl;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.VerticalLayout;

/**
 * @param <B>
 * @author MyCollab Ltd.
 * @since 3.0
 */
public abstract class AbstractPreviewItemComp<B> extends AbstractCssPageView {
    private static final long serialVersionUID = 1L;

    protected B beanItem;
    protected AddViewLayout2 previewLayout;
    protected VerticalLayout previewContent;
    protected AdvancedPreviewBeanForm<B> previewForm;
    protected VerticalTabsheet previewItemContainer;

    public AbstractPreviewItemComp(Resource iconResource) {
        super();
        previewItemContainer = new CrmVerticalTabsheet(false);

        addComponent(previewItemContainer);
        previewItemContainer.setSizeFull();
        previewItemContainer.setNavigatorWidth("100%");
        previewItemContainer.setNavigatorStyleName("sidebar-menu");
        previewItemContainer.setContainerStyleName("tab-content");

        CssLayout navigatorWrapper = previewItemContainer.getNavigatorWrapper();
        navigatorWrapper.setWidth("250px");

        previewItemContainer.addSelectedTabChangeListener(new SelectedTabChangeListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void selectedTabChange(SelectedTabChangeEvent event) {
                Tab tab = ((VerticalTabsheet) event.getSource()).getSelectedTab();
                previewItemContainer.selectTab(((TabImpl) tab).getTabId());
            }
        });

        previewLayout = new AddViewLayout2("", iconResource);
        previewLayout.setStyleName("readview-layout");
        previewLayout.setMargin(new MarginInfo(false, true, true, true));

        previewContent = new VerticalLayout();
        previewContent.setWidth("100%");

        previewForm = initPreviewForm();

        ComponentContainer actionControls = createButtonControls();
        if (actionControls != null) {
            previewLayout.addHeaderRight(actionControls);
        }

        previewItemContainer.replaceContainer(previewLayout, previewLayout.getBody());

        initRelatedComponents();

        previewContent.addComponent(previewForm);
        previewContent.addComponent(createBottomPanel());
    }

    @Override
    public void attach() {
        super.attach();

        if (this.getParent() instanceof CustomLayout) {
            this.getParent().addStyleName("preview-comp");
        }
    }

    public void previewItem(final B item) {
        this.beanItem = item;
        previewLayout.setTitle(initFormTitle());
        previewLayout.initTitleStyleName();

        previewForm.setFormLayoutFactory(initFormLayoutFactory());
        previewForm.setBeanFormFieldFactory(initBeanFormFieldFactory());
        previewForm.setBean(item);

        onPreviewItem();
    }

    public B getBeanItem() {
        return beanItem;
    }

    public AdvancedPreviewBeanForm<B> getPreviewForm() {
        return previewForm;
    }

    abstract protected void onPreviewItem();

    abstract protected String initFormTitle();

    abstract protected AdvancedPreviewBeanForm<B> initPreviewForm();

    abstract protected void initRelatedComponents();

    abstract protected IFormLayoutFactory initFormLayoutFactory();

    abstract protected AbstractBeanFieldGroupViewFieldFactory<B> initBeanFormFieldFactory();

    abstract protected ComponentContainer createButtonControls();

    abstract protected ComponentContainer createBottomPanel();

}
