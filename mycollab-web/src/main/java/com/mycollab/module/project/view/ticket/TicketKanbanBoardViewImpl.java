package com.mycollab.module.project.view.ticket;

import com.google.common.base.MoreObjects;
import com.google.common.eventbus.Subscribe;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Text;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.OptionI18nEnum;
import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.module.file.StorageUtils;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectLinkGenerator;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.domain.Risk;
import com.mycollab.module.project.domain.Task;
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria;
import com.mycollab.module.project.event.TicketEvent;
import com.mycollab.module.project.i18n.BugI18nEnum;
import com.mycollab.module.project.i18n.RiskI18nEnum;
import com.mycollab.module.project.i18n.TaskI18nEnum;
import com.mycollab.module.project.query.TicketQueryInfo;
import com.mycollab.module.project.service.ProjectMemberService;
import com.mycollab.module.project.service.ProjectTaskService;
import com.mycollab.module.project.service.ProjectTicketService;
import com.mycollab.module.project.service.RiskService;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.ui.components.TicketRowRender;
import com.mycollab.module.project.ui.components.IBlockContainer;
import com.mycollab.module.project.view.ProjectView;
import com.mycollab.module.project.view.service.TicketComponentFactory;
import com.mycollab.module.tracker.domain.BugWithBLOBs;
import com.mycollab.module.tracker.service.BugService;
import com.mycollab.module.user.domain.SimpleUser;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.*;
import com.mycollab.vaadin.event.HasSearchHandlers;
import com.mycollab.vaadin.mvp.AbstractVerticalPageView;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.ui.UIUtils;
import com.mycollab.vaadin.web.ui.ButtonGroup;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.dnd.DropEffect;
import com.vaadin.shared.ui.dnd.EffectAllowed;
import com.vaadin.ui.*;
import com.vaadin.ui.dnd.DragSourceExtension;
import com.vaadin.ui.dnd.DropTargetExtension;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.collections4.CollectionUtils;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum.*;

/**
 * @author MyCollab Ltd
 * @since 5.1.1
 */
@ViewComponent
public class TicketKanbanBoardViewImpl extends AbstractVerticalPageView implements TicketKanbanBoardView {

    private ProjectTicketService ticketService = AppContextUtil.getSpringBean(ProjectTicketService.class);

    private TicketSearchPanel searchPanel;
    private MCssLayout kanbanLayout;
    private Map<Pair, KanbanBlock> kanbanBlocks;
    private ProjectTicketSearchCriteria baseCriteria;

    private StatusI18nEnum[] statuses = OptionI18nEnum.statuses;

    private ApplicationEventListener<TicketEvent.SearchRequest> searchHandler = new
            ApplicationEventListener<TicketEvent.SearchRequest>() {
                @Override
                @Subscribe
                public void handle(TicketEvent.SearchRequest event) {
                    ProjectTicketSearchCriteria criteria = event.getSearchCriteria();
                    criteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
                    queryTickets(criteria);
                }
            };

    public TicketKanbanBoardViewImpl() {
        this.withSpacing(true).withMargin(new MarginInfo(false, true, true, true));
        searchPanel = new TicketSearchPanel();
        MHorizontalLayout groupWrapLayout = new MHorizontalLayout();
        groupWrapLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        searchPanel.addHeaderRight(groupWrapLayout);

        MButton allFilterBtn = new MButton("All").withStyleName(WebThemes.BUTTON_OPTION).withListener((Button.ClickListener) clickEvent -> {
            statuses = OptionI18nEnum.statuses;
            baseCriteria.setTypes(new SetSearchField<>(ProjectTypeConstants.BUG, ProjectTypeConstants.TASK, ProjectTypeConstants.RISK));
            queryTickets(baseCriteria);
        });

        MButton filterBugsBtn = new MButton(UserUIContext.getMessage(BugI18nEnum.SINGLE)).withStyleName(WebThemes.BUTTON_OPTION).withListener((Button.ClickListener) clickEvent -> {
            statuses = OptionI18nEnum.bugStatuses;
            baseCriteria.setTypes(new SetSearchField<>(ProjectTypeConstants.BUG));
            queryTickets(baseCriteria);
        });

        MButton filterTasksBtn = new MButton(UserUIContext.getMessage(TaskI18nEnum.SINGLE)).withStyleName(WebThemes.BUTTON_OPTION).withListener((Button.ClickListener) clickEvent -> {
            statuses = OptionI18nEnum.taskStatuses;
            baseCriteria.setTypes(new SetSearchField<>(ProjectTypeConstants.TASK));
            queryTickets(baseCriteria);
        });

        MButton filterRisksBtn = new MButton(UserUIContext.getMessage(RiskI18nEnum.SINGLE)).withStyleName(WebThemes.BUTTON_OPTION).withListener((Button.ClickListener) clickEvent -> {
            statuses = OptionI18nEnum.riskStatuses;
            baseCriteria.setTypes(new SetSearchField<>(ProjectTypeConstants.RISK));
            queryTickets(baseCriteria);
        });

        ButtonGroup group = new ButtonGroup(allFilterBtn, filterBugsBtn, filterTasksBtn, filterRisksBtn).withDefaultButton(allFilterBtn);

        MHorizontalLayout controlLayout = new MHorizontalLayout(ELabel.html("Filter by: "), group)
                .withDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        kanbanLayout = new MCssLayout().withStyleName("kanban-layout", WebThemes.NO_SCROLLABLE_CONTAINER);
        this.with(searchPanel, controlLayout, kanbanLayout).expand(kanbanLayout);
    }

