/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.view.account;

import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Span;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.module.crm.CrmDataTypeFactory;
import com.mycollab.module.crm.CrmLinkGenerator;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.Account;
import com.mycollab.module.crm.domain.SimpleCase;
import com.mycollab.module.crm.domain.criteria.CaseSearchCriteria;
import com.mycollab.module.crm.i18n.CaseI18nEnum;
import com.mycollab.module.crm.i18n.OptionI18nEnum.CasePriority;
import com.mycollab.module.crm.service.CaseService;
import com.mycollab.module.crm.ui.CrmAssetsManager;
import com.mycollab.module.crm.ui.components.RelatedListComp2;
import com.mycollab.module.user.AccountLinkGenerator;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.AbstractBeanBlockList;
import com.mycollab.vaadin.web.ui.ConfirmDialogExt;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.mycollab.module.crm.i18n.OptionI18nEnum.CaseStatus;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class AccountCaseListComp extends RelatedListComp2<CaseService, CaseSearchCriteria, SimpleCase> {
    private static final long serialVersionUID = -8763667647686473453L;
    private Account account;

    private final static Map<String, String> colorsMap;

    static {
        Map<String, String> tmpMap = new HashMap<>();
        for (int i = 0; i < CrmDataTypeFactory.casesStatuses.length; i++) {
            String roleKeyName = CrmDataTypeFactory.casesStatuses[i].name();
            if (!tmpMap.containsKey(roleKeyName)) {
                tmpMap.put(roleKeyName, AbstractBeanBlockList.COLOR_STYLENAME_LIST[i]);
            }
        }
        colorsMap = Collections.unmodifiableMap(tmpMap);
    }

    public AccountCaseListComp() {
        super(AppContextUtil.getSpringBean(CaseService.class), 20);
        setMargin(true);
        this.setBlockDisplayHandler(new AccountCaseBlockDisplay());
    }

    @Override
    protected Component generateTopControls() {
        MHorizontalLayout controlsBtnWrap = new MHorizontalLayout().withFullWidth();

        MHorizontalLayout notesWrap = new MHorizontalLayout().withFullWidth();
        ELabel noteLbl = new ELabel(UserUIContext.getMessage(GenericI18Enum.OPT_NOTE)).withUndefinedWidth()
                .withStyleName("list-note-lbl");
        notesWrap.addComponent(noteLbl);

        MCssLayout noteBlock = new MCssLayout().withFullWidth().withStyleName("list-note-block");
        for (CaseStatus status : CrmDataTypeFactory.casesStatuses) {
            ELabel note = new ELabel(UserUIContext.getMessage(status)).withStyleName("note-label", colorsMap.get(status
                    .name())).withUndefinedWidth();
            noteBlock.addComponent(note);
        }
        notesWrap.with(noteBlock).expand(noteBlock);
        controlsBtnWrap.with(notesWrap).expand(notesWrap);

        if (UserUIContext.canWrite(RolePermissionCollections.CRM_CASE)) {
            MButton createBtn = new MButton(UserUIContext.getMessage(CaseI18nEnum.NEW), clickEvent -> fireNewRelatedItem(""))
                    .withIcon(VaadinIcons.PLUS).withStyleName(WebThemes.BUTTON_ACTION);
            controlsBtnWrap.with(createBtn).withAlign(createBtn, Alignment.TOP_RIGHT);
        }

        return controlsBtnWrap;
    }

    void displayCases(final Account account) {
        this.account = account;
        loadCases();
    }

    private void loadCases() {
        final CaseSearchCriteria criteria = new CaseSearchCriteria();
        criteria.setAccountId(new NumberSearchField(account.getId()));
        setSearchCriteria(criteria);
    }

    @Override
    public void refresh() {
        loadCases();
    }

    private class AccountCaseBlockDisplay implements BlockDisplayHandler<SimpleCase> {

        @Override
        public Component generateBlock(final SimpleCase oneCase, int blockIndex) {
            MCssLayout beanBlock = new MCssLayout().withWidth("350px").withStyleName("bean-block");

            VerticalLayout blockContent = new VerticalLayout();
            MHorizontalLayout blockTop = new MHorizontalLayout();
            CssLayout iconWrap = new CssLayout();
            iconWrap.setStyleName("icon-wrap");
            ELabel caseIcon = ELabel.fontIcon(CrmAssetsManager.getAsset(CrmTypeConstants.CASE));
            iconWrap.addComponent(caseIcon);
            blockTop.addComponent(iconWrap);

            VerticalLayout caseInfo = new VerticalLayout();
            caseInfo.setSpacing(true);

            MButton deleteBtn = new MButton("", clickEvent ->
                    ConfirmDialogExt.show(UI.getCurrent(),
                            UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, AppUI.getSiteName()),
                            UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                            UserUIContext.getMessage(GenericI18Enum.ACTION_YES),
                            UserUIContext.getMessage(GenericI18Enum.ACTION_NO),
                            confirmDialog -> {
                                if (confirmDialog.isConfirmed()) {
                                    CaseService caseService = AppContextUtil.getSpringBean(CaseService.class);
                                    caseService.removeWithSession(oneCase, UserUIContext.getUsername(), AppUI.getAccountId());
                                    AccountCaseListComp.this.refresh();
                                }
                            })
            ).withIcon(VaadinIcons.TRASH).withStyleName(WebThemes.BUTTON_ICON_ONLY);

            blockContent.addComponent(deleteBtn);
            blockContent.setComponentAlignment(deleteBtn, Alignment.TOP_RIGHT);

            A caseLink = new A(CrmLinkGenerator.generateCasePreviewLink(oneCase.getId())).appendText(oneCase.getSubject());
            ELabel caseSubject = ELabel.h3(caseLink.write());
            caseInfo.addComponent(caseSubject);

            Label casePriority = new Label(UserUIContext.getMessage(CaseI18nEnum.FORM_PRIORITY) + ": " +
                    UserUIContext.getMessage(CasePriority.class, oneCase.getPriority()));
            caseInfo.addComponent(casePriority);

            Label caseStatus = new Label(UserUIContext.getMessage(GenericI18Enum.FORM_STATUS) + ": " +
                    UserUIContext.getMessage(CaseStatus.class, oneCase.getStatus()));
            caseInfo.addComponent(caseStatus);

            if (oneCase.getStatus() != null) {
                beanBlock.addStyleName(colorsMap.get(oneCase.getStatus()));
            }

            String assigneeValue = (oneCase.getAssignuser() == null) ? new Span().appendText(UserUIContext.getMessage
                    (GenericI18Enum.OPT_UNDEFINED)).write() : new A(AccountLinkGenerator.generateUserLink(oneCase.getAssignuser()))
                    .appendText(oneCase.getAssignUserFullName()).write();
            Label caseAssignUser = ELabel.html(UserUIContext.getMessage(GenericI18Enum.FORM_ASSIGNEE) + ": " + assigneeValue);
            caseInfo.addComponent(caseAssignUser);

            ELabel caseCreatedTime = new ELabel(UserUIContext.getMessage(GenericI18Enum.FORM_CREATED_TIME) + ": "
                    + UserUIContext.formatPrettyTime(oneCase.getCreatedtime()))
                    .withDescription(UserUIContext.formatDateTime(oneCase.getCreatedtime()));
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
