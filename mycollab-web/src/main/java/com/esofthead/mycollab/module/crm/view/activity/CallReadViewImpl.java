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
import com.esofthead.mycollab.module.crm.domain.SimpleCall;
import com.esofthead.mycollab.module.crm.i18n.CrmCommonI18nEnum;
import com.esofthead.mycollab.module.crm.ui.components.AbstractPreviewItemComp;
import com.esofthead.mycollab.module.crm.ui.components.CrmFollowersComp;
import com.esofthead.mycollab.module.crm.ui.components.CrmPreviewFormControlsGenerator;
import com.esofthead.mycollab.module.crm.ui.components.DateInfoComp;
import com.esofthead.mycollab.module.crm.ui.components.DynaFormLayout;
import com.esofthead.mycollab.module.crm.ui.components.NoteListItems;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 */
@ViewComponent
public class CallReadViewImpl extends AbstractPreviewItemComp<SimpleCall>
		implements CallReadView {

	private static final long serialVersionUID = 1L;

	protected NoteListItems noteListItems;
	private DateInfoComp dateInfoComp;
	private CrmFollowersComp<SimpleCall> followersComp;

	public CallReadViewImpl() {
		super(MyCollabResource.newResource("icons/22/crm/call.png"));
	}

	@Override
	protected AdvancedPreviewBeanForm<SimpleCall> initPreviewForm() {
		return new AdvancedPreviewBeanForm<SimpleCall>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void showHistory() {
				final CallHistoryLogWindow historyLog = new CallHistoryLogWindow(
						ModuleNameConstants.CRM, CrmTypeConstants.CALL);
				historyLog.loadHistory(beanItem.getId());
				UI.getCurrent().addWindow(historyLog);
			}
		};
	}

	@Override
	protected ComponentContainer createButtonControls() {
		return new CrmPreviewFormControlsGenerator<SimpleCall>(previewForm)
				.createButtonControls(RolePermissionCollections.CRM_CALL);
	}

	@Override
	protected ComponentContainer createBottomPanel() {
		return noteListItems;
	}

	@Override
	protected void onPreviewItem() {
		this.displayNotes();
		dateInfoComp.displayEntryDateTime(beanItem);
		followersComp.displayFollowers(beanItem);
	}

	@Override
	protected String initFormTitle() {
		return beanItem.getSubject();
	}

	@Override
	protected void initRelatedComponents() {
		this.noteListItems = new NoteListItems(
				AppContext.getMessage(CrmCommonI18nEnum.TAB_NOTE));

		VerticalLayout basicInfo = new VerticalLayout();
		basicInfo.setWidth("100%");
		basicInfo.setMargin(true);
		basicInfo.setSpacing(true);
		basicInfo.setStyleName("basic-info");

		CssLayout navigatorWrapper = previewItemContainer.getNavigatorWrapper();

		dateInfoComp = new DateInfoComp();
		basicInfo.addComponent(dateInfoComp);

		followersComp = new CrmFollowersComp<SimpleCall>(CrmTypeConstants.CALL,
				RolePermissionCollections.CRM_CALL);
		basicInfo.addComponent(followersComp);

		navigatorWrapper.addComponentAsFirst(basicInfo);

		previewItemContainer.addTab(previewContent, "about",
				AppContext.getMessage(CrmCommonI18nEnum.TAB_ABOUT));
		previewItemContainer.selectTab("about");
	}

	@Override
	protected IFormLayoutFactory initFormLayoutFactory() {
		return new DynaFormLayout(CrmTypeConstants.CALL,
				CallDefaultFormLayoutFactory.getForm());
	}

	@Override
	protected AbstractBeanFieldGroupViewFieldFactory<SimpleCall> initBeanFormFieldFactory() {
		return new CallReadFormFieldFactory(previewForm);
	}

	protected void displayNotes() {
		this.noteListItems.showNotes(CrmTypeConstants.CALL, beanItem.getId());
	}

	@Override
	public SimpleCall getItem() {
		return beanItem;
	}

	@Override
	public HasPreviewFormHandlers<SimpleCall> getPreviewFormHandlers() {
		return previewForm;
	}
}
