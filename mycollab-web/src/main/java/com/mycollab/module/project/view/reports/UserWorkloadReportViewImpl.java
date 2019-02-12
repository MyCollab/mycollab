package com.mycollab.module.project.view.reports;

import com.hp.gagawa.java.elements.*;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.core.Tuple2;
import com.mycollab.core.utils.NumberUtils;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.SearchCriteria.OrderField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.html.DivLessFormatter;
import com.mycollab.module.file.StorageUtils;
import com.mycollab.module.project.ProjectLinkGenerator;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.domain.SimpleProject;
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria;
import com.mycollab.module.project.i18n.ProjectI18nEnum;
import com.mycollab.module.project.i18n.TimeTrackingI18nEnum;
import com.mycollab.module.project.service.ProjectRoleService;
import com.mycollab.module.project.service.ProjectService;
import com.mycollab.module.project.service.ProjectTicketService;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.ui.ProjectAssetsUtil;
import com.mycollab.security.PermissionMap;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.AsyncInvoker;
import com.mycollab.vaadin.TooltipHelper;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.HasSearchHandlers;
import com.mycollab.vaadin.mvp.AbstractVerticalPageView;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Map;
import java.util.*;

/**
 * @author MyCollab Ltd
 * @since 5.3.0
 */
@ViewComponent
public class UserWorkloadReportViewImpl extends AbstractVerticalPageView implements UserWorkloadReportView {

    private ProjectTicketSearchCriteria baseCriteria;

    private TicketCrossProjectsSearchPanel searchPanel;
    private MVerticalLayout wrapBody;

    public UserWorkloadReportViewImpl() {
        searchPanel = new TicketCrossProjectsSearchPanel();

        wrapBody = new MVerticalLayout().withMargin(new MarginInfo(true, false, false, false));
        with(searchPanel, wrapBody).expand(wrapBody);
    }

    @Override
    public HasSearchHandlers<ProjectTicketSearchCriteria> getSearchHandlers() {
        return this.searchPanel;
    }

    @Override
    public void queryTickets(ProjectTicketSearchCriteria searchCriteria) {
        this.baseCriteria = searchCriteria;
        wrapBody.removeAllComponents();

        ProjectService projectService = AppContextUtil.getSpringBean(ProjectService.class);
        List<SimpleProject> projects;
        if (UserUIContext.isAdmin()) {
            projects = projectService.getProjectsUserInvolved(null, AppUI.getAccountId());
        } else {
            projects = projectService.getProjectsUserInvolved(UserUIContext.getUsername(), AppUI.getAccountId());
        }

        AsyncInvoker.access(getUI(), new AsyncInvoker.PageCommand() {
            public void run() {
                ProjectTicketService projectTicketService = AppContextUtil.getSpringBean(ProjectTicketService.class);

                if (UserUIContext.isAdmin()) {
                    projects.forEach(project -> {
                        wrapBody.addComponent(buildProjectLink(project));

                        baseCriteria.setProjectIds(new SetSearchField<>(project.getId()));
                        baseCriteria.setOrderFields(Arrays.asList(new OrderField("assignUser", SearchCriteria.ASC)));
                        List<ProjectTicket> ticketsByCriteria = (List<ProjectTicket>) projectTicketService.findTicketsByCriteria(new BasicSearchRequest<>(baseCriteria));
                        wrapBody.addComponent(buildProjectTicketsLayout(ticketsByCriteria));
                        push();
                    });
                } else {
                    Map<Integer, SimpleProject> mapProjects = new HashMap<>();
                    projects.forEach(project -> mapProjects.put(project.getId(), project));
                    ProjectRoleService roleService = AppContextUtil.getSpringBean(ProjectRoleService.class);
                    List<Tuple2<Integer, PermissionMap>> projectsPermissions = roleService.findProjectsPermissions(UserUIContext.getUsername(), new ArrayList<>(mapProjects.keySet()), AppUI.getAccountId());
                    projectsPermissions.forEach(prjPermission -> {
                        Integer projectId = prjPermission.getItem1();
                        PermissionMap permissionMap = prjPermission.getItem2();
                        List<String> ticketTypes = new ArrayList<>();
                        if (permissionMap.canWrite(ProjectRolePermissionCollections.TASKS)) {
                            ticketTypes.add(ProjectTypeConstants.TASK);
                        }

                        if (permissionMap.canWrite(ProjectRolePermissionCollections.BUGS)) {
                            ticketTypes.add(ProjectTypeConstants.BUG);
                        }

                        if (permissionMap.canWrite(ProjectRolePermissionCollections.RISKS)) {
                            ticketTypes.add(ProjectTypeConstants.RISK);
                        }


                        baseCriteria.setProjectIds(new SetSearchField<>(projectId));
                        if (!ticketTypes.isEmpty()) {
                            baseCriteria.setTypes(new SetSearchField<>(ticketTypes));
                            List<ProjectTicket> ticketsByCriteria = (List<ProjectTicket>) projectTicketService.findTicketsByCriteria(new BasicSearchRequest<>(baseCriteria));
                            wrapBody.addComponent(buildProjectTicketsLayout(ticketsByCriteria));
                            push();
                        }
                    });
                }
            }
        });
    }