    @Override
    public HasSearchHandlers<ProjectTicketSearchCriteria> getSearchHandlers() {
        return searchPanel;
    }

    @Override
    public void attach() {
        EventBusFactory.getInstance().register(searchHandler);
        super.attach();
    }

    @Override
    public void detach() {
        setProjectNavigatorVisibility(true);
        EventBusFactory.getInstance().unregister(searchHandler);
        super.detach();
    }

    private void setProjectNavigatorVisibility(boolean visibility) {
        ProjectView view = UIUtils.getRoot(this, ProjectView.class);
        if (view != null) {
            view.setNavigatorVisibility(visibility);
        }
    }

    @Override
    public void display() {
        searchPanel.selectQueryInfo(TicketQueryInfo.ALL_TICKETS);
    }

    private void reload() {
        if (baseCriteria == null) {
            display();
        } else {
            queryTickets(baseCriteria);
        }
    }

    @Override
    public void queryTickets(ProjectTicketSearchCriteria searchCriteria) {
        baseCriteria = searchCriteria;
        kanbanLayout.removeAllComponents();

        setProjectNavigatorVisibility(false);
        buildMembersBlock();

        AsyncInvoker.access(getUI(), new AsyncInvoker.PageCommand() {
            @Override
            public void run() {
                int totalTickets = ticketService.getTotalCount(searchCriteria);
                searchPanel.setTotalCountNumber(totalTickets);
                int pages = totalTickets / 50;
                for (int page = 0; page < pages + 1; page++) {
                    List<ProjectTicket> tickets = (List<ProjectTicket>) ticketService.findPageableListByCriteria(new BasicSearchRequest<>(searchCriteria, page + 1, 50));
                    if (CollectionUtils.isNotEmpty(tickets)) {
                        tickets.forEach(ticket -> {
                            String status = ticket.getStatus();
                            String assignee = MoreObjects.firstNonNull(ticket.getAssignUser(), "");
                            Pair pair = new Pair(assignee, status);
                            KanbanBlock kanbanBlock = kanbanBlocks.get(pair);
                            if (kanbanBlock != null) {
                                kanbanBlock.addBlockItem(new KanbanBlockItem(ticket));
                            }
                        });
                        this.push();
                    }
                }
            }
        });
    }

    private void buildMembersBlock() {
        ProjectMemberService projectMemberService = AppContextUtil.getSpringBean(ProjectMemberService.class);
        List<SimpleUser> activeMembers = projectMemberService.getActiveUsersInProject(CurrentProjectVariables.getProjectId(), AppUI.getAccountId());

        kanbanBlocks = new ConcurrentHashMap<>();
        kanbanLayout.add(new NotAssigneeLayout());
        activeMembers.forEach(assignee -> kanbanLayout.addComponent(new MemberLayout(assignee)));
    }

    private class MemberLayout extends MVerticalLayout {
        private SimpleUser member;
        private MHorizontalLayout bodyLayout;

