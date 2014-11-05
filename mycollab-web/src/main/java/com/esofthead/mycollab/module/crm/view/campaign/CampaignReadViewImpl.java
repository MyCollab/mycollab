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
package com.esofthead.mycollab.module.crm.view.campaign;

import java.util.Date;
import java.util.GregorianCalendar;

import com.esofthead.mycollab.common.ModuleNameConstants;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.SimpleAccount;
import com.esofthead.mycollab.module.crm.domain.SimpleActivity;
import com.esofthead.mycollab.module.crm.domain.SimpleCampaign;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
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
public class CampaignReadViewImpl extends
		AbstractPreviewItemComp<SimpleCampaign> implements CampaignReadView {

	private static final long serialVersionUID = 1L;

	protected CampaignAccountListComp associateAccountList;
	protected CampaignContactListComp associateContactList;
	protected CampaignLeadListComp associateLeadList;
	protected ActivityRelatedItemListComp associateActivityList;
	protected NoteListItems noteListItems;

	private PeopleInfoComp peopleInfoComp;
	private DateInfoComp dateInfoComp;
	private CrmFollowersComp<SimpleCampaign> compFollowers;

	public CampaignReadViewImpl() {
		super(MyCollabResource.newResource("icons/22/crm/campaign.png"));
	}

	@Override
	protected AdvancedPreviewBeanForm<SimpleCampaign> initPreviewForm() {
		return new AdvancedPreviewBeanForm<SimpleCampaign>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void showHistory() {
				final CampaignHistoryLogWindow historyLog = new CampaignHistoryLogWindow(
						ModuleNameConstants.CRM, CrmTypeConstants.CAMPAIGN);
				historyLog.loadHistory(beanItem.getId());
				UI.getCurrent().addWindow(historyLog);
			}
		};
	}

	@Override
	protected ComponentContainer createButtonControls() {
		return new CrmPreviewFormControlsGenerator<SimpleCampaign>(previewForm)
				.createButtonControls(RolePermissionCollections.CRM_CAMPAIGN);
	}

	@Override
	protected ComponentContainer createBottomPanel() {
		return noteListItems;
	}

	@Override
	protected void initRelatedComponents() {
		associateAccountList = new CampaignAccountListComp();
		associateContactList = new CampaignContactListComp();
		associateLeadList = new CampaignLeadListComp();
		associateActivityList = new ActivityRelatedItemListComp(true);
		noteListItems = new NoteListItems(
				AppContext.getMessage(CrmCommonI18nEnum.TAB_NOTE));

		VerticalLayout basicInfo = new VerticalLayout();
		basicInfo.setWidth("100%");
		basicInfo.setMargin(true);
		basicInfo.setSpacing(true);
		basicInfo.setStyleName("basic-info");

		CssLayout navigatorWrapper = previewItemContainer.getNavigatorWrapper();

		dateInfoComp = new DateInfoComp();
		basicInfo.addComponent(dateInfoComp);

		peopleInfoComp = new PeopleInfoComp();
		basicInfo.addComponent(peopleInfoComp);

		compFollowers = new CrmFollowersComp<SimpleCampaign>(
				CrmTypeConstants.CAMPAIGN,
				RolePermissionCollections.CRM_CAMPAIGN);
		basicInfo.addComponent(compFollowers);

		navigatorWrapper.addComponentAsFirst(basicInfo);

		previewItemContainer.addTab(previewContent, "about",
				AppContext.getMessage(CrmCommonI18nEnum.TAB_ABOUT));
		previewItemContainer.addTab(associateAccountList, "account",
				AppContext.getMessage(CrmCommonI18nEnum.TAB_ACCOUNT));
		previewItemContainer.addTab(associateContactList, "contact",
				AppContext.getMessage(CrmCommonI18nEnum.TAB_CONTACT));
		previewItemContainer.addTab(associateLeadList, "lead",
				AppContext.getMessage(CrmCommonI18nEnum.TAB_LEAD));
		previewItemContainer.addTab(associateActivityList, "activity",
				AppContext.getMessage(CrmCommonI18nEnum.TAB_ACTIVITY));
	}

	protected void displayNotes() {
		noteListItems.showNotes(CrmTypeConstants.CAMPAIGN, beanItem.getId());
	}

	protected void displayActivities() {
		ActivitySearchCriteria criteria = new ActivitySearchCriteria();
		criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
		criteria.setType(new StringSearchField(SearchField.AND,
				CrmTypeConstants.CAMPAIGN));
		criteria.setTypeid(new NumberSearchField(beanItem.getId()));
		associateActivityList.setSearchCriteria(criteria);
	}

	protected void displayAccounts() {
		associateAccountList.displayAccounts(beanItem);
	}

	protected void displayContacts() {
		associateContactList.displayContacts(beanItem);
	}

	protected void displayLeads() {
		associateLeadList.displayLeads(beanItem);
	}

	@Override
	public AdvancedPreviewBeanForm<SimpleCampaign> getPreviewForm() {
		return previewForm;
	}

	@Override
	protected void onPreviewItem() {
		displayActivities();
		displayAccounts();
		displayContacts();
		displayLeads();
		displayNotes();

		dateInfoComp.displayEntryDateTime(beanItem);
		peopleInfoComp.displayEntryPeople(beanItem);
		compFollowers.displayFollowers(beanItem);

		previewItemContainer.selectTab("about");

		previewLayout.resetTitleStyle();

		Date now = new GregorianCalendar().getTime();
		String status = this.beanItem.getStatus();
		if (!"Complete".equals(status)
				&& (this.beanItem.getEnddate() != null && this.beanItem
						.getEnddate().before(now))) {
			previewLayout.setTitleStyleName("hdr-text-overdue");
		}
	}

	@Override
	protected String initFormTitle() {
		return beanItem.getCampaignname();
	}

	@Override
	protected IFormLayoutFactory initFormLayoutFactory() {
		return new DynaFormLayout(CrmTypeConstants.CAMPAIGN,
				CampaignDefaultDynaFormLayoutFactory.getForm());
	}

	@Override
	protected AbstractBeanFieldGroupViewFieldFactory<SimpleCampaign> initBeanFormFieldFactory() {
		return new CampaignReadFormFieldFactory(previewForm);
	}

	@Override
	public SimpleCampaign getItem() {
		return beanItem;
	}

	@Override
	public HasPreviewFormHandlers<SimpleCampaign> getPreviewFormHandlers() {
		return previewForm;
	}

	@Override
	public IRelatedListHandlers<SimpleActivity> getRelatedActivityHandlers() {
		return associateActivityList;
	}

	@Override
	public IRelatedListHandlers<SimpleAccount> getRelatedAccountHandlers() {
		return associateAccountList;
	}

	@Override
	public IRelatedListHandlers<SimpleContact> getRelatedContactHandlers() {
		return associateContactList;
	}

	@Override
	public IRelatedListHandlers<SimpleLead> getRelatedLeadHandlers() {
		return associateLeadList;
	}
}
