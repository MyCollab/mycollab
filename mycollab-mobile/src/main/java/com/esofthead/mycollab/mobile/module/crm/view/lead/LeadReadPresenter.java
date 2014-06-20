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

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.mobile.module.crm.events.LeadEvent;
import com.esofthead.mycollab.mobile.module.crm.ui.CrmGenericPresenter;
import com.esofthead.mycollab.mobile.ui.ConfirmDialog;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.esofthead.mycollab.module.crm.service.LeadService;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.DefaultPreviewFormHandler;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */
public class LeadReadPresenter extends CrmGenericPresenter<LeadReadView> {
	private static final long serialVersionUID = 2716978291456310563L;

	public LeadReadPresenter() {
		super(LeadReadView.class);
	}

	@Override
	protected void postInitView() {
		view.getPreviewFormHandlers().addFormHandler(
				new DefaultPreviewFormHandler<SimpleLead>() {
					@Override
					public void onEdit(SimpleLead data) {
						EventBus.getInstance().fireEvent(
								new LeadEvent.GotoEdit(this, data));
					}

					@Override
					public void onAdd(SimpleLead data) {
						EventBus.getInstance().fireEvent(
								new LeadEvent.GotoAdd(this, null));
					}

					@Override
					public void onDelete(final SimpleLead data) {
						ConfirmDialog.show(
								UI.getCurrent(),
								AppContext
										.getMessage(GenericI18Enum.DIALOG_CONFIRM_DELETE_RECORD_MESSAGE),
								AppContext
										.getMessage(GenericI18Enum.BUTTON_YES_LABEL),
								AppContext
										.getMessage(GenericI18Enum.BUTTON_NO_LABEL),
								new ConfirmDialog.CloseListener() {
									private static final long serialVersionUID = 1L;

									@Override
									public void onClose(ConfirmDialog dialog) {
										if (dialog.isConfirmed()) {
											LeadService LeadService = ApplicationContextUtil
													.getSpringBean(LeadService.class);
											LeadService.removeWithSession(
													data.getId(),
													AppContext.getUsername(),
													AppContext.getAccountId());
											EventBus.getInstance().fireEvent(
													new LeadEvent.GotoList(
															this, null));
										}
									}
								});
					}

					@Override
					public void onClone(SimpleLead data) {
						SimpleLead cloneData = (SimpleLead) data.copy();
						cloneData.setId(null);
						EventBus.getInstance().fireEvent(
								new LeadEvent.GotoEdit(this, cloneData));
					}

					@Override
					public void onCancel() {
						EventBus.getInstance().fireEvent(
								new LeadEvent.GotoList(this, null));
					}

					@Override
					public void gotoNext(SimpleLead data) {
						LeadService contactService = ApplicationContextUtil
								.getSpringBean(LeadService.class);
						LeadSearchCriteria criteria = new LeadSearchCriteria();
						criteria.setSaccountid(new NumberSearchField(AppContext
								.getAccountId()));
						criteria.setId(new NumberSearchField(data.getId(),
								NumberSearchField.GREATER));
						Integer nextId = contactService
								.getNextItemKey(criteria);
						if (nextId != null) {
							EventBus.getInstance().fireEvent(
									new LeadEvent.GotoRead(this, nextId));
						} else {
							NotificationUtil.showGotoLastRecordNotification();
						}

					}

					@Override
					public void gotoPrevious(SimpleLead data) {
						LeadService contactService = ApplicationContextUtil
								.getSpringBean(LeadService.class);
						LeadSearchCriteria criteria = new LeadSearchCriteria();
						criteria.setSaccountid(new NumberSearchField(AppContext
								.getAccountId()));
						criteria.setId(new NumberSearchField(data.getId(),
								NumberSearchField.LESSTHAN));
						Integer nextId = contactService
								.getPreviousItemKey(criteria);
						if (nextId != null) {
							EventBus.getInstance().fireEvent(
									new LeadEvent.GotoRead(this, nextId));
						} else {
							NotificationUtil.showGotoFirstRecordNotification();
						}
					}
				});
	}

	@Override
	protected void onGo(ComponentContainer container, ScreenData<?> data) {
		if (AppContext.canRead(RolePermissionCollections.CRM_LEAD)) {

			if (data.getParams() instanceof Integer) {
				LeadService leadService = ApplicationContextUtil
						.getSpringBean(LeadService.class);
				SimpleLead lead = leadService.findById(
						(Integer) data.getParams(), AppContext.getAccountId());
				if (lead != null) {
					super.onGo(container, data);
					view.previewItem(lead);

					AppContext.addFragment(CrmLinkGenerator
							.generateLeadPreviewLink(lead.getId()), AppContext
							.getMessage(
									GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE,
									"Lead", lead.getLeadName()));

				} else {
					NotificationUtil.showRecordNotExistNotification();
					return;
				}

			}
		} else {
			NotificationUtil.showMessagePermissionAlert();
		}
	}

}
