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
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.Milestone;
import com.esofthead.mycollab.module.project.domain.SimpleMilestone;
import com.esofthead.mycollab.module.project.events.MilestoneEvent;
import com.esofthead.mycollab.module.project.service.MilestoneService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.AdvancedEditBeanForm;
import com.esofthead.mycollab.vaadin.ui.DynaFormLayout;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.2.0
 */
class MilestoneAddWindow extends Window {
    MilestoneAddWindow(final SimpleMilestone milestone) {
        if (milestone.getId() == null) {
            setCaption("New milestone");
        } else {
            setCaption("Edit milestone");
        }
        this.setWidth("800px");
        this.setModal(true);
        this.setResizable(false);
        VerticalLayout content = new VerticalLayout();
        this.setContent(content);
        final AdvancedEditBeanForm<SimpleMilestone> editBeanForm = new AdvancedEditBeanForm<>();
        content.addComponent(editBeanForm);
        editBeanForm.setFormLayoutFactory(new DynaFormLayout(ProjectTypeConstants.MILESTONE,
                MilestoneDefaultFormLayoutFactory.getForm(), Milestone.Field.id.name()));
        editBeanForm.setBeanFormFieldFactory(new MilestoneEditFormFieldFactory(editBeanForm));
        editBeanForm.setBean(milestone);

        MHorizontalLayout buttonControls = new MHorizontalLayout().withMargin(new MarginInfo(true, true, true, false));
        buttonControls.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);

        Button updateAllBtn = new Button("Update other fields", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                EventBusFactory.getInstance().post(new MilestoneEvent.GotoAdd(MilestoneAddWindow.this, milestone));
                close();
            }
        });
        updateAllBtn.addStyleName(UIConstants.BUTTON_LINK);

        Button saveBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_SAVE), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                if (editBeanForm.validateForm()) {
                    MilestoneService milestoneService = ApplicationContextUtil.getSpringBean(MilestoneService.class);
                    milestone.setProjectid(CurrentProjectVariables.getProjectId());
                    milestone.setSaccountid(AppContext.getAccountId());
                    Integer milestoneId;
                    if (milestone.getId() == null) {
                        milestoneId = milestoneService.saveWithSession(milestone, AppContext.getUsername());
                    } else {
                        milestoneService.updateWithSession(milestone, AppContext.getUsername());
                        milestoneId = milestone.getId();
                    }

                    EventBusFactory.getInstance().post(new MilestoneEvent.NewMilestoneAdded(MilestoneAddWindow.this, milestoneId));
                    close();
                }
            }
        });
        saveBtn.setIcon(FontAwesome.SAVE);
        saveBtn.setStyleName(UIConstants.BUTTON_ACTION);
        saveBtn.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        Button cancelBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                MilestoneAddWindow.this.close();
            }
        });
        cancelBtn.setStyleName(UIConstants.THEME_GRAY_LINK);
        buttonControls.with(updateAllBtn, saveBtn, cancelBtn);
        content.addComponent(buttonControls);
        content.setComponentAlignment(buttonControls, Alignment.MIDDLE_RIGHT);
    }
}
