/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.view.setting;

import com.mycollab.module.crm.domain.CrmNotificationSetting;
import com.mycollab.vaadin.mvp.AbstractVerticalPageView;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.vaadin.shared.ui.MarginInfo;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
// TODO
@ViewComponent
public class CrmNotificationSettingViewImpl extends AbstractVerticalPageView implements CrmNotificationSettingView {
    private static final long serialVersionUID = 1L;

    @Override
    public void showNotificationSettings(final CrmNotificationSetting notification) {
        this.removeAllComponents();

        MVerticalLayout bodyWrapper = new MVerticalLayout();
        bodyWrapper.setSizeFull();

        MVerticalLayout body = new MVerticalLayout().withMargin(new MarginInfo(true, false, false, false));

//        final OptionGroup optionGroup = new OptionGroup(null);
//        optionGroup.setItemCaptionMode(ItemCaptionMode.EXPLICIT);
//
//        optionGroup.addItem(NotificationType.Default.name());
//        optionGroup.setItemCaption(NotificationType.Default.name(),
//                UserUIContext.getMessage(ProjectSettingI18nEnum.OPT_DEFAULT_SETTING));
//
//        optionGroup.addItem(NotificationType.None.name());
//        optionGroup.setItemCaption(NotificationType.None.name(), UserUIContext
//                .getMessage(ProjectSettingI18nEnum.OPT_NONE_SETTING));
//
//        optionGroup.addItem(NotificationType.Minimal.name());
//        optionGroup.setItemCaption(NotificationType.Minimal.name(),
//                UserUIContext.getMessage(ProjectSettingI18nEnum.OPT_MINIMUM_SETTING));
//
//        optionGroup.addItem(NotificationType.Full.name());
//        optionGroup.setItemCaption(NotificationType.Full.name(), UserUIContext
//                .getMessage(ProjectSettingI18nEnum.OPT_MAXIMUM_SETTING));
//
//        optionGroup.setHeight("100%");
//
//        body.with(optionGroup).withAlign(optionGroup, Alignment.MIDDLE_LEFT).expand(optionGroup);
//
//        String levelVal = notification.getLevel();
//        if (levelVal == null) {
//            optionGroup.select(NotificationType.Default.name());
//        } else {
//            optionGroup.select(levelVal);
//        }
//
//        Button updateBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_UPDATE_LABEL), clickEvent -> {
//            try {
//                notification.setLevel((String) optionGroup.getValue());
//                CrmNotificationSettingService crmNotificationSettingService = AppContextUtil
//                        .getSpringBean(CrmNotificationSettingService.class);
//                if (notification.getId() == null) {
//                    crmNotificationSettingService.saveWithSession(notification, UserUIContext.getUsername());
//                } else {
//                    crmNotificationSettingService.updateWithSession(notification, UserUIContext.getUsername());
//                }
//                NotificationUtil.showNotification(UserUIContext.getMessage(GenericI18Enum.OPT_CONGRATS),
//                        UserUIContext.getMessage(ProjectSettingI18nEnum.DIALOG_UPDATE_SUCCESS));
//            } catch (Exception e) {
//                throw new MyCollabException(e);
//            }
//        }).withIcon(VaadinIcons.REFRESH).withStyleName(WebThemes.BUTTON_ACTION);
//        body.with(updateBtn).withAlign(updateBtn, Alignment.BOTTOM_LEFT);
//
//        bodyWrapper.addComponent(body);
//        this.addComponent(bodyWrapper);
    }
}
