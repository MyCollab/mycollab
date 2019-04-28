package com.mycollab.module.project.view.ticket;

import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Span;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.file.StorageUtils;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.service.ProjectTicketService;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.IgnoreBindingField;
import com.mycollab.vaadin.ui.VerticalRemoveInlineComponentMarker;
import com.mycollab.vaadin.web.ui.OptionPopupContent;
import com.mycollab.vaadin.web.ui.SplitButton;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.apache.commons.collections.CollectionUtils;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.List;

public class SubTicketsComp extends IgnoreBindingField {
    private static final long serialVersionUID = 1L;

//        private ApplicationEventListener<TaskEvent.NewTaskAdded> newTaskAddedHandler = new
//                ApplicationEventListener<TaskEvent.NewTaskAdded>() {
//                    @Override
//                    @Subscribe
//                    public void handle(TaskEvent.NewTaskAdded event) {
//                        final TaskService taskService = AppContextUtil.getSpringBean(TaskService.class);
//                        SimpleTask task = taskService.findById(event.getData(), AppUI.getAccountId());
//                        if (task != null && tasksLayout != null) {
//                            tasksLayout.addComponent(generateSubTicketContent(task), 0);
//                        }
//                    }
//                };

    private MVerticalLayout ticketsLayout;
    private ProjectTicket parentTicket;

    public SubTicketsComp(ProjectTicket parentTicket) {
        this.parentTicket = parentTicket;
    }

    @Override
    public void attach() {
//            EventBusFactory.getInstance().register(newTaskAddedHandler);
        super.attach();
    }

    @Override
    public void detach() {
//            EventBusFactory.getInstance().unregister(newTaskAddedHandler);
        super.detach();
    }

    @Override
    protected Component initContent() {
        MHorizontalLayout contentLayout = new MHorizontalLayout().withFullWidth();
        ticketsLayout = new VerticalRemoveInlineComponentMarker().withFullWidth().withMargin(new MarginInfo(false, true, true, false));
        contentLayout.with(ticketsLayout).expand(ticketsLayout);

        if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS)) {
            MButton addNewTaskBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_ADD), clickEvent -> {
//                SimpleTask task = new SimpleTask();
//                task.setMilestoneid(beanItem.getMilestoneid());
                // TODO
//                    task.setParenttaskid(beanItem.getId());
//                task.setPriority(OptionI18nEnum.Priority.Medium.name());
//                task.setProjectid(beanItem.getProjectid());
//                task.setSaccountid(beanItem.getSaccountid());
//                UI.getCurrent().addWindow(new TaskAddWindow(task));
            }).withStyleName(WebThemes.BUTTON_ACTION).withIcon(VaadinIcons.PLUS);

            SplitButton splitButton = new SplitButton(addNewTaskBtn);
            splitButton.setWidthUndefined();
            splitButton.addStyleName(WebThemes.BUTTON_ACTION);

            OptionPopupContent popupButtonsControl = new OptionPopupContent();
            Button selectBtn = new Button(UserUIContext.getMessage(GenericI18Enum.BUTTON_SELECT), clickEvent -> {
                splitButton.setPopupVisible(false);
                UI.getCurrent().addWindow(new SelectChildTicketWindow(parentTicket));
            });
            popupButtonsControl.addOption(selectBtn);
            splitButton.setContent(popupButtonsControl);
            contentLayout.addComponent(splitButton);
        }

        ProjectTicketService projectTicketService = AppContextUtil.getSpringBean(ProjectTicketService.class);
        List<ProjectTicket> subTickets = projectTicketService.findSubTickets(ProjectTypeConstants.TASK, parentTicket.getTypeId());
        if (CollectionUtils.isNotEmpty(subTickets)) {
            for (ProjectTicket subTicket : subTickets) {
                ticketsLayout.addComponent(generateSubTicketContent(subTicket));
            }
        }
        return contentLayout;
    }

    @Override
    protected void doSetValue(Object o) {

    }

    @Override
    public Object getValue() {
        return null;
    }

    private HorizontalLayout generateSubTicketContent(ProjectTicket subTicket) {
        MHorizontalLayout layout = new MHorizontalLayout().withStyleName(WebThemes.HOVER_EFFECT_NOT_BOX);
        layout.setDefaultComponentAlignment(Alignment.TOP_LEFT);

        CheckBox checkBox = new CheckBox("", subTicket.isClosed());
        checkBox.setVisible(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS));
        layout.with(checkBox);

        Span priorityLink = new Span().appendText(ProjectAssetsManager.getPriorityHtml(subTicket.getPriority()))
                .setTitle(subTicket.getPriority());
        layout.with(ELabel.html(priorityLink.write()).withUndefinedWidth());

        String taskStatus = UserUIContext.getMessage(com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum.class, subTicket.getStatus());
        ELabel statusLbl = new ELabel(taskStatus).withStyleName(WebThemes.FIELD_NOTE).withUndefinedWidth();
        layout.with(statusLbl);

        String avatarLink = StorageUtils.getAvatarPath(subTicket.getAssignUserAvatarId(), 16);
        Img avatarImg = new Img(subTicket.getAssignUserFullName(), avatarLink).setCSSClass(WebThemes.CIRCLE_BOX)
                .setTitle(subTicket.getAssignUserFullName());
        layout.with(ELabel.html(avatarImg.write()).withUndefinedWidth());

        ToggleTicketSummaryWithParentRelationshipField toggleTaskSummaryField = new ToggleTicketSummaryWithParentRelationshipField(parentTicket, subTicket);
        layout.with(toggleTaskSummaryField).expand(toggleTaskSummaryField);

//            checkBox.addValueChangeListener(valueChangeEvent -> {
//                Boolean selectedFlag = checkBox.getValue();
//                TaskService taskService = AppContextUtil.getSpringBean(TaskService.class);
//                if (selectedFlag) {
//                    statusLbl.setValue(UserUIContext.getMessage(StatusI18nEnum.class, StatusI18nEnum.Closed.name()));
//                    subTicket.setStatus(StatusI18nEnum.Closed.name());
//                    subTicket.setPercentagecomplete(100d);
//                    toggleTaskSummaryField.closeTask();
//                } else {
//                    statusLbl.setValue(UserUIContext.getMessage(StatusI18nEnum.class, StatusI18nEnum.Open.name()));
//                    subTicket.setStatus(StatusI18nEnum.Open.name());
//                    subTicket.setPercentagecomplete(0d);
//                    if (subTicket.isOverdue()) {
//                        toggleTaskSummaryField.overdueTask();
//                    } else {
//                        toggleTaskSummaryField.reOpenTask();
//                    }
//                }
//                taskService.updateSelectiveWithSession(subTicket, UserUIContext.getUsername());
//                toggleTaskSummaryField.updateLabel();
//            });
        return layout;
    }
}