    private MHorizontalLayout buildProjectLink(SimpleProject project) {
        Component projectLogo = ProjectAssetsUtil.projectLogoComp(project.getShortname(), project.getId(), project.getAvatarid(), 64);
        A projectLink = new A(ProjectLinkGenerator.generateProjectLink(project.getId())).appendText(project.getName());
        projectLink.setAttribute("onmouseover", TooltipHelper.projectHoverJsFunction(ProjectTypeConstants.PROJECT,
                project.getId() + ""));
        projectLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction());

        Div projectDiv = new Div().appendChild(projectLink);
        ELabel projectLbl = ELabel.html(projectDiv.write()).withStyleName(ValoTheme.LABEL_H2, ValoTheme.LABEL_NO_MARGIN);

        MHorizontalLayout footer = new MHorizontalLayout().withMargin(false).withStyleName(WebThemes.META_INFO, WebThemes.FLEX_DISPLAY);
        footer.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

        if (com.mycollab.core.utils.StringUtils.isNotBlank(project.getMemlead())) {
            Div leadAvatar = new DivLessFormatter().appendChild(new Img("", StorageUtils.getAvatarPath
                            (project.getLeadAvatarId(), 16)).setCSSClass(WebThemes.CIRCLE_BOX), DivLessFormatter.EMPTY_SPACE,
                    new A(ProjectLinkGenerator.generateProjectMemberLink(project.getId(), project.getMemlead()))
                            .appendText(com.mycollab.core.utils.StringUtils.trim(project.getLeadFullName(), 30, true)))
                    .setTitle(project.getLeadFullName());
            ELabel leadLbl = ELabel.html(UserUIContext.getMessage(ProjectI18nEnum.FORM_LEADER) + ": " + leadAvatar.write()).withUndefinedWidth();
            footer.with(leadLbl);
        }
        if (com.mycollab.core.utils.StringUtils.isNotBlank(project.getHomepage())) {
            ELabel homepageLbl = ELabel.html(VaadinIcons.GLOBE.getHtml() + " " + new A(project.getHomepage())
                    .appendText(project.getHomepage()).setTarget("_blank").write())
                    .withStyleName(ValoTheme.LABEL_SMALL).withUndefinedWidth();
            homepageLbl.setDescription(UserUIContext.getMessage(ProjectI18nEnum.FORM_HOME_PAGE));
            footer.addComponent(homepageLbl);
        }

        if (project.getPlanstartdate() != null) {
            ELabel dateLbl = ELabel.html(VaadinIcons.TIME_FORWARD.getHtml() + " " + UserUIContext.formatDate(project.getPlanstartdate()))
                    .withStyleName(ValoTheme.LABEL_SMALL).withDescription(UserUIContext.getMessage(GenericI18Enum.FORM_START_DATE)).withUndefinedWidth();
            footer.addComponent(dateLbl);
        }

        if (project.getPlanenddate() != null) {
            ELabel dateLbl = ELabel.html(VaadinIcons.TIME_BACKWARD.getHtml() + " " + UserUIContext.formatDate(project.getPlanenddate()))
                    .withStyleName(ValoTheme.LABEL_SMALL).withDescription(UserUIContext.getMessage(GenericI18Enum.FORM_END_DATE)).withUndefinedWidth();
            footer.addComponent(dateLbl);
        }

        if (project.getClientid() != null && !SiteConfiguration.isCommunityEdition()) {
            Div clientDiv = new Div();
            if (project.getClientAvatarId() == null) {
                clientDiv.appendText(VaadinIcons.INSTITUTION.getHtml() + " ");
            } else {
                Img clientImg = new Img("", StorageUtils.getEntityLogoPath(AppUI.getAccountId(), project.getClientAvatarId(), 16))
                        .setCSSClass(WebThemes.CIRCLE_BOX);
                clientDiv.appendChild(clientImg).appendChild(DivLessFormatter.EMPTY_SPACE);
            }
            clientDiv.appendChild(new A(ProjectLinkGenerator.generateClientPreviewLink(project.getClientid()))
                    .appendText(com.mycollab.core.utils.StringUtils.trim(project.getClientName(), 30, true)));
            ELabel clientLink = ELabel.html(clientDiv.write()).withStyleName(WebThemes.BUTTON_LINK)
                    .withUndefinedWidth();
            footer.addComponents(clientLink);
        }

