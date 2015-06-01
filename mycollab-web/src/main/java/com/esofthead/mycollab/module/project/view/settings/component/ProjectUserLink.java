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
package com.esofthead.mycollab.module.project.view.settings.component;

import com.esofthead.mycollab.configuration.StorageManager;
import com.esofthead.mycollab.html.DivLessFormatter;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.utils.TooltipHelper;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Img;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

/**
 *
 * @author MyCollab Ltd.
 * @since 1.0
 *
 */
public class ProjectUserLink extends Label {
    private static final long serialVersionUID = 1L;

    public ProjectUserLink(String username, String userAvatarId, String displayName) {
        if (StringUtils.isBlank(username)) {
            return;
        }
        this.setContentMode(ContentMode.HTML);
        DivLessFormatter div = new DivLessFormatter();
        String uid = UUID.randomUUID().toString();
        Img avatarLink = new Img("", StorageManager.getAvatarLink(userAvatarId, 16));
        A memberLink = new A().setId("tag" + uid).setHref(ProjectLinkBuilder.generateProjectMemberFullLink(
                CurrentProjectVariables.getProjectId(), username)).appendText(displayName);
        memberLink.setAttribute("onmouseover", TooltipHelper.userHoverJsDunction(uid, username));
        memberLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction(uid));
        div.appendChild(avatarLink, DivLessFormatter.EMPTY_SPACE(), memberLink, DivLessFormatter.EMPTY_SPACE(), TooltipHelper.buildDivTooltipEnable(uid));
        this.setValue(div.write());
    }
}
