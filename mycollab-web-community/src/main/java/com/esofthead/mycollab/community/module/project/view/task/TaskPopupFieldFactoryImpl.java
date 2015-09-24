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

import com.esofthead.mycollab.configuration.Storage;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.module.project.view.task.TaskPopupFieldFactory;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.form.field.PopupBeanField;
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
        return new PopupBeanField(ProjectAssetsManager.getTaskPriorityHtml(task.getPriority()));
    }

    @Override
    public PopupView createTaskAssigneePopupField(SimpleTask task) {
        String avatarLink = Storage.getAvatarPath(task.getAssignUserAvatarId(), 16);
        Img img = new Img(task.getAssignUserFullName(), avatarLink).setTitle(task.getAssignUserFullName());
        return new PopupBeanField(img.write());
    }

    @Override
    public PopupView createTaskCommentsPopupField(SimpleTask task) {
        return new PopupBeanField(FontAwesome.COMMENT_O.getHtml() + " " + task.getNumComments());
    }

    @Override
    public PopupView createTaskStatusPopupField(SimpleTask task) {
        return new PopupBeanField(FontAwesome.INFO_CIRCLE, task.getStatus());
    }

    @Override
    public PopupView createTaskPercentagePopupField(SimpleTask task) {
        if (task.getPercentagecomplete() != null && task.getPercentagecomplete() > 0) {
            return new PopupBeanField(VaadinIcons.CALENDAR_CLOCK.getHtml() + " " + String.format(" %s%%",
                    task.getPercentagecomplete()));
        } else {
            Div divHint = new Div().setCSSClass("nonValue");
            divHint.appendText(VaadinIcons.CALENDAR_CLOCK.getHtml());
            divHint.appendChild(new Span().appendText(" Click to edit").setCSSClass("hide"));
            return new PopupBeanField(divHint.write());
        }
    }

    @Override
    public PopupView createTaskMilestonePopupField(SimpleTask task) {
        if (task.getMilestoneid() == null) {
            Div divHint = new Div().setCSSClass("nonValue");
            divHint.appendText(ProjectAssetsManager.getAsset(ProjectTypeConstants.MILESTONE).getHtml());
            divHint.appendChild(new Span().appendText(" Click to edit").setCSSClass("hide"));
            return new PopupBeanField(divHint.write());
        } else {
            return new PopupBeanField(ProjectAssetsManager.getAsset(ProjectTypeConstants.MILESTONE), task.getMilestoneName());
        }
    }

    @Override
    public PopupView createTaskDeadlinePopupField(SimpleTask task) {
        if (task.getDeadlineRoundPlusOne() == null) {
            Div divHint = new Div().setCSSClass("nonValue");
            divHint.appendText(FontAwesome.CLOCK_O.getHtml());
            divHint.appendChild(new Span().appendText(" Click to edit").setCSSClass("hide"));
            return new PopupBeanField(divHint.write());
        } else {
            return new PopupBeanField(String.format(" %s %s", FontAwesome.CLOCK_O.getHtml(),
                    AppContext.formatPrettyTime(task.getDeadlineRoundPlusOne())));
        }
    }
}
