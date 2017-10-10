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

import com.mycollab.module.project.domain.SimpleProjectMember;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.data.Property;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectMemberSelectionField extends CustomField<String> {
    private static final long serialVersionUID = 1L;

    private ProjectMemberSelectionBox memberSelectionBox;
    private MButton assignToMeBtn;

    public ProjectMemberSelectionField() {
        this.setImmediate(true);
        memberSelectionBox = new ProjectMemberSelectionBox(true);
        memberSelectionBox.addValueChangeListener(valueChangeEvent -> {
            SimpleProjectMember value = (SimpleProjectMember) memberSelectionBox.getValue();
            if (value != null) {
                setValue(value.getDisplayName());
            } else {
                setValue(null);
            }
        });

        assignToMeBtn = new MButton(UserUIContext.getMessage(ProjectCommonI18nEnum.ACTION_ASSIGN_TO_ME), clickEvent -> {
            memberSelectionBox.setValue(UserUIContext.getUser().getUsername());
        }).withStyleName(WebThemes.BUTTON_LINK);
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
        SimpleProjectMember value = (SimpleProjectMember) memberSelectionBox.getValue();
        if (value != null) {
            this.setInternalValue(value.getUsername());
        } else {
            this.setInternalValue(null);
        }

        super.commit();
    }

    @Override
    protected Component initContent() {
        return new MHorizontalLayout(memberSelectionBox, assignToMeBtn);
    }

    @Override
    public Class<? extends String> getType() {
        return String.class;
    }

    public void addValueChangeListener(ValueChangeListener listener) {
        memberSelectionBox.addValueChangeListener(listener);
    }
}
