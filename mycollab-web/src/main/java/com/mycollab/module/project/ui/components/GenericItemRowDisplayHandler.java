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
package com.mycollab.module.project.ui.components;

import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Text;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.html.DivLessFormatter;
import com.mycollab.module.file.StorageUtils;
import com.mycollab.module.project.ProjectLinkBuilder;
import com.mycollab.module.project.ProjectLinkGenerator;
import com.mycollab.module.project.domain.ProjectGenericItem;
import com.mycollab.vaadin.TooltipHelper;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.IBeanList;
import com.mycollab.vaadin.ui.SafeHtmlLabel;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.ui.Component;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.2.10
 */
public class GenericItemRowDisplayHandler implements IBeanList.RowDisplayHandler<ProjectGenericItem> {
    @Override
    public Component generateRow(IBeanList<ProjectGenericItem> host, ProjectGenericItem item, int rowIndex) {
        MVerticalLayout layout = new MVerticalLayout().withFullWidth().withStyleName(WebThemes.BORDER_BOTTOM, WebThemes.HOVER_EFFECT_NOT_BOX);
        ELabel link = ELabel.h3("").withFullWidth();
        if (item.isBug() || item.isTask()) {
            link.setValue(ProjectLinkBuilder.generateProjectItemHtmlLinkAndTooltip(item.getProjectShortName(),
                    item.getProjectId(), item.getName(), item.getType(), item.getExtraTypeId() + ""));
        } else {
            link.setValue(ProjectLinkBuilder.generateProjectItemHtmlLinkAndTooltip(item.getProjectShortName(),
                    item.getProjectId(), item.getName(), item.getType(), item.getTypeId()));
        }

        String desc = (StringUtils.isBlank(item.getDescription())) ? UserUIContext.getMessage(GenericI18Enum.OPT_UNDEFINED) :
                item.getDescription();
        SafeHtmlLabel descLbl = new SafeHtmlLabel(desc);

        Div div = new Div().setStyle("width:100%");
        Text createdByTxt = new Text(UserUIContext.getMessage(GenericI18Enum.OPT_CREATED_BY) + ": ");
        Div lastUpdatedOn = new Div().appendChild(new Text(UserUIContext.getMessage(GenericI18Enum.OPT_LAST_MODIFIED,
                UserUIContext.formatPrettyTime(item.getLastUpdatedTime()))))
                .setTitle(UserUIContext.formatDateTime(item.getLastUpdatedTime())).setStyle("float:right;margin-right:5px");

        if (StringUtils.isBlank(item.getCreatedUser())) {
            div.appendChild(createdByTxt, DivLessFormatter.EMPTY_SPACE, new Text(UserUIContext.getMessage(GenericI18Enum.OPT_UNDEFINED)),
                    lastUpdatedOn);
        } else {
            Img userAvatar = new Img("", StorageUtils.getAvatarPath(item.getCreatedUserAvatarId(), 16))
                    .setCSSClass(WebThemes.CIRCLE_BOX);
            A userLink = new A().setId("tag" + TooltipHelper.TOOLTIP_ID).setHref(ProjectLinkGenerator.generateProjectMemberLink(item.getProjectId(), item
                    .getCreatedUser())).appendText(item.getCreatedUserDisplayName());
            userLink.setAttribute("onmouseover", TooltipHelper.userHoverJsFunction(item.getCreatedUser()));
            userLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction());

            div.appendChild(createdByTxt, DivLessFormatter.EMPTY_SPACE, userAvatar, DivLessFormatter.EMPTY_SPACE,
                    userLink, lastUpdatedOn);
        }

        ELabel footer = ELabel.html(div.write()).withStyleName(WebThemes.META_INFO).withFullWidth();
        layout.with(link, descLbl, footer);
        return layout;
    }
}
