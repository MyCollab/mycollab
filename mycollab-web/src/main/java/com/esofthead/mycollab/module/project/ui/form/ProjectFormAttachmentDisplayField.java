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
package com.esofthead.mycollab.module.project.ui.form;

import com.esofthead.mycollab.module.file.AttachmentType;
import com.esofthead.mycollab.module.project.ui.components.ProjectAttachmentDisplayComponentFactory;
import com.esofthead.mycollab.vaadin.ui.AttachmentDisplayComponent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.5.3
 *
 */
@SuppressWarnings("rawtypes")
public class ProjectFormAttachmentDisplayField extends CustomField {
	private static final long serialVersionUID = 1L;

	private int projectid;
	private AttachmentType type;
	private int typeid;

	public ProjectFormAttachmentDisplayField(final int projectid,
			final AttachmentType type, final int typeid) {
		this.projectid = projectid;
		this.type = type;
		this.typeid = typeid;
	}

	@Override
	public Class<?> getType() {
		return Object.class;
	}

	@Override
	protected Component initContent() {
		final Component comp = ProjectAttachmentDisplayComponentFactory
				.getAttachmentDisplayComponent(projectid, type, typeid);
		if (comp == null || !(comp instanceof AttachmentDisplayComponent)) {
			final Label l = new Label("&nbsp;", ContentMode.HTML);
			return l;
		} else {
			return comp;
		}
	}
}
