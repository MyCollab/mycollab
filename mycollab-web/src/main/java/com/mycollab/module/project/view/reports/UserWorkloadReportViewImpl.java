package com.mycollab.module.project.view.reports;

import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.jarektoro.responsivelayout.ResponsiveColumn;
import com.jarektoro.responsivelayout.ResponsiveLayout;
import com.jarektoro.responsivelayout.ResponsiveRow;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.core.MyCollabException;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.SearchCriteria.OrderField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.query.LazyValueInjector;
import com.mycollab.db.query.SearchFieldInfo;
import com.mycollab.html.DivLessFormatter;
import com.mycollab.module.file.StorageUtils;
import com.mycollab.module.project.ProjectLinkGenerator;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.domain.SimpleProject;
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria;
import com.mycollab.module.project.event.TicketEvent;
import com.mycollab.module.project.i18n.MilestoneI18nEnum;
import com.mycollab.module.project.i18n.ProjectI18nEnum;
import com.mycollab.module.project.query.TicketQueryInfo;
import com.mycollab.module.project.service.ProjectRoleService;
import com.mycollab.module.project.service.ProjectService;
import com.mycollab.module.project.service.ProjectTicketService;
import com.mycollab.module.project.ui.ProjectAssetsUtil;
import com.mycollab.module.project.view.ticket.*;
import com.mycollab.security.PermissionMap;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.vaadin.TooltipHelper;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.HasSearchHandlers;
import com.mycollab.vaadin.mvp.AbstractVerticalPageView;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.SavedFilterComboBox;
import com.mycollab.vaadin.web.ui.StringValueComboBox;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.3.0
 */
@ViewComponent
public class UserWorkloadReportViewImpl extends AbstractVerticalPageView implements UserWorkloadReportView {

    private ProjectTicketSearchCriteria baseCriteria;

    private TicketCrossProjectsSearchPanel searchPanel;
    private ResponsiveLayout wrapBody;
    private MVerticalLayout projectTicketsContentLayout;
    private TicketGroupOrderComponent ticketGroupOrderComponent;
    private TicketSavedFilterComboBox savedFilterComboBox;

    private ProjectRoleService projectRoleService;
    private ProjectTicketService projectTicketService;

    private SimpleProject selectedProject = null;
    private int currentPage = 0;
    private String groupByState;
    private String sortDirection;

    public UserWorkloadReportViewImpl() {
        projectRoleService = AppContextUtil.getSpringBean(ProjectRoleService.class);
        projectTicketService = AppContextUtil.getSpringBean(ProjectTicketService.class);
        searchPanel = new TicketCrossProjectsSearchPanel();

        wrapBody = new ResponsiveLayout();
        withSpacing(true).with(searchPanel, wrapBody).expand(wrapBody);

        MHorizontalLayout extraCompsHeaderLayout = new MHorizontalLayout();
        extraCompsHeaderLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

        savedFilterComboBox = new TicketSavedFilterComboBox();
        savedFilterComboBox.addQuerySelectListener((SavedFilterComboBox.QuerySelectListener) querySelectEvent -> {
            List<SearchFieldInfo<ProjectTicketSearchCriteria>> fieldInfos = querySelectEvent.getSearchFieldInfos();
            ProjectTicketSearchCriteria criteria = SearchFieldInfo.buildSearchCriteria(ProjectTicketSearchCriteria.class,
                    fieldInfos);
            EventBusFactory.getInstance().post(new TicketEvent.SearchRequest(UserWorkloadReportViewImpl.this, criteria));
            queryTickets(criteria);
        });

        extraCompsHeaderLayout.addComponent(new ELabel(UserUIContext.getMessage(GenericI18Enum.SAVE_FILTER_VALUE)));
        extraCompsHeaderLayout.addComponent(savedFilterComboBox);

        extraCompsHeaderLayout.addComponent(new ELabel(UserUIContext.getMessage(GenericI18Enum.ACTION_SORT)));
        StringValueComboBox sortCombo = new StringValueComboBox(false, UserUIContext.getMessage(GenericI18Enum.OPT_SORT_DESCENDING),
                UserUIContext.getMessage(GenericI18Enum.OPT_SORT_ASCENDING));
        sortCombo.setWidth("130px");
        sortCombo.addValueChangeListener(valueChangeEvent -> {
            String sortValue = sortCombo.getValue();
            if (UserUIContext.getMessage(GenericI18Enum.OPT_SORT_ASCENDING).equals(sortValue)) {
                sortDirection = SearchCriteria.ASC;
            } else {
                sortDirection = SearchCriteria.DESC;
            }
            displayProjectTickets(selectedProject);
        });
        sortDirection = SearchCriteria.DESC;
        extraCompsHeaderLayout.addComponent(sortCombo);

        extraCompsHeaderLayout.addComponent(new ELabel(UserUIContext.getMessage(GenericI18Enum.OPT_GROUP)));
        StringValueComboBox groupCombo = new StringValueComboBox(false, UserUIContext.getMessage(GenericI18Enum.FORM_DUE_DATE),
                UserUIContext.getMessage(GenericI18Enum.FORM_START_DATE), UserUIContext.getMessage(GenericI18Enum.FORM_CREATED_TIME),
                UserUIContext.getMessage(GenericI18Enum.OPT_PLAIN), UserUIContext.getMessage(GenericI18Enum.OPT_USER),
                UserUIContext.getMessage(MilestoneI18nEnum.SINGLE));
        groupByState = UserUIContext.getMessage(MilestoneI18nEnum.SINGLE);
        groupCombo.setValue(UserUIContext.getMessage(MilestoneI18nEnum.SINGLE));
        groupCombo.addValueChangeListener(valueChangeEvent -> {
            groupByState = groupCombo.getValue();
            displayProjectTickets(selectedProject);
        });
        groupCombo.setWidth("130px");

        extraCompsHeaderLayout.addComponent(groupCombo);

        MButton printBtn = new MButton("", clickEvent -> UI.getCurrent().addWindow(
                new TicketCustomizeReportOutputWindow(new LazyValueInjector() {
                    @Override
                    protected Object doEval() {
                        return baseCriteria;
                    }
                }))).withIcon(VaadinIcons.PRINT).withStyleName(WebThemes.BUTTON_OPTION)
                .withDescription(UserUIContext.getMessage(GenericI18Enum.ACTION_EXPORT));
        extraCompsHeaderLayout.addComponent(printBtn);

        searchPanel.addHeaderRight(extraCompsHeaderLayout);
    }

