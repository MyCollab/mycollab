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

import com.mycollab.common.domain.FavoriteItem;
import com.mycollab.common.service.FavoriteItemService;
import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.core.MyCollabException;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.IPreviewView;
import com.mycollab.vaadin.touchkit.NavigationBarQuickMenu;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.IFormLayoutFactory;
import com.mycollab.vaadin.ui.UIConstants;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @param <B>
 * @author MyCollab Ltd.
 * @since 3.0
 */
public abstract class AbstractPreviewItemComp<B> extends AbstractMobilePageView implements IPreviewView<B> {
    private static final long serialVersionUID = 1L;
    private static Logger LOG = LoggerFactory.getLogger(AbstractPreviewItemComp.class);

    protected B beanItem;
    protected AdvancedPreviewBeanForm<B> previewForm;
    private Button favoriteBtn;

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
        } else if (buttonControls instanceof HorizontalLayout) {
            if (StringUtils.isNotBlank(getType()) && !SiteConfiguration.isCommunityEdition()) {
                favoriteBtn = new MButton("", clickEvent -> toggleFavorite()).withIcon(FontAwesome.HEART).withStyleName
                        (UIConstants.CIRCLE_BOX);
                ((HorizontalLayout)buttonControls).addComponent(favoriteBtn, 0);
            }
            this.setRightComponent(buttonControls);
        } else {
            throw new MyCollabException("Not support controls " + buttonControls);
        }

        if (favoriteBtn != null) {
            if (isFavorite()) {
                favoriteBtn.addStyleName("favorite-btn-selected");
            } else {
                favoriteBtn.addStyleName("favorite-btn");
            }
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

    abstract protected String getType();

    abstract protected IFormLayoutFactory initFormLayoutFactory();

    abstract protected AbstractBeanFieldGroupViewFieldFactory<B> initBeanFormFieldFactory();

    abstract protected ComponentContainer createButtonControls();

    abstract protected ComponentContainer createBottomPanel();

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
            favoriteItem.setSaccountid(MyCollabUI.getAccountId());
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

}
