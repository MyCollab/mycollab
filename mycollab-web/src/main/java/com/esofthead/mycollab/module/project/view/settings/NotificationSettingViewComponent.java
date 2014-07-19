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

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.arguments.ValuedBean;
import com.esofthead.mycollab.core.persistence.service.ICrudService;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.i18n.NotificationSettingI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 * @param <B>
 * @param <S>
 */
public abstract class NotificationSettingViewComponent<B extends ValuedBean, S extends ICrudService>
		extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	private Map<String, String> levels = new LinkedHashMap<String, String>();

	protected B bean;
	protected S service;
	protected CssLayout mainLayout;
	protected String level;

	public NotificationSettingViewComponent(B bean, S service) {
		this.bean = bean;
		this.service = service;

		levels.put("Default", AppContext
				.getMessage(NotificationSettingI18nEnum.OPT_DEFAULT_SETTING));
		levels.put("None", AppContext
				.getMessage(NotificationSettingI18nEnum.OPT_NONE_SETTING));
		levels.put("Minimal", AppContext
				.getMessage(NotificationSettingI18nEnum.OPT_MINIMUM_SETTING));
		levels.put("Full", AppContext
				.getMessage(NotificationSettingI18nEnum.OPT_MAXIMUM_SETTING));

		constructBody();
	}

	private void constructBody() {
		this.setWidth("100%");
		this.setSpacing(true);
		this.addStyleName("readview-layout");
		this.setMargin(new MarginInfo(true, true, false, true));

		HorizontalLayout header = new HorizontalLayout();
		header.setWidth("100%");
		header.setSpacing(true);
		header.setMargin(new MarginInfo(true, false, true, false));
		header.setStyleName(UIConstants.HEADER_VIEW);

		final Image titleIcon = new Image(null,
				MyCollabResource.newResource("icons/22/crm/notification.png"));
		header.addComponent(titleIcon);
		header.setComponentAlignment(titleIcon, Alignment.MIDDLE_LEFT);

		final Label searchtitle = new Label(
				AppContext.getMessage(NotificationSettingI18nEnum.VIEW_TITLE));
		searchtitle.setStyleName(UIConstants.HEADER_TEXT);
		header.addComponent(searchtitle);
		header.setExpandRatio(searchtitle, 1.0f);
		header.setComponentAlignment(searchtitle, Alignment.MIDDLE_LEFT);

		this.addComponent(header);

		VerticalLayout bodyWrapper = new VerticalLayout();
		bodyWrapper.setSpacing(true);
		bodyWrapper.setMargin(new MarginInfo(true, false, false, false));
		bodyWrapper.setSizeFull();

		HorizontalLayout notificationLabelWrapper = new HorizontalLayout();
		notificationLabelWrapper.setSizeFull();
		notificationLabelWrapper.setMargin(true);

		notificationLabelWrapper.setStyleName("notification-label");

		Label notificationLabel = new Label(
				AppContext.getMessage(NotificationSettingI18nEnum.EXT_LEVEL));
		notificationLabel.addStyleName("h2");

		notificationLabel.setHeight(Sizeable.SIZE_UNDEFINED,
				Sizeable.Unit.PIXELS);
		notificationLabelWrapper.addComponent(notificationLabel);

		bodyWrapper.addComponent(notificationLabelWrapper);

		VerticalLayout body = new VerticalLayout();
		body.setSpacing(true);
		body.setMargin(new MarginInfo(true, false, false, false));

		final OptionGroup optionGroup = new OptionGroup(null);
		optionGroup.setItemCaptionMode(ItemCaptionMode.EXPLICIT);
		for (String level : levels.keySet()) {
			optionGroup.addItem(level);
			optionGroup.setItemCaption(level, levels.get(level));
		}

		optionGroup.setHeight("100%");

		body.addComponent(optionGroup);
		body.setExpandRatio(optionGroup, 1.0f);
		body.setComponentAlignment(optionGroup, Alignment.MIDDLE_LEFT);

		try {
			String levelVal = (String) PropertyUtils.getProperty(bean, "level");
			if (levelVal == null) {
				optionGroup.select("Default");
				level = "Default";
			} else {
				level = levelVal;
				optionGroup.select(level);
			}

			optionGroup.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					level = (String) optionGroup.getValue();
				}
			});

			Button upgradeBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_UPDATE_LABEL),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(ClickEvent event) {
							try {
								PropertyUtils.setProperty(bean, "level", level);
								Integer projectId = (CurrentProjectVariables
										.getProjectId() > 0) ? CurrentProjectVariables
										.getProjectId() : 1;
								PropertyUtils.setProperty(bean, "projectid",
										projectId);
								PropertyUtils.setProperty(bean, "saccountid",
										AppContext.getAccountId());
								PropertyUtils.setProperty(bean, "username",
										AppContext.getUsername());

								if ((Integer) PropertyUtils.getProperty(bean,
										"id") == null) {
									NotificationSettingViewComponent.this.service
											.saveWithSession(bean,
													AppContext.getUsername());
								} else {
									NotificationSettingViewComponent.this.service
											.updateWithSession(bean,
													AppContext.getUsername());
								}
								NotificationUtil.showNotification(AppContext
										.getMessage(NotificationSettingI18nEnum.DIALOG_UPDATE_SUCCESS));
							} catch (Exception e) {
								throw new MyCollabException(e);
							}
						}
					});
			upgradeBtn.addStyleName(UIConstants.THEME_GREEN_LINK);
			upgradeBtn.setIcon(MyCollabResource
					.newResource("icons/16/crm/refresh.png"));
			body.addComponent(upgradeBtn);
			body.setComponentAlignment(upgradeBtn, Alignment.BOTTOM_CENTER);
		} catch (Exception e) {
			throw new MyCollabException(e);
		}

		bodyWrapper.addComponent(body);
		this.addComponent(bodyWrapper);

	}
}