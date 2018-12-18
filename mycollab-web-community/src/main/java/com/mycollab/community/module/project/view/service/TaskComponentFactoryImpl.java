/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.community.module.project.view.service;

import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Span;
import com.mycollab.common.i18n.FollowerI18nEnum;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.ShellI18nEnum;
import com.mycollab.community.vaadin.web.ui.field.MetaFieldBuilder;
import com.mycollab.core.utils.NumberUtils;
import com.mycollab.module.file.StorageUtils;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.SimpleTask;
import com.mycollab.module.project.i18n.MilestoneI18nEnum;
import com.mycollab.module.project.i18n.TaskI18nEnum;
import com.mycollab.module.project.i18n.TimeTrackingI18nEnum;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.view.service.TaskComponentFactory;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.UIConstants;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.AbstractComponent;
import org.springframework.stereotype.Service;

/**
 * @author MyCollab Ltd
 * @since 5.1.2
 */
@Service
public class TaskComponentFactoryImpl implements TaskComponentFactory {

    @Override
    public AbstractComponent createPriorityPopupField(SimpleTask task) {
        return new MetaFieldBuilder().withCaption(ProjectAssetsManager.getPriorityHtml(task.getPriority()))
                .withDescription(UserUIContext.getMessage(GenericI18Enum.FORM_PRIORITY_HELP)).build();
    }

    @Override
    public AbstractComponent createAssigneePopupField(SimpleTask task) {
        String avatarLink = StorageUtils.getAvatarPath(task.getAssignUserAvatarId(), 16);
        Img img = new Img(task.getAssignUserFullName(), avatarLink).setCSSClass(UIConstants.CIRCLE_BOX)
                .setTitle(task.getAssignUserFullName());
        return new MetaFieldBuilder().withCaption(img.write())
                .withDescription(UserUIContext.getMessage(ShellI18nEnum.OPT_UPGRADE_PRO_INTRO,
                        UserUIContext.getMessage(GenericI18Enum.FORM_ASSIGNEE))).build();
    }

    @Override
    public AbstractComponent createCommentsPopupField(SimpleTask task) {
        return new MetaFieldBuilder().withCaption(VaadinIcons.COMMENT_O.getHtml() + " " + NumberUtils.zeroIfNull(task.getNumComments()))
                .withDescription(UserUIContext.getMessage(ShellI18nEnum.OPT_UPGRADE_PRO_INTRO,
                        UserUIContext.getMessage(GenericI18Enum.OPT_COMMENTS))).build();
    }

    @Override
    public AbstractComponent createStatusPopupField(SimpleTask task) {
        return new MetaFieldBuilder().withCaptionAndIcon(VaadinIcons.INFO_CIRCLE, task.getStatus()).withDescription
                (UserUIContext.getMessage(ShellI18nEnum.OPT_UPGRADE_PRO_INTRO,
                        UserUIContext.getMessage(GenericI18Enum.FORM_STATUS))).build();
    }

    @Override
    public AbstractComponent createPercentagePopupField(SimpleTask task) {
        if (task.getPercentagecomplete() != null && task.getPercentagecomplete() > 0) {
            return new MetaFieldBuilder().withCaptionAndIcon(VaadinIcons.CALENDAR_CLOCK,
                    String.format(" %s%%", task.getPercentagecomplete()))
                    .withDescription(UserUIContext.getMessage(ShellI18nEnum.OPT_UPGRADE_PRO_INTRO,
                            UserUIContext.getMessage(TaskI18nEnum.FORM_PERCENTAGE_COMPLETE))).build();
        } else {
            Div divHint = new Div().setCSSClass("nonValue");
            divHint.appendText(VaadinIcons.CALENDAR_CLOCK.getHtml());
            divHint.appendChild(new Span().appendText(UserUIContext.getMessage(GenericI18Enum.OPT_UNDEFINED)).setCSSClass("hide"));
            return new MetaFieldBuilder().withCaption(divHint.write())
                    .withDescription(UserUIContext.getMessage(ShellI18nEnum.OPT_UPGRADE_PRO_INTRO,
                            UserUIContext.getMessage(TaskI18nEnum.FORM_PERCENTAGE_COMPLETE))).build();
        }
    }

