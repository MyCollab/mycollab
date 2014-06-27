package com.esofthead.mycollab.mobile.module.crm.view.activity;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.mobile.module.crm.events.CrmEvent;
import com.esofthead.mycollab.mobile.module.crm.view.account.AccountSelectionView;
import com.esofthead.mycollab.mobile.module.crm.view.campaign.CampaignListDisplay;
import com.esofthead.mycollab.mobile.module.crm.view.cases.CaseListDisplay;
import com.esofthead.mycollab.mobile.module.crm.view.contact.ContactListDisplay;
import com.esofthead.mycollab.mobile.module.crm.view.lead.LeadListDisplay;
import com.esofthead.mycollab.mobile.module.crm.view.opportunity.OpportunityListDisplay;
import com.esofthead.mycollab.mobile.ui.AbstractMobileTabPageView;
import com.esofthead.mycollab.mobile.ui.IconConstants;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.SimpleCampaign;
import com.esofthead.mycollab.module.crm.domain.SimpleCase;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.crm.domain.criteria.CampaignSearchCriteria;
import com.esofthead.mycollab.module.crm.domain.criteria.CaseSearchCriteria;
import com.esofthead.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.esofthead.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.esofthead.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.CrmCommonI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.FieldSelection;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.3.1
 * 
 */
@SuppressWarnings({ "rawtypes", "serial", "unchecked" })
public class RelatedItemSelectionView extends AbstractMobileTabPageView {
	private static final long serialVersionUID = 2360108571912662272L;

	private AccountSelectionView accountList;
	private ContactListDisplay contactList;
	private CampaignListDisplay campaignList;
	private LeadListDisplay leadList;
	private OpportunityListDisplay opportunityList;
	private CaseListDisplay caseList;

	private FieldSelection selectionField;
	private NavigationButton backBtn;

	public RelatedItemSelectionView(FieldSelection selectionField) {
		this.selectionField = selectionField;
		backBtn = new NavigationButton("Back");
		backBtn.setStyleName("back");
		buildComponents();
	}

