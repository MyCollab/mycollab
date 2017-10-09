package com.mycollab.mobile.ui;

import com.mycollab.core.utils.StringUtils;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.vaadin.event.HasSearchHandlers;
import com.mycollab.vaadin.event.SearchHandler;
import com.vaadin.ui.TextField;
import org.vaadin.jouni.dom.Dom;
import org.vaadin.resetbuttonfortextfield.ResetButtonForTextField;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mycollab Ltd
 * @since 5.2.5
 */
public abstract class SearchInputField<S extends SearchCriteria> extends TextField implements HasSearchHandlers<S> {
    private List<SearchHandler<S>> searchHandlers;

    public SearchInputField() {
        this.setImmediate(true);
        this.setStyleName("searchinputfield");
        Dom dom = new Dom(this);
        dom.setAttribute("placeholder", "Search");
        this.setTextChangeEventMode(TextChangeEventMode.TIMEOUT);
        this.setTextChangeTimeout(3000);
        this.addValueChangeListener(valueChangeEvent -> {
            String value = getValue();
            if (StringUtils.isNotBlank(value)) {
                final S searchCriteria = fillUpSearchCriteria(value);
                notifySearchHandler(searchCriteria);
            }
        });
        ResetButtonForTextField resetBtn = ResetButtonForTextField.extend(this);
        resetBtn.addResetButtonClickedListener(() -> {
            final S searchCriteria = fillUpSearchCriteria(null);
            notifySearchHandler(searchCriteria);
        });
    }

    @Override
    public void addSearchHandler(SearchHandler<S> handler) {
        if (searchHandlers == null) {
            searchHandlers = new ArrayList<>();
        }
        searchHandlers.add(handler);
    }

    @Override
    public void notifySearchHandler(final S criteria) {
        if (searchHandlers != null) {
            for (SearchHandler<S> handler : searchHandlers) {
                handler.onSearch(criteria);
            }
        }
    }

    abstract protected S fillUpSearchCriteria(String value);

    @Override
    public void setTotalCountNumber(Integer totalCountNumber) {

    }
}
