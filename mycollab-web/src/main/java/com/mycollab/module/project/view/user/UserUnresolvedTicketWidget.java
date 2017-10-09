package com.mycollab.module.project.view.user;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.utils.DateTimeUtils;
import com.mycollab.db.arguments.RangeDateSearchField;
import com.mycollab.db.arguments.SearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria;
import com.mycollab.module.project.i18n.ProjectI18nEnum;
import com.mycollab.module.project.service.ProjectTicketService;
import com.mycollab.module.project.view.UserDashboardView;
import com.mycollab.module.project.view.ticket.TicketRowDisplayHandler;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.UIUtils;
import com.mycollab.vaadin.web.ui.DefaultBeanPagedList;
import com.mycollab.vaadin.web.ui.Depot;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CssLayout;
import org.joda.time.LocalDate;

import java.util.Date;

/**
 * @author MyCollab Ltd
 * @since 5.2.4
 */
public class UserUnresolvedTicketWidget extends Depot {
    private ProjectTicketSearchCriteria searchCriteria;
    private DefaultBeanPagedList<ProjectTicketService, ProjectTicketSearchCriteria, ProjectTicket> ticketList;
    private String title = "";

    public UserUnresolvedTicketWidget() {
        super("", new CssLayout());
        this.setWidth("100%");
        final CheckBox myItemsSelection = new CheckBox(UserUIContext.getMessage(GenericI18Enum.OPT_MY_ITEMS));
        myItemsSelection.addValueChangeListener(valueChangeEvent -> {
            boolean isMyItemsOption = myItemsSelection.getValue();
            if (searchCriteria != null) {
                if (isMyItemsOption) {
                    searchCriteria.setAssignUser(StringSearchField.and(UserUIContext.getUsername()));
                } else {
                    searchCriteria.setAssignUser(null);
                }
                updateSearchResult();
            }
        });
        ticketList = new DefaultBeanPagedList<ProjectTicketService, ProjectTicketSearchCriteria, ProjectTicket>
                (AppContextUtil.getSpringBean(ProjectTicketService.class), new TicketRowDisplayHandler(true), 10) {
            @Override
            protected String stringWhenEmptyList() {
                return UserUIContext.getMessage(ProjectI18nEnum.OPT_NO_TICKET);
            }
        };
        this.addHeaderElement(myItemsSelection);
        this.bodyContent.addComponent(ticketList);
    }

    public void displayUnresolvedAssignmentsThisWeek() {
        title = UserUIContext.getMessage(ProjectI18nEnum.OPT_UNRESOLVED_TICKET_THIS_WEEK);
        searchCriteria = new ProjectTicketSearchCriteria();
        searchCriteria.setIsOpenned(new SearchField());
        UserDashboardView userDashboardView = UIUtils.getRoot(this, UserDashboardView.class);
        searchCriteria.setProjectIds(new SetSearchField<>(userDashboardView.getInvolvedProjectKeys()));
        LocalDate now = new LocalDate();
        Date[] bounceDateOfWeek = DateTimeUtils.getBounceDatesOfWeek(now.toDate());
        RangeDateSearchField range = new RangeDateSearchField(bounceDateOfWeek[0], bounceDateOfWeek[1]);
        searchCriteria.setDateInRange(range);
        updateSearchResult();
    }

    public void displayNoUnresolvedAssignmentsThisWeek() {
        title = UserUIContext.getMessage(ProjectI18nEnum.OPT_UNRESOLVED_TICKET_THIS_WEEK);
        this.setTitle(String.format(title, 0));
    }

    public void displayUnresolvedAssignmentsNextWeek() {
        title = UserUIContext.getMessage(ProjectI18nEnum.OPT_UNRESOLVED_TICKET_NEXT_WEEK);
        searchCriteria = new ProjectTicketSearchCriteria();
        UserDashboardView userDashboardView = UIUtils.getRoot(this, UserDashboardView.class);
        searchCriteria.setIsOpenned(new SearchField());
        searchCriteria.setProjectIds(new SetSearchField<>(userDashboardView.getInvolvedProjectKeys()));
        LocalDate now = new LocalDate();
        now = now.plusDays(7);
        Date[] bounceDateOfWeek = DateTimeUtils.getBounceDatesOfWeek(now.toDate());
        RangeDateSearchField range = new RangeDateSearchField(bounceDateOfWeek[0], bounceDateOfWeek[1]);
        searchCriteria.setDateInRange(range);
        updateSearchResult();
    }

    public void displayNoUnresolvedAssignmentsNextWeek() {
        title = UserUIContext.getMessage(ProjectI18nEnum.OPT_UNRESOLVED_TICKET_NEXT_WEEK);
        this.setTitle(String.format(title, 0));
    }

    private void updateSearchResult() {
        ticketList.setSearchCriteria(searchCriteria);
        this.setTitle(String.format(title, ticketList.getTotalCount()));
    }
}