        MVerticalLayout bodyLayout = new MVerticalLayout(projectLbl, footer).withMargin(false).withSpacing(false);
        return new MHorizontalLayout(projectLogo, bodyLayout).expand(bodyLayout).alignAll(Alignment.MIDDLE_LEFT)
                .withMargin(new MarginInfo(true, false, false, false)).withFullWidth();
    }

    private MVerticalLayout buildProjectTicketsLayout(List<ProjectTicket> tickets) {
        MVerticalLayout layout = new MVerticalLayout().withSpacing(false).withMargin(false);
        String currentAssignUser = "";
        UserGroupLayout userGroupLayout = null;
        for (ProjectTicket ticket : tickets) {
            if (!StringUtils.equals(currentAssignUser, ticket.getAssignUser())) {
                currentAssignUser = ticket.getAssignUser();
                userGroupLayout = new UserGroupLayout(ticket.getProjectId(), ticket.getAssignUser(), ticket.getAssignUserFullName(), ticket.getAssignUserAvatarId());
                layout.add(userGroupLayout);
            }
            userGroupLayout.addTicketComp(buildTicketComp(ticket));
        }
        return layout;
    }

    private MVerticalLayout buildTicketComp(ProjectTicket ticket) {
        A ticketDiv = new A(ProjectLinkGenerator.generateProjectItemLink(ticket.getProjectShortName(), ticket.getProjectId(), ticket.getType(), ticket.getTypeId() + "")).
                appendText(ticket.getName());
        MVerticalLayout layout = new MVerticalLayout(ELabel.html(ProjectAssetsManager.getAsset(ticket.getType()).getHtml() + " " + ticketDiv.write()));
        layout.addStyleName(WebThemes.BORDER_LIST_ROW);
        if (ticket.isTask()) {
            layout.addStyleName("task");
        } else if (ticket.isBug()) {
            layout.addStyleName("bug");
        } else if (ticket.isRisk()) {
            layout.addStyleName("risk");
        }
        CssLayout footer = new CssLayout();
        footer.addComponent(buildTicketCommentComp(ticket));
        footer.addComponent(buildStartdateComp(ticket));
        footer.addComponent(buildEnddateComp(ticket));
        footer.addComponent(buildDuedateComp(ticket));
        if (!SiteConfiguration.isCommunityEdition()) {
            footer.addComponent(buildBillableHoursComp(ticket));
            footer.addComponent(buildNonBillableHoursComp(ticket));
        }
        layout.with(footer);
        return layout;
    }

    private Component buildTicketCommentComp(ProjectTicket ticket) {
        return ELabel.html(VaadinIcons.COMMENT_O.getHtml() + " " + NumberUtils.zeroIfNull(ticket.getNumComments()))
                .withDescription(UserUIContext.getMessage(GenericI18Enum.OPT_COMMENTS)).withStyleName(WebThemes.META_INFO);
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

    private static class UserGroupLayout extends CssLayout {
        private VerticalLayout bodyLayout;

        UserGroupLayout(Integer projectId, String username, String displayName, String avatarId) {
            this.setWidth("100%");
            this.addStyleName(WebThemes.MARGIN_LEFT_HALF);
            Div userDiv;
            if (username == null) {
                userDiv = new Div().appendText(UserUIContext.getMessage(GenericI18Enum.OPT_UNDEFINED));
            } else {
                Img userAvatar = new Img("", StorageUtils.getAvatarPath(avatarId, 16))
                        .setCSSClass(WebThemes.CIRCLE_BOX);
                A userLink = new A(ProjectLinkGenerator.generateProjectMemberLink(projectId, username)).appendText(displayName);
                userDiv = new Div().appendChild(userAvatar, new Text(" "), userLink);
            }

            this.addComponent(ELabel.html(userDiv.write()).withStyleName(ValoTheme.LABEL_H3, WebThemes.MARGIN_TOP, WebThemes.MARGIN_BOTTOM));
            bodyLayout = new MVerticalLayout().withStyleName(WebThemes.MARGIN_LEFT_HALF, WebThemes.BORDER_LIST).withSpacing(false).withFullWidth();
            this.addComponent(bodyLayout);
        }

        void addTicketComp(Component ticketComp) {
            bodyLayout.addComponent(ticketComp);
        }
    }

    @Override
    public ProjectTicketSearchCriteria getCriteria() {
        return baseCriteria;
    }
}
