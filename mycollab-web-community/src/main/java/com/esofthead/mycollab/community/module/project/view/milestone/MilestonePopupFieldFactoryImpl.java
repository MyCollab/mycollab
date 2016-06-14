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
package com.esofthead.mycollab.community.module.project.view.milestone;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.community.vaadin.web.ui.field.MetaFieldBuilder;
import com.esofthead.mycollab.configuration.StorageFactory;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.project.domain.SimpleMilestone;
import com.esofthead.mycollab.module.project.i18n.TimeTrackingI18nEnum;
import com.esofthead.mycollab.module.project.view.milestone.MilestonePopupFieldFactory;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Span;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.AbstractComponent;
import org.vaadin.teemu.VaadinIcons;

/**
 * @author MyCollab Ltd
 * @since 5.2.0
 */
@ViewComponent
public class MilestonePopupFieldFactoryImpl implements MilestonePopupFieldFactory {
    @Override
    public AbstractComponent createMilestoneAssigneePopupField(SimpleMilestone milestone, boolean isDisplayName) {
        String avatarLink = StorageFactory.getInstance().getAvatarPath(milestone.getOwnerAvatarId(), 16);
        Img img = new Img(milestone.getOwnerFullName(), avatarLink).setTitle(milestone.getOwnerFullName());
        if (isDisplayName) {
            return new MetaFieldBuilder().withCaption(img.write() + " " + StringUtils.trim(milestone.getOwnerFullName(), 20, true))
                    .withDescription(AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE)).build();
        } else {
            return new MetaFieldBuilder().withCaption(img.write())
                    .withDescription(AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE)).build();
        }

    }

    @Override
    public AbstractComponent createStartDatePopupField(SimpleMilestone milestone) {
        if (milestone.getStartdate() == null) {
            Div divHint = new Div().setCSSClass("nonValue");
            divHint.appendText(VaadinIcons.TIME_FORWARD.getHtml());
            divHint.appendChild(new Span().appendText(" Start date is not set").setCSSClass("hide"));
            return new MetaFieldBuilder().withCaption(divHint.write()).withDescription("Start date").build();
        } else {
            return new MetaFieldBuilder().withCaption(String.format(" %s %s", VaadinIcons.TIME_FORWARD.getHtml(),
                    AppContext.formatDate(milestone.getStartdate()))).withDescription("Start date").build();
        }
    }

    @Override
    public AbstractComponent createEndDatePopupField(SimpleMilestone milestone) {
        if (milestone.getEnddate() == null) {
            Div divHint = new Div().setCSSClass("nonValue");
            divHint.appendText(VaadinIcons.TIME_BACKWARD.getHtml());
            divHint.appendChild(new Span().appendText(" End date is not set").setCSSClass("hide"));
            return new MetaFieldBuilder().withCaption(divHint.write()).withDescription("End date").build();
        } else {
            return new MetaFieldBuilder().withCaption(String.format(" %s %s", VaadinIcons.TIME_BACKWARD.getHtml(),
                    AppContext.formatDate(milestone.getEnddate()))).withDescription("End date").build();
        }
    }

    @Override
    public AbstractComponent createBillableHoursPopupField(SimpleMilestone milestone) {
        return new MetaFieldBuilder().withCaptionAndIcon(FontAwesome.MONEY, "" + (milestone.getTotalBugBillableHours() + milestone.getTotalTaskBillableHours()))
                .withDescription(AppContext.getMessage(TimeTrackingI18nEnum.OPT_BILLABLE_HOURS)).build();
    }

    @Override
    public AbstractComponent createNonBillableHoursPopupField(SimpleMilestone milestone) {
        return new MetaFieldBuilder().withCaptionAndIcon(FontAwesome.GIFT, "" + (milestone.getTotalBugNonBillableHours() + milestone.getTotalTaskNonBillableHours()))
                .withDescription(AppContext.getMessage(TimeTrackingI18nEnum.OPT_NON_BILLABLE_HOURS)).build();
    }
}
