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
package com.esofthead.mycollab.module.project.view.milestone;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleMilestone;
import com.esofthead.mycollab.module.project.domain.criteria.MilestoneSearchCriteria;
import com.esofthead.mycollab.module.project.events.MilestoneEvent;
import com.esofthead.mycollab.module.project.i18n.MilestoneI18nEnum;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.MilestoneStatus;
import com.esofthead.mycollab.module.project.service.MilestoneService;
import com.esofthead.mycollab.module.project.ui.components.ProjectViewHeader;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectUserLink;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasMassItemActionHandlers;
import com.esofthead.mycollab.vaadin.events.HasSearchHandlers;
import com.esofthead.mycollab.vaadin.events.HasSelectableItemHandlers;
import com.esofthead.mycollab.vaadin.events.HasSelectionOptionHandlers;
import com.esofthead.mycollab.vaadin.mvp.AbstractLazyPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.mvp.ViewScope;
import com.esofthead.mycollab.vaadin.ui.*;
import com.esofthead.mycollab.vaadin.ui.table.AbstractPagedBeanTable;
import com.esofthead.mycollab.web.CustomLayoutExt;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.hene.popupbutton.PopupButton;
import org.vaadin.maddon.layouts.MHorizontalLayout;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent(scope = ViewScope.PROTOTYPE)
public class MilestoneListViewImpl extends AbstractLazyPageView implements MilestoneListView {
    private static final long serialVersionUID = 1L;

    private CssLayout inProgressContainer;
    private Label inProgressHeader;

    private CssLayout futureContainer;
    private Label futureHeader;

    private CssLayout closeContainer;
    private Label closedHeader;

    private Button createBtn;

    private List<SimpleMilestone> milestones;

    @Override
    protected void displayView() {
        initUI();
        constructBody();

        this.createBtn.setEnabled(CurrentProjectVariables
                .canWrite(ProjectRolePermissionCollections.MILESTONES));

        int totalClosedMilestones = 0, totalInprogressMilestones = 0, totalFutureMilestones = 0;

        for ( SimpleMilestone milestone : milestones) {
            ComponentContainer componentContainer = constructMilestoneBox(milestone);
            if (MilestoneStatus.InProgress.name().equals(milestone.getStatus())) {
                inProgressContainer.addComponent(componentContainer);
                totalInprogressMilestones++;
            } else if (MilestoneStatus.Future.name().equals(
                    milestone.getStatus())) {
                futureContainer.addComponent(componentContainer);
                totalFutureMilestones++;
            } else if (MilestoneStatus.Closed.name().equals(
                    milestone.getStatus())) {
                closeContainer.addComponent(componentContainer);
                totalClosedMilestones++;
            }
        }

        updateClosedMilestoneNumber(totalClosedMilestones);
        updateFutureMilestoneNumber(totalFutureMilestones);
        updateInProgressMilestoneNumber(totalInprogressMilestones);
    }

    @Override
    public void displayMilestones(final List<SimpleMilestone> milestones) {
        this.milestones = milestones;
        this.lazyLoadView();
    }

    private void initUI() {
        Label headerText = new ProjectViewHeader(ProjectTypeConstants.MILESTONE,
                AppContext.getMessage(MilestoneI18nEnum.VIEW_LIST_TITLE));

        MHorizontalLayout header = new MHorizontalLayout()
                .withStyleName("hdr-view").withWidth("100%").withMargin(true)
                .with(headerText, createHeaderRight())
                .withAlign(headerText, Alignment.MIDDLE_LEFT).expand(headerText);
        this.addComponent(header);
    }

    private HorizontalLayout createHeaderRight() {
        final HorizontalLayout layout = new HorizontalLayout();

        createBtn = new Button(
                AppContext.getMessage(MilestoneI18nEnum.BUTTON_NEW_PHASE),
                new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(final ClickEvent event) {
                        EventBusFactory.getInstance().post(
                                new MilestoneEvent.GotoAdd(
                                        MilestoneListViewImpl.this, null));
                    }
                });
        createBtn.setIcon(FontAwesome.PLUS);
        createBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
        createBtn.setEnabled(CurrentProjectVariables
                .canWrite(ProjectRolePermissionCollections.MILESTONES));
        layout.addComponent(createBtn);
        layout.setComponentAlignment(createBtn, Alignment.MIDDLE_RIGHT);

