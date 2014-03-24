package com.esofthead.mycollab.module.crm.view.account;

import java.util.Set;

import org.vaadin.dialogs.ConfirmDialog;

import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.utils.LocalizationHelper;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.Account;
import com.esofthead.mycollab.module.crm.domain.SimpleCase;
import com.esofthead.mycollab.module.crm.domain.criteria.CaseSearchCriteria;
import com.esofthead.mycollab.module.crm.service.CaseService;
import com.esofthead.mycollab.module.crm.ui.components.RelatedListComp2;
import com.esofthead.mycollab.module.user.UserLinkUtils;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.ConfirmDialogExt;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.event.MouseEvents;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class AccountCaseListComp extends RelatedListComp2<CaseService, CaseSearchCriteria, SimpleCase> {
	private static final long serialVersionUID = -8763667647686473453L;
	private Account account;

	public AccountCaseListComp() {
		super(ApplicationContextUtil.getSpringBean(CaseService.class), 20);
		this.setBlockDisplayHandler(new AccountCaseBlockDisplay());
	}

	@Override
	protected Component generateTopControls() {
		VerticalLayout controlsBtnWrap = new VerticalLayout();
		controlsBtnWrap.setWidth("100%");
		final Button createBtn = new Button();
		createBtn.setSizeUndefined();
		createBtn.setEnabled(AppContext
				.canWrite(RolePermissionCollections.CRM_CASE));
		createBtn.addStyleName(UIConstants.THEME_GREEN_LINK);
		createBtn.setCaption("New Case");
		createBtn.setIcon(MyCollabResource
				.newResource("icons/16/addRecord.png"));
		createBtn
		.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(
					final Button.ClickEvent event) {
				fireNewRelatedItem("");
			}
		});

		controlsBtnWrap.addComponent(createBtn);
		controlsBtnWrap.setComponentAlignment(createBtn, Alignment.MIDDLE_RIGHT);
		return controlsBtnWrap;
	}

	public void displayCases(final Account account) {
		this.account = account;
		loadCases();
	}

	private void loadCases() {
		final CaseSearchCriteria criteria = new CaseSearchCriteria();
		criteria.setSaccountid(new NumberSearchField(SearchField.AND,
				AppContext.getAccountId()));
		criteria.setAccountId(new NumberSearchField(SearchField.AND, account
				.getId()));
		setSearchCriteria(criteria);
	}

	@Override
	public void refresh() {
		loadCases();
	}

	@Override
	public void setSelectedItems(final Set selectedItems) {
		fireSelectedRelatedItems(selectedItems);
	}

	public class AccountCaseBlockDisplay implements BlockDisplayHandler<SimpleCase> {

		@Override
		public Component generateBlock(final SimpleCase oneCase, int blockIndex) {
			CssLayout beanBlock = new CssLayout();
			beanBlock.addStyleName("bean-block");
			beanBlock.setWidth("350px");

			VerticalLayout blockContent = new VerticalLayout();
			HorizontalLayout blockTop = new HorizontalLayout();
			blockTop.setSpacing(true);
			CssLayout iconWrap = new CssLayout();
			iconWrap.setStyleName("icon-wrap");
			Image caseIcon = new Image(null, MyCollabResource.newResource("icons/48/crm/contact_icon.png"));
			iconWrap.addComponent(caseIcon);
			blockTop.addComponent(iconWrap);

			VerticalLayout caseInfo = new VerticalLayout();
			caseInfo.setSpacing(true);

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
										final CaseService contactService = ApplicationContextUtil
												.getSpringBean(CaseService.class);
										oneCase.setAccountid(null);
										contactService
										.updateWithSession(
												oneCase,
												AppContext
												.getUsername());
										AccountCaseListComp.this.refresh();
									}
								}
							});
				}
			});
			btnDelete.addStyleName("icon-btn");

			blockContent.addComponent(btnDelete);
			blockContent.setComponentAlignment(btnDelete, Alignment.TOP_RIGHT);

			Label caseSubject = new Label("Subject: <a href='" + SiteConfiguration.getSiteUrl(AppContext.getSession().getSubdomain()) 
					+ CrmLinkGenerator.generateCrmItemLink(CrmTypeConstants.CASE, oneCase.getId()) 
					+ "'>" + oneCase.getSubject() + "</a>", ContentMode.HTML);

			caseInfo.addComponent(caseSubject);

			Label casePriority = new Label("Priority: " + (oneCase.getPriority() != null ? oneCase.getPriority() : ""));
			caseInfo.addComponent(casePriority);

			Label caseStatus = new Label("Status: " + (oneCase.getStatus() != null ? oneCase.getStatus() : ""));
			caseInfo.addComponent(caseStatus);

			Label caseAssignUser = new Label("Assigned User: " 
					+ (oneCase.getAssignuser() != null ? 
							"<a href='" + UserLinkUtils.generatePreviewFullUserLink(SiteConfiguration.getSiteUrl(AppContext.getSession().getSubdomain()), oneCase.getAssignuser()) 
							+ "'>" + oneCase.getAssignUserFullName() + "</a>" 
							: ""), ContentMode.HTML);
			caseInfo.addComponent(caseAssignUser);

			Label caseCreatedTime = new Label("Created Time: " + AppContext.formatDate(oneCase.getCreatedtime()));
			caseInfo.addComponent(caseCreatedTime);

			blockTop.addComponent(caseInfo);
			blockTop.setExpandRatio(caseInfo, 1.0f);
			blockTop.setWidth("100%");
			blockContent.addComponent(blockTop);

			blockContent.setWidth("100%");

			beanBlock.addComponent(blockContent);
			return beanBlock;
		}

	}

}
