package com.esofthead.mycollab.mobile.module.crm.view.activity;

import com.esofthead.mycollab.mobile.module.crm.view.account.AccountSelectionView;
import com.esofthead.mycollab.mobile.module.crm.view.campaign.CampaignSelectionView;
import com.esofthead.mycollab.mobile.module.crm.view.cases.CaseSelectionView;
import com.esofthead.mycollab.mobile.module.crm.view.contact.ContactSelectionView;
import com.esofthead.mycollab.mobile.module.crm.view.lead.LeadSelectionView;
import com.esofthead.mycollab.mobile.module.crm.view.opportunity.OpportunitySelectionView;
import com.esofthead.mycollab.mobile.ui.AbstractMobileTabPageView;
import com.esofthead.mycollab.mobile.ui.IconConstants;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.i18n.CrmCommonI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.FieldSelection;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.ui.Component;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.3.1
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class RelatedItemSelectionView extends AbstractMobileTabPageView {
	private static final long serialVersionUID = 2360108571912662272L;

	private AccountSelectionView accountList;
	private ContactSelectionView contactList;
	private CampaignSelectionView campaignList;
	private LeadSelectionView leadList;
	private OpportunitySelectionView opportunityList;
	private CaseSelectionView caseList;

	private FieldSelection selectionField;

	private Component previousView;

	public RelatedItemSelectionView(FieldSelection selectionField) {
		this.selectionField = selectionField;
		buildComponents();
	}

	@Override
	public void attach() {
		super.attach();
		Component parent = this.getParent();
		while (parent != null && !(parent instanceof NavigationManager)) {
			parent = parent.getParent();
		}
		this.previousView = ((NavigationManager) parent).getCurrentComponent();
	}

	public void selectTab(String tabName) {
		if (tabName == null) {
			this.setSelectedTab(accountList);
			return;
		}
		switch (tabName) {
		case CrmTypeConstants.CAMPAIGN:
			this.setSelectedTab(campaignList);
			break;
		case CrmTypeConstants.CASE:
			this.setSelectedTab(caseList);
			break;
		case CrmTypeConstants.CONTACT:
			this.setSelectedTab(contactList);
			break;
		case CrmTypeConstants.LEAD:
			this.setSelectedTab(leadList);
			break;
		case CrmTypeConstants.OPPORTUNITY:
			this.setSelectedTab(opportunityList);
			break;
		default:
			this.setSelectedTab(accountList);
			break;
		}
	}

	private void buildComponents() {
		buildTabs();

		this.addTab(
				accountList,
				"<span class=\"nav-btn-icon\" aria-hidden=\"true\" data-icon=\""
						+ IconConstants.CRM_ACCOUNT
						+ "\"></span><div class=\"screen-reader-text\">"
						+ AppContext
								.getMessage(CrmCommonI18nEnum.TOOLBAR_ACCOUNTS_HEADER)
						+ "</div>");
		this.addTab(
				contactList,
				"<span class=\"nav-btn-icon\" aria-hidden=\"true\" data-icon=\""
						+ IconConstants.CRM_CONTACT
						+ "\"></span><div class=\"screen-reader-text\">"
						+ AppContext
								.getMessage(CrmCommonI18nEnum.TOOLBAR_CONTACTS_HEADER)
						+ "</div>");

		this.addTab(
				campaignList,
				"<span class=\"nav-btn-icon\" aria-hidden=\"true\" data-icon=\""
						+ IconConstants.CRM_CAMPAIGN
						+ "\"></span><div class=\"screen-reader-text\">"
						+ AppContext
								.getMessage(CrmCommonI18nEnum.TOOLBAR_CAMPAIGNS_HEADER)
						+ "</div>");
		this.addTab(
				leadList,
				"<span class=\"nav-btn-icon\" aria-hidden=\"true\" data-icon=\""
						+ IconConstants.CRM_LEAD
						+ "\"></span><div class=\"screen-reader-text\">"
						+ AppContext
								.getMessage(CrmCommonI18nEnum.TOOLBAR_LEADS_HEADER)
						+ "</div>");
		this.addTab(
				opportunityList,
				"<span class=\"nav-btn-icon\" aria-hidden=\"true\" data-icon=\""
						+ IconConstants.CRM_OPPORTUNITY
						+ "\"></span><div class=\"screen-reader-text\">"
						+ AppContext
								.getMessage(CrmCommonI18nEnum.TOOLBAR_OPPORTUNTIES_HEADER)
						+ "</div>");
		this.addTab(
				caseList,
				"<span class=\"nav-btn-icon\" aria-hidden=\"true\" data-icon=\""
						+ IconConstants.CRM_CASE
						+ "\"></span><div class=\"screen-reader-text\">"
						+ AppContext
								.getMessage(CrmCommonI18nEnum.TOOLBAR_CASES_HEADER)
						+ "</div>");

		this.addListener(new SelectedTabChangeListener() {
			private static final long serialVersionUID = -2091640999903863902L;

			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				Component currentComponent = getSelelectedTab().getComponent();
				if (currentComponent == accountList) {
					accountList.load();
					accountList.setPreviousComponent(previousView);
				} else if (currentComponent == contactList) {
					contactList.load();
					contactList.setPreviousComponent(previousView);
				} else if (currentComponent == campaignList) {
					campaignList.load();
					campaignList.setPreviousComponent(previousView);
				} else if (currentComponent == leadList) {
					leadList.load();
					leadList.setPreviousComponent(previousView);
				} else if (currentComponent == opportunityList) {
					opportunityList.load();
					opportunityList.setPreviousComponent(previousView);
				} else if (currentComponent == caseList) {
					caseList.load();
					caseList.setPreviousComponent(previousView);
				}
			}
		});

	}

	private void buildTabs() {
		accountList = new AccountSelectionView();
		accountList.setSelectionField(selectionField);

		contactList = new ContactSelectionView();
		contactList.setSelectionField(selectionField);

		campaignList = new CampaignSelectionView();
		campaignList.setSelectionField(selectionField);

		caseList = new CaseSelectionView();
		caseList.setSelectionField(selectionField);

		leadList = new LeadSelectionView();
		leadList.setSelectionField(selectionField);

		opportunityList = new OpportunitySelectionView();
		opportunityList.setSelectionField(selectionField);
	}
}
