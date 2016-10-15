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
import com.mycollab.core.utils.StringUtils;
import com.mycollab.db.arguments.SearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.eventmanager.ApplicationEventListener;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria;
import com.mycollab.module.project.event.TicketEvent;
import com.mycollab.module.project.i18n.TaskI18nEnum;
import com.mycollab.module.project.service.ProjectTicketService;
import com.mycollab.module.user.CommonTooltipGenerator;
import com.mycollab.module.user.domain.SimpleUser;
import com.mycollab.module.user.service.UserService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.mycollab.vaadin.web.ui.Depot;
import com.mycollab.vaadin.web.ui.ProgressBarIndicator;
import com.mycollab.vaadin.web.ui.WebUIConstants;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.UI;
import org.apache.commons.collections.CollectionUtils;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class UnresolvedTicketsByAssigneeWidget extends Depot {
    private static final long serialVersionUID = 1L;

    private ProjectTicketSearchCriteria searchCriteria;
    private int totalCountItems;
    private List<GroupItem> groupItems;

    private ApplicationEventListener<TicketEvent.HasTicketPropertyChanged> ticketPropertyChangedHandler = new
            ApplicationEventListener<TicketEvent.HasTicketPropertyChanged>() {
                @Override
                @Subscribe
                public void handle(TicketEvent.HasTicketPropertyChanged event) {
                    if (searchCriteria != null && ("assignUser".equals(event.getData()) || "all".equals(event.getData()))) {
                        UI.getCurrent().access(() -> setSearchCriteria(searchCriteria));
                    }
                }
            };

    public UnresolvedTicketsByAssigneeWidget() {
        super("", new MVerticalLayout());
        setContentBorder(true);
    }

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

    public void setSearchCriteria(final ProjectTicketSearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;

        ProjectTicketService projectTicketService = AppContextUtil.getSpringBean(ProjectTicketService.class);
        totalCountItems = projectTicketService.getTotalCount(searchCriteria);
        groupItems = projectTicketService.getAssigneeSummary(searchCriteria);

        this.setTitle(String.format("%s (%d)", UserUIContext.getMessage(TaskI18nEnum.WIDGET_UNRESOLVED_BY_ASSIGNEE_TITLE), totalCountItems));
        displayPlainMode();
    }

    private void displayPlainMode() {
        bodyContent.removeAllComponents();
        int totalAssignTicketCounts = 0;
        if (CollectionUtils.isNotEmpty(groupItems)) {
            for (GroupItem item : groupItems) {
                MHorizontalLayout assigneeLayout = new MHorizontalLayout().withFullWidth();
                assigneeLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

                String assignUser = item.getGroupid();
                String assignUserFullName = item.getGroupid() == null ? "" : item.getGroupname();

                if (StringUtils.isBlank(assignUserFullName)) {
                    assignUserFullName = StringUtils.extractNameFromEmail(item.getGroupid());
                }

                TicketAssigneeLink ticketAssigneeLink = new TicketAssigneeLink(assignUser, item.getExtraValue(), assignUserFullName);
                assigneeLayout.addComponent(new MCssLayout(ticketAssigneeLink).withWidth("110px"));
                ProgressBarIndicator indicator = new ProgressBarIndicator(totalCountItems, totalCountItems - item.getValue().intValue(), false);
                indicator.setWidth("100%");
                assigneeLayout.with(indicator).expand(indicator);
                bodyContent.addComponent(assigneeLayout);
                totalAssignTicketCounts += item.getValue().intValue();
            }
        }
        int totalUnassignTicketsCount = totalCountItems - totalAssignTicketCounts;
        if (totalUnassignTicketsCount > 0) {
            MButton unassignLink = new MButton("No assignee").withStyleName(WebUIConstants.BUTTON_LINK)
                    .withIcon(UserAvatarControlFactory.createAvatarResource(null, 16)).withListener(clickEvent -> {
                        ProjectTicketSearchCriteria criteria = BeanUtility.deepClone(searchCriteria);
                        criteria.setUnAssignee(new SearchField());
                        EventBusFactory.getInstance().post(new TicketEvent.SearchRequest(UnresolvedTicketsByAssigneeWidget.this,
                                criteria));
                    });
            MHorizontalLayout assigneeLayout = new MHorizontalLayout().withFullWidth();
            assigneeLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
            assigneeLayout.addComponent(new MCssLayout(unassignLink).withWidth("110px"));
            ProgressBarIndicator indicator = new ProgressBarIndicator(totalCountItems, totalCountItems - totalUnassignTicketsCount, false);
            indicator.setWidth("100%");
            assigneeLayout.with(indicator).expand(indicator);
            bodyContent.addComponent(assigneeLayout);
        }
    }

    private class TicketAssigneeLink extends MButton {
        private static final long serialVersionUID = 1L;

        TicketAssigneeLink(final String assignee, String assigneeAvatarId, final String assigneeFullName) {
            super(StringUtils.trim(assigneeFullName, 25, true));

            this.withListener(clickEvent -> {
                ProjectTicketSearchCriteria criteria = BeanUtility.deepClone(searchCriteria);
                criteria.setAssignUser(StringSearchField.and(assignee));
                criteria.setTypes(CurrentProjectVariables.getRestrictedTicketTypes());
                EventBusFactory.getInstance().post(new TicketEvent.SearchRequest(UnresolvedTicketsByAssigneeWidget.this,
                        criteria));
            }).withWidth("100%").withIcon(UserAvatarControlFactory.createAvatarResource(assigneeAvatarId, 16))
                    .withStyleName(WebUIConstants.BUTTON_LINK, UIConstants.TEXT_ELLIPSIS);
            UserService service = AppContextUtil.getSpringBean(UserService.class);
            SimpleUser user = service.findUserByUserNameInAccount(assignee, MyCollabUI.getAccountId());
            this.setDescription(CommonTooltipGenerator.generateTooltipUser(UserUIContext.getUserLocale(), user,
                    MyCollabUI.getSiteUrl(), UserUIContext.getUserTimeZone()));
        }
    }
}
