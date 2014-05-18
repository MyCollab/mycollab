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
package com.esofthead.mycollab.mobile.module.crm.view.lead;

import java.util.Arrays;
import java.util.GregorianCalendar;

import com.esofthead.mycollab.common.UrlEncodeDecoder;
import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.mobile.module.crm.events.LeadEvent;
import com.esofthead.mycollab.mobile.module.crm.ui.CrmGenericPresenter;
import com.esofthead.mycollab.module.crm.domain.CampaignLead;
import com.esofthead.mycollab.module.crm.domain.Lead;
import com.esofthead.mycollab.module.crm.domain.OpportunityLead;
import com.esofthead.mycollab.module.crm.domain.SimpleCampaign;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.crm.service.CampaignService;
import com.esofthead.mycollab.module.crm.service.LeadService;
import com.esofthead.mycollab.module.crm.service.OpportunityService;
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
 * 
 */
public class LeadAddPresenter extends CrmGenericPresenter<LeadAddView> {
	private static final long serialVersionUID = -873280663492245087L;

	public LeadAddPresenter() {
		super(LeadAddView.class);
	}

	@Override
	protected void postInitView() {
		view.getEditFormHandlers().addFormHandler(
				new EditFormHandler<SimpleLead>() {
					private static final long serialVersionUID = 1L;

					@Override
					public void onSave(final SimpleLead lead) {
						saveLead(lead);
						ViewState viewState = HistoryViewManager.back();
						if (viewState instanceof NullViewState) {
							EventBus.getInstance().fireEvent(
									new LeadEvent.GotoList(this, null));
						}
					}

					@Override
					public void onCancel() {
						ViewState viewState = HistoryViewManager.back();
						if (viewState instanceof NullViewState) {
							EventBus.getInstance().fireEvent(
									new LeadEvent.GotoList(this, null));
						}
					}

					@Override
					public void onSaveAndNew(final SimpleLead lead) {
						saveLead(lead);
						EventBus.getInstance().fireEvent(
								new LeadEvent.GotoAdd(this, null));
					}
				});
	}

	@Override
	protected void onGo(MobileNavigationManager container, ScreenData<?> data) {
		if (AppContext.canWrite(RolePermissionCollections.CRM_LEAD)) {

			SimpleLead lead = null;

			if (data.getParams() instanceof SimpleLead) {
				lead = (SimpleLead) data.getParams();
			} else if (data.getParams() instanceof Integer) {
				LeadService leadService = ApplicationContextUtil
						.getSpringBean(LeadService.class);
				lead = leadService.findById((Integer) data.getParams(),
						AppContext.getAccountId());
				if (lead == null) {
					NotificationUtil.showRecordNotExistNotification();
					return;
				}
			}

			super.onGo(container, data);
			view.editItem(lead);

			if (lead.getId() == null) {
				AppContext.addFragment("crm/lead/add", AppContext.getMessage(
						GenericI18Enum.BROWSER_ADD_ITEM_TITLE, "Lead"));
			} else {
				AppContext.addFragment(
						"crm/lead/edit/"
								+ UrlEncodeDecoder.encode(lead.getId()),
						AppContext.getMessage(
								GenericI18Enum.BROWSER_EDIT_ITEM_TITLE, "Lead",
								lead.getLastname()));
			}
		} else {
			NotificationUtil.showMessagePermissionAlert();
		}

	}

	public void saveLead(Lead lead) {
		LeadService leadService = ApplicationContextUtil
				.getSpringBean(LeadService.class);

		lead.setSaccountid(AppContext.getAccountId());
		if (lead.getId() == null) {
			leadService.saveWithSession(lead, AppContext.getUsername());

			if (lead.getExtraData() != null
					&& (lead.getExtraData() instanceof SimpleCampaign)) {
				CampaignLead associateLead = new CampaignLead();
				associateLead.setCampaignid(((SimpleCampaign) lead
						.getExtraData()).getId());
				associateLead.setLeadid(lead.getId());
				associateLead.setCreatedtime(new GregorianCalendar().getTime());

				CampaignService campaignService = ApplicationContextUtil
						.getSpringBean(CampaignService.class);
				campaignService
						.saveCampaignLeadRelationship(
								Arrays.asList(associateLead),
								AppContext.getAccountId());
			} else if (lead.getExtraData() != null
					&& lead.getExtraData() instanceof SimpleOpportunity) {
				OpportunityLead associateLead = new OpportunityLead();
				associateLead.setOpportunityid(((SimpleOpportunity) lead
						.getExtraData()).getId());
				associateLead.setLeadid(lead.getId());
				associateLead.setCreatedtime(new GregorianCalendar().getTime());

				OpportunityService opportunityService = ApplicationContextUtil
						.getSpringBean(OpportunityService.class);
				opportunityService
						.saveOpportunityLeadRelationship(
								Arrays.asList(associateLead),
								AppContext.getAccountId());
			}
		} else {
			leadService.updateWithSession(lead, AppContext.getUsername());
		}

	}
}
