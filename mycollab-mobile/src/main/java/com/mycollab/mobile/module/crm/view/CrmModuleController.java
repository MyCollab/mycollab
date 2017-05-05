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
package com.mycollab.mobile.module.crm.view;

import com.google.common.eventbus.Subscribe;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.eventmanager.ApplicationEventListener;
import com.mycollab.mobile.module.crm.CrmModuleScreenData;
import com.mycollab.mobile.module.crm.events.*;
import com.mycollab.mobile.module.crm.events.ActivityEvent.CallEdit;
import com.mycollab.mobile.module.crm.ui.CrmRelatedItemsScreenData;
import com.mycollab.mobile.module.crm.view.account.AccountListPresenter;
import com.mycollab.mobile.module.crm.view.account.AccountReadPresenter;
import com.mycollab.mobile.module.crm.view.account.IAccountAddPresenter;
import com.mycollab.mobile.module.crm.view.activity.*;
import com.mycollab.mobile.module.crm.view.campaign.CampaignListPresenter;
import com.mycollab.mobile.module.crm.view.campaign.CampaignReadPresenter;
import com.mycollab.mobile.module.crm.view.campaign.ICampaignAddPresenter;
import com.mycollab.mobile.module.crm.view.cases.CaseListPresenter;
import com.mycollab.mobile.module.crm.view.cases.CaseReadPresenter;
import com.mycollab.mobile.module.crm.view.cases.ICaseAddPresenter;
import com.mycollab.mobile.module.crm.view.contact.ContactListPresenter;
import com.mycollab.mobile.module.crm.view.contact.ContactReadPresenter;
import com.mycollab.mobile.module.crm.view.contact.IContactAddPresenter;
import com.mycollab.mobile.module.crm.view.lead.ILeadAddPresenter;
import com.mycollab.mobile.module.crm.view.lead.LeadListPresenter;
import com.mycollab.mobile.module.crm.view.lead.LeadReadPresenter;
import com.mycollab.mobile.module.crm.view.opportunity.IOpportunityAddPresenter;
import com.mycollab.mobile.module.crm.view.opportunity.OpportunityListPresenter;
import com.mycollab.mobile.module.crm.view.opportunity.OpportunityReadPresenter;
import com.mycollab.mobile.mvp.view.PresenterOptionUtil;
import com.mycollab.module.crm.domain.*;
import com.mycollab.module.crm.domain.criteria.*;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.mvp.AbstractController;
import com.mycollab.vaadin.mvp.IPresenter;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.mvp.ScreenData;
import com.vaadin.addon.touchkit.ui.NavigationManager;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
public class CrmModuleController extends AbstractController {
    private static final long serialVersionUID = 6995176903239247669L;
    final private NavigationManager crmViewNavigation;

    public CrmModuleController(NavigationManager navigationManager) {
        this.crmViewNavigation = navigationManager;

        bindCrmEvents();
        bindAccountEvents();
        bindActivityEvents();
        bindContactEvents();
        bindCampaignEvents();
        bindCaseEvents();
        bindLeadEvents();
        bindOpportunityEvents();
    }

    private void bindCrmEvents() {
        this.register(new ApplicationEventListener<CrmEvent.GotoActivitiesView>() {

            private static final long serialVersionUID = -3626315180394209108L;

            @Subscribe
            @Override
            public void handle(CrmEvent.GotoActivitiesView event) {
                AllActivitiesPresenter presenter = PresenterResolver.getPresenter(AllActivitiesPresenter.class);
                presenter.go(crmViewNavigation, (CrmModuleScreenData.GotoModule) event.getData());
            }
        });
    }

