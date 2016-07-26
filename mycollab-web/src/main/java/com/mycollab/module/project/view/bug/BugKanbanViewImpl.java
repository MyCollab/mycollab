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
package com.mycollab.module.project.view.bug;

import com.google.common.eventbus.Subscribe;
import com.mycollab.common.domain.OptionVal;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.eventmanager.ApplicationEventListener;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.events.BugEvent;
import com.mycollab.module.project.i18n.BugI18nEnum;
import com.mycollab.module.project.i18n.OptionI18nEnum;
import com.mycollab.module.project.view.ProjectView;
import com.mycollab.module.project.view.bug.components.BugSavedFilterComboBox;
import com.mycollab.module.project.view.bug.components.ToggleBugSummaryField;
import com.mycollab.module.tracker.domain.SimpleBug;
import com.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.mycollab.module.tracker.service.BugService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.AsyncInvoker;
import com.mycollab.vaadin.events.HasSearchHandlers;
import com.mycollab.vaadin.mvp.AbstractPageView;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.ui.UIUtils;
import com.mycollab.vaadin.web.ui.OptionPopupContent;
import com.mycollab.vaadin.web.ui.ToggleButtonGroup;
import com.mycollab.vaadin.web.ui.WebUIConstants;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.event.dd.acceptcriteria.Not;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.dd.VerticalDropLocation;
import com.vaadin.ui.*;
import fi.jasoft.dragdroplayouts.DDVerticalLayout;
import fi.jasoft.dragdroplayouts.client.ui.LayoutDragMode;
import fi.jasoft.dragdroplayouts.events.LayoutBoundTransferable;
import fi.jasoft.dragdroplayouts.events.VerticalLocationIs;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.hene.popupbutton.PopupButton;
import org.vaadin.jouni.restrain.Restrain;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author MyCollab Ltd
 * @since 5.1.1
 */
@ViewComponent
public class BugKanbanViewImpl extends AbstractPageView implements BugKanbanView {
    private static Logger LOG = LoggerFactory.getLogger(BugKanbanViewImpl.class);

    private BugService bugService = AppContextUtil.getSpringBean(BugService.class);

    private BugSearchPanel searchPanel;
    private MHorizontalLayout kanbanLayout;
    private Map<String, KanbanBlock> kanbanBlocks;
    private com.vaadin.ui.ComponentContainer newBugComp = null;

