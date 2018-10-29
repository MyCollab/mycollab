/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view.settings.component;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
// TODO
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
    protected void doSetValue(Object o) {

    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    protected Component initContent() {
        return new ProjectUserLink(projectId, username, userAvatarId, displayName);
    }
}