    private void bindAccountEvents() {
        this.register(new ApplicationEventListener<AccountEvent.GotoList>() {
            private static final long serialVersionUID = -3451799893080539849L;

            @Subscribe
            @Override
            public void handle(AccountEvent.GotoList event) {
                AccountListPresenter presenter = PresenterResolver.getPresenter(AccountListPresenter.class);
                AccountSearchCriteria criteria = new AccountSearchCriteria();
                criteria.setSaccountid(new NumberSearchField(MyCollabUI.getAccountId()));
                presenter.go(crmViewNavigation, new ScreenData.Search<>(criteria));
            }

        });

        this.register(new ApplicationEventListener<AccountEvent.GotoAdd>() {
            private static final long serialVersionUID = -3309942209489453346L;

            @Subscribe
            @Override
            public void handle(AccountEvent.GotoAdd event) {
                IPresenter presenter = PresenterOptionUtil.getPresenter(IAccountAddPresenter.class);
                presenter.go(crmViewNavigation, new ScreenData.Add<>(new SimpleAccount()));
            }
        });

        this.register(new ApplicationEventListener<AccountEvent.GotoEdit>() {
            private static final long serialVersionUID = 5328513173395719936L;

            @Subscribe
            @Override
            public void handle(AccountEvent.GotoEdit event) {
                IPresenter presenter = PresenterOptionUtil.getPresenter(IAccountAddPresenter.class);
                presenter.go(crmViewNavigation, new ScreenData.Edit<>(event.getData()));
            }
        });

        this.register(new ApplicationEventListener<AccountEvent.GotoRead>() {
            private static final long serialVersionUID = -5805283303669877715L;

            @Subscribe
            @Override
            public void handle(AccountEvent.GotoRead event) {
                AccountReadPresenter presenter = PresenterResolver.getPresenter(AccountReadPresenter.class);
                presenter.go(crmViewNavigation, new ScreenData.Preview(event.getData()));
            }
        });

        this.register(new ApplicationEventListener<AccountEvent.GotoRelatedItems>() {
            private static final long serialVersionUID = 259904372741221966L;

            @Subscribe
            @Override
            public void handle(AccountEvent.GotoRelatedItems event) {
                if (event.getData() instanceof CrmRelatedItemsScreenData)
                    crmViewNavigation.navigateTo(((CrmRelatedItemsScreenData) event.getData()).getParams());
            }
        });
    }

