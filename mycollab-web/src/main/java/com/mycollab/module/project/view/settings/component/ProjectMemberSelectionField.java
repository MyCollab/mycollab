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

import com.mycollab.module.project.domain.SimpleProjectMember;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.web.ui.WebThemes;
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
        memberSelectionBox = new ProjectMemberSelectionBox(true);

        assignToMeBtn = new MButton(UserUIContext.getMessage(ProjectCommonI18nEnum.ACTION_ASSIGN_TO_ME), clickEvent -> {
            memberSelectionBox.setSelectedUser(UserUIContext.getUser().getUsername());
        }).withStyleName(WebThemes.BUTTON_LINK);
    }

    @Override
    protected Component initContent() {
        return new MHorizontalLayout(memberSelectionBox, assignToMeBtn);
    }

    @Override
    protected void doSetValue(String value) {
        memberSelectionBox.setSelectedUser(value);
    }

    @Override
    public String getValue() {
        SimpleProjectMember member = memberSelectionBox.getValue();
        return (member != null) ? member.getUsername() : null;
    }
}
