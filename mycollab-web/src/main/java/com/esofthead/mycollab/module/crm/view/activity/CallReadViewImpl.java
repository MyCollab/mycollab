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
import com.esofthead.mycollab.module.crm.domain.SimpleCall;
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
public class CallReadViewImpl extends AbstractPreviewItemComp<SimpleCall>
		implements CallReadView {

	private static final long serialVersionUID = 1L;

	protected NoteListItems noteListItems;

	public CallReadViewImpl() {
		super(MyCollabResource.newResource("icons/22/crm/call.png"));
	}

	@Override
	protected AdvancedPreviewBeanForm<SimpleCall> initPreviewForm() {
		return new AdvancedPreviewBeanForm<SimpleCall>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void showHistory() {
				final CallHistoryLogWindow historyLog = new CallHistoryLogWindow(
						ModuleNameConstants.CRM, CrmTypeConstants.CALL);
				historyLog.loadHistory(beanItem.getId());
				UI.getCurrent().addWindow(historyLog);
			}
		};
	}

	@Override
	protected ComponentContainer createButtonControls() {
		return new CrmPreviewFormControlsGenerator<SimpleCall>(previewForm)
				.createButtonControls(RolePermissionCollections.CRM_CALL);
	}

	@Override
	protected ComponentContainer createBottomPanel() {
		return noteListItems;
	}

	@Override
	protected void onPreviewItem() {
		this.displayNotes();
	}

	@Override
	protected String initFormTitle() {
		return beanItem.getSubject();
	}

	@Override
	protected void initRelatedComponents() {
		this.noteListItems = new NoteListItems("Notes");

		previewItemContainer.addTab(previewLayout, "About");
		previewItemContainer.selectTab("About");
	}

	@Override
	protected IFormLayoutFactory initFormLayoutFactory() {
		return new DynaFormLayout(CrmTypeConstants.CALL,
				CallDefaultFormLayoutFactory.getForm());
	}

	@Override
	protected AbstractBeanFieldGroupViewFieldFactory<SimpleCall> initBeanFormFieldFactory() {
		return new CallReadFormFieldFactory(previewForm);
	}

	protected void displayNotes() {
		this.noteListItems.showNotes(CrmTypeConstants.CALL, beanItem.getId());
	}

	@Override
	public SimpleCall getItem() {
		return beanItem;
	}

	@Override
	public HasPreviewFormHandlers<SimpleCall> getPreviewFormHandlers() {
		return previewForm;
	}
}
