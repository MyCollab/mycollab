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
package com.esofthead.mycollab.mobile.module.crm.view.campaign;

import java.util.Arrays;
import java.util.GregorianCalendar;

import com.esofthead.mycollab.common.UrlEncodeDecoder;
import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.mobile.module.crm.events.CampaignEvent;
import com.esofthead.mycollab.mobile.module.crm.ui.CrmGenericPresenter;
import com.esofthead.mycollab.module.crm.domain.CampaignLead;
import com.esofthead.mycollab.module.crm.domain.CampaignWithBLOBs;
import com.esofthead.mycollab.module.crm.domain.SimpleCampaign;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.service.CampaignService;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.EditFormHandler;
import com.esofthead.mycollab.vaadin.mvp.HistoryViewManager;
import com.esofthead.mycollab.vaadin.mvp.NullViewState;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.mvp.ViewState;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.esofthead.vaadin.mobilecomponent.MobileNavigationManager;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class CampaignAddPresenter extends CrmGenericPresenter<CampaignAddview> {

	private static final long serialVersionUID = 1L;

	public CampaignAddPresenter() {
		super(CampaignAddview.class);
	}

	@Override
	protected void postInitView() {
		view.getEditFormHandlers().addFormHandler(
				new EditFormHandler<SimpleCampaign>() {
					private static final long serialVersionUID = 1L;

					@Override
					public void onSave(final SimpleCampaign campaign) {
						saveCampaign(campaign);
						ViewState viewState = HistoryViewManager.back();
						if (viewState instanceof NullViewState) {
							EventBus.getInstance().fireEvent(
									new CampaignEvent.GotoList(this, null));
						}
					}

					@Override
					public void onCancel() {
						ViewState viewState = HistoryViewManager.back();
						if (viewState instanceof NullViewState) {
							EventBus.getInstance().fireEvent(
									new CampaignEvent.GotoList(this, null));
						}
					}

					@Override
					public void onSaveAndNew(final SimpleCampaign campaign) {
						saveCampaign(campaign);
						EventBus.getInstance().fireEvent(
								new CampaignEvent.GotoAdd(this, null));
					}
				});
	}

	@Override
	protected void onGo(MobileNavigationManager container, ScreenData<?> data) {
		if (AppContext.canWrite(RolePermissionCollections.CRM_CAMPAIGN)) {

			SimpleCampaign campaign = null;
			if (data.getParams() instanceof SimpleCampaign) {
				campaign = (SimpleCampaign) data.getParams();
			} else if (data.getParams() instanceof Integer) {
				CampaignService campaignService = ApplicationContextUtil
						.getSpringBean(CampaignService.class);
				campaign = campaignService.findById(
						(Integer) data.getParams(), AppContext.getAccountId());
				if (campaign == null) {
					NotificationUtil.showRecordNotExistNotification();
					return;
				}
			}

			super.onGo(container, data);
			view.editItem(campaign);

			if (campaign.getId() == null) {
				AppContext.addFragment("crm/campaign/add", AppContext
						.getMessage(GenericI18Enum.BROWSER_ADD_ITEM_TITLE,
								"Campaign"));
			} else {
				AppContext.addFragment(
						"crm/campaign/edit/"
								+ UrlEncodeDecoder.encode(campaign.getId()),
								AppContext.getMessage(
										GenericI18Enum.BROWSER_EDIT_ITEM_TITLE,
										"Campaign", campaign.getCampaignname()));
			}
		} else {
			NotificationUtil.showMessagePermissionAlert();
		}
	}

	public void saveCampaign(CampaignWithBLOBs campaign) {
		CampaignService campaignService = ApplicationContextUtil
				.getSpringBean(CampaignService.class);

		campaign.setSaccountid(AppContext.getAccountId());
		if (campaign.getId() == null) {
			campaignService.saveWithSession(campaign, AppContext.getUsername());

			if (campaign.getExtraData() != null
					&& campaign.getExtraData() instanceof SimpleLead) {
				CampaignLead associateLead = new CampaignLead();
				associateLead.setCampaignid(campaign.getId());
				associateLead.setLeadid(((SimpleLead) campaign.getExtraData())
						.getId());
				associateLead.setCreatedtime(new GregorianCalendar().getTime());

				campaignService
				.saveCampaignLeadRelationship(
						Arrays.asList(associateLead),
						AppContext.getAccountId());
			}
		} else {
			campaignService.updateWithSession(campaign,
					AppContext.getUsername());
		}

	}
}
