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
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.events.BugEvent;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.project.view.parameters.BugFilterParameter;
import com.esofthead.mycollab.module.project.view.parameters.BugScreenData;
import com.esofthead.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.module.user.CommonTooltipGenerator;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.Depot;
import com.esofthead.mycollab.vaadin.ui.ProgressBarIndicator;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.vaadin.ui.Button;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class UnresolvedBugsByAssigneeWidget2 extends Depot {
    private static final long serialVersionUID = 1L;

    private BugSearchCriteria bugSearchCriteria;

    public UnresolvedBugsByAssigneeWidget2() {
        super("", new MVerticalLayout());
        setContentBorder(true);
    }

    public void setSearchCriteria(final BugSearchCriteria searchCriteria) {
        bugSearchCriteria = searchCriteria;
        bodyContent.removeAllComponents();
        BugService bugService = ApplicationContextUtil.getSpringBean(BugService.class);
        int totalCount = bugService.getTotalCount(searchCriteria);
        setTitle(AppContext
                .getMessage(BugI18nEnum.WIDGET_UNRESOLVED_BY_ASSIGNEE_TITLE) + " (" + totalCount + ")");

        final List<GroupItem> groupItems = bugService
                .getAssignedDefectsSummary(searchCriteria);
        if (!groupItems.isEmpty()) {
            for (GroupItem item : groupItems) {
                MHorizontalLayout assigneeLayout = new MHorizontalLayout().withWidth("100%");

                String assignUser = item.getGroupid();
                String assignUserFullName = item.getGroupid() == null ? AppContext
                        .getMessage(BugI18nEnum.OPT_UNDEFINED_USER) : item
                        .getGroupname();
                if (assignUserFullName == null
                        || "".equals(assignUserFullName.trim())) {
                    assignUserFullName = StringUtils
                            .extractNameFromEmail(assignUser);
                }
                BugAssigneeLink userLbl = new BugAssigneeLink(assignUser,
                        item.getExtraValue(), assignUserFullName);
                assigneeLayout.addComponent(userLbl);
                ProgressBarIndicator indicator = new ProgressBarIndicator(
                        totalCount, totalCount - item.getValue(), false);
                indicator.setWidth("100%");
                assigneeLayout.with(indicator).expand(indicator);
                bodyContent.addComponent(assigneeLayout);
            }

        }
    }

    class BugAssigneeLink extends Button {
        private static final long serialVersionUID = 1L;

        public BugAssigneeLink(final String assignee,
                               final String assigneeAvatarId, final String assigneeFullName) {
            super(assigneeFullName, new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final ClickEvent event) {
                    bugSearchCriteria.setAssignuser(new StringSearchField(SearchField.AND, assignee));
                    BugFilterParameter param = new BugFilterParameter("Unresolved Bugs of " + assigneeFullName,
                            bugSearchCriteria);
                    EventBusFactory.getInstance().post(
                            new BugEvent.GotoList(this, new BugScreenData.Search(param)));
                }
            });

            this.setStyleName("link");
            this.setWidth("110px");
            this.addStyleName(UIConstants.TEXT_ELLIPSIS);
            this.setIcon(UserAvatarControlFactory.createAvatarResource(assigneeAvatarId, 16));
            UserService service = ApplicationContextUtil
                    .getSpringBean(UserService.class);
            SimpleUser user = service.findUserByUserNameInAccount(assignee,
                    AppContext.getAccountId());
            this.setDescription(CommonTooltipGenerator.generateTooltipUser(AppContext.getUserLocale(), user,
                    AppContext.getSiteUrl(), AppContext.getTimezone()));
        }
    }
}