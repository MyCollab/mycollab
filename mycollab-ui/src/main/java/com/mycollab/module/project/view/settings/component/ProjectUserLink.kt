/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view.settings.component

import com.hp.gagawa.java.elements.A
import com.hp.gagawa.java.elements.Img
import com.mycollab.core.utils.StringUtils
import com.mycollab.html.DivLessFormatter
import com.mycollab.module.file.service.AbstractStorageService
import com.mycollab.module.project.ProjectLinkBuilder
import com.mycollab.module.project.ProjectLinkGenerator
import com.mycollab.spring.AppContextUtil
import com.mycollab.vaadin.TooltipHelper
import com.mycollab.vaadin.TooltipHelper.TOOLTIP_ID
import com.mycollab.vaadin.ui.UIConstants
import com.vaadin.shared.ui.label.ContentMode
import com.vaadin.ui.Label

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class ProjectUserLink(projectId: Int, username: String?, userAvatarId: String?, displayName: String?) : Label() {

    init {
        if (username != null) {
            this.contentMode = ContentMode.HTML
            val div = DivLessFormatter()
            val avatarLink = Img("", AppContextUtil.getSpringBean(AbstractStorageService::class.java)
                    .getAvatarPath(userAvatarId, 16))
            avatarLink.cssClass = UIConstants.CIRCLE_BOX
            val memberLink = A().setId("tag" + TOOLTIP_ID).setHref(ProjectLinkGenerator.generateProjectMemberLink(
                    projectId, username)).appendText(StringUtils.trim(displayName, 30, true))
            memberLink.setAttribute("onmouseover", TooltipHelper.userHoverJsFunction(username))
            memberLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction())
            div.appendChild(avatarLink, DivLessFormatter.EMPTY_SPACE, memberLink)
            this.value = div.write()
        }
    }
}
