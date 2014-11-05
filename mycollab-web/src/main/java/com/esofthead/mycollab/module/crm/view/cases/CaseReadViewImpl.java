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
package com.esofthead.mycollab.module.crm.view.cases;

import com.esofthead.mycollab.common.ModuleNameConstants;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.SimpleActivity;
import com.esofthead.mycollab.module.crm.domain.SimpleCase;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.domain.criteria.ActivitySearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.CrmCommonI18nEnum;
import com.esofthead.mycollab.module.crm.ui.components.AbstractPreviewItemComp;
import com.esofthead.mycollab.module.crm.ui.components.CrmFollowersComp;
import com.esofthead.mycollab.module.crm.ui.components.CrmPreviewFormControlsGenerator;
import com.esofthead.mycollab.module.crm.ui.components.DateInfoComp;
import com.esofthead.mycollab.module.crm.ui.components.DynaFormLayout;
import com.esofthead.mycollab.module.crm.ui.components.NoteListItems;
import com.esofthead.mycollab.module.crm.ui.components.PeopleInfoComp;
import com.esofthead.mycollab.module.crm.view.activity.ActivityRelatedItemListComp;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.IRelatedListHandlers;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@ViewComponent
public class CaseReadViewImpl extends AbstractPreviewItemComp<SimpleCase>
		implements CaseReadView {

	private static final long serialVersionUID = 1L;
	protected CaseContactListComp associateContactList;
	protected NoteListItems noteListItems;
	protected ActivityRelatedItemListComp associateActivityList;

	private PeopleInfoComp peopleInfoComp;
	private DateInfoComp dateInfoComp;
	private CrmFollowersComp<SimpleCase> followersComp;

	public CaseReadViewImpl() {
		super(MyCollabResource.newResource("icons/22/crm/case.png"));
	}

	@Override
	protected AdvancedPreviewBeanForm<SimpleCase> initPreviewForm() {
		return new AdvancedPreviewBeanForm<SimpleCase>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void showHistory() {
				CaseHistoryLogWindow historyLog = new CaseHistoryLogWindow(
						ModuleNameConstants.CRM, CrmTypeConstants.CASE);
				historyLog.loadHistory(beanItem.getId());
				UI.getCurrent().addWindow(historyLog);
			}
		};
	}

	@Override
	protected ComponentContainer createButtonControls() {
		return new CrmPreviewFormControlsGenerator<SimpleCase>(previewForm)
				.createButtonControls(RolePermissionCollections.CRM_CASE);
	}

	@Override
	protected ComponentContainer createBottomPanel() {
		return noteListItems;
	}

	@Override
	protected void onPreviewItem() {
		displayNotes();
		displayActivities();
		displayContacts();

		peopleInfoComp.displayEntryPeople(beanItem);
		dateInfoComp.displayEntryDateTime(beanItem);
		followersComp.displayFollowers(beanItem);

		previewItemContainer.selectTab("about");
	}

	@Override
	protected String initFormTitle() {
		return beanItem.getSubject();
	}

	@Override
	protected void initRelatedComponents() {
		associateContactList = new CaseContactListComp();
		associateActivityList = new ActivityRelatedItemListComp(true);
		noteListItems = new NoteListItems(
				AppContext.getMessage(CrmCommonI18nEnum.TAB_NOTE));

		CssLayout navigatorWrapper = previewItemContainer.getNavigatorWrapper();
		VerticalLayout basicInfo = new VerticalLayout();
		basicInfo.setWidth("100%");
		basicInfo.setMargin(true);
		basicInfo.setSpacing(true);
		basicInfo.setStyleName("basic-info");

		dateInfoComp = new DateInfoComp();
		basicInfo.addComponent(dateInfoComp);

		peopleInfoComp = new PeopleInfoComp();
		basicInfo.addComponent(peopleInfoComp);

		followersComp = new CrmFollowersComp<SimpleCase>(CrmTypeConstants.CASE,
				RolePermissionCollections.CRM_CASE);
		basicInfo.addComponent(followersComp);

		navigatorWrapper.addComponentAsFirst(basicInfo);

		previewItemContainer.addTab(previewContent, "about",
				AppContext.getMessage(CrmCommonI18nEnum.TAB_ABOUT));
		previewItemContainer.addTab(associateContactList, "contact",
				AppContext.getMessage(CrmCommonI18nEnum.TAB_CONTACT));
		previewItemContainer.addTab(associateActivityList, "activity",
				AppContext.getMessage(CrmCommonI18nEnum.TAB_ACTIVITY));
	}

	@Override
	protected IFormLayoutFactory initFormLayoutFactory() {
		return new DynaFormLayout(CrmTypeConstants.CASE,
				CasesDefaultFormLayoutFactory.getForm());
	}

	@Override
	protected AbstractBeanFieldGroupViewFieldFactory<SimpleCase> initBeanFormFieldFactory() {
		return new CaseReadFormFieldFactory(previewForm);
	}

	protected void displayNotes() {
		noteListItems.showNotes(CrmTypeConstants.CASE, beanItem.getId());
	}

	protected void displayActivities() {
		ActivitySearchCriteria criteria = new ActivitySearchCriteria();
		criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
		criteria.setType(new StringSearchField(SearchField.AND,
				CrmTypeConstants.CASE));
		criteria.setTypeid(new NumberSearchField(beanItem.getId()));
		associateActivityList.setSearchCriteria(criteria);
	}

	protected void displayContacts() {
		associateContactList.displayContacts(beanItem);
	}

	@Override
	public SimpleCase getItem() {
		return beanItem;
	}

	@Override
	public HasPreviewFormHandlers<SimpleCase> getPreviewFormHandlers() {
		return previewForm;
	}

	@Override
	public IRelatedListHandlers<SimpleActivity> getRelatedActivityHandlers() {
		return associateActivityList;
	}

	@Override
	public IRelatedListHandlers<SimpleContact> getRelatedContactHandlers() {
		return associateContactList;
	}
}
