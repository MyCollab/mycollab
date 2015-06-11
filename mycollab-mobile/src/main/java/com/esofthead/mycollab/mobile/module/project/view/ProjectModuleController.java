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
package com.esofthead.mycollab.mobile.module.project.view;

import com.esofthead.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.esofthead.mycollab.configuration.PasswordEncryptHelper;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.utils.BeanUtility;
import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.MobileApplication;
import com.esofthead.mycollab.mobile.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.mobile.module.project.events.*;
import com.esofthead.mycollab.mobile.module.project.events.ProjectEvent.AllActivities;
import com.esofthead.mycollab.mobile.module.project.events.TaskEvent.GoInsideList;
import com.esofthead.mycollab.mobile.module.project.view.bug.BugPresenter;
import com.esofthead.mycollab.mobile.module.project.view.message.MessagePresenter;
import com.esofthead.mycollab.mobile.module.project.view.milestone.MilestonePresenter;
import com.esofthead.mycollab.mobile.module.project.view.parameters.*;
import com.esofthead.mycollab.mobile.module.project.view.settings.ProjectUserPresenter;
import com.esofthead.mycollab.mobile.module.project.view.task.TaskPresenter;
import com.esofthead.mycollab.module.project.ProjectMemberStatusConstants;
import com.esofthead.mycollab.module.project.domain.SimpleMilestone;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.domain.SimpleTaskList;
import com.esofthead.mycollab.module.project.domain.criteria.MessageSearchCriteria;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectSearchCriteria;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.esofthead.mycollab.module.user.domain.SimpleBillingAccount;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.domain.UserPreference;
import com.esofthead.mycollab.module.user.service.BillingAccountService;
import com.esofthead.mycollab.module.user.service.UserPreferenceService;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractController;
import com.esofthead.mycollab.vaadin.mvp.PageActionChain;
import com.esofthead.mycollab.vaadin.mvp.PresenterResolver;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.vaadin.mobilecomponent.MobileNavigationManager;
import com.google.common.eventbus.Subscribe;
import com.vaadin.addon.touchkit.extensions.LocalStorage;
import com.vaadin.ui.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author MyCollab Inc.
 * @since 4.3.1
 */
public class ProjectModuleController extends AbstractController {
    private static final long serialVersionUID = 8999456416358169209L;

    private final MobileNavigationManager navManager;

    private static final Logger LOG = LoggerFactory.getLogger(ProjectModuleController.class);

    public ProjectModuleController(MobileNavigationManager navigationManager) {
        this.navManager = navigationManager;

        bindProjectEvents();
        bindBugEvents();
        bindMessageEvents();
        bindMilestoneEvents();
        bindTaskEvents();
        bindMemberEvents();
    }

