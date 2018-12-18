/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.community.module.project.view.service;

import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Span;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.ShellI18nEnum;
import com.mycollab.community.vaadin.web.ui.field.MetaFieldBuilder;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.module.file.StorageUtils;
import com.mycollab.module.project.domain.SimpleMilestone;
import com.mycollab.module.project.i18n.TimeTrackingI18nEnum;
import com.mycollab.module.project.view.service.MilestoneComponentFactory;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.UIConstants;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.AbstractComponent;
import org.springframework.stereotype.Service;

/**
 * @author MyCollab Ltd
 * @since 5.2.0
 */
@Service
public class MilestoneComponentFactoryImpl implements MilestoneComponentFactory {
    @Override
    public AbstractComponent createMilestoneAssigneePopupField(SimpleMilestone milestone, boolean isDisplayName) {
        String avatarLink = StorageUtils.getAvatarPath(milestone.getOwnerAvatarId(), 16);
        Img img = new Img(milestone.getOwnerFullName(), avatarLink).setCSSClass(UIConstants.CIRCLE_BOX)
                .setTitle(milestone.getOwnerFullName());
        if (isDisplayName) {
            return new MetaFieldBuilder().withCaption(img.write() + " " + StringUtils.trim(milestone.getOwnerFullName(), 20, true))
                    .withDescription(UserUIContext.getMessage(ShellI18nEnum.OPT_UPGRADE_PRO_INTRO,
                            UserUIContext.getMessage(GenericI18Enum.FORM_ASSIGNEE))).build();
        } else {
            return new MetaFieldBuilder().withCaption(img.write())
                    .withDescription(UserUIContext.getMessage(ShellI18nEnum.OPT_UPGRADE_PRO_INTRO,
                            UserUIContext.getMessage(GenericI18Enum.FORM_ASSIGNEE))).build();
        }

    }

    @Override
    public AbstractComponent createStartDatePopupField(SimpleMilestone milestone) {
        if (milestone.getStartdate() == null) {
            Div divHint = new Div().setCSSClass("nonValue");
            divHint.appendText(VaadinIcons.TIME_FORWARD.getHtml());
            divHint.appendChild(new Span().appendText(UserUIContext.getMessage(GenericI18Enum.OPT_UNDEFINED)).setCSSClass("hide"));
            return new MetaFieldBuilder().withCaption(divHint.write())
                    .withDescription(UserUIContext.getMessage(ShellI18nEnum.OPT_UPGRADE_PRO_INTRO,
                            UserUIContext.getMessage(GenericI18Enum.FORM_START_DATE))).build();
        } else {
            return new MetaFieldBuilder().withCaption(String.format(" %s %s", VaadinIcons.TIME_FORWARD.getHtml(),
                    UserUIContext.formatDate(milestone.getStartdate())))
                    .withDescription(UserUIContext.getMessage(ShellI18nEnum.OPT_UPGRADE_PRO_INTRO,
                            UserUIContext.getMessage(GenericI18Enum.FORM_START_DATE))).build();
        }
    }

    @Override
    public AbstractComponent createEndDatePopupField(SimpleMilestone milestone) {
        if (milestone.getEnddate() == null) {
            Div divHint = new Div().setCSSClass("nonValue");
            divHint.appendText(VaadinIcons.TIME_BACKWARD.getHtml());
            divHint.appendChild(new Span().appendText(UserUIContext.getMessage(GenericI18Enum.OPT_UNDEFINED)).setCSSClass("hide"));
            return new MetaFieldBuilder().withCaption(divHint.write())
                    .withDescription(UserUIContext.getMessage(ShellI18nEnum.OPT_UPGRADE_PRO_INTRO,
                            UserUIContext.getMessage(GenericI18Enum.FORM_END_DATE))).build();
        } else {
            return new MetaFieldBuilder().withCaption(String.format(" %s %s", VaadinIcons.TIME_BACKWARD.getHtml(),
                    UserUIContext.formatDate(milestone.getEnddate())))
                    .withDescription(UserUIContext.getMessage(ShellI18nEnum.OPT_UPGRADE_PRO_INTRO,
                            UserUIContext.getMessage(GenericI18Enum.FORM_END_DATE))).build();
        }
    }

    @Override
    public AbstractComponent createBillableHoursPopupField(SimpleMilestone milestone) {
        return new MetaFieldBuilder().withCaptionAndIcon(VaadinIcons.MONEY, "" + (milestone.getTotalBugBillableHours() + milestone.getTotalTaskBillableHours()))
                .withDescription(UserUIContext.getMessage(TimeTrackingI18nEnum.OPT_BILLABLE_HOURS)).build();
    }

    @Override
    public AbstractComponent createNonBillableHoursPopupField(SimpleMilestone milestone) {
        return new MetaFieldBuilder().withCaptionAndIcon(VaadinIcons.GIFT, "" + (milestone.getTotalBugNonBillableHours() + milestone.getTotalTaskNonBillableHours()))
                .withDescription(UserUIContext.getMessage(TimeTrackingI18nEnum.OPT_NON_BILLABLE_HOURS)).build();
    }
}
