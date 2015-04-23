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

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.SimpleCall;
import com.esofthead.mycollab.module.crm.i18n.CrmCommonI18nEnum;
import com.esofthead.mycollab.module.crm.ui.CrmAssetsManager;
import com.esofthead.mycollab.module.crm.ui.components.*;
import com.esofthead.mycollab.schedule.email.crm.CallRelayEmailNotificationAction;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.TabSheetLazyLoadComponent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

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

	private CrmCommentDisplay commentList;
	private CallHistoryLogList historyLogList;
	private DateInfoComp dateInfoComp;
	private CrmFollowersComp<SimpleCall> followersComp;

	public CallReadViewImpl() {
		super(CrmAssetsManager.getAsset(CrmTypeConstants.CALL));
	}

	@Override
	protected AdvancedPreviewBeanForm<SimpleCall> initPreviewForm() {
		return new AdvancedPreviewBeanForm<>();
	}

	@Override
	protected ComponentContainer createButtonControls() {
		return new CrmPreviewFormControlsGenerator<>(previewForm)
				.createButtonControls(RolePermissionCollections.CRM_CALL);
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
		dateInfoComp.displayEntryDateTime(beanItem);
		followersComp.displayFollowers(beanItem);
	}

	@Override
	protected String initFormTitle() {
		return beanItem.getSubject();
	}

	@Override
	protected void initRelatedComponents() {
commentList = new CrmCommentDisplay(CrmTypeConstants.CALL, CallRelayEmailNotificationAction.class);
		historyLogList = new CallHistoryLogList();

		MVerticalLayout basicInfo = new MVerticalLayout().withWidth("100%").withStyleName("basic-info");
		CssLayout navigatorWrapper = previewItemContainer.getNavigatorWrapper();

		dateInfoComp = new DateInfoComp();
		basicInfo.addComponent(dateInfoComp);

		followersComp = new CrmFollowersComp<>(CrmTypeConstants.CALL,
				RolePermissionCollections.CRM_CALL);
		basicInfo.addComponent(followersComp);

		navigatorWrapper.addComponentAsFirst(basicInfo);

		previewItemContainer.addTab(previewContent, CrmTypeConstants.DETAIL,
				AppContext.getMessage(CrmCommonI18nEnum.TAB_ABOUT));
		previewItemContainer.selectTab(CrmTypeConstants.DETAIL);
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

	@Override
	public SimpleCall getItem() {
		return beanItem;
	}

	@Override
	public HasPreviewFormHandlers<SimpleCall> getPreviewFormHandlers() {
		return previewForm;
	}
}
