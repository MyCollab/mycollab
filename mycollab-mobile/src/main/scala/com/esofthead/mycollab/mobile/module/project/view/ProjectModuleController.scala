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
package com.esofthead.mycollab.mobile.module.project.view

import com.esofthead.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum
import com.esofthead.mycollab.configuration.PasswordEncryptHelper
import com.esofthead.mycollab.core.MyCollabException
import com.esofthead.mycollab.core.arguments.{SetSearchField, SearchField, NumberSearchField, StringSearchField}
import com.esofthead.mycollab.core.utils.BeanUtility
import com.esofthead.mycollab.eventmanager.{ApplicationEventListener, EventBusFactory}
import com.esofthead.mycollab.mobile.MobileApplication
import com.esofthead.mycollab.mobile.module.project.events._
import com.esofthead.mycollab.mobile.module.project.view.bug.BugPresenter
import com.esofthead.mycollab.mobile.module.project.view.message.MessagePresenter
import com.esofthead.mycollab.mobile.module.project.view.milestone.MilestonePresenter
import com.esofthead.mycollab.mobile.module.project.view.parameters.ProjectScreenData.{ViewActivities, Add}
import com.esofthead.mycollab.mobile.module.project.view.parameters._
import com.esofthead.mycollab.mobile.module.project.view.settings.ProjectUserPresenter
import com.esofthead.mycollab.mobile.module.project.view.task.TaskPresenter
import com.esofthead.mycollab.module.project.{ProjectMemberStatusConstants, CurrentProjectVariables}
import com.esofthead.mycollab.module.project.domain.{SimpleProject, SimpleProjectMember, SimpleTask, SimpleMilestone}
import com.esofthead.mycollab.module.project.domain.criteria._
import com.esofthead.mycollab.module.tracker.domain.SimpleBug
import com.esofthead.mycollab.module.tracker.domain.criteria.BugSearchCriteria
import com.esofthead.mycollab.module.user.domain.{SimpleBillingAccount, SimpleUser}
import com.esofthead.mycollab.module.user.service.{BillingAccountService, UserService}
import com.esofthead.mycollab.spring.ApplicationContextUtil
import com.esofthead.mycollab.vaadin.AppContext
import com.esofthead.mycollab.vaadin.mvp.{ScreenData, PageActionChain, PresenterResolver, AbstractController}
import com.google.common.eventbus.Subscribe
import com.vaadin.addon.touchkit.extensions.LocalStorage
import com.vaadin.addon.touchkit.ui.NavigationManager
import com.vaadin.ui.UI

/**
  * @author MyCollab Ltd
  * @since 5.2.5
  */
class ProjectModuleController(val navManager: NavigationManager) extends AbstractController {
  bindProjectEvents()
  bindBugEvents()
  bindMessageEvents()
  bindMilestoneEvents()
  bindTaskEvents()
  bindMemberEvents()

