package com.esofthead.mycollab.mobile.module.project.view;

import com.esofthead.mycollab.mobile.ui.DefaultPagedBeanList;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectSearchCriteria;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
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
		public Component generateRow(SimpleProject obj, int rowIndex) {
			final Button b = new Button(obj.getName());
			b.setWidth("100%");
			return b;
		}

	}

}
