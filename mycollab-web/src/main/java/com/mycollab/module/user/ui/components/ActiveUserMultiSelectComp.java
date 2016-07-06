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
import com.mycollab.module.billing.RegisterStatusConstants;
import com.mycollab.module.user.domain.SimpleUser;
import com.mycollab.module.user.domain.criteria.UserSearchCriteria;
import com.mycollab.module.user.service.UserService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.web.ui.MultiSelectComp;
import com.mycollab.vaadin.ui.UserAvatarControlFactory;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.3.3
 */
public class ActiveUserMultiSelectComp extends MultiSelectComp<SimpleUser> {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(ActiveUserMultiSelectComp.class);

    public ActiveUserMultiSelectComp() {
        super("displayName", false);
    }

    @Override
    protected List<SimpleUser> createData() {
        UserSearchCriteria criteria = new UserSearchCriteria();
        criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
        criteria.setRegisterStatuses(new SetSearchField<>(RegisterStatusConstants.ACTIVE));

        UserService userService = AppContextUtil.getSpringBean(UserService.class);
        return userService.findPagableListByCriteria(new BasicSearchRequest<>(
                criteria, 0, Integer.MAX_VALUE));
    }

    @Override
    protected void requestAddNewComp() {

    }

    @Override
    protected ItemSelectionComp<SimpleUser> buildItem(final SimpleUser item) {
        ItemSelectionComp<SimpleUser> buildItem = super.buildItem(item);
        String userAvatarId = "";

        try {
            userAvatarId = (String) PropertyUtils.getProperty(item, "avatarid");
        } catch (Exception e) {
            LOG.error("Error while getting project member avatar", e);
        }

        buildItem.setIcon(UserAvatarControlFactory.createAvatarResource(
                userAvatarId, 16));
        return buildItem;
    }

    @Override
    public Class<? extends SimpleUser> getType() {
        return SimpleUser.class;
    }
}
