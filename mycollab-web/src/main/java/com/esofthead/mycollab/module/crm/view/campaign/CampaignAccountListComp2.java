package com.esofthead.mycollab.module.crm.view.campaign;

import java.util.Set;

import org.vaadin.dialogs.ConfirmDialog;

import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.utils.LocalizationHelper;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.CampaignAccount;
import com.esofthead.mycollab.module.crm.domain.CampaignWithBLOBs;
import com.esofthead.mycollab.module.crm.domain.SimpleAccount;
import com.esofthead.mycollab.module.crm.domain.criteria.AccountSearchCriteria;
import com.esofthead.mycollab.module.crm.service.AccountService;
import com.esofthead.mycollab.module.crm.service.CampaignService;
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

public class CampaignAccountListComp2 extends
RelatedListComp2<AccountService, AccountSearchCriteria, SimpleAccount> {
	private static final long serialVersionUID = 4624196496275152351L;

	private CampaignWithBLOBs campaign;

	public CampaignAccountListComp2() {
		super(ApplicationContextUtil.getSpringBean(AccountService.class), 20);
		this.setBlockDisplayHandler(new CampaignAccountBlockDisplay());
	}

	@Override
	public void setSelectedItems(Set selectedItems) {
		fireSelectedRelatedItems(selectedItems);
	}

	@Override
	public void refresh() {
		loadAccounts();
	}

	public void displayAccounts(CampaignWithBLOBs campaign) {
		this.campaign = campaign;
		loadAccounts();
	}

	private void loadAccounts() {
		AccountSearchCriteria criteria = new AccountSearchCriteria();
		criteria.setSaccountid(new NumberSearchField(SearchField.AND,
				AppContext.getAccountId()));
		criteria.setCampaignId(new NumberSearchField(SearchField.AND, campaign
				.getId()));
		this.setSearchCriteria(criteria);
	}

	@Override
	protected Component generateTopControls() {
		VerticalLayout controlsBtnWrap = new VerticalLayout();
		controlsBtnWrap.setWidth("100%");
		final SplitButton controlsBtn = new SplitButton();
		controlsBtn.setSizeUndefined();
		controlsBtn.setEnabled(AppContext
				.canWrite(RolePermissionCollections.CRM_ACCOUNT));
		controlsBtn.addStyleName(UIConstants.THEME_GREEN_LINK);
		controlsBtn.setCaption("New Account");
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
		final Button selectBtn = new Button("Select from existing contacts",
				new Button.ClickListener() {

			@Override
			public void buttonClick(final ClickEvent event) {
				final CampaignAccountSelectionWindow accountsWindow = new CampaignAccountSelectionWindow(
						CampaignAccountListComp2.this);
				final AccountSearchCriteria criteria = new AccountSearchCriteria();
				criteria.setSaccountid(new NumberSearchField(AppContext
						.getAccountId()));
				UI.getCurrent().addWindow(accountsWindow);
				accountsWindow.setSearchCriteria(criteria);
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

	protected class CampaignAccountBlockDisplay implements BlockDisplayHandler<SimpleAccount> {

		@Override
		public Component generateBlock(final SimpleAccount account, int blockIndex) {
			CssLayout beanBlock = new CssLayout();
			beanBlock.addStyleName("bean-block");
			beanBlock.setWidth("350px");

			VerticalLayout blockContent = new VerticalLayout();
			HorizontalLayout blockTop = new HorizontalLayout();
			blockTop.setSpacing(true);
			CssLayout iconWrap = new CssLayout();
			iconWrap.setStyleName("icon-wrap");
			Image accountAvatar = new Image(null, MyCollabResource.newResource("icons/48/crm/contact_icon.png"));
			iconWrap.addComponent(accountAvatar);
			blockTop.addComponent(iconWrap);

			VerticalLayout accountInfo = new VerticalLayout();
			accountInfo.setSpacing(true);

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
										CampaignService campaignService = ApplicationContextUtil
												.getSpringBean(CampaignService.class);
										CampaignAccount associateAccount = new CampaignAccount();
										associateAccount
										.setAccountid(account
												.getId());
										associateAccount
										.setCampaignid(campaign
												.getId());
										campaignService
										.removeCampaignAccountRelationship(
												associateAccount,
												AppContext
												.getAccountId());
										CampaignAccountListComp2.this
										.refresh();
									}
								}
							});
				}
			});
			btnDelete.addStyleName("icon-btn");

			blockContent.addComponent(btnDelete);
			blockContent.setComponentAlignment(btnDelete, Alignment.TOP_RIGHT);

			Label accountName = new Label("Name: <a href='" + SiteConfiguration.getSiteUrl(AppContext.getSession().getSubdomain()) 
					+ CrmLinkGenerator.generateCrmItemLink(CrmTypeConstants.ACCOUNT, account.getId()) 
					+ "'>" + account.getAccountname() + "</a>", ContentMode.HTML);

			accountInfo.addComponent(accountName);

			Label accountOfficePhone = new Label("Office Phone: " + (account.getPhoneoffice() != null ? account.getPhoneoffice() : ""));
			accountInfo.addComponent(accountOfficePhone);

			Label accountEmail = new Label("Email: " 
					+ (account.getEmail() != null ? 
							"<a href='mailto:" + account.getEmail() + "'>" + account.getEmail() + "</a>" 
							: "")
							, ContentMode.HTML);
			accountInfo.addComponent(accountEmail);

			Label accountCity = new Label("City: " + (account.getCity() != null ? account.getCity() : ""));
			accountInfo.addComponent(accountCity);

			blockTop.addComponent(accountInfo);
			blockTop.setExpandRatio(accountInfo, 1.0f);
			blockTop.setWidth("100%");
			blockContent.addComponent(blockTop);

			blockContent.setWidth("100%");

			beanBlock.addComponent(blockContent);
			return beanBlock;
		}

	}
}
