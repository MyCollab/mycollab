/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.ui.form;

import com.hp.gagawa.java.elements.A;
import com.mycollab.html.DivLessFormatter;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectLinkGenerator;
import com.mycollab.module.project.domain.SimpleProject;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.vaadin.TooltipHelper;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.UIConstants;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import static com.mycollab.vaadin.TooltipHelper.TOOLTIP_ID;

/**
 * @author MyCollab Ltd.
 * @since 5.0.4
 */
// TODO
public class ProjectItemViewField extends CustomField<String> {
    private String type;
    private String typeId;
    private String typeDisplayName;

    public ProjectItemViewField(String type, String typeId, String typeDisplayName) {
        this.type = type;
        this.typeId = typeId;
        this.typeDisplayName = typeDisplayName;
    }

    @Override
    protected Component initContent() {
        if (typeId.equals("null")) {
            return new Label();
        }

        SimpleProject project = CurrentProjectVariables.getProject();
        DivLessFormatter div = new DivLessFormatter();

        A milestoneLink = new A().setId("tag" + TooltipHelper.TOOLTIP_ID).setHref(ProjectLinkGenerator.generateProjectItemLink(project.getShortname(),
                project.getId(), type, typeId)).appendText(typeDisplayName);
        milestoneLink.setAttribute("onmouseover", TooltipHelper.projectHoverJsFunction(type, typeId + ""));
        milestoneLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction());
        div.appendChild(milestoneLink);
        ELabel label = ELabel.html(div.write()).withStyleName(UIConstants.TEXT_ELLIPSIS);
        return new MHorizontalLayout(ELabel.fontIcon(ProjectAssetsManager.getAsset(type)).withUndefinedWidth(), label).expand(label);
    }

    @Override
    protected void doSetValue(String s) {

    }

    @Override
    public String getValue() {
        return null;
    }
}
