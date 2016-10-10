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
package com.mycollab.module.project.view.ticket;

import com.google.common.eventbus.Subscribe;
import com.mycollab.common.domain.GroupItem;
import com.mycollab.core.utils.BeanUtility;
import com.mycollab.eventmanager.ApplicationEventListener;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria;
import com.mycollab.module.project.event.TicketEvent;
import com.mycollab.module.project.i18n.OptionI18nEnum;
import com.mycollab.module.project.i18n.OptionI18nEnum.Priority;
import com.mycollab.module.project.i18n.TaskI18nEnum;
import com.mycollab.module.project.service.ProjectTicketService;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.web.ui.ButtonI18nComp;
import com.mycollab.vaadin.web.ui.Depot;
import com.mycollab.vaadin.web.ui.ProgressBarIndicator;
import com.mycollab.vaadin.web.ui.WebUIConstants;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Collections;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class UnresolvedTicketByPriorityWidget extends Depot {
    private static final long serialVersionUID = 1L;

    private ProjectTicketSearchCriteria searchCriteria;
    private int totalCount;
    private List<GroupItem> groupItems;

    public UnresolvedTicketByPriorityWidget() {
        super("", new MVerticalLayout());
        setContentBorder(true);
    }

    private ApplicationEventListener<TicketEvent.HasTicketPropertyChanged> ticketPropertyChangedHandler = new
            ApplicationEventListener<TicketEvent.HasTicketPropertyChanged>() {
        @Override
        @Subscribe
        public void handle(TicketEvent.HasTicketPropertyChanged event) {
            if (searchCriteria != null && "priority".equals(event.getData())) {
                UI.getCurrent().access(() -> setSearchCriteria(searchCriteria));
            }
        }
    };

    @Override
    public void attach() {
        EventBusFactory.getInstance().register(ticketPropertyChangedHandler);
        super.attach();
    }

    @Override
    public void detach() {
        EventBusFactory.getInstance().unregister(ticketPropertyChangedHandler);
        super.detach();
    }

    public void setSearchCriteria(ProjectTicketSearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;

        ProjectTicketService ticketService = AppContextUtil.getSpringBean(ProjectTicketService.class);
        totalCount = ticketService.getTotalCount(searchCriteria);
        groupItems = ticketService.getPrioritySummary(searchCriteria);
        displayPlainMode();
    }

    private void displayPlainMode() {
        bodyContent.removeAllComponents();
        TicketPriorityClickListener listener = new TicketPriorityClickListener();
        this.setTitle(UserUIContext.getMessage(TaskI18nEnum.WIDGET_UNRESOLVED_BY_PRIORITY_TITLE) + " (" + totalCount + ")");

        if (!groupItems.isEmpty()) {
            for (Priority priority : OptionI18nEnum.priorities) {
                boolean isFound = false;
                for (GroupItem item : groupItems) {
                    if (priority.name().equals(item.getGroupid())) {
                        isFound = true;
                        MHorizontalLayout priorityLayout = new MHorizontalLayout().withFullWidth();
                        priorityLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
                        MButton priorityLink = new ButtonI18nComp(priority.name(), priority, listener)
                                .withIcon(ProjectAssetsManager.getPriority(priority.name()))
                                .withStyleName(WebUIConstants.BUTTON_LINK, "priority-" + priority.name().toLowerCase())
                                .withWidth("110px");

                        priorityLayout.addComponent(priorityLink);
                        ProgressBarIndicator indicator = new ProgressBarIndicator(totalCount, totalCount - item.getValue().intValue(), false);
                        indicator.setWidth("100%");
                        priorityLayout.with(indicator).expand(indicator);

                        bodyContent.addComponent(priorityLayout);
                    }
                }

                if (!isFound) {
                    MHorizontalLayout priorityLayout = new MHorizontalLayout().withFullWidth();
                    priorityLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
                    MButton priorityLink = new ButtonI18nComp(priority.name(), priority, listener)
                            .withIcon(ProjectAssetsManager.getPriority(priority.name()))
                            .withStyleName(WebUIConstants.BUTTON_LINK, "priority-" + priority.name().toLowerCase())
                            .withWidth("110px");
                    priorityLayout.addComponent(priorityLink);
                    ProgressBarIndicator indicator = new ProgressBarIndicator(totalCount, totalCount, false);
                    indicator.setWidth("100%");
                    priorityLayout.with(indicator).expand(indicator);
                    this.bodyContent.addComponent(priorityLayout);
                }
            }
        }
    }

    private class TicketPriorityClickListener implements Button.ClickListener {
        private static final long serialVersionUID = 1L;

        @Override
        public void buttonClick(final ClickEvent event) {
            String key = ((ButtonI18nComp) event.getButton()).getKey();
            ProjectTicketSearchCriteria criteria = BeanUtility.deepClone(searchCriteria);
            criteria.setTypes(CurrentProjectVariables.getRestrictedTicketTypes());
            criteria.addExtraField(ProjectTicketSearchCriteria.p_priority.andStringParamInList(Collections.singletonList(key)));
            EventBusFactory.getInstance().post(new TicketEvent.SearchRequest(this, criteria));
        }
    }
}