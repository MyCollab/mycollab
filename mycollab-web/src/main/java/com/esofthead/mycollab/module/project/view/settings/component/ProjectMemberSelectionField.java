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

import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.vaadin.data.Property;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class ProjectMemberSelectionField extends CustomField<String> {

	private static final long serialVersionUID = 1L;

	private ProjectMemberSelectionBox memberSelectionBox;

	public ProjectMemberSelectionField() {
		super();
		memberSelectionBox = new ProjectMemberSelectionBox();
		memberSelectionBox.setWidth("100%");
	}

	@Override
	public void setPropertyDataSource(Property newDataSource) {
		Object value = newDataSource.getValue();
		if (value instanceof String) {
			memberSelectionBox.setValue(value);
		}
		super.setPropertyDataSource(newDataSource);
	}

	@Override
	public void commit() throws SourceException, InvalidValueException {
		SimpleProjectMember value = (SimpleProjectMember) memberSelectionBox
				.getValue();
		if (value != null) {
			this.setInternalValue(value.getUsername());
		} else {
			this.setInternalValue(null);
		}

		super.commit();
	}

	public ProjectMemberSelectionBox getWrappedComponent() {
		return memberSelectionBox;
	}

	@Override
	protected Component initContent() {
		return memberSelectionBox;
	}

	@Override
	public Class<? extends String> getType() {
		return String.class;
	}

}
