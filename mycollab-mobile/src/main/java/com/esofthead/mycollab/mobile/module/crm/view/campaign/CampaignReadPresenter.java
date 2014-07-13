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

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.crm.events.CampaignEvent;
import com.esofthead.mycollab.mobile.module.crm.ui.CrmGenericPresenter;
import com.esofthead.mycollab.mobile.ui.ConfirmDialog;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.domain.SimpleCampaign;
import com.esofthead.mycollab.module.crm.domain.criteria.CampaignSearchCriteria;
import com.esofthead.mycollab.module.crm.service.CampaignService;
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
public class CampaignReadPresenter extends
		CrmGenericPresenter<CampaignReadView> {
	private static final long serialVersionUID = 724501700304510910L;

	public CampaignReadPresenter() {
		super(CampaignReadView.class);
	}

	@Override
	protected void postInitView() {
		view.getPreviewFormHandlers().addFormHandler(
				new DefaultPreviewFormHandler<SimpleCampaign>() {
					@Override
					public void onEdit(SimpleCampaign data) {
						EventBusFactory.getInstance().post(
								new CampaignEvent.GotoEdit(this, data));
					}

					@Override
					public void onAdd(SimpleCampaign data) {
						EventBusFactory.getInstance().post(
								new CampaignEvent.GotoAdd(this, null));
					}

					@Override
					public void onDelete(final SimpleCampaign data) {
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
											CampaignService campaignService = ApplicationContextUtil
													.getSpringBean(CampaignService.class);
											campaignService.removeWithSession(
													data.getId(),
													AppContext.getUsername(),
													AppContext.getAccountId());
											EventBusFactory.getInstance().post(
													new CampaignEvent.GotoList(
															this, null));
										}
									}
								});
					}

					@Override
					public void onClone(SimpleCampaign data) {
						SimpleCampaign cloneData = (SimpleCampaign) data.copy();
						cloneData.setId(null);
						EventBusFactory.getInstance().post(
								new CampaignEvent.GotoEdit(this, cloneData));
					}

					@Override
					public void onCancel() {
						EventBusFactory.getInstance().post(
								new CampaignEvent.GotoList(this, null));
					}

					@Override
					public void gotoNext(SimpleCampaign data) {
						CampaignService contactService = ApplicationContextUtil
								.getSpringBean(CampaignService.class);
						CampaignSearchCriteria criteria = new CampaignSearchCriteria();
						criteria.setSaccountid(new NumberSearchField(AppContext
								.getAccountId()));
						criteria.setId(new NumberSearchField(data.getId(),
								NumberSearchField.GREATER));
						Integer nextId = contactService
								.getNextItemKey(criteria);
						if (nextId != null) {
							EventBusFactory.getInstance().post(
									new CampaignEvent.GotoRead(this, nextId));
						} else {
							NotificationUtil.showGotoLastRecordNotification();
						}

					}

					@Override
					public void gotoPrevious(SimpleCampaign data) {
						CampaignService contactService = ApplicationContextUtil
								.getSpringBean(CampaignService.class);
						CampaignSearchCriteria criteria = new CampaignSearchCriteria();
						criteria.setSaccountid(new NumberSearchField(AppContext
								.getAccountId()));
						criteria.setId(new NumberSearchField(data.getId(),
								NumberSearchField.LESSTHAN));
						Integer nextId = contactService
								.getPreviousItemKey(criteria);
						if (nextId != null) {
							EventBusFactory.getInstance().post(
									new CampaignEvent.GotoRead(this, nextId));
						} else {
							NotificationUtil.showGotoFirstRecordNotification();
						}
					}
				});
	}

	@Override
	protected void onGo(ComponentContainer container, ScreenData<?> data) {
		if (AppContext.canRead(RolePermissionCollections.CRM_CAMPAIGN)) {

			if (data.getParams() instanceof Integer) {
				CampaignService campaignService = ApplicationContextUtil
						.getSpringBean(CampaignService.class);
				SimpleCampaign campaign = campaignService.findById(
						(Integer) data.getParams(), AppContext.getAccountId());
				if (campaign != null) {
					super.onGo(container, data);
					view.previewItem(campaign);
					AppContext.addFragment(CrmLinkGenerator
							.generateCampaignPreviewLink(campaign.getId()),
							AppContext.getMessage(
									GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE,
									"Campaign", campaign.getCampaignname()));
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
