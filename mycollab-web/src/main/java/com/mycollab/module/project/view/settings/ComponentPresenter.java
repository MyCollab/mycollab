/**
 * mycollab-web - Parent pom providing dependency and plugin management for applications
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
package com.mycollab.module.project.view.settings;

import com.mycollab.core.MyCollabException;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.view.parameters.ComponentScreenData;
import com.mycollab.module.tracker.domain.criteria.ComponentSearchCriteria;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ComponentPresenter extends AbstractPresenter<ProjectComponentContainer> {
    private static final long serialVersionUID = 1L;

    public ComponentPresenter() {
        super(ProjectComponentContainer.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        view.removeAllComponents();
        AbstractPresenter<?> presenter;

        if (data instanceof ComponentScreenData.Add) {
            presenter = PresenterResolver.getPresenter(ComponentAddPresenter.class);
        } else if (data instanceof ComponentScreenData.Edit) {
            presenter = PresenterResolver.getPresenter(ComponentAddPresenter.class);
        } else if (data instanceof ComponentScreenData.Search) {
            presenter = PresenterResolver.getPresenter(ComponentListPresenter.class);
        } else if (data instanceof ComponentScreenData.Read) {
            presenter = PresenterResolver.getPresenter(ComponentReadPresenter.class);
        } else if (data == null) {
            ComponentSearchCriteria criteria = new ComponentSearchCriteria();
            criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
            data = new ComponentScreenData.Search(criteria);
            presenter = PresenterResolver.getPresenter(ComponentListPresenter.class);
        } else {
            throw new MyCollabException("Do not support screen data");
        }

        presenter.go(view, data);
    }

}
