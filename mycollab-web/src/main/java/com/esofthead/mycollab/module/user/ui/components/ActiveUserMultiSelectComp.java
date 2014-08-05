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

	private static Logger log = LoggerFactory
			.getLogger(ActiveUserMultiSelectComp.class);

	public ActiveUserMultiSelectComp() {
		super("displayName");
	}

	@Override
	protected List<SimpleUser> createData() {
		UserSearchCriteria criteria = new UserSearchCriteria();
		criteria.setSaccountid(new NumberSearchField(SearchField.AND,
				AppContext.getAccountId()));
		criteria.setRegisterStatuses(new SetSearchField<String>(
				SearchField.AND,
				new String[] { RegisterStatusConstants.ACTIVE }));

		UserService userService = ApplicationContextUtil
				.getSpringBean(UserService.class);
		List<SimpleUser> userList = userService
				.findPagableListByCriteria(new SearchRequest<UserSearchCriteria>(
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
			log.error("Error while getting project member avatar", e);
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
