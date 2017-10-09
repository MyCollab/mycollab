package com.mycollab.community.module.project.view.service;

import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Span;
import com.mycollab.common.i18n.FollowerI18nEnum;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.ShellI18nEnum;
import com.mycollab.community.vaadin.web.ui.field.MetaFieldBuilder;
import com.mycollab.core.SecureAccessException;
import com.mycollab.core.utils.NumberUtils;
import com.mycollab.module.file.StorageUtils;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.domain.SimpleTask;
import com.mycollab.module.project.i18n.*;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.view.bug.BugEditForm;
import com.mycollab.module.project.view.service.TicketComponentFactory;
import com.mycollab.module.project.view.task.TaskEditForm;
import com.mycollab.module.tracker.domain.SimpleBug;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.vaadin.teemu.VaadinIcons;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

import java.util.Date;

/**
 * @author MyCollab Ltd
 * @since 5.4.3
 */
@Service
public class TicketComponentFactoryImpl implements TicketComponentFactory {
    @Override
    public AbstractComponent createStartDatePopupField(ProjectTicket assignment) {
        if (assignment.getStartDate() == null) {
            Div divHint = new Div().setCSSClass("nonValue");
            divHint.appendText(VaadinIcons.TIME_FORWARD.getHtml());
            divHint.appendChild(new Span().appendText(UserUIContext.getMessage(GenericI18Enum.OPT_UNDEFINED)).setCSSClass("hide"));
            return new MetaFieldBuilder().withCaption(divHint.write())
                    .withDescription(UserUIContext.getMessage(ShellI18nEnum.OPT_UPGRADE_PRO_INTRO,
                            UserUIContext.getMessage(GenericI18Enum.FORM_START_DATE))).build();
        } else {
            return new MetaFieldBuilder().withCaption(String.format(" %s %s", VaadinIcons.TIME_FORWARD.getHtml(),
                    UserUIContext.formatDate(assignment.getStartDate())))
                    .withDescription(UserUIContext.getMessage(ShellI18nEnum.OPT_UPGRADE_PRO_INTRO,
                            UserUIContext.getMessage(GenericI18Enum.FORM_START_DATE))).build();
        }
    }

    @Override
    public AbstractComponent createEndDatePopupField(ProjectTicket assignment) {
        if (assignment.getEndDate() == null) {
            Div divHint = new Div().setCSSClass("nonValue");
            divHint.appendText(VaadinIcons.TIME_BACKWARD.getHtml());
            divHint.appendChild(new Span().appendText(UserUIContext.getMessage(GenericI18Enum.OPT_UNDEFINED)).setCSSClass("hide"));
            return new MetaFieldBuilder().withCaption(divHint.write()).withDescription(UserUIContext.getMessage(ShellI18nEnum.OPT_UPGRADE_PRO_INTRO,
                    UserUIContext.getMessage(GenericI18Enum.FORM_END_DATE))).build();
        } else {
            return new MetaFieldBuilder().withCaption(String.format(" %s %s", VaadinIcons.TIME_BACKWARD.getHtml(),
                    UserUIContext.formatDate(assignment.getEndDate())))
                    .withDescription(UserUIContext.getMessage(ShellI18nEnum.OPT_UPGRADE_PRO_INTRO,
                            UserUIContext.getMessage(GenericI18Enum.FORM_END_DATE))).build();
        }
    }

    @Override
    public AbstractComponent createDueDatePopupField(ProjectTicket assignment) {
        if (assignment.getDueDatePlusOne() == null) {
            Div divHint = new Div().setCSSClass("nonValue");
            divHint.appendText(FontAwesome.CLOCK_O.getHtml());
            divHint.appendChild(new Span().appendText(UserUIContext.getMessage(GenericI18Enum.OPT_UNDEFINED)).setCSSClass("hide"));
            return new MetaFieldBuilder().withCaption(divHint.write())
                    .withDescription(UserUIContext.getMessage(ShellI18nEnum.OPT_UPGRADE_PRO_INTRO,
                            UserUIContext.getMessage(GenericI18Enum.FORM_DUE_DATE))).build();
        } else {
            return new MetaFieldBuilder().withCaption(String.format(" %s %s", FontAwesome.CLOCK_O.getHtml(),
                    UserUIContext.formatPrettyTime(assignment.getDueDatePlusOne())))
                    .withDescription(UserUIContext.getMessage(ShellI18nEnum.OPT_UPGRADE_PRO_INTRO,
                            UserUIContext.getMessage(GenericI18Enum.FORM_DUE_DATE))).build();
        }
    }

    @Override
    public AbstractComponent createPriorityPopupField(ProjectTicket assignment) {
        return new MetaFieldBuilder().withCaption(ProjectAssetsManager.getPriorityHtml(assignment.getPriority()) + " " +
                UserUIContext.getMessage(OptionI18nEnum.Priority.class, assignment.getPriority()))
                .withDescription(UserUIContext.getMessage(ShellI18nEnum.OPT_UPGRADE_PRO_INTRO,
                        UserUIContext.getMessage(GenericI18Enum.FORM_PRIORITY))).build();
    }

    @Override
    public AbstractComponent createBillableHoursPopupField(ProjectTicket task) {
        return null;
    }

    @Override
    public AbstractComponent createNonBillableHoursPopupField(ProjectTicket task) {
        return null;
    }

    @Override
    public AbstractComponent createFollowersPopupField(ProjectTicket assignment) {
        return new MetaFieldBuilder().withCaptionAndIcon(FontAwesome.EYE, "" + NumberUtils.zeroIfNull(assignment.getNumFollowers()))
                .withDescription(UserUIContext.getMessage(ShellI18nEnum.OPT_UPGRADE_PRO_INTRO,
                        UserUIContext.getMessage(FollowerI18nEnum.OPT_SUB_INFO_WATCHERS))).build();
    }

