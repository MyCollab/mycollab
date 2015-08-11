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
package com.esofthead.mycollab.module.project.view.bug.components;

import com.esofthead.mycollab.common.domain.GroupItem;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.ProjectResources;
import com.esofthead.mycollab.module.project.events.BugEvent;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugPriority;
import com.esofthead.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.ButtonI18nComp;
import com.esofthead.mycollab.vaadin.ui.Depot;
import com.esofthead.mycollab.vaadin.ui.ProgressBarIndicator;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class UnresolvedBugsByPriorityWidget2 extends Depot {
    private static final long serialVersionUID = 1L;

    private BugSearchCriteria bugSearchCriteria;

    public UnresolvedBugsByPriorityWidget2() {
        super("", new MVerticalLayout());
        this.setContentBorder(true);
    }

    public void setSearchCriteria(final BugSearchCriteria searchCriteria) {
        this.bugSearchCriteria = searchCriteria;
        this.bodyContent.removeAllComponents();
        BugService bugService = ApplicationContextUtil.getSpringBean(BugService.class);
        int totalCount = bugService.getTotalCount(searchCriteria);
        this.setTitle(AppContext.getMessage(BugI18nEnum.WIDGET_UNRESOLVED_BY_PRIORITY_TITLE) + " (" + totalCount + ")");
        List<GroupItem> groupItems = bugService.getPrioritySummary(searchCriteria);
        BugPriorityClickListener listener = new BugPriorityClickListener();

        if (!groupItems.isEmpty()) {
            for (BugPriority priority : OptionI18nEnum.bug_priorities) {
                boolean isFound = false;
                for (GroupItem item : groupItems) {
                    if (priority.name().equals(item.getGroupid())) {
                        isFound = true;
                        MHorizontalLayout priorityLayout = new MHorizontalLayout().withWidth("100%");
                        ButtonI18nComp userLbl = new ButtonI18nComp(priority.name(), priority, listener);
                        Resource iconPriority = new ExternalResource(ProjectResources.getIconResourceLink12ByBugPriority(priority.name()));
                        userLbl.setIcon(iconPriority);
                        userLbl.setWidth("110px");
                        userLbl.setStyleName(UIConstants.THEME_LINK);

                        ProgressBarIndicator indicator = new ProgressBarIndicator(totalCount, totalCount - item.getValue(), false);
                        indicator.setWidth("100%");

                        priorityLayout.with(userLbl, indicator).expand(indicator);

                        this.bodyContent.addComponent(priorityLayout);
                    }
                }

                if (!isFound) {
                    MHorizontalLayout priorityLayout = new MHorizontalLayout().withWidth("100%");
                    Button userLbl = new ButtonI18nComp(priority.name(), priority, listener);
                    Resource iconPriority = new ExternalResource(
                            ProjectResources.getIconResourceLink12ByBugPriority(priority.name()));
                    userLbl.setIcon(iconPriority);
                    userLbl.setWidth("110px");
                    userLbl.setStyleName(UIConstants.THEME_LINK);
                    ProgressBarIndicator indicator = new ProgressBarIndicator(totalCount, totalCount, false);
                    indicator.setWidth("100%");
                    priorityLayout.with(userLbl, indicator).expand(indicator);
                    this.bodyContent.addComponent(priorityLayout);
                }
            }

        }
    }

    private class BugPriorityClickListener implements Button.ClickListener {
        private static final long serialVersionUID = 1L;

        @Override
        public void buttonClick(final ClickEvent event) {
            String key = ((ButtonI18nComp) event.getButton()).getKey();
            bugSearchCriteria.setPriorities(new SetSearchField<>(new String[]{key}));
            EventBusFactory.getInstance().post(new BugEvent.SearchRequest(this, bugSearchCriteria));
        }
    }
}