    private ApplicationEventListener<BugEvent.SearchRequest> searchHandler = new
            ApplicationEventListener<BugEvent.SearchRequest>() {
                @Override
                @Subscribe
                public void handle(BugEvent.SearchRequest event) {
                    BugSearchCriteria criteria = (BugSearchCriteria) event.getData();
                    if (criteria != null) {
                        criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
                        criteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField("bugIndex", SearchCriteria.ASC)));
                        queryBug(criteria);
                    }
                }
            };

    public BugKanbanViewImpl() {
        this.setSizeFull();
        this.withSpacing(true).withMargin(new MarginInfo(false, true, true, true));

        searchPanel = new BugSearchPanel();
        MHorizontalLayout groupWrapLayout = new MHorizontalLayout();
        groupWrapLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

        searchPanel.addHeaderRight(groupWrapLayout);

        MButton advanceDisplayBtn = new MButton("List", clickEvent -> EventBusFactory.getInstance().post(new BugEvent.GotoList(this, null)))
                .withWidth("100px").withIcon(FontAwesome.SITEMAP);
        advanceDisplayBtn.setDescription("Detail");

        MButton kanbanBtn = new MButton("Kanban").withWidth("100px").withIcon(FontAwesome.TH);
        kanbanBtn.setDescription("Kanban View");

        ToggleButtonGroup viewButtons = new ToggleButtonGroup();
        viewButtons.addButton(advanceDisplayBtn);
        viewButtons.addButton(kanbanBtn);
        viewButtons.withDefaultButton(kanbanBtn);
        groupWrapLayout.addComponent(viewButtons);

        kanbanLayout = new MHorizontalLayout().withMargin(new MarginInfo(true, false, true, false)).withStyleName("kanban-layout")
                .withFullHeight();
        this.with(searchPanel, kanbanLayout).expand(kanbanLayout);
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

    @Override
    public HasSearchHandlers<BugSearchCriteria> getSearchHandlers() {
        return searchPanel;
    }

    private void setProjectNavigatorVisibility(boolean visibility) {
        ProjectView view = UIUtils.getRoot(this, ProjectView.class);
        if (view != null) {
            view.setNavigatorVisibility(visibility);
        }
    }

    @Override
    public void displayView() {
        searchPanel.selectQueryInfo(BugSavedFilterComboBox.ALL_BUGS);
    }

    @Override
    public void queryBug(final BugSearchCriteria searchCriteria) {
        kanbanLayout.removeAllComponents();
        kanbanBlocks = new ConcurrentHashMap<>();

        setProjectNavigatorVisibility(false);
        AsyncInvoker.access(new AsyncInvoker.PageCommand() {
            @Override
            public void run() {
                List<OptionVal> optionVals = new ArrayList<>();
                for (OptionI18nEnum.BugStatus bugStatus : OptionI18nEnum.bug_statuses) {
                    OptionVal option = new OptionVal();
                    option.setTypeval(bugStatus.name());
                    option.setType(ProjectTypeConstants.BUG);
                    optionVals.add(option);
                }
                for (OptionVal optionVal : optionVals) {
                    KanbanBlock kanbanBlock = new KanbanBlock(optionVal);
                    kanbanBlocks.put(optionVal.getTypeval(), kanbanBlock);
                    kanbanLayout.addComponent(kanbanBlock);
                }
                this.push();

                int totalBugs = bugService.getTotalCount(searchCriteria);
                searchPanel.setTotalCountNumber(totalBugs);
                int pages = totalBugs / 50;
                for (int page = 0; page < pages + 1; page++) {
                    List<SimpleBug> bugs = bugService.findPageableListByCriteria(new BasicSearchRequest<>
                            (searchCriteria, page + 1, 50));
                    if (CollectionUtils.isNotEmpty(bugs)) {
                        for (SimpleBug bug : bugs) {
                            String status = bug.getStatus();
                            KanbanBlock kanbanBlock = kanbanBlocks.get(status);
                            if (kanbanBlock == null) {
                                LOG.error("Can not find a kanban block for status: " + status);
                            } else {
                                kanbanBlock.addBlockItem(new KanbanBugBlockItem(bug));
                            }
                        }
                        this.push();
                    }
                }
            }
        });
    }

    private static class KanbanBugBlockItem extends CustomComponent {
        private SimpleBug bug;

        KanbanBugBlockItem(final SimpleBug bug) {
            this.bug = bug;
            MVerticalLayout root = new MVerticalLayout();
            root.addStyleName("kanban-item");
            this.setCompositionRoot(root);

            BugPopupFieldFactory popupFieldFactory = ViewManager.getCacheComponent(BugPopupFieldFactory.class);
            MHorizontalLayout headerLayout = new MHorizontalLayout();
            ToggleBugSummaryField bugLinkLbl = new ToggleBugSummaryField(bug, 70);

            if (bug.isCompleted()) {
                bugLinkLbl.addStyleName("completed");
                bugLinkLbl.removeStyleName("overdue pending");
            } else if (bug.isOverdue()) {
                bugLinkLbl.addStyleName("overdue");
                bugLinkLbl.removeStyleName("completed pending");
            }

            bugLinkLbl.addStyleName(UIConstants.LABEL_WORD_WRAP);
            AbstractComponent priorityField = popupFieldFactory.createPriorityPopupField(bug);
            headerLayout.with(priorityField, bugLinkLbl).expand(bugLinkLbl);

            root.addComponent(headerLayout);

            CssLayout footer = new CssLayout();

            // Build footer
            AbstractComponent commentField = popupFieldFactory.createCommentsPopupField(bug);
            footer.addComponent(commentField);

            AbstractComponent followerField = popupFieldFactory.createFollowersPopupField(bug);
            footer.addComponent(followerField);

            AbstractComponent deadlineField = popupFieldFactory.createDeadlinePopupField(bug);
            footer.addComponent(deadlineField);

            AbstractComponent startDateField = popupFieldFactory.createStartDatePopupField(bug);
            footer.addComponent(startDateField);

            AbstractComponent endDateField = popupFieldFactory.createEndDatePopupField(bug);
            footer.addComponent(endDateField);

            AbstractComponent assigneeField = popupFieldFactory.createAssigneePopupField(bug);
            footer.addComponent(assigneeField);

            root.addComponent(footer);
        }
    }

    private class KanbanBlock extends CustomComponent {
        private OptionVal optionVal;
        private MVerticalLayout root;
        private DDVerticalLayout dragLayoutContainer;
        private Label header;

        public KanbanBlock(OptionVal stage) {
            this.setHeight("100%");
            this.optionVal = stage;
            root = new MVerticalLayout();
            root.setWidth("300px");
            root.addStyleName("kanban-block");
            this.setCompositionRoot(root);

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
                    if (dragComponent instanceof KanbanBugBlockItem) {
                        KanbanBugBlockItem kanbanItem = (KanbanBugBlockItem) dragComponent;
                        int newIndex = details.getOverIndex();
                        if (details.getDropLocation() == VerticalDropLocation.BOTTOM) {
                            dragLayoutContainer.addComponent(kanbanItem);
                        } else if (newIndex == -1) {
                            dragLayoutContainer.addComponent(kanbanItem, 0);
                        } else {
                            dragLayoutContainer.addComponent(kanbanItem, newIndex);
                        }
                        SimpleBug bug = kanbanItem.bug;
                        bug.setStatus(optionVal.getTypeval());
                        BugService bugService = AppContextUtil.getSpringBean(BugService.class);
                        bugService.updateSelectiveWithSession(bug, AppContext.getUsername());
                        updateComponentCount();

                        Component sourceComponent = transferable.getSourceComponent();
                        KanbanBlock sourceKanban = UIUtils.getRoot(sourceComponent, KanbanBlock.class);
                        if (sourceKanban != null && sourceKanban != KanbanBlock.this) {
                            sourceKanban.updateComponentCount();
                        }

                        //Update bug index
                        List<Map<String, Integer>> indexMap = new ArrayList<>();
                        for (int i = 0; i < dragLayoutContainer.getComponentCount(); i++) {
                            Component subComponent = dragLayoutContainer.getComponent(i);
                            if (subComponent instanceof KanbanBugBlockItem) {
                                KanbanBugBlockItem blockItem = (KanbanBugBlockItem) dragLayoutContainer.getComponent(i);
                                Map<String, Integer> map = new HashMap<>(2);
                                map.put("id", blockItem.bug.getId());
                                map.put("index", i);
                                indexMap.add(map);
                            }
                        }
                        if (indexMap.size() > 0) {
                            bugService.massUpdateBugIndexes(indexMap, AppContext.getAccountId());
                        }
                    }
                }

                @Override
                public AcceptCriterion getAcceptCriterion() {
                    return new Not(VerticalLocationIs.MIDDLE);
                }
            });
            new Restrain(dragLayoutContainer).setMinHeight("50px").setMaxHeight((Page.getCurrent()
                    .getBrowserWindowHeight() - 450) + "px");

            HorizontalLayout headerLayout = new HorizontalLayout();
            headerLayout.setWidth("100%");
            header = new Label(AppContext.getMessage(OptionI18nEnum.BugStatus.class, optionVal.getTypeval()));
            header.addStyleName("header");
            headerLayout.addComponent(header);
            headerLayout.setComponentAlignment(header, Alignment.MIDDLE_LEFT);
            headerLayout.setExpandRatio(header, 1.0f);

            root.with(headerLayout, dragLayoutContainer);

            if (OptionI18nEnum.BugStatus.Open.name().equals(optionVal.getTypeval())) {
                OptionPopupContent popupContent = new OptionPopupContent();
                PopupButton controlsBtn = new PopupButton();
                controlsBtn.addStyleName(WebUIConstants.BUTTON_LINK);
                headerLayout.addComponent(controlsBtn);
                headerLayout.setComponentAlignment(controlsBtn, Alignment.MIDDLE_RIGHT);

                if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.BUGS)) {
                    MButton addBtn = new MButton(AppContext.getMessage(BugI18nEnum.NEW), clickEvent -> addNewBugComp())
                            .withIcon(FontAwesome.PLUS);
                    popupContent.addOption(addBtn);
                }
                controlsBtn.setContent(popupContent);

                if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.BUGS)) {
                    MButton addNewBtn = new MButton(AppContext.getMessage(BugI18nEnum.NEW), clickEvent -> addNewBugComp())
                            .withIcon(FontAwesome.PLUS).withStyleName(WebUIConstants.BUTTON_ACTION);
                    addNewBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.BUGS));
                    root.with(addNewBtn).withAlign(addNewBtn, Alignment.TOP_RIGHT);
                }
            }
        }

        void addBlockItem(KanbanBugBlockItem comp) {
            dragLayoutContainer.addComponent(comp);
            updateComponentCount();
        }

        private void updateComponentCount() {
            header.setValue(String.format("%s (%d)", AppContext.getMessage(OptionI18nEnum.BugStatus.class, optionVal.getTypeval()),
                    dragLayoutContainer.getComponentCount()));
        }

        void addNewBugComp() {
            Component testComp = (dragLayoutContainer.getComponentCount() > 0) ? dragLayoutContainer.getComponent(0) : null;
            if (testComp instanceof KanbanBugBlockItem || testComp == null) {
                final SimpleBug bug = new SimpleBug();
                bug.setSaccountid(AppContext.getAccountId());
                bug.setProjectid(CurrentProjectVariables.getProjectId());
                bug.setStatus(optionVal.getTypeval());
                bug.setProjectShortName(CurrentProjectVariables.getShortName());
                bug.setLogby(AppContext.getUsername());
                final MVerticalLayout layout = new MVerticalLayout();
                layout.addStyleName("kanban-item");
                final TextField bugNameField = new TextField();
                bugNameField.focus();
                bugNameField.setWidth("100%");
                layout.with(bugNameField);

                MButton saveBtn = new MButton(AppContext.getMessage(GenericI18Enum.BUTTON_ADD), clickEvent -> {
                    String summary = bugNameField.getValue();
                    if (StringUtils.isNotBlank(summary)) {
                        bug.setSummary(summary);
                        BugService bugService = AppContextUtil.getSpringBean(BugService.class);
                        bugService.saveWithSession(bug, AppContext.getUsername());
                        dragLayoutContainer.removeComponent(layout);
                        KanbanBugBlockItem kanbanBugBlockItem = new KanbanBugBlockItem(bug);
                        dragLayoutContainer.addComponent(kanbanBugBlockItem, 0);
                        updateComponentCount();
                    }
                }).withStyleName(WebUIConstants.BUTTON_ACTION);

                MButton cancelBtn = new MButton(AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL), clickEvent -> {
                    dragLayoutContainer.removeComponent(layout);
                    newBugComp = null;
                }).withStyleName(WebUIConstants.BUTTON_OPTION);

                MHorizontalLayout controlsBtn = new MHorizontalLayout(cancelBtn, saveBtn);
                layout.with(controlsBtn).withAlign(controlsBtn, Alignment.MIDDLE_RIGHT);
                if (newBugComp != null) {
                    if (newBugComp.getParent() != null) {
                        ((ComponentContainer) newBugComp.getParent()).removeComponent(newBugComp);
                    }
                }
                newBugComp = layout;
                dragLayoutContainer.addComponent(layout, 0);
                dragLayoutContainer.markAsDirty();
            }
        }
    }
}