        MemberLayout(SimpleUser member) {
            withMargin(false);
            this.member = member;
            bodyLayout = new MHorizontalLayout();
            for (StatusI18nEnum status : statuses) {
                KanbanBlock kanbanBlock = new KanbanBlock(member.getUsername(), status.name());
                kanbanBlocks.put(new Pair(member.getUsername(), status.name()), kanbanBlock);
                bodyLayout.with(kanbanBlock);
            }

            Img userAvatar = new Img("", StorageUtils.getAvatarPath(member.getAvatarid(), 16))
                    .setCSSClass(WebThemes.CIRCLE_BOX);
            A userLink = new A(ProjectLinkGenerator.generateProjectMemberLink(CurrentProjectVariables.getProjectId(), member.getUsername())).appendText(member.getDisplayName());
            Div userDiv = new Div().appendChild(userAvatar, new Text(" "), userLink);
            this.with(ELabel.html(userDiv.write()).withStyleName(ValoTheme.LABEL_H3, WebThemes.MARGIN_TOP, WebThemes.MARGIN_BOTTOM), bodyLayout);
        }
    }

    private class NotAssigneeLayout extends MVerticalLayout {
        private MHorizontalLayout bodyLayout;

        NotAssigneeLayout() {
            withMargin(false);
            bodyLayout = new MHorizontalLayout();
            for (StatusI18nEnum status : statuses) {
                KanbanBlock kanbanBlock = new KanbanBlock("", status.name());
                kanbanBlocks.put(new Pair("", status.name()), kanbanBlock);
                bodyLayout.with(kanbanBlock);
            }

            with(new ELabel(UserUIContext.getMessage(GenericI18Enum.OPT_UNDEFINED)).withStyleName(ValoTheme.LABEL_NO_MARGIN, ValoTheme.LABEL_H3), bodyLayout);
        }
    }

    private static class KanbanBlockItem extends TicketRowRender {
        private ProjectTicket projectTicket;

        private KanbanBlockItem(ProjectTicket ticket) {
            this.projectTicket = ticket;
            this.addStyleName("kanban-item list-row");
            if (ticket.isBug()) {
                this.addStyleName("bug");
            } else if (ticket.isRisk()) {
                this.addStyleName("risk");
            } else if (ticket.isTask()) {
                this.addStyleName("task");
            }

            TicketComponentFactory popupFieldFactory = AppContextUtil.getSpringBean(TicketComponentFactory.class);

            MHorizontalLayout headerLayout = new MHorizontalLayout();

            ToggleTicketSummaryField toggleTicketSummaryField = new ToggleTicketSummaryField(projectTicket);
            ELabel iconLbl = ELabel.html(ProjectAssetsManager.getAsset(ticket.getType()).getHtml()).withUndefinedWidth();
            headerLayout.with(iconLbl, toggleTicketSummaryField).expand(toggleTicketSummaryField);

            this.with(headerLayout);

            CssLayout footer = new CssLayout();

            footer.addComponent(popupFieldFactory.createCommentsPopupField(projectTicket));
            footer.addComponent(popupFieldFactory.createFollowersPopupField(projectTicket));
            footer.addComponent(popupFieldFactory.createAssigneePopupField(projectTicket));

            this.addComponent(footer);

            DragSourceExtension<KanbanBlockItem> dragSource = new DragSourceExtension<>(this);

            // set the allowed effect
            dragSource.setEffectAllowed(EffectAllowed.MOVE);
            // set the text to transfer
            dragSource.setDataTransferText("hello receiver");
            // set other data to transfer (in this case HTML)
            dragSource.setDataTransferData("text/html", "<label>hello receiver</label>");
        }
    }

    private static class KanbanBlock extends MVerticalLayout implements IBlockContainer {
        private String status;
        private String assignee;

        private MVerticalLayout dragLayoutContainer;
        private Label header;

