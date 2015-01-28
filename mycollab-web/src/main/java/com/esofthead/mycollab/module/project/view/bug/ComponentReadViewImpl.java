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
import com.esofthead.mycollab.common.ModuleNameConstants;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.ValuedBean;
import com.esofthead.mycollab.core.utils.BeanUtility;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.project.i18n.ComponentI18nEnum;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugStatus;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.project.ui.components.AbstractPreviewItemComp2;
import com.esofthead.mycollab.module.project.ui.components.CommentDisplay;
import com.esofthead.mycollab.module.project.ui.components.DateInfoComp;
import com.esofthead.mycollab.module.project.ui.components.DynaFormLayout;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectUserFormLinkField;
import com.esofthead.mycollab.module.tracker.domain.Component;
import com.esofthead.mycollab.module.tracker.domain.SimpleComponent;
import com.esofthead.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.esofthead.mycollab.module.tracker.service.ComponentService;
import com.esofthead.mycollab.schedule.email.project.ComponentRelayEmailNotificationAction;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.mvp.ViewScope;
import com.esofthead.mycollab.vaadin.ui.*;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComponentContainer;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent(scope = ViewScope.PROTOTYPE)
public class ComponentReadViewImpl extends
        AbstractPreviewItemComp2<SimpleComponent> implements ComponentReadView {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory
            .getLogger(ComponentReadViewImpl.class);

    private RelatedBugComp relatedBugComp;
    private CommentDisplay commentDisplay;
    private ComponentHistoryLogList historyLogList;
    private Button quickActionStatusBtn;

    private DateInfoComp dateInfoComp;
    private PeopleInfoComp peopleInfoComp;

    public ComponentReadViewImpl() {
        super(AppContext.getMessage(ComponentI18nEnum.VIEW_READ_TITLE),
                MyCollabResource
                        .newResource(WebResourceIds._22_project_component));
    }

    @Override
    public SimpleComponent getItem() {
        return this.beanItem;
    }

    @Override
    public HasPreviewFormHandlers<SimpleComponent> getPreviewFormHandlers() {
        return this.previewForm;
    }

    @Override
    protected String initFormTitle() {
        return beanItem.getComponentname();
    }

    @Override
    protected IFormLayoutFactory initFormLayoutFactory() {
        return new DynaFormLayout(ProjectTypeConstants.BUG_COMPONENT,
                ComponentDefaultFormLayoutFactory.getForm(),
                Component.Field.componentname.name());
    }

    @Override
    protected void initRelatedComponents() {
        relatedBugComp = new RelatedBugComp();

        commentDisplay = new CommentDisplay(CommentType.PRJ_COMPONENT,
                CurrentProjectVariables.getProjectId(), true, true,
                ComponentRelayEmailNotificationAction.class);
        commentDisplay.setWidth("100%");
        commentDisplay.setMargin(true);

        historyLogList = new ComponentHistoryLogList(ModuleNameConstants.PRJ,
                ProjectTypeConstants.BUG_COMPONENT);

        dateInfoComp = new DateInfoComp();
        addToSideBar(dateInfoComp);

        peopleInfoComp = new PeopleInfoComp();
        addToSideBar(peopleInfoComp);
    }

    @Override
    protected void onPreviewItem() {
        relatedBugComp.displayBugReports();

        commentDisplay.loadComments("" + beanItem.getId());
        historyLogList.loadHistory(beanItem.getId());

        dateInfoComp.displayEntryDateTime(beanItem);
        peopleInfoComp.displayEntryPeople(beanItem);

        if (StatusI18nEnum.Open.name().equals(beanItem.getStatus())) {
            removeLayoutStyleName(UIConstants.LINK_COMPLETED);
            quickActionStatusBtn.setCaption(AppContext
                    .getMessage(GenericI18Enum.BUTTON_CLOSE));
            quickActionStatusBtn.setIcon(MyCollabResource
                    .newResource(WebResourceIds._16_project_closeTask));
        } else {
            addLayoutStyleName(UIConstants.LINK_COMPLETED);
            quickActionStatusBtn.setCaption(AppContext
                    .getMessage(GenericI18Enum.BUTTON_REOPEN));
            quickActionStatusBtn.setIcon(MyCollabResource
                    .newResource(WebResourceIds._16_project_reopenTask));

        }

    }

    @Override
    protected AdvancedPreviewBeanForm<SimpleComponent> initPreviewForm() {
        return new AdvancedPreviewBeanForm<>();
    }

    @Override
    protected AbstractBeanFieldGroupViewFieldFactory<SimpleComponent> initBeanFormFieldFactory() {
        return new AbstractBeanFieldGroupViewFieldFactory<SimpleComponent>(
                previewForm) {
            private static final long serialVersionUID = 1L;

            @Override
            protected Field<?> onCreateField(final Object propertyId) {
                if (Component.Field.userlead.equalTo(propertyId)) {
                    return new ProjectUserFormLinkField(beanItem.getUserlead(),
                            beanItem.getUserLeadAvatarId(),
                            beanItem.getUserLeadFullName());
                }
                return null;
            }
        };
    }

    @Override
    protected ComponentContainer createButtonControls() {
        ProjectPreviewFormControlsGenerator<SimpleComponent> componentPreviewForm = new
                ProjectPreviewFormControlsGenerator<>(
                previewForm);
        final HorizontalLayout topPanel = componentPreviewForm
                .createButtonControls(ProjectRolePermissionCollections.COMPONENTS);
        quickActionStatusBtn = new Button("", new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                if (StatusI18nEnum.Closed.name().equals(beanItem.getStatus())) {
                    beanItem.setStatus(StatusI18nEnum.Open.name());
                    ComponentReadViewImpl.this
                            .removeLayoutStyleName(UIConstants.LINK_COMPLETED);
                    quickActionStatusBtn.setCaption(AppContext
                            .getMessage(GenericI18Enum.BUTTON_CLOSE));
                    quickActionStatusBtn.setIcon(MyCollabResource
                            .newResource(WebResourceIds._16_project_closeTask));
                } else {
                    beanItem.setStatus(StatusI18nEnum.Closed.name());

                    ComponentReadViewImpl.this
                            .addLayoutStyleName(UIConstants.LINK_COMPLETED);
                    quickActionStatusBtn.setCaption(AppContext
                            .getMessage(GenericI18Enum.BUTTON_REOPEN));
                    quickActionStatusBtn.setIcon(MyCollabResource
                            .newResource(WebResourceIds._16_project_reopenTask));
                }

                ComponentService service = ApplicationContextUtil
                        .getSpringBean(ComponentService.class);
                service.updateSelectiveWithSession(beanItem,
                        AppContext.getUsername());

            }
        });

        quickActionStatusBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
        componentPreviewForm.insertToControlBlock(quickActionStatusBtn);

        if (!CurrentProjectVariables
                .canWrite(ProjectRolePermissionCollections.COMPONENTS)) {
            quickActionStatusBtn.setEnabled(false);
        }
        return topPanel;
    }

    @Override
    protected ComponentContainer createBottomPanel() {
        final TabsheetLazyLoadComp tabContainer = new TabsheetLazyLoadComp();

        tabContainer.addTab(commentDisplay, AppContext
                        .getMessage(ProjectCommonI18nEnum.TAB_COMMENT),
                MyCollabResource
                        .newResource(WebResourceIds._16_project_gray_comment));

        tabContainer.addTab(historyLogList, AppContext
                        .getMessage(ProjectCommonI18nEnum.TAB_HISTORY),
                MyCollabResource
                        .newResource(WebResourceIds._16_project_gray_history));

        tabContainer.addTab(relatedBugComp, AppContext
                .getMessage(BugI18nEnum.TAB_RELATED_BUGS), MyCollabResource
                .newResource(WebResourceIds._16_project_gray_bug));

        return tabContainer;
    }

    private class RelatedBugComp extends VerticalLayout implements
            IBugReportDisplayContainer {
        private static final long serialVersionUID = 1L;

        private HorizontalLayout bottomLayout;

        public RelatedBugComp() {
            this.setMargin(false);

            final HorizontalLayout header = new HorizontalLayout();
            header.setMargin(new MarginInfo(true, true, true, false));
            header.setSpacing(true);
            header.setWidth("100%");
            header.addStyleName("relatedbug-comp-header");
            final Label taskGroupSelection = new Label("");
            taskGroupSelection.addStyleName("h2");
            taskGroupSelection.addStyleName(UIConstants.THEME_NO_BORDER);
            header.addComponent(taskGroupSelection);
            header.setExpandRatio(taskGroupSelection, 1.0f);
            header.setComponentAlignment(taskGroupSelection,
                    Alignment.MIDDLE_LEFT);

            ToggleButtonGroup viewGroup = new ToggleButtonGroup();

            final Button simpleDisplay = new Button(null,
                    new Button.ClickListener() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void buttonClick(final ClickEvent event) {
                            displaySimpleView();
                        }
                    });
            simpleDisplay.setIcon(MyCollabResource
                    .newResource(WebResourceIds._16_project_list_display));

            viewGroup.addButton(simpleDisplay);

            final Button advanceDisplay = new Button(null,
                    new Button.ClickListener() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void buttonClick(final ClickEvent event) {
                            displayAdvancedView();
                        }
                    });
            advanceDisplay
                    .setIcon(MyCollabResource
                            .newResource(WebResourceIds._16_project_bug_advanced_display));
            viewGroup.addButton(advanceDisplay);
            header.addComponent(viewGroup);
            header.setComponentAlignment(viewGroup, Alignment.MIDDLE_RIGHT);

            this.addComponent(header);

            this.bottomLayout = new HorizontalLayout();
            this.bottomLayout.setSpacing(true);
            this.bottomLayout.setWidth("100%");
            this.bottomLayout
                    .setMargin(new MarginInfo(false, true, false, true));

            advanceDisplay.addStyleName("selected");

        }

        private void displaySimpleView() {
            if (this.getComponentCount() > 1) {
                this.removeComponent(this.getComponent(1));
            }

            final BugSearchCriteria criteria = new BugSearchCriteria();
            criteria.setProjectId(new NumberSearchField(CurrentProjectVariables
                    .getProjectId()));
            criteria.setComponentids(new SetSearchField<>(beanItem
                    .getId()));

            final BugSimpleDisplayWidget displayWidget = new BugSimpleDisplayWidget();
            this.addComponent(displayWidget);
            this.setMargin(true);
            displayWidget.setSearchCriteria(criteria);

        }

        private void displayAdvancedView() {
            if (this.getComponentCount() > 1) {
                this.removeComponent(this.getComponent(1));
            }

            this.addComponent(this.bottomLayout);

            this.bottomLayout.removeAllComponents();

            final SimpleProject project = CurrentProjectVariables.getProject();

            final VerticalLayout leftColumn = new VerticalLayout();
            leftColumn.setSpacing(true);
            leftColumn.setWidth("100%");

            this.bottomLayout.addComponent(leftColumn);
            this.bottomLayout.setExpandRatio(leftColumn, 1.0f);
            final UnresolvedBugsByPriorityWidget unresolvedBugWidget = new UnresolvedBugsByPriorityWidget(
                    this);
            leftColumn.addComponent(unresolvedBugWidget);
            leftColumn.setComponentAlignment(unresolvedBugWidget,
                    Alignment.MIDDLE_CENTER);

            final BugSearchCriteria unresolvedByPrioritySearchCriteria = new BugSearchCriteria();
            unresolvedByPrioritySearchCriteria
                    .setProjectId(new NumberSearchField(project.getId()));
            unresolvedByPrioritySearchCriteria
                    .setComponentids(new SetSearchField<>(beanItem
                            .getId()));
            unresolvedByPrioritySearchCriteria
                    .setStatuses(new SetSearchField<>(SearchField.AND,
                            new String[]{BugStatus.InProgress.name(),
                                    BugStatus.Open.name(),
                                    BugStatus.ReOpened.name()}));
            unresolvedBugWidget
                    .setSearchCriteria(unresolvedByPrioritySearchCriteria);

            final UnresolvedBugsByAssigneeWidget unresolvedByAssigneeWidget = new UnresolvedBugsByAssigneeWidget(
                    this);
            leftColumn.addComponent(unresolvedByAssigneeWidget);
            leftColumn.setComponentAlignment(unresolvedByAssigneeWidget,
                    Alignment.MIDDLE_CENTER);

            final BugSearchCriteria unresolvedByAssigneeSearchCriteria = new BugSearchCriteria();
            unresolvedByAssigneeSearchCriteria
                    .setProjectId(new NumberSearchField(project.getId()));
            unresolvedByAssigneeSearchCriteria
                    .setComponentids(new SetSearchField<>(beanItem
                            .getId()));
            unresolvedByAssigneeSearchCriteria
                    .setStatuses(new SetSearchField<>(SearchField.AND,
                            new String[]{BugStatus.InProgress.name(),
                                    BugStatus.Open.name(),
                                    BugStatus.ReOpened.name()}));
            unresolvedByAssigneeWidget
                    .setSearchCriteria(unresolvedByAssigneeSearchCriteria);

            final VerticalLayout rightColumn = new VerticalLayout();
            rightColumn.setMargin(new MarginInfo(false, false, false, true));
            this.bottomLayout.addComponent(rightColumn);

            final BugSearchCriteria chartSearchCriteria = new BugSearchCriteria();
            chartSearchCriteria.setProjectId(new NumberSearchField(
                    CurrentProjectVariables.getProjectId()));
            chartSearchCriteria.setComponentids(new SetSearchField<>(
                    beanItem.getId()));

            BugChartComponent bugChartComponent = new BugChartComponent(chartSearchCriteria, 400,
                    200);
            rightColumn.addComponent(bugChartComponent);
            rightColumn.setWidth("410px");

        }

        @Override
        public void displayBugReports() {
            this.displayAdvancedView();
        }

        @Override
        public void displayBugListWidget(final String title,
                                         final BugSearchCriteria criteria) {
            this.bottomLayout.removeAllComponents();
            final BugListWidget bugListWidget = new BugListWidget(title
                    + " Bug List", "Back to component dashboard", criteria,
                    this);
            bugListWidget.setWidth("100%");
            this.bottomLayout.addComponent(bugListWidget);
        }

    }

    @Override
    public ComponentContainer getWidget() {
        return this;
    }

    private class PeopleInfoComp extends VerticalLayout {
        private static final long serialVersionUID = 1L;

        public void displayEntryPeople(ValuedBean bean) {
            this.removeAllComponents();
            this.setSpacing(true);
            this.setMargin(new MarginInfo(false, false, false, true));

            Label peopleInfoHeader = new Label(
                    AppContext
                            .getMessage(ProjectCommonI18nEnum.SUB_INFO_PEOPLE));
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
                        bean, "createduser");
                String createdUserAvatarId = (String) PropertyUtils
                        .getProperty(bean, "createdUserAvatarId");
                String createdUserDisplayName = (String) PropertyUtils
                        .getProperty(bean, "createdUserFullName");

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
                        bean, "userlead");
                String assignUserAvatarId = (String) PropertyUtils.getProperty(
                        bean, "userLeadAvatarId");
                String assignUserDisplayName = (String) PropertyUtils
                        .getProperty(bean, "userLeadFullName");

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
