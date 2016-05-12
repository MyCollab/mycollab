/**
 * This file is part of mycollab-ui.
 *
 * mycollab-ui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-ui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-ui.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.user.ui.format;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.html.FormatUtils;
import com.esofthead.mycollab.module.mail.MailUtils;
import com.esofthead.mycollab.module.user.AccountLinkGenerator;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.spring.AppContextUtil;
import com.esofthead.mycollab.utils.HistoryFieldFormat;
import com.esofthead.mycollab.vaadin.AppContext;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Img;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.esofthead.mycollab.core.utils.StringUtils.isBlank;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class UserHistoryFieldFormat implements HistoryFieldFormat {
    private static final Logger LOG = LoggerFactory.getLogger(UserHistoryFieldFormat.class);

    @Override
    public String toString(String value) {
        return toString(value, true, AppContext.getMessage(GenericI18Enum
                .FORM_EMPTY));
    }

    @Override
    public String toString(String value, Boolean displayAsHtml, String msgIfBlank) {
        if (isBlank(value)) {
            return msgIfBlank;
        }

        try {
            UserService userService = AppContextUtil.getSpringBean(UserService.class);
            SimpleUser user = userService.findUserByUserNameInAccount(value, AppContext.getAccountId());
            if (user != null) {
                if (displayAsHtml) {
                    String userAvatarLink = MailUtils.getAvatarLink(user.getAvatarid(), 16);
                    Img img = FormatUtils.newImg("avatar", userAvatarLink);

                    String userLink = AccountLinkGenerator.generatePreviewFullUserLink(
                            MailUtils.getSiteUrl(AppContext.getAccountId()), user.getUsername());

                    A link = FormatUtils.newA(userLink, user.getDisplayName());
                    return FormatUtils.newLink(img, link).write();
                } else {
                    return user.getDisplayName();
                }
            }
        } catch (Exception e) {
            LOG.error("Error", e);
        }
        return value;
    }
}
