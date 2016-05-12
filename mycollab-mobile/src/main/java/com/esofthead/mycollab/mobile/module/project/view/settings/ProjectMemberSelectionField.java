/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.module.project.view.settings;

import com.esofthead.mycollab.mobile.ui.AbstractSelectionCustomField;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.spring.AppContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.FieldSelection;
import com.vaadin.data.Property;

/**
 * @author MyCollab Ltd.
 * @since 4.5.0
 */
public class ProjectMemberSelectionField extends AbstractSelectionCustomField<String, SimpleProjectMember>
        implements FieldSelection<SimpleProjectMember> {
    private static final long serialVersionUID = 1L;

    public ProjectMemberSelectionField() {
        super(ProjectMemberSelectionView.class);
    }

    @Override
    public void setPropertyDataSource(Property newDataSource) {
        Object value = newDataSource.getValue();
        if (value instanceof String) {
            setMemberByVal((String) value);
        }
        super.setPropertyDataSource(newDataSource);
    }

    private void setMemberByVal(String value) {
        ProjectMemberService service = AppContextUtil.getSpringBean(ProjectMemberService.class);
        SimpleProjectMember member = service.findMemberByUsername(value, CurrentProjectVariables.getProjectId(), AppContext.getAccountId());
        if (member != null) {
            setInternalMember(member);
        }
    }

    private void setInternalMember(SimpleProjectMember member) {
        this.beanItem = member;
        navButton.setCaption(member.getDisplayName());
    }

    @Override
    public Class<String> getType() {
        return String.class;
    }

    @Override
    public void fireValueChange(SimpleProjectMember data) {
        setInternalMember(data);
        setInternalValue(data.getUsername());
    }
}
