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
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    PageActionChain chain = new PageActionChain(new ProjectScreenData.Goto(obj.getId()));
                    EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(ProjectRowDisplayHandler.this, chain));
                }
            });
			b.setWidth("100%");
			return b;
		}

	}

}
