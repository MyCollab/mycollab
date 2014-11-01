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
package com.esofthead.mycollab.mobile.module.project.view.bug;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.mobile.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.mobile.module.project.ui.form.field.ProjectFormAttachmentUploadField;
import com.esofthead.mycollab.mobile.module.project.view.milestone.MilestoneComboBox;
import com.esofthead.mycollab.mobile.module.project.view.settings.ProjectMemberSelectionField;
import com.esofthead.mycollab.mobile.ui.AbstractEditItemComp;
import com.esofthead.mycollab.mobile.ui.GridFormLayoutHelper;
import com.esofthead.mycollab.module.file.AttachmentType;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugPriority;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugSeverity;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.vaadin.addon.touchkit.ui.DatePicker;
import com.vaadin.addon.touchkit.ui.NumberField;
import com.vaadin.data.Property;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * @author MyCollab Ltd.
 *
 * @since 4.5.2
 */

/*
 * TODO: Add support for Attachments, Components, Versions when they're ready
 */
@ViewComponent
public class BugAddViewImpl extends AbstractEditItemComp<SimpleBug> implements
		BugAddView {

	private static final long serialVersionUID = -688386159095055595L;

	private ProjectFormAttachmentUploadField attachmentUploadField;

	@Override
	protected String initFormTitle() {
		return beanItem.getId() == null ? AppContext
				.getMessage(BugI18nEnum.FORM_NEW_BUG_TITLE) : "["
				+ CurrentProjectVariables.getProject().getShortname() + "-"
				+ beanItem.getBugkey() + "]";
	}

	@Override
	protected IFormLayoutFactory initFormLayoutFactory() {
		return new EditFormLayoutFactory();
	}

	@Override
	protected AbstractBeanFieldGroupEditFieldFactory<SimpleBug> initBeanFormFieldFactory() {
		return new EditFormFieldFactory(this.editForm);
	}

	private class EditFormFieldFactory extends
			AbstractBeanFieldGroupEditFieldFactory<SimpleBug> {

		private static final long serialVersionUID = 1L;

		public EditFormFieldFactory(GenericBeanForm<SimpleBug> form) {
			super(form);
		}

		@Override
		protected Field<?> onCreateField(final Object propertyId) {

			if (propertyId.equals("environment")) {
				final TextArea field = new TextArea("", "");
				field.setNullRepresentation("");
				return field;
			} else if (propertyId.equals("description")) {
				final TextArea field = new TextArea("", "");
				field.setNullRepresentation("");
				return field;
			} else if (propertyId.equals("duedate")) {
				return new DatePicker();
			} else if (propertyId.equals("priority")) {
				if (beanItem.getPriority() == null) {
					beanItem.setPriority(BugPriority.Major.name());
				}
				return new BugPriorityComboBox();
			} else if (propertyId.equals("assignuser")) {
				return new ProjectMemberSelectionField();
			} else if (propertyId.equals("severity")) {
				if (beanItem.getSeverity() == null) {
					beanItem.setSeverity(BugSeverity.Major.name());
				}
				return new BugSeverityComboBox();
			} else if (propertyId.equals("summary")) {
				final TextField tf = new TextField();
				if (isValidateForm) {
					tf.setNullRepresentation("");
					tf.setRequired(true);
					tf.setRequiredError("Bug summary must be not null");
				}

				return tf;
			} else if (propertyId.equals("milestoneid")) {
				final MilestoneComboBox milestoneBox = new MilestoneComboBox();
				milestoneBox
						.addValueChangeListener(new Property.ValueChangeListener() {
							private static final long serialVersionUID = 1L;

							@Override
							public void valueChange(
									Property.ValueChangeEvent event) {
								String milestoneName = milestoneBox
										.getItemCaption(milestoneBox.getValue());
								beanItem.setMilestoneName(milestoneName);
							}
						});
				return milestoneBox;
			} else if (propertyId.equals("estimatetime")
					|| (propertyId.equals("estimateremaintime"))) {
				NumberField field = new NumberField();
				return field;
			} else if (propertyId.equals("id")) {
				attachmentUploadField = new ProjectFormAttachmentUploadField();
				if (beanItem.getId() != null) {
					attachmentUploadField.getAttachments(
							beanItem.getProjectid(),
							AttachmentType.PROJECT_BUG_TYPE, beanItem.getId());
				}
				return attachmentUploadField;
			}

			return null;
		}
	}

	public class EditFormLayoutFactory implements IFormLayoutFactory {

		private static final long serialVersionUID = -9159483523170247666L;

		private GridFormLayoutHelper informationLayout;

		@Override
		public ComponentContainer getLayout() {
			final VerticalLayout layout = new VerticalLayout();
			layout.setMargin(false);
			Label header = new Label(
					AppContext.getMessage(BugI18nEnum.M_FORM_READ_TITLE));
			header.setStyleName("h2");
			layout.addComponent(header);

			this.informationLayout = new GridFormLayoutHelper(1, 11, "100%",
					"150px", Alignment.TOP_LEFT);
			this.informationLayout.getLayout().addStyleName(
					"colored-gridlayout");
			this.informationLayout.getLayout().setMargin(false);
			this.informationLayout.getLayout().setWidth("100%");
			layout.addComponent(this.informationLayout.getLayout());
			layout.setComponentAlignment(this.informationLayout.getLayout(),
					Alignment.BOTTOM_CENTER);
			return layout;
		}

		@Override
		public void attachField(Object propertyId, Field<?> field) {
			if (propertyId.equals("summary")) {
				this.informationLayout.addComponent(field,
						AppContext.getMessage(BugI18nEnum.FORM_SUMMARY), 0, 0);
			} else if (propertyId.equals("milestoneid")) {
				this.informationLayout.addComponent(field,
						AppContext.getMessage(BugI18nEnum.FORM_PHASE), 0, 1);
			} else if (propertyId.equals("environment")) {
				this.informationLayout.addComponent(field,
						AppContext.getMessage(BugI18nEnum.FORM_ENVIRONMENT), 0,
						2);
			} else if (propertyId.equals("priority")) {
				this.informationLayout.addComponent(field,
						AppContext.getMessage(BugI18nEnum.FORM_PRIORITY), 0, 3);
			} else if (propertyId.equals("severity")) {
				this.informationLayout.addComponent(field,
						AppContext.getMessage(BugI18nEnum.FORM_SEVERITY), 0, 4);
			} else if (propertyId.equals("duedate")) {
				this.informationLayout.addComponent(field,
						AppContext.getMessage(BugI18nEnum.FORM_DUE_DATE), 0, 5);
			} else if (propertyId.equals("estimatetime")) {
				this.informationLayout.addComponent(field, AppContext
						.getMessage(BugI18nEnum.FORM_ORIGINAL_ESTIMATE), 0, 6);
			} else if (propertyId.equals("estimateremaintime")) {
				this.informationLayout
						.addComponent(field, AppContext
								.getMessage(BugI18nEnum.FORM_REMAIN_ESTIMATE),
								0, 7);
			} else if (propertyId.equals("assignuser")) {
				this.informationLayout.addComponent(field,
						AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE), 0,
						8);
			} else if (propertyId.equals("id")) {
				this.informationLayout.addComponent(field,
						AppContext.getMessage(BugI18nEnum.FORM_ATTACHMENT), 0,
						9);
			} else if (propertyId.equals("description")) {
				this.informationLayout.addComponent(field,
						AppContext.getMessage(GenericI18Enum.FORM_DESCRIPTION),
						0, 10);
			}
		}

	}

	@Override
	public ProjectFormAttachmentUploadField getAttachUploadField() {
		return attachmentUploadField;
	}
}