    private void bindActivityEvents() {
        this.register(new ApplicationEventListener<ActivityEvent.GotoList>() {
            private static final long serialVersionUID = 6101515891859134103L;

            @Subscribe
            @Override
            public void handle(ActivityEvent.GotoList event) {
                ActivityListPresenter presenter = PresenterResolver.getPresenter(ActivityListPresenter.class);
                ActivitySearchCriteria criteria = new ActivitySearchCriteria();
                criteria.setSaccountid(new NumberSearchField(MyCollabUI.getAccountId()));
                presenter.go(crmViewNavigation, new ScreenData.Search<>(criteria));
            }
        });
        this.register(new ApplicationEventListener<ActivityEvent.TaskRead>() {
            private static final long serialVersionUID = -3723195748802950651L;

            @Subscribe
            @Override
            public void handle(ActivityEvent.TaskRead event) {
                AssignmentReadPresenter presenter = PresenterResolver.getPresenter(AssignmentReadPresenter.class);
                presenter.go(crmViewNavigation, new ScreenData.Preview(event.getData()));
            }
        });

        this.register(new ApplicationEventListener<ActivityEvent.TaskAdd>() {
            private static final long serialVersionUID = -670224728085519779L;

            @Subscribe
            @Override
            public void handle(ActivityEvent.TaskAdd event) {
                AssignmentAddPresenter presenter = PresenterResolver.getPresenter(AssignmentAddPresenter.class);
                presenter.go(crmViewNavigation, new ScreenData.Add<>(new SimpleCrmTask()));
            }
        });

        this.register(new ApplicationEventListener<ActivityEvent.TaskEdit>() {
            private static final long serialVersionUID = -670224728085519779L;

            @Subscribe
            @Override
            public void handle(ActivityEvent.TaskEdit event) {
                AssignmentAddPresenter presenter = PresenterResolver.getPresenter(AssignmentAddPresenter.class);
                presenter.go(crmViewNavigation, new ScreenData.Edit<>(event.getData()));
            }
        });

        this.register(new ApplicationEventListener<ActivityEvent.CallRead>() {
            private static final long serialVersionUID = -3723195748802950651L;

            @Subscribe
            @SuppressWarnings({"unchecked", "rawtypes"})
            @Override
            public void handle(ActivityEvent.CallRead event) {
                CallReadPresenter presenter = PresenterResolver.getPresenter(CallReadPresenter.class);
                presenter.go(crmViewNavigation, new ScreenData.Preview(event.getData()));
            }
        });

        this.register(new ApplicationEventListener<ActivityEvent.CallAdd>() {
            private static final long serialVersionUID = 8759218728614616964L;

            @Subscribe
            @Override
            public void handle(ActivityEvent.CallAdd event) {
                CallAddPresenter presenter = PresenterResolver.getPresenter(CallAddPresenter.class);
                presenter.go(crmViewNavigation, new ScreenData.Add<>(new SimpleCall()));
            }
        });

        this.register(new ApplicationEventListener<ActivityEvent.CallEdit>() {
            private static final long serialVersionUID = -5416887922292705051L;

            @Subscribe
            @Override
            public void handle(CallEdit event) {
                CallAddPresenter presenter = PresenterResolver.getPresenter(CallAddPresenter.class);
                presenter.go(crmViewNavigation, new ScreenData.Edit<>(event.getData()));
            }
        });

        this.register(new ApplicationEventListener<ActivityEvent.MeetingRead>() {
            private static final long serialVersionUID = -3723195748802950651L;

            @Subscribe
            @Override
            public void handle(ActivityEvent.MeetingRead event) {
                MeetingReadPresenter presenter = PresenterResolver.getPresenter(MeetingReadPresenter.class);
                presenter.go(crmViewNavigation, new ScreenData.Preview(event.getData()));
            }
        });

        this.register(new ApplicationEventListener<ActivityEvent.MeetingAdd>() {
            private static final long serialVersionUID = -7369637977421183110L;

            @Subscribe
            @Override
            public void handle(ActivityEvent.MeetingAdd event) {
                MeetingAddPresenter presenter = PresenterResolver.getPresenter(MeetingAddPresenter.class);
                presenter.go(crmViewNavigation, new ScreenData.Add<>(new SimpleMeeting()));
            }
        });

        this.register(new ApplicationEventListener<ActivityEvent.MeetingEdit>() {
            private static final long serialVersionUID = 1784955912645269021L;

            @Subscribe
            @Override
            public void handle(ActivityEvent.MeetingEdit event) {
                MeetingAddPresenter presenter = PresenterResolver.getPresenter(MeetingAddPresenter.class);
                presenter.go(crmViewNavigation, new ScreenData.Edit<>(event.getData()));
            }
        });

        this.register(new ApplicationEventListener<ActivityEvent.GotoRelatedItems>() {
            private static final long serialVersionUID = -2245568910777045010L;

            @Subscribe
            @Override
            public void handle(ActivityEvent.GotoRelatedItems event) {
                if (event.getData() instanceof CrmRelatedItemsScreenData) {
                    crmViewNavigation.navigateTo(((CrmRelatedItemsScreenData) event.getData()).getParams());
                }
            }
        });
    }

