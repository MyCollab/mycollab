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
package com.esofthead.mycollab.module.crm.view.activity;

import com.esofthead.mycollab.common.ModuleNameConstants;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.SimpleMeeting;
import com.esofthead.mycollab.module.crm.i18n.CrmCommonI18nEnum;
import com.esofthead.mycollab.module.crm.ui.CrmAssetsManager;
import com.esofthead.mycollab.module.crm.ui.components.*;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@ViewComponent
public class MeetingReadViewImpl extends AbstractPreviewItemComp<SimpleMeeting>
		implements MeetingReadView {

	private static final long serialVersionUID = 1L;

	protected NoteListItems noteListItems;
	private DateInfoComp dateInfoComp;
	private CrmFollowersComp<SimpleMeeting> followersComp;

	public MeetingReadViewImpl() {
		super(CrmAssetsManager.getAsset(CrmTypeConstants.MEETING));
	}

	@Override
	protected AdvancedPreviewBeanForm<SimpleMeeting> initPreviewForm() {
		return new AdvancedPreviewBeanForm<SimpleMeeting>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void showHistory() {
				final MeetingHistoryLogWindow historyLog = new MeetingHistoryLogWindow(
						ModuleNameConstants.CRM, CrmTypeConstants.MEETING);
				historyLog.loadHistory(beanItem.getId());
				UI.getCurrent().addWindow(historyLog);
			}
		};
	}

	@Override
	protected ComponentContainer createButtonControls() {
		return new CrmPreviewFormControlsGenerator<>(previewForm)
				.createButtonControls(RolePermissionCollections.CRM_MEETING);
	}

	@Override
	protected ComponentContainer createBottomPanel() {
		return noteListItems;
	}

	@Override
	protected void onPreviewItem() {
		displayNotes();
		dateInfoComp.displayEntryDateTime(beanItem);
		followersComp.displayFollowers(beanItem);
	}

	@Override
	protected String initFormTitle() {
		return beanItem.getSubject();
	}

	@Override
	protected void initRelatedComponents() {
		noteListItems = new NoteListItems(
				AppContext.getMessage(CrmCommonI18nEnum.TAB_NOTE));

		MVerticalLayout basicInfo = new MVerticalLayout().withWidth("100%").withStyleName("basic-info");

		CssLayout navigatorWrapper = previewItemContainer.getNavigatorWrapper();

		dateInfoComp = new DateInfoComp();
		basicInfo.addComponent(dateInfoComp);

		followersComp = new CrmFollowersComp<>(
				CrmTypeConstants.MEETING, RolePermissionCollections.CRM_MEETING);
		basicInfo.addComponent(followersComp);

		navigatorWrapper.addComponentAsFirst(basicInfo);

		previewItemContainer.addTab(previewContent, CrmTypeConstants.DETAIL,
				AppContext.getMessage(CrmCommonI18nEnum.TAB_ABOUT));
		previewItemContainer.selectTab(CrmTypeConstants.DETAIL);
	}

	@Override
	protected IFormLayoutFactory initFormLayoutFactory() {
		return new DynaFormLayout(CrmTypeConstants.MEETING,
				MeetingDefaultFormLayoutFactory.getForm());
	}

	@Override
	protected AbstractBeanFieldGroupViewFieldFactory<SimpleMeeting> initBeanFormFieldFactory() {
		return new MeetingReadFormFieldFactory(previewForm);
	}

	protected void displayNotes() {
		noteListItems.showNotes(CrmTypeConstants.MEETING, beanItem.getId());
	}

	@Override
	public SimpleMeeting getItem() {
		return beanItem;
	}

	@Override
	public HasPreviewFormHandlers<SimpleMeeting> getPreviewFormHandlers() {
		return previewForm;
	}
}
