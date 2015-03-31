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
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.vaadin.ui.LabelLink;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author MyCollab Ltd.
 * @since 1.0
 *
 */
public class ProjectUserLink extends LabelLink {
    private static final long serialVersionUID = 1L;

    public ProjectUserLink(String username, String userAvatarId,
                           String displayName) {
        this(username, userAvatarId, displayName, true, true);
    }

    public ProjectUserLink(String username, String userAvatarId,
                           String displayName, boolean useWordWrap,
                           boolean isDisplayAvatar) {

        super(displayName, ProjectLinkBuilder.generateProjectMemberFullLink(
                CurrentProjectVariables.getProjectId(), username));

        if (isDisplayAvatar && StringUtils.isNotBlank(username)) {
            String link = StorageManager.getAvatarLink(userAvatarId, 16);

            this.setIconLink(link);
        }

        this.setStyleName("link");

        if (useWordWrap) {
            this.addStyleName(UIConstants.WORD_WRAP);
        }
    }
}
