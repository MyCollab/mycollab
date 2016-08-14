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
package com.mycollab.module.user.ui.components;

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.module.billing.RegisterStatusConstants;
import com.mycollab.module.user.domain.SimpleUser;
import com.mycollab.module.user.domain.criteria.UserSearchCriteria;
import com.mycollab.module.user.service.UserService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.vaadin.ui.ComboBox;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class ActiveUserComboBox extends ComboBox {
    private static final long serialVersionUID = 1L;

    public ActiveUserComboBox() {
        this.setItemCaptionMode(ItemCaptionMode.EXPLICIT);

        UserSearchCriteria criteria = new UserSearchCriteria();
        criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
        criteria.setRegisterStatuses(new SetSearchField<>(RegisterStatusConstants.ACTIVE));

        UserService userService = AppContextUtil.getSpringBean(UserService.class);
        List<SimpleUser> userList = userService.findPageableListByCriteria(new BasicSearchRequest<>(criteria));
        loadUserList(userList);
    }

    private void loadUserList(List<SimpleUser> userList) {
        for (SimpleUser user : userList) {
            this.addItem(user.getUsername());
            this.setItemCaption(user.getUsername(), StringUtils.trim(user.getDisplayName(), 30, true));
            this.setItemIcon(user.getUsername(), UserAvatarControlFactory.createAvatarResource(user.getAvatarid(), 16));
        }
    }
}
