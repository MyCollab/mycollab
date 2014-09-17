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

import com.esofthead.mycollab.common.UrlEncodeDecoder;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.crm.events.LeadEvent;
import com.esofthead.mycollab.mobile.shell.events.ShellEvent;
import com.esofthead.mycollab.mobile.ui.AbstractMobilePresenter;
import com.esofthead.mycollab.module.crm.domain.Lead;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.service.LeadService;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.EditFormHandler;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */
public class LeadAddPresenter extends AbstractMobilePresenter<LeadAddView> {
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
						EventBusFactory.getInstance().post(
								new ShellEvent.NavigateBack(this, null));
					}

					@Override
					public void onCancel() {
					}

					@Override
					public void onSaveAndNew(final SimpleLead lead) {
						saveLead(lead);
						EventBusFactory.getInstance().post(
								new LeadEvent.GotoAdd(this, null));
					}
				});
	}

	@Override
	protected void onGo(ComponentContainer container, ScreenData<?> data) {
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

	private void saveLead(Lead lead) {
		LeadService leadService = ApplicationContextUtil
				.getSpringBean(LeadService.class);

		lead.setSaccountid(AppContext.getAccountId());
		if (lead.getId() == null) {
			leadService.saveWithSession(lead, AppContext.getUsername());
		} else {
			leadService.updateWithSession(lead, AppContext.getUsername());
		}

	}
}
