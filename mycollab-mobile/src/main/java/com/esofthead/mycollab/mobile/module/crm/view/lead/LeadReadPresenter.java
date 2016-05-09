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
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.crm.events.ActivityEvent;
import com.esofthead.mycollab.mobile.module.crm.events.CampaignEvent;
import com.esofthead.mycollab.mobile.module.crm.events.LeadEvent;
import com.esofthead.mycollab.mobile.module.crm.view.AbstractCrmPresenter;
import com.esofthead.mycollab.mobile.shell.events.ShellEvent;
import com.esofthead.mycollab.mobile.ui.ConfirmDialog;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.*;
import com.esofthead.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.LeadI18nEnum;
import com.esofthead.mycollab.module.crm.service.CampaignService;
import com.esofthead.mycollab.module.crm.service.LeadService;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.DefaultPreviewFormHandler;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.esofthead.mycollab.vaadin.ui.RelatedListHandler;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class LeadReadPresenter extends AbstractCrmPresenter<LeadReadView> {
    private static final long serialVersionUID = 2716978291456310563L;

    public LeadReadPresenter() {
        super(LeadReadView.class);
    }

    @Override
    protected void postInitView() {
        view.getPreviewFormHandlers().addFormHandler(new DefaultPreviewFormHandler<SimpleLead>() {
            @Override
            public void onEdit(SimpleLead data) {
                EventBusFactory.getInstance().post(new LeadEvent.GotoEdit(this, data));
            }

            @Override
            public void onAdd(SimpleLead data) {
                EventBusFactory.getInstance().post(new LeadEvent.GotoAdd(this, null));
            }

            @Override
            public void onDelete(final SimpleLead data) {
                ConfirmDialog.show(UI.getCurrent(),
                        AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                        AppContext.getMessage(GenericI18Enum.BUTTON_YES),
                        AppContext.getMessage(GenericI18Enum.BUTTON_NO),
                        new ConfirmDialog.CloseListener() {
                            private static final long serialVersionUID = 1L;

                            @Override
                            public void onClose(ConfirmDialog dialog) {
                                if (dialog.isConfirmed()) {
                                    LeadService LeadService = ApplicationContextUtil.getSpringBean(LeadService.class);
                                    LeadService.removeWithSession(data, AppContext.getUsername(), AppContext.getAccountId());
                                    EventBusFactory.getInstance().post(new LeadEvent.GotoList(this, null));
                                }
                            }
                        });
            }

            @Override
            public void onClone(SimpleLead data) {
                SimpleLead cloneData = (SimpleLead) data.copy();
                cloneData.setId(null);
                EventBusFactory.getInstance().post(new LeadEvent.GotoEdit(this, cloneData));
            }

            @Override
            public void onCancel() {
                EventBusFactory.getInstance().post(new LeadEvent.GotoList(this, null));
            }

            @Override
            public void gotoNext(SimpleLead data) {
                LeadService contactService = ApplicationContextUtil.getSpringBean(LeadService.class);
                LeadSearchCriteria criteria = new LeadSearchCriteria();
                criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
                criteria.setId(new NumberSearchField(data.getId(), NumberSearchField.GREATER));
                Integer nextId = contactService.getNextItemKey(criteria);
                if (nextId != null) {
                    EventBusFactory.getInstance().post(new LeadEvent.GotoRead(this, nextId));
                } else {
                    NotificationUtil.showGotoLastRecordNotification();
                }

            }

            @Override
            public void gotoPrevious(SimpleLead data) {
                LeadService contactService = ApplicationContextUtil.getSpringBean(LeadService.class);
                LeadSearchCriteria criteria = new LeadSearchCriteria();
                criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
                criteria.setId(new NumberSearchField(data.getId(), NumberSearchField.LESSTHAN));
                Integer nextId = contactService.getPreviousItemKey(criteria);
                if (nextId != null) {
                    EventBusFactory.getInstance().post(
                            new LeadEvent.GotoRead(this, nextId));
                } else {
                    NotificationUtil.showGotoFirstRecordNotification();
                }
            }
        });
        view.getRelatedCampaignHandlers().addRelatedListHandler(
                new RelatedListHandler<SimpleCampaign>() {

                    @Override
                    public void selectAssociateItems(Set<SimpleCampaign> items) {
                        SimpleLead lead = view.getItem();
                        List<CampaignLead> associateCampaigns = new ArrayList<>();
                        for (SimpleCampaign campaign : items) {
                            CampaignLead associateCampaign = new CampaignLead();
                            associateCampaign.setCampaignid(campaign.getId());
                            associateCampaign.setLeadid(lead.getId());
                            associateCampaign.setCreatedtime(new GregorianCalendar().getTime());
                            associateCampaigns.add(associateCampaign);
                        }

                        CampaignService campaignService = ApplicationContextUtil.getSpringBean(CampaignService.class);
                        campaignService.saveCampaignLeadRelationship(associateCampaigns, AppContext.getAccountId());
                        EventBusFactory.getInstance().post(new ShellEvent.NavigateBack(this, null));
                    }

                    @Override
                    public void createNewRelatedItem(String itemId) {
                        SimpleCampaign campaign = new SimpleCampaign();
                        campaign.setExtraData(view.getItem());
                        EventBusFactory.getInstance().post(new CampaignEvent.GotoEdit(LeadReadPresenter.this, campaign));

                    }
                });
        view.getRelatedActivityHandlers().addRelatedListHandler(
                new RelatedListHandler<SimpleActivity>() {

                    @Override
                    public void selectAssociateItems(Set<SimpleActivity> items) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void createNewRelatedItem(String itemId) {
                        if (itemId.equals(CrmTypeConstants.TASK)) {
                            final SimpleTask task = new SimpleTask();
                            task.setType(CrmTypeConstants.ACCOUNT);
                            task.setTypeid(view.getItem().getId());
                            EventBusFactory.getInstance().post(new ActivityEvent.TaskEdit(LeadReadPresenter.this, task));
                        } else if (itemId.equals(CrmTypeConstants.MEETING)) {
                            final SimpleMeeting meeting = new SimpleMeeting();
                            meeting.setType(CrmTypeConstants.ACCOUNT);
                            meeting.setTypeid(view.getItem().getId());
                            EventBusFactory.getInstance().post(new ActivityEvent.MeetingEdit(LeadReadPresenter.this, meeting));
                        } else if (itemId.equals(CrmTypeConstants.CALL)) {
                            final SimpleCall call = new SimpleCall();
                            call.setType(CrmTypeConstants.ACCOUNT);
                            call.setTypeid(view.getItem().getId());
                            EventBusFactory.getInstance().post(new ActivityEvent.CallEdit(LeadReadPresenter.this, call));
                        }
                    }
                });
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        if (AppContext.canRead(RolePermissionCollections.CRM_LEAD)) {

            if (data.getParams() instanceof Integer) {
                LeadService leadService = ApplicationContextUtil.getSpringBean(LeadService.class);
                SimpleLead lead = leadService.findById((Integer) data.getParams(), AppContext.getAccountId());
                if (lead != null) {
                    view.previewItem(lead);
                    super.onGo(container, data);

                    AppContext.addFragment(CrmLinkGenerator.generateLeadPreviewLink(lead.getId()),
                            AppContext.getMessage(GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE,
                                    AppContext.getMessage(LeadI18nEnum.SINGLE), lead.getLeadName()));

                } else {
                    NotificationUtil.showRecordNotExistNotification();
                }

            }
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }

}