        KanbanBlock(String assignee, String stage) {
            this.status = stage;
            this.assignee = assignee;
            this.withWidth("250px").withStyleName("kanban-block").withMargin(false);
            final String optionId = UUID.randomUUID().toString() + "-" + stage.hashCode();
            this.setId(optionId);
//            JavaScript.getCurrent().execute("$('#" + optionId + "').css({'background-color':'lightgray'});");

            dragLayoutContainer = new MVerticalLayout().withMargin(false).withId("drag-container");
            DropTargetExtension<VerticalLayout> dropTarget = new DropTargetExtension<>(dragLayoutContainer);
            // the drop effect must match the allowed effect in the drag source for a successful drop
            dropTarget.setDropEffect(DropEffect.MOVE);

            // catch the drops
            dropTarget.addDropListener(event -> {
                // if the drag source is in the same UI as the target
                Optional<AbstractComponent> dragSource = event.getDragSourceComponent();
                if (dragSource.isPresent() && dragSource.get() instanceof KanbanBlockItem) {
                    KanbanBlockItem kanbanItem = (KanbanBlockItem) dragSource.get();
                    ProjectTicket ticket = kanbanItem.projectTicket;

                    if (ticket.isBug() && (!stage.equals(Open.name()) && !stage.equals(ReOpen.name()) && !stage.equals(Verified.name())
                            && !stage.equals(Resolved.name()) && !stage.equals(InProgress.name()) && !stage.equals(Unresolved.name()))) {
                        NotificationUtil.showErrorNotification("Invalid state for bug");
                    } else if (ticket.isRisk() && (!stage.equals(Open.name()) && !stage.equals(Closed.name()))) {
                        NotificationUtil.showErrorNotification("Invalid state for risk");
                    } else if (ticket.isTask() && (!stage.equals(Open.name()) && !stage.equals(Pending.name()) && !stage.equals(InProgress.name()) && !stage.equals(Closed.name()))) {
                        NotificationUtil.showErrorNotification("Invalid state for task");
                    } else {
                        dragLayoutContainer.addComponent(kanbanItem);

                        if (ticket.isBug()) {
                            BugWithBLOBs bug = ProjectTicket.buildBug(ticket);
                            bug.setStatus(stage);
                            bug.setAssignuser(assignee);
                            BugService bugService = AppContextUtil.getSpringBean(BugService.class);
                            bugService.updateSelectiveWithSession(bug, UserUIContext.getUsername());
                        } else if (ticket.isTask()) {
                            Task task = ProjectTicket.buildTask(ticket);
                            task.setStatus(stage);
                            task.setAssignuser(assignee);
                            ProjectTaskService taskService = AppContextUtil.getSpringBean(ProjectTaskService.class);
                            taskService.updateSelectiveWithSession(task, UserUIContext.getUsername());
                        } else if (ticket.isRisk()) {
                            Risk risk = ProjectTicket.buildRisk(ticket);
                            risk.setStatus(stage);
                            risk.setAssignuser(assignee);
                            RiskService riskService = AppContextUtil.getSpringBean(RiskService.class);
                            riskService.updateSelectiveWithSession(risk, UserUIContext.getUsername());
                        }


                        refresh();

                        Component sourceComponent = event.getComponent();
                        KanbanBlock sourceKanban = UIUtils.getRoot(sourceComponent, KanbanBlock.class);
                        if (sourceKanban != null && sourceKanban != KanbanBlock.this) {
                            sourceKanban.refresh();
                        }
                    }
                }
            });

            MHorizontalLayout headerLayout = new MHorizontalLayout().withSpacing(false).withFullWidth().withStyleName("header");
            header = new Label(UserUIContext.getMessage(StatusI18nEnum.class, stage));
            headerLayout.with(header).expand(header);
            with(headerLayout, dragLayoutContainer);
        }

        void addBlockItem(KanbanBlockItem comp) {
            dragLayoutContainer.addComponent(comp);
            refresh();
        }

        private int getTicketComponentCount() {
            Component testComp = (dragLayoutContainer.getComponentCount() > 0) ? dragLayoutContainer.getComponent(0) : null;
            if (testComp instanceof KanbanBlockItem || testComp == null) {
                return dragLayoutContainer.getComponentCount();
            } else {
                return (dragLayoutContainer.getComponentCount() - 1);
            }
        }

        @Override
        public void refresh() {
            header.setValue(String.format("%s (%d)", UserUIContext.getMessage(StatusI18nEnum.class, status), getTicketComponentCount()));
        }
    }

    private static class Pair {
        private String memberName;

        private String status;

        Pair(String memberName, String status) {
            this.memberName = memberName;
            this.status = status;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair pair = (Pair) o;
            return memberName.equals(pair.memberName) &&
                    status.equals(pair.status);
        }

        @Override
        public int hashCode() {
            return Objects.hash(memberName, status);
        }
    }
}
