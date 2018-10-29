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
package com.mycollab.module.project.view.user;

import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.domain.ProjectGenericItem;
import com.mycollab.module.project.domain.criteria.ProjectGenericItemSearchCriteria;
import com.mycollab.module.project.i18n.ProjectI18nEnum;
import com.mycollab.module.project.service.ProjectGenericItemService;
import com.mycollab.module.project.ui.components.GenericItemRowDisplayHandler;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.AbstractVerticalPageView;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.DefaultBeanPagedList;
import com.vaadin.icons.VaadinIcons;

/**
 * @author MyCollab Ltd.
 * @since 5.0.3
 */
@ViewComponent
public class ProjectSearchItemsViewImpl extends AbstractVerticalPageView implements ProjectSearchItemsView {

    public ProjectSearchItemsViewImpl() {
        this.withMargin(true);
    }

    @Override
    public void displayResults(String value) {
        this.removeAllComponents();

        ELabel headerLbl = ELabel.h2("");

        DefaultBeanPagedList<ProjectGenericItemService, ProjectGenericItemSearchCriteria, ProjectGenericItem>
                searchItemsTable = new DefaultBeanPagedList<>(AppContextUtil.getSpringBean(ProjectGenericItemService.class),
                new GenericItemRowDisplayHandler());
        searchItemsTable.setControlStyle("borderlessControl");

        this.with(headerLbl, searchItemsTable);
        ProjectGenericItemSearchCriteria criteria = new ProjectGenericItemSearchCriteria();
        criteria.setPrjKeys(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
        criteria.setTxtValue(StringSearchField.and(value));
        int foundNum = searchItemsTable.setSearchCriteria(criteria);
        headerLbl.setValue(String.format(VaadinIcons.SEARCH.getHtml() + " " + UserUIContext.getMessage(ProjectI18nEnum.OPT_SEARCH_TERM)
                , value, foundNum));
    }
}