    private void bindContactEvents() {
        this.register(new ApplicationEventListener<ContactEvent.GotoList>() {
            private static final long serialVersionUID = 3327061919614830145L;

            @Subscribe
            @Override
            public void handle(ContactEvent.GotoList event) {
                ContactListPresenter presenter = PresenterResolver.getPresenter(ContactListPresenter.class);

                ContactSearchCriteria searchCriteria = new ContactSearchCriteria();
                searchCriteria.setSaccountid(new NumberSearchField(MyCollabUI.getAccountId()));
                presenter.go(crmViewNavigation, new ScreenData.Search<>(searchCriteria));
            }
        });

        this.register(new ApplicationEventListener<ContactEvent.GotoAdd>() {
            private static final long serialVersionUID = -9082569633338794831L;

            @Subscribe
            @Override
            public void handle(ContactEvent.GotoAdd event) {
                IPresenter presenter = PresenterOptionUtil.getPresenter(IContactAddPresenter.class);
                presenter.go(crmViewNavigation, new ScreenData.Add<>(new SimpleContact()));
            }
        });

        this.register(new ApplicationEventListener<ContactEvent.GotoEdit>() {
            private static final long serialVersionUID = 1465740039647654585L;

            @Subscribe
            @Override
            public void handle(ContactEvent.GotoEdit event) {
                IPresenter presenter = PresenterOptionUtil.getPresenter(IContactAddPresenter.class);
                presenter.go(crmViewNavigation, new ScreenData.Edit<>(event.getData()));
            }
        });

        this.register(new ApplicationEventListener<ContactEvent.GotoRead>() {
            private static final long serialVersionUID = -5099988781106338890L;

            @Subscribe
            @Override
            public void handle(ContactEvent.GotoRead event) {
                ContactReadPresenter presenter = PresenterResolver.getPresenter(ContactReadPresenter.class);
                presenter.go(crmViewNavigation, new ScreenData.Preview(event.getData()));
            }
        });

        this.register(new ApplicationEventListener<ContactEvent.GotoRelatedItems>() {
            private static final long serialVersionUID = -8341031306697617759L;

            @Subscribe
            @Override
            public void handle(ContactEvent.GotoRelatedItems event) {
                if (event.getData() instanceof CrmRelatedItemsScreenData)
                    crmViewNavigation.navigateTo(((CrmRelatedItemsScreenData) event.getData()).getParams());
            }
        });
    }

    private void bindCampaignEvents() {
        this.register(new ApplicationEventListener<CampaignEvent.GotoList>() {
            private static final long serialVersionUID = 1553727404269228168L;

            @Subscribe
            @Override
            public void handle(CampaignEvent.GotoList event) {
                CampaignListPresenter presenter = PresenterResolver.getPresenter(CampaignListPresenter.class);
                CampaignSearchCriteria searchCriteria = new CampaignSearchCriteria();
                searchCriteria.setSaccountid(new NumberSearchField(MyCollabUI.getAccountId()));

                presenter.go(crmViewNavigation, new ScreenData.Search<>(searchCriteria));
            }
        });

        this.register(new ApplicationEventListener<CampaignEvent.GotoAdd>() {
            private static final long serialVersionUID = 1240143124315010237L;

            @Subscribe
            @Override
            public void handle(CampaignEvent.GotoAdd event) {
                IPresenter presenter = PresenterOptionUtil.getPresenter(ICampaignAddPresenter.class);
                presenter.go(crmViewNavigation, new ScreenData.Add<>(new SimpleCampaign()));
            }
        });

        this.register(new ApplicationEventListener<CampaignEvent.GotoEdit>() {
            private static final long serialVersionUID = 7877885891797325699L;

            @Subscribe
            @Override
            public void handle(CampaignEvent.GotoEdit event) {
                IPresenter presenter = PresenterOptionUtil.getPresenter(ICampaignAddPresenter.class);
                presenter.go(crmViewNavigation, new ScreenData.Edit<>(event.getData()));
            }
        });

        this.register(new ApplicationEventListener<CampaignEvent.GotoRead>() {
            private static final long serialVersionUID = -9221302504462965422L;

            @Subscribe
            @Override
            public void handle(CampaignEvent.GotoRead event) {
                CampaignReadPresenter presenter = PresenterResolver.getPresenter(CampaignReadPresenter.class);
                presenter.go(crmViewNavigation, new ScreenData.Preview(event.getData()));
            }
        });

        this.register(new ApplicationEventListener<CampaignEvent.GotoRelatedItems>() {
            private static final long serialVersionUID = -1867638793934682142L;

            @Subscribe
            @Override
            public void handle(CampaignEvent.GotoRelatedItems event) {
                if (event.getData() instanceof CrmRelatedItemsScreenData)
                    crmViewNavigation.navigateTo(((CrmRelatedItemsScreenData) event.getData()).getParams());
            }
        });
    }

