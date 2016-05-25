/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.premium.module.user.accountsettings.view

import com.esofthead.mycollab.configuration.SiteConfiguration
import com.esofthead.mycollab.core.arguments.{NumberSearchField, SetSearchField}
import com.esofthead.mycollab.eventmanager.ApplicationEventListener
import com.esofthead.mycollab.module.billing.RegisterStatusConstants
import com.esofthead.mycollab.module.user.accountsettings.billing.view.IBillingPresenter
import com.esofthead.mycollab.module.user.accountsettings.customize.view.AccountSettingPresenter
import com.esofthead.mycollab.module.user.accountsettings.profile.view.ProfilePresenter
import com.esofthead.mycollab.module.user.accountsettings.setup.view.SetupPresenter
import com.esofthead.mycollab.module.user.accountsettings.team.view.UserPermissionManagementPresenter
import com.esofthead.mycollab.module.user.accountsettings.view.AccountModule
import com.esofthead.mycollab.module.user.accountsettings.view.events.{AccountBillingEvent, ProfileEvent, SettingEvent, SetupEvent}
import com.esofthead.mycollab.module.user.accountsettings.view.parameters.SettingExtScreenData.{GeneralSetting, ThemeCustomize}
import com.esofthead.mycollab.module.user.accountsettings.view.parameters.{BillingScreenData, RoleScreenData, UserScreenData}
import com.esofthead.mycollab.module.user.domain.criteria.{RoleSearchCriteria, UserSearchCriteria}
import com.esofthead.mycollab.module.user.domain.{Role, SimpleUser}
import com.esofthead.mycollab.module.user.events.{RoleEvent, UserEvent}
import com.esofthead.mycollab.vaadin.AppContext
import com.esofthead.mycollab.vaadin.mvp.{AbstractController, PresenterResolver}
import com.google.common.eventbus.Subscribe

/**
  * @author MyCollab Ltd
  * @since 5.0.9
  */
class UserAccountController(container: AccountModule) extends AbstractController {
  bindProfileEvents()
  bindBillingEvents()
  bindRoleEvents()
  bindUserEvents()
  bingSettingEvents()

  if (!SiteConfiguration.isDemandEdition) {
    bindSetupEvents()
  }

  private def bindBillingEvents(): Unit = {
    this.register(new ApplicationEventListener[AccountBillingEvent.CancelAccount]() {
      @Subscribe def handle(event: AccountBillingEvent.CancelAccount) {
        val presenter = PresenterResolver.getPresenter(classOf[IBillingPresenter])
        presenter.go(container, new BillingScreenData.CancelAccount)
      }
    })
    this.register(new ApplicationEventListener[AccountBillingEvent.GotoSummary]() {
      @Subscribe def handle(event: AccountBillingEvent.GotoSummary) {
        val presenter = PresenterResolver.getPresenter(classOf[IBillingPresenter])
        presenter.go(container, new BillingScreenData.BillingSummary)
      }
    })
  }

  private def bindProfileEvents(): Unit = {
    this.register(new ApplicationEventListener[ProfileEvent.GotoProfileView]() {
      @Subscribe def handle(event: ProfileEvent.GotoProfileView) {
        val presenter = PresenterResolver.getPresenter(classOf[ProfilePresenter])
        presenter.go(container, null)
      }
    })
  }

  private def bindUserEvents(): Unit = {
    this.register(new ApplicationEventListener[UserEvent.GotoAdd]() {
      @Subscribe def handle(event: UserEvent.GotoAdd) {
        val presenter = PresenterResolver.getPresenter(classOf[UserPermissionManagementPresenter])
        presenter.go(container, new UserScreenData.Add(new SimpleUser))
      }
    })
    this.register(new ApplicationEventListener[UserEvent.GotoEdit]() {
      @Subscribe def handle(event: UserEvent.GotoEdit) {
        val presenter = PresenterResolver.getPresenter(classOf[UserPermissionManagementPresenter])
        val user = event.getData.asInstanceOf[SimpleUser]
        presenter.go(container, new UserScreenData.Edit(user))
      }
    })
    this.register(new ApplicationEventListener[UserEvent.GotoRead]() {
      @Subscribe def handle(event: UserEvent.GotoRead) {
        val presenter = PresenterResolver.getPresenter(classOf[UserPermissionManagementPresenter])
        presenter.go(container, new UserScreenData.Read(event.getData.asInstanceOf[String]))
      }
    })
    this.register(new ApplicationEventListener[UserEvent.GotoList]() {
      @Subscribe def handle(event: UserEvent.GotoList) {
        val presenter = PresenterResolver.getPresenter(classOf[UserPermissionManagementPresenter])
        val criteria = new UserSearchCriteria
        criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId))
        criteria.setRegisterStatuses(new SetSearchField[String](RegisterStatusConstants.ACTIVE, RegisterStatusConstants.NOT_LOG_IN_YET))
        presenter.go(container, new UserScreenData.Search(criteria))
      }
    })
  }

  private def bindRoleEvents(): Unit = {
    this.register(new ApplicationEventListener[RoleEvent.GotoAdd]() {
      @Subscribe def handle(event: RoleEvent.GotoAdd) {
        val presenter = PresenterResolver.getPresenter(classOf[UserPermissionManagementPresenter])
        presenter.go(container, new RoleScreenData.Add(new Role))
      }
    })
    this.register(new ApplicationEventListener[RoleEvent.GotoEdit]() {
      @Subscribe def handle(event: RoleEvent.GotoEdit) {
        val presenter = PresenterResolver.getPresenter(classOf[UserPermissionManagementPresenter])
        val role = event.getData.asInstanceOf[Role]
        presenter.go(container, new RoleScreenData.Edit(role))
      }
    })
    this.register(new ApplicationEventListener[RoleEvent.GotoRead]() {
      @Subscribe def handle(event: RoleEvent.GotoRead) {
        val presenter = PresenterResolver.getPresenter(classOf[UserPermissionManagementPresenter])
        presenter.go(container, new RoleScreenData.Read(event.getData.asInstanceOf[Integer]))
      }
    })
    this.register(new ApplicationEventListener[RoleEvent.GotoList]() {
      @Subscribe def handle(event: RoleEvent.GotoList) {
        val presenter = PresenterResolver.getPresenter(classOf[UserPermissionManagementPresenter])
        val criteria = new RoleSearchCriteria
        criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId))
        presenter.go(container, new RoleScreenData.Search(criteria))
      }
    })
  }

  private def bingSettingEvents(): Unit = {
    this.register(new ApplicationEventListener[SettingEvent.GotoGeneralSetting]() {
      @Subscribe def handle(event: SettingEvent.GotoGeneralSetting) {
        val presenter = PresenterResolver.getPresenter(classOf[AccountSettingPresenter])
        presenter.go(container, new GeneralSetting())
      }
    })

    this.register(new ApplicationEventListener[SettingEvent.GotoTheme]() {
      @Subscribe def handle(event: SettingEvent.GotoTheme) {
        val presenter = PresenterResolver.getPresenter(classOf[AccountSettingPresenter])
        presenter.go(container, new ThemeCustomize())
      }
    })
  }

  private def bindSetupEvents(): Unit = {
    this.register(new ApplicationEventListener[SetupEvent.GotoSetupPage]() {
      @Subscribe def handle(event: SetupEvent.GotoSetupPage) {
        val presenter = PresenterResolver.getPresenter(classOf[SetupPresenter])
        presenter.go(container, null)
      }
    })
  }
}
