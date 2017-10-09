package com.mycollab.vaadin.web.ui;

import com.mycollab.common.domain.FavoriteItem;
import com.mycollab.common.service.FavoriteItemService;
import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.PageView;
import com.mycollab.vaadin.ui.ELabel;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.viritin.button.MButton;
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
    private boolean isDisplaySideBar = true;

    protected AdvancedPreviewBeanForm<B> previewForm;
    protected ReadViewLayout previewLayout;
    protected MHorizontalLayout header;
    private HorizontalLayout actionControls;
    private MVerticalLayout sidebarContent;
    private MVerticalLayout bodyContent;
    private MButton favoriteBtn;

    public AbstractPreviewItemComp(String headerText, FontAwesome iconResource) {
        this(headerText, iconResource, null);
    }

    public AbstractPreviewItemComp(MHorizontalLayout customHeader, ReadViewLayout layout) {
        this.header = customHeader;
        this.addComponent(header);
        isDisplaySideBar = false;
        this.previewLayout = layout;
        initContent();
    }

    public AbstractPreviewItemComp(String headerText, FontAwesome iconResource, ReadViewLayout layout) {
        ELabel headerLbl = ELabel.h2("").withWidthUndefined();
        this.previewLayout = layout;

        header = new MHorizontalLayout().withStyleName("hdr-view").withFullWidth();
        header.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

        if (iconResource != null) {
            String title = iconResource.getHtml() + " " + headerText;
            headerLbl.setValue(title);
        } else {
            headerLbl.setValue(headerText);
        }

        if (SiteConfiguration.isCommunityEdition()) {
            header.with(headerLbl).expand(headerLbl);
        } else {
            favoriteBtn = new MButton("", clickEvent -> toggleFavorite()).withIcon(FontAwesome.HEART);

            Label spaceLbl = new Label();
            header.with(headerLbl, favoriteBtn, spaceLbl).expand(spaceLbl);
        }

        this.addComponent(header);
        ComponentContainer extraComp;
        if ((extraComp = createExtraControls()) != null) {
            this.addComponent(extraComp);
        }
        initContent();
    }

    private void initContent() {
        previewForm = initPreviewForm();
        actionControls = createButtonControls();
        if (actionControls != null) {
            header.with(actionControls).withAlign(actionControls, Alignment.TOP_RIGHT);
        }

        MCssLayout contentWrapper = new MCssLayout().withFullWidth().withStyleName(WebThemes.CONTENT_WRAPPER);

        if (previewLayout == null)
            previewLayout = new DefaultReadViewLayout("");

        contentWrapper.addComponent(previewLayout);

        if (isDisplaySideBar) {
            RightSidebarLayout bodyContainer = new RightSidebarLayout();
            bodyContainer.setSizeFull();

            bodyContent = new MVerticalLayout(previewForm).withSpacing(false).withMargin(false).withFullWidth();
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

    private void toggleFavorite() {
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
            favoriteItem.setSaccountid(AppUI.getAccountId());
            favoriteItem.setCreateduser(UserUIContext.getUsername());
            FavoriteItemService favoriteItemService = AppContextUtil.getSpringBean(FavoriteItemService.class);
            favoriteItemService.saveOrDelete(favoriteItem);
        } catch (Exception e) {
            LOG.error("Error while set favorite flag to bean", e);
        }
    }

    private boolean isFavorite() {
        try {
            FavoriteItemService favoriteItemService = AppContextUtil.getSpringBean(FavoriteItemService.class);
            return favoriteItemService.isUserFavorite(UserUIContext.getUsername(), getType(),
                    PropertyUtils.getProperty(beanItem, "id").toString());
        } catch (Exception e) {
            LOG.error("Error while check favorite", e);
            return false;
        }
    }

    public void previewItem(final B item) {
        this.beanItem = item;
        initLayout();

        if (previewLayout instanceof DefaultReadViewLayout) {
            ((DefaultReadViewLayout) previewLayout).setTitle(initFormTitle());
        }

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
    final public void addViewListener(ViewListener listener) {

    }

    abstract protected void onPreviewItem();

    abstract protected String initFormTitle();

    abstract protected AdvancedPreviewBeanForm<B> initPreviewForm();

    protected ComponentContainer createExtraControls() {
        return null;
    }

    abstract protected HorizontalLayout createButtonControls();

    abstract protected ComponentContainer createBottomPanel();
}
