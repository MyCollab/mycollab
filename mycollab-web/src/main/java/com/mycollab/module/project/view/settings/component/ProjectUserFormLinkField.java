/**
 * mycollab-web - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view.settings.component;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectUserFormLinkField extends CustomField {
    private static final long serialVersionUID = 1L;

    private Integer projectId;
    private String username;
    private String userAvatarId;
    private String displayName;

    public ProjectUserFormLinkField(Integer projectId, String username, String userAvatarId, String displayName) {
        this.projectId = projectId;
        this.username = username;
        this.userAvatarId = userAvatarId;
        this.displayName = displayName;
    }

    @Override
    public Class<?> getType() {
        return Object.class;
    }

    @Override
    protected Component initContent() {
        return new ProjectUserLink(projectId, username, userAvatarId, displayName);
    }
}
