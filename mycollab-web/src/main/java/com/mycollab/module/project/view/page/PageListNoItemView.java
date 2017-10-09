package com.mycollab.module.project.view.page;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.event.PageEvent;
import com.mycollab.module.project.i18n.PageI18nEnum;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.ui.components.ProjectListNoItemView;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 5.0.2
 */
public class PageListNoItemView extends ProjectListNoItemView {
    @Override
    protected FontAwesome viewIcon() {
        return ProjectAssetsManager.getAsset(ProjectTypeConstants.PAGE);
    }

    @Override
    protected String viewTitle() {
        return UserUIContext.getMessage(GenericI18Enum.VIEW_NO_ITEM_TITLE);
    }

    @Override
    protected String viewHint() {
        return UserUIContext.getMessage(GenericI18Enum.VIEW_NO_ITEM_HINT);
    }

    @Override
    protected String actionMessage() {
        return UserUIContext.getMessage(PageI18nEnum.NEW);
    }

    @Override
    protected Button.ClickListener actionListener() {
        return new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                EventBusFactory.getInstance().post(new PageEvent.GotoAdd(this, null));
            }
        };
    }

    protected MHorizontalLayout createControlButtons() {
        if (hasPermission()) {
            MButton createPageBtn = new MButton(actionMessage(), actionListener()).withStyleName(WebThemes.BUTTON_ACTION);

            MButton createPageGroupBtn = new MButton(UserUIContext.getMessage(PageI18nEnum.NEW_GROUP), clickEvent -> {
                UI.getCurrent().addWindow(new GroupPageAddWindow());
            }).withStyleName(WebThemes.BUTTON_ACTION);

            return new MHorizontalLayout(createPageBtn, new Label(" or "), createPageGroupBtn)
                    .alignAll(Alignment.MIDDLE_CENTER);
        } else {
            return new MHorizontalLayout();
        }
    }

    @Override
    protected boolean hasPermission() {
        return CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.PAGES);
    }
}