  private def bindProjectEvents() {
    this.register(new ApplicationEventListener[ProjectEvent.GotoLogin]() {
      @Subscribe def handle(event: ProjectEvent.GotoLogin) {
        val presenter: ProjectLoginPresenter = PresenterResolver.getPresenter(classOf[ProjectLoginPresenter])
        presenter.go(navManager, null)
      }
    })
    this.register(new ApplicationEventListener[ProjectEvent.PlainLogin]() {
      @Subscribe def handle(event: ProjectEvent.PlainLogin) {
        val data: Array[String] = event.getData.asInstanceOf[Array[String]]
        try {
          doLogin(data(0), data(1), data(2).toBoolean)
        }
        catch {
          case exception: MyCollabException => {
            EventBusFactory.getInstance.post(new ProjectEvent.GotoLogin(this, null))
          }
        }
      }
    })
    this.register(new ApplicationEventListener[ProjectEvent.GotoAdd]() {
      @Subscribe def handle(event: ProjectEvent.GotoAdd): Unit = {
        val presenter = PresenterResolver.getPresenter(classOf[ProjectAddPresenter])
        presenter.go(navManager, new Add(new SimpleProject))
      }
    })
    this.register(new ApplicationEventListener[ProjectEvent.GotoProjectList]() {
      @Subscribe def handle(event: ProjectEvent.GotoProjectList) {
        val presenter: ProjectListPresenter = PresenterResolver.getPresenter(classOf[ProjectListPresenter])
        val criteria: ProjectSearchCriteria = new ProjectSearchCriteria
        criteria.setInvolvedMember(new StringSearchField(AppContext.getUsername))
        criteria.setProjectStatuses(new SetSearchField[String](StatusI18nEnum.Open.name))
        presenter.go(navManager, new ScreenData.Search[ProjectSearchCriteria](criteria))
      }
    })
    this.register(new ApplicationEventListener[ProjectEvent.GotoMyProject]() {
      @Subscribe def handle(event: ProjectEvent.GotoMyProject) {
        val presenter: ProjectViewPresenter = PresenterResolver.getPresenter(classOf[ProjectViewPresenter])
        presenter.handleChain(navManager, event.getData.asInstanceOf[PageActionChain])
      }
    })
    this.register(new ApplicationEventListener[ProjectEvent.AllActivities]() {
      @Subscribe def handle(event: ProjectEvent.AllActivities) {
        val presenter: AllActivityStreamPresenter = PresenterResolver.getPresenter(classOf[AllActivityStreamPresenter])
        presenter.go(navManager, event.getData.asInstanceOf[ProjectScreenData.AllActivities])
      }
    })
    this.register(new ApplicationEventListener[ProjectEvent.MyProjectActivities]() {
      @Subscribe def handle(event: ProjectEvent.MyProjectActivities) {
        val presenter: ProjectActivityStreamPresenter = PresenterResolver.getPresenter(classOf[ProjectActivityStreamPresenter])
        presenter.go(navManager,new ViewActivities(event.getData.asInstanceOf[Integer]))
      }
    })
  }

  private def bindBugEvents() {
    this.register(new ApplicationEventListener[BugEvent.GotoList]() {
      @Subscribe def handle(event: BugEvent.GotoList) {
        val params: Any = event.getData
        val presenter: BugPresenter = PresenterResolver.getPresenter(classOf[BugPresenter])
        if (params == null) {
          val criteria: BugSearchCriteria = new BugSearchCriteria
          criteria.setProjectId(new NumberSearchField(SearchField.AND, CurrentProjectVariables.getProjectId))
          presenter.go(navManager, new BugScreenData.Search(criteria))
        }
        else if (params.isInstanceOf[BugScreenData.Search]) {
          presenter.go(navManager, params.asInstanceOf[BugScreenData.Search])
        }
        else {
          throw new MyCollabException("Invalid search parameter: " + BeanUtility.printBeanObj(params))
        }
      }
    })
    this.register(new ApplicationEventListener[BugEvent.GotoRead]() {
      @Subscribe def handle(event: BugEvent.GotoRead) {
        val data: BugScreenData.Read = new BugScreenData.Read(event.getData.asInstanceOf[Integer])
        val presenter: BugPresenter = PresenterResolver.getPresenter(classOf[BugPresenter])
        presenter.go(navManager, data)
      }
    })
    this.register(new ApplicationEventListener[BugEvent.GotoAdd]() {
      @Subscribe def handle(event: BugEvent.GotoAdd) {
        val data: BugScreenData.Add = new BugScreenData.Add(new SimpleBug)
        val presenter: BugPresenter = PresenterResolver.getPresenter(classOf[BugPresenter])
        presenter.go(navManager, data)
      }
    })
    this.register(new ApplicationEventListener[BugEvent.GotoEdit]() {
      @Subscribe def handle(event: BugEvent.GotoEdit) {
        val data: BugScreenData.Edit = new BugScreenData.Edit(event.getData.asInstanceOf[SimpleBug])
        val presenter: BugPresenter = PresenterResolver.getPresenter(classOf[BugPresenter])
        presenter.go(navManager, data)
      }
    })
  }

