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

import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.user.AccountLinkBuilder;
import com.esofthead.mycollab.utils.TooltipHelper;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.hp.gagawa.java.elements.A;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import org.vaadin.viritin.layouts.MVerticalLayout;

import static com.esofthead.mycollab.utils.TooltipHelper.TOOLTIP_ID;

/**
 * @author MyCollab Ltd.
 * @since 5.0.4
 */
public class UserBlock extends MVerticalLayout {
    public UserBlock(String username, String userAvatarId, String displayName) {
        withMargin(false).withWidth("80px");
        Image avatar = UserAvatarControlFactory.createUserAvatarEmbeddedComponent(userAvatarId, 64);
        avatar.addStyleName(UIConstants.CIRCLE_BOX);

        A userLink = new A().setId("tag" + TOOLTIP_ID).setHref(AccountLinkBuilder.generatePreviewFullUserLink(username))
                .appendText(StringUtils.trim(displayName, 30, true));
        userLink.setAttribute("onmouseover", TooltipHelper.userHoverJsFunction(username));
        userLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction());

        Label userLbl = new Label(userLink.write(), ContentMode.HTML);
        with(avatar, userLbl);
    }
}