    private void bindCaseEvents() {
        this.register(new ApplicationEventListener<CaseEvent.GotoList>() {
            private static final long serialVersionUID = -3618797051826954301L;

            @Subscribe
            @Override
            public void handle(CaseEvent.GotoList event) {
                CaseListPresenter presenter = PresenterResolver.getPresenter(CaseListPresenter.class);

                CaseSearchCriteria searchCriteria = new CaseSearchCriteria();
                searchCriteria.setSaccountid(new NumberSearchField(MyCollabUI.getAccountId()));
                presenter.go(crmViewNavigation, new ScreenData.Search<>(searchCriteria));
            }
        });

        this.register(new ApplicationEventListener<CaseEvent.GotoAdd>() {
            private static final long serialVersionUID = 1735667150147480819L;

            @Subscribe
            @Override
            public void handle(CaseEvent.GotoAdd event) {
                IPresenter presenter = PresenterResolver.getPresenter(ICaseAddPresenter.class);
                presenter.go(crmViewNavigation, new ScreenData.Add<>(new SimpleCase()));
            }
        });

        this.register(new ApplicationEventListener<CaseEvent.GotoEdit>() {
            private static final long serialVersionUID = 2353880791537378472L;

            @Subscribe
            @Override
            public void handle(CaseEvent.GotoEdit event) {
                IPresenter presenter = PresenterResolver.getPresenter(ICaseAddPresenter.class);
                presenter.go(crmViewNavigation, new ScreenData.Edit<>(event.getData()));
            }
        });

        this.register(new ApplicationEventListener<CaseEvent.GotoRead>() {
            private static final long serialVersionUID = -5491126759925853548L;

            @Subscribe
            @Override
            public void handle(CaseEvent.GotoRead event) {
                CaseReadPresenter presenter = PresenterResolver.getPresenter(CaseReadPresenter.class);
                presenter.go(crmViewNavigation, new ScreenData.Preview(event.getData()));
            }
        });

        this.register(new ApplicationEventListener<CaseEvent.GotoRelatedItems>() {
            private static final long serialVersionUID = 1019540906038925888L;

            @Subscribe
            @Override
            public void handle(CaseEvent.GotoRelatedItems event) {
                if (event.getData() instanceof CrmRelatedItemsScreenData)
                    crmViewNavigation.navigateTo(((CrmRelatedItemsScreenData) event.getData()).getParams());
            }
        });
    }

