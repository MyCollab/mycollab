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

import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.domain.SimpleMilestone;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum;
import com.esofthead.mycollab.module.project.service.MilestoneService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.ELabel;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Span;
import com.vaadin.data.Property;
import com.vaadin.event.FieldEvents;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.teemu.VaadinIcons;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.2.3
 */
public class ToggleMilestoneSummaryField extends CssLayout {
    private boolean isRead = true;
    private ELabel milestoneLbl;
    private MHorizontalLayout buttonControls;
    private SimpleMilestone milestone;
    private int maxLength;

    ToggleMilestoneSummaryField(final SimpleMilestone milestone) {
        this(milestone, Integer.MAX_VALUE);
    }

    ToggleMilestoneSummaryField(final SimpleMilestone milestone, int maxLength) {
        this.milestone = milestone;
        this.maxLength = maxLength;
        this.setWidth("100%");
        milestoneLbl = new ELabel(buildMilestoneLink(), ContentMode.HTML).withStyleName(ValoTheme.LABEL_H3).withWidthUndefined();
        milestoneLbl.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        milestoneLbl.addStyleName(UIConstants.LABEL_WORD_WRAP);
        this.addComponent(milestoneLbl);
        buttonControls = new MHorizontalLayout().withStyleName("toggle").withSpacing(false);
        if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.MILESTONES)) {
            this.addStyleName("editable-field");
            Button instantEditBtn = new Button(null, new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    if (isRead) {
                        ToggleMilestoneSummaryField.this.removeComponent(milestoneLbl);
                        ToggleMilestoneSummaryField.this.removeComponent(buttonControls);
                        final TextField editField = new TextField();
                        editField.setValue(milestone.getName());
                        editField.setWidth("100%");
                        editField.focus();
                        ToggleMilestoneSummaryField.this.addComponent(editField);
                        ToggleMilestoneSummaryField.this.removeStyleName("editable-field");
                        editField.addValueChangeListener(new Property.ValueChangeListener() {
                            @Override
                            public void valueChange(Property.ValueChangeEvent event) {
                                updateFieldValue(editField);
                            }
                        });
                        editField.addBlurListener(new FieldEvents.BlurListener() {
                            @Override
                            public void blur(FieldEvents.BlurEvent event) {
                                updateFieldValue(editField);
                            }
                        });
                        isRead = !isRead;
                    }
                }
            });
            instantEditBtn.setDescription("Edit task name");
            instantEditBtn.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
            instantEditBtn.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
            instantEditBtn.setIcon(FontAwesome.EDIT);
            buttonControls.with(instantEditBtn);
            this.addComponent(buttonControls);
        }
    }

    private void updateFieldValue(TextField editField) {
        removeComponent(editField);
        addComponent(milestoneLbl);
        addComponent(buttonControls);
        addStyleName("editable-field");
        String newValue = editField.getValue();
        if (StringUtils.isNotBlank(newValue) && !newValue.equals(milestone.getName())) {
            milestone.setName(newValue);
            milestoneLbl.setValue(buildMilestoneLink());
            MilestoneService milestoneService = ApplicationContextUtil.getSpringBean(MilestoneService.class);
            milestoneService.updateWithSession(milestone, AppContext.getUsername());
        }

        isRead = !isRead;
    }

    private String buildMilestoneLink() {
        A milestoneLink = new A(ProjectLinkBuilder.generateMilestonePreviewFullLink(milestone.getProjectid(), milestone.getId()));
        milestoneLink.appendText(StringUtils.trim(milestone.getName(), maxLength, true));

        Div milestoneDiv = new Div().appendText(VaadinIcons.CALENDAR_BRIEFCASE.getHtml() + " ").appendChild(milestoneLink)
                .appendText(" (" + AppContext.getMessage(OptionI18nEnum.MilestoneStatus.class, milestone.getStatus()) + ")");
        if (milestone.isOverdue()) {
            milestoneLink.setCSSClass("overdue");
            milestoneDiv.appendChild(new Span().setCSSClass(UIConstants.LABEL_META_INFO).appendText(" - Due in " + AppContext
                    .formatDuration(milestone.getEnddate())));
        } else if (OptionI18nEnum.MilestoneStatus.Closed.name().equals(milestone.getStatus())) {
            milestoneLink.setCSSClass("completed");
        }
        return milestoneDiv.write();
    }
}
