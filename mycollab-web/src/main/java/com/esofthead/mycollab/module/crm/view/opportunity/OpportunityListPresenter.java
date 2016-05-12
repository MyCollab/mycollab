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
package com.esofthead.mycollab.module.crm.view.opportunity;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.SecureAccessException;
import com.esofthead.mycollab.core.persistence.service.ISearchableService;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.Opportunity;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.OpportunityI18nEnum;
import com.esofthead.mycollab.module.crm.service.OpportunityService;
import com.esofthead.mycollab.module.crm.view.CrmGenericListPresenter;
import com.esofthead.mycollab.module.crm.view.CrmModule;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.AppContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.MassUpdateCommand;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.web.ui.DefaultMassEditActionHandler;
import com.esofthead.mycollab.vaadin.web.ui.MailFormWindow;
import com.vaadin.ui.ComponentContainer;
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
                            AppContext.getMessage(GenericI18Enum.WINDOW_MASS_UPDATE_TITLE, AppContext.getMessage
                                    (OpportunityI18nEnum.LIST)),
                            OpportunityListPresenter.this);
                    UI.getCurrent().addWindow(massUpdateWindow);
                }
            }

            @Override
            protected String getReportTitle() {
                return AppContext.getMessage(OpportunityI18nEnum.LIST);
            }

            @Override
            protected Class<?> getReportModelClassType() {
                return SimpleOpportunity.class;
            }
        });
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        CrmModule.navigateItem(CrmTypeConstants.OPPORTUNITY);
        if (AppContext.canRead(RolePermissionCollections.CRM_OPPORTUNITY)) {
            searchCriteria = (OpportunitySearchCriteria) data.getParams();
            int totalCount = opportunityService.getTotalCount(searchCriteria);
            if (totalCount > 0) {
                this.displayListView(container, data);
                doSearch(searchCriteria);
            } else {
                this.displayNoExistItems(container, data);
            }

            AppContext.addFragment("crm/opportunity/list", AppContext.getMessage(OpportunityI18nEnum.LIST));
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
                opportunityService.massRemoveWithSession(keyList, AppContext.getUsername(), AppContext.getAccountId());
                doSearch(searchCriteria);
                checkWhetherEnableTableActionControl();
            }
        } else {
            opportunityService.removeByCriteria(searchCriteria, AppContext.getAccountId());
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
                opportunityService.massUpdateWithSession(value, keyList, AppContext.getAccountId());
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
