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
package com.esofthead.mycollab.module.project.view.task;

import com.esofthead.mycollab.common.UrlEncodeDecoder;
import com.esofthead.mycollab.common.domain.OptionVal;
import com.esofthead.mycollab.common.domain.SaveSearchResultWithBLOBs;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.service.OptionValService;
import com.esofthead.mycollab.configuration.Storage;
import com.esofthead.mycollab.core.UserInvalidInputException;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.db.query.SearchFieldInfo;
import com.esofthead.mycollab.core.utils.XStreamJsonDeSerializer;
import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.html.DivLessFormatter;
import com.esofthead.mycollab.module.project.*;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.esofthead.mycollab.module.project.events.TaskEvent;
import com.esofthead.mycollab.module.project.i18n.TaskI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectTaskService;
import com.esofthead.mycollab.module.project.view.ProjectView;
import com.esofthead.mycollab.module.project.view.kanban.AddNewColumnWindow;
import com.esofthead.mycollab.shell.events.ShellEvent;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.*;
import com.google.common.eventbus.Subscribe;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.vaadin.data.Property;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.event.dd.acceptcriteria.Not;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.dd.HorizontalDropLocation;
import com.vaadin.shared.ui.dd.VerticalDropLocation;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import fi.jasoft.dragdroplayouts.DDHorizontalLayout;
import fi.jasoft.dragdroplayouts.DDVerticalLayout;
import fi.jasoft.dragdroplayouts.client.ui.LayoutDragMode;
import fi.jasoft.dragdroplayouts.events.LayoutBoundTransferable;
import fi.jasoft.dragdroplayouts.events.VerticalLocationIs;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.hene.popupbutton.PopupButton;
import org.vaadin.jouni.restrain.Restrain;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author MyCollab Ltd
 * @since 5.1.1
 */
@ViewComponent
public class TaskKanbanviewImpl extends AbstractPageView implements TaskKanbanview {
    private static Logger LOG = LoggerFactory.getLogger(TaskKanbanviewImpl.class);

    private ProjectTaskService taskService = ApplicationContextUtil.getSpringBean(ProjectTaskService.class);
    private OptionValService optionValService = ApplicationContextUtil.getSpringBean(OptionValService.class);

    private boolean projectNavigatorVisibility = false;

    private DDHorizontalLayout kanbanLayout;
    private Map<String, KanbanBlock> kanbanBlocks;
    private Button toogleMenuShowBtn;
    private ComponentContainer newTaskComp = null;

    private ApplicationEventListener<TaskEvent.SearchRequest> searchHandler = new
            ApplicationEventListener<TaskEvent.SearchRequest>() {
                @Override
                @Subscribe
                public void handle(TaskEvent.SearchRequest event) {
                    TaskSearchCriteria criteria = (TaskSearchCriteria) event.getData();
                    if (criteria != null) {
                        criteria.setProjectid(new NumberSearchField(CurrentProjectVariables.getProjectId()));
                        criteria.setOrderFields(Arrays.asList(new SearchCriteria.OrderField("taskindex", SearchCriteria.ASC)));
                        queryTask(criteria);
                    }
                }
            };

