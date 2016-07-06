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
package com.mycollab.module.project.view.settings

import com.mycollab.eventmanager.EventBusFactory
import com.mycollab.module.project.events.ProjectEvent
import com.mycollab.module.project.view.ProjectUrlResolver
import com.mycollab.module.project.view.parameters.{ProjectScreenData, ProjectSettingScreenData}
import com.mycollab.vaadin.mvp.PageActionChain
import com.mycollab.common.UrlTokenizer

/**
  * @author MyCollab Ltd
  * @since 5.0.9
  */
class SettingUrlResolver extends ProjectUrlResolver {
  protected override def handlePage(params: String*) {
    val projectId = new UrlTokenizer(params(0)).getInt
    val chain = new PageActionChain(new ProjectScreenData.Goto(projectId), new ProjectSettingScreenData.ViewSettings)
    EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain))
  }
}
