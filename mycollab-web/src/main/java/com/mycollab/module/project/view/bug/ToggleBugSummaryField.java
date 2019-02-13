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
package com.mycollab.module.project.view.bug;

import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Span;
import com.mycollab.core.utils.BeanUtility;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.html.DivLessFormatter;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectLinkGenerator;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.i18n.BugI18nEnum;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.module.tracker.domain.BugWithBLOBs;
import com.mycollab.module.tracker.domain.SimpleBug;
import com.mycollab.module.tracker.service.BugService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.TooltipHelper;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.AbstractToggleSummaryField;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.2.3
 */
class ToggleBugSummaryField extends AbstractToggleSummaryField {
    private boolean isRead = true;
    private BugWithBLOBs bug;
    private int maxLength;

    ToggleBugSummaryField(final BugWithBLOBs bug) {
        this(bug, Integer.MAX_VALUE);
    }

    ToggleBugSummaryField(final BugWithBLOBs bug, int trimCharacters) {
        this.bug = bug;
        this.maxLength = trimCharacters;
        titleLinkLbl = ELabel.html(buildBugLink()).withStyleName(WebThemes.LABEL_WORD_WRAP).withUndefinedWidth();
        this.addComponent(titleLinkLbl);
        buttonControls = new MHorizontalLayout().withStyleName("toggle").withSpacing(false);
        if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.BUGS)) {
            this.addStyleName("editable-field");
            MButton instantEditBtn = new MButton("", clickEvent -> {
                if (isRead) {
                    ToggleBugSummaryField.this.removeComponent(titleLinkLbl);
                    ToggleBugSummaryField.this.removeComponent(buttonControls);
                    final TextField editField = new TextField();
                    editField.setValue(bug.getName());
                    editField.setWidth("100%");
                    editField.focus();
                    ToggleBugSummaryField.this.addComponent(editField);
                    ToggleBugSummaryField.this.removeStyleName("editable-field");
                    editField.addValueChangeListener(valueChangeEvent -> updateFieldValue(editField));
                    editField.addBlurListener(blurEvent -> updateFieldValue(editField));
                    isRead = !isRead;
                }
            }).withDescription(UserUIContext.getMessage(BugI18nEnum.OPT_EDIT_BUG_NAME))
                    .withIcon(VaadinIcons.EDIT).withStyleName(ValoTheme.BUTTON_ICON_ONLY, ValoTheme.BUTTON_ICON_ALIGN_TOP);
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
        if (StringUtils.isNotBlank(newValue) && !newValue.equals(bug.getName())) {
            bug.setName(newValue);
            titleLinkLbl.setValue(buildBugLink());
            BugService bugService = AppContextUtil.getSpringBean(BugService.class);
            bugService.updateSelectiveWithSession(BeanUtility.deepClone(bug), UserUIContext.getUsername());
        }

        isRead = !isRead;
    }

    private String buildBugLink() {
        String linkName = StringUtils.trim(bug.getName(), maxLength, true);
        A bugLink = new A().setId("tag" + TooltipHelper.TOOLTIP_ID).
                setHref(ProjectLinkGenerator.generateBugPreviewLink(bug.getBugkey(),
                        CurrentProjectVariables.getShortName())).appendText(linkName).setStyle("display:inline");
        Div resultDiv = new DivLessFormatter().appendChild(bugLink);
        if (SimpleBug.isOverdue(bug)) {
            bugLink.setCSSClass("overdue");
            resultDiv.appendChild(new Span().setCSSClass(WebThemes.META_INFO)
                    .appendText(" - " + UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_DUE_IN, UserUIContext.formatDuration(bug.getDuedate()))));
        } else if (SimpleBug.isCompleted(bug)) {
            bugLink.setCSSClass("completed");
        }

        bugLink.setAttribute("onmouseover", TooltipHelper.projectHoverJsFunction(ProjectTypeConstants.BUG, bug.getId() + ""));
        bugLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction());

        return resultDiv.write();
    }
}
