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
package com.mycollab.module.project.ui.form;

import com.mycollab.module.project.ui.components.ProjectAttachmentDisplayComponentFactory;
import com.mycollab.vaadin.web.ui.AttachmentDisplayComponent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;

/**
 * @author MyCollab Ltd.
 * @since 4.5.3
 */
public class ProjectFormAttachmentDisplayField extends CustomField {
    private static final long serialVersionUID = 1L;

    private int projectId;
    private String type;
    private int typeId;

    public ProjectFormAttachmentDisplayField(final int projectId, final String type, final int typeId) {
        this.projectId = projectId;
        this.type = type;
        this.typeId = typeId;
    }

    @Override
    public Class<?> getType() {
        return Object.class;
    }

    @Override
    protected Component initContent() {
        final Component comp = ProjectAttachmentDisplayComponentFactory.getAttachmentDisplayComponent(projectId, type, typeId);
        if (!(comp instanceof AttachmentDisplayComponent)) {
            return new Label("&nbsp;", ContentMode.HTML);
        } else {
            return comp;
        }
    }
}
