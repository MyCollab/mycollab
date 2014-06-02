package com.esofthead.mycollab.module.user;

import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Text;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1.2
 * 
 */
public class AccountLinkBuilder {

	public static String generatePreviewFullUserLink(String username) {
		return AccountLinkUtils.generatePreviewFullUserLink(
				AppContext.getSiteUrl(), username);
	}

	public static String generateUserHtmlLink(String username) {
		UserService userService = ApplicationContextUtil
				.getSpringBean(UserService.class);
		SimpleUser user = userService.findUserByUserNameInAccount(username,
				AppContext.getAccountId());
		if (user != null) {
			A link = new A();
			link.setHref(generatePreviewFullUserLink(username));
			Text text = new Text(user.getDisplayName());
			link.appendChild(text);
			return link.write();
		} else {
			return null;
		}
	}
}
