/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.module.project.view.bug;

import com.esofthead.mycollab.common.GenericLinkUtils;
import com.esofthead.mycollab.mobile.ui.AbstractListPresenter;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
public class BugListPresenter extends AbstractListPresenter<BugListView, BugSearchCriteria, SimpleBug> {
    private static final long serialVersionUID = -3814540725962187693L;

    public BugListPresenter() {
        super(BugListView.class);
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        if (CurrentProjectVariables.canRead(ProjectRolePermissionCollections.BUGS)) {
            BugSearchCriteria param = (BugSearchCriteria) data.getParams();
            super.onGo(container, data);
            this.doSearch(param);
            AppContext.addFragment("project/bug/list/" + GenericLinkUtils.encodeParam(CurrentProjectVariables.getProjectId()),
                    AppContext.getMessage(BugI18nEnum.VIEW_LIST_TITLE));

        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }
}
