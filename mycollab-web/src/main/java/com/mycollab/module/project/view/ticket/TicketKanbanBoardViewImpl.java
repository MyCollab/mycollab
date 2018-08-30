package com.mycollab.module.project.view.ticket;

import com.google.common.eventbus.Subscribe;
import com.mycollab.common.i18n.OptionI18nEnum;
import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.domain.Risk;
import com.mycollab.module.project.domain.Task;
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria;
import com.mycollab.module.project.event.TicketEvent;
import com.mycollab.module.project.i18n.BugI18nEnum;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.module.project.i18n.RiskI18nEnum;
import com.mycollab.module.project.i18n.TaskI18nEnum;
import com.mycollab.module.project.query.TicketQueryInfo;
import com.mycollab.module.project.service.ProjectTaskService;
import com.mycollab.module.project.service.ProjectTicketService;
import com.mycollab.module.project.service.RiskService;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.ui.components.BlockRowRender;
import com.mycollab.module.project.ui.components.IBlockContainer;
import com.mycollab.module.project.view.ProjectView;
import com.mycollab.module.project.view.service.TicketComponentFactory;
import com.mycollab.module.tracker.domain.BugWithBLOBs;
import com.mycollab.module.tracker.service.BugService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.ApplicationEventListener;
import com.mycollab.vaadin.AsyncInvoker;
import com.mycollab.vaadin.AsyncInvoker.PageCommand;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.HasSearchHandlers;
import com.mycollab.vaadin.mvp.AbstractVerticalPageView;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.ui.UIUtils;
import com.mycollab.vaadin.web.ui.ToggleButtonGroup;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.event.dd.acceptcriteria.Not;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.dd.HorizontalDropLocation;
import com.vaadin.shared.ui.dd.VerticalDropLocation;
import com.vaadin.ui.*;
import fi.jasoft.dragdroplayouts.DDHorizontalLayout;
import fi.jasoft.dragdroplayouts.DDVerticalLayout;
import fi.jasoft.dragdroplayouts.client.ui.LayoutDragMode;
import fi.jasoft.dragdroplayouts.events.LayoutBoundTransferable;
import fi.jasoft.dragdroplayouts.events.VerticalLocationIs;
import org.apache.commons.collections.CollectionUtils;
import org.vaadin.jouni.restrain.Restrain;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
    private DDHorizontalLayout kanbanLayout;
    private Map<String, KanbanBlock> kanbanBlocks;
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
        this.setSizeFull();
        this.withSpacing(true).withMargin(new MarginInfo(false, true, true, true));
        searchPanel = new TicketSearchPanel();
        MHorizontalLayout groupWrapLayout = new MHorizontalLayout();
        groupWrapLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        searchPanel.addHeaderRight(groupWrapLayout);

        MButton advanceDisplayBtn = new MButton(UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_LIST),
                clickEvent -> EventBusFactory.getInstance().post(new TicketEvent.GotoDashboard(this, null)))
                .withIcon(FontAwesome.NAVICON).withWidth("100px");

        MButton kanbanBtn = new MButton(UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_KANBAN)).withIcon(FontAwesome.TH)
                .withWidth("100px");

        ToggleButtonGroup viewButtons = new ToggleButtonGroup();
        viewButtons.addButton(advanceDisplayBtn);
        viewButtons.addButton(kanbanBtn);
        viewButtons.withDefaultButton(kanbanBtn);
        groupWrapLayout.addComponent(viewButtons);

        ToggleButtonGroup group = new ToggleButtonGroup();

        MButton allFilterBtn = new MButton("All").withStyleName(WebThemes.BUTTON_OPTION).withListener((Button.ClickListener) clickEvent -> {
            statuses = OptionI18nEnum.statuses;
            baseCriteria.setTypes(new SetSearchField<>(ProjectTypeConstants.BUG, ProjectTypeConstants.TASK, ProjectTypeConstants.RISK));
            queryTickets(baseCriteria);
        });
        group.addButton(allFilterBtn);
        group.withDefaultButton(allFilterBtn);

        MButton filterBugsBtn = new MButton(UserUIContext.getMessage(BugI18nEnum.SINGLE)).withStyleName(WebThemes.BUTTON_OPTION).withListener((Button.ClickListener) clickEvent -> {
            statuses = OptionI18nEnum.bugStatuses;
            baseCriteria.setTypes(new SetSearchField<>(ProjectTypeConstants.BUG));
            queryTickets(baseCriteria);
        });
        group.addButton(filterBugsBtn);

        MButton filterTasksBtn = new MButton(UserUIContext.getMessage(TaskI18nEnum.SINGLE)).withStyleName(WebThemes.BUTTON_OPTION).withListener((Button.ClickListener) clickEvent -> {
            statuses = OptionI18nEnum.taskStatuses;
            baseCriteria.setTypes(new SetSearchField<>(ProjectTypeConstants.TASK));
            queryTickets(baseCriteria);
        });
        group.addButton(filterTasksBtn);

        MButton filterRisksBtn = new MButton(UserUIContext.getMessage(RiskI18nEnum.SINGLE)).withStyleName(WebThemes.BUTTON_OPTION).withListener((Button.ClickListener) clickEvent -> {
            statuses = OptionI18nEnum.riskStatuses;
            baseCriteria.setTypes(new SetSearchField<>(ProjectTypeConstants.RISK));
            queryTickets(baseCriteria);
        });
        group.addButton(filterRisksBtn);

        MHorizontalLayout controlLayout = new MHorizontalLayout(ELabel.html("Filter by: "), group)
                .withDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        kanbanLayout = new DDHorizontalLayout();
        kanbanLayout.setHeight("100%");
        kanbanLayout.addStyleName("kanban-layout");
        kanbanLayout.setSpacing(true);
        kanbanLayout.setComponentHorizontalDropRatio(0.3f);
        kanbanLayout.setDragMode(LayoutDragMode.CLONE_OTHER);

        // Enable dropping components
        kanbanLayout.setDropHandler(new DropHandler() {
            @Override
            public void drop(DragAndDropEvent event) {
                LayoutBoundTransferable transferable = (LayoutBoundTransferable) event.getTransferable();

                DDHorizontalLayout.HorizontalLayoutTargetDetails details = (DDHorizontalLayout.HorizontalLayoutTargetDetails) event
                        .getTargetDetails();
                Component dragComponent = transferable.getComponent();
                if (dragComponent instanceof KanbanBlock) {
                    KanbanBlock kanbanItem = (KanbanBlock) dragComponent;
                    int newIndex = details.getOverIndex();
                    if (details.getDropLocation() == HorizontalDropLocation.RIGHT) {
                        kanbanLayout.addComponent(kanbanItem);
                    } else if (newIndex == -1) {
                        kanbanLayout.addComponent(kanbanItem, 0);
                    } else {
                        kanbanLayout.addComponent(kanbanItem, newIndex);
                    }
                }
            }

            @Override
            public AcceptCriterion getAcceptCriterion() {
                return new Not(VerticalLocationIs.MIDDLE);
            }
        });

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
        kanbanBlocks = new ConcurrentHashMap<>();

        setProjectNavigatorVisibility(false);
        AsyncInvoker.access(getUI(), new PageCommand() {
            @Override
            public void run() {
                Arrays.stream(statuses).forEach(status -> {
                    KanbanBlock block = new KanbanBlock(status.name());
                    kanbanBlocks.put(status.name(), block);
                    kanbanLayout.addComponent(block);
                });
                push();

                int totalTickets = ticketService.getTotalCount(searchCriteria);
                searchPanel.setTotalCountNumber(totalTickets);
                int pages = totalTickets / 50;
                for (int page = 0; page < pages + 1; page++) {
                    List<ProjectTicket> tickets = (List<ProjectTicket>) ticketService.findPageableListByCriteria(new BasicSearchRequest<>(searchCriteria, page + 1, 50));
                    if (CollectionUtils.isNotEmpty(tickets)) {
                        tickets.forEach(ticket -> {
                            String status = ticket.getStatus();
                            KanbanBlock kanbanBlock = kanbanBlocks.get(status);
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

    private static class KanbanBlockItem extends BlockRowRender {
        private ProjectTicket projectTicket;

        private KanbanBlockItem(final ProjectTicket ticket) {
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
            ELabel iconLbl = ELabel.html(ProjectAssetsManager.getAsset(ticket.getType()).getHtml()).withWidthUndefined();
            headerLayout.with(iconLbl, toggleTicketSummaryField).expand(toggleTicketSummaryField);

            this.with(headerLayout);

            CssLayout footer = new CssLayout();

            footer.addComponent(popupFieldFactory.createCommentsPopupField(projectTicket));
            footer.addComponent(popupFieldFactory.createFollowersPopupField(projectTicket));
            footer.addComponent(popupFieldFactory.createAssigneePopupField(projectTicket));

            this.addComponent(footer);
        }
    }

    private class KanbanBlock extends MVerticalLayout implements IBlockContainer {
        private String status;
        private DDVerticalLayout dragLayoutContainer;
        private Label header;

        KanbanBlock(String stage) {
            this.withFullHeight().withWidth("250px").withStyleName("kanban-block").withMargin(false);
            this.status = stage;
            final String optionId = UUID.randomUUID().toString() + "-" + stage.hashCode();
            this.setId(optionId);
            JavaScript.getCurrent().execute("$('#" + optionId + "').css({'background-color':'lightgray'});");

            dragLayoutContainer = new DDVerticalLayout();
            dragLayoutContainer.setSpacing(true);
            dragLayoutContainer.setComponentVerticalDropRatio(0.3f);
            dragLayoutContainer.setDragMode(LayoutDragMode.CLONE);
            dragLayoutContainer.setDropHandler(new DropHandler() {
                @Override
                public void drop(DragAndDropEvent event) {
                    LayoutBoundTransferable transferable = (LayoutBoundTransferable) event.getTransferable();

                    DDVerticalLayout.VerticalLayoutTargetDetails details = (DDVerticalLayout.VerticalLayoutTargetDetails) event
                            .getTargetDetails();

                    Component dragComponent = transferable.getComponent();
                    if (dragComponent instanceof KanbanBlockItem) {
                        KanbanBlockItem kanbanItem = (KanbanBlockItem) dragComponent;
                        ProjectTicket ticket = kanbanItem.projectTicket;

                        if (ticket.isBug() && (!stage.equals(Open.name()) && !stage.equals(ReOpen.name()) && !stage.equals(Verified.name())
                                && !stage.equals(Resolved.name()) && !stage.equals(InProgress.name()) && !stage.equals(Unresolved.name()))) {
                            NotificationUtil.showErrorNotification("Invalid state for bug");
                        } else if (ticket.isRisk() && (!stage.equals(Open.name()) && !stage.equals(Closed.name()))) {
                            NotificationUtil.showErrorNotification("Invalid state for risk");
                        } else if (ticket.isTask() && (!stage.equals(Open.name()) && !stage.equals(Pending.name()) && !stage.equals(InProgress.name()) && !stage.equals(Closed.name()))) {
                            NotificationUtil.showErrorNotification("Invalid state for task");
                        } else {
                            int newIndex = details.getOverIndex();
                            if (details.getDropLocation() == VerticalDropLocation.BOTTOM) {
                                dragLayoutContainer.addComponent(kanbanItem);
                            } else if (newIndex == -1) {
                                dragLayoutContainer.addComponent(kanbanItem, 0);
                            } else {
                                dragLayoutContainer.addComponent(kanbanItem, newIndex);
                            }

                            if (ticket.isBug()) {
                                BugWithBLOBs bug = ProjectTicket.buildBug(ticket);
                                bug.setStatus(stage);
                                BugService bugService = AppContextUtil.getSpringBean(BugService.class);
                                bugService.updateSelectiveWithSession(bug, UserUIContext.getUsername());
                            } else if (ticket.isTask()) {
                                Task task = ProjectTicket.buildTask(ticket);
                                task.setStatus(stage);
                                ProjectTaskService taskService = AppContextUtil.getSpringBean(ProjectTaskService.class);
                                taskService.updateSelectiveWithSession(task, UserUIContext.getUsername());
                            } else if (ticket.isRisk()) {
                                Risk risk = ProjectTicket.buildRisk(ticket);
                                risk.setStatus(stage);
                                RiskService riskService = AppContextUtil.getSpringBean(RiskService.class);
                                riskService.updateSelectiveWithSession(risk, UserUIContext.getUsername());
                            }

                            refresh();

                            Component sourceComponent = transferable.getSourceComponent();
                            KanbanBlock sourceKanban = UIUtils.getRoot(sourceComponent, KanbanBlock.class);
                            if (sourceKanban != null && sourceKanban != KanbanBlock.this) {
                                sourceKanban.refresh();
                            }
                        }
                    }
                }

                @Override
                public AcceptCriterion getAcceptCriterion() {
                    return new Not(VerticalLocationIs.MIDDLE);
                }
            });
            new Restrain(dragLayoutContainer).setMinHeight("50px").setMaxHeight((UIUtils.getBrowserHeight() - 390) + "px");

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
}
