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
package com.esofthead.mycollab.module.project.view.settings;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum;
import com.esofthead.mycollab.module.project.view.bug.components.BugRowRenderer;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.domain.Version;
import com.esofthead.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.spring.AppContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.web.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.vaadin.web.ui.DefaultBeanPagedList;
import com.esofthead.mycollab.vaadin.web.ui.DynaFormLayout;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.esofthead.mycollab.vaadin.web.ui.field.ContainerViewField;
import com.esofthead.mycollab.vaadin.web.ui.field.DateViewField;
import com.esofthead.mycollab.vaadin.web.ui.field.I18nFormViewField;
import com.vaadin.data.Property;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Field;
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
        setFormLayoutFactory(new DynaFormLayout(ProjectTypeConstants.BUG_VERSION,
                VersionDefaultFormLayoutFactory.getForm(), Version.Field.versionname.name()));
        setBeanFormFieldFactory(new ReadFormFieldFactory(this));
        super.setBean(bean);
    }

    private static class ReadFormFieldFactory extends AbstractBeanFieldGroupViewFieldFactory<Version> {
        private static final long serialVersionUID = 1L;

        ReadFormFieldFactory(GenericBeanForm<Version> form) {
            super(form);
        }

        @Override
        protected Field<?> onCreateField(Object propertyId) {
            Version beanItem = attachForm.getBean();
            if (Version.Field.duedate.equalTo(propertyId)) {
                return new DateViewField(beanItem.getDuedate());
            } else if (Version.Field.id.equalTo(propertyId)) {
                ContainerViewField containerField = new ContainerViewField();
                containerField.addComponentField(new BugsComp(beanItem));
                return containerField;
            } else if (Version.Field.status.equalTo(propertyId)) {
                return new I18nFormViewField(beanItem.getStatus(), com.esofthead.mycollab.common.i18n.OptionI18nEnum
                        .StatusI18nEnum.class).withStyleName
                        (UIConstants.FIELD_NOTE);
            }
            return null;
        }
    }

    private static class BugsComp extends MVerticalLayout {
        private BugSearchCriteria searchCriteria;
        private DefaultBeanPagedList<BugService, BugSearchCriteria, SimpleBug> bugList;

        BugsComp(Version beanItem) {
            withMargin(false).withWidth("100%");
            MHorizontalLayout header = new MHorizontalLayout();

            final CheckBox openSelection = new BugStatusCheckbox(OptionI18nEnum.BugStatus.Open, true);
            CheckBox reOpenSelection = new BugStatusCheckbox(OptionI18nEnum.BugStatus.ReOpen, true);
            CheckBox verifiedSelection = new BugStatusCheckbox(OptionI18nEnum.BugStatus.Verified, true);
            CheckBox resolvedSelection = new BugStatusCheckbox(OptionI18nEnum.BugStatus.Resolved, true);

            Label spacingLbl1 = new Label("");

            header.with(openSelection, reOpenSelection, verifiedSelection, resolvedSelection,
                    spacingLbl1).alignAll(Alignment.MIDDLE_LEFT);

            bugList = new DefaultBeanPagedList<>(AppContextUtil.getSpringBean(BugService.class), new BugRowRenderer());
            bugList.setMargin(new MarginInfo(true, true, true, false));
            bugList.setControlStyle("");

            searchCriteria = new BugSearchCriteria();
            searchCriteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
            searchCriteria.setVersionids(new SetSearchField<>(beanItem.getId()));
            searchCriteria.setStatuses(new SetSearchField<>(OptionI18nEnum.BugStatus.Open.name(), OptionI18nEnum.BugStatus.ReOpen.name(),
                    OptionI18nEnum.BugStatus.Verified.name(), OptionI18nEnum.BugStatus.Resolved.name()));
            updateSearchStatus();

            this.with(header, bugList);
        }

        private void updateTypeSearchStatus(boolean selection, String type) {
            SetSearchField<String> types = searchCriteria.getStatuses();
            if (types == null) {
                types = new SetSearchField<>();
            }
            if (selection) {
                types.addValue(type);
            } else {
                types.removeValue(type);
            }
            searchCriteria.setStatuses(types);
            updateSearchStatus();
        }

        private void updateSearchStatus() {
            bugList.setSearchCriteria(searchCriteria);
        }

        private class BugStatusCheckbox extends CheckBox {
            BugStatusCheckbox(final Enum name, boolean defaultValue) {
                super(AppContext.getMessage(name), defaultValue);
                this.addValueChangeListener(new ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                        updateTypeSearchStatus(BugStatusCheckbox.this.getValue(), name.name());
                    }
                });
            }
        }
    }
}
