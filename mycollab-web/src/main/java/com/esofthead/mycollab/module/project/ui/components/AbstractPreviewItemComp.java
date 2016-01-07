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

import com.esofthead.mycollab.common.domain.FavoriteItem;
import com.esofthead.mycollab.common.service.FavoriteItemService;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.PageView;
import com.esofthead.mycollab.vaadin.ui.*;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.*;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.3.3
 */
public abstract class AbstractPreviewItemComp<B> extends VerticalLayout implements PageView {
    private static final long serialVersionUID = 1L;
    private static Logger LOG = LoggerFactory.getLogger(AbstractPreviewItemComp.class);

    protected B beanItem;
    private FavoriteItemService favoriteItemService = ApplicationContextUtil.getSpringBean(FavoriteItemService.class);
    private boolean isDisplaySideBar = true;

    protected AdvancedPreviewBeanForm<B> previewForm;
    protected ReadViewLayout previewLayout;
    protected ComponentContainer header;
    private MVerticalLayout sidebarContent;
    private MVerticalLayout bodyContent;
    private Button favoriteBtn;

    public AbstractPreviewItemComp(String headerText, Resource iconResource) {
        this(headerText, iconResource, null);
    }

    public AbstractPreviewItemComp(ComponentContainer customHeader, ReadViewLayout layout) {
        this.header = customHeader;
        this.addComponent(header);
        isDisplaySideBar = false;
        this.previewLayout = layout;
        initContent();
    }

    public AbstractPreviewItemComp(String headerText, Resource iconResource, ReadViewLayout layout) {
        ELabel headerLbl = ELabel.h3("");
        headerLbl.setSizeUndefined();

        this.previewLayout = layout;

        header = new MHorizontalLayout().withSpacing(false).withStyleName("hdr-view").withWidth("100%").withMargin(true);
        ((MHorizontalLayout) header).setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

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

        favoriteBtn = new Button(FontAwesome.STAR);
        favoriteBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                toggleFavorite();
            }
        });

        Label spaceLbl = new Label();

        ((MHorizontalLayout) header).with(headerLbl, favoriteBtn, spaceLbl).expand(spaceLbl);

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

        MCssLayout contentWrapper = new MCssLayout().withFullWidth().withStyleName(UIConstants.CONTENT_WRAPPER);

        if (previewLayout == null)
            previewLayout = new DefaultReadViewLayout("");

        contentWrapper.addComponent(previewLayout);

        if (isDisplaySideBar) {
            RightSidebarLayout bodyContainer = new RightSidebarLayout();
            bodyContainer.setSizeFull();
            bodyContainer.addStyleName("readview-body-wrap");

            bodyContent = new MVerticalLayout().withSpacing(false).withMargin(false).withFullWidth().with(previewForm);
            bodyContainer.setContent(bodyContent);
            sidebarContent = new MVerticalLayout().withWidth("250px").withStyleName("readview-sidebar");
            bodyContainer.setSidebar(sidebarContent);

            previewLayout.addBody(bodyContainer);
        } else {
            CssLayout bodyContainer = new CssLayout();
            bodyContainer.setSizeFull();
            bodyContainer.addStyleName("readview-body-wrap");
            bodyContent = new MVerticalLayout().withSpacing(false).withFullWidth().withMargin(false).with(previewForm);
            bodyContainer.addComponent(bodyContent);
            previewLayout.addBody(bodyContainer);
        }

        this.addComponent(contentWrapper);
    }

    abstract protected void initRelatedComponents();

    abstract protected String getType();

    protected void toggleFavorite() {
        try {
            if (isFavorite()) {
                favoriteBtn.removeStyleName("favorite-btn-selected");
                favoriteBtn.addStyleName("favorite-btn");
            } else {
                favoriteBtn.addStyleName("favorite-btn-selected");
                favoriteBtn.removeStyleName("favorite-btn");
            }
            FavoriteItem favoriteItem = new FavoriteItem();
            favoriteItem.setExtratypeid(CurrentProjectVariables.getProjectId());
            favoriteItem.setType(getType());
            favoriteItem.setTypeid(PropertyUtils.getProperty(beanItem, "id").toString());
            favoriteItem.setSaccountid(AppContext.getAccountId());
            favoriteItem.setCreateduser(AppContext.getUsername());
            FavoriteItemService favoriteItemService = ApplicationContextUtil.getSpringBean(FavoriteItemService.class);
            favoriteItemService.saveOrDelete(favoriteItem);
        } catch (Exception e) {
            LOG.error("Error while set favorite flag to bean", e);
        }
    }

    private boolean isFavorite() {
        try {
            return favoriteItemService.isUserFavorite(AppContext.getUsername(), getType(),
                    PropertyUtils.getProperty(beanItem, "id").toString());
        } catch (Exception e) {
            LOG.error("Error while check favorite", e);
            return false;
        }
    }

    public void previewItem(final B item) {
        this.beanItem = item;
        initLayout();
        previewLayout.setTitle(initFormTitle());

        previewForm.setFormLayoutFactory(initFormLayoutFactory());
        previewForm.setBeanFormFieldFactory(initBeanFormFieldFactory());
        previewForm.setBean(item);

        if (favoriteBtn != null) {
            if (isFavorite()) {
                favoriteBtn.addStyleName("favorite-btn-selected");
            } else {
                favoriteBtn.addStyleName("favorite-btn");
            }
        }

        onPreviewItem();
    }

    private void initLayout() {
        if (sidebarContent != null) {
            sidebarContent.removeAllComponents();
        }

        initRelatedComponents();
        ComponentContainer bottomPanel = createBottomPanel();
        if (bottomPanel != null) {
            if (bodyContent.getComponentCount() >= 2) {
                bodyContent.replaceComponent(bodyContent.getComponent(bodyContent.getComponentCount() - 1), bottomPanel);
            } else {
                bodyContent.addComponent(bottomPanel);
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