    @Override
    public HasSearchHandlers<ProjectTicketSearchCriteria> getSearchHandlers() {
        return this.searchPanel;
    }

    @Override
    public void display() {
        wrapBody.removeAllComponents();
        ProjectTicketSearchCriteria searchCriteria = new ProjectTicketSearchCriteria();

        ProjectService projectService = AppContextUtil.getSpringBean(ProjectService.class);
        List<SimpleProject> projects;
        if (UserUIContext.isAdmin()) {
            projects = projectService.getProjectsUserInvolved(null, AppUI.getAccountId());
        } else {
            projects = projectService.getProjectsUserInvolved(UserUIContext.getUsername(), AppUI.getAccountId());
        }

        ResponsiveRow row = wrapBody.addRow();
        ProjectListComp projectListComp = new ProjectListComp(projects);
        ResponsiveColumn leftCol = new ResponsiveColumn(12, 12, 3, 3);
        leftCol.setContent(projectListComp);

        row.addColumn(leftCol);

        ResponsiveColumn rightCol = new ResponsiveColumn(12, 12, 9, 9);
        projectTicketsContentLayout = new MVerticalLayout();
        rightCol.setContent(projectTicketsContentLayout);
        row.addColumn(rightCol);

        savedFilterComboBox.selectQueryInfo(TicketQueryInfo.OPEN_TICKETS);
    }

    @Override
    public void queryTickets(ProjectTicketSearchCriteria searchCriteria) {
        this.baseCriteria = searchCriteria;
        if (selectedProject != null) {
            displayProjectTickets(selectedProject);
        }
    }

    private class ProjectListComp extends MVerticalLayout {
        ProjectListComp(List<SimpleProject> projects) {
            withSpacing(false).withMargin(false).withStyleName(WebThemes.BORDER_TOP);
            projects.forEach(project -> addComponent(buildProjectComp(project)));
        }

        MHorizontalLayout buildProjectComp(SimpleProject project) {
            MButton projectLink = new MButton(project.getName()).withListener((Button.ClickListener) event -> displayProjectTickets(project)).withStyleName(WebThemes.BUTTON_LINK);
            return new MHorizontalLayout(projectLink).withMargin(new MarginInfo(false, true, false, true))
                    .withFullWidth().withStyleName("list-row");
        }
    }

