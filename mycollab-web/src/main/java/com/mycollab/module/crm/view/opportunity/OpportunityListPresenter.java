package com.mycollab.module.crm.view.opportunity;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.SecureAccessException;
import com.mycollab.db.persistence.service.ISearchableService;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.Opportunity;
import com.mycollab.module.crm.domain.SimpleOpportunity;
import com.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.mycollab.module.crm.i18n.OpportunityI18nEnum;
import com.mycollab.module.crm.service.OpportunityService;
import com.mycollab.module.crm.view.CrmGenericListPresenter;
import com.mycollab.module.crm.view.CrmModule;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.MassUpdateCommand;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.web.ui.DefaultMassEditActionHandler;
import com.mycollab.vaadin.web.ui.MailFormWindow;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.UI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class OpportunityListPresenter extends CrmGenericListPresenter<OpportunityListView, OpportunitySearchCriteria, SimpleOpportunity>
        implements MassUpdateCommand<Opportunity> {
    private static final long serialVersionUID = 1L;

    private OpportunityService opportunityService;

    public OpportunityListPresenter() {
        super(OpportunityListView.class, OpportunityCrmListNoItemView.class);
    }

    @Override
    protected void postInitView() {
        super.postInitView();

        opportunityService = AppContextUtil.getSpringBean(OpportunityService.class);

        view.getPopupActionHandlers().setMassActionHandler(new DefaultMassEditActionHandler(this) {

            @Override
            protected void onSelectExtra(String id) {
                if ("mail".equals(id)) {
                    UI.getCurrent().addWindow(new MailFormWindow());
                } else if ("massUpdate".equals(id)) {
                    MassUpdateOpportunityWindow massUpdateWindow = new MassUpdateOpportunityWindow(
                            UserUIContext.getMessage(GenericI18Enum.WINDOW_MASS_UPDATE_TITLE, UserUIContext.getMessage
                                    (OpportunityI18nEnum.LIST)),
                            OpportunityListPresenter.this);
                    UI.getCurrent().addWindow(massUpdateWindow);
                }
            }

            @Override
            protected String getReportTitle() {
                return UserUIContext.getMessage(OpportunityI18nEnum.LIST);
            }

            @Override
            protected Class<?> getReportModelClassType() {
                return SimpleOpportunity.class;
            }
        });
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        CrmModule.navigateItem(CrmTypeConstants.OPPORTUNITY);
        if (UserUIContext.canRead(RolePermissionCollections.CRM_OPPORTUNITY)) {
            searchCriteria = (OpportunitySearchCriteria) data.getParams();
            int totalCount = opportunityService.getTotalCount(searchCriteria);
            if (totalCount > 0) {
                this.displayListView(container, data);
                doSearch(searchCriteria);
            } else {
                this.displayNoExistItems(container, data);
            }

            AppUI.addFragment("crm/opportunity/list", UserUIContext.getMessage(OpportunityI18nEnum.LIST));
        } else {
            throw new SecureAccessException();
        }
    }

    @Override
    protected void deleteSelectedItems() {
        if (!isSelectAll) {
            Collection<SimpleOpportunity> currentDataList = view.getPagedBeanTable().getCurrentDataList();
            List<Opportunity> keyList = new ArrayList<>();
            for (SimpleOpportunity item : currentDataList) {
                if (item.isSelected()) {
                    keyList.add(item);
                }
            }

            if (keyList.size() > 0) {
                opportunityService.massRemoveWithSession(keyList, UserUIContext.getUsername(), AppUI.getAccountId());
                doSearch(searchCriteria);
                checkWhetherEnableTableActionControl();
            }
        } else {
            opportunityService.removeByCriteria(searchCriteria, AppUI.getAccountId());
            doSearch(searchCriteria);
        }
    }

    @Override
    public void massUpdate(Opportunity value) {
        if (!isSelectAll) {
            Collection<SimpleOpportunity> currentDataList = view.getPagedBeanTable().getCurrentDataList();
            List<Integer> keyList = new ArrayList<>();
            for (SimpleOpportunity item : currentDataList) {
                if (item.isSelected()) {
                    keyList.add(item.getId());
                }
            }

            if (keyList.size() > 0) {
                opportunityService.massUpdateWithSession(value, keyList, AppUI.getAccountId());
                doSearch(searchCriteria);
            }
        } else {
            opportunityService.updateBySearchCriteria(value, searchCriteria);
            doSearch(searchCriteria);
        }
    }

    @Override
    public ISearchableService<OpportunitySearchCriteria> getSearchService() {
        return AppContextUtil.getSpringBean(OpportunityService.class);
    }
}