    @Override
    public AbstractComponent createMilestonePopupField(SimpleTask task) {
        if (task.getMilestoneid() == null) {
            Div divHint = new Div().setCSSClass("nonValue");
            divHint.appendText(ProjectAssetsManager.getAsset(ProjectTypeConstants.MILESTONE).getHtml());
            divHint.appendChild(new Span().appendText(UserUIContext.getMessage(GenericI18Enum.OPT_UNDEFINED)).setCSSClass("hide"));
            return new MetaFieldBuilder().withCaption(divHint.write())
                    .withDescription(UserUIContext.getMessage(ShellI18nEnum.OPT_UPGRADE_PRO_INTRO,
                            UserUIContext.getMessage(MilestoneI18nEnum.SINGLE))).build();
        } else {
            return new MetaFieldBuilder().withCaptionAndIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.MILESTONE), task
                    .getMilestoneName()).withDescription(UserUIContext.getMessage(ShellI18nEnum.OPT_UPGRADE_PRO_INTRO,
                    UserUIContext.getMessage(MilestoneI18nEnum.SINGLE))).build();
        }
    }

    @Override
    public AbstractComponent createDeadlinePopupField(SimpleTask task) {
        if (task.getDeadlineRoundPlusOne() == null) {
            Div divHint = new Div().setCSSClass("nonValue");
            divHint.appendText(VaadinIcons.CLOCK.getHtml());
            divHint.appendChild(new Span().appendText(UserUIContext.getMessage(GenericI18Enum.OPT_UNDEFINED)).setCSSClass("hide"));
            return new MetaFieldBuilder().withCaption(divHint.write())
                    .withDescription(UserUIContext.getMessage(ShellI18nEnum.OPT_UPGRADE_PRO_INTRO,
                            UserUIContext.getMessage(GenericI18Enum.FORM_DUE_DATE))).build();
        } else {
            return new MetaFieldBuilder().withCaption(String.format(" %s %s", VaadinIcons.CLOCK.getHtml(),
                    UserUIContext.formatPrettyTime(task.getDeadlineRoundPlusOne())))
                    .withDescription(UserUIContext.getMessage(ShellI18nEnum.OPT_UPGRADE_PRO_INTRO,
                            UserUIContext.getMessage(GenericI18Enum.FORM_DUE_DATE))).build();
        }
    }

    @Override
    public AbstractComponent createStartDatePopupField(SimpleTask task) {
        if (task.getStartdate() == null) {
            Div divHint = new Div().setCSSClass("nonValue");
            divHint.appendText(VaadinIcons.TIME_FORWARD.getHtml());
            divHint.appendChild(new Span().appendText(UserUIContext.getMessage(GenericI18Enum.OPT_UNDEFINED)).setCSSClass("hide"));
            return new MetaFieldBuilder().withCaption(divHint.write())
                    .withDescription(UserUIContext.getMessage(ShellI18nEnum.OPT_UPGRADE_PRO_INTRO,
                            UserUIContext.getMessage(GenericI18Enum.FORM_START_DATE))).build();
        } else {
            return new MetaFieldBuilder().withCaption(String.format(" %s %s", VaadinIcons.TIME_FORWARD.getHtml(),
                    UserUIContext.formatDate(task.getStartdate())))
                    .withDescription(UserUIContext.getMessage(ShellI18nEnum.OPT_UPGRADE_PRO_INTRO,
                            UserUIContext.getMessage(GenericI18Enum.FORM_START_DATE))).build();
        }
    }

    @Override
    public AbstractComponent createEndDatePopupField(SimpleTask task) {
        if (task.getEnddate() == null) {
            Div divHint = new Div().setCSSClass("nonValue");
            divHint.appendText(VaadinIcons.TIME_BACKWARD.getHtml());
            divHint.appendChild(new Span().appendText(UserUIContext.getMessage(GenericI18Enum.OPT_UNDEFINED)).setCSSClass("hide"));
            return new MetaFieldBuilder().withCaption(divHint.write())
                    .withDescription(UserUIContext.getMessage(ShellI18nEnum.OPT_UPGRADE_PRO_INTRO,
                            UserUIContext.getMessage(GenericI18Enum.FORM_END_DATE))).build();
        } else {
            return new MetaFieldBuilder().withCaption(String.format(" %s %s", VaadinIcons.TIME_BACKWARD.getHtml(),
                    UserUIContext.formatDate(task.getEnddate())))
                    .withDescription(UserUIContext.getMessage(ShellI18nEnum.OPT_UPGRADE_PRO_INTRO,
                            UserUIContext.getMessage(GenericI18Enum.FORM_END_DATE))).build();
        }
    }

    @Override
    public AbstractComponent createFollowersPopupField(SimpleTask task) {
        return new MetaFieldBuilder().withCaptionAndIcon(VaadinIcons.EYE, "" + NumberUtils.zeroIfNull(task.getNumFollowers()))
                .withDescription(UserUIContext.getMessage(FollowerI18nEnum.OPT_SUB_INFO_WATCHERS)).build();
    }

    @Override
    public AbstractComponent createBillableHoursPopupField(SimpleTask task) {
        return new MetaFieldBuilder().withCaptionAndIcon(VaadinIcons.MONEY, "" + NumberUtils.zeroIfNull(task.getBillableHours()))
                .withDescription(UserUIContext.getMessage(TimeTrackingI18nEnum.OPT_BILLABLE_HOURS)).build();
    }

    @Override
    public AbstractComponent createNonBillableHoursPopupField(SimpleTask task) {
        return new MetaFieldBuilder().withCaptionAndIcon(VaadinIcons.GIFT, "" + NumberUtils.zeroIfNull(task.getNonBillableHours()))
                .withDescription(UserUIContext.getMessage(TimeTrackingI18nEnum.OPT_NON_BILLABLE_HOURS)).build();
    }
}
