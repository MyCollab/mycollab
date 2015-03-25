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

import com.esofthead.mycollab.common.CommentType;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.ValuedBean;
import com.esofthead.mycollab.core.utils.BeanUtility;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.file.AttachmentType;
import com.esofthead.mycollab.module.project.*;
import com.esofthead.mycollab.module.project.events.BugComponentEvent;
import com.esofthead.mycollab.module.project.events.BugEvent;
import com.esofthead.mycollab.module.project.events.BugVersionEvent;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugPriority;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugResolution;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugSeverity;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugStatus;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.module.project.ui.components.*;
import com.esofthead.mycollab.module.project.ui.form.ProjectFormAttachmentDisplayField;
import com.esofthead.mycollab.module.project.view.bug.components.LinkIssueWindow;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectUserFormLinkField;
import com.esofthead.mycollab.module.tracker.domain.BugWithBLOBs;
import com.esofthead.mycollab.module.tracker.domain.Component;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.domain.Version;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.schedule.email.project.BugRelayEmailNotificationAction;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.mvp.ViewScope;
import com.esofthead.mycollab.vaadin.ui.*;
import com.esofthead.mycollab.vaadin.ui.form.field.*;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComponentContainer;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.maddon.button.MButton;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;
import org.vaadin.peter.buttongroup.ButtonGroup;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent(scope = ViewScope.PROTOTYPE)
public class BugReadViewImpl extends AbstractPreviewItemComp<SimpleBug>
        implements BugReadView, IBugCallbackStatusComp {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(BugReadViewImpl.class);

    private TagViewComponent tagViewComponent;

    private HorizontalLayout bugWorkflowControl;

    private BugHistoryList historyList;

    private ProjectFollowersComp<SimpleBug> bugFollowersList;

    private BugTimeLogSheet bugTimeLogList;

    private CommentDisplay commentList;

    private DateInfoComp dateInfoComp;

    private PeopleInfoComp peopleInfoComp;

    public BugReadViewImpl() {
        super(AppContext.getMessage(BugI18nEnum.VIEW_READ_TITLE),
                ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG), new BugPreviewFormLayout());
    }

    private void displayWorkflowControl() {
        if (BugStatus.Open.name().equals(beanItem.getStatus())
                || BugStatus.ReOpened.name().equals(beanItem.getStatus())) {
            this.bugWorkflowControl.removeAllComponents();
            final ButtonGroup navButton = new ButtonGroup();
            final Button startProgressBtn = new Button(
                    AppContext.getMessage(BugI18nEnum.BUTTON_START_PROGRESS),
                    new Button.ClickListener() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void buttonClick(final ClickEvent event) {
                            beanItem.setStatus(BugStatus.InProgress.name());
                            final BugService bugService = ApplicationContextUtil
                                    .getSpringBean(BugService.class);
                            bugService.updateSelectiveWithSession(beanItem,
                                    AppContext.getUsername());
                            displayWorkflowControl();
                        }
                    });
            startProgressBtn.addStyleName(UIConstants.THEME_BROWN_LINK);
            navButton.addButton(startProgressBtn);

            final Button resolveBtn = new Button(
                    AppContext.getMessage(BugI18nEnum.BUTTON_RESOLVED),
                    new Button.ClickListener() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void buttonClick(final ClickEvent event) {
                            UI.getCurrent().addWindow(
                                    new ResolvedInputWindow(
                                            BugReadViewImpl.this, beanItem));
                        }
                    });
            resolveBtn.addStyleName(UIConstants.THEME_BROWN_LINK);
            navButton.addButton(resolveBtn);

            final Button wontFixBtn = new Button(
                    AppContext.getMessage(BugI18nEnum.BUTTON_WONTFIX),
                    new Button.ClickListener() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void buttonClick(final ClickEvent event) {
                            UI.getCurrent().addWindow(
                                    new WontFixExplainWindow(
                                            BugReadViewImpl.this, beanItem));
                        }
                    });
            wontFixBtn.addStyleName(UIConstants.THEME_BROWN_LINK);
            navButton.addButton(wontFixBtn);
            this.bugWorkflowControl.addComponent(navButton);
        } else if (BugStatus.InProgress.name()
                .equals(beanItem.getStatus())) {
            this.bugWorkflowControl.removeAllComponents();
            final ButtonGroup navButton = new ButtonGroup();
            final Button stopProgressBtn = new Button(
                    AppContext.getMessage(BugI18nEnum.BUTTON_STOP_PROGRESS),
                    new Button.ClickListener() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void buttonClick(final ClickEvent event) {
                            beanItem.setStatus(BugStatus.Open.name());
                            final BugService bugService = ApplicationContextUtil
                                    .getSpringBean(BugService.class);
                            bugService.updateSelectiveWithSession(beanItem,
                                    AppContext.getUsername());
                            displayWorkflowControl();
                        }
                    });
            stopProgressBtn.addStyleName(UIConstants.THEME_BROWN_LINK);
            navButton.addButton(stopProgressBtn);

            final Button resolveBtn = new Button(
                    AppContext.getMessage(BugI18nEnum.BUTTON_RESOLVED),
                    new Button.ClickListener() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void buttonClick(final ClickEvent event) {
                            UI.getCurrent().addWindow(
                                    new ResolvedInputWindow(
                                            BugReadViewImpl.this, beanItem));
                        }
                    });
            resolveBtn.addStyleName(UIConstants.THEME_BROWN_LINK);
            navButton.addButton(resolveBtn);
            this.bugWorkflowControl.addComponent(navButton);
        } else if (BugStatus.Verified.name().equals(beanItem.getStatus())) {
            this.bugWorkflowControl.removeAllComponents();
            final ButtonGroup navButton = new ButtonGroup();
            final Button reopenBtn = new Button(
                    AppContext.getMessage(GenericI18Enum.BUTTON_REOPEN),
                    new Button.ClickListener() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void buttonClick(final ClickEvent event) {
                            UI.getCurrent().addWindow(
                                    new ReOpenWindow(BugReadViewImpl.this,
                                            beanItem));
                        }
                    });
            reopenBtn.addStyleName(UIConstants.THEME_BROWN_LINK);
            navButton.addButton(reopenBtn);

            this.bugWorkflowControl.addComponent(navButton);
        } else if (BugStatus.Resolved.name().equals(beanItem.getStatus())) {
            this.bugWorkflowControl.removeAllComponents();
            final ButtonGroup navButton = new ButtonGroup();
            final Button reopenBtn = new Button(
                    AppContext.getMessage(GenericI18Enum.BUTTON_REOPEN),
                    new Button.ClickListener() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void buttonClick(final ClickEvent event) {
                            UI.getCurrent().addWindow(
                                    new ReOpenWindow(BugReadViewImpl.this,
                                            beanItem));
                        }
                    });
            reopenBtn.addStyleName(UIConstants.THEME_BROWN_LINK);
            navButton.addButton(reopenBtn);

            final Button approveNCloseBtn = new Button(
                    AppContext.getMessage(BugI18nEnum.BUTTON_APPROVE_CLOSE),
                    new Button.ClickListener() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void buttonClick(final ClickEvent event) {
                            UI.getCurrent().addWindow(
                                    new ApproveInputWindow(
                                            BugReadViewImpl.this, beanItem));
                        }
                    });
            approveNCloseBtn.addStyleName(UIConstants.THEME_BROWN_LINK);
            navButton.addButton(approveNCloseBtn);
            this.bugWorkflowControl.addComponent(navButton);
        } else if (BugStatus.Resolved.name().equals(beanItem.getStatus())) {
            this.bugWorkflowControl.removeAllComponents();
            final ButtonGroup navButton = new ButtonGroup();
            final Button reopenBtn = new Button(
                    AppContext.getMessage(GenericI18Enum.BUTTON_REOPEN),
                    new Button.ClickListener() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void buttonClick(final ClickEvent event) {
                            UI.getCurrent().addWindow(
                                    new ReOpenWindow(BugReadViewImpl.this,
                                            beanItem));
                        }
                    });
            reopenBtn.setStyleName(UIConstants.THEME_BROWN_LINK);
            navButton.addButton(reopenBtn);

            this.bugWorkflowControl.addComponent(navButton);
        }
        this.bugWorkflowControl.setEnabled(CurrentProjectVariables
                .canWrite(ProjectRolePermissionCollections.BUGS));
    }

    @Override
    public SimpleBug getItem() {
        return beanItem;
    }

    @Override
    public void previewItem(final SimpleBug item) {
        super.previewItem(item);
        displayWorkflowControl();
        ((BugPreviewFormLayout) previewLayout).displayBugHeader(beanItem);
    }

    @Override
    public void refreshBugItem() {
        EventBusFactory.getInstance().post(
                new BugEvent.GotoRead(BugReadViewImpl.this, beanItem
                        .getId()));
    }

    @Override
    protected void initRelatedComponents() {
        commentList = new CommentDisplay(CommentType.PRJ_BUG,
                CurrentProjectVariables.getProjectId(), true, true,
                BugRelayEmailNotificationAction.class);

        historyList = new BugHistoryList();
        dateInfoComp = new DateInfoComp();
        peopleInfoComp = new PeopleInfoComp();
        bugFollowersList = new ProjectFollowersComp<>(
                ProjectTypeConstants.BUG, ProjectRolePermissionCollections.BUGS);
        bugTimeLogList = new BugTimeLogSheet();
        addToSideBar(dateInfoComp, peopleInfoComp, bugFollowersList, bugTimeLogList);
    }

    @Override
    protected void onPreviewItem() {
        tagViewComponent.display(ProjectTypeConstants.BUG, beanItem.getId());
        commentList.loadComments("" + beanItem.getId());
        historyList.loadHistory(beanItem.getId());
        bugTimeLogList.displayTime(beanItem);

        bugFollowersList.displayFollowers(beanItem);

        dateInfoComp.displayEntryDateTime(beanItem);
        peopleInfoComp.displayEntryPeople(beanItem);
    }

    @Override
    protected String initFormTitle() {
        return AppContext.getMessage(BugI18nEnum.FORM_READ_TITLE,
                beanItem.getBugkey(), beanItem.getSummary());
    }

    @Override
    protected String getType() {
        return ProjectTypeConstants.BUG;
    }

    private static class BugPreviewFormLayout extends ReadViewLayout {
        private Label titleLbl;

        void displayBugHeader(SimpleBug bug) {
            MHorizontalLayout header = new MHorizontalLayout().withWidth("100%");
            titleLbl = new Label(bug.getSummary());
            titleLbl.setStyleName("headerName");
            header.with(titleLbl).expand(titleLbl);
            this.addHeader(header);

            this.clearTitleStyleName();
            if (bug.isCompleted()) {
                this.addTitleStyleName(UIConstants.LINK_COMPLETED);
            } else if (bug.isOverdue()) {
                this.setTitleStyleName("headerNameOverdue");
            }
        }

        @Override
        public void clearTitleStyleName() {
            this.titleLbl.setStyleName("headerName");
        }

        @Override
        public void addTitleStyleName(final String styleName) {
            this.titleLbl.addStyleName(styleName);
        }

        @Override
        public void setTitleStyleName(final String styleName) {
            this.titleLbl.setStyleName(styleName);
        }

        @Override
        public void removeTitleStyleName(final String styleName) {
            this.titleLbl.removeStyleName(styleName);
        }

        @Override
        public void setTitle(final String title) {
        }
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
        ProjectPreviewFormControlsGenerator<SimpleBug> bugPreviewFormControls = new
                ProjectPreviewFormControlsGenerator<>(
                previewForm);
        MButton linkBtn = new MButton("Link", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                UI.getCurrent().addWindow(new LinkIssueWindow(beanItem));
            }
        }).withIcon(FontAwesome.LINK);
        linkBtn.addStyleName("black");
        bugPreviewFormControls.addOptionButton(linkBtn);

        final HorizontalLayout topPanel = bugPreviewFormControls
                .createButtonControls(
                        ProjectPreviewFormControlsGenerator.ADD_BTN_PRESENTED
                                | ProjectPreviewFormControlsGenerator.DELETE_BTN_PRESENTED
                                | ProjectPreviewFormControlsGenerator.EDIT_BTN_PRESENTED
                                | ProjectPreviewFormControlsGenerator.CLONE_BTN_PRESENTED,
                        ProjectRolePermissionCollections.BUGS);

        final Button assignBtn = new Button(
                AppContext.getMessage(GenericI18Enum.BUTTON_ASSIGN),
                new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(final ClickEvent event) {
                        UI.getCurrent().addWindow(
                                new AssignBugWindow(BugReadViewImpl.this,
                                        beanItem));
                    }
                });
        assignBtn.setEnabled(CurrentProjectVariables
                .canWrite(ProjectRolePermissionCollections.BUGS));
        assignBtn.setIcon(FontAwesome.SHARE);

        assignBtn.setStyleName(UIConstants.THEME_GREEN_LINK);

        this.bugWorkflowControl = new HorizontalLayout();
        this.bugWorkflowControl.setMargin(false);
        this.bugWorkflowControl.addStyleName("workflow-controls");

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
        final TabSheetLazyLoadComponent tabBugDetail = new TabSheetLazyLoadComponent();
        tabBugDetail.addTab(commentList, AppContext.getMessage(ProjectCommonI18nEnum.TAB_COMMENT), FontAwesome.COMMENTS);
        tabBugDetail.addTab(historyList, AppContext.getMessage(ProjectCommonI18nEnum.TAB_HISTORY), FontAwesome.HISTORY);
        return tabBugDetail;
    }

    private class FormLayoutFactory implements IFormLayoutFactory {
        private static final long serialVersionUID = 1L;
        private GridFormLayoutHelper informationLayout;

        @Override
        public void attachField(final Object propertyId, final Field<?> field) {
            if (BugWithBLOBs.Field.description.equalTo(propertyId)) {
                this.informationLayout.addComponent(field,
                        AppContext.getMessage(GenericI18Enum.FORM_DESCRIPTION),
                        0, 0, 2, "100%");
            } else if (BugWithBLOBs.Field.environment.equalTo(propertyId)) {
                this.informationLayout.addComponent(field,
                        AppContext.getMessage(BugI18nEnum.FORM_ENVIRONMENT), 0,
                        1, 2, "100%");
            } else if (BugWithBLOBs.Field.status.equalTo(propertyId)) {
                this.informationLayout.addComponent(field,
                        AppContext.getMessage(BugI18nEnum.FORM_STATUS), 0, 2);
            } else if (BugWithBLOBs.Field.priority.equalTo(propertyId)) {
                this.informationLayout.addComponent(field,
                        AppContext.getMessage(BugI18nEnum.FORM_PRIORITY), 1, 2);
            } else if (BugWithBLOBs.Field.severity.equalTo(propertyId)) {
                this.informationLayout.addComponent(field,
                        AppContext.getMessage(BugI18nEnum.FORM_SEVERITY), 0, 3);
            } else if (BugWithBLOBs.Field.resolution.equalTo(propertyId)) {
                this.informationLayout.addComponent(field,
                        AppContext.getMessage(BugI18nEnum.FORM_RESOLUTION), 1,
                        3);
            } else if (BugWithBLOBs.Field.duedate.equalTo(propertyId)) {
                this.informationLayout.addComponent(field,
                        AppContext.getMessage(BugI18nEnum.FORM_DUE_DATE), 0, 4);
            } else if (BugWithBLOBs.Field.createdtime.equalTo(propertyId)) {
                this.informationLayout.addComponent(field,
                        AppContext.getMessage(BugI18nEnum.FORM_CREATED_TIME),
                        1, 4);
            } else if (SimpleBug.Field.loguserFullName.equalTo(propertyId)) {
                this.informationLayout.addComponent(field,
                        AppContext.getMessage(BugI18nEnum.FORM_LOG_BY), 0, 5);
            } else if (SimpleBug.Field.assignuserFullName.equalTo(propertyId)) {
                this.informationLayout.addComponent(field,
                        AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE), 1,
                        5);
            } else if (SimpleBug.Field.milestoneName.equalTo(propertyId)) {
                this.informationLayout.addComponent(field,
                        AppContext.getMessage(BugI18nEnum.FORM_PHASE), 0, 6, 2,
                        "100%");
            } else if (SimpleBug.Field.components.equalTo(propertyId)) {
                this.informationLayout.addComponent(field,
                        AppContext.getMessage(BugI18nEnum.FORM_COMPONENTS), 0,
                        7, 2, "100%");
            } else if (SimpleBug.Field.affectedVersions.equalTo(propertyId)) {
                this.informationLayout.addComponent(field, AppContext
                                .getMessage(BugI18nEnum.FORM_AFFECTED_VERSIONS), 0, 8,
                        2, "100%");
            } else if (SimpleBug.Field.fixedVersions.equalTo(propertyId)) {
                this.informationLayout.addComponent(field,
                        AppContext.getMessage(BugI18nEnum.FORM_FIXED_VERSIONS),
                        0, 9, 2, "100%");
            } else if (BugWithBLOBs.Field.id.equalTo(propertyId)) {
                this.informationLayout.addComponent(field,
                        AppContext.getMessage(BugI18nEnum.FORM_ATTACHMENT), 0,
                        10, 2, "100%");
            }
        }

        @Override
        public ComponentContainer getLayout() {
            final VerticalLayout layout = new VerticalLayout();
            this.informationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(2, 11);
            layout.addComponent(this.informationLayout.getLayout());
            layout.setComponentAlignment(this.informationLayout.getLayout(),
                    Alignment.BOTTOM_CENTER);
            return layout;
        }
    }

    private class PreviewFormFieldFactory extends
            AbstractBeanFieldGroupViewFieldFactory<SimpleBug> {
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
                        beanItem.getAssignUserAvatarId(),
                        beanItem.getAssignuserFullName());
            } else if (SimpleBug.Field.loguserFullName.equalTo(propertyId)) {
                return new ProjectUserFormLinkField(beanItem.getLogby(),
                        beanItem.getLoguserAvatarId(),
                        beanItem.getLoguserFullName());
            } else if (BugWithBLOBs.Field.id.equalTo(propertyId)) {
                return new ProjectFormAttachmentDisplayField(
                        beanItem.getProjectid(),
                        AttachmentType.PROJECT_BUG_TYPE, beanItem.getId());
            } else if (SimpleBug.Field.components.equalTo(propertyId)) {
                final List<Component> components = beanItem.getComponents();
                if (CollectionUtils.isNotEmpty(components)) {
                    final ContainerViewField componentContainer = new ContainerViewField();
                    for (final Component component : beanItem.getComponents()) {
                        final Button componentLink = new Button(
                                component.getComponentname(),
                                new Button.ClickListener() {
                                    private static final long serialVersionUID = 1L;

                                    @Override
                                    public void buttonClick(
                                            final ClickEvent event) {
                                        EventBusFactory.getInstance().post(
                                                new BugComponentEvent.GotoRead(
                                                        BugReadViewImpl.this,
                                                        component.getId()));
                                    }
                                });
                        componentContainer.addComponentField(componentLink);
                        componentLink.setStyleName("link");
                        componentLink.addStyleName("block");
                    }
                    return componentContainer;
                } else {
                    return new DefaultViewField("");
                }
            } else if (SimpleBug.Field.affectedVersions.equalTo(propertyId)) {
                final List<Version> affectedVersions = beanItem
                        .getAffectedVersions();
                if (CollectionUtils.isNotEmpty(affectedVersions)) {
                    final ContainerViewField componentContainer = new ContainerViewField();
                    for (final Version version : beanItem.getAffectedVersions()) {
                        final Button versionLink = new Button(
                                version.getVersionname(),
                                new Button.ClickListener() {
                                    private static final long serialVersionUID = 1L;

                                    @Override
                                    public void buttonClick(
                                            final ClickEvent event) {
                                        EventBusFactory.getInstance().post(
                                                new BugVersionEvent.GotoRead(
                                                        BugReadViewImpl.this,
                                                        version.getId()));
                                    }
                                });
                        componentContainer.addComponentField(versionLink);
                        versionLink.setStyleName("link");
                        versionLink.addStyleName("block");
                    }
                    return componentContainer;
                } else {
                    return new DefaultViewField("");
                }
            } else if (SimpleBug.Field.fixedVersions.equalTo(propertyId)) {
                final List<Version> fixedVersions = beanItem.getFixedVersions();
                if (CollectionUtils.isNotEmpty(fixedVersions)) {
                    final ContainerViewField componentContainer = new ContainerViewField();
                    for (final Version version : beanItem.getFixedVersions()) {
                        final Button versionLink = new Button(
                                version.getVersionname(),
                                new Button.ClickListener() {
                                    private static final long serialVersionUID = 1L;

                                    @Override
                                    public void buttonClick(
                                            final ClickEvent event) {
                                        EventBusFactory.getInstance().post(
                                                new BugVersionEvent.GotoRead(
                                                        BugReadViewImpl.this,
                                                        version.getId()));
                                    }
                                });
                        componentContainer.addComponentField(versionLink);
                        versionLink.setStyleName("link");
                        versionLink.addStyleName("block");
                    }
                    return componentContainer;
                } else {
                    return new DefaultViewField("");
                }

            } else if (SimpleBug.Field.milestoneName.equalTo(propertyId)) {
                if (beanItem.getMilestoneid() != null) {
                    return new LinkViewField(
                            beanItem.getMilestoneName(),
                            ProjectLinkBuilder
                                    .generateMilestonePreviewFullLink(
                                            beanItem.getProjectid(),
                                            beanItem.getMilestoneid()),
                            ProjectAssetsManager.getAsset(ProjectTypeConstants.MILESTONE));
                } else {
                    return new DefaultViewField("");
                }

            } else if (BugWithBLOBs.Field.environment.equalTo(propertyId)) {
                return new RichTextViewField(beanItem.getEnvironment());
            } else if (BugWithBLOBs.Field.description.equalTo(propertyId)) {
                return new RichTextViewField(beanItem.getDescription());
            } else if (BugWithBLOBs.Field.status.equalTo(propertyId)) {
                return new I18nFormViewField(beanItem.getStatus(),
                        BugStatus.class);
            } else if (BugWithBLOBs.Field.priority.equalTo(propertyId)) {
                if (StringUtils.isNotBlank(beanItem.getPriority())) {
                    final Resource iconPriority = new ExternalResource(
                            ProjectResources
                                    .getIconResourceLink12ByBugPriority(beanItem
                                            .getPriority()));
                    final Image iconEmbedded = new Image(null, iconPriority);
                    final Label lbPriority = new Label(AppContext.getMessage(
                            BugPriority.class, beanItem.getPriority()));

                    final ContainerHorizontalViewField containerField = new ContainerHorizontalViewField();
                    containerField.addComponentField(iconEmbedded);
                    containerField.addComponentField(lbPriority);
                    containerField.getLayout().setExpandRatio(lbPriority, 1.0f);
                    return containerField;
                }
            } else if (BugWithBLOBs.Field.severity.equalTo(propertyId)) {
                if (StringUtils.isNotBlank(beanItem.getSeverity())) {
                    final Resource iconPriority = new ExternalResource(
                            ProjectResources
                                    .getIconResourceLink12ByBugSeverity(beanItem
                                            .getSeverity()));
                    final Image iconEmbedded = new Image();
                    iconEmbedded.setSource(iconPriority);
                    final Label lbPriority = new Label(AppContext.getMessage(
                            BugSeverity.class, beanItem.getSeverity()));

                    final ContainerHorizontalViewField containerField = new ContainerHorizontalViewField();
                    containerField.addComponentField(iconEmbedded);
                    containerField.addComponentField(lbPriority);
                    containerField.getLayout().setExpandRatio(lbPriority, 1.0f);
                    return containerField;
                }
            } else if (BugWithBLOBs.Field.resolution.equalTo(propertyId)) {
                return new I18nFormViewField(beanItem.getResolution(),
                        BugResolution.class);
            }
            return null;
        }
    }

    @Override
    public HasPreviewFormHandlers<SimpleBug> getPreviewFormHandlers() {
        return this.previewForm;
    }

    private class PeopleInfoComp extends MVerticalLayout {
        private static final long serialVersionUID = 1L;

        private void displayEntryPeople(ValuedBean bean) {
            this.removeAllComponents();
            this.withMargin(new MarginInfo(false, false, false, true));

            Label peopleInfoHeader = new Label(FontAwesome.USER.getHtml() + " " +
                    AppContext
                            .getMessage(ProjectCommonI18nEnum.SUB_INFO_PEOPLE), ContentMode.HTML);
            peopleInfoHeader.setStyleName("info-hdr");
            this.addComponent(peopleInfoHeader);

            GridLayout layout = new GridLayout(2, 2);
            layout.setSpacing(true);
            layout.setWidth("100%");
            layout.setMargin(new MarginInfo(false, false, false, true));
            try {
                Label createdLbl = new Label(
                        AppContext
                                .getMessage(ProjectCommonI18nEnum.ITEM_CREATED_PEOPLE));
                createdLbl.setSizeUndefined();
                layout.addComponent(createdLbl, 0, 0);

                String createdUserName = (String) PropertyUtils.getProperty(
                        bean, "logby");
                String createdUserAvatarId = (String) PropertyUtils
                        .getProperty(bean, "loguserAvatarId");
                String createdUserDisplayName = (String) PropertyUtils
                        .getProperty(bean, "loguserFullName");

                UserLink createdUserLink = new UserLink(createdUserName,
                        createdUserAvatarId, createdUserDisplayName);
                layout.addComponent(createdUserLink, 1, 0);
                layout.setColumnExpandRatio(1, 1.0f);

                Label assigneeLbl = new Label(
                        AppContext
                                .getMessage(ProjectCommonI18nEnum.ITEM_ASSIGN_PEOPLE));
                assigneeLbl.setSizeUndefined();
                layout.addComponent(assigneeLbl, 0, 1);
                String assignUserName = (String) PropertyUtils.getProperty(
                        bean, "assignuser");
                String assignUserAvatarId = (String) PropertyUtils.getProperty(
                        bean, "assignUserAvatarId");
                String assignUserDisplayName = (String) PropertyUtils
                        .getProperty(bean, "assignuserFullName");

                UserLink assignUserLink = new UserLink(assignUserName,
                        assignUserAvatarId, assignUserDisplayName);
                layout.addComponent(assignUserLink, 1, 1);
            } catch (Exception e) {
                LOG.error("Can not build user link {} ",
                        BeanUtility.printBeanObj(bean));
            }

            this.addComponent(layout);

        }
    }
}
