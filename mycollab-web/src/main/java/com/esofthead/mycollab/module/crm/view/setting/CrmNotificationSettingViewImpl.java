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
package com.esofthead.mycollab.module.crm.view.setting;

import com.esofthead.mycollab.common.NotificationType;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.module.crm.domain.CrmNotificationSetting;
import com.esofthead.mycollab.module.crm.service.CrmNotificationSettingService;
import com.esofthead.mycollab.module.project.i18n.ProjectSettingI18nEnum;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.BlockWidget;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.WebResourceIds;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 *
 */
@ViewComponent
public class CrmNotificationSettingViewImpl extends AbstractPageView implements
		CrmNotificationSettingView {
	private static final long serialVersionUID = 1L;

	@Override
	public void showNotificationSettings(CrmNotificationSetting notification) {
		this.removeAllComponents();

		SettingComp component = new SettingComp(notification);
		this.addComponent(component);
	}

	private static class SettingComp extends BlockWidget {
		private static final long serialVersionUID = 1L;

		public SettingComp(final CrmNotificationSetting notification) {
			VerticalLayout bodyWrapper = new VerticalLayout();
			bodyWrapper.setSpacing(true);
			bodyWrapper.setMargin(true);
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

			VerticalLayout body = new VerticalLayout();
			body.setSpacing(true);
			body.setMargin(new MarginInfo(true, false, false, false));

			final OptionGroup optionGroup = new OptionGroup(null);
			optionGroup.setItemCaptionMode(ItemCaptionMode.EXPLICIT);

			optionGroup.addItem(NotificationType.Default.name());
			optionGroup
					.setItemCaption(
							NotificationType.Default.name(),
							AppContext
									.getMessage(ProjectSettingI18nEnum.OPT_DEFAULT_SETTING));

			optionGroup.addItem(NotificationType.None.name());
			optionGroup.setItemCaption(NotificationType.None.name(), AppContext
					.getMessage(ProjectSettingI18nEnum.OPT_NONE_SETTING));

			optionGroup.addItem(NotificationType.Minimal.name());
			optionGroup
					.setItemCaption(
							NotificationType.Minimal.name(),
							AppContext
									.getMessage(ProjectSettingI18nEnum.OPT_MINIMUM_SETTING));

			optionGroup.addItem(NotificationType.Full.name());
			optionGroup.setItemCaption(NotificationType.Full.name(), AppContext
					.getMessage(ProjectSettingI18nEnum.OPT_MAXIMUM_SETTING));

			optionGroup.setHeight("100%");

			body.addComponent(optionGroup);
			body.setExpandRatio(optionGroup, 1.0f);
			body.setComponentAlignment(optionGroup, Alignment.MIDDLE_LEFT);

			String levelVal = notification.getLevel();
			if (levelVal == null) {
				optionGroup.select(NotificationType.Default.name());
			} else {
				optionGroup.select(levelVal);
			}

			Button updateBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_UPDATE_LABEL),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(ClickEvent event) {
							try {
								notification.setLevel((String) optionGroup
										.getValue());
								CrmNotificationSettingService crmNotificationSettingService = ApplicationContextUtil
										.getSpringBean(CrmNotificationSettingService.class);
								if (notification.getId() == null) {
									crmNotificationSettingService
											.saveWithSession(notification,
													AppContext.getUsername());
								} else {
									crmNotificationSettingService
											.updateWithSession(notification,
													AppContext.getUsername());
								}
								NotificationUtil.showNotification(AppContext
										.getMessage(ProjectSettingI18nEnum.DIALOG_UPDATE_SUCCESS));
							} catch (Exception e) {
								throw new MyCollabException(e);
							}
						}
					});
			updateBtn.addStyleName(UIConstants.THEME_GREEN_LINK);
			updateBtn.setIcon(MyCollabResource
					.newResource(WebResourceIds._16_crm_refresh));
			body.addComponent(updateBtn);
			body.setComponentAlignment(updateBtn, Alignment.BOTTOM_LEFT);

			bodyWrapper.addComponent(body);
			this.addComponent(bodyWrapper);
		}
	}
}
