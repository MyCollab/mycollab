package com.mycollab.mobile.module.crm.view;

import com.mycollab.common.domain.criteria.ActivityStreamSearchCriteria;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ScreenData;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
public class AllActivitiesPresenter extends AbstractCrmPresenter<AllActivitiesView> {
    private static final long serialVersionUID = -2422488836026839744L;

    public AllActivitiesPresenter() {
        super(AllActivitiesView.class);
    }

    @Override
    protected void onGo(HasComponents navigator, ScreenData<?> data) {
        NavigationManager currentNav = (NavigationManager) navigator;
        ActivityStreamSearchCriteria searchCriteria = new ActivityStreamSearchCriteria();
        searchCriteria.setModuleSet(new SetSearchField(CrmModule.TYPE));
        searchCriteria.setSaccountid(new NumberSearchField(AppUI.getAccountId()));
        searchCriteria.setTypes(getRestrictedItemTypes());
        searchCriteria.addOrderField(new SearchCriteria.OrderField("createdTime", SearchCriteria.DESC));
        getView().getPagedBeanTable().setSearchCriteria(searchCriteria);
        currentNav.navigateTo(getView());
    }

    private SetSearchField<String> getRestrictedItemTypes() {
        SetSearchField<String> types = new SetSearchField<>();
        if (UserUIContext.canRead(RolePermissionCollections.CRM_ACCOUNT)) {
            types.addValue(CrmTypeConstants.ACCOUNT);
        }
        if (UserUIContext.canRead(RolePermissionCollections.CRM_CONTACT)) {
            types.addValue(CrmTypeConstants.CONTACT);
        }
        if (UserUIContext.canRead(RolePermissionCollections.CRM_LEAD)) {
            types.addValue(CrmTypeConstants.LEAD);
        }
        if (UserUIContext.canRead(RolePermissionCollections.CRM_CAMPAIGN)) {
            types.addValue(CrmTypeConstants.CAMPAIGN);
        }
        if (UserUIContext.canRead(RolePermissionCollections.CRM_OPPORTUNITY)) {
            types.addValue(CrmTypeConstants.OPPORTUNITY);
        }
        if (UserUIContext.canRead(RolePermissionCollections.CRM_CASE)) {
            types.addValue(CrmTypeConstants.CASE);
        }
        if (UserUIContext.canRead(RolePermissionCollections.CRM_TASK)) {
            types.addValue(CrmTypeConstants.TASK);
        }
        if (UserUIContext.canRead(RolePermissionCollections.CRM_MEETING)) {
            types.addValue(CrmTypeConstants.MEETING);
        }
        if (UserUIContext.canRead(RolePermissionCollections.CRM_CALL)) {
            types.addValue(CrmTypeConstants.CALL);
        }
        return types;
    }
}
