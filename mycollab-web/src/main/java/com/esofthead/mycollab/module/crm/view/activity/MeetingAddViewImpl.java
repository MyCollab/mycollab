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

import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.MeetingWithBLOBs;
import com.esofthead.mycollab.module.crm.ui.CrmAssetsManager;
import com.esofthead.mycollab.module.crm.ui.components.AbstractEditItemComp;
import com.esofthead.mycollab.module.crm.ui.components.DynaFormLayout;
import com.esofthead.mycollab.module.crm.ui.components.RelatedEditItemField;
import com.esofthead.mycollab.vaadin.events.HasEditFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.*;
import com.esofthead.mycollab.vaadin.ui.form.field.RichTextEditField;
import com.vaadin.server.Resource;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextField;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 */
@ViewComponent
public class MeetingAddViewImpl extends AbstractEditItemComp<MeetingWithBLOBs>
		implements MeetingAddView {

	private static final long serialVersionUID = 1L;

	@Override
	protected String initFormTitle() {
		return (beanItem.getId() == null) ? "New Meeting" : beanItem
				.getSubject();
	}

	@Override
	protected Resource initFormIconResource() {
		return CrmAssetsManager.getAsset(CrmTypeConstants.MEETING);
	}

	@Override
	protected ComponentContainer createButtonControls() {
		return new EditFormControlsGenerator<>(editForm)
				.createButtonControls();
	}

	@Override
	protected AdvancedEditBeanForm<MeetingWithBLOBs> initPreviewForm() {
		return new AdvancedEditBeanForm<MeetingWithBLOBs>();
	}

	@Override
	protected IFormLayoutFactory initFormLayoutFactory() {
		return new DynaFormLayout(CrmTypeConstants.MEETING,
				MeetingDefaultFormLayoutFactory.getForm());
	}

	@Override
	protected AbstractBeanFieldGroupEditFieldFactory<MeetingWithBLOBs> initBeanFormFieldFactory() {
		return new MeetingEditFormFieldFactory(editForm);
	}

	private class MeetingEditFormFieldFactory extends
			AbstractBeanFieldGroupEditFieldFactory<MeetingWithBLOBs> {

		public MeetingEditFormFieldFactory(
				GenericBeanForm<MeetingWithBLOBs> form) {
			super(form);
		}

		private static final long serialVersionUID = 1L;

		@Override
		protected Field<?> onCreateField(Object propertyId) {
			if (propertyId.equals("subject")) {
				TextField tf = new TextField();
				if (isValidateForm) {
					tf.setNullRepresentation("");
					tf.setRequired(true);
					tf.setRequiredError("Subject must not be null");
				}

				return tf;
			} else if (propertyId.equals("status")) {
				return new MeetingStatusComboBox();
			} else if (propertyId.equals("startdate")) {
				return new DateTimePickerField();
			} else if (propertyId.equals("enddate")) {
				return new DateTimePickerField();
			} else if (propertyId.equals("description")) {
				return new RichTextEditField();
			} else if (propertyId.equals("type")) {
				RelatedEditItemField field = new RelatedEditItemField(
						new String[] { CrmTypeConstants.ACCOUNT,
								CrmTypeConstants.CAMPAIGN,
								CrmTypeConstants.CONTACT,
								CrmTypeConstants.LEAD,
								CrmTypeConstants.OPPORTUNITY,
								CrmTypeConstants.CASE }, attachForm.getBean());
				return field;
			} else if (propertyId.equals("typeid")) {
				return new DummyCustomField<String>();
			} else if (propertyId.equals("isrecurrence")) {
				return null;
			}
			return null;
		}
	}

	@Override
	public HasEditFormHandlers<MeetingWithBLOBs> getEditFormHandlers() {
		return editForm;
	}

	private class MeetingStatusComboBox extends ValueComboBox {

		private static final long serialVersionUID = 1L;

		public MeetingStatusComboBox() {
			super();
			setCaption(null);
			this.loadData("Planned", "Held", "Not Held");
		}
	}
}