    public TaskKanbanviewImpl() {
        this.setSizeFull();
        this.withMargin(new MarginInfo(false, true, true, true));

        MHorizontalLayout header = new MHorizontalLayout().withMargin(new MarginInfo(true, false, true, false))
                .withStyleName("hdr-view").withWidth("100%");
        header.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        Label headerText = new Label("Kanban Board", ContentMode.HTML);
        headerText.setStyleName(UIConstants.HEADER_TEXT);
        CssLayout headerWrapper = new CssLayout();
        headerWrapper.addComponent(headerText);

        toogleMenuShowBtn = new Button("", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                projectNavigatorVisibility = !projectNavigatorVisibility;
                setProjectNavigatorVisibility(projectNavigatorVisibility);
                if (projectNavigatorVisibility) {
                    toogleMenuShowBtn.setCaption("Hide menu");
                } else {
                    toogleMenuShowBtn.setCaption("Show menu");
                }
            }
        });
        toogleMenuShowBtn.addStyleName(UIConstants.THEME_LINK);

        final SavedFilterComboBox savedFilterComboBox = new SavedFilterComboBox(ProjectTypeConstants.TASK);
        savedFilterComboBox.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                SaveSearchResultWithBLOBs item = (SaveSearchResultWithBLOBs) savedFilterComboBox.getValue();
                if (item != null) {
                    List<SearchFieldInfo> fieldInfos = (List<SearchFieldInfo>) XStreamJsonDeSerializer.fromJson(item
                            .getQuerytext());
                    // @HACK: === the library serialize with extra list
                    // wrapper
                    if (CollectionUtils.isEmpty(fieldInfos)) {
                        throw new UserInvalidInputException("There is no field in search criterion");
                    }
                    fieldInfos = (List<SearchFieldInfo>) fieldInfos.get(0);
                    TaskSearchCriteria criteria = SearchFieldInfo.buildSearchCriteria(TaskSearchCriteria.class,
                            fieldInfos);
                    criteria.setProjectid(new NumberSearchField(CurrentProjectVariables.getProjectId()));
                    EventBusFactory.getInstance().post(new TaskEvent.SearchRequest(TaskKanbanviewImpl.this, criteria));
                } else {
                    display();
                }
            }
        });

        Button addNewColumnBtn = new Button("Add a new column", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                UI.getCurrent().addWindow(new AddNewColumnWindow(TaskKanbanviewImpl.this, ProjectTypeConstants.TASK));
            }
        });
        addNewColumnBtn.setStyleName(UIConstants.THEME_GREEN_LINK);

        Button cancelBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                EventBusFactory.getInstance().post(new ShellEvent.GotoProjectModule(this, new String[]{
                        "task", "dashboard", UrlEncodeDecoder.encode(CurrentProjectVariables.getProjectId())}));
            }
        });
        cancelBtn.setStyleName(UIConstants.THEME_GRAY_LINK);

        header.with(headerWrapper, savedFilterComboBox, toogleMenuShowBtn, addNewColumnBtn, cancelBtn)
                .withAlign(headerWrapper, Alignment.MIDDLE_LEFT)
                .withAlign(toogleMenuShowBtn, Alignment.MIDDLE_RIGHT).withAlign(cancelBtn, Alignment.MIDDLE_RIGHT)
                .withAlign(addNewColumnBtn, Alignment.MIDDLE_RIGHT).expand(headerWrapper);

        kanbanLayout = new DDHorizontalLayout();
        kanbanLayout.setHeight("100%");
        kanbanLayout.addStyleName("kanban-layout");
        kanbanLayout.setSpacing(true);
        kanbanLayout.setComponentHorizontalDropRatio(0.3f);
        kanbanLayout.setDragMode(LayoutDragMode.CLONE_OTHER);

