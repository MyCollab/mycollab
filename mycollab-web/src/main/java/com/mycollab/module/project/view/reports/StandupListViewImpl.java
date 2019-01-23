package com.mycollab.module.project.view.reports;

import com.google.common.eventbus.Subscribe;
import com.hp.gagawa.java.elements.A;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.db.arguments.DateSearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.module.project.ProjectLinkGenerator;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.SimpleStandupReport;
import com.mycollab.module.project.domain.StandupReportStatistic;
import com.mycollab.module.project.domain.criteria.StandupReportSearchCriteria;
import com.mycollab.module.project.event.StandUpEvent;
import com.mycollab.module.project.i18n.StandupI18nEnum;
import com.mycollab.module.project.service.StandupReportService;
import com.mycollab.module.project.ui.ProjectAssetsUtil;
import com.mycollab.module.project.ui.components.ComponentUtils;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.ApplicationEventListener;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.vaadin.TooltipHelper;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.AbstractVerticalPageView;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.*;
import com.mycollab.vaadin.web.ui.AbstractBeanPagedList;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.apache.commons.collections.CollectionUtils;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.time.LocalDate;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class StandupListViewImpl extends AbstractVerticalPageView implements StandupListView {
    private static final long serialVersionUID = 1L;

    private DateField standupCalendar = new DateField();

    private ProjectListComp projectListComp;
    private StandupPerProjectView standupPerProjectView;
    private List<Integer> projectIds;
    private StandupReportStatistic selectedProject = null;
    private LocalDate onDate = LocalDate.now();

    private ApplicationEventListener<StandUpEvent.DisplayStandupInProject> displayStandupHandler = new
            ApplicationEventListener<StandUpEvent.DisplayStandupInProject>() {
                @Override
                @Subscribe
                public void handle(StandUpEvent.DisplayStandupInProject event) {
                    Integer projectId = event.getData();
                    standupPerProjectView.displayReports(projectId, onDate);
                }
            };

    public StandupListViewImpl() {
        this.setMargin(new MarginInfo(false, false, true, false));
        standupCalendar.addValueChangeListener(valueChangeEvent -> {
            onDate = standupCalendar.getValue();
            showReports();
        });
    }

    @Override
    public void attach() {
        EventBusFactory.getInstance().register(displayStandupHandler);
        super.attach();
    }

    @Override
    public void detach() {
        EventBusFactory.getInstance().unregister(displayStandupHandler);
        super.detach();
    }

    @Override
    public void display(List<Integer> projectIds, LocalDate date) {
        this.projectIds = projectIds;
        this.onDate = date;
        standupCalendar.setValue(date);
    }

    private void showReports() {
        removeAllComponents();
        if (CollectionUtils.isNotEmpty(projectIds)) {
            MHorizontalLayout headerLayout = constructHeader();
            ELabel listLnl = ELabel.h3("Projects (" + projectIds.size() + ")");
            MHorizontalLayout favoriteListHeaderPanel = new MHorizontalLayout(listLnl).expand(listLnl).withMargin(new
                    MarginInfo(false, false, false, true)).withStyleName(WebThemes.PANEL_HEADER).withFullWidth().alignAll(Alignment.MIDDLE_LEFT);
            projectListComp = new ProjectListComp();
            MVerticalLayout projectListPanel = new MVerticalLayout(favoriteListHeaderPanel, projectListComp).withMargin(false).withSpacing(false).withWidth("300px");

            standupPerProjectView = new StandupPerProjectView();
            standupPerProjectView.setMargin(new MarginInfo(false, false, false, true));
            MHorizontalLayout bodyLayout = new MHorizontalLayout(projectListPanel, standupPerProjectView).expand(standupPerProjectView);
            with(headerLayout, bodyLayout).expand(bodyLayout);

            int totalCount = projectListComp.display(projectIds, onDate);
            if (totalCount > 0) {
                StandupReportStatistic firstProject = projectListComp.getItemAt(0);
                if (firstProject != null) {
                    viewStandupReportsForProject(firstProject);
                }
                Component firstRow = projectListComp.getRowAt(0);
                if (firstRow != null) {
                    projectListComp.setSelectedRow(firstRow);
                }
            }
        }
    }

    private void viewStandupReportsForProject(StandupReportStatistic project) {
        this.selectedProject = project;
        standupPerProjectView.displayReports(project.getProjectId(), onDate);
    }

    private class ProjectListComp extends AbstractBeanPagedList<StandupReportStatistic> {
        private StandupReportService standupReportService;
        private List<Integer> projectIds;
        private LocalDate onDate;

        ProjectListComp() {
            super(new ProjectRowHandler(), 10);
            addStyleName(WebThemes.BORDER_LIST);
            standupReportService = AppContextUtil.getSpringBean(StandupReportService.class);
        }

        @Override
        protected QueryHandler<StandupReportStatistic> buildQueryHandler() {
            return new QueryHandler<StandupReportStatistic>() {
                @Override
                public int queryTotalCount() {
                    return projectIds.size();
                }

                @Override
                public List<StandupReportStatistic> queryCurrentData() {
                    return standupReportService.getProjectReportsStatistic(projectIds, onDate, searchRequest);
                }
            };
        }

        int display(List<Integer> projectIds, LocalDate date) {
            this.projectIds = projectIds;
            this.onDate = date;
            doSearch();
            return projectIds.size();
        }
    }

    private class ProjectRowHandler implements IBeanList.RowDisplayHandler<StandupReportStatistic> {
        @Override
        public Component generateRow(final IBeanList<StandupReportStatistic> host, final StandupReportStatistic project, int rowIndex) {
            ELabel projectLbl = new ELabel(project.getProjectName()).withStyleName(UIConstants.TEXT_ELLIPSIS);
            final MHorizontalLayout layout = new MHorizontalLayout(ProjectAssetsUtil.projectLogoComp(project
                    .getProjectKey(), project.getProjectId(), project.getProjectAvatarId(), 32),
                    projectLbl, new ELabel(" (" + project.getTotalWrittenReports() + " / "
                    + project.getTotalReports() + ")").withUndefinedWidth()).expand(projectLbl).withStyleName(WebThemes
                    .BORDER_LIST_ROW).withStyleName(WebThemes.CURSOR_POINTER).withFullWidth().alignAll(Alignment.MIDDLE_LEFT);
            layout.addLayoutClickListener(layoutClickEvent -> {
                selectedProject = project;
                EventBusFactory.getInstance().post(new StandUpEvent.DisplayStandupInProject(this, project.getProjectId()));
                ((AbstractBeanPagedList) host).setSelectedRow(layout);
            });
            return layout;
        }
    }

    private MHorizontalLayout constructHeader() {
        MHorizontalLayout header = new MHorizontalLayout().withSpacing(false).withMargin((new MarginInfo(true, false, true, false))).withFullWidth();
        header.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

        MHorizontalLayout headerLeft = new MHorizontalLayout();

        HeaderWithIcon titleLbl = ComponentUtils.headerH2(ProjectTypeConstants.STANDUP,
                UserUIContext.getMessage(StandupI18nEnum.VIEW_LIST_TITLE));

        headerLeft.with(titleLbl, standupCalendar);
        header.with(headerLeft).withAlign(headerLeft, Alignment.TOP_LEFT);

        MButton newReportBtn = new MButton(UserUIContext.getMessage(StandupI18nEnum.BUTTON_ADD_REPORT_LABEL), clickEvent -> {
            if (selectedProject != null) {
                UI.getCurrent().addWindow(new StandupAddWindow(selectedProject, onDate));
            } else {
                NotificationUtil.showErrorNotification("You do not select any project");
            }
        }).withIcon(VaadinIcons.PLUS).withStyleName(WebThemes.BUTTON_ACTION);

        header.with(newReportBtn).withAlign(newReportBtn, Alignment.TOP_RIGHT);
        return header;
    }

    private static class StandupPerProjectView extends MVerticalLayout {
        private BeanList<StandupReportService, StandupReportSearchCriteria, SimpleStandupReport> reportInDay;
        private StandupMissingComp standupMissingComp;

        void displayReports(Integer projectId, LocalDate onDate) {
            removeAllComponents();
            reportInDay = new BeanList<>(AppContextUtil.getSpringBean(StandupReportService.class),
                    new StandupReportRowDisplay());
            standupMissingComp = new StandupMissingComp(projectId, onDate);
            this.with(standupMissingComp, reportInDay).expand(reportInDay);

            StandupReportSearchCriteria baseCriteria = new StandupReportSearchCriteria();
            baseCriteria.setOnDate(new DateSearchField(onDate, DateSearchField.EQUAL));
            baseCriteria.setProjectIds(new SetSearchField<>(projectId));
            reportInDay.setSearchCriteria(baseCriteria);
        }
    }

    private static class StandupReportRowDisplay implements IBeanList.RowDisplayHandler<SimpleStandupReport> {

        @Override
        public Component generateRow(IBeanList<SimpleStandupReport> host, SimpleStandupReport report, int rowIndex) {
            MHorizontalLayout rowLayout = new MHorizontalLayout().withStyleName(WebThemes.BORDER);

            MVerticalLayout userInfo = new MVerticalLayout().withWidth("200px").withFullHeight().withStyleName(WebThemes
                    .HOVER_EFFECT_NOT_BOX);
            userInfo.setDefaultComponentAlignment(Alignment.TOP_CENTER);

            Image userAvatar = UserAvatarControlFactory.createUserAvatarEmbeddedComponent(report.getLogByAvatarId(), 100);
            userAvatar.addStyleName(UIConstants.CIRCLE_BOX);
            userInfo.addComponent(userAvatar);
            Label memberLink = ELabel.html(buildMemberLink(report));
            userInfo.with(memberLink).expand(memberLink).withAlign(memberLink, Alignment.TOP_CENTER);
            rowLayout.addComponent(userInfo);

            MVerticalLayout reportContent = new MVerticalLayout().withStyleName(WebThemes.BORDER_LEFT, WebThemes.HOVER_EFFECT_NOT_BOX);

            reportContent.addComponent(ELabel.h3(UserUIContext.getMessage(StandupI18nEnum.STANDUP_LASTDAY)).withStyleName(UIConstants.LABEL_WORD_WRAP).withFullWidth());
            Label whatYesterdayField = new SafeHtmlLabel(report.getWhatlastday());
            whatYesterdayField.setWidth("100%");
            reportContent.addComponent(whatYesterdayField);

            reportContent.addComponent(ELabel.h3(UserUIContext.getMessage(StandupI18nEnum.STANDUP_TODAY)).withStyleName(UIConstants.LABEL_WORD_WRAP).withFullWidth());
            Label whatTodayField = new SafeHtmlLabel(report.getWhattoday());
            whatTodayField.setWidth("100%");
            reportContent.addComponent(whatTodayField);

            reportContent.addComponent(ELabel.h3(UserUIContext.getMessage(StandupI18nEnum.STANDUP_ISSUE)).withStyleName(UIConstants.LABEL_WORD_WRAP).withFullWidth());
            Label whatProblemField = new SafeHtmlLabel(report.getWhatproblem());
            whatProblemField.setWidth("100%");
            reportContent.addComponent(whatProblemField);

            rowLayout.with(reportContent).expand(reportContent);
            return rowLayout;
        }

        private String buildMemberLink(SimpleStandupReport report) {
            A userLink = new A().setId("tag" + TooltipHelper.TOOLTIP_ID).setHref(ProjectLinkGenerator.generateProjectMemberLink(report.getProjectid(), report.getLogby()))
                    .appendText(StringUtils.trim(report.getLogByFullName(), 30, true));
            userLink.setAttribute("onmouseover", TooltipHelper.userHoverJsFunction(report.getLogby()));
            userLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction());

            return userLink.write();
        }

    }
}
