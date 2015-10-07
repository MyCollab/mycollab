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
package com.esofthead.mycollab.community.module.project.view.task;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.configuration.StorageFactory;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.i18n.TaskI18nEnum;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.module.project.view.task.TaskPopupFieldFactory;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.form.field.PopupFieldBuilder;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Span;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.PopupView;
import org.vaadin.teemu.VaadinIcons;

/**
 * @author MyCollab Ltd
 * @since 5.1.2
 */
@ViewComponent
public class TaskPopupFieldFactoryImpl implements TaskPopupFieldFactory {

    @Override
    public PopupView createTaskPriorityPopupField(SimpleTask task) {
        return new PopupFieldBuilder().withCaption(ProjectAssetsManager.getTaskPriorityHtml(task.getPriority()))
                .withDescription(AppContext.getMessage(TaskI18nEnum.FORM_PRIORITY)).build();
    }

    @Override
    public PopupView createTaskAssigneePopupField(SimpleTask task) {
        String avatarLink = StorageFactory.getInstance().getAvatarPath(task.getAssignUserAvatarId(), 16);
        Img img = new Img(task.getAssignUserFullName(), avatarLink).setTitle(task.getAssignUserFullName());
        return new PopupFieldBuilder().withCaption(img.write()).withDescription(AppContext.getMessage(GenericI18Enum
                .FORM_ASSIGNEE)).build();
    }

    @Override
    public PopupView createTaskCommentsPopupField(SimpleTask task) {
        return new PopupFieldBuilder().withCaption(FontAwesome.COMMENT_O.getHtml() + " " + task.getNumComments())
                .withDescription("Comments").build();
    }

    @Override
    public PopupView createTaskStatusPopupField(SimpleTask task) {
        return new PopupFieldBuilder().withCaptionAndIcon(FontAwesome.INFO_CIRCLE, task.getStatus()).withDescription
                (AppContext.getMessage(TaskI18nEnum.FORM_STATUS)).build();
    }

    @Override
    public PopupView createTaskPercentagePopupField(SimpleTask task) {
        if (task.getPercentagecomplete() != null && task.getPercentagecomplete() > 0) {
            return new PopupFieldBuilder().withCaptionAndIcon(VaadinIcons.CALENDAR_CLOCK,
                    String.format(" %s%%", task.getPercentagecomplete())).withDescription("Percentage complete").build();
        } else {
            Div divHint = new Div().setCSSClass("nonValue");
            divHint.appendText(VaadinIcons.CALENDAR_CLOCK.getHtml());
            divHint.appendChild(new Span().appendText(" Click to edit").setCSSClass("hide"));
            return new PopupFieldBuilder().withCaption(divHint.write()).withDescription("Percentage complete").build();
        }
    }

    @Override
    public PopupView createTaskMilestonePopupField(SimpleTask task) {
        if (task.getMilestoneid() == null) {
            Div divHint = new Div().setCSSClass("nonValue");
            divHint.appendText(ProjectAssetsManager.getAsset(ProjectTypeConstants.MILESTONE).getHtml());
            divHint.appendChild(new Span().appendText(" Click to edit").setCSSClass("hide"));
            return new PopupFieldBuilder().withCaption(divHint.write()).withDescription("Milestone").build();
        } else {
            return new PopupFieldBuilder().withCaptionAndIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.MILESTONE), task
                    .getMilestoneName()).withDescription("Milestone").build();
        }
    }

    @Override
    public PopupView createTaskDeadlinePopupField(SimpleTask task) {
        if (task.getDeadlineRoundPlusOne() == null) {
            Div divHint = new Div().setCSSClass("nonValue");
            divHint.appendText(FontAwesome.CLOCK_O.getHtml());
            divHint.appendChild(new Span().appendText(" Click to edit").setCSSClass("hide"));
            return new PopupFieldBuilder().withCaption(divHint.write()).withDescription("Deadline").build();
        } else {
            return new PopupFieldBuilder().withCaption(String.format(" %s %s", FontAwesome.CLOCK_O.getHtml(),
                    AppContext.formatPrettyTime(task.getDeadlineRoundPlusOne()))).withDescription("Deadline").build();
        }
    }

    @Override
    public PopupView createTaskBillableHoursPopupField(SimpleTask task) {
        return new PopupFieldBuilder().withCaptionAndIcon(FontAwesome.MONEY, "" + task.getBillableHours())
                .withDescription("Billable hours").build();
    }

    @Override
    public PopupView createTaskNonBillableHoursPopupField(SimpleTask task) {
        return new PopupFieldBuilder().withCaptionAndIcon(FontAwesome.GIFT, "" + task.getNonBillableHours())
                .withDescription("Non billable hours").build();
    }
}