    @Override
    public AbstractComponent createCommentsPopupField(ProjectTicket assignment) {
        return new MetaFieldBuilder().withCaption(FontAwesome.COMMENT_O.getHtml() + " " + NumberUtils.zeroIfNull(assignment.getNumComments()))
                .withDescription(UserUIContext.getMessage(ShellI18nEnum.OPT_UPGRADE_PRO_INTRO,
                        UserUIContext.getMessage(GenericI18Enum.OPT_COMMENTS))).build();
    }

    @Override
    public AbstractComponent createStatusPopupField(ProjectTicket assignment) {
        return new MetaFieldBuilder().withCaptionAndIcon(FontAwesome.INFO_CIRCLE, assignment.getStatus()).withDescription
                (UserUIContext.getMessage(ShellI18nEnum.OPT_UPGRADE_PRO_INTRO,
                        UserUIContext.getMessage(GenericI18Enum.FORM_STATUS))).build();
    }

    @Override
    public AbstractComponent createAssigneePopupField(ProjectTicket ticket) {
        String avatarLink = StorageUtils.getAvatarPath(ticket.getAssignUserAvatarId(), 16);
        Img img = new Img(ticket.getAssignUserFullName(), avatarLink).setCSSClass(UIConstants.CIRCLE_BOX)
                .setTitle(ticket.getAssignUserFullName());
        return new MetaFieldBuilder().withCaption(img.write())
                .withDescription(UserUIContext.getMessage(ShellI18nEnum.OPT_UPGRADE_PRO_INTRO,
                        UserUIContext.getMessage(GenericI18Enum.FORM_ASSIGNEE))).build();
    }

    @Override
    public MWindow createNewTicketWindow(Date date, Integer prjId, Integer milestoneId, boolean isIncludeMilestone) {
        return new NewTicketWindow(date, prjId, milestoneId, isIncludeMilestone);
    }

    private static class NewTicketWindow extends MWindow {

        private ComboBox typeSelection;
        private CssLayout formLayout;

        NewTicketWindow(Date date, final Integer prjId, final Integer milestoneId, boolean isIncludeMilestone) {
            super(UserUIContext.getMessage(TicketI18nEnum.NEW));
            MVerticalLayout content = new MVerticalLayout();
            withModal(true).withResizable(false).withCenter().withWidth("1200px").withContent(content);

            typeSelection = new ComboBox();
            typeSelection.setItemCaptionMode(AbstractSelect.ItemCaptionMode.EXPLICIT_DEFAULTS_ID);
            if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS)) {
                typeSelection.addItem(UserUIContext.getMessage(TaskI18nEnum.SINGLE));
                typeSelection.setItemIcon(UserUIContext.getMessage(TaskI18nEnum.SINGLE), ProjectAssetsManager.getAsset(ProjectTypeConstants.TASK));
            }

            if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.BUGS)) {
                typeSelection.addItem(UserUIContext.getMessage(BugI18nEnum.SINGLE));
                typeSelection.setItemIcon(UserUIContext.getMessage(BugI18nEnum.SINGLE), ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG));
            }

            if (isIncludeMilestone && CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.MILESTONES)) {
                typeSelection.addItem(UserUIContext.getMessage(MilestoneI18nEnum.SINGLE));
                typeSelection.setItemIcon(UserUIContext.getMessage(MilestoneI18nEnum.SINGLE), ProjectAssetsManager.getAsset(ProjectTypeConstants.MILESTONE));
            }

            typeSelection.setNullSelectionAllowed(false);
            if (CollectionUtils.isNotEmpty(typeSelection.getItemIds())) {
                typeSelection.select(typeSelection.getItemIds().iterator().next());
            } else {
                throw new SecureAccessException();
            }

            typeSelection.setNullSelectionAllowed(false);
            typeSelection.addValueChangeListener(valueChangeEvent -> doChange(date, prjId, milestoneId));

            GridFormLayoutHelper formLayoutHelper = GridFormLayoutHelper.defaultFormLayoutHelper(1, 1);
            formLayoutHelper.addComponent(typeSelection, UserUIContext.getMessage(GenericI18Enum.FORM_TYPE), 0, 0);
            formLayout = new CssLayout();
            formLayout.setWidth("100%");
            content.with(formLayoutHelper.getLayout(), formLayout);
            doChange(date, prjId, milestoneId);
        }

        private void doChange(Date dateValue, final Integer prjId, final Integer milestoneId) {
            formLayout.removeAllComponents();
            String value = (String) typeSelection.getValue();
            if (UserUIContext.getMessage(TaskI18nEnum.SINGLE).equals(value)) {
                SimpleTask task = new SimpleTask();
                task.setProjectid(prjId);
                task.setMilestoneid(milestoneId);
                task.setSaccountid(AppUI.getAccountId());
                task.setCreateduser(UserUIContext.getUsername());
                task.setStartdate(dateValue);
                TaskEditForm editForm = new TaskEditForm() {
                    @Override
                    protected void postExecution() {
                        close();
                    }
                };
                editForm.setBean(task);
                formLayout.addComponent(editForm);
            } else if (UserUIContext.getMessage(BugI18nEnum.SINGLE).equals(value)) {
                SimpleBug bug = new SimpleBug();
                bug.setProjectid(prjId);
                bug.setSaccountid(AppUI.getAccountId());
                bug.setStartdate(dateValue);
                bug.setMilestoneid(milestoneId);
                bug.setCreateduser(UserUIContext.getUsername());
                BugEditForm editForm = new BugEditForm() {
                    @Override
                    protected void postExecution() {
                        close();
                    }
                };
                editForm.setBean(bug);
                formLayout.addComponent(editForm);
            }
        }
    }
}