	@Override
	public void attach() {
		super.attach();
		Component parent = this.getParent();
		while (parent != null && !(parent instanceof NavigationManager)) {
			parent = parent.getParent();
		}
		this.backBtn.setTargetView(((NavigationManager) parent)
				.getCurrentComponent());
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
					accountList
							.setLeftComponent(RelatedItemSelectionView.this.backBtn);
				} else if (currentComponent == contactList) {
					ContactSearchCriteria criteria = new ContactSearchCriteria();
					criteria.setSaccountid(new NumberSearchField(
							SearchField.AND, AppContext.getAccountId()));
					contactList.setSearchCriteria(criteria);
					SimpleContact clearContact = new SimpleContact();
					contactList.getBeanContainer().addItemAt(0, clearContact);
				} else if (currentComponent == campaignList) {
					CampaignSearchCriteria criteria = new CampaignSearchCriteria();
					criteria.setSaccountid(new NumberSearchField(
							SearchField.AND, AppContext.getAccountId()));
					campaignList.setSearchCriteria(criteria);
					SimpleCampaign clearCampaign = new SimpleCampaign();
					campaignList.getBeanContainer().addItemAt(0, clearCampaign);
				} else if (currentComponent == leadList) {
					LeadSearchCriteria criteria = new LeadSearchCriteria();
					criteria.setSaccountid(new NumberSearchField(
							SearchField.AND, AppContext.getAccountId()));
					leadList.setSearchCriteria(criteria);
					SimpleLead clearLead = new SimpleLead();
					leadList.getBeanContainer().addItemAt(0, clearLead);
				} else if (currentComponent == opportunityList) {
					OpportunitySearchCriteria criteria = new OpportunitySearchCriteria();
					criteria.setSaccountid(new NumberSearchField(
							SearchField.AND, AppContext.getAccountId()));
					opportunityList.setSearchCriteria(criteria);
					SimpleOpportunity clearOpportunity = new SimpleOpportunity();
					opportunityList.getBeanContainer().addItemAt(0,
							clearOpportunity);
				} else if (currentComponent == caseList) {
					CaseSearchCriteria criteria = new CaseSearchCriteria();
					criteria.setSaccountid(new NumberSearchField(
							SearchField.AND, AppContext.getAccountId()));
					caseList.setSearchCriteria(criteria);
					SimpleCase clearCase = new SimpleCase();
					caseList.getBeanContainer().addItemAt(0, clearCase);
				}
			}
		});

	}

	private void buildTabs() {
		accountList = new AccountSelectionView();
		accountList.setSelectionField(selectionField);

		contactList = new ContactListDisplay("contactName");
		contactList.setWidth("100%");
		contactList.addGeneratedColumn("contactName",
				new Table.ColumnGenerator() {
					private static final long serialVersionUID = 1L;

					@Override
					public com.vaadin.ui.Component generateCell(
							final Table source, final Object itemId,
							final Object columnId) {
						final SimpleContact contact = contactList
								.getBeanByIndex(itemId);

						Button b = new Button(contact.getContactName(),
								new Button.ClickListener() {

									@Override
									public void buttonClick(
											final Button.ClickEvent event) {
										selectionField.fireValueChange(contact);
										EventBus.getInstance().fireEvent(
												new CrmEvent.NavigateBack(this,
														null));
									}
								});
						return b;
					}
				});

		campaignList = new CampaignListDisplay("campaignname");
		campaignList.setWidth("100%");
		campaignList.addGeneratedColumn("campaignname",
				new Table.ColumnGenerator() {
					private static final long serialVersionUID = 1L;

					@Override
					public com.vaadin.ui.Component generateCell(
							final Table source, final Object itemId,
							final Object columnId) {
						final SimpleCampaign campaign = campaignList
								.getBeanByIndex(itemId);

						Button b = new Button(campaign.getCampaignname(),
								new Button.ClickListener() {

									@Override
									public void buttonClick(
											final Button.ClickEvent event) {
										selectionField
												.fireValueChange(campaign);
										EventBus.getInstance().fireEvent(
												new CrmEvent.NavigateBack(this,
														null));
									}
								});
						return b;
					}
				});

		caseList = new CaseListDisplay("subject");
		caseList.setWidth("100%");
		caseList.addGeneratedColumn("subject", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public com.vaadin.ui.Component generateCell(final Table source,
					final Object itemId, final Object columnId) {
				final SimpleCase mycase = caseList.getBeanByIndex(itemId);

				Button b = new Button(mycase.getSubject(),
						new Button.ClickListener() {

							@Override
							public void buttonClick(
									final Button.ClickEvent event) {
								selectionField.fireValueChange(mycase);
								EventBus.getInstance().fireEvent(
										new CrmEvent.NavigateBack(this, null));
							}
						});
				return b;
			}
		});

		leadList = new LeadListDisplay("leadName");
		leadList.setWidth("100%");
		leadList.addGeneratedColumn("leadName", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public com.vaadin.ui.Component generateCell(final Table source,
					final Object itemId, final Object columnId) {
				final SimpleLead lead = leadList.getBeanByIndex(itemId);

				Button b = new Button(lead.getLeadName(),
						new Button.ClickListener() {

							@Override
							public void buttonClick(
									final Button.ClickEvent event) {
								selectionField.fireValueChange(lead);
								EventBus.getInstance().fireEvent(
										new CrmEvent.NavigateBack(this, null));
							}
						});
				return b;
			}
		});

		opportunityList = new OpportunityListDisplay("opportunityname");
		opportunityList.setWidth("100%");
		opportunityList.addGeneratedColumn("opportunityname",
				new Table.ColumnGenerator() {
					private static final long serialVersionUID = 1L;

					@Override
					public com.vaadin.ui.Component generateCell(
							final Table source, final Object itemId,
							final Object columnId) {
						final SimpleOpportunity opportunity = opportunityList
								.getBeanByIndex(itemId);

						Button b = new Button(opportunity.getOpportunityname(),
								new Button.ClickListener() {

									@Override
									public void buttonClick(
											final Button.ClickEvent event) {
										selectionField
												.fireValueChange(opportunity);
										EventBus.getInstance().fireEvent(
												new CrmEvent.NavigateBack(this,
														null));
									}
								});
						return b;
					}
				});
	}
}
