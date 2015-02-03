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
package com.esofthead.mycollab.module.project.view.bug.components;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.tracker.domain.RelatedBug;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.*;
import com.vaadin.data.Property;
import com.vaadin.data.util.PropertyFormatter;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;
import org.vaadin.suggestfield.BeanSuggestionConverter;
import org.vaadin.suggestfield.SuggestField;
import org.vaadin.suggestfield.client.SuggestFieldSuggestion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.6.0
 */
public class LinkIssueWindow extends Window {
    public LinkIssueWindow(SimpleBug bug) {
        super("Link");
        this.setResizable(false);
        this.setModal(true);

        MVerticalLayout contentLayout = new MVerticalLayout().withMargin(false)
                .withWidth("100%");

        RelatedBugEditForm form = new RelatedBugEditForm();
        RelatedBug relatedBug = new RelatedBug();
        relatedBug.setBugid(bug.getId());
        relatedBug.setRelatetype(ProjectTypeConstants.BUG);
        form.setBean(relatedBug);
        contentLayout.add(form);
        this.center();
        this.setWidth("700px");
        this.setContent(contentLayout);
    }

    private class RelatedBugEditForm extends AdvancedEditBeanForm<RelatedBug> {
        private List<SimpleBug> items = new ArrayList<>();

        @Override
        public void setBean(final RelatedBug newDataSource) {
            this.setFormLayoutFactory(new FormLayoutFactory());
            this.setBeanFormFieldFactory(new EditFormFieldFactory(RelatedBugEditForm.this));
            super.setBean(newDataSource);
        }

        class FormLayoutFactory implements IFormLayoutFactory {
            private GridFormLayoutHelper informationLayout;

            @Override
            public ComponentContainer getLayout() {
                final VerticalLayout layout = new VerticalLayout();
                this.informationLayout = new GridFormLayoutHelper(1, 3, "100%",
                        "167px", Alignment.TOP_LEFT);
                this.informationLayout.getLayout().setWidth("100%");
                this.informationLayout.getLayout().setMargin(false);
                this.informationLayout.getLayout().addStyleName(
                        "colored-gridlayout");

                layout.addComponent(this.informationLayout.getLayout());

                final MHorizontalLayout controlsBtn = new MHorizontalLayout().withMargin(new MarginInfo(true, true,
                        true, false));
                layout.addComponent(controlsBtn);
                layout.setComponentAlignment(controlsBtn, Alignment.MIDDLE_RIGHT);

                Button saveBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_SAVE));
                saveBtn.setStyleName(UIConstants.THEME_GREEN_LINK);

                Button cancelBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL), new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent clickEvent) {
                        LinkIssueWindow.this.close();
                    }
                });
                cancelBtn.setStyleName(UIConstants.THEME_GRAY_LINK);

                controlsBtn.with(saveBtn, cancelBtn).alignAll(Alignment.MIDDLE_RIGHT);
                return layout;
            }

            @Override
            public void attachField(Object propertyId, Field<?> field) {
                if (RelatedBug.Field.relatetype.equalTo(propertyId)) {
                    informationLayout.addComponent(field, "This bug", 0, 0);
                } else if (RelatedBug.Field.bugid.equalTo(propertyId)) {
                    informationLayout.addComponent(field, "Bug", 0, 1);
                } else if (RelatedBug.Field.comment.equalTo(propertyId)) {
                    informationLayout.addComponent(field, "Comment", 0, 2);
                }
            }
        }

        private class EditFormFieldFactory extends
                AbstractBeanFieldGroupEditFieldFactory<RelatedBug> {

            EditFormFieldFactory(GenericBeanForm<RelatedBug> form) {
                super(form);
            }

            @Override
            protected Field<?> onCreateField(Object propertyId) {
                if (RelatedBug.Field.relatetype.equalTo(propertyId)) {
                    return new BugRelationComboBox();
                } else if (RelatedBug.Field.relatedid.equalTo(propertyId)) {
                    return new RelatedBugSuggestField();
                } else if (RelatedBug.Field.comment.equalTo(propertyId)) {
                    return new RichTextArea();
                }
                return null;
            }
        }

        private class RelatedBugSuggestField extends SuggestField {
            private BugService bugService = ApplicationContextUtil.getSpringBean(BugService.class);
            private BugSearchCriteria searchCriteria;

            RelatedBugSuggestField() {
                this.setInputPrompt("Type bug summary or key");
                this.setPopupWidth(600);
                this.setSuggestionHandler(new SuggestField.SuggestionHandler() {
                    @Override
                    public List<Object> searchItems(String query) {
                        return handleSearchQuery(query);
                    }
                });

                this.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        System.out.println("SuugestField value changed");
                        Notification.show("Selected " + RelatedBugSuggestField.this.getValue());
                    }
                });

                this.setSuggestionConverter(new RelatedBuggestionConverter());
                searchCriteria = new BugSearchCriteria();
                searchCriteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
            }

            @Override
            public void setPropertyDataSource(Property newDataSource) {
                super.setPropertyDataSource(newDataSource);
            }

            private List<Object> handleSearchQuery(String query) {
                if ("".equals(query) || query == null) {
                    return Collections.emptyList();
                }

                items = bugService.findPagableListByCriteria(new SearchRequest<>(searchCriteria));
                return new ArrayList<Object>(items);
            }
        }

        private class RelatedBuggestionConverter extends BeanSuggestionConverter {

            public RelatedBuggestionConverter() {
                super(SimpleBug.class, "id", "summary", "summary");
            }

            @Override
            public Object toItem(SuggestFieldSuggestion suggestion) {
                SimpleBug result = null;
                for (SimpleBug bean : items) {
                    if (bean.getId().toString().equals(suggestion.getId())) {
                        result = bean;
                        break;
                    }
                }
                return result.getId();
            }

        }
    }
}
