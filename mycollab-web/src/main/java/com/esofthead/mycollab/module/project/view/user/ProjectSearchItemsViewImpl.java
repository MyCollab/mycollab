/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.project.view.user;

import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.domain.ProjectGenericItem;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectGenericItemSearchCriteria;
import com.esofthead.mycollab.module.project.service.ProjectGenericItemService;
import com.esofthead.mycollab.module.project.ui.components.GenericItemRowDisplayHandler;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.ELabel;
import com.esofthead.mycollab.vaadin.web.ui.DefaultBeanPagedList;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author MyCollab Ltd.
 * @since 5.0.3
 */
@ViewComponent
public class ProjectSearchItemsViewImpl extends AbstractPageView implements ProjectSearchItemsView {
    private static final String headerTitle = FontAwesome.SEARCH.getHtml() + " Search for '%s' (Found: %d)";

    public ProjectSearchItemsViewImpl() {
        this.withMargin(true);
    }

    @Override
    public void displayResults(String value) {
        this.removeAllComponents();

        ELabel headerLbl = new ELabel("", ContentMode.HTML).withStyleName(ValoTheme.LABEL_H2, ValoTheme.LABEL_NO_MARGIN);

        DefaultBeanPagedList<ProjectGenericItemService, ProjectGenericItemSearchCriteria, ProjectGenericItem>
                searchItemsTable = new DefaultBeanPagedList<>(ApplicationContextUtil.getSpringBean(ProjectGenericItemService.class),
                new GenericItemRowDisplayHandler());
        searchItemsTable.setControlStyle("borderlessControl");

        this.with(headerLbl, searchItemsTable);
        ProjectGenericItemSearchCriteria criteria = new ProjectGenericItemSearchCriteria();
        criteria.setPrjKeys(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
        criteria.setTxtValue(StringSearchField.and(value));
        int foundNum = searchItemsTable.setSearchCriteria(criteria);
        headerLbl.setValue(String.format(headerTitle, value, foundNum));
    }
}
