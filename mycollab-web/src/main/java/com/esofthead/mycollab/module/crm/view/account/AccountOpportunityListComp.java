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
package com.esofthead.mycollab.module.crm.view.account;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.module.crm.CrmDataTypeFactory;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.Account;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.OpportunityI18nEnum;
import com.esofthead.mycollab.module.crm.service.OpportunityService;
import com.esofthead.mycollab.module.crm.ui.CrmAssetsManager;
import com.esofthead.mycollab.module.crm.ui.components.RelatedListComp2;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.*;
import com.esofthead.mycollab.vaadin.web.ui.AbstractBeanBlockList;
import com.esofthead.mycollab.vaadin.web.ui.ConfirmDialogExt;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class AccountOpportunityListComp extends RelatedListComp2<OpportunityService, OpportunitySearchCriteria, SimpleOpportunity> {
    private static final long serialVersionUID = -2414709814283942446L;

    private Account account;

    static final Map<String, String> colormap;

    static {
        Map<String, String> tmpMap = new HashMap<>();
        for (int i = 0; i < CrmDataTypeFactory.getOpportunitySalesStageList().length; i++) {
            String roleKeyName = CrmDataTypeFactory.getOpportunitySalesStageList()[i];
            if (!tmpMap.containsKey(roleKeyName)) {
                tmpMap.put(roleKeyName, AbstractBeanBlockList.COLOR_STYLENAME_LIST[i]);
            }
        }
        colormap = Collections.unmodifiableMap(tmpMap);
    }

    public AccountOpportunityListComp() {
        super(ApplicationContextUtil.getSpringBean(OpportunityService.class),
                20);
        this.setBlockDisplayHandler(new AccountOpportunityBlockDisplay());
    }

    @Override
    protected Component generateTopControls() {
        HorizontalLayout controlsBtnWrap = new HorizontalLayout();
        controlsBtnWrap.setWidth("100%");

        MHorizontalLayout notesWrap = new MHorizontalLayout().withWidth("100%");
        Label noteLbl = new Label("Note: ");
        noteLbl.setSizeUndefined();
        noteLbl.setStyleName("list-note-lbl");
        notesWrap.addComponent(noteLbl);

        CssLayout noteBlock = new CssLayout();
        noteBlock.setWidth("100%");
        noteBlock.setStyleName("list-note-block");
        for (int i = 0; i < CrmDataTypeFactory.getOpportunitySalesStageList().length; i++) {
            Label note = new Label(CrmDataTypeFactory.getOpportunitySalesStageList()[i]);
            note.setStyleName("note-label");
            note.addStyleName(colormap.get(CrmDataTypeFactory.getOpportunitySalesStageList()[i]));
            note.setSizeUndefined();

            noteBlock.addComponent(note);
        }
        notesWrap.with(noteBlock).expand(noteBlock);

        controlsBtnWrap.addComponent(notesWrap);

        controlsBtnWrap.setWidth("100%");
        final Button createBtn = new Button(AppContext.getMessage(OpportunityI18nEnum.BUTTON_NEW_OPPORTUNITY), new Button.ClickListener() {
            private static final long serialVersionUID = -8101659779838108951L;

            @Override
            public void buttonClick(final Button.ClickEvent event) {
                fireNewRelatedItem("");
            }
        });
        createBtn.setSizeUndefined();
        createBtn.setEnabled(AppContext.canWrite(RolePermissionCollections.CRM_OPPORTUNITY));
        createBtn.addStyleName(UIConstants.BUTTON_ACTION);
        createBtn.setIcon(FontAwesome.PLUS);

        controlsBtnWrap.addComponent(createBtn);
        controlsBtnWrap.setComponentAlignment(createBtn, Alignment.MIDDLE_RIGHT);
        return controlsBtnWrap;
    }

    public void displayOpportunities(final Account account) {
        this.account = account;
        loadOpportunities();
    }

    private void loadOpportunities() {
        final OpportunitySearchCriteria criteria = new OpportunitySearchCriteria();
        criteria.setSaccountid(new NumberSearchField(SearchField.AND, AppContext.getAccountId()));
        criteria.setAccountId(new NumberSearchField(SearchField.AND, account.getId()));
        setSearchCriteria(criteria);
    }

    @Override
    public void refresh() {
        loadOpportunities();
    }

    public class AccountOpportunityBlockDisplay implements BlockDisplayHandler<SimpleOpportunity> {

        @Override
        public Component generateBlock(final SimpleOpportunity opportunity, int blockIndex) {
            CssLayout beanBlock = new CssLayout();
            beanBlock.addStyleName("bean-block");
            beanBlock.setWidth("350px");

            VerticalLayout blockContent = new VerticalLayout();
            MHorizontalLayout blockTop = new MHorizontalLayout().withWidth("100%");
            CssLayout iconWrap = new CssLayout();
            iconWrap.setStyleName("icon-wrap");
            ELabel opportunityIcon = new ELabel(CrmAssetsManager.getAsset(CrmTypeConstants.OPPORTUNITY));
            iconWrap.addComponent(opportunityIcon);
            blockTop.addComponent(iconWrap);

            VerticalLayout opportunityInfo = new VerticalLayout();
            opportunityInfo.setSpacing(true);

            MButton btnDelete = new MButton(FontAwesome.TRASH_O);
            btnDelete.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    ConfirmDialogExt.show(UI.getCurrent(),
                            AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, AppContext.getSiteName()),
                            AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                            AppContext.getMessage(GenericI18Enum.BUTTON_YES),
                            AppContext.getMessage(GenericI18Enum.BUTTON_NO),
                            new ConfirmDialog.Listener() {
                                private static final long serialVersionUID = 1L;

                                @Override
                                public void onClose(ConfirmDialog dialog) {
                                    if (dialog.isConfirmed()) {
                                        OpportunityService opportunityService = ApplicationContextUtil.getSpringBean(OpportunityService.class);
                                        opportunityService.removeWithSession(opportunity,
                                                AppContext.getUsername(), AppContext.getAccountId());
                                        AccountOpportunityListComp.this.refresh();
                                    }
                                }
                            });
                }
            });
            btnDelete.addStyleName(UIConstants.BUTTON_ICON_ONLY);

            blockContent.addComponent(btnDelete);
            blockContent.setComponentAlignment(btnDelete, Alignment.TOP_RIGHT);

            Label opportunityName = new Label("Name: <a href='"
                    + SiteConfiguration.getSiteUrl(AppContext.getUser()
                    .getSubdomain())
                    + CrmLinkGenerator.generateCrmItemLink(
                    CrmTypeConstants.OPPORTUNITY, opportunity.getId())
                    + "'>" + opportunity.getOpportunityname() + "</a>",
                    ContentMode.HTML);

            opportunityInfo.addComponent(opportunityName);

            Label opportunityAmount = new Label("Amount: " + (opportunity.getAmount() != null ? opportunity.getAmount() : ""));
            if (opportunity.getCurrency() != null && opportunity.getAmount() != null) {
                opportunityAmount.setValue(opportunityAmount.getValue() + opportunity.getCurrency().getSymbol());
            }
            opportunityInfo.addComponent(opportunityAmount);

            Label opportunitySaleStage = new Label("Sale Stage: " + (opportunity.getSalesstage() != null ? opportunity.getSalesstage() : ""));
            opportunityInfo.addComponent(opportunitySaleStage);

            if (opportunity.getSalesstage() != null) {
                beanBlock.addStyleName(colormap.get(opportunity.getSalesstage()));
            }

            ELabel opportunityExpectedCloseDate = new ELabel("Expected Close Date: " +
                    AppContext.formatPrettyTime(opportunity.getExpectedcloseddate()))
                    .withDescription(AppContext.formatDate(opportunity.getExpectedcloseddate()));
            opportunityInfo.addComponent(opportunityExpectedCloseDate);

            blockTop.with(opportunityInfo).expand(opportunityInfo);
            blockContent.addComponent(blockTop);

            blockContent.setWidth("100%");

            beanBlock.addComponent(blockContent);
            return beanBlock;
        }
    }

}
