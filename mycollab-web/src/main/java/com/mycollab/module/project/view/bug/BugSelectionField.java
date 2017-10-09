package com.mycollab.module.project.view.bug;

import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.tracker.domain.SimpleBug;
import com.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.mycollab.module.tracker.service.BugService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.ui.FieldSelection;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import org.vaadin.suggestfield.BeanSuggestionConverter;
import org.vaadin.suggestfield.SuggestField;
import org.vaadin.suggestfield.client.SuggestFieldSuggestion;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.2.12
 */
public class BugSelectionField extends CustomField<SimpleBug> implements FieldSelection<SimpleBug> {
    private SimpleBug selectedBug;
    private SuggestField suggestField;
    private List<SimpleBug> items;
    private BugService bugService;

    public BugSelectionField() {
        bugService = AppContextUtil.getSpringBean(BugService.class);
        suggestField = new SuggestField();
        suggestField.setPopupWidth(600);
        suggestField.setWidth("400px");
        suggestField.setInputPrompt("Enter related bug's name");
        suggestField.setInvalidAllowed(false);
        suggestField.setSuggestionHandler(this::handleSearchQuery);

        suggestField.setSuggestionConverter(new BugSuggestionConverter());
    }

    public SimpleBug getSelectedBug() {
        return selectedBug;
    }

    @Override
    protected Component initContent() {
        MHorizontalLayout layout = new MHorizontalLayout();
        MButton browseBtn = new MButton(FontAwesome.ELLIPSIS_H)
                .withListener(clickEvent -> UI.getCurrent().addWindow(new BugSelectionWindow(BugSelectionField.this)))
                .withStyleName(WebThemes.BUTTON_OPTION, WebThemes.BUTTON_SMALL_PADDING);
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
        if ("".equals(query) || query == null) {
            return Collections.emptyList();
        }
        BugSearchCriteria searchCriteria = new BugSearchCriteria();
        searchCriteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
        searchCriteria.setName(StringSearchField.and(query));
        items = (List<SimpleBug>) bugService.findPageableListByCriteria(new BasicSearchRequest<>(searchCriteria));
        return new ArrayList<>(items);
    }

    private class BugSuggestionConverter extends BeanSuggestionConverter {

        private BugSuggestionConverter() {
            super(SimpleBug.class, "id", "name", "name");
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
