/**
 * This file is part of mycollab-ui.
 *
 * mycollab-ui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-ui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-ui.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.vaadin.ui;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.arguments.NotBindable;
import com.esofthead.mycollab.core.utils.ClassUtils;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.validator.constraints.DateComparision;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.FieldGroup.CommitHandler;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Field;
import com.vaadin.ui.RichTextArea;

/**
 * 
 * @author MyCollab Ltd
 * @since 3.0
 * 
 * @param <B>
 */
public abstract class AbstractBeanFieldGroupEditFieldFactory<B> implements
		IBeanFieldGroupFieldFactory<B>, CommitHandler {

	private static final long serialVersionUID = 1L;

	private static Logger log = LoggerFactory
			.getLogger(AbstractBeanFieldGroupEditFieldFactory.class);

	protected GenericBeanForm<B> attachForm;
	protected FieldGroup fieldGroup;
	protected boolean isValidateForm;
	private Validator validation;

	public AbstractBeanFieldGroupEditFieldFactory(GenericBeanForm<B> form) {
		this(form, true);
	}

	public AbstractBeanFieldGroupEditFieldFactory(GenericBeanForm<B> form,
			boolean isValidateForm) {
		this.attachForm = form;
		this.fieldGroup = new FieldGroup();
		this.fieldGroup.setBuffered(true);
		this.isValidateForm = isValidateForm;

		if (isValidateForm) {
			this.fieldGroup.addCommitHandler(this);
			validation = ApplicationContextUtil
					.getSpringBean(LocalValidatorFactoryBean.class);
		}
	}

	@Override
	public void setBean(B bean) {
		fieldGroup.setItemDataSource(new BeanItem<B>(bean));

		Class<?> beanClass = bean.getClass();
		java.lang.reflect.Field[] fields = ClassUtils.getAllFields(beanClass);
		for (java.lang.reflect.Field field : fields) {
			Field<?> formField = onCreateField(field.getName());
			if (formField == null) {
				if (field.getAnnotation(NotBindable.class) != null) {
					continue;
				} else {
					formField = fieldGroup.buildAndBind(field.getName());
				}
			} else {
				if (formField instanceof DummyCustomField) {
					continue;
				} else if (!(formField instanceof CompoundCustomField)) {
					log.debug("Bind field: {} of form field {}",
							field.getName(), formField);
					fieldGroup.bind(formField, field.getName());
				}
			}

			if (formField instanceof AbstractTextField) {
				((AbstractTextField) formField).setNullRepresentation("");
			} else if (formField instanceof RichTextArea) {
				((RichTextArea) formField).setNullRepresentation("");
			}

			attachForm.attachField(field.getName(), formField);
		}
	}

	@Override
	public void commit() {
		try {
			fieldGroup.commit();
			attachForm.setValid(true);
		} catch (CommitException e) {
			attachForm.setValid(false);
			NotificationUtil.showErrorNotification(e.getCause().getMessage());
		} catch (Exception e) {
			throw new MyCollabException(e);
		}
	}

	@Override
	public void preCommit(FieldGroup.CommitEvent commitEvent)
			throws CommitException {
		for (Object propertyId : fieldGroup.getBoundPropertyIds()) {
			fieldGroup.getField(propertyId).removeStyleName("errorField");
		}
		StringBuilder errorMsg = new StringBuilder();
		int violationCount = 0;
		for (Field<?> f : commitEvent.getFieldBinder().getFields()) {
			try {
				if (f instanceof CustomField) {
					continue;
				}
				f.validate();
			} catch (com.vaadin.data.Validator.InvalidValueException e) {
				violationCount++;
				errorMsg.append(e.getHtmlMessage()).append("<br/>");
				f.addStyleName("errorField");
			}
		}
		if (violationCount > 0) {
			throw new CommitException(errorMsg.toString());
		}
	}

	@Override
	public void postCommit(FieldGroup.CommitEvent commitEvent)
			throws CommitException {
		Set<ConstraintViolation<B>> violations = validation.validate(attachForm
				.getBean());
		if (violations.size() > 0) {
			StringBuilder errorMsg = new StringBuilder();

			for (@SuppressWarnings("rawtypes")
			ConstraintViolation violation : violations) {
				errorMsg.append(violation.getMessage()).append("<br/>");

				if (violation.getPropertyPath() != null
						&& !violation.getPropertyPath().toString().equals("")) {
					fieldGroup.getField(violation.getPropertyPath().toString())
							.addStyleName("errorField");
				} else {
					Annotation validateAnno = violation
							.getConstraintDescriptor().getAnnotation();
					if (validateAnno instanceof DateComparision) {
						String firstDateField = ((DateComparision) validateAnno)
								.firstDateField();
						String lastDateField = ((DateComparision) validateAnno)
								.lastDateField();

						fieldGroup.getField(firstDateField).addStyleName(
								"errorField");
						fieldGroup.getField(lastDateField).addStyleName(
								"errorField");
					}
				}

			}
			throw new CommitException(errorMsg.toString());
		}
	}

	abstract protected Field<?> onCreateField(Object propertyId);
}
