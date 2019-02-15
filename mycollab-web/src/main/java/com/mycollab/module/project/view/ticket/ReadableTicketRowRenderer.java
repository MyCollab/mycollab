package com.mycollab.module.project.view.ticket;

import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Span;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.core.utils.NumberUtils;
import com.mycollab.module.project.ProjectLinkGenerator;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.i18n.TimeTrackingI18nEnum;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.ui.components.TicketRowRender;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd
 * @since 7.0.0
 */
public class ReadableTicketRowRenderer extends TicketRowRender {

    public ReadableTicketRowRenderer(ProjectTicket ticket) {
        this.ticket = ticket;
        A ticketDiv;
        if (ticket.isBug() || ticket.isTask()) {
            ticketDiv = new A(ProjectLinkGenerator.generateProjectItemLink(ticket.getProjectShortName(), ticket.getProjectId(), ticket.getType(), ticket.getExtraTypeId() + "")).
                    appendText(ticket.getName());
        } else {
            ticketDiv = new A(ProjectLinkGenerator.generateProjectItemLink(ticket.getProjectShortName(), ticket.getProjectId(), ticket.getType(), ticket.getTypeId() + "")).
                    appendText(ticket.getName());
        }
        this.with(ELabel.html(ProjectAssetsManager.getAsset(ticket.getType()).getHtml() + " " + ticketDiv.write())
                .withStyleName(WebThemes.LABEL_WORD_WRAP).withFullWidth());
        this.addStyleName(WebThemes.BORDER_LIST_ROW);
        if (ticket.isTask()) {
            this.addStyleName("task");
        } else if (ticket.isBug()) {
            this.addStyleName("bug");
        } else if (ticket.isRisk()) {
            this.addStyleName("risk");
        }
        CssLayout footer = new CssLayout();
        footer.addComponent(buildTicketCommentComp(ticket));
        footer.addComponent(buildTicketStatusComp(ticket));
        footer.addComponent(buildStartdateComp(ticket));
        footer.addComponent(buildEnddateComp(ticket));
        footer.addComponent(buildDuedateComp(ticket));
        if (!SiteConfiguration.isCommunityEdition()) {
            footer.addComponent(buildBillableHoursComp(ticket));
            footer.addComponent(buildNonBillableHoursComp(ticket));
        }
        this.with(footer);
    }

    private Component buildTicketCommentComp(ProjectTicket ticket) {
        return ELabel.html(VaadinIcons.COMMENT_O.getHtml() + " " + NumberUtils.zeroIfNull(ticket.getNumComments()))
                .withDescription(UserUIContext.getMessage(GenericI18Enum.OPT_COMMENTS)).withStyleName(WebThemes.META_INFO);
    }

    private Component buildTicketStatusComp(ProjectTicket ticket) {
        return ELabel.html(VaadinIcons.INFO_CIRCLE.getHtml() + " " + ticket.getStatus())
                .withDescription(UserUIContext.getMessage(GenericI18Enum.FORM_STATUS)).withStyleName(WebThemes.MARGIN_LEFT_HALF, WebThemes.META_INFO);
    }

    private Component buildStartdateComp(ProjectTicket ticket) {
        if (ticket.getStartDate() == null) {
            Div divHint = new Div().setCSSClass("nonValue");
            divHint.appendText(VaadinIcons.TIME_FORWARD.getHtml());
            divHint.appendChild(new Span().appendText(UserUIContext.getMessage(GenericI18Enum.OPT_UNDEFINED)).setCSSClass("hide"));
            return ELabel.html(divHint.write()).withDescription(UserUIContext.getMessage(GenericI18Enum.FORM_START_DATE))
                    .withStyleName(WebThemes.META_INFO, WebThemes.MARGIN_LEFT_HALF);
        } else {
            Div startDateDiv = new Div().appendText(String.format(" %s %s", VaadinIcons.TIME_FORWARD.getHtml(),
                    UserUIContext.formatDate(ticket.getStartDate())));
            return ELabel.html(startDateDiv.write()).withDescription(UserUIContext.getMessage(GenericI18Enum.FORM_START_DATE))
                    .withStyleName(WebThemes.META_INFO, WebThemes.MARGIN_LEFT_HALF);
        }
    }

    private Component buildEnddateComp(ProjectTicket ticket) {
        if (ticket.getEndDate() == null) {
            Div divHint = new Div().setCSSClass("nonValue");
            divHint.appendText(VaadinIcons.TIME_BACKWARD.getHtml());
            divHint.appendChild(new Span().appendText(UserUIContext.getMessage(GenericI18Enum.OPT_UNDEFINED)).setCSSClass("hide"));
            return ELabel.html(divHint.write()).withDescription(UserUIContext.getMessage(GenericI18Enum.FORM_START_DATE))
                    .withStyleName(WebThemes.META_INFO, WebThemes.MARGIN_LEFT_HALF);
        } else {
            Div startDateDiv = new Div().appendText(String.format(" %s %s", VaadinIcons.TIME_BACKWARD.getHtml(),
                    UserUIContext.formatDate(ticket.getEndDate())));
            return ELabel.html(startDateDiv.write()).withDescription(UserUIContext.getMessage(GenericI18Enum.FORM_END_DATE))
                    .withStyleName(WebThemes.META_INFO, WebThemes.MARGIN_LEFT_HALF);
        }
    }

    private Component buildDuedateComp(ProjectTicket ticket) {
        if (ticket.getDueDate() == null) {
            Div divHint = new Div().setCSSClass("nonValue");
            divHint.appendText(VaadinIcons.CLOCK.getHtml());
            divHint.appendChild(new Span().appendText(UserUIContext.getMessage(GenericI18Enum.OPT_UNDEFINED)).setCSSClass("hide"));
            return ELabel.html(divHint.write()).withDescription(UserUIContext.getMessage(GenericI18Enum.FORM_START_DATE))
                    .withStyleName(WebThemes.META_INFO, WebThemes.MARGIN_LEFT_HALF);
        } else {
            Div startDateDiv = new Div().appendText(String.format(" %s %s", VaadinIcons.CLOCK.getHtml(),
                    UserUIContext.formatDate(ticket.getDueDate())));
            return ELabel.html(startDateDiv.write()).withDescription(UserUIContext.getMessage(GenericI18Enum.FORM_DUE_DATE))
                    .withStyleName(WebThemes.META_INFO, WebThemes.MARGIN_LEFT_HALF);
        }
    }

    private Component buildBillableHoursComp(ProjectTicket ticket) {
        return ELabel.html(VaadinIcons.MONEY.getHtml() + " " + NumberUtils.zeroIfNull(ticket.getBillableHours()))
                .withDescription(UserUIContext.getMessage(TimeTrackingI18nEnum.OPT_BILLABLE_HOURS)).withStyleName(WebThemes.META_INFO, WebThemes.MARGIN_LEFT_HALF);
    }

    private Component buildNonBillableHoursComp(ProjectTicket ticket) {
        return ELabel.html(VaadinIcons.GIFT.getHtml() + " " + NumberUtils.zeroIfNull(ticket.getNonBillableHours()))
                .withDescription(UserUIContext.getMessage(TimeTrackingI18nEnum.OPT_NON_BILLABLE_HOURS)).withStyleName(WebThemes.META_INFO, WebThemes.MARGIN_LEFT_HALF);
    }
}
