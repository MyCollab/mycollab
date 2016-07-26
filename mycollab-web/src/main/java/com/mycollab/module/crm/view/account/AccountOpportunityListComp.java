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
package com.mycollab.module.crm.view.account;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchField;
import com.mycollab.module.crm.CrmDataTypeFactory;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.data.CrmLinkBuilder;
import com.mycollab.module.crm.domain.Account;
import com.mycollab.module.crm.domain.SimpleOpportunity;
import com.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.mycollab.module.crm.i18n.OpportunityI18nEnum;
import com.mycollab.module.crm.service.OpportunityService;
import com.mycollab.module.crm.ui.CrmAssetsManager;
import com.mycollab.module.crm.ui.components.RelatedListComp2;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.AbstractBeanBlockList;
import com.mycollab.vaadin.web.ui.ConfirmDialogExt;
import com.mycollab.vaadin.web.ui.WebUIConstants;
import com.google.common.base.MoreObjects;
import com.hp.gagawa.java.elements.A;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
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
        super(AppContextUtil.getSpringBean(OpportunityService.class), 20);
        this.setBlockDisplayHandler(new AccountOpportunityBlockDisplay());
    }

    @Override
    protected Component generateTopControls() {
        MHorizontalLayout controlsBtnWrap = new MHorizontalLayout().withFullWidth();

        MHorizontalLayout notesWrap = new MHorizontalLayout().withFullWidth();
        ELabel noteLbl = new ELabel("Note: ").withWidthUndefined().withStyleName("list-note-lbl");
        notesWrap.addComponent(noteLbl);

        CssLayout noteBlock = new CssLayout();
        noteBlock.setWidth("100%");
        noteBlock.setStyleName("list-note-block");
        for (int i = 0; i < CrmDataTypeFactory.getOpportunitySalesStageList().length; i++) {
            ELabel note = new ELabel(CrmDataTypeFactory.getOpportunitySalesStageList()[i])
                    .withStyleName("note-label", colormap.get(CrmDataTypeFactory.getOpportunitySalesStageList()[i]))
                    .withWidthUndefined();
            noteBlock.addComponent(note);
        }
        notesWrap.with(noteBlock).expand(noteBlock);
        controlsBtnWrap.addComponent(notesWrap);

        if (AppContext.canWrite(RolePermissionCollections.CRM_OPPORTUNITY)) {
            MButton createBtn = new MButton(AppContext.getMessage(OpportunityI18nEnum.NEW), clickEvent -> fireNewRelatedItem(""))
                    .withIcon(FontAwesome.PLUS).withStyleName(WebUIConstants.BUTTON_ACTION);

            controlsBtnWrap.with(createBtn).withAlign(createBtn, Alignment.TOP_RIGHT);
        }

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

            MHorizontalLayout blockTop = new MHorizontalLayout().withFullWidth();
            CssLayout iconWrap = new CssLayout();
            iconWrap.setStyleName("icon-wrap");
            ELabel opportunityIcon = ELabel.fontIcon(CrmAssetsManager.getAsset(CrmTypeConstants.OPPORTUNITY));
            iconWrap.addComponent(opportunityIcon);
            blockTop.addComponent(iconWrap);

            VerticalLayout opportunityInfo = new VerticalLayout();
            opportunityInfo.setSpacing(true);

            MButton btnDelete = new MButton("", clickEvent -> {
                ConfirmDialogExt.show(UI.getCurrent(),
                        AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, AppContext.getSiteName()),
                        AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                        AppContext.getMessage(GenericI18Enum.BUTTON_YES),
                        AppContext.getMessage(GenericI18Enum.BUTTON_NO),
                        confirmDialog -> {
                            if (confirmDialog.isConfirmed()) {
                                OpportunityService opportunityService = AppContextUtil.getSpringBean(OpportunityService.class);
                                opportunityService.removeWithSession(opportunity,
                                        AppContext.getUsername(), AppContext.getAccountId());
                                AccountOpportunityListComp.this.refresh();
                            }
                        });
            }).withIcon(FontAwesome.TRASH_O).withStyleName(WebUIConstants.BUTTON_ICON_ONLY);

            VerticalLayout blockContent = new VerticalLayout();
            blockContent.addComponent(btnDelete);
            blockContent.setComponentAlignment(btnDelete, Alignment.TOP_RIGHT);

            A opportunityLink = new A(CrmLinkBuilder.generateOpportunityPreviewLinkFull(opportunity.getId()))
                    .appendText(opportunity.getOpportunityname());
            opportunityInfo.addComponent(ELabel.h3(opportunityLink.write()));

            Label opportunityAmount = new Label("Amount: " + (opportunity.getAmount() != null ? opportunity.getAmount() : ""));
            if (opportunity.getCurrencyid() != null && opportunity.getAmount() != null) {
                opportunityAmount.setValue(opportunityAmount.getValue() + opportunity.getCurrencyid());
            }
            opportunityInfo.addComponent(opportunityAmount);

            Label opportunitySaleStage = new Label("Sale Stage: " + MoreObjects.firstNonNull(opportunity.getSalesstage(), ""));
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
