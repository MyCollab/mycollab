/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.view.account;

import com.hp.gagawa.java.elements.A;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.module.crm.CrmDataTypeFactory;
import com.mycollab.module.crm.CrmLinkGenerator;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.Account;
import com.mycollab.module.crm.domain.SimpleOpportunity;
import com.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.mycollab.module.crm.i18n.OpportunityI18nEnum;
import com.mycollab.module.crm.service.OpportunityService;
import com.mycollab.module.crm.ui.CrmAssetsManager;
import com.mycollab.module.crm.ui.components.RelatedListComp2;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.AbstractBeanBlockList;
import com.mycollab.vaadin.web.ui.ConfirmDialogExt;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.mycollab.module.crm.i18n.OptionI18nEnum.OpportunitySalesStage;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class AccountOpportunityListComp extends RelatedListComp2<OpportunityService, OpportunitySearchCriteria, SimpleOpportunity> {
    private static final long serialVersionUID = -2414709814283942446L;
    static final Map<String, String> colormap;

    private Account account;

    static {
        Map<String, String> tmpMap = new HashMap<>();
        for (int i = 0; i < CrmDataTypeFactory.opportunitySalesStageList.length; i++) {
            OpportunitySalesStage roleKeyName = CrmDataTypeFactory.opportunitySalesStageList[i];
            if (!tmpMap.containsKey(roleKeyName.name())) {
                tmpMap.put(roleKeyName.name(), AbstractBeanBlockList.COLOR_STYLENAME_LIST[i]);
            }
        }
        colormap = Collections.unmodifiableMap(tmpMap);
    }

    AccountOpportunityListComp() {
        super(AppContextUtil.getSpringBean(OpportunityService.class), 20);
        setMargin(true);
        this.setBlockDisplayHandler(new AccountOpportunityBlockDisplay());
    }

    @Override
    protected Component generateTopControls() {
        MHorizontalLayout controlsBtnWrap = new MHorizontalLayout().withFullWidth();

        MHorizontalLayout notesWrap = new MHorizontalLayout().withFullWidth();
        ELabel noteLbl = new ELabel(UserUIContext.getMessage(GenericI18Enum.OPT_NOTE))
                .withWidthUndefined();
        notesWrap.addComponent(noteLbl);

        MCssLayout noteBlock = new MCssLayout().withFullWidth().withStyleName("list-note-block");
        for (OpportunitySalesStage stage : CrmDataTypeFactory.opportunitySalesStageList) {
            MHorizontalLayout note = new MHorizontalLayout(new ELabel(UserUIContext.getMessage(stage)))
                    .withStyleName("note-label", colormap.get(stage.name()));
            noteBlock.addComponent(note);
        }
        notesWrap.with(noteBlock).expand(noteBlock);
        controlsBtnWrap.with(notesWrap).expand(notesWrap);

        if (UserUIContext.canWrite(RolePermissionCollections.CRM_OPPORTUNITY)) {
            MButton createBtn = new MButton(UserUIContext.getMessage(OpportunityI18nEnum.NEW), clickEvent -> fireNewRelatedItem(""))
                    .withIcon(FontAwesome.PLUS).withStyleName(WebThemes.BUTTON_ACTION);
            controlsBtnWrap.with(createBtn).withAlign(createBtn, Alignment.TOP_RIGHT);
        }

        return controlsBtnWrap;
    }

    void displayOpportunities(final Account account) {
        this.account = account;
        loadOpportunities();
    }

    private void loadOpportunities() {
        final OpportunitySearchCriteria criteria = new OpportunitySearchCriteria();
        criteria.setSaccountid(new NumberSearchField(AppUI.getAccountId()));
        criteria.setAccountId(new NumberSearchField(account.getId()));
        setSearchCriteria(criteria);
    }

    @Override
    public void refresh() {
        loadOpportunities();
    }

    private class AccountOpportunityBlockDisplay implements BlockDisplayHandler<SimpleOpportunity> {

        @Override
        public Component generateBlock(final SimpleOpportunity opportunity, int blockIndex) {
            MCssLayout beanBlock = new MCssLayout().withWidth("350px").withStyleName("bean-block");

            MHorizontalLayout blockTop = new MHorizontalLayout().withFullWidth();
            CssLayout iconWrap = new CssLayout();
            iconWrap.setStyleName("icon-wrap");
            ELabel opportunityIcon = ELabel.fontIcon(CrmAssetsManager.getAsset(CrmTypeConstants.OPPORTUNITY));
            iconWrap.addComponent(opportunityIcon);
            blockTop.addComponent(iconWrap);

            VerticalLayout opportunityInfo = new VerticalLayout();
            opportunityInfo.setSpacing(true);

            MButton btnDelete = new MButton("", clickEvent ->
                    ConfirmDialogExt.show(UI.getCurrent(),
                            UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, AppUI.getSiteName()),
                            UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                            UserUIContext.getMessage(GenericI18Enum.ACTION_YES),
                            UserUIContext.getMessage(GenericI18Enum.ACTION_NO),
                            confirmDialog -> {
                                if (confirmDialog.isConfirmed()) {
                                    OpportunityService opportunityService = AppContextUtil.getSpringBean(OpportunityService.class);
                                    opportunityService.removeWithSession(opportunity,
                                            UserUIContext.getUsername(), AppUI.getAccountId());
                                    AccountOpportunityListComp.this.refresh();
                                }
                            })
            ).withIcon(FontAwesome.TRASH_O).withStyleName(WebThemes.BUTTON_ICON_ONLY);

            VerticalLayout blockContent = new VerticalLayout();
            blockContent.addComponent(btnDelete);
            blockContent.setComponentAlignment(btnDelete, Alignment.TOP_RIGHT);

            A opportunityLink = new A(CrmLinkGenerator.generateOpportunityPreviewLink(opportunity.getId()))
                    .appendText(opportunity.getOpportunityname());
            opportunityInfo.addComponent(ELabel.h3(opportunityLink.write()));

            Label opportunityAmount = new Label(UserUIContext.getMessage(OpportunityI18nEnum.FORM_AMOUNT) + ": " +
                    (opportunity.getAmount() != null ? opportunity.getAmount() : ""));
            if (opportunity.getCurrencyid() != null && opportunity.getAmount() != null) {
                opportunityAmount.setValue(opportunityAmount.getValue() + opportunity.getCurrencyid());
            }
            opportunityInfo.addComponent(opportunityAmount);

            Label opportunitySaleStage = new Label(UserUIContext.getMessage(OpportunityI18nEnum.FORM_SALE_STAGE) + ": " +
                    UserUIContext.getMessage(OpportunitySalesStage.class, opportunity.getSalesstage()));
            opportunityInfo.addComponent(opportunitySaleStage);

            if (opportunity.getSalesstage() != null) {
                beanBlock.addStyleName(colormap.get(opportunity.getSalesstage()));
            }

            ELabel opportunityExpectedCloseDate = new ELabel(UserUIContext.getMessage(OpportunityI18nEnum.FORM_EXPECTED_CLOSE_DATE) + ": " +
                    UserUIContext.formatPrettyTime(opportunity.getExpectedcloseddate()))
                    .withDescription(UserUIContext.formatDate(opportunity.getExpectedcloseddate()));
            opportunityInfo.addComponent(opportunityExpectedCloseDate);

            blockTop.with(opportunityInfo).expand(opportunityInfo);
            blockContent.addComponent(blockTop);
            blockContent.setWidth("100%");

            beanBlock.addComponent(blockContent);
            return beanBlock;
        }
    }
}