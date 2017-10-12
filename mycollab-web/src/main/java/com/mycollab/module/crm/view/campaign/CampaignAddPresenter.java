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
package com.mycollab.module.crm.view.campaign;

import com.mycollab.common.UrlEncodeDecoder;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.ResourceNotFoundException;
import com.mycollab.core.SecureAccessException;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.CampaignWithBLOBs;
import com.mycollab.module.crm.domain.SimpleCampaign;
import com.mycollab.module.crm.event.CampaignEvent;
import com.mycollab.module.crm.i18n.CampaignI18nEnum;
import com.mycollab.module.crm.service.CampaignService;
import com.mycollab.module.crm.view.CrmGenericPresenter;
import com.mycollab.module.crm.view.CrmModule;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.IEditFormHandler;
import com.mycollab.vaadin.mvp.ScreenData;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class CampaignAddPresenter extends CrmGenericPresenter<CampaignAddView> {
    private static final long serialVersionUID = 1L;

    public CampaignAddPresenter() {
        super(CampaignAddView.class);
    }

    @Override
    protected void postInitView() {
        view.getEditFormHandlers().addFormHandler(new IEditFormHandler<SimpleCampaign>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSave(final SimpleCampaign campaign) {
                int campaignId = saveCampaign(campaign);
                EventBusFactory.getInstance().post(new CampaignEvent.GotoRead(this, campaignId));
            }

            @Override
            public void onCancel() {
                EventBusFactory.getInstance().post(new CampaignEvent.GotoList(this, null));
            }

            @Override
            public void onSaveAndNew(final SimpleCampaign campaign) {
                saveCampaign(campaign);
                EventBusFactory.getInstance().post(new CampaignEvent.GotoAdd(this, null));
            }
        });
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        CrmModule.navigateItem(CrmTypeConstants.CAMPAIGN);
        if (UserUIContext.canWrite(RolePermissionCollections.CRM_CAMPAIGN)) {
            SimpleCampaign campaign = null;
            if (data.getParams() instanceof SimpleCampaign) {
                campaign = (SimpleCampaign) data.getParams();
            } else if (data.getParams() instanceof Integer) {
                CampaignService campaignService = AppContextUtil.getSpringBean(CampaignService.class);
                campaign = campaignService.findById((Integer) data.getParams(), AppUI.getAccountId());
            }
            if (campaign == null) {
                throw new ResourceNotFoundException();
            }

            super.onGo(container, data);
            view.editItem(campaign);

            if (campaign.getId() == null) {
                AppUI.addFragment("crm/campaign/add", UserUIContext.getMessage(GenericI18Enum.BROWSER_ADD_ITEM_TITLE,
                        UserUIContext.getMessage(CampaignI18nEnum.SINGLE)));
            } else {
                AppUI.addFragment("crm/campaign/edit/" + UrlEncodeDecoder.encode(campaign.getId()),
                        UserUIContext.getMessage(GenericI18Enum.BROWSER_EDIT_ITEM_TITLE,
                                UserUIContext.getMessage(CampaignI18nEnum.SINGLE), campaign.getCampaignname()));
            }
        } else {
            throw new SecureAccessException();
        }
    }

    private int saveCampaign(CampaignWithBLOBs campaign) {
        CampaignService campaignService = AppContextUtil.getSpringBean(CampaignService.class);
        campaign.setSaccountid(AppUI.getAccountId());
        if (campaign.getId() == null) {
            campaignService.saveWithSession(campaign, UserUIContext.getUsername());
        } else {
            campaignService.updateWithSession(campaign, UserUIContext.getUsername());
        }
        return campaign.getId();
    }
}