  private def bindMessageEvents() {
    this.register(new ApplicationEventListener[MessageEvent.GotoAdd]() {
      @Subscribe def handle(event: MessageEvent.GotoAdd) {
        val data: MessageScreenData.Add = new MessageScreenData.Add
        val presenter: MessagePresenter = PresenterResolver.getPresenter(classOf[MessagePresenter])
        presenter.go(navManager, data)
      }
    })
    this.register(new ApplicationEventListener[MessageEvent.GotoList]() {
      @Subscribe def handle(event: MessageEvent.GotoList) {
        val searchCriteria: MessageSearchCriteria = new MessageSearchCriteria
        searchCriteria.setProjectids(new SetSearchField[Integer](CurrentProjectVariables.getProjectId))
        val data: MessageScreenData.Search = new MessageScreenData.Search(searchCriteria)
        val presenter: MessagePresenter = PresenterResolver.getPresenter(classOf[MessagePresenter])
        presenter.go(navManager, data)
      }
    })
    this.register(new ApplicationEventListener[MessageEvent.GotoRead]() {
      @Subscribe def handle(event: MessageEvent.GotoRead) {
        val data: MessageScreenData.Read = new MessageScreenData.Read(event.getData.asInstanceOf[Integer])
        val presenter: MessagePresenter = PresenterResolver.getPresenter(classOf[MessagePresenter])
        presenter.go(navManager, data)
      }
    })
  }

  private def bindMilestoneEvents() {
    this.register(new ApplicationEventListener[MilestoneEvent.GotoList]() {
      @Subscribe def handle(event: MilestoneEvent.GotoList) {
        val params: Any = event.getData
        val presenter: MilestonePresenter = PresenterResolver.getPresenter(classOf[MilestonePresenter])
        if (params == null) {
          val criteria: MilestoneSearchCriteria = new MilestoneSearchCriteria
          criteria.setProjectId(new NumberSearchField(SearchField.AND, CurrentProjectVariables.getProjectId))
          presenter.go(navManager, new MilestoneScreenData.Search(criteria))
        }
        else if (params.isInstanceOf[MilestoneScreenData.Search]) {
          presenter.go(navManager, params.asInstanceOf[MilestoneScreenData.Search])
        }
        else {
          throw new MyCollabException("Invalid search parameter: " + BeanUtility.printBeanObj(params))
        }
      }
    })
    this.register(new ApplicationEventListener[MilestoneEvent.GotoRead]() {
      @Subscribe def handle(event: MilestoneEvent.GotoRead) {
        val data: MilestoneScreenData.Read = new MilestoneScreenData.Read(event.getData.asInstanceOf[Integer])
        val presenter: MilestonePresenter = PresenterResolver.getPresenter(classOf[MilestonePresenter])
        presenter.go(navManager, data)
      }
    })
    this.register(new ApplicationEventListener[MilestoneEvent.GotoAdd]() {
      @Subscribe def handle(event: MilestoneEvent.GotoAdd) {
        val data: MilestoneScreenData.Add = new MilestoneScreenData.Add(new SimpleMilestone)
        val presenter: MilestonePresenter = PresenterResolver.getPresenter(classOf[MilestonePresenter])
        presenter.go(navManager, data)
      }
    })
    this.register(new ApplicationEventListener[MilestoneEvent.GotoEdit]() {
      @Subscribe def handle(event: MilestoneEvent.GotoEdit) {
        val data: MilestoneScreenData.Edit = new MilestoneScreenData.Edit(event.getData.asInstanceOf[SimpleMilestone])
        val presenter: MilestonePresenter = PresenterResolver.getPresenter(classOf[MilestonePresenter])
        presenter.go(navManager, data)
      }
    })
  }

