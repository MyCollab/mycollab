package com.mycollab.mobile.module.project.ui;


import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.UIConstants;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.UI;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.2.5
 */
public class ProjectCommentRequestComp extends MHorizontalLayout {
    public ProjectCommentRequestComp(final String typeVal, final String typeIdVal, final Integer extraTypeIdVal) {
        withMargin(true);
        ELabel hintLbl = ELabel.html(FontAwesome.COMMENT.getHtml() + " " + UserUIContext.getMessage(GenericI18Enum.ACTION_ADD_COMMENT))
                .withStyleName(UIConstants.META_INFO);
        this.addComponent(hintLbl);
        this.addLayoutClickListener(layoutClickEvent -> {
            ((NavigationManager) UI.getCurrent().getContent()).navigateTo(new ProjectCommentInputView(typeVal, typeIdVal, extraTypeIdVal));
        });
    }
}
