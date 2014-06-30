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

package com.esofthead.mycollab.module.project.view.task;

import java.util.HashSet;
import java.util.Set;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.domain.SimpleTaskList;
import com.esofthead.mycollab.module.project.domain.criteria.TaskListSearchCriteria;
import com.esofthead.mycollab.module.project.events.TaskListEvent;
import com.esofthead.mycollab.module.project.service.ProjectTaskListService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.BeanList;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.event.dd.acceptcriteria.Not;
import com.vaadin.shared.ui.dd.VerticalDropLocation;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

import fi.jasoft.dragdroplayouts.DDVerticalLayout;
import fi.jasoft.dragdroplayouts.client.ui.LayoutDragMode;
import fi.jasoft.dragdroplayouts.events.LayoutBoundTransferable;
import fi.jasoft.dragdroplayouts.events.VerticalLocationIs;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class TaskGroupReorderViewImpl extends AbstractPageView implements
		TaskGroupReorderView {
	private static final long serialVersionUID = 1L;
	private BeanList<ProjectTaskListService, TaskListSearchCriteria, SimpleTaskList> taskLists;
	private Button saveOrderBtn;
	private final Set<SimpleTaskList> changeSet = new HashSet<SimpleTaskList>();

	public TaskGroupReorderViewImpl() {
		super();
		this.setMargin(true);
		constructHeader();
	}

	private void constructHeader() {
		CssLayout headerWrapper = new CssLayout();
		headerWrapper.setWidth("100%");
		headerWrapper.addStyleName("taskgroup-header");

		HorizontalLayout header = new HorizontalLayout();
		header.setSpacing(true);
		header.setWidth("100%");
		Label headerLbl = new Label("All Tasks");
		headerLbl.setStyleName("h2");
		header.addComponent(headerLbl);
		header.setComponentAlignment(headerLbl, Alignment.MIDDLE_LEFT);
		header.setExpandRatio(headerLbl, 1.0f);

		Button backToListBtn = new Button("Back to task list",
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(Button.ClickEvent event) {
						EventBus.getInstance()
								.fireEvent(
										new TaskListEvent.GotoTaskListScreen(
												this, null));
					}
				});
		backToListBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
		header.addComponent(backToListBtn);
		header.setComponentAlignment(backToListBtn, Alignment.MIDDLE_RIGHT);

		saveOrderBtn = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_SAVE_LABEL),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(Button.ClickEvent event) {
						EventBus.getInstance().fireEvent(
								new TaskListEvent.SaveReoderTaskList(event,
										changeSet));
					}
				});
		saveOrderBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
		header.addComponent(saveOrderBtn);
		header.setComponentAlignment(saveOrderBtn, Alignment.MIDDLE_RIGHT);

		headerWrapper.addComponent(header);

		this.addComponent(headerWrapper);

		final DDVerticalLayout ddLayout = new DDVerticalLayout();
		ddLayout.addStyleName("taskgroup-reorder");
		ddLayout.setComponentVerticalDropRatio(0.3f);
		ddLayout.setDragMode(LayoutDragMode.CLONE);
		ddLayout.setDropHandler(new DropHandler() {
			private static final long serialVersionUID = 1L;

			@Override
			public AcceptCriterion getAcceptCriterion() {
				return new Not(VerticalLocationIs.MIDDLE);
			}

			@Override
			public void drop(DragAndDropEvent event) {
				LayoutBoundTransferable transferable = (LayoutBoundTransferable) event
						.getTransferable();

				DDVerticalLayout.VerticalLayoutTargetDetails details = (DDVerticalLayout.VerticalLayoutTargetDetails) event
						.getTargetDetails();

				TaskListComponent comp = (TaskListComponent) transferable
						.getComponent();

				int currentIndex = ddLayout.getComponentIndex(comp);
				int newIndex = details.getOverIndex();

				ddLayout.removeComponent(comp);

				if (currentIndex > newIndex
						&& details.getDropLocation() == VerticalDropLocation.BOTTOM) {
					newIndex++;
				}

				SimpleTaskList dropTaskList = comp.getTaskList();
				dropTaskList.setGroupindex(newIndex);
				changeSet.add(dropTaskList);
				ddLayout.addComponent(comp, newIndex);

				// change affected task list items
				for (int i = 0; i < ddLayout.getComponentCount(); i++) {
					TaskListComponent affectedComp = (TaskListComponent) ddLayout
							.getComponent(i);
					SimpleTaskList affectedTaskList = affectedComp
							.getTaskList();
					affectedTaskList.setGroupindex(i);
					changeSet.add(affectedTaskList);
				}
			}
		});

		taskLists = new BeanList<ProjectTaskListService, TaskListSearchCriteria, SimpleTaskList>(
				null,
				ApplicationContextUtil
						.getSpringBean(ProjectTaskListService.class),
				TaskListRowDisplayHandler.class, ddLayout);
		this.addComponent(taskLists);
	}

	@Override
	public void displayTaskLists() {
		TaskListSearchCriteria criteria = new TaskListSearchCriteria();
		criteria.setProjectId(new NumberSearchField(CurrentProjectVariables
				.getProjectId()));
		taskLists.setSearchCriteria(criteria);
	}

	public static class TaskListRowDisplayHandler implements
			BeanList.RowDisplayHandler<SimpleTaskList> {
		private static final long serialVersionUID = 1L;

		@Override
		public Component generateRow(SimpleTaskList taskList, int rowIndex) {
			return new TaskListComponent(taskList);
		}
	}

	private static class TaskListComponent extends CssLayout {
		private static final long serialVersionUID = 1L;
		private final SimpleTaskList taskList;

		public TaskListComponent(SimpleTaskList taskList) {
			this.taskList = taskList;
			this.setStyleName("task-component");
			this.setWidth("100%");
			Label taskName = new Label(taskList.getName());
			taskName.addStyleName("task-name");
			if ("Closed".equals(taskList.getStatus())) {
				taskName.addStyleName(UIConstants.LINK_COMPLETED);
			}
			this.addComponent(taskName);
			Label taskCreatedTime = new Label("Last updated on "
					+ DateTimeUtils.getStringDateFromNow(
							taskList.getLastupdatedtime(),
							AppContext.getUserLocale()));
			taskCreatedTime.setStyleName("created-time");
			this.addComponent(taskCreatedTime);
		}

		public SimpleTaskList getTaskList() {
			return taskList;
		}
	}
}