    private void bindLeadEvents() {
        this.register(new ApplicationEventListener<LeadEvent.GotoList>() {
            private static final long serialVersionUID = 9037270302083265873L;

            @Subscribe
            @Override
            public void handle(LeadEvent.GotoList event) {
                LeadListPresenter presenter = PresenterResolver.getPresenter(LeadListPresenter.class);
                LeadSearchCriteria searchCriteria = new LeadSearchCriteria();
                searchCriteria.setSaccountid(new NumberSearchField(MyCollabUI.getAccountId()));
                presenter.go(crmViewNavigation, new ScreenData.Search<>(searchCriteria));
            }
        });

        this.register(new ApplicationEventListener<LeadEvent.GotoRead>() {
            private static final long serialVersionUID = 9113847281543934181L;

            @Subscribe
            @Override
            public void handle(LeadEvent.GotoRead event) {
                LeadReadPresenter presenter = PresenterResolver.getPresenter(LeadReadPresenter.class);
                presenter.go(crmViewNavigation, new ScreenData.Preview(event.getData()));
            }
        });

        this.register(new ApplicationEventListener<LeadEvent.GotoAdd>() {
            private static final long serialVersionUID = -211740720642515595L;

            @Subscribe
            @Override
            public void handle(LeadEvent.GotoAdd event) {
                IPresenter presenter = PresenterOptionUtil.getPresenter(ILeadAddPresenter.class);
                presenter.go(crmViewNavigation, new ScreenData.Add<>(new SimpleLead()));
            }
        });

        this.register(new ApplicationEventListener<LeadEvent.GotoEdit>() {
            private static final long serialVersionUID = 915856771120600013L;

            @Subscribe
            @Override
            public void handle(LeadEvent.GotoEdit event) {
                IPresenter presenter = PresenterOptionUtil.getPresenter(ILeadAddPresenter.class);
                presenter.go(crmViewNavigation, new ScreenData.Edit<>(event.getData()));
            }
        });

        this.register(new ApplicationEventListener<LeadEvent.GotoRelatedItems>() {
            private static final long serialVersionUID = -1655170606113750709L;

            @Subscribe
            @Override
            public void handle(LeadEvent.GotoRelatedItems event) {
                if (event.getData() instanceof CrmRelatedItemsScreenData)
                    crmViewNavigation.navigateTo(((CrmRelatedItemsScreenData) event.getData()).getParams());
            }
        });
    }

    private void bindOpportunityEvents() {
        this.register(new ApplicationEventListener<OpportunityEvent.GotoList>() {
            private static final long serialVersionUID = -2575430958965270606L;

            @Subscribe
            @Override
            public void handle(OpportunityEvent.GotoList event) {
                OpportunityListPresenter presenter = PresenterResolver.getPresenter(OpportunityListPresenter.class);
                OpportunitySearchCriteria searchCriteria = new OpportunitySearchCriteria();
                searchCriteria.setSaccountid(new NumberSearchField(MyCollabUI.getAccountId()));
                presenter.go(crmViewNavigation, new ScreenData.Search<>(searchCriteria));

            }
        });

        this.register(new ApplicationEventListener<OpportunityEvent.GotoRead>() {
            private static final long serialVersionUID = -4783961655267073679L;

            @Subscribe
            @Override
            public void handle(OpportunityEvent.GotoRead event) {
                OpportunityReadPresenter presenter = PresenterResolver.getPresenter(OpportunityReadPresenter.class);
                presenter.go(crmViewNavigation, new ScreenData.Preview(event.getData()));
            }
        });

        this.register(new ApplicationEventListener<OpportunityEvent.GotoAdd>() {
            private static final long serialVersionUID = -1102539216312225338L;

            @Subscribe
            @Override
            public void handle(OpportunityEvent.GotoAdd event) {
                IPresenter presenter = PresenterResolver.getPresenter(IOpportunityAddPresenter.class);
                presenter.go(crmViewNavigation, new ScreenData.Add<>(new SimpleOpportunity()));
            }
        });

        this.register(new ApplicationEventListener<OpportunityEvent.GotoEdit>() {
            private static final long serialVersionUID = -5752644127546011966L;

            @Subscribe
            @Override
            public void handle(OpportunityEvent.GotoEdit event) {
                IPresenter presenter = PresenterResolver.getPresenter(IOpportunityAddPresenter.class);
                presenter.go(crmViewNavigation, new ScreenData.Edit<>(event.getData()));
            }
        });

        this.register(new ApplicationEventListener<OpportunityEvent.GotoRelatedItems>() {
            private static final long serialVersionUID = 2389909957063829985L;

            @Subscribe
            @Override
            public void handle(OpportunityEvent.GotoRelatedItems event) {
                if (event.getData() instanceof CrmRelatedItemsScreenData)
                    crmViewNavigation.navigateTo(((CrmRelatedItemsScreenData) event.getData()).getParams());
            }
        });
    }
}
