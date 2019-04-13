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
package com.mycollab.module.project.view.settings;

import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.mycollab.core.utils.DateTimeUtils;
import com.mycollab.db.arguments.DateSearchField;
import com.mycollab.db.arguments.SearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.domain.Version;
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria;
import com.mycollab.module.project.service.ProjectTicketService;
import com.mycollab.module.project.view.ticket.TicketRowRenderer;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.mycollab.vaadin.ui.field.DateViewField;
import com.mycollab.vaadin.ui.field.I18nFormViewField;
import com.mycollab.vaadin.ui.field.RichTextViewField;
import com.mycollab.vaadin.web.ui.AdvancedPreviewBeanForm;
import com.mycollab.vaadin.web.ui.DefaultBeanPagedList;
import com.mycollab.vaadin.web.ui.DefaultDynaFormLayout;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.mycollab.vaadin.web.ui.field.ContainerViewField;
import com.vaadin.data.HasValue;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Label;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.2.10
 */
public class VersionPreviewForm extends AdvancedPreviewBeanForm<Version> {
    @Override
    public void setBean(Version bean) {
        setFormLayoutFactory(new DefaultDynaFormLayout(ProjectTypeConstants.VERSION,
                VersionDefaultFormLayoutFactory.getReadForm(), Version.Field.name.name()));
        setBeanFormFieldFactory(new ReadFormFieldFactory(this));
        super.setBean(bean);
    }

    private static class ReadFormFieldFactory extends AbstractBeanFieldGroupViewFieldFactory<Version> {
        private static final long serialVersionUID = 1L;

        ReadFormFieldFactory(GenericBeanForm<Version> form) {
            super(form);
        }

        @Override
        protected HasValue<?> onCreateField(Object propertyId) {
            Version beanItem = attachForm.getBean();
            if (Version.Field.duedate.equalTo(propertyId)) {
                return new DateViewField();
            } else if ("section-assignments".equals(propertyId)) {
                ContainerViewField containerField = new ContainerViewField();
                containerField.addComponentField(new TicketsComp(beanItem));
                return containerField;
            } else if (Version.Field.status.equalTo(propertyId)) {
                return new I18nFormViewField(StatusI18nEnum.class).withStyleName(WebThemes.FIELD_NOTE);
            } else if (Version.Field.description.equalTo(propertyId)) {
                return new RichTextViewField();
            }
            return null;
        }
    }

    private static class TicketsComp extends MVerticalLayout {
        private ProjectTicketSearchCriteria searchCriteria;
        private DefaultBeanPagedList<ProjectTicketService, ProjectTicketSearchCriteria, ProjectTicket> ticketList;

        TicketsComp(Version beanItem) {
            withMargin(false).withSpacing(true).withFullWidth();

            CheckBox openSelection = new CheckBox(UserUIContext.getMessage(StatusI18nEnum.Open), true);
            openSelection.addValueChangeListener(valueChangeEvent -> {
                if (openSelection.getValue()) {
                    searchCriteria.setOpen(new SearchField());
                } else {
                    searchCriteria.setOpen(null);
                }
                updateSearchStatus();
            });

            CheckBox overdueSelection = new CheckBox(UserUIContext.getMessage(StatusI18nEnum.Overdue), false);
            overdueSelection.addValueChangeListener(valueChangeEvent -> {
                if (overdueSelection.getValue()) {
                    searchCriteria.setDueDate(new DateSearchField(DateTimeUtils.getCurrentDateWithoutMS().toLocalDate(),
                            DateSearchField.LESS_THAN));
                } else {
                    searchCriteria.setDueDate(null);
                }
                updateSearchStatus();
            });

            Label spacingLbl1 = new Label("");

            MHorizontalLayout header = new MHorizontalLayout(openSelection, overdueSelection, spacingLbl1).alignAll(Alignment.MIDDLE_LEFT);

            ticketList = new DefaultBeanPagedList<>(AppContextUtil.getSpringBean(ProjectTicketService.class), new TicketRowRenderer());
            ticketList.setControlStyle("");

            searchCriteria = new ProjectTicketSearchCriteria();
            searchCriteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
            searchCriteria.setTypes(new SetSearchField<>(ProjectTypeConstants.BUG, ProjectTypeConstants.TASK));
            searchCriteria.setVersionIds(new SetSearchField<>(beanItem.getId()));
            updateSearchStatus();

            this.with(header, ticketList);
        }

        private void updateSearchStatus() {
            ticketList.setSearchCriteria(searchCriteria);
        }
    }
}
