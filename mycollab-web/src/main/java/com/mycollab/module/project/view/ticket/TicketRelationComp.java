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
package com.mycollab.module.project.view.ticket;

import com.hp.gagawa.java.elements.A;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectLinkGenerator;
import com.mycollab.module.project.dao.TicketRelationMapper;
import com.mycollab.module.project.domain.SimpleTicketRelation;
import com.mycollab.module.project.domain.TicketRelationExample;
import com.mycollab.module.project.i18n.TicketI18nEnum;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.TooltipHelper;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.RemoveInlineComponentMarker;
import com.mycollab.vaadin.ui.UIUtils;
import com.mycollab.vaadin.web.ui.AbstractToggleSummaryField;
import com.mycollab.vaadin.web.ui.ConfirmDialogExt;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.2.12
 */
public class TicketRelationComp extends AbstractToggleSummaryField {

    public TicketRelationComp(SimpleTicketRelation ticketRelation) {
        titleLinkLbl = ELabel.html(buildTicketLink(ticketRelation)).withStyleName(WebThemes.LABEL_WORD_WRAP).withUndefinedWidth();
        this.addComponent(titleLinkLbl);
        this.addStyleName("editable-field");
        buttonControls = new MHorizontalLayout().withStyleName("toggle").withSpacing(false);
        MButton unlinkBtn = new MButton("", clickEvent -> {
            ConfirmDialogExt.show(UI.getCurrent(), UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE,
                    AppUI.getSiteName()),
                    UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                    UserUIContext.getMessage(GenericI18Enum.ACTION_YES),
                    UserUIContext.getMessage(GenericI18Enum.ACTION_NO), confirmDialog -> {
                        TicketRelationExample ex = new TicketRelationExample();
                        ex.or().andTicketidEqualTo(ticketRelation.getTicketid()).andTickettypeEqualTo(ticketRelation.getTickettype());
                        ex.or().andTicketidEqualTo(ticketRelation.getTypeid()).andTickettypeEqualTo(ticketRelation.getType());

                        TicketRelationMapper ticketRelationMapper = AppContextUtil.getSpringBean(TicketRelationMapper.class);
                        ticketRelationMapper.deleteByExample(ex);
                        UIUtils.removeChildAssociate(TicketRelationComp.this, RemoveInlineComponentMarker.class);
                    });
        }).withIcon(VaadinIcons.UNLINK).withStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP, ValoTheme.BUTTON_ICON_ONLY)
                .withDescription(UserUIContext.getMessage(TicketI18nEnum.OPT_REMOVE_RELATIONSHIP));
        buttonControls.with(unlinkBtn);
        this.addComponent(buttonControls);
    }

    private String buildTicketLink(SimpleTicketRelation ticketRelation) {
        if (ticketRelation.getLtr()) {
            A ticketLink = new A(ProjectLinkGenerator.generateProjectItemLink(CurrentProjectVariables.getShortName(),
                    CurrentProjectVariables.getProjectId(), ticketRelation.getType(), ticketRelation.getTypeKey() + ""))
                    .appendText(ProjectAssetsManager.getAsset(ticketRelation.getType()).getHtml() + " " + ticketRelation.getTypeName()).setId("tag" + TooltipHelper.TOOLTIP_ID);
            ticketLink.setAttribute("onmouseover", TooltipHelper.projectHoverJsFunction(ticketRelation.getType(), "" + ticketRelation.getTypeid()));
            ticketLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction());
            return ticketLink.write();
        } else {
            A ticketLink = new A(ProjectLinkGenerator.generateProjectItemLink(CurrentProjectVariables.getShortName(),
                    CurrentProjectVariables.getProjectId(), ticketRelation.getTickettype(), ticketRelation.getTicketKey() + ""))
                    .appendText(ProjectAssetsManager.getAsset(ticketRelation.getTickettype()).getHtml() + " " + ticketRelation.getTicketName()).setId("tag" + TooltipHelper.TOOLTIP_ID);
            ticketLink.setAttribute("onmouseover", TooltipHelper.projectHoverJsFunction(ticketRelation.getTickettype(), "" + ticketRelation.getTicketid()));
            ticketLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction());
            return ticketLink.write();
        }
    }
}
