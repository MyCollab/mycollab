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

import com.esofthead.mycollab.configuration.Storage;
import com.esofthead.mycollab.html.DivLessFormatter;
import com.esofthead.mycollab.html.FormatUtils;
import com.esofthead.mycollab.module.mail.MailUtils;
import com.esofthead.mycollab.module.user.AccountLinkGenerator;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.utils.TooltipHelper;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.HistoryFieldFormat;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Span;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class UserHistoryFieldFormat implements HistoryFieldFormat {

	private static final Logger LOG = LoggerFactory
			.getLogger(UserHistoryFieldFormat.class);

	@Override
	public Component toVaadinComponent(String value) {
		UserService userService = ApplicationContextUtil
				.getSpringBean(UserService.class);
		SimpleUser user = userService.findUserByUserNameInAccount(value,
				AppContext.getAccountId());
		if (user != null) {
			String uid = UUID.randomUUID().toString();
			DivLessFormatter div = new DivLessFormatter();
			Img userAvatar = new Img("", Storage.getAvatarPath(user.getAvatarid(), 16));
			A userLink = new A().setId("tag" + uid).setHref(AccountLinkGenerator.generatePreviewFullUserLink(AppContext.getSiteUrl(), user.getUsername()))
					.appendText(user.getDisplayName());
			userLink.setAttribute("onmouseover", TooltipHelper.userHoverJsFunction(uid, user.getUsername()));
			userLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction(uid));
			div.appendChild(userAvatar, DivLessFormatter.EMPTY_SPACE(), userLink, DivLessFormatter.EMPTY_SPACE(), TooltipHelper.buildDivTooltipEnable(uid));
			return new Label(div.write(), ContentMode.HTML);
		} else {
			return new Label("");
		}
	}

	@Override
	public String toString(String value) {
		if (org.apache.commons.lang3.StringUtils.isBlank(value)) {
			return new Span().write();
		}

		try {
			UserService userService = ApplicationContextUtil
					.getSpringBean(UserService.class);
			SimpleUser user = userService.findUserByUserNameInAccount(value,
					AppContext.getAccountId());
			if (user != null) {
				String userAvatarLink = MailUtils.getAvatarLink(
						user.getAvatarid(), 16);
				Img img = FormatUtils.newImg("avatar", userAvatarLink);

				String userLink = AccountLinkGenerator
						.generatePreviewFullUserLink(
								MailUtils.getSiteUrl(AppContext.getAccountId()),
								user.getUsername());

				A link = FormatUtils.newA(userLink, user.getDisplayName());
				return FormatUtils.newLink(img, link).write();
			}
		} catch (Exception e) {
			LOG.error("Error", e);
		}
		return value;
	}
}
