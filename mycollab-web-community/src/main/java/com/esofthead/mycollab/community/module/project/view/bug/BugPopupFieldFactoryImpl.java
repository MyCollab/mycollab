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

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.configuration.StorageFactory;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.module.project.view.bug.BugPopupFieldFactory;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.community.vaadin.web.ui.field.MetaFieldBuilder;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Span;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.PopupView;
import org.vaadin.teemu.VaadinIcons;

/**
 * @author MyCollab Ltd
 * @since 5.1.3
 */
@ViewComponent
public class BugPopupFieldFactoryImpl implements BugPopupFieldFactory {

    @Override
    public AbstractComponent createPriorityPopupField(SimpleBug bug) {
        return new MetaFieldBuilder().withCaption(ProjectAssetsManager.getTaskPriorityHtml(bug.getPriority()))
                .withDescription("Priority").build();
    }

    @Override
    public AbstractComponent createAssigneePopupField(SimpleBug bug) {
        String avatarLink = StorageFactory.getInstance().getAvatarPath(bug.getAssignUserAvatarId(), 16);
        Img img = new Img(bug.getAssignuserFullName(), avatarLink).setTitle(bug.getAssignuserFullName());
        return new MetaFieldBuilder().withCaption(img.write()).withDescription(AppContext.getMessage(GenericI18Enum
                .FORM_ASSIGNEE)).build();
    }

    @Override
    public AbstractComponent createCommentsPopupField(SimpleBug bug) {
        if (bug.getNumComments() != null) {
            return new MetaFieldBuilder().withCaptionAndIcon(FontAwesome.COMMENT_O, "" + bug.getNumComments())
                    .withDescription("Comments").build();
        } else {
            return new MetaFieldBuilder().withCaptionAndIcon(FontAwesome.COMMENT_O, " 0").withDescription
                    ("Comments").build();
        }
    }

    @Override
    public AbstractComponent createMilestonePopupField(SimpleBug bug) {
        if (bug.getMilestoneid() == null) {
            Div divHint = new Div().setCSSClass("nonValue");
            divHint.appendText(ProjectAssetsManager.getAsset(ProjectTypeConstants.MILESTONE).getHtml());
            divHint.appendChild(new Span().appendText(" Milestone is not set").setCSSClass("hide"));
            return new MetaFieldBuilder().withCaption(divHint.write()).withDescription(AppContext.getMessage
                    (BugI18nEnum.FORM_PHASE)).build();
        } else {
            return new MetaFieldBuilder().withCaptionAndIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.MILESTONE),
                    bug.getMilestoneName()).withDescription(AppContext.getMessage(BugI18nEnum.FORM_PHASE)).build();
        }
    }

    @Override
    public AbstractComponent createStatusPopupField(SimpleBug bug) {
        return new MetaFieldBuilder().withCaptionAndIcon(FontAwesome.INFO_CIRCLE, AppContext.getMessage(OptionI18nEnum.BugStatus
                .class, bug.getStatus())).withDescription(AppContext.getMessage(GenericI18Enum.FORM_STATUS)).build();
    }

    @Override
    public AbstractComponent createDeadlinePopupField(SimpleBug bug) {
        if (bug.getDueDateRoundPlusOne() == null) {
            Div divHint = new Div().setCSSClass("nonValue");
            divHint.appendText(FontAwesome.CLOCK_O.getHtml());
            divHint.appendChild(new Span().appendText(" Deadline is not set").setCSSClass("hide"));
            return new MetaFieldBuilder().withCaption(divHint.write()).withDescription(AppContext.getMessage
                    (GenericI18Enum.FORM_DUE_DATE)).build();
        } else {
            return new MetaFieldBuilder().withCaption(String.format("%s %s", FontAwesome.CLOCK_O.getHtml(),
                    AppContext.formatPrettyTime(bug.getDueDateRoundPlusOne()))).withDescription(AppContext.getMessage
                    (GenericI18Enum.FORM_DUE_DATE)).build();
        }
    }

    @Override
    public AbstractComponent createStartDatePopupField(SimpleBug bug) {
        if (bug.getStartdate() == null) {
            Div divHint = new Div().setCSSClass("nonValue");
            divHint.appendText(VaadinIcons.TIME_FORWARD.getHtml());
            divHint.appendChild(new Span().appendText(" Start date is not set").setCSSClass("hide"));
            return new MetaFieldBuilder().withCaption(divHint.write()).withDescription(AppContext.getMessage
                    (GenericI18Enum.FORM_START_DATE)).build();
        } else {
            return new MetaFieldBuilder().withCaption(String.format("%s %s", VaadinIcons.TIME_FORWARD.getHtml(),
                    AppContext.formatPrettyTime(bug.getStartdate()))).withDescription(AppContext.getMessage
                    (GenericI18Enum.FORM_START_DATE)).build();
        }
    }

    @Override
    public AbstractComponent createEndDatePopupField(SimpleBug bug) {
        if (bug.getEnddate() == null) {
            Div divHint = new Div().setCSSClass("nonValue");
            divHint.appendText(VaadinIcons.TIME_BACKWARD.getHtml());
            divHint.appendChild(new Span().appendText(" End date is not set").setCSSClass("hide"));
            return new MetaFieldBuilder().withCaption(divHint.write()).withDescription(AppContext.getMessage
                    (GenericI18Enum.FORM_END_DATE)).build();
        } else {
            return new MetaFieldBuilder().withCaption(String.format("%s %s", VaadinIcons.TIME_BACKWARD.getHtml(),
                    AppContext.formatPrettyTime(bug.getEnddate()))).withDescription(AppContext.getMessage
                    (GenericI18Enum.FORM_END_DATE)).build();
        }
    }

    @Override
    public AbstractComponent createBillableHoursPopupField(SimpleBug bug) {
        return new MetaFieldBuilder().withCaptionAndIcon(FontAwesome.MONEY, "" + bug.getBillableHours())
                .withDescription("Billable hours").build();
    }

    @Override
    public AbstractComponent createNonbillableHoursPopupField(SimpleBug bug) {
        return new MetaFieldBuilder().withCaptionAndIcon(FontAwesome.GIFT, "" + bug.getNonBillableHours())
                .withDescription("Non billable hours").build();
    }

    @Override
    public AbstractComponent createFollowersPopupField(SimpleBug bug) {
        return new MetaFieldBuilder().withCaptionAndIcon(FontAwesome.EYE, "" + bug.getNumFollowers())
                .withDescription("Followers").build();
    }
}
