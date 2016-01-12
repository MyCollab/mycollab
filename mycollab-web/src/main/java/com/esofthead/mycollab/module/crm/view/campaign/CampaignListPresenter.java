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
package com.esofthead.mycollab.module.crm.view.campaign;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.persistence.service.ISearchableService;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.CampaignWithBLOBs;
import com.esofthead.mycollab.module.crm.domain.SimpleCampaign;
import com.esofthead.mycollab.module.crm.domain.criteria.CampaignSearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.CampaignI18nEnum;
import com.esofthead.mycollab.module.crm.service.CampaignService;
import com.esofthead.mycollab.module.crm.view.CrmGenericListPresenter;
import com.esofthead.mycollab.module.crm.view.CrmModule;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.web.ui.DefaultMassEditActionHandler;
import com.esofthead.mycollab.vaadin.mvp.MassUpdateCommand;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.web.ui.MailFormWindow;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class CampaignListPresenter extends
        CrmGenericListPresenter<CampaignListView, CampaignSearchCriteria, SimpleCampaign>
        implements MassUpdateCommand<CampaignWithBLOBs> {

    private static final long serialVersionUID = 1L;
    private CampaignService campaignService;

    public CampaignListPresenter() {
        super(CampaignListView.class, CampaignCrmListNoItemView.class);
    }

    @Override
    protected void postInitView() {
        super.postInitView();
        campaignService = ApplicationContextUtil.getSpringBean(CampaignService.class);

        view.getPopupActionHandlers().setMassActionHandler(new DefaultMassEditActionHandler(this) {

            @Override
            protected void onSelectExtra(String id) {
                if ("mail".equals(id)) {
                    UI.getCurrent().addWindow(new MailFormWindow());
                } else if ("massUpdate".equals(id)) {
                    MassUpdateCampaignWindow massUpdateWindow = new MassUpdateCampaignWindow(
                            AppContext.getMessage(GenericI18Enum.WINDOW_MASS_UPDATE_TITLE,
                                    "Campaign"), CampaignListPresenter.this);
                    UI.getCurrent().addWindow(massUpdateWindow);
                }
            }

            @Override
            protected String getReportTitle() {
                return AppContext.getMessage(CampaignI18nEnum.VIEW_LIST_TITLE);
            }

            @Override
            protected Class<?> getReportModelClassType() {
                return SimpleCampaign.class;
            }
        });
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        CrmModule.navigateItem(CrmTypeConstants.CAMPAIGN);
        if (AppContext.canRead(RolePermissionCollections.CRM_CAMPAIGN)) {
            searchCriteria = (CampaignSearchCriteria) data.getParams();
            int totalCount = campaignService.getTotalCount(searchCriteria);
            if (totalCount > 0) {
                this.displayListView(container, data);
                doSearch(searchCriteria);
            } else {
                this.displayNoExistItems(container, data);
            }

            AppContext.addFragment("crm/campaign/list",
                    AppContext.getMessage(CampaignI18nEnum.VIEW_LIST_TITLE));
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }

    @Override
    protected void deleteSelectedItems() {
        if (!isSelectAll) {
            Collection<SimpleCampaign> currentDataList = view.getPagedBeanTable().getCurrentDataList();
            List<CampaignWithBLOBs> keyList = new ArrayList<>();
            for (SimpleCampaign item : currentDataList) {
                if (item.isSelected()) {
                    keyList.add(item);
                }
            }

            if (keyList.size() > 0) {
                campaignService.massRemoveWithSession(keyList, AppContext.getUsername(), AppContext.getAccountId());
                doSearch(searchCriteria);
                checkWhetherEnableTableActionControl();
            }
        } else {
            campaignService.removeByCriteria(searchCriteria, AppContext.getAccountId());
            doSearch(searchCriteria);
        }
    }

    @Override
    public void massUpdate(CampaignWithBLOBs value) {
        if (!isSelectAll) {
            Collection<SimpleCampaign> currentDataList = view.getPagedBeanTable().getCurrentDataList();
            List<Integer> keyList = new ArrayList<>();
            for (SimpleCampaign item : currentDataList) {
                if (item.isSelected()) {
                    keyList.add(item.getId());
                }
            }
            if (keyList.size() > 0) {
                campaignService.massUpdateWithSession(value, keyList, AppContext.getAccountId());
                doSearch(searchCriteria);
            }
        } else {
            campaignService.updateBySearchCriteria(value, searchCriteria);
            doSearch(searchCriteria);
        }
    }

    @Override
    public ISearchableService<CampaignSearchCriteria> getSearchService() {
        return ApplicationContextUtil.getSpringBean(CampaignService.class);
    }
}
