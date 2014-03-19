package com.esofthead.mycollab.module.project.view.task;

import java.util.Arrays;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.esofthead.mycollab.module.project.events.TaskListEvent;
import com.esofthead.mycollab.module.project.view.parameters.TaskFilterParameter;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.UiUtils;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
@ViewComponent
public class FilterTaskViewImpl extends AbstractPageView implements
		FilterTaskView {
	private static final long serialVersionUID = 1L;

	private Label headerText;
	private VerticalLayout rightColumn;
	private TaskTableDisplay taskTableDisplay;
	private UnresolvedTaskByAssigneeWidget unresolvedTaskByAssigneeWidget;
	private UnresolvedTaskByPriorityWidget unresolvedTaskByPriorityWidget;

	public FilterTaskViewImpl() {
		final HorizontalLayout header = new HorizontalLayout();
		header.setMargin(true);
		header.setSpacing(true);
		header.setStyleName("hdr-view");
		header.setWidth("100%");
		header.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
		Image titleIcon = new Image(null,
				MyCollabResource.newResource("icons/24/project/task.png"));
		header.addComponent(titleIcon);

		headerText = new Label();
		headerText.setSizeUndefined();
		headerText.setStyleName("hdr-text");

		Button backtoTaskListBtn = new Button("Back to task dashboard",
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						EventBus.getInstance()
								.fireEvent(
										new TaskListEvent.GotoTaskListScreen(
												this, null));

					}
				});
		backtoTaskListBtn.setStyleName(UIConstants.THEME_GREEN_LINK);

		UiUtils.addComponent(header, titleIcon, Alignment.MIDDLE_LEFT);
		UiUtils.addComponent(header, headerText, Alignment.MIDDLE_LEFT);
		UiUtils.addComponent(header, backtoTaskListBtn, Alignment.MIDDLE_LEFT);

		this.addComponent(header);

		HorizontalLayout contentLayout = new HorizontalLayout();
		contentLayout.setWidth("100%");
		contentLayout.setSpacing(true);
		this.addComponent(contentLayout);

		this.taskTableDisplay = new TaskTableDisplay(TaskTableFieldDef.id,
				Arrays.asList(TaskTableFieldDef.taskname,
						TaskTableFieldDef.startdate, TaskTableFieldDef.duedate,
						TaskTableFieldDef.percentagecomplete));
		taskTableDisplay.setWidth("100%");
		contentLayout.addComponent(taskTableDisplay);
		contentLayout.setExpandRatio(taskTableDisplay, 1.0f);

		rightColumn = new VerticalLayout();
		rightColumn.setWidth("300px");
		contentLayout.addComponent(rightColumn);
		unresolvedTaskByAssigneeWidget = new UnresolvedTaskByAssigneeWidget();
		rightColumn.addComponent(unresolvedTaskByAssigneeWidget);

		unresolvedTaskByPriorityWidget = new UnresolvedTaskByPriorityWidget();
		rightColumn.addComponent(unresolvedTaskByPriorityWidget);
	}

	@Override
	public void filterTasks(TaskFilterParameter filterParam) {
		headerText.setCaption(filterParam.getScreenTitle());
		taskTableDisplay.setSearchCriteria(filterParam.getSearchCriteria());

		TaskSearchCriteria searchCriteria = new TaskSearchCriteria();
		searchCriteria.setProjectid(new NumberSearchField(
				CurrentProjectVariables.getProjectId()));
		unresolvedTaskByAssigneeWidget.setSearchCriteria(searchCriteria);

		unresolvedTaskByAssigneeWidget.setSearchCriteria(searchCriteria);
		unresolvedTaskByPriorityWidget.setSearchCriteria(searchCriteria);
	}

}
