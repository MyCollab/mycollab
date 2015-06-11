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
package com.esofthead.mycollab.module.user.ui.components;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.module.billing.RegisterStatusConstants;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.domain.criteria.UserSearchCriteria;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.vaadin.ui.ListSelect;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ActiveUserListSelect extends ListSelect {
    private static final long serialVersionUID = 1L;

    private List<SimpleUser> userList;

    @SuppressWarnings("unchecked")
    public ActiveUserListSelect() {
        this.setItemCaptionMode(ItemCaptionMode.EXPLICIT);
        this.setMultiSelect(true);

        UserSearchCriteria criteria = new UserSearchCriteria();
        criteria.setSaccountid(new NumberSearchField(SearchField.AND,
                AppContext.getAccountId()));
        criteria.setRegisterStatuses(new SetSearchField<>(RegisterStatusConstants.ACTIVE));

        UserService userService = ApplicationContextUtil.getSpringBean(UserService.class);
        userList = userService.findPagableListByCriteria(new SearchRequest<>(
                criteria, 0, Integer.MAX_VALUE));

        for (SimpleUser user : userList) {
            this.addItem(user.getUsername());
            this.setItemCaption(user.getUsername(), user.getDisplayName());
            this.setItemIcon(user.getUsername(),
                    UserAvatarControlFactory.createAvatarResource(user.getAvatarid(), 16));
        }

        this.setRows(4);
    }
}
