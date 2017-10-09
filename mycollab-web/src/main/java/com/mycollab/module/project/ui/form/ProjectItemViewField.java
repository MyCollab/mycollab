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
        return new MHorizontalLayout(ELabel.fontIcon(ProjectAssetsManager.getAsset(type)).withWidthUndefined(), label).expand(label);
    }

    @Override
    public Class getType() {
        return String.class;
    }
}
