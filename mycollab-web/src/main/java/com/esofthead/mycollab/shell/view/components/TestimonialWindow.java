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
package com.esofthead.mycollab.shell.view.components;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.support.domain.TestimonialForm;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.*;
import com.esofthead.mycollab.vaadin.ui.grid.GridFormLayoutHelper;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.1.3
 */
public class TestimonialWindow extends Window {
    private static Logger LOG = LoggerFactory.getLogger(TestimonialWindow.class);

    TestimonialWindow() {
        super("Thank you! We appreciate your help!");
        this.setModal(true);
        this.setResizable(false);
        this.setWidth("900px");

        MVerticalLayout content = new MVerticalLayout().withMargin(false);

        final TestimonialForm entity = new TestimonialForm();
        final AdvancedEditBeanForm<TestimonialForm> editForm = new AdvancedEditBeanForm<>();
        editForm.setFormLayoutFactory(new IFormLayoutFactory() {
            GridFormLayoutHelper gridFormLayoutHelper;

            @Override
            public ComponentContainer getLayout() {
                gridFormLayoutHelper = GridFormLayoutHelper.defaultFormLayoutHelper(2, 4);
                return gridFormLayoutHelper.getLayout();
            }

            @Override
            public void attachField(Object propertyId, Field<?> field) {
                if ("displayname".equals(propertyId)) {
                    gridFormLayoutHelper.addComponent(field, "Name", 0, 0, 2, "100%");
                } else if ("company".equals(propertyId)) {
                    gridFormLayoutHelper.addComponent(field, "Company", 0, 1);
                } else if ("jobrole".equals(propertyId)) {
                    gridFormLayoutHelper.addComponent(field, "Job Role", 1, 1);
                } else if ("website".equals(propertyId)) {
                    gridFormLayoutHelper.addComponent(field, "Website", 0, 2);
                } else if ("email".equals(propertyId)) {
                    gridFormLayoutHelper.addComponent(field, "Email", 1, 2);
                } else if ("testimonial".equals(propertyId)) {
                    gridFormLayoutHelper.addComponent(field, "Testimonial", 0, 3, 2, "100%");
                }
            }
        });
        editForm.setBeanFormFieldFactory(new AbstractBeanFieldGroupEditFieldFactory<TestimonialForm>(editForm) {
            @Override
            protected Field<?> onCreateField(Object propertyId) {
                if ("testimonial".equals(propertyId)) {
                    return new TextArea();
                }
                return null;
            }
        });
        editForm.setBean(entity);
        content.addComponent(editForm);

        MHorizontalLayout buttonControls = new MHorizontalLayout().withMargin(true);

        Button cancelBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                TestimonialWindow.this.close();
            }
        });
        cancelBtn.setStyleName(UIConstants.THEME_GRAY_LINK);

        Button submitBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_SUBMIT), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                if (editForm.validateForm()) {
                    TestimonialWindow.this.close();
                    NotificationUtil.showNotification("We appreciate your kindness action", "Thank you for your time");
                    try {
                        RestTemplate restTemplate = new RestTemplate();
                        restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
                        MultiValueMap<String, Object> values = new LinkedMultiValueMap<>();
                        values.add("company", entity.getCompany());
                        values.add("displayname", entity.getDisplayname());
                        values.add("email", entity.getEmail());
                        values.add("jobrole", entity.getJobrole());
                        values.add("testimonial", entity.getTestimonial());
                        values.add("website", entity.getWebsite());
                        restTemplate.postForObject("https://api.mycollab.com/api/testimonial", values, String.class);
                    } catch (Exception e) {
                        LOG.error("Error when call remote api", e);
                    }
                }
            }
        });
        submitBtn.setStyleName(UIConstants.BUTTON_ACTION);
        submitBtn.setIcon(FontAwesome.MAIL_FORWARD);

        buttonControls.with(cancelBtn, submitBtn);

        content.with(buttonControls).withAlign(buttonControls, Alignment.MIDDLE_RIGHT);
        this.setContent(content);
    }
}
