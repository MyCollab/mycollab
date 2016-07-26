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
package com.mycollab.module.project.view.task.components;

import com.google.common.eventbus.Subscribe;
import com.mycollab.common.domain.GroupItem;
import com.mycollab.common.domain.OptionVal;
import com.mycollab.common.i18n.OptionI18nEnum;
import com.mycollab.common.service.OptionValService;
import com.mycollab.core.utils.BeanUtility;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.eventmanager.ApplicationEventListener;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.mycollab.module.project.events.TaskEvent;
import com.mycollab.module.project.i18n.TaskI18nEnum;
import com.mycollab.module.project.service.ProjectTaskService;
import com.mycollab.module.project.view.task.ITaskStatusChartWidget;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.web.ui.ButtonI18nComp;
import com.mycollab.vaadin.web.ui.DepotWithChart;
import com.mycollab.vaadin.web.ui.ProgressBarIndicator;
import com.mycollab.vaadin.web.ui.WebUIConstants;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.2.0
 */
public class UnresolvedTaskByStatusWidget extends DepotWithChart {
    private static final long serialVersionUID = 1L;

    private TaskSearchCriteria searchCriteria;
    private int totalCount;
    private List<GroupItem> groupItems;

    private ApplicationEventListener<TaskEvent.HasTaskChange> taskChangeHandler = new ApplicationEventListener<TaskEvent.HasTaskChange>() {
        @Override
        @Subscribe
        public void handle(TaskEvent.HasTaskChange event) {
            if (searchCriteria != null) {
                UI.getCurrent().access(() -> setSearchCriteria(searchCriteria));
            }
        }
    };

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

    public void setSearchCriteria(TaskSearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;

        ProjectTaskService taskService = AppContextUtil.getSpringBean(ProjectTaskService.class);
        totalCount = taskService.getTotalCount(searchCriteria);
        groupItems = taskService.getStatusSummary(searchCriteria);
        displayPlainMode();
    }

    @Override
    protected void displayPlainMode() {
        this.bodyContent.removeAllComponents();
        TaskStatusClickListener listener = new TaskStatusClickListener();
        this.setTitle(AppContext.getMessage(TaskI18nEnum.WIDGET_UNRESOLVED_BY_STATUS_TITLE) + " (" + totalCount + ")");

        if (!groupItems.isEmpty()) {
            OptionValService optionValService = AppContextUtil.getSpringBean(OptionValService.class);
            List<OptionVal> optionVals = optionValService.findOptionVals(ProjectTypeConstants.TASK,
                    CurrentProjectVariables.getProjectId(), AppContext.getAccountId());

            for (OptionVal optionVal : optionVals) {
                if (OptionI18nEnum.StatusI18nEnum.Closed.name().equals(optionVal.getTypeval())) {
                    continue;
                }
                boolean isFound = false;
                for (GroupItem item : groupItems) {
                    if (optionVal.getTypeval().equals(item.getGroupid())) {
                        isFound = true;
                        MHorizontalLayout statusLayout = new MHorizontalLayout().withFullWidth();
                        statusLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
                        String statusCaption = AppContext.getMessage(OptionI18nEnum.StatusI18nEnum.class, optionVal.getTypeval());
                        MButton statusLink = new ButtonI18nComp(optionVal.getTypeval())
                                .withCaption(StringUtils.trim(statusCaption, 25, true))
                                .withDescription(statusCaption)
                                .withListener(listener).withStyleName(WebUIConstants.BUTTON_LINK).withIcon(FontAwesome.FLAG);
                        statusLink.setWidth("110px");

                        statusLayout.addComponent(statusLink);
                        ProgressBarIndicator indicator = new ProgressBarIndicator(totalCount, totalCount - item
                                .getValue().intValue(), false);
                        indicator.setWidth("100%");
                        statusLayout.with(indicator).expand(indicator);

                        bodyContent.addComponent(statusLayout);
                    }
                }
                if (!isFound) {
                    MHorizontalLayout statusLayout = new MHorizontalLayout().withFullWidth();
                    statusLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
                    String statusCaption = AppContext.getMessage(OptionI18nEnum.StatusI18nEnum.class, optionVal.getTypeval());
                    MButton statusLink = new ButtonI18nComp(optionVal.getTypeval())
                            .withCaption(StringUtils.trim(statusCaption, 25, true)).withDescription(statusCaption)
                            .withListener(listener).withStyleName(WebUIConstants.BUTTON_LINK).withIcon(FontAwesome.FLAG);
                    statusLink.addStyleName(UIConstants.TEXT_ELLIPSIS);
                    statusLink.setWidth("110px");
                    statusLayout.addComponent(statusLink);
                    ProgressBarIndicator indicator = new ProgressBarIndicator(totalCount, totalCount, false);
                    indicator.setWidth("100%");
                    statusLayout.with(indicator).expand(indicator);
                    this.bodyContent.addComponent(statusLayout);
                }
            }
        }
    }

    @Override
    protected void displayChartMode() {
        this.bodyContent.removeAllComponents();
        ITaskStatusChartWidget taskStatusChartWidget = ViewManager.getCacheComponent(ITaskStatusChartWidget.class);
        taskStatusChartWidget.displayChart(searchCriteria);
        bodyContent.addComponent(taskStatusChartWidget);
    }

    private class TaskStatusClickListener implements Button.ClickListener {
        private static final long serialVersionUID = 1L;

        @Override
        public void buttonClick(final Button.ClickEvent event) {
            String key = ((ButtonI18nComp) event.getButton()).getKey();
            TaskSearchCriteria criteria = BeanUtility.deepClone(searchCriteria);
            criteria.setStatuses(new SetSearchField<>(key));
            EventBusFactory.getInstance().post(new TaskEvent.SearchRequest(this, criteria));
        }
    }
}
