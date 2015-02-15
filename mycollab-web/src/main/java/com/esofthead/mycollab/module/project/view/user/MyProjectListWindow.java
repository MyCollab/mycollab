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

import com.esofthead.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectSearchCriteria;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.ui.Window;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0.0
 * 
 */
public class MyProjectListWindow extends Window {
	private static final long serialVersionUID = -3927621612074942453L;

	public MyProjectListWindow() {
		super("My Projects");
		this.setModal(true);
		this.setResizable(false);
		this.center();
        this.addStyleName("myprojectlist");
        ProjectPagedList projectList = new ProjectPagedList();
        this.setContent(projectList);
        ProjectSearchCriteria searchCriteria = new ProjectSearchCriteria();
        searchCriteria.setInvolvedMember(new StringSearchField(SearchField.AND,
                AppContext.getUsername()));
        searchCriteria.setProjectStatuses(new SetSearchField<>(
                new String[]{StatusI18nEnum.Open.name()}));
        projectList.setSearchCriteria(searchCriteria);
	}
}
