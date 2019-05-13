package com.mycollab.module.project.view.ticket;

import com.mycollab.db.arguments.BooleanSearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.dao.TicketHierarchyMapper;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.domain.TicketHierarchy;
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria;
import com.mycollab.module.project.event.TicketEvent;
import com.mycollab.module.project.i18n.TaskI18nEnum;
import com.mycollab.module.project.service.ProjectTicketService;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.IBeanList;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.web.ui.DefaultBeanPagedList;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

/**
 * @author MyCollab Ltd
 * @since 7.0.3
 */
public class SelectChildTicketWindow extends MWindow {
    private TicketSearchPanel ticketSearchPanel;
    private ProjectTicket parentTicket;

    public SelectChildTicketWindow(ProjectTicket parentTicket) {
        super(UserUIContext.getMessage(TaskI18nEnum.ACTION_SELECT_TASK));
        this.withModal(true).withResizable(false).withWidth(WebThemes.WINDOW_FORM_WIDTH);
        this.parentTicket = parentTicket;

        ProjectTicketSearchCriteria baseSearchCriteria = new ProjectTicketSearchCriteria();
        baseSearchCriteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
        baseSearchCriteria.setHasParentTicket(new BooleanSearchField(false));

        ticketSearchPanel = new TicketSearchPanel();
        DefaultBeanPagedList<ProjectTicketService, ProjectTicketSearchCriteria, ProjectTicket> ticketList = new DefaultBeanPagedList<>(
                AppContextUtil.getSpringBean(ProjectTicketService.class), new TicketRowRenderer(), 10);
        ticketSearchPanel.addSearchHandler(criteria -> {
            baseSearchCriteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
            criteria.setHasParentTicket(new BooleanSearchField(false));
            ticketList.setSearchCriteria(criteria);
        });
        MVerticalLayout content = new MVerticalLayout(ticketSearchPanel, ticketList).withSpacing(false);
        ticketList.setSearchCriteria(baseSearchCriteria);
        setContent(content);
    }

    private class TicketRowRenderer implements IBeanList.RowDisplayHandler<ProjectTicket> {
        @Override
        public Component generateRow(IBeanList<ProjectTicket> host, ProjectTicket item, int rowIndex) {
            MButton ticketLink = new MButton(item.getName(), clickEvent -> {
                if (item.getTypeId().equals(parentTicket.getTypeId())) {
                    NotificationUtil.showErrorNotification(UserUIContext.getMessage(TaskI18nEnum.ERROR_CAN_NOT_ASSIGN_PARENT_TASK_TO_ITSELF));
                } else {
                    TicketHierarchy ticketHierarchy = new TicketHierarchy();
                    ticketHierarchy.setParentid(parentTicket.getTypeId());
                    ticketHierarchy.setParenttype(parentTicket.getType());
                    ticketHierarchy.setProjectid(CurrentProjectVariables.getProjectId());
                    ticketHierarchy.setTicketid(item.getTypeId());
                    ticketHierarchy.setTickettype(item.getType());
                    TicketHierarchyMapper ticketHierarchyMapper = AppContextUtil.getSpringBean(TicketHierarchyMapper.class);
                    ticketHierarchyMapper.insert(ticketHierarchy);
                    EventBusFactory.getInstance().post(new TicketEvent.SubTicketAdded(this, item.getType(), item.getTypeId()));
                }

                close();
            }).withStyleName(WebThemes.BUTTON_LINK, WebThemes.TEXT_ELLIPSIS).withFullWidth();
            return new MHorizontalLayout(ELabel.fontIcon(ProjectAssetsManager.getAsset(item.getType())), ticketLink).expand(ticketLink).withStyleName("list-row").withFullWidth()
                    .alignAll(Alignment.MIDDLE_LEFT);
        }
    }
}
