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

import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.esofthead.mycollab.vaadin.ui.MultiSelectComp;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.3.3
 *
 */
public class ActiveUserMultiSelectComp extends MultiSelectComp<SimpleUser> {
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(ActiveUserMultiSelectComp.class);

	public ActiveUserMultiSelectComp() {
		super("displayName");
	}

	@Override
	protected List<SimpleUser> createData() {
		UserSearchCriteria criteria = new UserSearchCriteria();
		criteria.setSaccountid(new NumberSearchField(SearchField.AND, AppContext.getAccountId()));
		criteria.setRegisterStatuses(new SetSearchField<>(SearchField.AND,
				new String[] { RegisterStatusConstants.ACTIVE }));

		UserService userService = ApplicationContextUtil.getSpringBean(UserService.class);
		List<SimpleUser> userList = userService.findPagableListByCriteria(new SearchRequest<>(
						criteria, 0, Integer.MAX_VALUE));
		return userList;
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
