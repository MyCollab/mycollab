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
import com.esofthead.mycollab.core.UserInvalidInputException;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum;
import com.esofthead.mycollab.module.project.view.bug.IBugCallbackStatusComp;
import com.esofthead.mycollab.module.tracker.domain.RelatedBug;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.esofthead.mycollab.module.tracker.service.BugRelationService;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.*;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.esofthead.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.vaadin.suggestfield.BeanSuggestionConverter;
import org.vaadin.suggestfield.SuggestField;
import org.vaadin.suggestfield.client.SuggestFieldSuggestion;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.6.0
 */
public class LinkIssueWindow extends Window {
    private BugService bugService = ApplicationContextUtil.getSpringBean(BugService.class);

    private RelatedBugEditForm editForm;

    private BugSearchCriteria searchCriteria;
    private SimpleBug selectedBug;
    private SimpleBug hostedBug;
    private RelatedBug relatedBug;
    private IBugCallbackStatusComp callbackForm;

    public LinkIssueWindow(IBugCallbackStatusComp callbackForm, SimpleBug bug) {
        super("Link");
        this.callbackForm = callbackForm;
        this.setResizable(false);
        this.setModal(true);

        this.hostedBug = bug;
        searchCriteria = new BugSearchCriteria();
        searchCriteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));

        MVerticalLayout contentLayout = new MVerticalLayout().withMargin(false).withWidth("100%");

        editForm = new RelatedBugEditForm();
        relatedBug = new RelatedBug();
        relatedBug.setBugid(bug.getId());
        relatedBug.setRelatetype(OptionI18nEnum.BugRelation.Duplicated.name());
        editForm.setBean(relatedBug);
        contentLayout.add(editForm);
        this.center();
        this.setWidth("700px");
        this.setContent(contentLayout);
    }

    private class RelatedBugEditForm extends AdvancedEditBeanForm<RelatedBug> {
        @Override
        public void setBean(RelatedBug newDataSource) {
            this.setFormLayoutFactory(new FormLayoutFactory());
            this.setBeanFormFieldFactory(new EditFormFieldFactory(RelatedBugEditForm.this));
            super.setBean(newDataSource);
        }

        class FormLayoutFactory implements IFormLayoutFactory {
            private GridFormLayoutHelper informationLayout;

            @Override
            public ComponentContainer getLayout() {
                final VerticalLayout layout = new VerticalLayout();
                informationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(1, 3);

                layout.addComponent(informationLayout.getLayout());

                final MHorizontalLayout controlsBtn = new MHorizontalLayout().withMargin(new MarginInfo(true, true, true, false));
                layout.addComponent(controlsBtn);
                layout.setComponentAlignment(controlsBtn, Alignment.MIDDLE_RIGHT);

                Button saveBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_SAVE), new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent clickEvent) {
                        if (editForm.validateForm()) {
                            BugRelationService relatedBugService = ApplicationContextUtil.getSpringBean(BugRelationService.class);

                            if (selectedBug == null) {
                                throw new UserInvalidInputException("The related bug must be not null");
                            }

                            if (selectedBug.getId().equals(hostedBug.getId())) {
                                throw new UserInvalidInputException("The relation is invalid since the both entries are " + "the same");
                            }

                            relatedBug.setRelatedid(selectedBug.getId());
                            relatedBugService.saveWithSession(relatedBug, AppContext.getUsername());
                            LinkIssueWindow.this.close();
                            callbackForm.refreshBugItem();
                        }
                    }
                });
                saveBtn.addStyleName(UIConstants.BUTTON_ACTION);
                saveBtn.setIcon(FontAwesome.SAVE);
                saveBtn.setClickShortcut(ShortcutAction.KeyCode.ENTER);

                Button cancelBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL), new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent clickEvent) {
                        LinkIssueWindow.this.close();
                    }
                });
                cancelBtn.addStyleName(UIConstants.BUTTON_OPTION);

                controlsBtn.with(cancelBtn, saveBtn).alignAll(Alignment.MIDDLE_RIGHT);
                return layout;
            }

            @Override
            public void attachField(Object propertyId, Field<?> field) {
                if (RelatedBug.Field.relatetype.equalTo(propertyId)) {
                    informationLayout.addComponent(field, "This bug", 0, 0);
                } else if (RelatedBug.Field.relatedid.equalTo(propertyId)) {
                    informationLayout.addComponent(field, "Bug", 0, 1);
                } else if (RelatedBug.Field.comment.equalTo(propertyId)) {
                    informationLayout.addComponent(field, "Comment", 0, 2);
                }
            }
        }

        private class EditFormFieldFactory extends AbstractBeanFieldGroupEditFieldFactory<RelatedBug> {
            EditFormFieldFactory(GenericBeanForm<RelatedBug> form) {
                super(form);
            }

            @Override
            protected Field<?> onCreateField(Object propertyId) {
                if (RelatedBug.Field.relatetype.equalTo(propertyId)) {
                    return new BugRelationComboBox();
                } else if (RelatedBug.Field.relatedid.equalTo(propertyId)) {
                    return new RelatedBugField();
                } else if (RelatedBug.Field.comment.equalTo(propertyId)) {
                    return new RichTextArea();
                }
                return null;
            }
        }

        private class RelatedBugField extends CustomField<SimpleBug> implements FieldSelection<SimpleBug> {
            SuggestField suggestField;
            List<SimpleBug> items;

            RelatedBugField() {
                suggestField = new SuggestField();
                suggestField.setPopupWidth(600);
                suggestField.setWidth("400px");
                suggestField.setInputPrompt("Enter related bug's name");
                suggestField.setInvalidAllowed(false);

                suggestField.setSuggestionHandler(new SuggestField.SuggestionHandler() {
                    @Override
                    public List<Object> searchItems(String query) {
                        return handleSearchQuery(query);
                    }
                });

                suggestField.setSuggestionConverter(new BugSuggestionConverter());
            }

            @Override
            protected Component initContent() {
                MHorizontalLayout layout = new MHorizontalLayout();
                Button browseBtn = new Button(FontAwesome.ELLIPSIS_H);
                browseBtn.addStyleName(UIConstants.BUTTON_OPTION);
                browseBtn.addStyleName(UIConstants.BUTTON_SMALL_PADDING);
                browseBtn.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        UI.getCurrent().addWindow(new BugSelectionWindow(RelatedBugField.this));
                    }
                });
                layout.with(suggestField, new Label("or browse"), browseBtn);
                return layout;
            }

            @Override
            public Class<? extends SimpleBug> getType() {
                return SimpleBug.class;
            }

            @Override
            public void fireValueChange(SimpleBug data) {
                selectedBug = data;
                suggestField.setValue(selectedBug);
            }

            private List<Object> handleSearchQuery(String query) {
                if ("" .equals(query) || query == null) {
                    return Collections.emptyList();
                }
                searchCriteria.setSummary(StringSearchField.and(query));
                items = bugService.findPagableListByCriteria(new SearchRequest<>(searchCriteria));
                return new ArrayList<Object>(items);
            }

            private class BugSuggestionConverter extends BeanSuggestionConverter {

                public BugSuggestionConverter() {
                    super(SimpleBug.class, "id", "summary", "summary");
                }

                @Override
                public Object toItem(SuggestFieldSuggestion suggestion) {
                    for (SimpleBug bean : items) {
                        if (bean.getId().toString().equals(suggestion.getId())) {
                            selectedBug = bean;
                            break;
                        }
                    }
                    assert selectedBug != null : "This should not be happening";
                    return selectedBug;
                }
            }
        }
    }
}
