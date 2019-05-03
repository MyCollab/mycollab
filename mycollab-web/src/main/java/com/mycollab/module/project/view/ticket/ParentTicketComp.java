package com.mycollab.module.project.view.ticket;

import com.mycollab.core.MyCollabException;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.domain.SimpleTask;
import com.mycollab.module.project.i18n.TaskI18nEnum;
import com.mycollab.module.project.service.TaskService;
import com.mycollab.module.project.view.task.ToggleTaskSummaryField;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd
 * @since 7.0.3
 */
public class ParentTicketComp extends MHorizontalLayout {
    public ParentTicketComp(String parentTicketType, Integer parentTicketId, ProjectTicket childTask) {
        ELabel titleLbl = new ELabel(UserUIContext.getMessage(TaskI18nEnum.FORM_PARENT_TASK)).withStyleName(WebThemes.ARROW_BTN)
                .withUndefinedWidth();
        with(titleLbl);

        if (ProjectTypeConstants.TASK.equals(parentTicketType)) {
            TaskService taskService = AppContextUtil.getSpringBean(TaskService.class);
            SimpleTask parentTask = taskService.findById(parentTicketId, AppUI.getAccountId());
            if (parentTask != null) {
                ToggleTaskSummaryField toggleTaskSummaryField = new ToggleTaskSummaryField(parentTask, false);
                MButton unlinkBtn = new MButton("", clickEvent -> {
                    // TODO
//            childTask.setParenttaskid(null);
//                    TaskService taskService = AppContextUtil.getSpringBean(TaskService.class);
//                    taskService.updateWithSession(childTask, UserUIContext.getUsername());
//                    UIUtils.removeChildAssociate(ToggleTaskSummaryWithChildRelationshipField.this, RemoveInlineComponentMarker.class);
                }).withIcon(VaadinIcons.UNLINK).withStyleName(ValoTheme.BUTTON_ICON_ONLY, ValoTheme.BUTTON_ICON_ALIGN_TOP)
                        .withDescription(UserUIContext.getMessage(TaskI18nEnum.OPT_REMOVE_PARENT_CHILD_RELATIONSHIP));

                toggleTaskSummaryField.addControl(unlinkBtn);
                with(toggleTaskSummaryField);
            }
        } else {
            throw new MyCollabException("Not support parent ticket type " + parentTicketType + " yet");
        }

    }
}
