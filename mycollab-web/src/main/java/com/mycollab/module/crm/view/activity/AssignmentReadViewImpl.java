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
package com.mycollab.module.crm.view.activity;

import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.SimpleCrmTask;
import com.mycollab.module.crm.i18n.CrmCommonI18nEnum;
import com.mycollab.module.crm.ui.CrmAssetsManager;
import com.mycollab.module.crm.ui.components.*;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.mycollab.vaadin.ui.IFormLayoutFactory;
import com.mycollab.vaadin.web.ui.AdvancedPreviewBeanForm;
import com.mycollab.vaadin.web.ui.DefaultDynaFormLayout;
import com.vaadin.ui.ComponentContainer;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
@ViewComponent
public class AssignmentReadViewImpl extends AbstractPreviewItemComp<SimpleCrmTask> implements AssignmentReadView {
    private static final long serialVersionUID = 1L;

    private CrmActivityComponent activityComponent;

    private DateInfoComp dateInfoComp;
    private CrmFollowersComp<SimpleCrmTask> followersComp;

    public AssignmentReadViewImpl() {
        super(CrmAssetsManager.getAsset(CrmTypeConstants.TASK));
    }

    @Override
    protected AdvancedPreviewBeanForm<SimpleCrmTask> initPreviewForm() {
        return new AdvancedPreviewBeanForm<>();
    }

    @Override
    protected ComponentContainer createButtonControls() {
        return new CrmPreviewFormControlsGenerator<>(previewForm).createButtonControls(RolePermissionCollections.CRM_TASK);
    }

    @Override
    protected ComponentContainer createBottomPanel() {
        return activityComponent;
    }

    @Override
    protected void onPreviewItem() {
        activityComponent.loadActivities("" + beanItem.getId());
        dateInfoComp.displayEntryDateTime(beanItem);
        followersComp.displayFollowers(beanItem);
    }

    @Override
    protected String initFormTitle() {
        return beanItem.getSubject();
    }

    @Override
    protected void initRelatedComponents() {
        activityComponent = new CrmActivityComponent(CrmTypeConstants.TASK);

        dateInfoComp = new DateInfoComp();
        followersComp = new CrmFollowersComp<>(CrmTypeConstants.TASK, RolePermissionCollections.CRM_TASK);
        addToSideBar(dateInfoComp, followersComp);

        tabSheet.addTab(previewLayout, CrmTypeConstants.DETAIL, UserUIContext.getMessage(CrmCommonI18nEnum.TAB_ABOUT),
                CrmAssetsManager.getAsset(CrmTypeConstants.DETAIL));
        tabSheet.selectTab(CrmTypeConstants.DETAIL);
    }

    @Override
    protected IFormLayoutFactory initFormLayoutFactory() {
        return new DefaultDynaFormLayout(CrmTypeConstants.TASK, AssignmentDefaultFormLayoutFactory.getForm());
    }

    @Override
    protected AbstractBeanFieldGroupViewFieldFactory<SimpleCrmTask> initBeanFormFieldFactory() {
        return new AssignmentReadFormFieldFactory(previewForm);
    }

    @Override
    public SimpleCrmTask getItem() {
        return beanItem;
    }

    @Override
    public HasPreviewFormHandlers<SimpleCrmTask> getPreviewFormHandlers() {
        return previewForm;
    }

    @Override
    protected String getType() {
        return CrmTypeConstants.TASK;
    }
}
