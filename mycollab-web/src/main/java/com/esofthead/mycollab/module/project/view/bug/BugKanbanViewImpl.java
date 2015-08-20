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
package com.esofthead.mycollab.module.project.view.bug;

import com.esofthead.mycollab.common.UrlEncodeDecoder;
import com.esofthead.mycollab.common.domain.OptionVal;
import com.esofthead.mycollab.common.domain.SaveSearchResultWithBLOBs;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
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
import com.esofthead.mycollab.module.project.events.BugEvent;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum;
import com.esofthead.mycollab.module.project.view.ProjectView;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.esofthead.mycollab.module.tracker.service.BugService;
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
import com.vaadin.shared.ui.dd.VerticalDropLocation;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.ComponentContainer;
import fi.jasoft.dragdroplayouts.DDVerticalLayout;
import fi.jasoft.dragdroplayouts.client.ui.LayoutDragMode;
import fi.jasoft.dragdroplayouts.events.LayoutBoundTransferable;
import fi.jasoft.dragdroplayouts.events.VerticalLocationIs;
import org.apache.commons.collections.CollectionUtils;
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
public class BugKanbanViewImpl extends AbstractPageView implements BugKanbanView {
    private static Logger LOG = LoggerFactory.getLogger(BugKanbanViewImpl.class);

    private BugService bugService = ApplicationContextUtil.getSpringBean(BugService.class);

    private boolean projectNavigatorVisibility = false;

    private HorizontalLayout kanbanLayout;
    private Map<String, KanbanBlock> kanbanBlocks;
    private Button toogleMenuShowBtn;
    private com.vaadin.ui.ComponentContainer newBugComp = null;

    private ApplicationEventListener<BugEvent.SearchRequest> searchHandler = new
            ApplicationEventListener<BugEvent.SearchRequest>() {
                @Override
                @Subscribe
                public void handle(BugEvent.SearchRequest event) {
                    BugSearchCriteria criteria = (BugSearchCriteria) event.getData();
                    if (criteria != null) {
                        criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
                        criteria.setOrderFields(Arrays.asList(new SearchCriteria.OrderField("bugIndex", SearchCriteria.ASC)));
                        queryBug(criteria);
                    }
                }
            };

