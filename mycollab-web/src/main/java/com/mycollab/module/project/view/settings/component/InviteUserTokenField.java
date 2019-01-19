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


import com.explicatis.ext_token_field.ExtTokenField;
import com.explicatis.ext_token_field.SimpleTokenizable;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.service.ProjectMemberService;
import com.mycollab.module.user.domain.SimpleUser;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.vaadin.data.HasValue;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Notification;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author MyCollab Ltd
 * @since 5.2.1
 */
public class InviteUserTokenField extends CssLayout {
    private static final long serialVersionUID = 1L;

    private Set<String> inviteEmails;
    private List<SimpleUser> candidateUsers;
    private ExtTokenField tokenField = new ExtTokenField();

    public InviteUserTokenField() {
        inviteEmails = new HashSet<>();
        this.setWidth("100%");

        ProjectMemberService prjMemberService = AppContextUtil.getSpringBean(ProjectMemberService.class);
        candidateUsers = prjMemberService.getUsersNotInProject(CurrentProjectVariables.getProjectId(), AppUI.getAccountId());
        List<SimpleTokenizable> tokens = candidateUsers.stream().map(user -> new SimpleTokenizable(user.getUsername().hashCode(), user.getUsername())).collect(Collectors.toList());
        ComboBox<SimpleTokenizable> comboBox = new ComboBox<>("", tokens);
        comboBox.setItemCaptionGenerator(SimpleTokenizable::getStringValue);
        comboBox.setPlaceholder("Type here to add");
        comboBox.setNewItemProvider((ComboBox.NewItemProvider<SimpleTokenizable>) value -> {
            if (StringUtils.isValidEmail(value) && !inviteEmails.contains(value)) {
                inviteEmails.add(value);
                tokenField.addTokenizable(new SimpleTokenizable(value.hashCode(), value));
                return Optional.empty();
            } else {
                Notification.show("Warning", "Invalid input", Notification.Type.WARNING_MESSAGE);
                return Optional.empty();
            }
        });
        comboBox.addValueChangeListener(getComboBoxValueChange(tokenField));

        tokenField.setInputField(comboBox);
        tokenField.setEnableDefaultDeleteTokenAction(true);
        this.addComponent(tokenField);
    }

    private HasValue.ValueChangeListener<SimpleTokenizable> getComboBoxValueChange(ExtTokenField extTokenField) {
        return event -> {
            SimpleTokenizable token = event.getValue();

            if (token != null) {
                extTokenField.addTokenizable(token);
                inviteEmails.add(token.getStringValue());
                event.getSource().setValue(null);
            }
        };

    }


    public Collection<String> getInviteEmails() {
        return inviteEmails;
    }
}
