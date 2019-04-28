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

import com.mycollab.module.project.dao.TicketHierarchyMapper;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.domain.TicketHierarchyExample;
import com.mycollab.module.project.i18n.TaskI18nEnum;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.RemoveInlineComponentMarker;
import com.mycollab.vaadin.ui.UIUtils;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.button.MButton;

/**
 * @author MyCollab Ltd
 * @since 5.2.12
 */
public class ToggleTicketSummaryWithParentRelationshipField extends CssLayout {
    private ToggleTicketSummaryField toggleTicketSummaryField;

    public ToggleTicketSummaryWithParentRelationshipField(ProjectTicket parentTicket, ProjectTicket subTicket) {
        toggleTicketSummaryField = new ToggleTicketSummaryField(subTicket);
        MButton unlinkBtn = new MButton("", clickEvent -> {
            TicketHierarchyExample ex = new TicketHierarchyExample();
            ex.createCriteria().andParentidEqualTo(parentTicket.getTypeId()).andParenttypeEqualTo(parentTicket.getType())
                    .andTickettypeEqualTo(subTicket.getType()).andTicketidEqualTo(subTicket.getTypeId());
            TicketHierarchyMapper ticketHierarchyMapper = AppContextUtil.getSpringBean(TicketHierarchyMapper.class);
            ticketHierarchyMapper.deleteByExample(ex);
            UIUtils.removeChildAssociate(ToggleTicketSummaryWithParentRelationshipField.this, RemoveInlineComponentMarker.class);
        }).withIcon(VaadinIcons.UNLINK).withStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP, ValoTheme.BUTTON_ICON_ONLY)
                .withDescription(UserUIContext.getMessage(TaskI18nEnum.OPT_REMOVE_PARENT_CHILD_RELATIONSHIP));
        toggleTicketSummaryField.addControl(unlinkBtn);
        this.addComponent(toggleTicketSummaryField);
    }
}
