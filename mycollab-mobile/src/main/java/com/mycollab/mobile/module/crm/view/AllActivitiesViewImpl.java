/**
 * mycollab-mobile - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.mobile.module.crm.view;

import com.mycollab.common.domain.SimpleActivityStream;
import com.mycollab.common.domain.criteria.ActivityStreamSearchCriteria;
import com.mycollab.mobile.module.crm.ui.AbstractListPageView;
import com.mycollab.mobile.ui.AbstractPagedBeanList;
import com.mycollab.mobile.ui.IListView;
import com.mycollab.mobile.ui.SearchInputField;
import com.mycollab.vaadin.mvp.ViewComponent;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
@ViewComponent
public class AllActivitiesViewImpl extends AbstractListPageView<ActivityStreamSearchCriteria, SimpleActivityStream> implements IListView<ActivityStreamSearchCriteria, SimpleActivityStream> {
    private static final long serialVersionUID = 5251742381187041492L;

    public AllActivitiesViewImpl() {
        setCaption("Activities");
    }

    @Override
    protected AbstractPagedBeanList<ActivityStreamSearchCriteria, SimpleActivityStream> createBeanList() {
        return new ActivitiesStreamListDisplay();
    }

    @Override
    protected void onBecomingVisible() {
        super.onBecomingVisible();

    }

    @Override
    protected SearchInputField<ActivityStreamSearchCriteria> createSearchField() {
        return null;
    }
}
