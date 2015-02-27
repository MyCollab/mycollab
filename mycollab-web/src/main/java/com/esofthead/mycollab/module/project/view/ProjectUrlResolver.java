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
package com.esofthead.mycollab.module.project.view;

import com.esofthead.mycollab.common.UrlTokenizer;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.events.FollowingTicketEvent;
import com.esofthead.mycollab.module.project.events.ProjectEvent;
import com.esofthead.mycollab.module.project.events.TimeTrackingEvent;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.project.view.bug.BugUrlResolver;
import com.esofthead.mycollab.module.project.view.file.ProjectFileUrlResolver;
import com.esofthead.mycollab.module.project.view.message.MessageUrlResolver;
import com.esofthead.mycollab.module.project.view.milestone.MilestoneUrlResolver;
import com.esofthead.mycollab.module.project.view.page.PageUrlResolver;
import com.esofthead.mycollab.module.project.view.parameters.ProjectScreenData;
import com.esofthead.mycollab.module.project.view.problem.ProblemUrlResolver;
import com.esofthead.mycollab.module.project.view.risk.RiskUrlResolver;
import com.esofthead.mycollab.module.project.view.settings.RoleUrlResolver;
import com.esofthead.mycollab.module.project.view.settings.SettingUrlResolver;
import com.esofthead.mycollab.module.project.view.settings.UserUrlResolver;
import com.esofthead.mycollab.module.project.view.standup.StandupUrlResolver;
import com.esofthead.mycollab.module.project.view.task.ScheduleUrlResolver;
import com.esofthead.mycollab.module.project.view.time.TimeUrlResolver;
import com.esofthead.mycollab.shell.events.ShellEvent;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.desktop.ui.ModuleHelper;
import com.esofthead.mycollab.vaadin.mvp.PageActionChain;
import com.esofthead.mycollab.vaadin.mvp.UrlResolver;
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectUrlResolver extends UrlResolver {
    public UrlResolver build() {
        this.addSubResolver("dashboard", new ProjectPageUrlResolver());
        this.addSubResolver("tag", new ProjectTagUrlResolver());
        this.addSubResolver("message", new MessageUrlResolver());
        this.addSubResolver("milestone", new MilestoneUrlResolver());
        this.addSubResolver("task", new ScheduleUrlResolver());
        this.addSubResolver("bug", new BugUrlResolver());
        this.addSubResolver("page", new PageUrlResolver());
        this.addSubResolver("risk", new RiskUrlResolver());
        this.addSubResolver("problem", new ProblemUrlResolver());
        this.addSubResolver("standup", new StandupUrlResolver());
        this.addSubResolver("user", new UserUrlResolver());
        this.addSubResolver("role", new RoleUrlResolver());
        this.addSubResolver("setting", new SettingUrlResolver());
        this.addSubResolver("time", new TimeUrlResolver());
        this.addSubResolver("file", new ProjectFileUrlResolver());
        this.addSubResolver("following", new FollowingTicketsResolver());
        this.addSubResolver("timetracking", new TimeTrackingResolver());
        return this;
    }

    @Override
    public void handle(String... params) {
        if (!ModuleHelper.isCurrentProjectModule()) {
            EventBusFactory.getInstance().post(
                    new ShellEvent.GotoProjectModule(this, params));
        } else {
            super.handle(params);
        }
    }

    @Override
    protected void defaultPageErrorHandler() {
        EventBusFactory.getInstance().post(
                new ShellEvent.GotoProjectModule(this, null));
    }

    public static class ProjectTagUrlResolver extends ProjectUrlResolver {
        @Override
        protected void handlePage(String... params) {
            int projectId = new UrlTokenizer(params[0]).getInt();
            PageActionChain chain = new PageActionChain(
                    new ProjectScreenData.Goto(projectId), new ProjectScreenData.GotoTagList(null));
            EventBusFactory.getInstance().post(
                    new ProjectEvent.GotoMyProject(this, chain));
        }
    }

    public static class ProjectPageUrlResolver extends ProjectUrlResolver {
        @Override
        protected void handlePage(String... params) {
            if (ArrayUtils.isEmpty(params)) {
                EventBusFactory.getInstance().post(
                        new ShellEvent.GotoProjectModule(this, null));
            } else {
                int projectId = new UrlTokenizer(params[0]).getInt();
                PageActionChain chain = new PageActionChain(
                        new ProjectScreenData.Goto(projectId));
                EventBusFactory.getInstance().post(
                        new ProjectEvent.GotoMyProject(this, chain));
            }

        }
    }

    private static class FollowingTicketsResolver extends ProjectUrlResolver {
        @Override
        protected void handlePage(String... params) {
            ProjectService prjService = ApplicationContextUtil
                    .getSpringBean(ProjectService.class);
            List<Integer> prjKeys = prjService.getProjectKeysUserInvolved(
                    AppContext.getUsername(), AppContext.getAccountId());
            EventBusFactory.getInstance().post(new FollowingTicketEvent.GotoMyFollowingItems(this, prjKeys));
        }
    }

    private static class TimeTrackingResolver extends ProjectUrlResolver {
        @Override
        protected void handlePage(String... params) {
            ProjectService prjService = ApplicationContextUtil
                    .getSpringBean(ProjectService.class);
            List<Integer> prjKeys = prjService.getProjectKeysUserInvolved(
                    AppContext.getUsername(), AppContext.getAccountId());
            EventBusFactory.getInstance().post(new TimeTrackingEvent.GotoTimeTrackingView(this, prjKeys));
        }
    }
}
