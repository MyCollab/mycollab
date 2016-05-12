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
package com.esofthead.mycollab.module.project.view.bug.components;

import com.esofthead.mycollab.core.utils.BeanUtility;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.html.DivLessFormatter;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.tracker.domain.BugWithBLOBs;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.spring.AppContextUtil;
import com.esofthead.mycollab.utils.TooltipHelper;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.web.ui.AbstractToggleSummaryField;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Span;
import com.vaadin.data.Property;
import com.vaadin.event.FieldEvents;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import static com.esofthead.mycollab.utils.TooltipHelper.TOOLTIP_ID;

/**
 * @author MyCollab Ltd
 * @since 5.2.3
 */
public class ToggleBugSummaryField extends AbstractToggleSummaryField {
    private boolean isRead = true;
    private BugWithBLOBs bug;
    private int maxLength;

    public ToggleBugSummaryField(final BugWithBLOBs bug) {
        this(bug, Integer.MAX_VALUE);
    }

    public ToggleBugSummaryField(final BugWithBLOBs bug, int trimCharacters) {
        this.bug = bug;
        this.maxLength = trimCharacters;
        titleLinkLbl = new Label(buildBugLink(), ContentMode.HTML);
        titleLinkLbl.addStyleName(UIConstants.LABEL_WORD_WRAP);
        titleLinkLbl.setWidthUndefined();
        this.addComponent(titleLinkLbl);
        buttonControls = new MHorizontalLayout().withStyleName("toggle").withSpacing(false);
        if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.BUGS)) {
            this.addStyleName("editable-field");
            Button instantEditBtn = new Button(null, new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    if (isRead) {
                        ToggleBugSummaryField.this.removeComponent(titleLinkLbl);
                        ToggleBugSummaryField.this.removeComponent(buttonControls);
                        final TextField editField = new TextField();
                        editField.setValue(bug.getSummary());
                        editField.setWidth("100%");
                        editField.focus();
                        ToggleBugSummaryField.this.addComponent(editField);
                        ToggleBugSummaryField.this.removeStyleName("editable-field");
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
        addComponent(titleLinkLbl);
        addComponent(buttonControls);
        addStyleName("editable-field");
        String newValue = editField.getValue();
        if (StringUtils.isNotBlank(newValue) && !newValue.equals(bug.getSummary())) {
            bug.setSummary(newValue);
            titleLinkLbl.setValue(buildBugLink());
            BugService bugService = AppContextUtil.getSpringBean(BugService.class);
            bugService.updateSelectiveWithSession(BeanUtility.deepClone(bug), AppContext.getUsername());
        }

        isRead = !isRead;
    }

    private String buildBugLink() {
        String linkName = StringUtils.trim(bug.getSummary(), maxLength, true);
        A bugLink = new A().setId("tag" + TOOLTIP_ID).setHref(ProjectLinkBuilder.generateBugPreviewFullLink(bug.getBugkey(),
                CurrentProjectVariables.getShortName())).appendText(linkName).setStyle("display:inline");
        Div resultDiv = new DivLessFormatter().appendChild(bugLink);
        if (SimpleBug.isOverdue(bug)) {
            bugLink.setCSSClass("overdue");
            resultDiv.appendChild(new Span().setCSSClass(UIConstants.LABEL_META_INFO).appendText(" - Due in " + AppContext
                    .formatDuration(bug.getDuedate())));
        } else if (SimpleBug.isCompleted(bug)) {
            bugLink.setCSSClass("completed");
        }

        bugLink.setAttribute("onmouseover", TooltipHelper.projectHoverJsFunction(ProjectTypeConstants.BUG, bug.getId() + ""));
        bugLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction());

        return resultDiv.write();
    }
}