    public BugKanbanViewImpl() {
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

        final SavedFilterComboBox savedFilterComboBox = new SavedFilterComboBox(ProjectTypeConstants.BUG);
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
                    BugSearchCriteria criteria = SearchFieldInfo.buildSearchCriteria(BugSearchCriteria.class, fieldInfos);
                    criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
                    EventBusFactory.getInstance().post(new BugEvent.SearchRequest(BugKanbanViewImpl.this, criteria));
                } else {
                    display();
                }
            }
        });

        Button cancelBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                EventBusFactory.getInstance().post(new ShellEvent.GotoProjectModule(this, new String[]{
                        "bug", "dashboard", UrlEncodeDecoder.encode(CurrentProjectVariables.getProjectId())}));
            }
        });
        cancelBtn.setStyleName(UIConstants.THEME_GRAY_LINK);

        header.with(headerWrapper, savedFilterComboBox, toogleMenuShowBtn, cancelBtn).withAlign(headerWrapper, Alignment.MIDDLE_LEFT)
                .withAlign(toogleMenuShowBtn, Alignment.MIDDLE_RIGHT).withAlign(cancelBtn, Alignment.MIDDLE_RIGHT).expand(headerWrapper);

        kanbanLayout = new HorizontalLayout();
        kanbanLayout.setHeight("100%");
        kanbanLayout.addStyleName("kanban-layout");
        kanbanLayout.setSpacing(true);
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
        BugSearchCriteria searchCriteria = new BugSearchCriteria();
        searchCriteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
        searchCriteria.setOrderFields(Arrays.asList(new SearchCriteria.OrderField("bugIndex", SearchCriteria.ASC)));
        queryBug(searchCriteria);
    }

    private void queryBug(final BugSearchCriteria searchCriteria) {
        kanbanLayout.removeAllComponents();
        kanbanBlocks = new ConcurrentHashMap<>();

        toogleMenuShowBtn.setCaption("Show menu");
        setProjectNavigatorVisibility(false);
        UI.getCurrent().access(new Runnable() {
            @Override
            public void run() {
                List<OptionVal> optionVals = new ArrayList();
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
                UI.getCurrent().push();

                int totalTasks = bugService.getTotalCount(searchCriteria);
                int pages = totalTasks / 20;
                for (int page = 0; page < pages + 1; page++) {
                    List<SimpleBug> bugs = bugService.findPagableListByCriteria(new SearchRequest<>(searchCriteria, page + 1, 20));

                    for (SimpleBug bug : bugs) {
                        String status = bug.getStatus();
                        KanbanBlock kanbanBlock = kanbanBlocks.get(status);
                        if (kanbanBlock == null) {
                            LOG.error("Can not find a kanban block for status: " + status);
                        } else {
                            kanbanBlock.addBlockItem(new KanbanBugBlockItem(bug));
                        }
                    }
                    UI.getCurrent().push();
                }

            }
        });
    }

    private class KanbanBugBlockItem extends CustomComponent {
        private SimpleBug bug;

        KanbanBugBlockItem(final SimpleBug bug) {
            this.bug = bug;
            MVerticalLayout root = new MVerticalLayout();
            root.addStyleName("kanban-item");
            this.setCompositionRoot(root);

            String bugName = String.format("[%s-%s] %s", bug.getProjectShortName(), bug.getBugkey(), bug.getSummary());
            ButtonLink bugBtn = new ButtonLink(bugName, new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    EventBusFactory.getInstance().post(new BugEvent.GotoRead(KanbanBugBlockItem.this, bug.getId()));
                }
            });
            bugBtn.setIcon(new ExternalResource(ProjectResources.getIconResourceLink12ByBugPriority(bug.getPriority())));
            bugBtn.setDescription(ProjectTooltipGenerator.generateToolTipBug(AppContext.getUserLocale(), bug,
                    AppContext.getSiteUrl(), AppContext.getTimezone()));
            root.with(bugBtn);

            Div footerDiv = new Div().setStyle("display:flex").setCSSClass("footer2");

            // Build footer
            if (bug.getNumComments() != null && bug.getNumComments() > 0) {
                Div comment = new Div().appendText(FontAwesome.COMMENT_O.getHtml() + " " + bug.getNumComments()).setTitle("Comment");
                footerDiv.appendChild(comment).appendChild(DivLessFormatter.EMPTY_SPACE());
            }

            if (bug.getDueDateRoundPlusOne() != null) {
                String deadline = String.format("%s: %s", AppContext.getMessage(BugI18nEnum.FORM_DUE_DATE),
                        AppContext.formatDate(bug.getDueDateRoundPlusOne()));
                Div deadlineDiv = new Div().appendText(FontAwesome.CLOCK_O.getHtml() + " " + AppContext.formatPrettyTime(bug
                        .getDueDateRoundPlusOne())).setTitle(deadline);
                footerDiv.appendChild(deadlineDiv).appendChild(DivLessFormatter.EMPTY_SPACE());
            }

            if (bug.getAssignuser() != null) {
                Img userAvatar = new Img("", Storage.getAvatarPath(bug.getAssignUserAvatarId(), 16))
                        .setTitle(bug.getAssignuserFullName());
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
                        BugService bugService = ApplicationContextUtil.getSpringBean(BugService.class);
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
                    .getBrowserWindowHeight() - 410) + "px");

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
            Button addBtn = new Button("Add a bug", new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    addNewBugComp();
                }
            });
            addBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.BUGS));
            popupContent.addOption(addBtn);
            controlsBtn.setContent(popupContent);

            Button addNewBtn = new Button("Add a bug", new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    addNewBugComp();
                }
            });
            addNewBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.BUGS));
            addNewBtn.addStyleName(UIConstants.BUTTON_SMALL_PADDING);
            addNewBtn.addStyleName(UIConstants.THEME_GREEN_LINK);
            root.with(headerLayout, dragLayoutContainer, addNewBtn);
        }

        void addBlockItem(KanbanBugBlockItem comp) {
            dragLayoutContainer.addComponent(comp);
            updateComponentCount();
        }

        private void updateComponentCount() {
            header.setValue(String.format("%s (%d)", optionVal.getTypeval(), dragLayoutContainer.getComponentCount()));
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
                MHorizontalLayout controlsBtn = new MHorizontalLayout();
                Button saveBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_ADD), new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent clickEvent) {
                        String summary = bugNameField.getValue();
                        if (StringUtils.isNotBlank(summary)) {
                            bug.setSummary(summary);
                            BugService bugService = ApplicationContextUtil.getSpringBean(BugService.class);
                            bugService.saveWithSession(bug, AppContext.getUsername());
                            dragLayoutContainer.removeComponent(layout);
                            KanbanBugBlockItem kanbanBugBlockItem = new KanbanBugBlockItem(bug);
                            dragLayoutContainer.addComponent(kanbanBugBlockItem, 0);
                            updateComponentCount();
                        }
                    }
                });
                saveBtn.addStyleName(UIConstants.THEME_GREEN_LINK);

                Button cancelBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL), new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent clickEvent) {
                        dragLayoutContainer.removeComponent(layout);
                        newBugComp = null;
                    }
                });
                cancelBtn.addStyleName(UIConstants.THEME_GRAY_LINK);
                controlsBtn.with(saveBtn, cancelBtn);
                layout.with(controlsBtn).withAlign(controlsBtn, Alignment.MIDDLE_RIGHT);
                if (newBugComp != null) {
                    ((ComponentContainer) newBugComp.getParent()).removeComponent(newBugComp);
                }
                newBugComp = layout;
                dragLayoutContainer.addComponent(layout, 0);
                dragLayoutContainer.markAsDirty();
            }
        }
    }
}
