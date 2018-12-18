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
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.view.bug.BugRowRenderer;
import com.mycollab.module.tracker.domain.SimpleBug;
import com.mycollab.module.tracker.domain.Version;
import com.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.mycollab.module.tracker.service.BugService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.ui.field.DateViewField;
import com.mycollab.vaadin.ui.field.I18nFormViewField;
import com.mycollab.vaadin.ui.field.RichTextViewField;
import com.mycollab.vaadin.web.ui.AdvancedPreviewBeanForm;
import com.mycollab.vaadin.web.ui.DefaultBeanPagedList;
import com.mycollab.vaadin.web.ui.DefaultDynaFormLayout;
import com.mycollab.vaadin.web.ui.field.ContainerViewField;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.data.HasValue;
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
        setFormLayoutFactory(new DefaultDynaFormLayout(ProjectTypeConstants.BUG_VERSION,
                VersionDefaultFormLayoutFactory.getForm(), Version.Field.name.name()));
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
                return new DateViewField(beanItem.getDuedate());
            } else if (Version.Field.id.equalTo(propertyId)) {
                ContainerViewField containerField = new ContainerViewField();
                containerField.addComponentField(new BugsComp(beanItem));
                return containerField;
            } else if (Version.Field.status.equalTo(propertyId)) {
                return new I18nFormViewField(beanItem.getStatus(), StatusI18nEnum.class).withStyleName(UIConstants.FIELD_NOTE);
            } else if (Version.Field.description.equalTo(propertyId)) {
                return new RichTextViewField(beanItem.getDescription());
            }
            return null;
        }
    }

    private static class BugsComp extends MVerticalLayout {
        private BugSearchCriteria searchCriteria;
        private DefaultBeanPagedList<BugService, BugSearchCriteria, SimpleBug> bugList;

        BugsComp(Version beanItem) {
            withMargin(false).withFullWidth();
            MHorizontalLayout header = new MHorizontalLayout();

            final CheckBox openSelection = new BugStatusCheckbox(StatusI18nEnum.Open, true);
            CheckBox reOpenSelection = new BugStatusCheckbox(StatusI18nEnum.ReOpen, true);
            CheckBox verifiedSelection = new BugStatusCheckbox(StatusI18nEnum.Verified, true);
            CheckBox resolvedSelection = new BugStatusCheckbox(StatusI18nEnum.Resolved, true);

            Label spacingLbl1 = new Label("");

            header.with(openSelection, reOpenSelection, verifiedSelection, resolvedSelection, spacingLbl1).alignAll(Alignment.MIDDLE_LEFT);

            bugList = new DefaultBeanPagedList<>(AppContextUtil.getSpringBean(BugService.class), new BugRowRenderer());
            bugList.setControlStyle("");

            searchCriteria = new BugSearchCriteria();
            searchCriteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
            searchCriteria.setVersionids(new SetSearchField<>(beanItem.getId()));
            searchCriteria.setStatuses(new SetSearchField<>(StatusI18nEnum.Open.name(), StatusI18nEnum.ReOpen.name(),
                    StatusI18nEnum.Verified.name(), StatusI18nEnum.Resolved.name()));
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
                super(UserUIContext.getMessage(name), defaultValue);
                this.addValueChangeListener(event -> updateTypeSearchStatus(getValue(), name.name()));
            }
        }
    }
}
