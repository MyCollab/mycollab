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
package com.mycollab.module.user.ui.components;

import com.hp.gagawa.java.elements.A;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.module.user.AccountLinkGenerator;
import com.mycollab.vaadin.TooltipHelper;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.ui.Image;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 5.0.4
 */
public class UserBlock extends MVerticalLayout {
    public UserBlock(String username, String userAvatarId, String displayName) {
        withMargin(false).withWidth("80px");
        Image avatar = UserAvatarControlFactory.createUserAvatarEmbeddedComponent(userAvatarId, 48);
        avatar.addStyleName(WebThemes.CIRCLE_BOX);

        A userLink = new A().setId("tag" + TooltipHelper.TOOLTIP_ID).setHref(AccountLinkGenerator.generateUserLink(username))
                .appendText(StringUtils.trim(displayName, 30, true));
        userLink.setAttribute("onmouseover", TooltipHelper.userHoverJsFunction(username));
        userLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction());

        with(avatar, ELabel.html(userLink.write()));
    }
}
