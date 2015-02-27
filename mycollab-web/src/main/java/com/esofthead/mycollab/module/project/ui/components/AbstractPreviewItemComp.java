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

import com.esofthead.mycollab.vaadin.mvp.PageView;
import com.esofthead.mycollab.vaadin.ui.*;
import com.esofthead.vaadin.floatingcomponent.FloatingComponent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.3.3
 */
public abstract class AbstractPreviewItemComp<B> extends VerticalLayout
        implements PageView {
    private static final long serialVersionUID = 1L;

    protected B beanItem;
    protected AdvancedPreviewBeanForm<B> previewForm;
    protected ReadViewLayout previewLayout;

    protected ComponentContainer header;

    private MVerticalLayout sidebarContent;
    private MVerticalLayout bodyContent;

    public AbstractPreviewItemComp(String headerText, Resource iconResource) {
        this(headerText, iconResource, null);
    }

    public AbstractPreviewItemComp(ComponentContainer customHeader) {
        this.header = customHeader;
        this.addComponent(header);
        initContent();
    }

    public AbstractPreviewItemComp(String headerText, Resource iconResource, ReadViewLayout layout) {
        Label headerLbl = new Label("", ContentMode.HTML);
        headerLbl.setSizeUndefined();
        headerLbl.setStyleName("hdr-text");

        this.previewLayout = layout;

        header = new MHorizontalLayout();

        if (iconResource != null) {
            if (iconResource instanceof FontAwesome) {
                String title = ((FontAwesome) iconResource).getHtml() + " " + headerText;
                headerLbl.setValue(title);
            } else {
                Image titleIcon = new Image(null, iconResource);
                ((MHorizontalLayout) header).with(titleIcon).withAlign(titleIcon, Alignment.MIDDLE_LEFT);
                headerLbl.setValue(headerText);
            }
        } else {
            headerLbl.setValue(headerText);
        }

        ((MHorizontalLayout) header).with(headerLbl).withAlign
                (headerLbl, Alignment.MIDDLE_LEFT)
                .expand(headerLbl).withStyleName("hdr-view").withWidth("100%")
                .withSpacing(true).withMargin(true);

        this.addComponent(header);
        ComponentContainer extraComp;
        if ((extraComp = createExtraControls()) != null) {
            this.addComponent(extraComp);
        }
        initContent();
    }

    private void initContent() {
        previewForm = initPreviewForm();
        ComponentContainer actionControls = createButtonControls();
        if (actionControls != null) {
            actionControls.addStyleName("control-buttons");
            addHeaderRightContent(actionControls);
        }

        CssLayout contentWrapper = new CssLayout();
        contentWrapper.setStyleName("content-wrapper");

        if (previewLayout == null)
            previewLayout = new DefaultReadViewLayout("");

        contentWrapper.addComponent(previewLayout);

        RightSidebarLayout bodyContainer = new RightSidebarLayout();
        bodyContainer.setSizeFull();
        bodyContainer.addStyleName("readview-body-wrap");

        bodyContent = new MVerticalLayout().withSpacing(false).withMargin(false).with(previewForm);
        bodyContainer.setContent(bodyContent);

        sidebarContent = new MVerticalLayout().withWidth("250px").withSpacing(true).withStyleName("readview-sidebar");
        bodyContainer.setSidebar(sidebarContent);

        FloatingComponent floatSidebar = FloatingComponent
                .floatThis(sidebarContent);
        floatSidebar.setContainerId("main-body");

        previewLayout.addBody(bodyContainer);

        this.addComponent(contentWrapper);
    }

    abstract protected void initRelatedComponents();

    public void previewItem(final B item) {
        this.beanItem = item;
        initLayout();
        previewLayout.setTitle(initFormTitle());

        previewForm.setFormLayoutFactory(initFormLayoutFactory());
        previewForm.setBeanFormFieldFactory(initBeanFormFieldFactory());
        previewForm.setBean(item);

        onPreviewItem();
    }

    private void initLayout() {
        sidebarContent.removeAllComponents();
        initRelatedComponents();
        ComponentContainer bottomPanel = createBottomPanel();
        addBottomPanel(bottomPanel);
    }

    protected void addBottomPanel(ComponentContainer container) {
        if (container != null) {
            if (bodyContent.getComponentCount() >= 2) {
                bodyContent.replaceComponent(bodyContent
                                .getComponent(bodyContent.getComponentCount() - 1),
                        container);
            } else {
                bodyContent.addComponent(container);
            }
        }
    }

    public void addHeaderRightContent(Component c) {
        header.addComponent(c);
    }

    public void addToSideBar(Component... components) {
        for (Component component : components) {
            sidebarContent.addComponent(component);
        }
    }

    public B getBeanItem() {
        return beanItem;
    }

    public AdvancedPreviewBeanForm<B> getPreviewForm() {
        return previewForm;
    }

    protected void addLayoutStyleName(String styleName) {
        previewLayout.addTitleStyleName(styleName);
    }

    protected void removeLayoutStyleName(String styleName) {
        previewLayout.removeTitleStyleName(styleName);
    }

    @Override
    public ComponentContainer getWidget() {
        return this;
    }

    @SuppressWarnings("rawtypes")
    @Override
    final public void addViewListener(ViewListener listener) {

    }

    abstract protected void onPreviewItem();

    abstract protected String initFormTitle();

    abstract protected AdvancedPreviewBeanForm<B> initPreviewForm();

    abstract protected IFormLayoutFactory initFormLayoutFactory();

    abstract protected AbstractBeanFieldGroupViewFieldFactory<B> initBeanFormFieldFactory();

    protected ComponentContainer createExtraControls() {
        return null;
    }

    abstract protected ComponentContainer createButtonControls();

    abstract protected ComponentContainer createBottomPanel();
}