    private void bindProjectEvents() {
        this.register(new ApplicationEventListener<ProjectEvent.GotoLogin>() {

            private static final long serialVersionUID = -3978301997191156254L;

            @Subscribe
            @Override
            public void handle(ProjectEvent.GotoLogin event) {
                ProjectLoginPresenter presenter = PresenterResolver
                        .getPresenter(ProjectLoginPresenter.class);
                presenter.go(navManager, null);
            }
        });

        this.register(new ApplicationEventListener<ProjectEvent.PlainLogin>() {

            private static final long serialVersionUID = 7930156079489701720L;

            @Subscribe
            @Override
            public void handle(ProjectEvent.PlainLogin event) {
                String[] data = (String[]) event.getData();
                try {
                    doLogin(data[0], data[1], Boolean.valueOf(data[2]));
                } catch (MyCollabException exception) {
                    EventBusFactory.getInstance().post(
                            new ProjectEvent.GotoLogin(this, null));
                }
            }
        });

        this.register(new ApplicationEventListener<ProjectEvent.GotoProjectList>() {

            private static final long serialVersionUID = -9006615798118115613L;

            @Subscribe
            @Override
            public void handle(ProjectEvent.GotoProjectList event) {
                ProjectListPresenter presenter = PresenterResolver.getPresenter(ProjectListPresenter.class);
                ProjectSearchCriteria criteria = new ProjectSearchCriteria();
                criteria.setInvolvedMember(new StringSearchField(AppContext.getUsername()));
                criteria.setProjectStatuses(new SetSearchField<>(StatusI18nEnum.Open.name()));
                presenter.go(navManager, new ScreenData.Search<>(criteria));
            }
        });
        this.register(new ApplicationEventListener<ProjectEvent.GotoMyProject>() {
            private static final long serialVersionUID = 2554670937118159116L;

            @Subscribe
            @Override
            public void handle(ProjectEvent.GotoMyProject event) {
                ProjectViewPresenter presenter = PresenterResolver.getPresenter(ProjectViewPresenter.class);
                presenter.handleChain(navManager, (PageActionChain) event.getData());
            }
        });
        this.register(new ApplicationEventListener<ProjectEvent.AllActivities>() {

            private static final long serialVersionUID = 2264166446431876916L;

            @Subscribe
            @Override
            public void handle(AllActivities event) {
                AllActivityStreamPresenter presenter = PresenterResolver
                        .getPresenter(AllActivityStreamPresenter.class);
                presenter.go(navManager,
                        (ProjectScreenData.AllActivities) event.getData());
            }

        });
        this.register(new ApplicationEventListener<ProjectEvent.MyProjectActivities>() {
            private static final long serialVersionUID = -1636495590108668932L;

            @Subscribe
            @Override
            public void handle(ProjectEvent.MyProjectActivities event) {
                ProjectActivityStreamPresenter presenter = PresenterResolver
                        .getPresenter(ProjectActivityStreamPresenter.class);
                presenter.go(navManager,
                        (ProjectScreenData.ViewActivities) event.getData());
            }
        });
    }

    private void bindBugEvents() {
        this.register(new ApplicationEventListener<BugEvent.GotoList>() {

            private static final long serialVersionUID = 4076325106652853379L;

            @Subscribe
            @Override
            public void handle(BugEvent.GotoList event) {
                Object params = event.getData();
                BugPresenter presenter = PresenterResolver.getPresenter(BugPresenter.class);
                if (params == null) {
                    BugSearchCriteria criteria = new BugSearchCriteria();

                    criteria.setProjectId(new NumberSearchField(
                            SearchField.AND, CurrentProjectVariables
                            .getProjectId()));
                    BugFilterParameter parameter = new BugFilterParameter("Open Bugs", criteria);
                    presenter.go(navManager, new BugScreenData.Search(parameter));
                } else if (params instanceof BugScreenData.Search) {
                    presenter.go(navManager, (BugScreenData.Search) params);
                } else {
                    throw new MyCollabException("Invalid search parameter: "
                            + BeanUtility.printBeanObj(params));
                }
            }

        });
        this.register(new ApplicationEventListener<BugEvent.GotoRead>() {
            private static final long serialVersionUID = -7074159798708000630L;

            @Subscribe
            @Override
            public void handle(BugEvent.GotoRead event) {
                BugScreenData.Read data = new BugScreenData.Read(
                        (Integer) event.getData());
                BugPresenter presenter = PresenterResolver
                        .getPresenter(BugPresenter.class);
                presenter.go(navManager, data);
            }
        });
        this.register(new ApplicationEventListener<BugEvent.GotoAdd>() {
            private static final long serialVersionUID = 4397575911576124923L;

            @Subscribe
            @Override
            public void handle(BugEvent.GotoAdd event) {
                BugScreenData.Add data = new BugScreenData.Add(new SimpleBug());
                BugPresenter presenter = PresenterResolver
                        .getPresenter(BugPresenter.class);
                presenter.go(navManager, data);
            }
        });

        this.register(new ApplicationEventListener<BugEvent.GotoEdit>() {
            private static final long serialVersionUID = 1211187276122156430L;

            @Subscribe
            @Override
            public void handle(BugEvent.GotoEdit event) {
                BugScreenData.Edit data = new BugScreenData.Edit(
                        (SimpleBug) event.getData());
                BugPresenter presenter = PresenterResolver
                        .getPresenter(BugPresenter.class);
                presenter.go(navManager, data);
            }
        });
    }

