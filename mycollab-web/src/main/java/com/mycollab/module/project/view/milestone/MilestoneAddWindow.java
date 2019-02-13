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
package com.mycollab.module.project.view.milestone;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.file.AttachmentUtils;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.Milestone;
import com.mycollab.module.project.domain.SimpleMilestone;
import com.mycollab.module.project.event.MilestoneEvent;
import com.mycollab.module.project.event.TicketEvent;
import com.mycollab.module.project.i18n.MilestoneI18nEnum;
import com.mycollab.module.project.service.MilestoneService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.AdvancedEditBeanForm;
import com.mycollab.vaadin.web.ui.DefaultDynaFormLayout;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.mycollab.vaadin.web.ui.field.AttachmentUploadField;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MWindow;

/**
 * @author MyCollab Ltd
 * @since 5.2.0
 */
public class MilestoneAddWindow extends MWindow {
    public MilestoneAddWindow(final SimpleMilestone milestone) {
        if (milestone.getId() == null) {
            setCaption(UserUIContext.getMessage(MilestoneI18nEnum.NEW));
        } else {
            setCaption(UserUIContext.getMessage(MilestoneI18nEnum.SINGLE) + ": " + milestone.getName());
        }
        VerticalLayout content = new VerticalLayout();
        withWidth("800px").withModal(true).withResizable(false).withContent(content).withCenter();
        AdvancedEditBeanForm<SimpleMilestone> editForm = new AdvancedEditBeanForm<>();
        content.addComponent(editForm);
        editForm.setFormLayoutFactory(new DefaultDynaFormLayout(ProjectTypeConstants.MILESTONE,
                MilestoneDefaultFormLayoutFactory.getAddForm(), Milestone.Field.id.name()));
        MilestoneEditFormFieldFactory milestoneEditFormFieldFactory = new MilestoneEditFormFieldFactory(editForm);
        editForm.setBeanFormFieldFactory(milestoneEditFormFieldFactory);
        editForm.setBean(milestone);

        MButton saveBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SAVE), clickEvent -> {
            if (editForm.validateForm()) {
                MilestoneService milestoneService = AppContextUtil.getSpringBean(MilestoneService.class);
                milestone.setSaccountid(AppUI.getAccountId());
                Integer milestoneId;
                if (milestone.getId() == null) {
                    milestoneId = milestoneService.saveWithSession(milestone, UserUIContext.getUsername());
                } else {
                    milestoneService.updateWithSession(milestone, UserUIContext.getUsername());
                    milestoneId = milestone.getId();
                }

                AttachmentUploadField uploadField = milestoneEditFormFieldFactory.getAttachmentUploadField();
                String attachPath = AttachmentUtils.getProjectEntityAttachmentPath(AppUI.getAccountId(), milestone.getProjectid(),
                        ProjectTypeConstants.MILESTONE, "" + milestone.getId());
                uploadField.saveContentsToRepo(attachPath);

                EventBusFactory.getInstance().post(new MilestoneEvent.NewMilestoneAdded(MilestoneAddWindow.this, milestoneId));
                EventBusFactory.getInstance().post(new TicketEvent.NewTicketAdded(MilestoneAddWindow.this,
                        ProjectTypeConstants.MILESTONE, milestoneId));
                close();
            }
        }).withIcon(VaadinIcons.CLIPBOARD).withStyleName(WebThemes.BUTTON_ACTION);

        MButton cancelBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CANCEL), clickEvent -> close())
                .withStyleName(WebThemes.BUTTON_OPTION);
        MHorizontalLayout buttonControls = new MHorizontalLayout(cancelBtn, saveBtn);
        content.addComponent(buttonControls);
        content.setComponentAlignment(buttonControls, Alignment.MIDDLE_RIGHT);
    }
}
