/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.ui;

import com.mycollab.core.UserInvalidInputException;
import com.mycollab.core.arguments.NotBindable;
import com.mycollab.core.utils.ClassUtils;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.field.DefaultViewField;
import com.vaadin.data.*;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.TextField;
import org.apache.commons.beanutils.BeanUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author MyCollab Ltd
 * @since 5.2.3
 */
public abstract class AbstractBeanFieldGroupFieldFactory<B> implements IBeanFieldGroupFieldFactory<B> {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractBeanFieldGroupFieldFactory.class);

    private BeanValidationBinder<B> binder;
    private boolean isReadOnlyGroup;
    private javax.validation.Validator validation;

    protected GenericBeanForm<B> attachForm;

    public AbstractBeanFieldGroupFieldFactory(GenericBeanForm<B> form, boolean isValidateForm, boolean isReadOnlyGroup) {
        this.attachForm = form;
        this.isReadOnlyGroup = isReadOnlyGroup;

        if (isValidateForm) {
            validation = AppContextUtil.getValidator();
        }
    }

    @Override
    public void setBean(B bean) {
        binder = new BeanValidationBinder<>((Class<B>) bean.getClass());

        IFormLayoutFactory layoutFactory = attachForm.getLayoutFactory();
        if (layoutFactory instanceof WrappedFormLayoutFactory) {
            layoutFactory = ((WrappedFormLayoutFactory) layoutFactory).getWrappedFactory();
        }
        if (layoutFactory instanceof IDynaFormLayout) {
            IDynaFormLayout dynaFormLayout = (IDynaFormLayout) layoutFactory;
            Set<String> bindFields = dynaFormLayout.bindFields();
            for (String bindField : bindFields) {
                HasValue<?> formField = onCreateField(bindField);
                if (formField == null) {
                    if (isReadOnlyGroup) {
                        formField = new DefaultViewField();
                    } else {
                        formField = new TextField();
                    }
                } else {
                    if (formField instanceof IgnoreBindingField) {
                        attachForm.attachField(bindField, formField);
                        continue;
                    } else {
                        Binder.BindingBuilder<B, ?> bindingBuilder = binder.forField(formField);

                        if (formField instanceof Converter) {
                            bindingBuilder.withConverter((Converter) formField);
                        }
                        bindingBuilder.bind(bindField);
                    }
                }

                if (formField instanceof DateField) {
                    ((DateField) formField).setZoneId(UserUIContext.getUserTimeZone());
                    ((DateField) formField).setDateFormat(AppUI.getDateFormat());
                }
                attachForm.attachField(bindField, formField);
            }
        } else {
            Class<?> beanClass = bean.getClass();
            java.lang.reflect.Field[] fields = ClassUtils.getAllFields(beanClass);
            for (java.lang.reflect.Field field : fields) {
                HasValue<?> formField = onCreateField(field.getName());
                if (formField == null) {
                    if (field.getAnnotation(NotBindable.class) != null) {
                        continue;
                    } else {
                        if (isReadOnlyGroup) {
                            try {
                                String propertyValue = BeanUtils.getProperty(attachForm.getBean(), field.getName());
                                formField = new DefaultViewField(propertyValue);
                            } catch (Exception e) {
                                LOG.error("Error while get field value", e);
                                formField = new DefaultViewField("Error");
                            }
                        } else {
                            formField = new TextField();
                        }
                    }
                } else {
                    if (formField instanceof IgnoreBindingField) {
                        attachForm.attachField(field.getName(), formField);
                        continue;
                    } else {
                        Binder.BindingBuilder<B, ?> bindingBuilder = binder.forField(formField);

                        if (formField instanceof Converter) {
                            bindingBuilder.withConverter((Converter) formField);
                        }
                        bindingBuilder.bind(field.getName());
                    }
                }

                if (formField instanceof DateField) {
                    ((DateField) formField).setZoneId(UserUIContext.getUserTimeZone());
                    ((DateField) formField).setDateFormat(AppUI.getDateFormat());
                }

                attachForm.attachField(field.getName(), formField);
            }
        }
        binder.readBean(bean);
    }

    @Override
    public boolean commit() {
        try {
            binder.writeBean(attachForm.getBean());
        } catch (ValidationException e) {
            List<BindingValidationStatus<?>> fieldValidationErrors = e.getFieldValidationErrors();
            String errorMessage = fieldValidationErrors.stream().filter(it -> it.getStatus() == BindingValidationStatus.Status.ERROR ||
                    it.getStatus() == BindingValidationStatus.Status.UNRESOLVED)
                    .map(BindingValidationStatus::getMessage)
                    // sanitize the individual error strings to avoid code injection
                    // since we are displaying the resulting string as HTML
                    .map(errorString -> Jsoup.clean(errorString.orElse(""), Whitelist.simpleText()))
                    .collect(Collectors.joining("<br>"));
            throw new UserInvalidInputException(errorMessage);
        }

        Set<ConstraintViolation<B>> violations = validation.validate(attachForm.getBean());
        if (violations.size() > 0) {
            StringBuilder errorMsg = new StringBuilder();

            for (ConstraintViolation violation : violations) {
                errorMsg.append(violation.getMessage()).append("<br/>");

                Path propertyPath = violation.getPropertyPath();
                if (propertyPath != null && !propertyPath.toString().equals("")) {
                    Binder.Binding<B, ?> binding = binder.getBinding(propertyPath.toString()).orElse(null);
                    if (binding != null) {
                        ((Component) binding.getField()).addStyleName("errorField");
                    }
                }
            }
            throw new UserInvalidInputException(errorMsg.toString());
        }
        return true;
    }

    abstract protected HasValue<?> onCreateField(Object propertyId);
}
