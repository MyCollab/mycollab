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
package com.mycollab.module.project.view.page

import com.mycollab.common.UrlTokenizer
import com.mycollab.eventmanager.EventBusFactory
import com.mycollab.module.page.domain.Page
import com.mycollab.module.page.service.PageService
import com.mycollab.module.project.events.ProjectEvent
import com.mycollab.module.project.view.ProjectUrlResolver
import com.mycollab.module.project.view.parameters.{PageScreenData, ProjectScreenData}
import com.mycollab.vaadin.AppContext
import com.mycollab.vaadin.mvp.PageActionChain
import com.mycollab.common.{InvalidTokenException, UrlTokenizer}
import com.mycollab.core.MyCollabException
import com.mycollab.core.utils.StringUtils
import com.mycollab.spring.AppContextUtil

/**
  * @author MyCollab Ltd
  * @since 5.0.9
  */
class PageUrlResolver extends ProjectUrlResolver {
  this.addSubResolver("list", new ListUrlResolver)
  this.addSubResolver("add", new AddUrlResolver)
  this.addSubResolver("edit", new EditUrlResolver)
  this.addSubResolver("preview", new PreviewUrlResolver)

  private class ListUrlResolver extends ProjectUrlResolver {
    protected override def handlePage(params: String*) {
      try {
        val tokenizer = new UrlTokenizer(params(0))
        val projectId = tokenizer.getInt
        val pagePath = tokenizer.getRemainValue
        val chain = new PageActionChain(new ProjectScreenData.Goto(projectId),
          new PageScreenData.Search(pagePath))
        EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain))
      }
      catch {
        case e: Exception => throw new MyCollabException(e)
      }
    }
  }

  private class PreviewUrlResolver extends ProjectUrlResolver {
    protected override def handlePage(params: String*) {
      try {
        val tokenizer = new UrlTokenizer(params(0))
        val projectId = tokenizer.getInt
        val pagePath = tokenizer.getRemainValue
        val pageService = AppContextUtil.getSpringBean(classOf[PageService])
        val page = pageService.getPage(pagePath, AppContext.getUsername)
        if (page != null) {
          val chain = new PageActionChain(new ProjectScreenData.Goto(projectId),
            new PageScreenData.Read(page))
          EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain))
        }
        else {
          val chain = new PageActionChain(new ProjectScreenData.Goto(projectId))
          EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain))
        }
      }
      catch {
        case e: Exception => throw new MyCollabException(e)
      }
    }
  }

  private class EditUrlResolver extends ProjectUrlResolver {
    protected override def handlePage(params: String*) {
      try {
        val tokenizer = new UrlTokenizer(params(0))
        val projectId = tokenizer.getInt
        val pagePath = tokenizer.getRemainValue
        val pageService = AppContextUtil.getSpringBean(classOf[PageService])
        val page = pageService.getPage(pagePath, AppContext.getUsername)
        if (page != null) {
          val chain = new PageActionChain(new ProjectScreenData.Goto(projectId),
            new PageScreenData.Edit(page))
          EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain))
        }
        else {
          val chain = new PageActionChain(new ProjectScreenData.Goto(projectId))
          EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain))
        }
      }
      catch {
        case e: InvalidTokenException => throw new MyCollabException(e)
      }
    }
  }

  private class AddUrlResolver extends ProjectUrlResolver {
    protected override def handlePage(params: String*) {
      try {
        val tokenizer: UrlTokenizer = new UrlTokenizer(params(0))
        val projectId: Int = tokenizer.getInt
        val pagePath: String = tokenizer.getRemainValue
        val page: Page = new Page
        page.setPath(pagePath + "/" + StringUtils.generateSoftUniqueId)
        val chain: PageActionChain = new PageActionChain(new ProjectScreenData.Goto(projectId),
          new PageScreenData.Add(page))
        EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain))
      }
      catch {
        case e: Exception => throw new MyCollabException(e)
      }
    }
  }

}
