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
package com.esofthead.mycollab.mobile.module.project.view;

import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.project.events.ProjectEvent;
import com.esofthead.mycollab.mobile.module.project.view.parameters.ProjectScreenData;
import com.esofthead.mycollab.mobile.ui.DefaultPagedBeanList;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectSearchCriteria;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.mvp.PageActionChain;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;

/**
 * @author MyCollab Ltd.
 *
 * @since 4.4.0
 *
 */
public class ProjectListDisplay
		extends
		DefaultPagedBeanList<ProjectService, ProjectSearchCriteria, SimpleProject> {
	private static final long serialVersionUID = -3362055893248919249L;

	public ProjectListDisplay() {
		super(ApplicationContextUtil.getSpringBean(ProjectService.class),
				new ProjectRowDisplayHandler());
	}

	public static class ProjectRowDisplayHandler implements
			RowDisplayHandler<SimpleProject> {

		@Override
		public Component generateRow(final SimpleProject obj, int rowIndex) {
			final Button b = new Button(obj.getName());
			b.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 6404941057797908742L;

				@Override
				public void buttonClick(Button.ClickEvent event) {
					PageActionChain chain = new PageActionChain(
							new ProjectScreenData.Goto(obj.getId()));
					EventBusFactory.getInstance().post(
							new ProjectEvent.GotoMyProject(
									ProjectRowDisplayHandler.this, chain));
				}
			});
			b.setWidth("100%");
			b.addStyleName("list-item");
			return b;
		}

	}

}
