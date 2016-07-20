/**
 * This file is part of mycollab-web-community.
 *
 * mycollab-web-community is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web-community is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web-community.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.community.module.project.view.task;

import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Span;
import com.mycollab.common.i18n.FollowerI18nEnum;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.community.vaadin.web.ui.field.MetaFieldBuilder;
import com.mycollab.configuration.StorageFactory;
import com.mycollab.core.utils.NumberUtils;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.SimpleTask;
import com.mycollab.module.project.i18n.TaskI18nEnum;
import com.mycollab.module.project.i18n.TimeTrackingI18nEnum;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.view.task.TaskPopupFieldFactory;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.web.ui.UIConstants;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.AbstractComponent;
import org.vaadin.teemu.VaadinIcons;

/**
 * @author MyCollab Ltd
 * @since 5.1.2
 */
@ViewComponent
public class TaskPopupFieldFactoryImpl implements TaskPopupFieldFactory {

    @Override
    public AbstractComponent createPriorityPopupField(SimpleTask task) {
        return new MetaFieldBuilder().withCaption(ProjectAssetsManager.getTaskPriorityHtml(task.getPriority()))
                .withDescription(AppContext.getMessage(TaskI18nEnum.FORM_PRIORITY_HELP)).build();
    }

    @Override
    public AbstractComponent createAssigneePopupField(SimpleTask task) {
        String avatarLink = StorageFactory.getAvatarPath(task.getAssignUserAvatarId(), 16);
        Img img = new Img(task.getAssignUserFullName(), avatarLink).setCSSClass(UIConstants.CIRCLE_BOX)
                .setTitle(task.getAssignUserFullName());
        return new MetaFieldBuilder().withCaption(img.write()).withDescription(AppContext.getMessage(GenericI18Enum
                .FORM_ASSIGNEE)).build();
    }

    @Override
    public AbstractComponent createCommentsPopupField(SimpleTask task) {
        return new MetaFieldBuilder().withCaption(FontAwesome.COMMENT_O.getHtml() + " " + NumberUtils.zeroIfNull(task.getNumComments()))
                .withDescription("Comments").build();
    }

    @Override
    public AbstractComponent createStatusPopupField(SimpleTask task) {
        return new MetaFieldBuilder().withCaptionAndIcon(FontAwesome.INFO_CIRCLE, task.getStatus()).withDescription
                (AppContext.getMessage(GenericI18Enum.FORM_STATUS)).build();
    }

    @Override
    public AbstractComponent createPercentagePopupField(SimpleTask task) {
        if (task.getPercentagecomplete() != null && task.getPercentagecomplete() > 0) {
            return new MetaFieldBuilder().withCaptionAndIcon(VaadinIcons.CALENDAR_CLOCK,
                    String.format(" %s%%", task.getPercentagecomplete())).withDescription("Percentage complete").build();
        } else {
            Div divHint = new Div().setCSSClass("nonValue");
            divHint.appendText(VaadinIcons.CALENDAR_CLOCK.getHtml());
            divHint.appendChild(new Span().appendText(" Percentage complete is not set").setCSSClass("hide"));
            return new MetaFieldBuilder().withCaption(divHint.write()).withDescription("Percentage complete").build();
        }
    }

    @Override
    public AbstractComponent createMilestonePopupField(SimpleTask task) {
        if (task.getMilestoneid() == null) {
            Div divHint = new Div().setCSSClass("nonValue");
            divHint.appendText(ProjectAssetsManager.getAsset(ProjectTypeConstants.MILESTONE).getHtml());
            divHint.appendChild(new Span().appendText(" Milestone is not set").setCSSClass("hide"));
            return new MetaFieldBuilder().withCaption(divHint.write()).withDescription("Milestone").build();
        } else {
            return new MetaFieldBuilder().withCaptionAndIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.MILESTONE), task
                    .getMilestoneName()).withDescription("Milestone").build();
        }
    }

    @Override
    public AbstractComponent createDeadlinePopupField(SimpleTask task) {
        if (task.getDeadlineRoundPlusOne() == null) {
            Div divHint = new Div().setCSSClass("nonValue");
            divHint.appendText(FontAwesome.CLOCK_O.getHtml());
            divHint.appendChild(new Span().appendText(" Deadline is not set").setCSSClass("hide"));
            return new MetaFieldBuilder().withCaption(divHint.write()).withDescription("Deadline").build();
        } else {
            return new MetaFieldBuilder().withCaption(String.format(" %s %s", FontAwesome.CLOCK_O.getHtml(),
                    AppContext.formatPrettyTime(task.getDeadlineRoundPlusOne()))).withDescription("Deadline").build();
        }
    }

    @Override
    public AbstractComponent createStartDatePopupField(SimpleTask task) {
        if (task.getStartdate() == null) {
            Div divHint = new Div().setCSSClass("nonValue");
            divHint.appendText(VaadinIcons.TIME_FORWARD.getHtml());
            divHint.appendChild(new Span().appendText(" Start date is not set").setCSSClass("hide"));
            return new MetaFieldBuilder().withCaption(divHint.write()).withDescription("Start date").build();
        } else {
            return new MetaFieldBuilder().withCaption(String.format(" %s %s", VaadinIcons.TIME_FORWARD.getHtml(),
                    AppContext.formatDate(task.getStartdate()))).withDescription("Start date").build();
        }
    }

    @Override
    public AbstractComponent createEndDatePopupField(SimpleTask task) {
        if (task.getEnddate() == null) {
            Div divHint = new Div().setCSSClass("nonValue");
            divHint.appendText(VaadinIcons.TIME_BACKWARD.getHtml());
            divHint.appendChild(new Span().appendText(" End date is not set").setCSSClass("hide"));
            return new MetaFieldBuilder().withCaption(divHint.write()).withDescription("End date").build();
        } else {
            return new MetaFieldBuilder().withCaption(String.format(" %s %s", VaadinIcons.TIME_BACKWARD.getHtml(),
                    AppContext.formatDate(task.getEnddate()))).withDescription("End date").build();
        }
    }

    @Override
    public AbstractComponent createFollowersPopupField(SimpleTask task) {
        return new MetaFieldBuilder().withCaptionAndIcon(FontAwesome.EYE, "" + NumberUtils.zeroIfNull(task.getNumFollowers()))
                .withDescription(AppContext.getMessage(FollowerI18nEnum.OPT_SUB_INFO_WATCHERS)).build();
    }

    @Override
    public AbstractComponent createBillableHoursPopupField(SimpleTask task) {
        return new MetaFieldBuilder().withCaptionAndIcon(FontAwesome.MONEY, "" + NumberUtils.zeroIfNull(task.getBillableHours()))
                .withDescription(AppContext.getMessage(TimeTrackingI18nEnum.OPT_BILLABLE_HOURS)).build();
    }

    @Override
    public AbstractComponent createNonBillableHoursPopupField(SimpleTask task) {
        return new MetaFieldBuilder().withCaptionAndIcon(FontAwesome.GIFT, "" + NumberUtils.zeroIfNull(task.getNonBillableHours()))
                .withDescription(AppContext.getMessage(TimeTrackingI18nEnum.OPT_NON_BILLABLE_HOURS)).build();
    }
}
