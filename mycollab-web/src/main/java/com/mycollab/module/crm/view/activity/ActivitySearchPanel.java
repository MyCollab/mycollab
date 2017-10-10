/**
 * mycollab-web - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.view.activity;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.criteria.ActivitySearchCriteria;
import com.mycollab.module.crm.event.ActivityEvent;
import com.mycollab.module.crm.i18n.CallI18nEnum;
import com.mycollab.module.crm.i18n.MeetingI18nEnum;
import com.mycollab.module.crm.i18n.TaskI18nEnum;
import com.mycollab.module.crm.ui.components.ComponentUtils;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.HeaderWithFontAwesome;
import com.mycollab.vaadin.web.ui.*;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import org.vaadin.peter.buttongroup.ButtonGroup;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class ActivitySearchPanel extends DefaultGenericSearchPanel<ActivitySearchCriteria> {
    private static final long serialVersionUID = 1L;

    @Override
    protected SearchLayout<ActivitySearchCriteria> createAdvancedSearchLayout() {
        return null;
    }

    @Override
    protected HeaderWithFontAwesome buildSearchTitle() {
        return ComponentUtils.header(CrmTypeConstants.ACTIVITY, "Events");
    }

    @Override
    protected Component buildExtraControls() {
        final SplitButton splitBtn = new SplitButton();
        splitBtn.setSizeUndefined();
        splitBtn.setEnabled(UserUIContext.canWrite(RolePermissionCollections.CRM_CALL) || UserUIContext.canWrite(RolePermissionCollections.CRM_MEETING));
        splitBtn.addStyleName(WebThemes.BUTTON_ACTION);
        splitBtn.setIcon(FontAwesome.PLUS);
        splitBtn.setCaption(UserUIContext.getMessage(TaskI18nEnum.NEW));
        splitBtn.addClickListener(event -> EventBusFactory.getInstance().post(new ActivityEvent.TaskAdd(this, null)));

        OptionPopupContent btnControlsLayout = new OptionPopupContent();
        splitBtn.setContent(btnControlsLayout);

        Button createMeetingBtn = new Button(UserUIContext.getMessage(MeetingI18nEnum.NEW), clickEvent -> {
            splitBtn.setPopupVisible(false);
            EventBusFactory.getInstance().post(new ActivityEvent.MeetingAdd(this, null));
        });
        btnControlsLayout.addOption(createMeetingBtn);
        createMeetingBtn.setEnabled(UserUIContext.canWrite(RolePermissionCollections.CRM_MEETING));
        final Button createCallBtn = new Button(UserUIContext.getMessage(CallI18nEnum.NEW), clickEvent -> {
            splitBtn.setPopupVisible(false);
            EventBusFactory.getInstance().post(new ActivityEvent.CallAdd(this, null));
        });
        createCallBtn.setEnabled(UserUIContext.canWrite(RolePermissionCollections.CRM_CALL));
        btnControlsLayout.addOption(createCallBtn);

        ButtonGroup viewSwitcher = new ButtonGroup();

        Button calendarViewBtn = new Button("Calendar", clickEvent -> EventBusFactory.getInstance().post(new ActivityEvent.GotoCalendar(this, null)));
        calendarViewBtn.addStyleName(WebThemes.BUTTON_ACTION);
        viewSwitcher.addButton(calendarViewBtn);

        Button activityListBtn = new Button("Activities");
        activityListBtn.setStyleName("selected");
        activityListBtn.addStyleName(WebThemes.BUTTON_ACTION);
        viewSwitcher.addButton(activityListBtn);

        return new MHorizontalLayout(splitBtn, viewSwitcher);
    }

    @Override
    protected SearchLayout<ActivitySearchCriteria> createBasicSearchLayout() {
        return new EventBasicSearchLayout();
    }

    private class EventBasicSearchLayout extends BasicSearchLayout<ActivitySearchCriteria> {

        private TextField nameField;
        private CheckBox myItemCheckbox;

        EventBasicSearchLayout() {
            super(ActivitySearchPanel.this);
        }

        @Override
        public ComponentContainer constructBody() {
            MHorizontalLayout basicSearchBody = new MHorizontalLayout().withMargin(true);

            nameField = new MTextField().withInputPrompt(UserUIContext.getMessage(GenericI18Enum.ACTION_QUERY_BY_TEXT))
                    .withWidth(WebUIConstants.DEFAULT_CONTROL_WIDTH);
            basicSearchBody.with(nameField).withAlign(nameField, Alignment.MIDDLE_LEFT);

            this.myItemCheckbox = new CheckBox(UserUIContext.getMessage(GenericI18Enum.OPT_MY_ITEMS));
            basicSearchBody.with(myItemCheckbox).withAlign(myItemCheckbox, Alignment.MIDDLE_CENTER);

            MButton searchBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SEARCH), clickEvent -> callSearchAction())
                    .withIcon(FontAwesome.SEARCH).withStyleName(WebThemes.BUTTON_ACTION)
                    .withClickShortcut(ShortcutAction.KeyCode.ENTER);
            basicSearchBody.with(searchBtn).withAlign(searchBtn, Alignment.MIDDLE_LEFT);

            MButton clearBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CLEAR), clickEvent -> nameField.setValue(""))
                    .withStyleName(WebThemes.BUTTON_OPTION);
            basicSearchBody.with(clearBtn).withAlign(clearBtn, Alignment.MIDDLE_LEFT);
            return basicSearchBody;
        }

        @Override
        protected ActivitySearchCriteria fillUpSearchCriteria() {
            ActivitySearchCriteria searchCriteria = new ActivitySearchCriteria();
            searchCriteria.setSaccountid(new NumberSearchField(AppUI.getAccountId()));
            return searchCriteria;
        }
    }
}
