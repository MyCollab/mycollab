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
package com.mycollab.module.project.view.bug.components;

import com.mycollab.common.domain.GroupItem;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.project.events.BugEvent;
import com.mycollab.module.project.i18n.BugI18nEnum;
import com.mycollab.module.project.view.bug.IBugAssigneeChartWidget;
import com.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.mycollab.module.tracker.service.BugService;
import com.mycollab.module.user.CommonTooltipGenerator;
import com.mycollab.module.user.domain.SimpleUser;
import com.mycollab.module.user.service.UserService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.mycollab.vaadin.web.ui.DepotWithChart;
import com.mycollab.vaadin.web.ui.ProgressBarIndicator;
import com.mycollab.vaadin.web.ui.WebUIConstants;
import com.vaadin.ui.Alignment;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class UnresolvedBugsByAssigneeWidget extends DepotWithChart {
    private static final long serialVersionUID = 1L;

    private BugSearchCriteria searchCriteria;
    private List<GroupItem> groupItems;
    private int totalCount;

    public void setSearchCriteria(final BugSearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
        BugService bugService = AppContextUtil.getSpringBean(BugService.class);
        totalCount = bugService.getTotalCount(searchCriteria);
        setTitle(AppContext.getMessage(BugI18nEnum.WIDGET_UNRESOLVED_BY_ASSIGNEE_TITLE) + " (" + totalCount + ")");
        groupItems = bugService.getAssignedDefectsSummary(searchCriteria);
        displayPlainMode();
    }

    @Override
    protected void displayPlainMode() {
        bodyContent.removeAllComponents();
        if (!groupItems.isEmpty()) {
            for (GroupItem item : groupItems) {
                MHorizontalLayout assigneeLayout = new MHorizontalLayout().withFullWidth();
                assigneeLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
                String assignUser = item.getGroupid();
                String assignUserFullName = item.getGroupid() == null ? AppContext.getMessage(GenericI18Enum.OPT_UNDEFINED) :
                        item.getGroupname();
                if (assignUserFullName == null || "".equals(assignUserFullName.trim())) {
                    assignUserFullName = StringUtils.extractNameFromEmail(assignUser);
                }
                BugAssigneeLink userLbl = new BugAssigneeLink(assignUser, item.getExtraValue(), assignUserFullName);
                assigneeLayout.addComponent(userLbl);
                ProgressBarIndicator indicator = new ProgressBarIndicator(totalCount, totalCount - item.getValue().intValue(),
                        false);
                indicator.setWidth("100%");
                assigneeLayout.with(indicator).expand(indicator);
                bodyContent.addComponent(assigneeLayout);
            }
        }
    }

    @Override
    protected void displayChartMode() {
        bodyContent.removeAllComponents();
        IBugAssigneeChartWidget bugAssigneeChartWidget = ViewManager.getCacheComponent(IBugAssigneeChartWidget.class);
        bugAssigneeChartWidget.displayChart(searchCriteria);
        bodyContent.addComponent(bugAssigneeChartWidget);
    }

    class BugAssigneeLink extends MButton {
        private static final long serialVersionUID = 1L;

        BugAssigneeLink(final String assignee, final String assigneeAvatarId, final String assigneeFullName) {
            super(StringUtils.trim(assigneeFullName, 25, true));

            this.withListener(clickEvent -> {
                searchCriteria.setAssignuser(StringSearchField.and(assignee));
                EventBusFactory.getInstance().post(new BugEvent.SearchRequest(this, searchCriteria));
            }).withIcon(UserAvatarControlFactory.createAvatarResource(assigneeAvatarId, 16))
                    .withStyleName(WebUIConstants.BUTTON_LINK, UIConstants.TEXT_ELLIPSIS).withWidth("110px");
            UserService service = AppContextUtil.getSpringBean(UserService.class);
            SimpleUser user = service.findUserByUserNameInAccount(assignee, AppContext.getAccountId());
            this.setDescription(CommonTooltipGenerator.generateTooltipUser(AppContext.getUserLocale(), user,
                    AppContext.getSiteUrl(), AppContext.getUserTimeZone()));
        }
    }
}