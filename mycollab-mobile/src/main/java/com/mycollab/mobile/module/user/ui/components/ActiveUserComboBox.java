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
package com.mycollab.mobile.module.user.ui.components;

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.module.billing.RegisterStatusConstants;
import com.mycollab.module.user.domain.SimpleUser;
import com.mycollab.module.user.domain.criteria.UserSearchCriteria;
import com.mycollab.module.user.service.UserService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.vaadin.ui.ListSelect;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class ActiveUserComboBox extends ListSelect {
    private static final long serialVersionUID = 1L;

    public ActiveUserComboBox() {
        this.setItemCaptionMode(ItemCaptionMode.EXPLICIT);
        this.setRows(1);

        UserSearchCriteria criteria = new UserSearchCriteria();
        criteria.setSaccountid(new NumberSearchField(AppUI.getAccountId()));
        criteria.setRegisterStatuses(new SetSearchField<>(RegisterStatusConstants.ACTIVE));

        UserService userService = AppContextUtil.getSpringBean(UserService.class);
        List<SimpleUser> userList = (List<SimpleUser>)userService.findPageableListByCriteria(new BasicSearchRequest<>(criteria));
        loadUserList(userList);
    }

    private void loadUserList(List<SimpleUser> userList) {
        for (SimpleUser user : userList) {
            this.addItem(user.getUsername());
            this.setItemCaption(user.getUsername(), user.getDisplayName());
            this.setItemIcon(user.getUsername(), UserAvatarControlFactory.createAvatarResource(user.getAvatarid(), 16));
        }
    }
}
