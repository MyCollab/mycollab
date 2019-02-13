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
package com.mycollab.module.project.ui.components;

import com.hp.gagawa.java.elements.A;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectLinkGenerator;
import com.mycollab.vaadin.TooltipHelper;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Image;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 5.0.4
 */
public class ProjectMemberBlock extends MVerticalLayout {
    public ProjectMemberBlock(String username, String userAvatarId, String displayName) {
        withMargin(false).withWidth("80px");
        Image userAvatar = UserAvatarControlFactory.createUserAvatarEmbeddedComponent(userAvatarId, 48, displayName);
        userAvatar.addStyleName(WebThemes.CIRCLE_BOX);
        A userLink = new A().setId("tag" + TooltipHelper.TOOLTIP_ID).
                setHref(ProjectLinkGenerator.generateProjectMemberLink(CurrentProjectVariables.getProjectId(),
                        username)).appendText(StringUtils.trim(displayName, 30, true)).setTitle(displayName);
        userLink.setAttribute("onmouseover", TooltipHelper.userHoverJsFunction(username));
        userLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction());
        ELabel userLbl = ELabel.html(userLink.write()).withStyleName(ValoTheme.LABEL_SMALL).withFullWidth();
        with(userAvatar, userLbl).withAlign(userAvatar, Alignment.TOP_CENTER);
    }
}
