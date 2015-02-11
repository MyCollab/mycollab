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
package com.esofthead.mycollab.module.project.view.milestone;

import com.esofthead.mycollab.module.project.domain.Milestone;
import com.esofthead.mycollab.module.project.domain.SimpleMilestone;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.MilestoneStatus;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectMemberSelectionField;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.ui.I18nValueComboBox;
import com.esofthead.mycollab.vaadin.ui.ProgressBarIndicator;
import com.esofthead.mycollab.vaadin.ui.form.field.ContainerHorizontalViewField;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Field;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;

import java.util.Arrays;

/**
 * 
 * @author MyCollab Ltd.
 * @since 3.0
 * 
 * @param <B>
 */
public class MilestoneEditFormFieldFactory<B extends Milestone> extends
		AbstractBeanFieldGroupEditFieldFactory<B> {

	private static final long serialVersionUID = 1L;

	MilestoneEditFormFieldFactory(GenericBeanForm<B> form) {
		super(form);
	}

	@Override
	protected Field<?> onCreateField(Object propertyId) {
		if (propertyId.equals("owner")) {
			final ProjectMemberSelectionField userbox = new ProjectMemberSelectionField();
			userbox.setRequired(true);
			userbox.setRequiredError("Please select an assignee");
			return userbox;
		} else if (propertyId.equals("status")) {
			if (attachForm.getBean().getStatus() == null) {
				attachForm.getBean().setStatus(
						MilestoneStatus.InProgress.toString());
			}
			return new ProgressStatusComboBox();
		} else if (propertyId.equals("name")) {
			final TextField tf = new TextField();
			if (isValidateForm) {
				tf.setNullRepresentation("");
				tf.setRequired(true);
				tf.setRequiredError("Please enter name");
			}
			return tf;
		} else if (propertyId.equals("description")) {
			final RichTextArea descArea = new RichTextArea();
			descArea.setNullRepresentation("");
			return descArea;
		} else if (propertyId.equals("numOpenTasks")) {
			final ContainerHorizontalViewField taskComp = new ContainerHorizontalViewField();
			final int numOpenTask = (attachForm.getBean() instanceof SimpleMilestone) ? ((SimpleMilestone) attachForm
					.getBean()).getNumOpenTasks() : 0;
			final int numTasks = (attachForm.getBean() instanceof SimpleMilestone) ? ((SimpleMilestone) attachForm
					.getBean()).getNumTasks() : 0;

			final ProgressBarIndicator progressTask = new ProgressBarIndicator(
					numTasks, numOpenTask);
			progressTask.setWidth("100%");
			taskComp.addComponentField(progressTask);
			return taskComp;
		} else if (propertyId.equals("numOpenBugs")) {
			final ContainerHorizontalViewField bugComp = new ContainerHorizontalViewField();
			final int numOpenBugs = (attachForm.getBean() instanceof SimpleMilestone) ? ((SimpleMilestone) attachForm
					.getBean()).getNumOpenBugs() : 0;
			final int numBugs = (attachForm.getBean() instanceof SimpleMilestone) ? ((SimpleMilestone) attachForm
					.getBean()).getNumBugs() : 0;

			final ProgressBarIndicator progressBug = new ProgressBarIndicator(
					numBugs, numOpenBugs);
			progressBug.setWidth("100%");
			bugComp.addComponentField(progressBug);
			return bugComp;
		}

		return null;
	}

	private static class ProgressStatusComboBox extends I18nValueComboBox {
		private static final long serialVersionUID = 1L;

		public ProgressStatusComboBox() {
			super();
			setCaption(null);
			this.setNullSelectionAllowed(false);
			this.loadData(Arrays.asList(MilestoneStatus.InProgress,
					MilestoneStatus.Future, MilestoneStatus.Closed));

			this.setItemIcon(MilestoneStatus.InProgress.name(), FontAwesome.SPINNER);
			this.setItemIcon(MilestoneStatus.Future.name(), FontAwesome.CLOCK_O);
			this.setItemIcon(MilestoneStatus.Closed.name(), FontAwesome.MINUS);
		}
	}

}
