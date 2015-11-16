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

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.ValuedBean;
import com.esofthead.mycollab.core.utils.BeanUtility;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.html.DivLessFormatter;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.events.BugComponentEvent;
import com.esofthead.mycollab.module.project.events.BugEvent;
import com.esofthead.mycollab.module.project.events.BugVersionEvent;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugResolution;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugSeverity;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugStatus;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.module.project.ui.components.*;
import com.esofthead.mycollab.module.project.ui.form.ProjectFormAttachmentDisplayField;
import com.esofthead.mycollab.module.project.ui.form.ProjectItemViewField;
import com.esofthead.mycollab.module.project.ui.format.BugFieldFormatter;
import com.esofthead.mycollab.module.project.view.bug.components.LinkIssueWindow;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectUserFormLinkField;
import com.esofthead.mycollab.module.tracker.dao.RelatedBugMapper;
import com.esofthead.mycollab.module.tracker.domain.*;
import com.esofthead.mycollab.module.tracker.domain.Component;
import com.esofthead.mycollab.module.tracker.service.BugRelationService;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.schedule.email.project.BugRelayEmailNotificationAction;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.utils.TooltipHelper;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.mvp.ViewManager;
import com.esofthead.mycollab.vaadin.ui.*;
import com.esofthead.mycollab.vaadin.ui.form.field.*;
import com.esofthead.mycollab.vaadin.ui.grid.GridFormLayoutHelper;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.peter.buttongroup.ButtonGroup;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.List;
import java.util.UUID;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class BugReadViewImpl extends AbstractPreviewItemComp<SimpleBug> implements BugReadView, IBugCallbackStatusComp {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(BugReadViewImpl.class);

    private TagViewComponent tagViewComponent;
    private CssLayout bugWorkflowControl;
    private ProjectFollowersComp<SimpleBug> bugFollowersList;
    private BugTimeLogSheet bugTimeLogList;
    private DateInfoComp dateInfoComp;
    private PeopleInfoComp peopleInfoComp;
    private ProjectActivityComponent activityComponent;

    public BugReadViewImpl() {
        super(AppContext.getMessage(BugI18nEnum.VIEW_READ_TITLE),
                ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG), new BugPreviewFormLayout());
    }

    private void displayWorkflowControl() {
        if (BugStatus.Open.name().equals(beanItem.getStatus()) || BugStatus.ReOpened.name().equals(beanItem.getStatus())) {
            bugWorkflowControl.removeAllComponents();
            ButtonGroup navButton = new ButtonGroup();
            Button startProgressBtn = new Button(AppContext.getMessage(BugI18nEnum.BUTTON_START_PROGRESS), new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final ClickEvent event) {
                    beanItem.setStatus(BugStatus.InProgress.name());
                    BugService bugService = ApplicationContextUtil.getSpringBean(BugService.class);
                    bugService.updateSelectiveWithSession(beanItem, AppContext.getUsername());
                    displayWorkflowControl();
                }
            });
            startProgressBtn.addStyleName(UIConstants.BUTTON_ACTION);
            navButton.addButton(startProgressBtn);

            Button resolveBtn = new Button(AppContext.getMessage(BugI18nEnum.BUTTON_RESOLVED), new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final ClickEvent event) {
                    UI.getCurrent().addWindow(new ResolvedInputWindow(BugReadViewImpl.this, beanItem));
                }
            });
            resolveBtn.addStyleName(UIConstants.BUTTON_ACTION);
            navButton.addButton(resolveBtn);

            Button wontFixBtn = new Button(AppContext.getMessage(BugI18nEnum.BUTTON_WONTFIX), new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final ClickEvent event) {
                    UI.getCurrent().addWindow(new WontFixExplainWindow(BugReadViewImpl.this, beanItem));
                }
            });
            wontFixBtn.addStyleName(UIConstants.BUTTON_ACTION);
            navButton.addButton(wontFixBtn);
            bugWorkflowControl.addComponent(navButton);
        } else if (BugStatus.InProgress.name().equals(beanItem.getStatus())) {
            bugWorkflowControl.removeAllComponents();
            ButtonGroup navButton = new ButtonGroup();
            Button stopProgressBtn = new Button(AppContext.getMessage(BugI18nEnum.BUTTON_STOP_PROGRESS), new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final ClickEvent event) {
                    beanItem.setStatus(BugStatus.Open.name());
                    BugService bugService = ApplicationContextUtil.getSpringBean(BugService.class);
                    bugService.updateSelectiveWithSession(beanItem, AppContext.getUsername());
                    displayWorkflowControl();
                }
            });
            stopProgressBtn.addStyleName(UIConstants.BUTTON_ACTION);
            navButton.addButton(stopProgressBtn);

            Button resolveBtn = new Button(AppContext.getMessage(BugI18nEnum.BUTTON_RESOLVED), new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final ClickEvent event) {
                    UI.getCurrent().addWindow(new ResolvedInputWindow(BugReadViewImpl.this, beanItem));
                }
            });
            resolveBtn.addStyleName(UIConstants.BUTTON_ACTION);
            navButton.addButton(resolveBtn);
            bugWorkflowControl.addComponent(navButton);
        } else if (BugStatus.Verified.name().equals(beanItem.getStatus())) {
            bugWorkflowControl.removeAllComponents();
            ButtonGroup navButton = new ButtonGroup();
            Button reopenBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_REOPEN), new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final ClickEvent event) {
                    UI.getCurrent().addWindow(new ReOpenWindow(BugReadViewImpl.this, beanItem));
                }
            });
            reopenBtn.addStyleName(UIConstants.BUTTON_ACTION);
            navButton.addButton(reopenBtn);

            bugWorkflowControl.addComponent(navButton);
        } else if (BugStatus.Resolved.name().equals(beanItem.getStatus())) {
            bugWorkflowControl.removeAllComponents();
            ButtonGroup navButton = new ButtonGroup();
            Button reopenBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_REOPEN), new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final ClickEvent event) {
                    UI.getCurrent().addWindow(new ReOpenWindow(BugReadViewImpl.this, beanItem));
                }
            });
            reopenBtn.addStyleName(UIConstants.BUTTON_ACTION);
            navButton.addButton(reopenBtn);

            Button approveNCloseBtn = new Button(AppContext.getMessage(BugI18nEnum.BUTTON_APPROVE_CLOSE), new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final ClickEvent event) {
                    UI.getCurrent().addWindow(new ApproveInputWindow(BugReadViewImpl.this, beanItem));
                }
            });
            approveNCloseBtn.addStyleName(UIConstants.BUTTON_ACTION);
            navButton.addButton(approveNCloseBtn);
            bugWorkflowControl.addComponent(navButton);
        } else if (BugStatus.Resolved.name().equals(beanItem.getStatus())) {
            bugWorkflowControl.removeAllComponents();
            ButtonGroup navButton = new ButtonGroup();
            Button reopenBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_REOPEN), new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final ClickEvent event) {
                    UI.getCurrent().addWindow(new ReOpenWindow(BugReadViewImpl.this, beanItem));
                }
            });
            reopenBtn.setStyleName(UIConstants.BUTTON_ACTION);
            navButton.addButton(reopenBtn);

            bugWorkflowControl.addComponent(navButton);
        }
        bugWorkflowControl.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.BUGS));
    }

    @Override
    public SimpleBug getItem() {
        return beanItem;
    }

    @Override
    public void previewItem(SimpleBug item) {
        super.previewItem(item);
        displayWorkflowControl();
        ((BugPreviewFormLayout) previewLayout).displayBugHeader(beanItem);
    }

    @Override
    public void refreshBugItem() {
        EventBusFactory.getInstance().post(new BugEvent.GotoRead(BugReadViewImpl.this, beanItem.getId()));
    }

    @Override
    protected void initRelatedComponents() {
        activityComponent = new ProjectActivityComponent(ProjectTypeConstants.BUG, CurrentProjectVariables
                .getProjectId(), BugFieldFormatter.instance(), BugRelayEmailNotificationAction.class);
        dateInfoComp = new DateInfoComp();
        peopleInfoComp = new PeopleInfoComp();
        bugFollowersList = new ProjectFollowersComp<>(ProjectTypeConstants.BUG, ProjectRolePermissionCollections.BUGS);
        bugTimeLogList = ViewManager.getCacheComponent(BugTimeLogSheet.class);
        addToSideBar(dateInfoComp, peopleInfoComp, bugFollowersList, bugTimeLogList);
    }

    @Override
    protected void onPreviewItem() {
        tagViewComponent.display(ProjectTypeConstants.BUG, beanItem.getId());
        activityComponent.loadActivities("" + beanItem.getId());
        bugTimeLogList.displayTime(beanItem);

        bugFollowersList.displayFollowers(beanItem);

        dateInfoComp.displayEntryDateTime(beanItem);
        peopleInfoComp.displayEntryPeople(beanItem);
    }

    @Override
    protected String initFormTitle() {
        return AppContext.getMessage(BugI18nEnum.FORM_READ_TITLE, beanItem.getBugkey(), beanItem.getSummary());
    }

    @Override
    protected String getType() {
        return ProjectTypeConstants.BUG;
    }

    private static class BugPreviewFormLayout extends ReadViewLayout {
        private ELabel titleLbl;

        void displayBugHeader(final SimpleBug bug) {
            MVerticalLayout header = new MVerticalLayout().withWidth("100%").withMargin(false);
            titleLbl = ELabel.h2(bug.getSummary());
            header.with(titleLbl).expand(titleLbl);
            this.addHeader(header);

            if (bug.isCompleted()) {
                this.addTitleStyleName(UIConstants.LINK_COMPLETED);
            } else if (bug.isOverdue()) {
                this.addTitleStyleName(UIConstants.LABEL_OVERDUE);
            }

            BugRelationService bugRelationService = ApplicationContextUtil.getSpringBean(BugRelationService.class);
            List<SimpleRelatedBug> relatedBugs = bugRelationService.findRelatedBugs(bug.getId());
            if (relatedBugs != null && relatedBugs.size() > 0) {
                for (final SimpleRelatedBug relatedBug : relatedBugs) {
                    MHorizontalLayout bugContainer = new MHorizontalLayout().withWidth("100%");
                    bugContainer.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
                    String bugLinkValue = buildItemValue(relatedBug);
                    Button statusLink = new Button(AppContext.getMessage(OptionI18nEnum.BugRelation.class, relatedBug.getRelatedType()));
                    statusLink.setStyleName(UIConstants.BUTTON_BLOCK);
                    ELabel bugLink = new ELabel(bugLinkValue, ContentMode.HTML).withWidthUndefined();
                    Button removeBtn = new Button("Remove", new Button.ClickListener() {
                        @Override
                        public void buttonClick(ClickEvent clickEvent) {
                            ConfirmDialogExt.show(UI.getCurrent(), AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE,
                                    AppContext.getSiteName()),
                                    AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                                    AppContext.getMessage(GenericI18Enum.BUTTON_YES),
                                    AppContext.getMessage(GenericI18Enum.BUTTON_NO), new ConfirmDialog.Listener() {
                                        @Override
                                        public void onClose(ConfirmDialog confirmDialog) {
                                            RelatedBugExample ex = new RelatedBugExample();
                                            ex.createCriteria().andBugidEqualTo(bug.getId()).andRelatedidEqualTo(relatedBug.getBugId());
                                            RelatedBugMapper bugMapper = ApplicationContextUtil.getSpringBean(RelatedBugMapper.class);
                                            bugMapper.deleteByExample(ex);
                                            displayBugHeader(bug);
                                        }
                                    });
                        }
                    });
                    removeBtn.setIcon(FontAwesome.TRASH_O);
                    removeBtn.addStyleName(UIConstants.BUTTON_DANGER);
                    bugContainer.with(statusLink, removeBtn, bugLink).expand(bugLink).alignAll(Alignment.TOP_LEFT);
                    header.with(bugContainer);
                }
            }
        }

        @Override
        public void addTitleStyleName(String styleName) {
            titleLbl.addStyleName(styleName);
        }

        @Override
        public void removeTitleStyleName(String styleName) {
            titleLbl.removeStyleName(styleName);
        }

        @Override
        public void setTitle(String title) {
        }
    }

    private static String buildItemValue(SimpleRelatedBug relatedBug) {
        String uid = UUID.randomUUID().toString();
        String linkName = String.format("[#%d] - %s", relatedBug.getBugKey(), relatedBug.getBugSummary());
        A bugLink = new A().setId("tag" + uid).setHref(ProjectLinkBuilder.generateBugPreviewFullLink(relatedBug.getBugKey(),
                CurrentProjectVariables.getShortName())).appendText(linkName).setStyle("display:inline");
        bugLink.setAttribute("onmouseover", TooltipHelper.projectHoverJsFunction(uid, ProjectTypeConstants.BUG,
                relatedBug.getBugId() + ""));
        bugLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction(uid));
        return new Div().appendChild(bugLink, DivLessFormatter.EMPTY_SPACE(),
                TooltipHelper.buildDivTooltipEnable(uid)).write();
    }

    @Override
    protected AdvancedPreviewBeanForm<SimpleBug> initPreviewForm() {
        return new AdvancedPreviewBeanForm<>();
    }

    @Override
    protected IFormLayoutFactory initFormLayoutFactory() {
        return new FormLayoutFactory();
    }

    @Override
    protected AbstractBeanFieldGroupViewFieldFactory<SimpleBug> initBeanFormFieldFactory() {
        return new PreviewFormFieldFactory(this.previewForm);
    }

    @Override
    protected ComponentContainer createButtonControls() {
        ProjectPreviewFormControlsGenerator<SimpleBug> bugPreviewFormControls = new ProjectPreviewFormControlsGenerator<>(previewForm);
        MButton linkBtn = new MButton("Link", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                UI.getCurrent().addWindow(new LinkIssueWindow(BugReadViewImpl.this, beanItem));
            }
        }).withIcon(FontAwesome.LINK);
        linkBtn.addStyleName("black");
        bugPreviewFormControls.addOptionButton(linkBtn);

        HorizontalLayout topPanel = bugPreviewFormControls.createButtonControls(
                ProjectPreviewFormControlsGenerator.ADD_BTN_PRESENTED
                        | ProjectPreviewFormControlsGenerator.DELETE_BTN_PRESENTED
                        | ProjectPreviewFormControlsGenerator.EDIT_BTN_PRESENTED
                        | ProjectPreviewFormControlsGenerator.CLONE_BTN_PRESENTED,
                ProjectRolePermissionCollections.BUGS);

        Button assignBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_ASSIGN), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final ClickEvent event) {
                UI.getCurrent().addWindow(new AssignBugWindow(BugReadViewImpl.this, beanItem));
            }
        });
        assignBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.BUGS));
        assignBtn.setIcon(FontAwesome.SHARE);

        assignBtn.setStyleName(UIConstants.BUTTON_ACTION);

        bugWorkflowControl = new CssLayout();

        bugPreviewFormControls.insertToControlBlock(bugWorkflowControl);
        bugPreviewFormControls.insertToControlBlock(assignBtn);
        topPanel.setSizeUndefined();

        return topPanel;
    }

    protected ComponentContainer createExtraControls() {
        tagViewComponent = new TagViewComponent();
        return tagViewComponent;
    }

    @Override
    protected ComponentContainer createBottomPanel() {
        return activityComponent;
    }

    private static class FormLayoutFactory implements IFormLayoutFactory {
        private static final long serialVersionUID = 1L;
        private GridFormLayoutHelper informationLayout;

        @Override
        public void attachField(Object propertyId, final Field<?> field) {
            if (BugWithBLOBs.Field.description.equalTo(propertyId)) {
                informationLayout.addComponent(field, AppContext.getMessage(GenericI18Enum.FORM_DESCRIPTION), 0, 0, 2, "100%");
            } else if (BugWithBLOBs.Field.environment.equalTo(propertyId)) {
                informationLayout.addComponent(field, AppContext.getMessage(BugI18nEnum.FORM_ENVIRONMENT), 0, 1, 2, "100%");
            } else if (BugWithBLOBs.Field.status.equalTo(propertyId)) {
                informationLayout.addComponent(field, AppContext.getMessage(BugI18nEnum.FORM_STATUS), 0, 2);
            } else if (BugWithBLOBs.Field.priority.equalTo(propertyId)) {
                informationLayout.addComponent(field, AppContext.getMessage(BugI18nEnum.FORM_PRIORITY), 1, 2);
            } else if (BugWithBLOBs.Field.severity.equalTo(propertyId)) {
                informationLayout.addComponent(field, AppContext.getMessage(BugI18nEnum.FORM_SEVERITY), 0, 3);
            } else if (BugWithBLOBs.Field.resolution.equalTo(propertyId)) {
                informationLayout.addComponent(field, AppContext.getMessage(BugI18nEnum.FORM_RESOLUTION), 1, 3);
            } else if (BugWithBLOBs.Field.duedate.equalTo(propertyId)) {
                informationLayout.addComponent(field, AppContext.getMessage(BugI18nEnum.FORM_DUE_DATE), 0, 4);
            } else if (BugWithBLOBs.Field.createdtime.equalTo(propertyId)) {
                informationLayout.addComponent(field, AppContext.getMessage(BugI18nEnum.FORM_CREATED_TIME), 1, 4);
            } else if (SimpleBug.Field.loguserFullName.equalTo(propertyId)) {
                informationLayout.addComponent(field, AppContext.getMessage(BugI18nEnum.FORM_LOG_BY), 0, 5);
            } else if (SimpleBug.Field.assignuserFullName.equalTo(propertyId)) {
                informationLayout.addComponent(field, AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE), 1, 5);
            } else if (SimpleBug.Field.milestoneName.equalTo(propertyId)) {
                informationLayout.addComponent(field, AppContext.getMessage(BugI18nEnum.FORM_PHASE), 0, 6, 2, "100%");
            } else if (SimpleBug.Field.components.equalTo(propertyId)) {
                informationLayout.addComponent(field, AppContext.getMessage(BugI18nEnum.FORM_COMPONENTS), 0, 7, 2, "100%");
            } else if (SimpleBug.Field.affectedVersions.equalTo(propertyId)) {
                informationLayout.addComponent(field, AppContext.getMessage(BugI18nEnum.FORM_AFFECTED_VERSIONS), 0, 8, 2, "100%");
            } else if (SimpleBug.Field.fixedVersions.equalTo(propertyId)) {
                informationLayout.addComponent(field, AppContext.getMessage(BugI18nEnum.FORM_FIXED_VERSIONS), 0, 9, 2, "100%");
            } else if (BugWithBLOBs.Field.id.equalTo(propertyId)) {
                informationLayout.addComponent(field, AppContext.getMessage(BugI18nEnum.FORM_ATTACHMENT), 0, 10, 2, "100%");
            }
        }

        @Override
        public ComponentContainer getLayout() {
            final VerticalLayout layout = new VerticalLayout();
            informationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(2, 11);
            layout.addComponent(informationLayout.getLayout());
            layout.setComponentAlignment(informationLayout.getLayout(), Alignment.BOTTOM_CENTER);
            return layout;
        }
    }

    private class PreviewFormFieldFactory extends AbstractBeanFieldGroupViewFieldFactory<SimpleBug> {
        private static final long serialVersionUID = 1L;

        public PreviewFormFieldFactory(GenericBeanForm<SimpleBug> form) {
            super(form);
        }

        @Override
        protected Field<?> onCreateField(final Object propertyId) {
            if (BugWithBLOBs.Field.duedate.equalTo(propertyId)) {
                return new DateViewField(beanItem.getDuedate());
            } else if (BugWithBLOBs.Field.createdtime.equalTo(propertyId)) {
                return new DateViewField(beanItem.getCreatedtime());
            } else if (SimpleBug.Field.assignuserFullName.equalTo(propertyId)) {
                return new ProjectUserFormLinkField(beanItem.getAssignuser(),
                        beanItem.getAssignUserAvatarId(), beanItem.getAssignuserFullName());
            } else if (SimpleBug.Field.loguserFullName.equalTo(propertyId)) {
                return new ProjectUserFormLinkField(beanItem.getLogby(),
                        beanItem.getLoguserAvatarId(), beanItem.getLoguserFullName());
            } else if (BugWithBLOBs.Field.id.equalTo(propertyId)) {
                return new ProjectFormAttachmentDisplayField(
                        beanItem.getProjectid(), ProjectTypeConstants.BUG, beanItem.getId());
            } else if (SimpleBug.Field.components.equalTo(propertyId)) {
                final List<Component> components = beanItem.getComponents();
                if (CollectionUtils.isNotEmpty(components)) {
                    ContainerViewField componentContainer = new ContainerViewField();
                    for (final Component component : beanItem.getComponents()) {
                        Button componentLink = new Button(StringUtils.trim(component.getComponentname(), 25, true), new Button.ClickListener() {
                            private static final long serialVersionUID = 1L;

                            @Override
                            public void buttonClick(ClickEvent event) {
                                EventBusFactory.getInstance().post(new BugComponentEvent.GotoRead(
                                        BugReadViewImpl.this, component.getId()));
                            }
                        });
                        componentLink.setDescription(component.getComponentname());
                        componentContainer.addComponentField(componentLink);
                        componentLink.setStyleName(ValoTheme.BUTTON_SMALL);
                        componentLink.addStyleName(UIConstants.BUTTON_BLOCK);
                    }
                    return componentContainer;
                } else {
                    return new DefaultViewField("");
                }
            } else if (SimpleBug.Field.affectedVersions.equalTo(propertyId)) {
                List<Version> affectedVersions = beanItem.getAffectedVersions();
                if (CollectionUtils.isNotEmpty(affectedVersions)) {
                    ContainerViewField componentContainer = new ContainerViewField();
                    for (final Version version : beanItem.getAffectedVersions()) {
                        Button versionLink = new Button(StringUtils.trim(version.getVersionname(), 25, true), new Button.ClickListener() {
                            private static final long serialVersionUID = 1L;

                            @Override
                            public void buttonClick(ClickEvent event) {
                                EventBusFactory.getInstance().post(new BugVersionEvent.GotoRead(BugReadViewImpl.this, version.getId()));
                            }
                        });
                        versionLink.setDescription(version.getVersionname());
                        componentContainer.addComponentField(versionLink);
                        versionLink.setStyleName(ValoTheme.BUTTON_SMALL);
                        versionLink.addStyleName(UIConstants.BUTTON_BLOCK);
                    }
                    return componentContainer;
                } else {
                    return new DefaultViewField("");
                }
            } else if (SimpleBug.Field.fixedVersions.equalTo(propertyId)) {
                List<Version> fixedVersions = beanItem.getFixedVersions();
                if (CollectionUtils.isNotEmpty(fixedVersions)) {
                    ContainerViewField componentContainer = new ContainerViewField();
                    for (final Version version : beanItem.getFixedVersions()) {
                        Button versionLink = new Button(StringUtils.trim(version.getVersionname(), 25, true), new Button.ClickListener() {
                            private static final long serialVersionUID = 1L;

                            @Override
                            public void buttonClick(ClickEvent event) {
                                EventBusFactory.getInstance().post(new BugVersionEvent.GotoRead(BugReadViewImpl.this, version.getId()));
                            }
                        });
                        versionLink.setDescription(version.getVersionname());
                        componentContainer.addComponentField(versionLink);
                        versionLink.setStyleName(ValoTheme.BUTTON_SMALL);
                        versionLink.addStyleName(UIConstants.BUTTON_BLOCK);
                    }
                    return componentContainer;
                } else {
                    return new DefaultViewField("");
                }

            } else if (SimpleBug.Field.milestoneName.equalTo(propertyId)) {
                return new ProjectItemViewField(ProjectTypeConstants.MILESTONE, beanItem.getMilestoneid() + "", beanItem.getMilestoneName());
            } else if (BugWithBLOBs.Field.environment.equalTo(propertyId)) {
                return new RichTextViewField(beanItem.getEnvironment());
            } else if (BugWithBLOBs.Field.description.equalTo(propertyId)) {
                return new RichTextViewField(beanItem.getDescription());
            } else if (BugWithBLOBs.Field.status.equalTo(propertyId)) {
                return new I18nFormViewField(beanItem.getStatus(), BugStatus.class);
            } else if (BugWithBLOBs.Field.priority.equalTo(propertyId)) {
                if (StringUtils.isNotBlank(beanItem.getPriority())) {
                    String priorityLink = ProjectAssetsManager.getBugPriority(beanItem.getPriority()).getHtml() + " " + beanItem.getPriority();
                    DefaultViewField field = new DefaultViewField(priorityLink, ContentMode.HTML);
                    field.addStyleName("bug-" + beanItem.getPriority().toLowerCase());
                    return field;
                }
            } else if (BugWithBLOBs.Field.severity.equalTo(propertyId)) {
                if (StringUtils.isNotBlank(beanItem.getSeverity())) {
                    String severityLink = FontAwesome.STAR.getHtml() + " " +
                            AppContext.getMessage(BugSeverity.class, beanItem.getSeverity());
                    DefaultViewField lbPriority = new DefaultViewField(severityLink, ContentMode.HTML);
                    lbPriority.addStyleName("bug-severity-" + beanItem.getSeverity().toLowerCase());
                    return lbPriority;
                }
            } else if (BugWithBLOBs.Field.resolution.equalTo(propertyId)) {
                return new I18nFormViewField(beanItem.getResolution(), BugResolution.class);
            }
            return null;
        }
    }

    @Override
    public HasPreviewFormHandlers<SimpleBug> getPreviewFormHandlers() {
        return this.previewForm;
    }

    private static class PeopleInfoComp extends MVerticalLayout {
        private static final long serialVersionUID = 1L;

        private void displayEntryPeople(ValuedBean bean) {
            this.removeAllComponents();
            this.withMargin(new MarginInfo(false, false, false, true));

            Label peopleInfoHeader = new Label(FontAwesome.USER.getHtml() + " " +
                    AppContext.getMessage(ProjectCommonI18nEnum.SUB_INFO_PEOPLE), ContentMode.HTML);
            peopleInfoHeader.setStyleName("info-hdr");
            this.addComponent(peopleInfoHeader);

            GridLayout layout = new GridLayout(2, 2);
            layout.setSpacing(true);
            layout.setWidth("100%");
            layout.setMargin(new MarginInfo(false, false, false, true));
            try {
                Label createdLbl = new Label(AppContext.getMessage(ProjectCommonI18nEnum.ITEM_CREATED_PEOPLE));
                createdLbl.setSizeUndefined();
                layout.addComponent(createdLbl, 0, 0);

                String createdUserName = (String) PropertyUtils.getProperty(bean, "logby");
                String createdUserAvatarId = (String) PropertyUtils.getProperty(bean, "loguserAvatarId");
                String createdUserDisplayName = (String) PropertyUtils.getProperty(bean, "loguserFullName");

                ProjectMemberLink createdUserLink = new ProjectMemberLink(createdUserName, createdUserAvatarId, createdUserDisplayName);
                layout.addComponent(createdUserLink, 1, 0);
                layout.setColumnExpandRatio(1, 1.0f);

                Label assigneeLbl = new Label(AppContext.getMessage(ProjectCommonI18nEnum.ITEM_ASSIGN_PEOPLE));
                assigneeLbl.setSizeUndefined();
                layout.addComponent(assigneeLbl, 0, 1);
                String assignUserName = (String) PropertyUtils.getProperty(bean, "assignuser");
                String assignUserAvatarId = (String) PropertyUtils.getProperty(bean, "assignUserAvatarId");
                String assignUserDisplayName = (String) PropertyUtils.getProperty(bean, "assignuserFullName");

                ProjectMemberLink assignUserLink = new ProjectMemberLink(assignUserName, assignUserAvatarId, assignUserDisplayName);
                layout.addComponent(assignUserLink, 1, 1);
            } catch (Exception e) {
                LOG.error("Can not build user link {} ", BeanUtility.printBeanObj(bean));
            }

            this.addComponent(layout);

        }
    }
}
