/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.view.cases;

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.SimpleActivity;
import com.mycollab.module.crm.domain.SimpleCase;
import com.mycollab.module.crm.domain.SimpleContact;
import com.mycollab.module.crm.domain.criteria.ActivitySearchCriteria;
import com.mycollab.module.crm.i18n.ContactI18nEnum;
import com.mycollab.module.crm.i18n.CrmCommonI18nEnum;
import com.mycollab.module.crm.ui.CrmAssetsManager;
import com.mycollab.module.crm.ui.components.*;
import com.mycollab.module.crm.view.activity.ActivityRelatedItemListComp;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.mycollab.vaadin.ui.IFormLayoutFactory;
import com.mycollab.vaadin.ui.IRelatedListHandlers;
import com.mycollab.vaadin.web.ui.AdvancedPreviewBeanForm;
import com.mycollab.vaadin.web.ui.DefaultDynaFormLayout;
import com.vaadin.ui.ComponentContainer;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class CaseReadViewImpl extends AbstractPreviewItemComp<SimpleCase> implements CaseReadView {
    private static final long serialVersionUID = 1L;

    private CaseContactListComp associateContactList;
    private ActivityRelatedItemListComp associateActivityList;
    private CrmActivityComponent activityComponent;

    private PeopleInfoComp peopleInfoComp;
    private DateInfoComp dateInfoComp;
    private CrmFollowersComp<SimpleCase> followersComp;

    public CaseReadViewImpl() {
        super(CrmAssetsManager.getAsset(CrmTypeConstants.CASE));
    }

    @Override
    protected AdvancedPreviewBeanForm<SimpleCase> initPreviewForm() {
        return new AdvancedPreviewBeanForm<>();
    }

    @Override
    protected ComponentContainer createButtonControls() {
        return new CrmPreviewFormControlsGenerator<>(previewForm).createButtonControls(RolePermissionCollections.CRM_CASE);
    }

    @Override
    protected ComponentContainer createBottomPanel() {
        return activityComponent;
    }

    @Override
    protected void onPreviewItem() {
        activityComponent.loadActivities("" + beanItem.getId());
        displayActivities();
        displayContacts();

        peopleInfoComp.displayEntryPeople(beanItem);
        dateInfoComp.displayEntryDateTime(beanItem);
        followersComp.displayFollowers(beanItem);

        tabSheet.selectTab(CrmTypeConstants.DETAIL);
    }

    @Override
    protected String initFormTitle() {
        return beanItem.getSubject();
    }

    @Override
    protected void initRelatedComponents() {
        associateContactList = new CaseContactListComp();
        associateActivityList = new ActivityRelatedItemListComp(true);

        activityComponent = new CrmActivityComponent(CrmTypeConstants.CASE);

        dateInfoComp = new DateInfoComp();
        peopleInfoComp = new PeopleInfoComp();
        followersComp = new CrmFollowersComp<>(CrmTypeConstants.CASE, RolePermissionCollections.CRM_CASE);
        addToSideBar(dateInfoComp, peopleInfoComp, followersComp);

        tabSheet.addTab(previewLayout, CrmTypeConstants.DETAIL, UserUIContext.getMessage(CrmCommonI18nEnum.TAB_ABOUT),
                CrmAssetsManager.getAsset(CrmTypeConstants.DETAIL));
        tabSheet.addTab(associateContactList, CrmTypeConstants.CONTACT, UserUIContext.getMessage(ContactI18nEnum.LIST),
                CrmAssetsManager.getAsset(CrmTypeConstants.CONTACT));
        tabSheet.addTab(associateActivityList, CrmTypeConstants.ACTIVITY, UserUIContext.getMessage(CrmCommonI18nEnum.TAB_ACTIVITY),
                CrmAssetsManager.getAsset(CrmTypeConstants.ACTIVITY));
    }

    @Override
    protected IFormLayoutFactory initFormLayoutFactory() {
        return new DefaultDynaFormLayout(CrmTypeConstants.CASE, CasesDefaultFormLayoutFactory.getForm());
    }

    @Override
    protected AbstractBeanFieldGroupViewFieldFactory<SimpleCase> initBeanFormFieldFactory() {
        return new CaseReadFormFieldFactory(previewForm);
    }

    protected void displayActivities() {
        ActivitySearchCriteria criteria = new ActivitySearchCriteria();
        criteria.setSaccountid(new NumberSearchField(AppUI.getAccountId()));
        criteria.setType(StringSearchField.and(CrmTypeConstants.CASE));
        criteria.setTypeid(new NumberSearchField(beanItem.getId()));
        associateActivityList.setSearchCriteria(criteria);
    }

    private void displayContacts() {
        associateContactList.displayContacts(beanItem);
    }

    @Override
    public SimpleCase getItem() {
        return beanItem;
    }

    @Override
    public HasPreviewFormHandlers<SimpleCase> getPreviewFormHandlers() {
        return previewForm;
    }

    @Override
    public IRelatedListHandlers<SimpleActivity> getRelatedActivityHandlers() {
        return associateActivityList;
    }

    @Override
    public IRelatedListHandlers<SimpleContact> getRelatedContactHandlers() {
        return associateContactList;
    }

    @Override
    protected String getType() {
        return CrmTypeConstants.CASE;
    }
}