    private void displayProjectTickets(SimpleProject project) {
        selectedProject = project;
        if (selectedProject != null) {
            projectTicketsContentLayout.removeAllComponents();
            projectTicketsContentLayout.addComponent(buildProjectLink(project));

            if (UserUIContext.isAdmin()) {
                baseCriteria.setProjectIds(new SetSearchField<>(project.getId()));
                baseCriteria.setOrderFields(Arrays.asList(new OrderField("assignUser", SearchCriteria.ASC)));
            } else {
                PermissionMap permissionMap = projectRoleService.findProjectsPermission(UserUIContext.getUsername(), project.getId(), AppUI.getAccountId());
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

                baseCriteria.setProjectIds(new SetSearchField<>(project.getId()));
                if (!ticketTypes.isEmpty()) {
                    baseCriteria.setTypes(new SetSearchField<>(ticketTypes));
                }
            }

            if (UserUIContext.getMessage(GenericI18Enum.FORM_DUE_DATE).equals(groupByState)) {
                baseCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField("dueDate", sortDirection)));
                ticketGroupOrderComponent = new DueDateOrderComponent(ReadableTicketRowRenderer.class);
            } else if (UserUIContext.getMessage(GenericI18Enum.FORM_START_DATE).equals(groupByState)) {
                baseCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField("startdate", sortDirection)));
                ticketGroupOrderComponent = new StartDateOrderComponent(ReadableTicketRowRenderer.class);
            } else if (UserUIContext.getMessage(GenericI18Enum.OPT_PLAIN).equals(groupByState)) {
                baseCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField("lastupdatedtime", sortDirection)));
                ticketGroupOrderComponent = new SimpleListOrderComponent(ReadableTicketRowRenderer.class);
            } else if (UserUIContext.getMessage(GenericI18Enum.FORM_CREATED_TIME).equals(groupByState)) {
                baseCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField("createdtime", sortDirection)));
                ticketGroupOrderComponent = new CreatedDateOrderComponent(ReadableTicketRowRenderer.class);
            } else if (UserUIContext.getMessage(GenericI18Enum.OPT_USER).equals(groupByState)) {
                baseCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField("assignUser", sortDirection)));
                ticketGroupOrderComponent = new UserOrderComponent(ReadableTicketRowRenderer.class);
            } else if (UserUIContext.getMessage(MilestoneI18nEnum.SINGLE).equals(groupByState)) {
                baseCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField("milestoneId", sortDirection)));
                ticketGroupOrderComponent = new MilestoneOrderGroup(ReadableTicketRowRenderer.class);
            } else {
                throw new MyCollabException("Do not support group view by " + groupByState);
            }
            projectTicketsContentLayout.addComponent(ticketGroupOrderComponent);

            ProjectTicketService projectTicketService = AppContextUtil.getSpringBean(ProjectTicketService.class);
            int totalTasks = projectTicketService.getTotalTicketsCount(baseCriteria);
            currentPage = 0;
            int pages = totalTasks / 100;
            if (currentPage < pages) {
                MButton moreBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.ACTION_MORE), clickEvent -> {
                    int newTotalTickets = projectTicketService.getTotalTicketsCount(baseCriteria);
                    int newNumPages = newTotalTickets / 100;
                    currentPage++;
                    List<ProjectTicket> otherTickets = (List<ProjectTicket>) projectTicketService.findTicketsByCriteria(new
                            BasicSearchRequest<>(baseCriteria, currentPage + 1, 100));
                    ticketGroupOrderComponent.insertTickets(otherTickets);
                    if (currentPage >= newNumPages) {
                        wrapBody.removeComponent(wrapBody.getComponent(1));
                    }
                }).withStyleName(WebThemes.BUTTON_ACTION).withIcon(VaadinIcons.ANGLE_DOUBLE_DOWN);
                wrapBody.addComponent(moreBtn);
            }
            List<ProjectTicket> tickets = (List<ProjectTicket>) projectTicketService.findTicketsByCriteria(new BasicSearchRequest<>
                    (baseCriteria, currentPage + 1, 100));
            ticketGroupOrderComponent.insertTickets(tickets);
        }
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

        if (StringUtils.isNotBlank(project.getMemlead())) {
            Div leadAvatar = new DivLessFormatter().appendChild(new Img("", StorageUtils.getAvatarPath
                            (project.getLeadAvatarId(), 16)).setCSSClass(WebThemes.CIRCLE_BOX), DivLessFormatter.EMPTY_SPACE,
                    new A(ProjectLinkGenerator.generateProjectMemberLink(project.getId(), project.getMemlead()))
                            .appendText(com.mycollab.core.utils.StringUtils.trim(project.getLeadFullName(), 30, true)))
                    .setTitle(project.getLeadFullName());
            ELabel leadLbl = ELabel.html(UserUIContext.getMessage(ProjectI18nEnum.FORM_LEADER) + ": " + leadAvatar.write()).withUndefinedWidth();
            footer.with(leadLbl);
        }
        if (StringUtils.isNotBlank(project.getHomepage())) {
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

    @Override
    public ProjectTicketSearchCriteria getCriteria() {
        return baseCriteria;
    }
}
