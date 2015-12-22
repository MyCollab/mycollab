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

import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.html.DivLessFormatter;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.utils.TooltipHelper;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Span;
import com.vaadin.data.Property;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

import java.util.UUID;

/**
 * @author MyCollab Ltd
 * @since 5.2.3
 */
public class ToogleBugSummaryField extends CssLayout {
    private boolean isRead = true;
    private Label bugLinkLbl;
    private SimpleBug bug;
    private int maxLength;

    public ToogleBugSummaryField(final SimpleBug bug) {
        this(bug, Integer.MAX_VALUE);
    }

    public ToogleBugSummaryField(final SimpleBug bug, int trimCharacters) {
        this.bug = bug;
        this.maxLength = trimCharacters;
        bugLinkLbl = new Label(buildBugLink(), ContentMode.HTML);

        bugLinkLbl.addStyleName(UIConstants.LABEL_WORD_WRAP);
        bugLinkLbl.setWidthUndefined();
        this.addComponent(bugLinkLbl);
        if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.BUGS)) {
            this.addStyleName("editable-field");
            this.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
                @Override
                public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                    if (event.getClickedComponent() == bugLinkLbl) {
                        return;
                    }
                    if (isRead) {
                        ToogleBugSummaryField.this.removeComponent(bugLinkLbl);
                        final TextField editField = new TextField();
                        editField.setValue(bug.getSummary());
                        editField.setWidth("100%");
                        editField.focus();
                        ToogleBugSummaryField.this.addComponent(editField);
                        ToogleBugSummaryField.this.removeStyleName("editable-field");
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
        }
    }

    private void updateFieldValue(TextField editField) {
        ToogleBugSummaryField.this.removeComponent(editField);
        ToogleBugSummaryField.this.addComponent(bugLinkLbl);
        ToogleBugSummaryField.this.addStyleName("editable-field");
        String newValue = editField.getValue();
        if (StringUtils.isNotBlank(newValue) && !newValue.equals(bug.getSummary())) {
            bug.setSummary(newValue);
            bugLinkLbl.setValue(buildBugLink());
            BugService bugService = ApplicationContextUtil.getSpringBean(BugService.class);
            bugService.updateWithSession(bug, AppContext.getUsername());
        }

        isRead = !isRead;
    }

    private String buildBugLink() {
        String uid = UUID.randomUUID().toString();

        String linkName = String.format("[#%d] - %s", bug.getBugkey(), StringUtils.trim(bug.getSummary(), maxLength, true));
        A bugLink = new A().setId("tag" + uid).setHref(ProjectLinkBuilder.generateBugPreviewFullLink(bug.getBugkey(),
                CurrentProjectVariables.getShortName())).appendText(linkName).setStyle("display:inline");
        Div resultDiv = new DivLessFormatter().appendChild(bugLink);
        if (bug.isOverdue()) {
            bugLink.setCSSClass("overdue");
            resultDiv.appendChild(new Span().setCSSClass(UIConstants.LABEL_META_INFO).appendText(" - Due in " + AppContext
                    .formatDuration(bug.getDuedate())));
        } else if (bug.isCompleted()) {
            bugLink.setCSSClass("completed");
        }

        bugLink.setAttribute("onmouseover", TooltipHelper.projectHoverJsFunction(uid, ProjectTypeConstants.BUG, bug.getId() + ""));
        bugLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction(uid));

        resultDiv.appendChild(TooltipHelper.buildDivTooltipEnable(uid));
        return resultDiv.write();
    }
}
