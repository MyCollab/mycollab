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

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.esofthead.mycollab.configuration.PasswordEncryptHelper;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.utils.BeanUtility;
import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.MobileApplication;
import com.esofthead.mycollab.mobile.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.mobile.module.project.events.MessageEvent;
import com.esofthead.mycollab.mobile.module.project.events.ProjectEvent;
import com.esofthead.mycollab.mobile.module.project.events.TaskEvent;
import com.esofthead.mycollab.mobile.module.project.events.TaskEvent.GoInsideList;
import com.esofthead.mycollab.mobile.module.project.view.message.MessagePresenter;
import com.esofthead.mycollab.mobile.module.project.view.parameters.MessageScreenData;
import com.esofthead.mycollab.mobile.module.project.view.parameters.TaskGroupScreenData;
import com.esofthead.mycollab.mobile.module.project.view.parameters.TaskScreenData;
import com.esofthead.mycollab.mobile.module.project.view.task.TaskPresenter;
import com.esofthead.mycollab.module.project.domain.criteria.MessageSearchCriteria;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectSearchCriteria;
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

/**
 * @author MyCollab Inc.
 * 
 * @since 4.3.1
 */
public class ProjectModuleController extends AbstractController {

	private static final long serialVersionUID = 8999456416358169209L;

	private final MobileNavigationManager navManager;

	private static Logger log = LoggerFactory
			.getLogger(ProjectModuleController.class);

	public ProjectModuleController(MobileNavigationManager navigationManager) {
		this.navManager = navigationManager;

		bindProjectEvents();
		bindMessageEvents();
		bindTaskEvents();
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
				ProjectListPresenter presenter = PresenterResolver
						.getPresenter(ProjectListPresenter.class);
				ProjectSearchCriteria criteria = new ProjectSearchCriteria();
				criteria.setInvolvedMember(new StringSearchField(AppContext
						.getUsername()));
				criteria.setProjectStatuses(new SetSearchField<String>(
						new String[] { StatusI18nEnum.Open.name() }));
				presenter.go(navManager,
						new ScreenData.Search<ProjectSearchCriteria>(criteria));
			}
		});
		this.register(new ApplicationEventListener<ProjectEvent.GotoMyProject>() {
			private static final long serialVersionUID = 2554670937118159116L;

			@Subscribe
			@Override
			public void handle(ProjectEvent.GotoMyProject event) {
				ProjectViewPresenter presenter = PresenterResolver
						.getPresenter(ProjectViewPresenter.class);
				presenter.handleChain(navManager,
						(PageActionChain) event.getData());
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
				searchCriteria.setProjectids(new SetSearchField<Integer>(
						CurrentProjectVariables.getProjectId()));
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

		log.debug("Get billing account successfully: "
				+ BeanUtility.printBeanObj(billingAccount));

		UserPreferenceService preferenceService = ApplicationContextUtil
				.getSpringBean(UserPreferenceService.class);
		UserPreference pref = preferenceService.getPreferenceOfUser(username,
				AppContext.getAccountId());

		log.debug("Login to system successfully. Save user and preference "
				+ pref + " to session");

		if (isRememberPassword) {
			LocalStorage storage = LocalStorage.get();
			String storeVal = username + "$"
					+ PasswordEncryptHelper.encyptText(password);
			storage.put(MobileApplication.LOGIN_DATA, storeVal);
		}

		AppContext.getInstance().setSession(user, pref, billingAccount);
		pref.setLastaccessedtime(new Date());
		preferenceService.updateWithSession(pref, AppContext.getUsername());
		EventBusFactory.getInstance().post(
				new ProjectEvent.GotoProjectList(UI.getCurrent(), null));
	}

}
