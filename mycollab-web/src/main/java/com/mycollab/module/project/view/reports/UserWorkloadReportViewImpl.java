package com.mycollab.module.project.view.reports;

import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.mycollab.core.Tuple2;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.SearchCriteria.OrderField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.module.project.ProjectLinkGenerator;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.domain.SimpleProject;
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria;
import com.mycollab.module.project.service.ProjectRoleService;
import com.mycollab.module.project.service.ProjectService;
import com.mycollab.module.project.service.ProjectTicketService;
import com.mycollab.security.PermissionMap;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.AsyncInvoker;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.AbstractVerticalPageView;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.ELabel;
import com.vaadin.ui.Component;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

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

        wrapBody = new MVerticalLayout();
        with(searchPanel, wrapBody).expand(wrapBody);
    }

    @Override
    public void display() {
        ProjectTicketSearchCriteria searchCriteria = new ProjectTicketSearchCriteria();
        queryTickets(searchCriteria);
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

                        ProjectTicketSearchCriteria ticketSearchCriteria = new ProjectTicketSearchCriteria();
                        ticketSearchCriteria.setProjectIds(new SetSearchField<>(project.getId()));
                        ticketSearchCriteria.setOrderFields(Arrays.asList(new OrderField("assignUser", SearchCriteria.ASC)));
                        List<ProjectTicket> ticketsByCriteria = (List<ProjectTicket>) projectTicketService.findTicketsByCriteria(new BasicSearchRequest<>(ticketSearchCriteria));
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

                        ProjectTicketSearchCriteria ticketSearchCriteria = new ProjectTicketSearchCriteria();
                        ticketSearchCriteria.setProjectIds(new SetSearchField<>(projectId));
                        List<ProjectTicket> ticketsByCriteria = (List<ProjectTicket>) projectTicketService.findTicketsByCriteria(new BasicSearchRequest<>(ticketSearchCriteria));
                        ticketsByCriteria.forEach(ticket -> addComponent(new ELabel(ticket.getName())));
                        push();
                    });
                }
            }
        });
    }

    private ELabel buildProjectLink(SimpleProject project) {
        A projectLink = new A(ProjectLinkGenerator.generateProjectLink(project.getId())).appendText(project.getName());
        Div projectDiv = new Div().appendChild(projectLink);
        return ELabel.html(projectDiv.write());
    }

    private MVerticalLayout buildProjectTicketsLayout(List<ProjectTicket> tickets) {
        MVerticalLayout layout = new MVerticalLayout();
        String currentAssignUser = "";
        int assignTicketNum = 0;
        for (ProjectTicket ticket : tickets) {
            if (!currentAssignUser.equals(ticket.getAssignUserFullName())) {
                currentAssignUser = ticket.getAssignUserFullName();
                Div userDiv = new Div().appendText(currentAssignUser);
                layout.addComponent(ELabel.html(userDiv.write()));
            }
            layout.addComponent(buildTicketComp(ticket));
        }
        return layout;
    }

    private Component buildTicketComp(ProjectTicket ticket) {
        A ticketDiv = new A(ProjectLinkGenerator.generateProjectItemLink(ticket.getProjectShortName(), ticket.getProjectId(), ticket.getType(), ticket.getTypeId() + "")).
                appendText(ticket.getName());
        MCssLayout layout = new MCssLayout(ELabel.html(ticketDiv.write()));
        return layout;
    }

    @Override
    public ProjectTicketSearchCriteria getCriteria() {
        return baseCriteria;
    }
}
