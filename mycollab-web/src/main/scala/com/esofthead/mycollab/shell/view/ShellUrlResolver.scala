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
package com.esofthead.mycollab.shell.view

import com.esofthead.mycollab.module.crm.view.CrmUrlResolver
import com.esofthead.mycollab.module.file.view.FileUrlResolver
import com.esofthead.mycollab.module.project.view.ProjectUrlResolver
import com.esofthead.mycollab.premium.module.user.accountsettings.view.AccountSettingUrlResolver
import com.esofthead.mycollab.vaadin.mvp.UrlResolver
import org.apache.commons.lang3.StringUtils

/**
  * @author MyCollab Ltd
  * @since 5.0.9
  */
object ShellUrlResolver {
  val ROOT = new ShellUrlResolver
}
class ShellUrlResolver extends UrlResolver {
  this.addSubResolver("crm", new CrmUrlResolver().build)
  this.addSubResolver("project", new ProjectUrlResolver().build)
  this.addSubResolver("account", new AccountSettingUrlResolver().build)
  this.addSubResolver("document", new FileUrlResolver().build)

  def resolveFragment(newFragmentUrl: String) {
    if (!StringUtils.isBlank(newFragmentUrl)) {
      val tokens = newFragmentUrl.split("/")
      this.handle(tokens: _*)
    }
  }

  protected def defaultPageErrorHandler() {
  }
}
