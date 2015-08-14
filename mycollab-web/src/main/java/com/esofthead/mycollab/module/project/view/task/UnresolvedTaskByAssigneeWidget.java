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
package com.esofthead.mycollab.module.project.view.task;

import com.esofthead.mycollab.common.domain.GroupItem;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.esofthead.mycollab.module.project.events.TaskEvent;
import com.esofthead.mycollab.module.project.i18n.TaskI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectTaskService;
import com.esofthead.mycollab.module.user.CommonTooltipGenerator;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.Depot;
import com.esofthead.mycollab.vaadin.ui.ProgressBarIndicator;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.google.common.eventbus.Subscribe;
import com.rits.cloning.Cloner;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class UnresolvedTaskByAssigneeWidget extends Depot {
    private static final long serialVersionUID = 1L;

    private TaskSearchCriteria searchCriteria;

    private ApplicationEventListener<TaskEvent.HasTaskChange> taskChangeHandler = new
            ApplicationEventListener<TaskEvent.HasTaskChange>() {
                @Override
                @Subscribe
                public void handle(TaskEvent.HasTaskChange event) {
                    if (searchCriteria != null) {
                        UI.getCurrent().access(new Runnable() {
                            @Override
                            public void run() {
                                UnresolvedTaskByAssigneeWidget.this.setSearchCriteria(searchCriteria);
                            }
                        });
                    }
                }
            };

    public UnresolvedTaskByAssigneeWidget() {
        super(AppContext.getMessage(TaskI18nEnum.WIDGET_UNRESOLVED_BY_ASSIGNEE_TITLE), new MVerticalLayout());
        this.setContentBorder(true);
    }

    @Override
    public void attach() {
        EventBusFactory.getInstance().register(taskChangeHandler);
        super.attach();
    }

    @Override
    public void detach() {
        EventBusFactory.getInstance().unregister(taskChangeHandler);
        super.detach();
    }

    public void setSearchCriteria(final TaskSearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
        this.bodyContent.removeAllComponents();
        ProjectTaskService projectTaskService = ApplicationContextUtil.getSpringBean(ProjectTaskService.class);
        int totalCountItems = projectTaskService.getTotalCount(searchCriteria);
        final List<GroupItem> groupItems = projectTaskService.getAssignedDefectsSummary(searchCriteria);

        this.setTitle(String.format("%s (%d)", AppContext
                .getMessage(TaskI18nEnum.WIDGET_UNRESOLVED_BY_ASSIGNEE_TITLE), totalCountItems));

        if (!groupItems.isEmpty()) {
            for (GroupItem item : groupItems) {
                MHorizontalLayout assigneeLayout = new MHorizontalLayout().withWidth("100%");

                String assignUser = item.getGroupid();
                String assignUserFullName = item.getGroupid() == null ? "" : item.getGroupname();

                if (StringUtils.isBlank(assignUserFullName)) {
                    assignUserFullName = com.esofthead.mycollab.core.utils.StringUtils.extractNameFromEmail(item
                            .getGroupid());
                }

                TaskAssigneeLink userLbl = new TaskAssigneeLink(assignUser, item.getExtraValue(), assignUserFullName);
                assigneeLayout.addComponent(userLbl);
                ProgressBarIndicator indicator = new ProgressBarIndicator(totalCountItems,
                        totalCountItems - item.getValue(), false);
                indicator.setWidth("100%");
                assigneeLayout.with(indicator).expand(indicator);
                bodyContent.addComponent(assigneeLayout);
            }
        }
    }

    class TaskAssigneeLink extends Button {
        private static final long serialVersionUID = 1L;

        public TaskAssigneeLink(final String assignee, String assigneeAvatarId, final String assigneeFullName) {
            super(assigneeFullName, new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final ClickEvent event) {
                    Cloner cloner = new Cloner();
                    TaskSearchCriteria criteria = cloner.deepClone(searchCriteria);
                    criteria.setAssignUser(new StringSearchField(assignee));
                    EventBusFactory.getInstance().post(new TaskEvent.SearchRequest(this, criteria));
                }
            });

            this.setStyleName(UIConstants.THEME_LINK);
            this.setWidth("110px");
            this.addStyleName(UIConstants.TEXT_ELLIPSIS);
            this.setIcon(UserAvatarControlFactory.createAvatarResource(assigneeAvatarId, 16));
            UserService service = ApplicationContextUtil.getSpringBean(UserService.class);
            SimpleUser user = service.findUserByUserNameInAccount(assignee, AppContext.getAccountId());
            this.setDescription(CommonTooltipGenerator.generateTooltipUser(AppContext.getUserLocale(), user,
                    AppContext.getSiteUrl(), AppContext.getTimezone()));
        }
    }
}
