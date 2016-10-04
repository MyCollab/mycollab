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
package com.mycollab.module.project.view.milestone;

import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Span;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.IgnoreException;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectLinkGenerator;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.domain.Risk;
import com.mycollab.module.project.domain.Task;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.module.project.service.ProjectTaskService;
import com.mycollab.module.project.service.RiskService;
import com.mycollab.module.tracker.domain.BugWithBLOBs;
import com.mycollab.module.tracker.service.BugService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.TooltipHelper;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.web.ui.AbstractToggleSummaryField;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import static com.mycollab.vaadin.TooltipHelper.TOOLTIP_ID;

/**
 * @author MyCollab Ltd
 * @since 5.2.3
 */
public class ToggleTicketSummaryField extends AbstractToggleSummaryField {
    private ProjectTicket ticket;
    private boolean isRead = true;

    public ToggleTicketSummaryField(final ProjectTicket ticket) {
        this.ticket = ticket;
        this.setWidth("100%");
        titleLinkLbl = ELabel.html(buildTicketLink()).withStyleName(ValoTheme.LABEL_NO_MARGIN,
                UIConstants.LABEL_WORD_WRAP).withWidthUndefined();
        this.addComponent(titleLinkLbl);
        if (CurrentProjectVariables.canWriteTicket(ticket)) {
            this.addStyleName("editable-field");
            buttonControls = new MHorizontalLayout().withStyleName("toggle").withSpacing(false);
            MButton instantEditBtn = new MButton("", clickEvent -> {
                if (isRead) {
                    removeComponent(titleLinkLbl);
                    removeComponent(buttonControls);
                    final TextField editField = new TextField();
                    editField.setValue(ticket.getName());
                    editField.setWidth("100%");
                    editField.focus();
                    addComponent(editField);
                    removeStyleName("editable-field");
                    editField.addValueChangeListener(valueChangeEvent -> updateFieldValue(editField));
                    editField.addBlurListener(blurEvent -> updateFieldValue(editField));
                    isRead = !isRead;
                }
            }).withIcon(FontAwesome.EDIT).withStyleName(ValoTheme.BUTTON_ICON_ONLY, ValoTheme.BUTTON_ICON_ALIGN_TOP);
            instantEditBtn.setDescription(UserUIContext.getMessage(GenericI18Enum.ACTION_CLICK_TO_EDIT));
            buttonControls.with(instantEditBtn);

            this.addComponent(buttonControls);
        }
    }

    private void updateFieldValue(TextField editField) {
        removeComponent(editField);
        addComponent(titleLinkLbl);
        addComponent(buttonControls);
        addStyleName("editable-field");
        String newValue = editField.getValue();
        if (StringUtils.isNotBlank(newValue) && !newValue.equals(ticket.getName())) {
            ticket.setName(newValue);
            titleLinkLbl.setValue(buildTicketLink());
            if (ticket.isBug()) {
                BugWithBLOBs bug = new BugWithBLOBs();
                bug.setId(ticket.getTypeId());
                bug.setName(ticket.getName());
                bug.setSaccountid(MyCollabUI.getAccountId());
                BugService bugService = AppContextUtil.getSpringBean(BugService.class);
                bugService.updateSelectiveWithSession(bug, UserUIContext.getUsername());
            } else if (ticket.isTask()) {
                Task task = new Task();
                task.setId(ticket.getTypeId());
                task.setName(ticket.getName());
                task.setSaccountid(MyCollabUI.getAccountId());
                ProjectTaskService taskService = AppContextUtil.getSpringBean(ProjectTaskService.class);
                taskService.updateSelectiveWithSession(task, UserUIContext.getUsername());
            } else if (ticket.isRisk()) {
                Risk risk = new Risk();
                risk.setId(ticket.getTypeId());
                risk.setName(ticket.getName());
                risk.setSaccountid(MyCollabUI.getAccountId());
                RiskService riskService = AppContextUtil.getSpringBean(RiskService.class);
                riskService.updateSelectiveWithSession(risk, UserUIContext.getUsername());
            }
        }

        isRead = !isRead;
    }

    private String buildTicketLink() {
        Div issueDiv = new Div();

        A ticketLink = new A().setId("tag" + TOOLTIP_ID);
        if (ticket.isBug() || ticket.isTask()) {
            ticketLink.setHref(ProjectLinkGenerator.generateProjectItemLink(ticket.getProjectShortName(),
                    ticket.getProjectId(), ticket.getType(), ticket.getExtraTypeId() + ""));
        } else if (ticket.isRisk()) {
            ticketLink.setHref(ProjectLinkGenerator.generateProjectItemLink(ticket.getProjectShortName(),
                    ticket.getProjectId(), ticket.getType(), ticket.getTypeId() + ""));
        } else {
            throw new IgnoreException("Not support type: " + ticket.getType());
        }

        ticketLink.setAttribute("onmouseover", TooltipHelper.projectHoverJsFunction(ticket.getType(), ticket.getTypeId() + ""));
        ticketLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction());
        ticketLink.appendText(ticket.getName());

        issueDiv.appendChild(ticketLink);

        if (ticket.isClosed()) {
            ticketLink.setCSSClass("completed");
        } else if (ticket.isOverdue()) {
            ticketLink.setCSSClass("overdue");
            issueDiv.appendChild(new Span().setCSSClass(UIConstants.META_INFO).appendText(" - " + UserUIContext
                    .getMessage(ProjectCommonI18nEnum.OPT_DUE_IN, UserUIContext.formatDuration(ticket.getDueDate()))));
        }
        return issueDiv.write();
    }
}