        return layout;
    }

    private void constructBody() {
        CustomLayout bodyContent = CustomLayoutExt.createLayout("milestoneView");

        bodyContent.setWidth("100%");
        bodyContent.setStyleName("milestone-view");

         MHorizontalLayout closedHeaderLayout = new MHorizontalLayout();

        closedHeader = new Label("", ContentMode.HTML);
        closedHeader.setSizeUndefined();
        closedHeaderLayout.with(closedHeader).withAlign(closedHeader, Alignment.MIDDLE_CENTER);

        bodyContent.addComponent(closedHeaderLayout, "closed-header");
        closeContainer = new CssLayout();
        closeContainer.setStyleName("milestone-col");
        closeContainer.setWidth("100%");
        bodyContent.addComponent(closeContainer, "closed-milestones");

         MHorizontalLayout inProgressHeaderLayout = new MHorizontalLayout();
        inProgressHeader = new Label("", ContentMode.HTML);
        inProgressHeader.setSizeUndefined();
        inProgressHeaderLayout.addComponent(inProgressHeader);
        inProgressHeaderLayout.setComponentAlignment(inProgressHeader,
                Alignment.MIDDLE_CENTER);

        bodyContent.addComponent(inProgressHeaderLayout, "in-progress-header");
        inProgressContainer = new CssLayout();
        inProgressContainer.setStyleName("milestone-col");
        inProgressContainer.setWidth("100%");
        bodyContent.addComponent(this.inProgressContainer, "in-progress-milestones");

         MHorizontalLayout futureHeaderLayout = new MHorizontalLayout();
        futureHeader = new Label("", ContentMode.HTML);
        futureHeader.setSizeUndefined();
        futureHeaderLayout.addComponent(futureHeader);
        futureHeaderLayout.setComponentAlignment(futureHeader, Alignment.MIDDLE_CENTER);

        bodyContent.addComponent(futureHeaderLayout, "future-header");
        futureContainer = new CssLayout();
        futureContainer.setStyleName("milestone-col");
        futureContainer.setWidth("100%");
        bodyContent.addComponent(this.futureContainer, "future-milestones");

        this.addComponent(bodyContent);
    }

    private void updateClosedMilestoneNumber(int closeMilestones) {
        closedHeader.setValue(FontAwesome.MINUS.getHtml() + " " +
                AppContext
                        .getMessage(MilestoneI18nEnum.WIDGET_CLOSED_PHASE_TITLE) + " (" + closeMilestones + ")");
    }

    private void updateFutureMilestoneNumber(int futureMilestones) {
        futureHeader.setValue(FontAwesome.CLOCK_O.getHtml() + " " +
                AppContext
                        .getMessage(MilestoneI18nEnum.WIDGET_FUTURE_PHASE_TITLE) + " (" + futureMilestones + ")");
    }

    private void updateInProgressMilestoneNumber(int inProgressMilestones) {
        inProgressHeader.setValue(FontAwesome.SPINNER.getHtml() + " " +
                AppContext
                        .getMessage(MilestoneI18nEnum.WIDGET_INPROGRESS_PHASE_TITLE) + " (" + inProgressMilestones +
                ")");
    }

    private ComponentContainer constructMilestoneBox(
            final SimpleMilestone milestone) {
        CssLayout layout = new CssLayout();
        layout.addStyleName(UIConstants.MILESTONE_BOX);
        layout.setWidth("100%");

        LabelLink milestoneLink = new LabelLink(milestone.getName(),
                ProjectLinkBuilder.generateMilestonePreviewFullLink(
                        milestone.getProjectid(), milestone.getId()));
        milestoneLink.setStyleName("link");
        milestoneLink.addStyleName("bold");
        milestoneLink.addStyleName(UIConstants.WORD_WRAP);
        milestoneLink.addStyleName("milestone-name");
        milestoneLink.setWidth("100%");

        MHorizontalLayout milestoneHeader = new MHorizontalLayout()
                .withWidth("100%").with(milestoneLink).expand(milestoneLink);

        PopupButton taskSettingPopupBtn = new PopupButton();
        taskSettingPopupBtn.setWidth("20px");
        OptionPopupContent filterBtnLayout = new OptionPopupContent().withWidth("100px");

        Button editButton = new Button(
                AppContext.getMessage(GenericI18Enum.BUTTON_EDIT),
                new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(ClickEvent event) {
                        EventBusFactory.getInstance().post(
                                new MilestoneEvent.GotoEdit(
                                        MilestoneListViewImpl.this, milestone));
                    }
                });
        editButton.setEnabled(CurrentProjectVariables
                .canWrite(ProjectRolePermissionCollections.MILESTONES));
        editButton.setIcon(FontAwesome.EDIT);
        filterBtnLayout.addOption(editButton);

        Button deleteBtn = new Button(
                AppContext.getMessage(GenericI18Enum.BUTTON_DELETE),
                new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(ClickEvent event) {
                        ConfirmDialogExt.show(
                                UI.getCurrent(),
                                AppContext.getMessage(
                                        GenericI18Enum.DIALOG_DELETE_TITLE,
                                        SiteConfiguration.getSiteName()),
                                AppContext
                                        .getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                                AppContext
                                        .getMessage(GenericI18Enum.BUTTON_YES),
                                AppContext.getMessage(GenericI18Enum.BUTTON_NO),
                                new ConfirmDialog.Listener() {
                                    private static final long serialVersionUID = 1L;

                                    @Override
                                    public void onClose(ConfirmDialog dialog) {
                                        if (dialog.isConfirmed()) {
                                            MilestoneService projectTaskService = ApplicationContextUtil
                                                    .getSpringBean(MilestoneService.class);
                                            projectTaskService.removeWithSession(
                                                    milestone.getId(),
                                                    AppContext.getUsername(),
                                                    AppContext.getAccountId());
                                            milestones.remove(milestone);
                                            displayMilestones(milestones);
                                        }
                                    }
                                });
                    }
                });
        deleteBtn.setIcon(FontAwesome.TRASH_O);
        deleteBtn.setEnabled(CurrentProjectVariables
                .canAccess(ProjectRolePermissionCollections.MILESTONES));
        filterBtnLayout.addOption(deleteBtn);

        taskSettingPopupBtn.setIcon(FontAwesome.COG);
        taskSettingPopupBtn.addStyleName(UIConstants.BUTTON_ICON_ONLY);
        taskSettingPopupBtn.setContent(filterBtnLayout);

        milestoneHeader.addComponent(taskSettingPopupBtn);
        layout.addComponent(milestoneHeader);

        MHorizontalLayout spacing = new MHorizontalLayout().withHeight("8px").withWidth("100%");
        layout.addComponent(spacing);

        GridFormLayoutHelper layoutHelper = new GridFormLayoutHelper(1, 5, "100%", "80px");
        layoutHelper.addComponent(
                new ELabel(AppContext.formatPrettyTime(milestone.getStartdate())).withDescription(AppContext.formatDate
                        (milestone.getStartdate())),
                AppContext.getMessage(MilestoneI18nEnum.FORM_START_DATE_FIELD),
                0, 0, Alignment.MIDDLE_LEFT);
        layoutHelper.addComponent(
                new ELabel(AppContext.formatPrettyTime(milestone.getEnddate())).withDescription(AppContext.formatDate
                        (milestone.getEnddate())),
                AppContext.getMessage(MilestoneI18nEnum.FORM_END_DATE_FIELD),
                0, 1, Alignment.MIDDLE_LEFT);

        CssLayout linkWrapper = new CssLayout();
        linkWrapper.setWidth("100%");
        linkWrapper.addComponent(new ProjectUserLink(milestone.getOwner(),
                milestone.getOwnerAvatarId(), milestone.getOwnerFullName()));
        layoutHelper.addComponent(linkWrapper,
                AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE), 0, 2,
                Alignment.MIDDLE_LEFT);

        ProgressBarIndicator progressTask = new ProgressBarIndicator(
                milestone.getNumTasks(), milestone.getNumOpenTasks());
        progressTask.setWidth("100%");

        layoutHelper.addComponent(progressTask,
                AppContext.getMessage(MilestoneI18nEnum.FORM_TASK_FIELD), 0, 3,
                Alignment.MIDDLE_LEFT);

        ProgressBarIndicator progressBug = new ProgressBarIndicator(
                milestone.getNumBugs(), milestone.getNumOpenBugs());
        progressBug.setWidth("100%");

        layoutHelper.addComponent(progressBug,
                AppContext.getMessage(MilestoneI18nEnum.FORM_BUG_FIELD), 0, 4,
                Alignment.MIDDLE_LEFT);
        GridLayout milestoneInfoLayout = layoutHelper.getLayout();
        milestoneInfoLayout.setWidth("100%");
        milestoneInfoLayout.setMargin(false);
        milestoneInfoLayout.setSpacing(true);
        layout.addComponent(milestoneInfoLayout);

        return layout;
    }

    @Override
    public void enableActionControls(int numOfSelectedItem) {
        throw new UnsupportedOperationException(
                "This view doesn't support this operation");
    }

    @Override
    public void disableActionControls() {
        throw new UnsupportedOperationException(
                "This view doesn't support this operation");
    }

    @Override
    public HasSearchHandlers<MilestoneSearchCriteria> getSearchHandlers() {
        throw new UnsupportedOperationException(
                "This view doesn't support this operation");
    }

    @Override
    public HasSelectionOptionHandlers getOptionSelectionHandlers() {
        throw new UnsupportedOperationException(
                "This view doesn't support this operation");
    }

    @Override
    public HasMassItemActionHandlers getPopupActionHandlers() {
        throw new UnsupportedOperationException(
                "This view doesn't support this operation");
    }

    @Override
    public HasSelectableItemHandlers<SimpleMilestone> getSelectableItemHandlers() {
        throw new UnsupportedOperationException(
                "This view doesn't support this operation");
    }

    @Override
    public AbstractPagedBeanTable<MilestoneSearchCriteria, SimpleMilestone> getPagedBeanTable() {
        throw new UnsupportedOperationException(
                "This view doesn't support this operation");
    }
}
