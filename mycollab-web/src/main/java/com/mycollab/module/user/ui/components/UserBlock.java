package com.mycollab.module.user.ui.components;

import com.hp.gagawa.java.elements.A;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.module.user.AccountLinkBuilder;
import com.mycollab.vaadin.TooltipHelper;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.vaadin.ui.Image;
import org.vaadin.viritin.layouts.MVerticalLayout;

import static com.mycollab.vaadin.TooltipHelper.TOOLTIP_ID;

/**
 * @author MyCollab Ltd.
 * @since 5.0.4
 */
public class UserBlock extends MVerticalLayout {
    public UserBlock(String username, String userAvatarId, String displayName) {
        withMargin(false).withWidth("80px");
        Image avatar = UserAvatarControlFactory.createUserAvatarEmbeddedComponent(userAvatarId, 48);
        avatar.addStyleName(UIConstants.CIRCLE_BOX);

        A userLink = new A().setId("tag" + TooltipHelper.TOOLTIP_ID).setHref(AccountLinkBuilder.generatePreviewFullUserLink(username))
                .appendText(StringUtils.trim(displayName, 30, true));
        userLink.setAttribute("onmouseover", TooltipHelper.userHoverJsFunction(username));
        userLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction());

        with(avatar, ELabel.html(userLink.write()));
    }
}