    private void bindMessageEvents() {
        this.register(new ApplicationEventListener<MessageEvent.GotoAdd>() {

            private static final long serialVersionUID = -1424602419540601233L;

            @Subscribe
            @Override
            public void handle(MessageEvent.GotoAdd event) {
                MessageScreenData.Add data = new MessageScreenData.Add();
                MessagePresenter presenter = PresenterResolver
                        .getPresenter(MessagePresenter.class);
                presenter.go(navManager, data);
            }

        });
        this.register(new ApplicationEventListener<MessageEvent.GotoList>() {

            private static final long serialVersionUID = -5988814292947013329L;

            @Subscribe
            @Override
            public void handle(MessageEvent.GotoList event) {
                MessageSearchCriteria searchCriteria = new MessageSearchCriteria();
                searchCriteria.setProjectids(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
                MessageScreenData.Search data = new MessageScreenData.Search(
                        searchCriteria);
                MessagePresenter presenter = PresenterResolver
                        .getPresenter(MessagePresenter.class);
                presenter.go(navManager, data);
            }
        });

        this.register(new ApplicationEventListener<MessageEvent.GotoRead>() {

            private static final long serialVersionUID = 252671591979715270L;

            @Subscribe
            @Override
            public void handle(MessageEvent.GotoRead event) {
                MessageScreenData.Read data = new MessageScreenData.Read(
                        (Integer) event.getData());
                MessagePresenter presenter = PresenterResolver
                        .getPresenter(MessagePresenter.class);
                presenter.go(navManager, data);
            }
        });
    }

    private void bindMilestoneEvents() {
        this.register(new ApplicationEventListener<MilestoneEvent.GotoList>() {

            private static final long serialVersionUID = -4211546107827460336L;

            @Subscribe
            @Override
            public void handle(MilestoneEvent.GotoList event) {
                MilestoneScreenData.List data = new MilestoneScreenData.List();
                MilestonePresenter presenter = PresenterResolver
                        .getPresenter(MilestonePresenter.class);
                presenter.go(navManager, data);
            }
        });
        this.register(new ApplicationEventListener<MilestoneEvent.GotoRead>() {

            private static final long serialVersionUID = -1560535564437953348L;

            @Subscribe
            @Override
            public void handle(MilestoneEvent.GotoRead event) {
                MilestoneScreenData.Read data = new MilestoneScreenData.Read(
                        (Integer) event.getData());
                MilestonePresenter presenter = PresenterResolver
                        .getPresenter(MilestonePresenter.class);
                presenter.go(navManager, data);
            }
        });
        this.register(new ApplicationEventListener<MilestoneEvent.GotoAdd>() {

            private static final long serialVersionUID = 7789241658397524718L;

            @Subscribe
            @Override
            public void handle(MilestoneEvent.GotoAdd event) {
                MilestoneScreenData.Add data = new MilestoneScreenData.Add(
                        new SimpleMilestone());
                MilestonePresenter presenter = PresenterResolver
                        .getPresenter(MilestonePresenter.class);
                presenter.go(navManager, data);
            }
        });
        this.register(new ApplicationEventListener<MilestoneEvent.GotoEdit>() {

            private static final long serialVersionUID = 150839801764244450L;

            @Subscribe
            @Override
            public void handle(MilestoneEvent.GotoEdit event) {
                MilestoneScreenData.Edit data = new MilestoneScreenData.Edit(
                        (SimpleMilestone) event.getData());
                MilestonePresenter presenter = PresenterResolver
                        .getPresenter(MilestonePresenter.class);
                presenter.go(navManager, data);
            }
        });
    }

    private void bindTaskEvents() {
        this.register(new ApplicationEventListener<TaskEvent.GotoList>() {

            private static final long serialVersionUID = -4009103684129154556L;

            @Subscribe
            @Override
            public void handle(TaskEvent.GotoList event) {
                TaskGroupScreenData.List data = new TaskGroupScreenData.List();
                TaskPresenter presenter = PresenterResolver
                        .getPresenter(TaskPresenter.class);
                presenter.go(navManager, data);
            }

        });
        this.register(new ApplicationEventListener<TaskEvent.GoInsideList>() {

            private static final long serialVersionUID = 5272374413178583391L;

            @Subscribe
            @Override
            public void handle(GoInsideList event) {
                TaskScreenData.List data = new TaskScreenData.List(
                        (Integer) event.getData());
                TaskPresenter presenter = PresenterResolver
                        .getPresenter(TaskPresenter.class);
                presenter.go(navManager, data);
            }

        });
        this.register(new ApplicationEventListener<TaskEvent.GotoRead>() {

            private static final long serialVersionUID = -5438389231124986497L;

            @Subscribe
            @Override
            public void handle(TaskEvent.GotoRead event) {
                TaskScreenData.Read data = new TaskScreenData.Read(
                        (Integer) event.getData());
                TaskPresenter presenter = PresenterResolver
                        .getPresenter(TaskPresenter.class);
                presenter.go(navManager, data);
            }

        });
        this.register(new ApplicationEventListener<TaskEvent.GotoEdit>() {

            private static final long serialVersionUID = 1585508595654062937L;

            @Subscribe
            @Override
            public void handle(TaskEvent.GotoEdit event) {
                TaskScreenData.Edit data = new TaskScreenData.Edit(
                        (SimpleTask) event.getData());
                TaskPresenter presenter = PresenterResolver
                        .getPresenter(TaskPresenter.class);
                presenter.go(navManager, data);
            }

        });
        this.register(new ApplicationEventListener<TaskEvent.GotoAdd>() {

            private static final long serialVersionUID = -2205879182939668100L;

            @Subscribe
            @Override
            public void handle(TaskEvent.GotoAdd event) {
                TaskScreenData.Add data = new TaskScreenData.Add(
                        (Integer) event.getData());
                TaskPresenter presenter = PresenterResolver
                        .getPresenter(TaskPresenter.class);
                presenter.go(navManager, data);
            }

        });
        this.register(new ApplicationEventListener<TaskEvent.GotoListView>() {

            private static final long serialVersionUID = 8482472427144553994L;

            @Subscribe
            @Override
            public void handle(TaskEvent.GotoListView event) {
                TaskGroupScreenData.Read data = new TaskGroupScreenData.Read(
                        (Integer) event.getData());
                TaskPresenter presenter = PresenterResolver
                        .getPresenter(TaskPresenter.class);
                presenter.go(navManager, data);
            }

        });
        this.register(new ApplicationEventListener<TaskEvent.GotoListAdd>() {

            private static final long serialVersionUID = -3087445308221821731L;

            @Subscribe
            @Override
            public void handle(TaskEvent.GotoListAdd event) {
                SimpleTaskList taskList = new SimpleTaskList();
                taskList.setProjectid(CurrentProjectVariables.getProjectId());
                taskList.setStatus(StatusI18nEnum.Open.name());
                TaskGroupScreenData.Add data = new TaskGroupScreenData.Add(
                        taskList);
                TaskPresenter presenter = PresenterResolver
                        .getPresenter(TaskPresenter.class);
                presenter.go(navManager, data);
            }

        });
        this.register(new ApplicationEventListener<TaskEvent.GotoListEdit>() {

            private static final long serialVersionUID = -5526088325467106990L;

            @Subscribe
            @Override
            public void handle(TaskEvent.GotoListEdit event) {
                TaskGroupScreenData.Edit data = new TaskGroupScreenData.Edit(
                        (SimpleTaskList) event.getData());
                TaskPresenter presenter = PresenterResolver
                        .getPresenter(TaskPresenter.class);
                presenter.go(navManager, data);
            }

        });
    }

    private void bindMemberEvents() {
        this.register(new ApplicationEventListener<ProjectMemberEvent.GotoList>() {

            private static final long serialVersionUID = 976165067913682631L;

            @Subscribe
            @Override
            public void handle(ProjectMemberEvent.GotoList event) {
                ProjectMemberSearchCriteria criteria = new ProjectMemberSearchCriteria();
                criteria.setProjectId(new NumberSearchField(
                        CurrentProjectVariables.getProjectId()));
                criteria.setSaccountid(new NumberSearchField(AppContext
                        .getAccountId()));
                criteria.setStatus(new StringSearchField(
                        ProjectMemberStatusConstants.ACTIVE));
                ProjectUserPresenter presenter = PresenterResolver
                        .getPresenter(ProjectUserPresenter.class);
                presenter.go(navManager, new ProjectMemberScreenData.Search(
                        criteria));
            }
        });
        this.register(new ApplicationEventListener<ProjectMemberEvent.GotoRead>() {

            private static final long serialVersionUID = 1295333471939691673L;

            @Subscribe
            @Override
            public void handle(ProjectMemberEvent.GotoRead event) {
                ProjectMemberScreenData.Read data = new ProjectMemberScreenData.Read(
                        event.getData());
                ProjectUserPresenter presenter = PresenterResolver
                        .getPresenter(ProjectUserPresenter.class);
                presenter.go(navManager, data);
            }

        });
        this.register(new ApplicationEventListener<ProjectMemberEvent.GotoEdit>() {

            private static final long serialVersionUID = 819995009295850294L;

            @Subscribe
            @Override
            public void handle(ProjectMemberEvent.GotoEdit event) {
                ProjectMemberScreenData.Edit data = new ProjectMemberScreenData.Edit(
                        (SimpleProjectMember) event.getData());
                ProjectUserPresenter presenter = PresenterResolver
                        .getPresenter(ProjectUserPresenter.class);
                presenter.go(navManager, data);
            }

        });
        this.register(new ApplicationEventListener<ProjectMemberEvent.GotoInviteMembers>() {

            private static final long serialVersionUID = -5406170172080742351L;

            @Subscribe
            @Override
            public void handle(ProjectMemberEvent.GotoInviteMembers event) {
                ProjectMemberScreenData.InviteProjectMembers data = new ProjectMemberScreenData.InviteProjectMembers();
                ProjectUserPresenter presenter = PresenterResolver
                        .getPresenter(ProjectUserPresenter.class);
                presenter.go(navManager, data);
            }

        });
    }

    public static void doLogin(String username, String password,
                               boolean isRememberPassword) throws MyCollabException {
        UserService userService = ApplicationContextUtil
                .getSpringBean(UserService.class);
        SimpleUser user = userService.authentication(username, password,
                AppContext.getSubDomain(), false);

        BillingAccountService billingAccountService = ApplicationContextUtil
                .getSpringBean(BillingAccountService.class);

        SimpleBillingAccount billingAccount = billingAccountService
                .getBillingAccountById(AppContext.getAccountId());

        LOG.debug("Get billing account successfully: "
                + BeanUtility.printBeanObj(billingAccount));

        UserPreferenceService preferenceService = ApplicationContextUtil
                .getSpringBean(UserPreferenceService.class);
        UserPreference pref = preferenceService.getPreferenceOfUser(username,
                AppContext.getAccountId());

        LOG.debug("Login to system successfully. Save user and preference "
                + pref + " to session");

        if (isRememberPassword) {
            LocalStorage storage = LocalStorage.get();
            String storeVal = username + "$"
                    + PasswordEncryptHelper.encryptText(password);
            storage.put(MobileApplication.LOGIN_DATA, storeVal);
        }

        AppContext.getInstance().setSessionVariables(user, pref, billingAccount);
        pref.setLastaccessedtime(new Date());
        preferenceService.updateWithSession(pref, AppContext.getUsername());
        EventBusFactory.getInstance().post(
                new ProjectEvent.GotoProjectList(UI.getCurrent(), null));
    }

}
