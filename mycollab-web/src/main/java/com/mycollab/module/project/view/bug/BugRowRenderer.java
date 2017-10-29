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
package com.mycollab.module.project.view.bug;

import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Span;
import com.mycollab.module.file.StorageUtils;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.tracker.domain.SimpleBug;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.IBeanList;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import static com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;

/**
 * @author MyCollab Ltd
 * @since 5.3.1
 */
public class BugRowRenderer implements IBeanList.RowDisplayHandler<SimpleBug> {
    @Override
    public Component generateRow(IBeanList<SimpleBug> host, SimpleBug bug, int rowIndex) {
        ToggleBugSummaryField toggleBugSummaryField = new ToggleBugSummaryField(bug);

        MHorizontalLayout rowComp = new MHorizontalLayout().withStyleName(WebThemes.HOVER_EFFECT_NOT_BOX);
        rowComp.addStyleName("margin-bottom");
        rowComp.setDefaultComponentAlignment(Alignment.TOP_LEFT);

        String bugPriority = bug.getPriority();
        Span priorityLink = new Span().appendText(ProjectAssetsManager.getPriorityHtml(bugPriority)).setTitle(bugPriority);

        Span statusSpan = new Span().appendText(UserUIContext.getMessage(StatusI18nEnum.class,
                bug.getStatus())).setCSSClass(UIConstants.BLOCK);

        String avatarLink = StorageUtils.getAvatarPath(bug.getAssignUserAvatarId(), 16);
        Img img = new Img(bug.getAssignuserFullName(), avatarLink).setTitle(bug.getAssignuserFullName())
                .setCSSClass(UIConstants.CIRCLE_BOX);

        rowComp.with(ELabel.fontIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG)).withWidthUndefined(),
                ELabel.html(priorityLink.write()).withWidthUndefined(),
                ELabel.html(statusSpan.write()).withWidthUndefined(),
                ELabel.html(img.write()).withWidthUndefined(),
                toggleBugSummaryField).expand(toggleBugSummaryField);
        return rowComp;
    }
}
