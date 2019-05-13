package com.mycollab.module.project.view.ticket;

import com.mycollab.core.MyCollabException;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.dao.TicketHierarchyMapper;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.domain.TicketHierarchyExample;
import com.mycollab.module.project.i18n.TaskI18nEnum;
import com.mycollab.module.project.service.ProjectTicketService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.RemoveInlineComponentMarker;
import com.mycollab.vaadin.ui.UIUtils;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd
 * @since 7.0.3
 */
public class ParentTicketComp extends MHorizontalLayout {
    public ParentTicketComp(String parentTicketType, Integer parentTicketId, ProjectTicket subTicket) {
        ELabel titleLbl = new ELabel(UserUIContext.getMessage(TaskI18nEnum.FORM_PARENT_TASK)).withStyleName(WebThemes.ARROW_BTN)
                .withUndefinedWidth();
        with(titleLbl);

        if (ProjectTypeConstants.TASK.equals(parentTicketType) || ProjectTypeConstants.BUG.equals(parentTicketType)) {
            ProjectTicketService projectTicketService = AppContextUtil.getSpringBean(ProjectTicketService.class);
            ProjectTicket parentTicket = projectTicketService.findTicket(parentTicketType, parentTicketId);
            if (parentTicket != null) {
                ToggleTicketSummaryField toggleTicketSummaryField = new ToggleTicketSummaryField(parentTicket);
                MButton unlinkBtn = new MButton("", clickEvent -> {
                    TicketHierarchyExample ex = new TicketHierarchyExample();
                    ex.createCriteria().andParentidEqualTo(parentTicket.getTypeId()).andParenttypeEqualTo(parentTicket.getType())
                            .andTickettypeEqualTo(subTicket.getType()).andTicketidEqualTo(subTicket.getTypeId());
                    TicketHierarchyMapper ticketHierarchyMapper = AppContextUtil.getSpringBean(TicketHierarchyMapper.class);
                    ticketHierarchyMapper.deleteByExample(ex);
                    UIUtils.removeChildAssociate(toggleTicketSummaryField, RemoveInlineComponentMarker.class);
                }).withIcon(VaadinIcons.UNLINK).withStyleName(ValoTheme.BUTTON_ICON_ONLY, ValoTheme.BUTTON_ICON_ALIGN_TOP)
                        .withDescription(UserUIContext.getMessage(TaskI18nEnum.OPT_REMOVE_PARENT_CHILD_RELATIONSHIP));

                toggleTicketSummaryField.addControl(unlinkBtn);
                with(toggleTicketSummaryField);
            }
        } else {
            throw new MyCollabException("Not support parent ticket type " + parentTicketType + " yet");
        }

    }
}
