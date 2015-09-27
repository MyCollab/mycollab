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
package com.esofthead.mycollab.community.module.project.view.bug;

import com.esofthead.mycollab.configuration.Storage;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.module.project.view.bug.BugPopupFieldFactory;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.form.field.PopupBeanField;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Span;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.PopupView;

/**
 * @author MyCollab Ltd
 * @since 5.1.3
 */
@ViewComponent
public class BugPopupFieldFactoryImpl implements BugPopupFieldFactory {

    @Override
    public PopupView createBugPriorityPopupField(SimpleBug bug) {
        return new PopupBeanField(ProjectAssetsManager.getTaskPriorityHtml(bug.getPriority()));
    }

    @Override
    public PopupView createBugAssigneePopupField(SimpleBug bug) {
        String avatarLink = Storage.getAvatarPath(bug.getAssignUserAvatarId(), 16);
        Img img = new Img(bug.getAssignuserFullName(), avatarLink).setTitle(bug.getAssignuserFullName());
        return new PopupBeanField(img.write());
    }

    @Override
    public PopupView createBugCommentsPopupField(SimpleBug bug) {
        if (bug.getNumComments() != null) {
            return new PopupBeanField(FontAwesome.COMMENT_O, "" + bug.getNumComments());
        } else {
            return new PopupBeanField(FontAwesome.COMMENT_O, " 0");
        }
    }

    @Override
    public PopupView createBugMilestonePopupField(SimpleBug bug) {
        if (bug.getMilestoneid() == null) {
            Div divHint = new Div().setCSSClass("nonValue");
            divHint.appendText(ProjectAssetsManager.getAsset(ProjectTypeConstants.MILESTONE).getHtml());
            divHint.appendChild(new Span().appendText(" Click to edit").setCSSClass("hide"));
            return new PopupBeanField(divHint.write());
        } else {
            return new PopupBeanField(ProjectAssetsManager.getAsset(ProjectTypeConstants.MILESTONE), bug.getMilestoneName());
        }
    }

    @Override
    public PopupView createBugStatusPopupField(SimpleBug bug) {
        return new PopupBeanField(FontAwesome.INFO_CIRCLE, bug.getStatus());
    }

    @Override
    public PopupView createBugDeadlinePopupField(SimpleBug bug) {
        if (bug.getDueDateRoundPlusOne() == null) {
            Div divHint = new Div().setCSSClass("nonValue");
            divHint.appendText(FontAwesome.CLOCK_O.getHtml());
            divHint.appendChild(new Span().appendText(" Click to edit").setCSSClass("hide"));
            return new PopupBeanField(divHint.write());
        } else {
            return new PopupBeanField(String.format("%s %s", FontAwesome.CLOCK_O.getHtml(),
                    AppContext.formatPrettyTime(bug.getDueDateRoundPlusOne())));
        }
    }
}
