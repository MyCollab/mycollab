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
package com.esofthead.mycollab.module.crm.view.lead;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.SimpleActivity;
import com.esofthead.mycollab.module.crm.domain.SimpleCampaign;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.domain.criteria.ActivitySearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.CrmCommonI18nEnum;
import com.esofthead.mycollab.module.crm.i18n.LeadI18nEnum;
import com.esofthead.mycollab.module.crm.ui.CrmAssetsManager;
import com.esofthead.mycollab.module.crm.ui.components.*;
import com.esofthead.mycollab.module.crm.view.activity.ActivityRelatedItemListComp;
import com.esofthead.mycollab.schedule.email.crm.LeadRelayEmailNotificationAction;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.*;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@ViewComponent
public class LeadReadViewImpl extends AbstractPreviewItemComp<SimpleLead>
		implements LeadReadView {
	private static final long serialVersionUID = 1L;

	private LeadCampaignListComp associateCampaignList;
	private ActivityRelatedItemListComp associateActivityList;
	private CrmCommentDisplay commentList;
	private LeadHistoryLogList historyLogList;

	private PeopleInfoComp peopleInfoComp;
	private DateInfoComp dateInfoComp;
	private CrmFollowersComp<SimpleLead> compFollowers;

	public LeadReadViewImpl() {
		super(CrmAssetsManager.getAsset(CrmTypeConstants.LEAD));
	}

	@Override
	protected AdvancedPreviewBeanForm<SimpleLead> initPreviewForm() {
		return new AdvancedPreviewBeanForm<>();
	}

	@Override
	protected ComponentContainer createButtonControls() {
		CrmPreviewFormControlsGenerator<SimpleLead> controlsButton = new CrmPreviewFormControlsGenerator<>(
				previewForm);

		Button convertButton = new Button(
				AppContext.getMessage(LeadI18nEnum.BUTTON_CONVERT_LEAD),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						previewForm.fireExtraAction("convert", beanItem);
					}
				});
		convertButton.setStyleName(UIConstants.THEME_GREEN_LINK);
		convertButton.setIcon(FontAwesome.FLASK);
		controlsButton.insertToControlBlock(convertButton);

		return controlsButton
				.createButtonControls(RolePermissionCollections.CRM_LEAD);
	}

	@Override
	protected ComponentContainer createBottomPanel() {
		TabSheetLazyLoadComponent tabTaskDetail = new TabSheetLazyLoadComponent();
		tabTaskDetail.addTab(commentList, AppContext.getMessage(GenericI18Enum.TAB_COMMENT, 0), FontAwesome.COMMENTS);
		tabTaskDetail.addTab(historyLogList, AppContext.getMessage(GenericI18Enum.TAB_HISTORY), FontAwesome.HISTORY);
		return tabTaskDetail;
	}

	@Override
	protected void onPreviewItem() {
		commentList.loadComments("" + beanItem.getId());
		historyLogList.loadHistory(beanItem.getId());
		displayActivities();
		displayCampaigns();

		dateInfoComp.displayEntryDateTime(beanItem);
		peopleInfoComp.displayEntryPeople(beanItem);
		compFollowers.displayFollowers(beanItem);

		previewItemContainer.selectTab(CrmTypeConstants.DETAIL);
	}

	@Override
	protected String initFormTitle() {
		return beanItem.getLeadName();
	}

	@Override
	protected void initRelatedComponents() {
		commentList = new CrmCommentDisplay(CrmTypeConstants.LEAD, LeadRelayEmailNotificationAction.class);
		historyLogList = new LeadHistoryLogList();

		associateCampaignList = new LeadCampaignListComp();
		associateActivityList = new ActivityRelatedItemListComp(true);

		CssLayout navigatorWrapper = previewItemContainer.getNavigatorWrapper();
		MVerticalLayout basicInfo = new MVerticalLayout().withWidth("100%").withStyleName("basic-info");

		dateInfoComp = new DateInfoComp();
		basicInfo.addComponent(dateInfoComp);

		peopleInfoComp = new PeopleInfoComp();
		basicInfo.addComponent(peopleInfoComp);

		compFollowers = new CrmFollowersComp<>(
				CrmTypeConstants.LEAD, RolePermissionCollections.CRM_LEAD);
		basicInfo.addComponent(compFollowers);

		navigatorWrapper.addComponentAsFirst(basicInfo);

		previewItemContainer.addTab(previewContent, CrmTypeConstants.DETAIL,
				AppContext.getMessage(CrmCommonI18nEnum.TAB_ABOUT));
		previewItemContainer.addTab(associateCampaignList, CrmTypeConstants.CAMPAIGN,
				AppContext.getMessage(CrmCommonI18nEnum.TAB_CAMPAIGN));
		previewItemContainer.addTab(associateActivityList, CrmTypeConstants.ACTIVITY,
				AppContext.getMessage(CrmCommonI18nEnum.TAB_ACTIVITY));
	}

	@Override
	protected IFormLayoutFactory initFormLayoutFactory() {
		return new DynaFormLayout(CrmTypeConstants.LEAD,
				LeadDefaultDynaFormLayoutFactory.getForm());
	}

	@Override
	protected AbstractBeanFieldGroupViewFieldFactory<SimpleLead> initBeanFormFieldFactory() {
		return new LeadReadFormFieldFactory(previewForm);
	}

	protected void displayCampaigns() {
		associateCampaignList.displayCampaigns(beanItem);
	}

	protected void displayActivities() {
		ActivitySearchCriteria criteria = new ActivitySearchCriteria();
		criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
		criteria.setType(new StringSearchField(CrmTypeConstants.LEAD));
		criteria.setTypeid(new NumberSearchField(beanItem.getId()));
		associateActivityList.setSearchCriteria(criteria);
	}

	@Override
	public SimpleLead getItem() {
		return beanItem;
	}

	@Override
	public HasPreviewFormHandlers<SimpleLead> getPreviewFormHandlers() {
		return previewForm;
	}

	@Override
	public IRelatedListHandlers<SimpleActivity> getRelatedActivityHandlers() {
		return associateActivityList;
	}

	@Override
	public IRelatedListHandlers<SimpleCampaign> getRelatedCampaignHandlers() {
		return associateCampaignList;
	}
}
