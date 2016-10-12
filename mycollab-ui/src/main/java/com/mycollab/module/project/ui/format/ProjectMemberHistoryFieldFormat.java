/**
 * This file is part of mycollab-ui.
 *
 * mycollab-ui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-ui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-ui.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.ui.format;

import com.mycollab.configuration.StorageFactory;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.html.DivLessFormatter;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectLinkBuilder;
import com.mycollab.module.user.domain.SimpleUser;
import com.mycollab.module.user.service.UserService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.ui.formatter.HistoryFieldFormat;
import com.mycollab.vaadin.TooltipHelper;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Img;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.mycollab.vaadin.TooltipHelper.TOOLTIP_ID;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public final class ProjectMemberHistoryFieldFormat implements HistoryFieldFormat {
    private static final Logger LOG = LoggerFactory.getLogger(ProjectMemberHistoryFieldFormat.class);

    @Override
    public String toString(String value) {
        return toString(value, true, "");
    }

    @Override
    public String toString(String value, Boolean displayAsHtml, String msgIfBlank) {
        if (StringUtils.isBlank(value)) {
            return msgIfBlank;
        }

        try {
            UserService userService = AppContextUtil.getSpringBean(UserService.class);
            SimpleUser user = userService.findUserByUserNameInAccount(value, MyCollabUI.getAccountId());
            if (user != null) {
                if (displayAsHtml) {
                    Img userAvatar = new Img("", StorageFactory.getAvatarPath(user.getAvatarid(), 16)).setCSSClass(UIConstants.CIRCLE_BOX);
                    A link = new A().setId("tag" + TOOLTIP_ID).setHref(ProjectLinkBuilder.generateProjectMemberFullLink
                            (CurrentProjectVariables.getProjectId(),
                                    user.getUsername())).appendText(StringUtils.trim(user.getDisplayName(), 30, true));
                    link.setAttribute("onmouseover", TooltipHelper.userHoverJsFunction(user.getUsername()));
                    link.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction());
                    return new DivLessFormatter().appendChild(userAvatar, DivLessFormatter.EMPTY_SPACE(), link).write();
                } else {
                    return user.getDisplayName();
                }
            }
        } catch (Exception e) {
            LOG.error("Error", e);
        }
        return value;
    }
}