//      Enable dropping components
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

                    //Update options index for this project
                    List<Map<String, Integer>> indexMap = new ArrayList<>();
                    for (int i = 0; i < kanbanLayout.getComponentCount(); i++) {
                        KanbanBlock blockItem = (KanbanBlock) kanbanLayout.getComponent(i);
                        Map<String, Integer> map = new HashedMap(2);
                        map.put("id", blockItem.optionVal.getId());
                        map.put("index", i);
                        indexMap.add(map);
                    }
                    if (indexMap.size() > 0) {
                        optionValService.massUpdateOptionIndexes(indexMap, AppContext.getAccountId());
                    }
                }
            }

            @Override
            public AcceptCriterion getAcceptCriterion() {
                return new Not(VerticalLocationIs.MIDDLE);
            }
        });
        this.with(header, kanbanLayout).expand(kanbanLayout);
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
        TaskSearchCriteria searchCriteria = new TaskSearchCriteria();
        searchCriteria.setProjectid(new NumberSearchField(CurrentProjectVariables.getProjectId()));
        searchCriteria.setOrderFields(Arrays.asList(new SearchCriteria.OrderField("taskindex", SearchCriteria.ASC)));
        queryTask(searchCriteria);
    }

    private void queryTask(final TaskSearchCriteria searchCriteria) {
        kanbanLayout.removeAllComponents();
        kanbanBlocks = new ConcurrentHashMap<>();

        toogleMenuShowBtn.setCaption("Show menu");
        setProjectNavigatorVisibility(false);
        UI.getCurrent().access(new Runnable() {
            @Override
            public void run() {
                List<OptionVal> optionVals = optionValService.findOptionVals(ProjectTypeConstants.TASK,
                        CurrentProjectVariables.getProjectId(), AppContext.getAccountId());
                for (OptionVal optionVal : optionVals) {
                    KanbanBlock kanbanBlock = new KanbanBlock(optionVal);
                    kanbanBlocks.put(optionVal.getTypeval(), kanbanBlock);
                    kanbanLayout.addComponent(kanbanBlock);
                }
                UI.getCurrent().push();

                int totalTasks = taskService.getTotalCount(searchCriteria);
                int pages = totalTasks / 20;
                for (int page = 0; page < pages + 1; page++) {
                    List<SimpleTask> tasks = taskService.findPagableListByCriteria(new SearchRequest<>(searchCriteria, page + 1, 20));

                    for (SimpleTask task : tasks) {
                        String status = task.getStatus();
                        KanbanBlock kanbanBlock = kanbanBlocks.get(status);
                        if (kanbanBlock == null) {
                            LOG.error("Can not find a kanban block for status: " + status);
                        } else {
                            kanbanBlock.addBlockItem(new KanbanTaskBlockItem(task));
                        }
                    }
                    UI.getCurrent().push();
                }

            }
        });
    }

    @Override
    public void addColumn(final OptionVal option) {
        UI.getCurrent().access(new Runnable() {
            @Override
            public void run() {
                KanbanBlock kanbanBlock = new KanbanBlock(option);
                kanbanBlocks.put(option.getTypeval(), kanbanBlock);
                kanbanLayout.addComponent(kanbanBlock);
                UI.getCurrent().push();
            }
        });
    }

    private class KanbanTaskBlockItem extends CustomComponent {
        private SimpleTask task;

        KanbanTaskBlockItem(final SimpleTask task) {
            this.task = task;
            MVerticalLayout root = new MVerticalLayout();
            root.addStyleName("kanban-item");
            this.setCompositionRoot(root);

            String taskname = String.format("[%s-%s] %s", task.getProjectShortname(), task.getTaskkey(), task.getTaskname());
            ButtonLink taskBtn = new ButtonLink(taskname, new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    EventBusFactory.getInstance().post(new TaskEvent.GotoRead(KanbanTaskBlockItem.this, task.getId()));
                }
            });
            taskBtn.setIcon(new ExternalResource(ProjectResources.getIconResourceLink12ByTaskPriority(task.getPriority())));
            taskBtn.setDescription(ProjectTooltipGenerator.generateToolTipTask(AppContext.getUserLocale(), task,
                    AppContext.getSiteUrl(), AppContext.getTimezone()));
            root.with(taskBtn);

            Div footerDiv = new Div().setStyle("display:flex").setCSSClass("footer2");

            // Build footer
            if (task.getNumComments() != null && task.getNumComments() > 0) {
                Div comment = new Div().appendText(FontAwesome.COMMENT_O.getHtml() + " " + task.getNumComments()).setTitle("Comment");
                footerDiv.appendChild(comment).appendChild(DivLessFormatter.EMPTY_SPACE());
            }

            if (task.getDeadlineRoundPlusOne() != null) {
                String deadline = String.format("%s: %s", AppContext.getMessage(TaskI18nEnum.FORM_DEADLINE),
                        AppContext.formatDate(task.getDeadlineRoundPlusOne()));
                Div deadlineDiv = new Div().appendText(FontAwesome.CLOCK_O.getHtml() + " " + AppContext.formatPrettyTime(task
                        .getDeadlineRoundPlusOne())).setTitle(deadline);
                footerDiv.appendChild(deadlineDiv).appendChild(DivLessFormatter.EMPTY_SPACE());
            }

            if (task.getAssignuser() != null) {
                Img userAvatar = new Img("", Storage.getAvatarPath(task.getAssignUserAvatarId(), 16))
                        .setTitle(task.getAssignUserFullName());
                footerDiv.appendChild(userAvatar);
            }

            if (footerDiv.getChildren().size() > 0) {
                Label footer = new Label(footerDiv.write(), ContentMode.HTML);
                root.addComponent(footer);
            }
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
                    if (dragComponent instanceof KanbanTaskBlockItem) {
                        KanbanTaskBlockItem kanbanItem = (KanbanTaskBlockItem) dragComponent;
                        int newIndex = details.getOverIndex();
                        if (details.getDropLocation() == VerticalDropLocation.BOTTOM) {
                            dragLayoutContainer.addComponent(kanbanItem);
                        } else if (newIndex == -1) {
                            dragLayoutContainer.addComponent(kanbanItem, 0);
                        } else {
                            dragLayoutContainer.addComponent(kanbanItem, newIndex);
                        }
                        SimpleTask task = kanbanItem.task;
                        task.setStatus(optionVal.getTypeval());
                        ProjectTaskService taskService = ApplicationContextUtil.getSpringBean(ProjectTaskService.class);
                        taskService.updateSelectiveWithSession(task, AppContext.getUsername());
                        updateComponentCount();

                        Component sourceComponent = transferable.getSourceComponent();
                        KanbanBlock sourceKanban = UIUtils.getRoot(sourceComponent, KanbanBlock.class);
                        if (sourceKanban != null && sourceKanban != KanbanBlock.this) {
                            sourceKanban.updateComponentCount();
                        }

                        //Update task index
                        List<Map<String, Integer>> indexMap = new ArrayList<>();
                        for (int i = 0; i < dragLayoutContainer.getComponentCount(); i++) {
                            Component subComponent = dragLayoutContainer.getComponent(i);
                            if (subComponent instanceof KanbanTaskBlockItem) {
                                KanbanTaskBlockItem blockItem = (KanbanTaskBlockItem) dragLayoutContainer.getComponent(i);
                                Map<String, Integer> map = new HashMap<>(2);
                                map.put("id", blockItem.task.getId());
                                map.put("index", i);
                                indexMap.add(map);
                            }
                        }
                        if (indexMap.size() > 0) {
                            taskService.massUpdateTaskIndexes(indexMap, AppContext.getAccountId());
                        }
                    }
                }

                @Override
                public AcceptCriterion getAcceptCriterion() {
                    return new Not(VerticalLocationIs.MIDDLE);
                }
            });
            new Restrain(dragLayoutContainer).setMinHeight("50px").setMaxHeight((Page.getCurrent()
                    .getBrowserWindowHeight() - 265) + "px");

            HorizontalLayout headerLayout = new HorizontalLayout();
            headerLayout.setWidth("100%");
            header = new Label(optionVal.getTypeval());
            header.addStyleName("header");
            headerLayout.addComponent(header);
            headerLayout.setComponentAlignment(header, Alignment.MIDDLE_LEFT);
            headerLayout.setExpandRatio(header, 1.0f);

            PopupButton controlsBtn = new PopupButton();
            controlsBtn.addStyleName(UIConstants.THEME_LINK);
            headerLayout.addComponent(controlsBtn);
            headerLayout.setComponentAlignment(controlsBtn, Alignment.MIDDLE_RIGHT);

            OptionPopupContent popupContent = new OptionPopupContent();
            Button addBtn = new Button("Add a task", new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    addNewTaskComp();
                }
            });
            addBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS));
            popupContent.addOption(addBtn);
            controlsBtn.setContent(popupContent);

            Button addNewBtn = new Button("Add a task", new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    addNewTaskComp();
                }
            });
            addNewBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS));
            addNewBtn.addStyleName(UIConstants.BUTTON_SMALL_PADDING);
            addNewBtn.addStyleName(UIConstants.THEME_GREEN_LINK);
            root.with(headerLayout, dragLayoutContainer, addNewBtn);
        }

        void addBlockItem(KanbanTaskBlockItem comp) {
            dragLayoutContainer.addComponent(comp);
            updateComponentCount();
        }

        private void updateComponentCount() {
            header.setValue(String.format("%s (%d)", optionVal.getTypeval(), dragLayoutContainer.getComponentCount()));
        }

        void addNewTaskComp() {
            Component testComp = (dragLayoutContainer.getComponentCount() > 0) ? dragLayoutContainer.getComponent(0) : null;
            if (testComp instanceof KanbanTaskBlockItem || testComp == null) {
                final SimpleTask task = new SimpleTask();
                task.setSaccountid(AppContext.getAccountId());
                task.setProjectid(CurrentProjectVariables.getProjectId());
                task.setPercentagecomplete(0d);
                task.setStatus(optionVal.getTypeval());
                task.setProjectShortname(CurrentProjectVariables.getShortName());
                final MVerticalLayout layout = new MVerticalLayout();
                layout.addStyleName("kanban-item");
                final TextField taskNameField = new TextField();
                taskNameField.focus();
                taskNameField.setWidth("100%");
                layout.with(taskNameField);
                MHorizontalLayout controlsBtn = new MHorizontalLayout();
                Button saveBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_ADD), new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent clickEvent) {
                        String taskName = taskNameField.getValue();
                        if (StringUtils.isNotBlank(taskName)) {
                            task.setTaskname(taskName);
                            ProjectTaskService taskService = ApplicationContextUtil.getSpringBean(ProjectTaskService.class);
                            taskService.saveWithSession(task, AppContext.getUsername());
                            dragLayoutContainer.removeComponent(layout);
                            KanbanTaskBlockItem kanbanTaskBlockItem = new KanbanTaskBlockItem(task);
                            dragLayoutContainer.addComponent(kanbanTaskBlockItem, 0);
                            updateComponentCount();
                        }
                    }
                });
                saveBtn.addStyleName(UIConstants.THEME_GREEN_LINK);

                Button cancelBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL), new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent clickEvent) {
                        dragLayoutContainer.removeComponent(layout);
                        newTaskComp = null;
                    }
                });
                cancelBtn.addStyleName(UIConstants.THEME_GRAY_LINK);
                controlsBtn.with(saveBtn, cancelBtn);
                layout.with(controlsBtn).withAlign(controlsBtn, Alignment.MIDDLE_RIGHT);
                if (newTaskComp != null) {
                    ((ComponentContainer) newTaskComp.getParent()).removeComponent(newTaskComp);
                }
                newTaskComp = layout;
                dragLayoutContainer.addComponent(layout, 0);
                dragLayoutContainer.markAsDirty();
            }
        }
    }
}
