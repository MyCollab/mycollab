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
package com.esofthead.mycollab.module.project.view.settings;

import com.esofthead.mycollab.common.NotificationType;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.module.project.domain.ProjectNotificationSetting;
import com.esofthead.mycollab.module.project.i18n.ProjectSettingI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectNotificationSettingService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.BlockWidget;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class ProjectNotificationSettingViewComponent extends BlockWidget {
    private static final long serialVersionUID = 1L;

    public ProjectNotificationSettingViewComponent(final ProjectNotificationSetting bean) {
        super(AppContext.getMessage(ProjectSettingI18nEnum.VIEW_TITLE));

        MVerticalLayout bodyWrapper = new MVerticalLayout();
        bodyWrapper.setSizeFull();

        HorizontalLayout notificationLabelWrapper = new HorizontalLayout();
        notificationLabelWrapper.setSizeFull();
        notificationLabelWrapper.setMargin(true);

        notificationLabelWrapper.setStyleName("notification-label");

        Label notificationLabel = new Label(
                AppContext.getMessage(ProjectSettingI18nEnum.EXT_LEVEL));
        notificationLabel.addStyleName("h2");

        notificationLabel.setHeightUndefined();
        notificationLabelWrapper.addComponent(notificationLabel);

        bodyWrapper.addComponent(notificationLabelWrapper);

        MVerticalLayout body = new MVerticalLayout().withMargin(new MarginInfo(true, false, false, false));

        final OptionGroup optionGroup = new OptionGroup(null);
        optionGroup.setItemCaptionMode(ItemCaptionMode.EXPLICIT);

        optionGroup.addItem(NotificationType.Default.name());
        optionGroup.setItemCaption(NotificationType.Default.name(), AppContext
                .getMessage(ProjectSettingI18nEnum.OPT_DEFAULT_SETTING));

        optionGroup.addItem(NotificationType.None.name());
        optionGroup.setItemCaption(NotificationType.None.name(),
                AppContext.getMessage(ProjectSettingI18nEnum.OPT_NONE_SETTING));

        optionGroup.addItem(NotificationType.Minimal.name());
        optionGroup.setItemCaption(NotificationType.Minimal.name(), AppContext
                .getMessage(ProjectSettingI18nEnum.OPT_MINIMUM_SETTING));

        optionGroup.addItem(NotificationType.Full.name());
        optionGroup.setItemCaption(NotificationType.Full.name(), AppContext
                .getMessage(ProjectSettingI18nEnum.OPT_MAXIMUM_SETTING));

        optionGroup.setHeight("100%");

        body.with(optionGroup).expand(optionGroup).withAlign(optionGroup, Alignment.MIDDLE_LEFT);

        String levelVal = bean.getLevel();
        if (levelVal == null) {
            optionGroup.select(NotificationType.Default.name());
        } else {
            optionGroup.select(levelVal);
        }

        Button updateBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_UPDATE_LABEL),
                new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(ClickEvent event) {
                        try {
                            bean.setLevel((String) optionGroup.getValue());
                            ProjectNotificationSettingService projectNotificationSettingService = ApplicationContextUtil
                                    .getSpringBean(ProjectNotificationSettingService.class);

                            if (bean.getId() == null) {
                                projectNotificationSettingService
                                        .saveWithSession(bean,
                                                AppContext.getUsername());
                            } else {
                                projectNotificationSettingService.updateWithSession(bean,
                                        AppContext.getUsername());
                            }
                            NotificationUtil.showNotification("Congrats", AppContext
                                    .getMessage(ProjectSettingI18nEnum.DIALOG_UPDATE_SUCCESS));
                        } catch (Exception e) {
                            throw new MyCollabException(e);
                        }
                    }
                });
        updateBtn.addStyleName(UIConstants.THEME_GREEN_LINK);
        updateBtn.setIcon(FontAwesome.REFRESH);
        body.addComponent(updateBtn);
        body.setComponentAlignment(updateBtn, Alignment.BOTTOM_LEFT);

        bodyWrapper.addComponent(body);
        this.addComponent(bodyWrapper);

    }
}