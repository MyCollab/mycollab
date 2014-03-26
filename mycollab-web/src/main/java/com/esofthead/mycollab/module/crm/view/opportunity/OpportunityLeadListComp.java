package com.esofthead.mycollab.module.crm.view.opportunity;

import java.util.Set;

import org.vaadin.dialogs.ConfirmDialog;

import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.utils.LocalizationHelper;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.Opportunity;
import com.esofthead.mycollab.module.crm.domain.OpportunityLead;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.esofthead.mycollab.module.crm.service.LeadService;
import com.esofthead.mycollab.module.crm.service.OpportunityService;
import com.esofthead.mycollab.module.crm.ui.components.RelatedListComp2;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.ConfirmDialogExt;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.SplitButton;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.event.MouseEvents;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class OpportunityLeadListComp extends
RelatedListComp2<LeadService, LeadSearchCriteria, SimpleLead> {
	private static final long serialVersionUID = -2052696198773718949L;

	private Opportunity opportunity;

	public OpportunityLeadListComp() {
		super(ApplicationContextUtil.getSpringBean(LeadService.class), 20);
		this.setBlockDisplayHandler(new OpportunityLeadBlockDisplay());
	}

	@Override
	protected Component generateTopControls() {
		VerticalLayout controlsBtnWrap = new VerticalLayout();
		controlsBtnWrap.setWidth("100%");
		final SplitButton controlsBtn = new SplitButton();
		controlsBtn.setSizeUndefined();
		controlsBtn.setEnabled(AppContext
				.canWrite(RolePermissionCollections.CRM_LEAD));
		controlsBtn.addStyleName(UIConstants.THEME_GREEN_LINK);
		controlsBtn.setCaption("New Lead");
		controlsBtn.setIcon(MyCollabResource
				.newResource("icons/16/addRecord.png"));
		controlsBtn
		.addClickListener(new SplitButton.SplitButtonClickListener() {
			@Override
			public void splitButtonClick(
					final SplitButton.SplitButtonClickEvent event) {
				fireNewRelatedItem("");
			}
		});
		final Button selectBtn = new Button("Select from existing leads",
				new Button.ClickListener() {
			@Override
			public void buttonClick(final ClickEvent event) {
				final OpportunityLeadSelectionWindow leadsWindow = new OpportunityLeadSelectionWindow(
						OpportunityLeadListComp.this);
				final LeadSearchCriteria criteria = new LeadSearchCriteria();
				criteria.setSaccountid(new NumberSearchField(AppContext
						.getAccountId()));
				UI.getCurrent().addWindow(leadsWindow);
				leadsWindow.setSearchCriteria(criteria);
				controlsBtn.setPopupVisible(false);
			}
		});
		selectBtn.setIcon(MyCollabResource.newResource("icons/16/select.png"));
		selectBtn.setStyleName("link");
		VerticalLayout buttonControlLayout = new VerticalLayout();
		buttonControlLayout.addComponent(selectBtn);
		controlsBtn.setContent(buttonControlLayout);

		controlsBtnWrap.addComponent(controlsBtn);
		controlsBtnWrap.setComponentAlignment(controlsBtn, Alignment.MIDDLE_RIGHT);
		return controlsBtnWrap;
	}

	public void displayLeads(final Opportunity opportunity) {
		this.opportunity = opportunity;
		loadLeads();
	}

	private void loadLeads() {
		final LeadSearchCriteria criteria = new LeadSearchCriteria();
		criteria.setSaccountid(new NumberSearchField(SearchField.AND,
				AppContext.getAccountId()));
		criteria.setOpportunityId(new NumberSearchField(SearchField.AND, opportunity
				.getId()));
		setSearchCriteria(criteria);
	}

	@Override
	public void refresh() {
		loadLeads();
	}

	@Override
	public void setSelectedItems(final Set selectedItems) {
		fireSelectedRelatedItems(selectedItems);
	}

	public class OpportunityLeadBlockDisplay implements BlockDisplayHandler<SimpleLead> {

		@Override
		public Component generateBlock(final SimpleLead lead, int blockIndex) {
			CssLayout beanBlock = new CssLayout();
			beanBlock.addStyleName("bean-block");
			beanBlock.setWidth("350px");

			VerticalLayout blockContent = new VerticalLayout();
			HorizontalLayout blockTop = new HorizontalLayout();
			blockTop.setSpacing(true);
			CssLayout iconWrap = new CssLayout();
			iconWrap.setStyleName("icon-wrap");
			Image leadAvatar = new Image(null, MyCollabResource.newResource("icons/48/crm/lead.png"));
			iconWrap.addComponent(leadAvatar);
			blockTop.addComponent(iconWrap);

			VerticalLayout leadInfo = new VerticalLayout();
			leadInfo.setSpacing(true);

			Image btnDelete = new Image(null, MyCollabResource
					.newResource("icons/12/project/icon_x.png"));
			btnDelete.addClickListener(new MouseEvents.ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void click(MouseEvents.ClickEvent event) {
					ConfirmDialogExt.show(
							UI.getCurrent(),
							LocalizationHelper.getMessage(
									GenericI18Enum.DELETE_DIALOG_TITLE,
									SiteConfiguration.getSiteName()),
									LocalizationHelper
									.getMessage(GenericI18Enum.CONFIRM_DELETE_RECORD_DIALOG_MESSAGE),
									LocalizationHelper
									.getMessage(GenericI18Enum.BUTTON_YES_LABEL),
									LocalizationHelper
									.getMessage(GenericI18Enum.BUTTON_NO_LABEL),
									new ConfirmDialog.Listener() {
								private static final long serialVersionUID = 1L;

								@Override
								public void onClose(ConfirmDialog dialog) {
									if (dialog.isConfirmed()) {
										final OpportunityService accountService = ApplicationContextUtil
												.getSpringBean(OpportunityService.class);
										final OpportunityLead associateLead = new OpportunityLead();
										associateLead
										.setOpportunityid(opportunity
												.getId());
										associateLead
										.setLeadid(lead
												.getId());
										accountService
										.removeOpportunityLeadRelationship(
												associateLead,
												AppContext
												.getAccountId());
										OpportunityLeadListComp.this.refresh();
									}
								}
							});
				}
			});
			btnDelete.addStyleName("icon-btn");

			blockContent.addComponent(btnDelete);
			blockContent.setComponentAlignment(btnDelete, Alignment.TOP_RIGHT);

			Label leadName = new Label("Name: <a href='" + SiteConfiguration.getSiteUrl(AppContext.getSession().getSubdomain()) 
					+ CrmLinkGenerator.generateCrmItemLink(CrmTypeConstants.LEAD, lead.getId()) 
					+ "'>" + lead.getLeadName() + "</a>", ContentMode.HTML);

			leadInfo.addComponent(leadName);

			Label leadStatus = new Label("Status: " + (lead.getStatus() != null ? lead.getStatus() : ""));
			leadInfo.addComponent(leadStatus);

			Label leadEmail = new Label("Email: " 
					+ (lead.getEmail() != null ? 
							"<a href='mailto:" + lead.getEmail() + "'>" + lead.getEmail() + "</a>" 
							: "")
							, ContentMode.HTML);
			leadInfo.addComponent(leadEmail);

			Label leadOfficePhone = new Label("Office Phone: " + (lead.getOfficephone() != null ? lead.getOfficephone() : ""));
			leadInfo.addComponent(leadOfficePhone);

			blockTop.addComponent(leadInfo);
			blockTop.setExpandRatio(leadInfo, 1.0f);
			blockTop.setWidth("100%");
			blockContent.addComponent(blockTop);

			blockContent.setWidth("100%");

			beanBlock.addComponent(blockContent);
			return beanBlock;
		}

	}
}
