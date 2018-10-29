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
package com.mycollab.module.user.ui.components;

import com.vaadin.ui.ListSelect;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
// TODO
public class ActiveUserListSelect extends ListSelect {
    private static final long serialVersionUID = 1L;

    public ActiveUserListSelect() {
//        this.setItemCaptionMode(ItemCaptionMode.EXPLICIT);
//        this.setMultiSelect(true);
//
//        UserSearchCriteria criteria = new UserSearchCriteria();
//        criteria.setSaccountid(new NumberSearchField(AppUI.getAccountId()));
//        criteria.setRegisterStatuses(new SetSearchField<>(RegisterStatusConstants.ACTIVE));
//
//        UserService userService = AppContextUtil.getSpringBean(UserService.class);
//        List<SimpleUser> users = (List<SimpleUser>) userService.findPageableListByCriteria(new BasicSearchRequest<>(criteria));
//
//        for (SimpleUser user : users) {
//            this.addItem(user.getUsername());
//            this.setItemCaption(user.getUsername(), user.getDisplayName());
//            this.setItemIcon(user.getUsername(), UserAvatarControlFactory.createAvatarResource(user.getAvatarid(), 16));
//        }

        this.setRows(4);
    }
}
