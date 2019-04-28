/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.community.module.project.view.service;

import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Span;
import com.mycollab.common.i18n.FollowerI18nEnum;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.ShellI18nEnum;
import com.mycollab.community.vaadin.web.ui.field.MetaFieldBuilder;
import com.mycollab.core.utils.NumberUtils;
import com.mycollab.form.view.LayoutType;
import com.mycollab.module.file.StorageUtils;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.*;
import com.mycollab.module.project.i18n.*;
import com.mycollab.module.project.i18n.OptionI18nEnum.Priority;
import com.mycollab.module.project.service.ProjectMemberService;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.ui.components.UserProjectComboBox;
import com.mycollab.module.project.view.bug.BugEditForm;
import com.mycollab.module.project.view.service.TicketComponentFactory;
import com.mycollab.module.project.view.task.TaskEditForm;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.IconGenerator;
import org.springframework.stereotype.Service;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
            divHint.appendText(VaadinIcons.CLOCK.getHtml());
            divHint.appendChild(new Span().appendText(UserUIContext.getMessage(GenericI18Enum.OPT_UNDEFINED)).setCSSClass("hide"));
            return new MetaFieldBuilder().withCaption(divHint.write())
                    .withDescription(UserUIContext.getMessage(ShellI18nEnum.OPT_UPGRADE_PRO_INTRO,
                            UserUIContext.getMessage(GenericI18Enum.FORM_DUE_DATE))).build();
        } else {
            return new MetaFieldBuilder().withCaption(String.format(" %s %s", VaadinIcons.CLOCK.getHtml(),
                    UserUIContext.formatPrettyTime(assignment.getDueDatePlusOne())))
                    .withDescription(UserUIContext.getMessage(ShellI18nEnum.OPT_UPGRADE_PRO_INTRO,
                            UserUIContext.getMessage(GenericI18Enum.FORM_DUE_DATE))).build();
        }
    }

    @Override
    public AbstractComponent createPriorityPopupField(ProjectTicket ticket) {
        return new MetaFieldBuilder().withCaption(ProjectAssetsManager.getPriorityHtml(ticket.getPriority()) + " " +
                UserUIContext.getMessage(Priority.class, ticket.getPriority()))
                .withDescription(UserUIContext.getMessage(ShellI18nEnum.OPT_UPGRADE_PRO_INTRO,
                        UserUIContext.getMessage(GenericI18Enum.FORM_PRIORITY))).build();
    }

    @Override
    public AbstractComponent createBillableHoursPopupField(ProjectTicket ticket) {
        return null;
    }

    @Override
    public AbstractComponent createNonBillableHoursPopupField(ProjectTicket ticket) {
        return null;
    }

    @Override
    public AbstractComponent createFollowersPopupField(ProjectTicket ticket) {
        return new MetaFieldBuilder().withCaptionAndIcon(VaadinIcons.EYE, "" + NumberUtils.zeroIfNull(ticket.getNumFollowers()))
                .withDescription(UserUIContext.getMessage(ShellI18nEnum.OPT_UPGRADE_PRO_INTRO,
                        UserUIContext.getMessage(FollowerI18nEnum.OPT_SUB_INFO_WATCHERS))).build();
    }

    @Override
    public AbstractComponent createCommentsPopupField(ProjectTicket assignment) {
        return new MetaFieldBuilder().withCaption(VaadinIcons.COMMENT_O.getHtml() + " " + NumberUtils.zeroIfNull(assignment.getNumComments()))
                .withDescription(UserUIContext.getMessage(ShellI18nEnum.OPT_UPGRADE_PRO_INTRO,
                        UserUIContext.getMessage(GenericI18Enum.OPT_COMMENTS))).build();
    }

    @Override
    public AbstractComponent createStatusPopupField(ProjectTicket assignment) {
        return new MetaFieldBuilder().withCaptionAndIcon(VaadinIcons.INFO_CIRCLE, assignment.getStatus()).withDescription
                (UserUIContext.getMessage(ShellI18nEnum.OPT_UPGRADE_PRO_INTRO,
                        UserUIContext.getMessage(GenericI18Enum.FORM_STATUS))).build();
    }

    @Override
    public AbstractComponent createAssigneePopupField(ProjectTicket ticket) {
        String avatarLink = StorageUtils.getAvatarPath(ticket.getAssignUserAvatarId(), 16);
        Img img = new Img(ticket.getAssignUserFullName(), avatarLink).setCSSClass(WebThemes.CIRCLE_BOX)
                .setTitle(ticket.getAssignUserFullName());
        return new MetaFieldBuilder().withCaption(img.write())
                .withDescription(UserUIContext.getMessage(ShellI18nEnum.OPT_UPGRADE_PRO_INTRO,
                        UserUIContext.getMessage(GenericI18Enum.FORM_ASSIGNEE))).build();
    }

    @Override
    public MWindow createNewTicketWindow(LocalDate date, Integer prjId, Integer milestoneId, boolean isIncludeMilestone) {
        return new NewTicketWindow(date, prjId, milestoneId, isIncludeMilestone);
    }

    private static class NewTicketWindow extends MWindow {
        private ComboBox<String> typeSelection;
        private MCssLayout formLayout;
        private boolean isIncludeMilestone;
        private SimpleProject selectedProject;

        NewTicketWindow(LocalDate date, Integer projectId, Integer milestoneId, boolean isIncludeMilestone) {
            super(UserUIContext.getMessage(TicketI18nEnum.NEW));
            this.isIncludeMilestone = isIncludeMilestone;
            this.addStyleName(WebThemes.NO_SCROLLABLE_CONTAINER);
            MVerticalLayout content = new MVerticalLayout();
            withModal(true).withResizable(false).withCenter().withWidth(WebThemes.WINDOW_FORM_WIDTH).withContent(content);

            UserProjectComboBox projectListSelect = new UserProjectComboBox();
            projectListSelect.setEmptySelectionAllowed(false);
            selectedProject = projectListSelect.setSelectedProjectById(projectId);

            typeSelection = new ComboBox<>();
            typeSelection.setWidth(WebThemes.FORM_CONTROL_WIDTH);
            typeSelection.setEmptySelectionAllowed(false);

            projectListSelect.addValueChangeListener(valueChangeEvent -> {
                selectedProject = projectListSelect.getValue();
                loadAssociateTicketTypePerProject();
            });

            loadAssociateTicketTypePerProject();
            typeSelection.addValueChangeListener(event -> doChange(date, milestoneId));

            GridFormLayoutHelper formLayoutHelper = GridFormLayoutHelper.defaultFormLayoutHelper(LayoutType.TWO_COLUMN);
            formLayoutHelper.addComponent(projectListSelect, UserUIContext.getMessage(ProjectI18nEnum.SINGLE), 0, 0);
            formLayoutHelper.addComponent(typeSelection, UserUIContext.getMessage(GenericI18Enum.FORM_TYPE), 1, 0);
            formLayoutHelper.getLayout().addStyleName(WebThemes.BORDER_BOTTOM);

            formLayout = new MCssLayout().withFullWidth();
            content.with(formLayoutHelper.getLayout(), formLayout);
            doChange(date, milestoneId);
        }

        private void loadAssociateTicketTypePerProject() {
            typeSelection.clear();
            List<String> ticketTypes = new ArrayList<>();

            if (UserUIContext.isAdmin()) {
                if (isIncludeMilestone) {
                    ticketTypes.add(UserUIContext.getMessage(MilestoneI18nEnum.SINGLE));
                }
                ticketTypes.add(UserUIContext.getMessage(TaskI18nEnum.SINGLE));
                ticketTypes.add(UserUIContext.getMessage(BugI18nEnum.SINGLE));

            } else {
                ProjectMemberService projectMemberService = AppContextUtil.getSpringBean(ProjectMemberService.class);
                SimpleProjectMember member = projectMemberService.findMemberByUsername(UserUIContext.getUsername(), selectedProject.getId(), AppUI.getAccountId());

                if (member != null) {
                    if (isIncludeMilestone && (member.canWrite(ProjectRolePermissionCollections.MILESTONES))) {
                        ticketTypes.add(UserUIContext.getMessage(MilestoneI18nEnum.SINGLE));
                    }

                    if (member.canWrite(ProjectRolePermissionCollections.TASKS)) {
                        ticketTypes.add(UserUIContext.getMessage(TaskI18nEnum.SINGLE));
                    }

                    if (member.canWrite(ProjectRolePermissionCollections.BUGS)) {
                        ticketTypes.add(UserUIContext.getMessage(BugI18nEnum.SINGLE));
                    }
                }
            }


            if (ticketTypes.size() > 0) {
                typeSelection.setItems(ticketTypes);
                typeSelection.setValue(ticketTypes.get(0));
                typeSelection.setItemIconGenerator((IconGenerator<String>) item -> {
                    if (item.equals(UserUIContext.getMessage(TaskI18nEnum.SINGLE))) {
                        return ProjectAssetsManager.getAsset(ProjectTypeConstants.TASK);
                    } else if (item.equals(UserUIContext.getMessage(BugI18nEnum.SINGLE))) {
                        return ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG);
                    } else if (item.equals(UserUIContext.getMessage(MilestoneI18nEnum.SINGLE))) {
                        return ProjectAssetsManager.getAsset(ProjectTypeConstants.MILESTONE);
                    } else {
                        throw new IllegalArgumentException();
                    }
                });
            }
        }

        private void doChange(LocalDate dateValue, Integer milestoneId) {
            formLayout.removeAllComponents();
            String value = typeSelection.getValue();
            if (UserUIContext.getMessage(TaskI18nEnum.SINGLE).equals(value)) {
                SimpleTask task = new SimpleTask();
                task.setProjectid(selectedProject.getId());
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
                bug.setProjectid(selectedProject.getId());
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
