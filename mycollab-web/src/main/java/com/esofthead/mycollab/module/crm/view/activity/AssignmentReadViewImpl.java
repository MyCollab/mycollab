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
package com.esofthead.mycollab.module.crm.view.activity;

import com.esofthead.mycollab.common.ModuleNameConstants;
import com.esofthead.mycollab.form.view.DynaFormLayout;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.SimpleTask;
import com.esofthead.mycollab.module.crm.ui.components.AbstractPreviewItemComp;
import com.esofthead.mycollab.module.crm.ui.components.CrmPreviewFormControlsGenerator;
import com.esofthead.mycollab.module.crm.ui.components.NoteListItems;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 */
@ViewComponent
public class AssignmentReadViewImpl extends AbstractPreviewItemComp<SimpleTask>
		implements AssignmentReadView {
	private static final long serialVersionUID = 1L;

	protected NoteListItems noteListItems;

	public AssignmentReadViewImpl() {
		super(MyCollabResource.newResource("icons/22/crm/task.png"));
	}

	@Override
	protected AdvancedPreviewBeanForm<SimpleTask> initPreviewForm() {
		return new AdvancedPreviewBeanForm<SimpleTask>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void showHistory() {
				final AssignmentHistoryLogWindow historyLog = new AssignmentHistoryLogWindow(
						ModuleNameConstants.CRM, CrmTypeConstants.TASK);
				historyLog.loadHistory(beanItem.getId());
				UI.getCurrent().addWindow(historyLog);
			}
		};
	}

	@Override
	protected ComponentContainer createButtonControls() {
		return new CrmPreviewFormControlsGenerator<SimpleTask>(previewForm)
				.createButtonControls(RolePermissionCollections.CRM_TASK);
	}

	@Override
	protected ComponentContainer createBottomPanel() {
		return noteListItems;
	}

	@Override
	protected void onPreviewItem() {
		displayNotes();
	}

	@Override
	protected String initFormTitle() {
		return beanItem.getSubject();
	}

	@Override
	protected void initRelatedComponents() {
		noteListItems = new NoteListItems("Notes");

		previewItemContainer.addTab(previewLayout, "About");
		previewItemContainer.selectTab("About");
	}

	@Override
	protected IFormLayoutFactory initFormLayoutFactory() {
		return new DynaFormLayout(CrmTypeConstants.TASK,
				AssignmentDefaultFormLayoutFactory.getForm());
	}

	@Override
	protected AbstractBeanFieldGroupViewFieldFactory<SimpleTask> initBeanFormFieldFactory() {
		return new AssignmentReadFormFieldFactory(previewForm);
	}

	protected void displayNotes() {
		noteListItems.showNotes(CrmTypeConstants.TASK, beanItem.getId());
	}

	@Override
	public SimpleTask getItem() {
		return beanItem;
	}

	@Override
	public HasPreviewFormHandlers<SimpleTask> getPreviewFormHandlers() {
		return previewForm;
	}
}
