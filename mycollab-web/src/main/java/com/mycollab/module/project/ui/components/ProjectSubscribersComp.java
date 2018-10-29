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
package com.mycollab.module.project.ui.components;

import com.mycollab.core.utils.StringUtils;
import com.mycollab.module.project.service.ProjectMemberService;
import com.mycollab.module.user.domain.SimpleUser;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomField;

import java.util.*;

/**
 * @author MyCollab Ltd.
 * @since 5.0.1
 */
// TODO
public class ProjectSubscribersComp extends CustomField {
    private int projectId;
    private boolean defaultSelectAll;
    private Set<String> selectedUsers;

    private Collection<FollowerCheckbox> memberSelections = new ArrayList<>();

    public ProjectSubscribersComp(boolean defaultSelectionAll, int projectId, String... selectedUsersParam) {
        this.projectId = projectId;
        this.defaultSelectAll = defaultSelectionAll;
        this.selectedUsers = new HashSet<>(Arrays.asList(selectedUsersParam));
    }

    @Override
    protected Component initContent() {
        ProjectMemberService projectMemberService = AppContextUtil.getSpringBean(ProjectMemberService.class);
        List<SimpleUser> members = projectMemberService.getActiveUsersInProject(projectId, AppUI.getAccountId());
        CssLayout container = new CssLayout();
        container.setStyleName("followers-container");
        final CheckBox selectAllCheckbox = new CheckBox("All", defaultSelectAll);
        selectAllCheckbox.addValueChangeListener(valueChangeEvent -> {
            boolean val = selectAllCheckbox.getValue();
            for (FollowerCheckbox followerCheckbox : memberSelections) {
                followerCheckbox.setValue(val);
            }
        });
        container.addComponent(selectAllCheckbox);
        for (SimpleUser user : members) {
            final FollowerCheckbox memberCheckbox = new FollowerCheckbox(user);
            memberCheckbox.addValueChangeListener(valueChangeEvent -> {
                if (!memberCheckbox.getValue()) {
                    selectAllCheckbox.setValue(false);
                }
            });
            if (defaultSelectAll || selectedUsers.contains(user.getUsername())) {
                memberCheckbox.setValue(true);
            }
            memberSelections.add(memberCheckbox);
            container.addComponent(memberCheckbox);
        }
        return container;
    }

    public void addFollower(String follower) {
        for (FollowerCheckbox followerCheckbox : memberSelections) {
            if (followerCheckbox.user.getUsername().equals(follower)) {
                followerCheckbox.setValue(true);
            }
        }
    }

    public List<String> getFollowers() {
        List<String> followers = new ArrayList<>();
        for (FollowerCheckbox followerCheckbox : memberSelections) {
            if (followerCheckbox.getValue()) {
                followers.add(followerCheckbox.user.getUsername());
            }
        }
        return followers;
    }

    @Override
    protected void doSetValue(Object o) {

    }

    @Override
    public Object getValue() {
        return null;
    }

    private static class FollowerCheckbox extends CheckBox {
        private SimpleUser user;

        FollowerCheckbox(SimpleUser user) {
            this.user = user;
            this.setCaption(StringUtils.trim(user.getDisplayName(), 20, true));
            this.setIcon(UserAvatarControlFactory.createAvatarResource(user.getAvatarid(), 16));
            this.setDescription(user.getDisplayName());
        }
    }
}