  private def bindTaskEvents() {
    this.register(new ApplicationEventListener[TaskEvent.GotoList]() {
      @Subscribe def handle(event: TaskEvent.GotoList) {
        val criteria: TaskSearchCriteria = new TaskSearchCriteria
        criteria.setProjectid(new NumberSearchField(SearchField.AND, CurrentProjectVariables.getProjectId))
        val data: TaskScreenData.Search = new TaskScreenData.Search(criteria)
        val presenter: TaskPresenter = PresenterResolver.getPresenter(classOf[TaskPresenter])
        presenter.go(navManager, data)
      }
    })
    this.register(new ApplicationEventListener[TaskEvent.GotoRead]() {
      @Subscribe def handle(event: TaskEvent.GotoRead) {
        val data: TaskScreenData.Read = new TaskScreenData.Read(event.getData.asInstanceOf[Integer])
        val presenter: TaskPresenter = PresenterResolver.getPresenter(classOf[TaskPresenter])
        presenter.go(navManager, data)
      }
    })
    this.register(new ApplicationEventListener[TaskEvent.GotoEdit]() {
      @Subscribe def handle(event: TaskEvent.GotoEdit) {
        val data: TaskScreenData.Edit = new TaskScreenData.Edit(event.getData.asInstanceOf[SimpleTask])
        val presenter: TaskPresenter = PresenterResolver.getPresenter(classOf[TaskPresenter])
        presenter.go(navManager, data)
      }
    })
    this.register(new ApplicationEventListener[TaskEvent.GotoAdd]() {
      @Subscribe def handle(event: TaskEvent.GotoAdd) {
        val data: TaskScreenData.Add = new TaskScreenData.Add(new SimpleTask)
        val presenter: TaskPresenter = PresenterResolver.getPresenter(classOf[TaskPresenter])
        presenter.go(navManager, data)
      }
    })
  }

  private def bindMemberEvents() {
    this.register(new ApplicationEventListener[ProjectMemberEvent.GotoList]() {
      @Subscribe def handle(event: ProjectMemberEvent.GotoList) {
        val criteria: ProjectMemberSearchCriteria = new ProjectMemberSearchCriteria
        criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId))
        criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId))
        criteria.setStatus(new StringSearchField(ProjectMemberStatusConstants.ACTIVE))
        val presenter: ProjectUserPresenter = PresenterResolver.getPresenter(classOf[ProjectUserPresenter])
        presenter.go(navManager, new ProjectMemberScreenData.Search(criteria))
      }
    })
    this.register(new ApplicationEventListener[ProjectMemberEvent.GotoRead]() {
      @Subscribe def handle(event: ProjectMemberEvent.GotoRead) {
        val data: ProjectMemberScreenData.Read = new ProjectMemberScreenData.Read(event.getData)
        val presenter: ProjectUserPresenter = PresenterResolver.getPresenter(classOf[ProjectUserPresenter])
        presenter.go(navManager, data)
      }
    })
    this.register(new ApplicationEventListener[ProjectMemberEvent.GotoEdit]() {
      @Subscribe def handle(event: ProjectMemberEvent.GotoEdit) {
        val data: ProjectMemberScreenData.Edit = new ProjectMemberScreenData.Edit(event.getData.asInstanceOf[SimpleProjectMember])
        val presenter: ProjectUserPresenter = PresenterResolver.getPresenter(classOf[ProjectUserPresenter])
        presenter.go(navManager, data)
      }
    })
    this.register(new ApplicationEventListener[ProjectMemberEvent.GotoInviteMembers]() {
      @Subscribe def handle(event: ProjectMemberEvent.GotoInviteMembers) {
        val data: ProjectMemberScreenData.InviteProjectMembers = new ProjectMemberScreenData.InviteProjectMembers
        val presenter: ProjectUserPresenter = PresenterResolver.getPresenter(classOf[ProjectUserPresenter])
        presenter.go(navManager, data)
      }
    })
  }

  @throws(classOf[MyCollabException])
  def doLogin(username: String, password: String, isRememberPassword: Boolean) {
    val userService: UserService = ApplicationContextUtil.getSpringBean(classOf[UserService])
    val user: SimpleUser = userService.authentication(username, password, AppContext.getSubDomain, false)
    val billingAccountService: BillingAccountService = ApplicationContextUtil.getSpringBean(classOf[BillingAccountService])
    val billingAccount: SimpleBillingAccount = billingAccountService.getBillingAccountById(AppContext.getAccountId)
    if (isRememberPassword) {
      val storage: LocalStorage = LocalStorage.get
      val storeVal: String = username + "$" + PasswordEncryptHelper.encryptText(password)
      storage.put(MobileApplication.LOGIN_DATA, storeVal)
    }
    AppContext.getInstance.setSessionVariables(user, billingAccount)
    EventBusFactory.getInstance.post(new ProjectEvent.GotoProjectList(UI.getCurrent, null))
  }
}
