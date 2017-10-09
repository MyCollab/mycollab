package com.mycollab.mobile.ui;

import com.mycollab.vaadin.ui.UIConstants;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.server.FontAwesome;

/**
 * @author MyCollab Ltd
 * @since 5.4.3
 */
public abstract class SearchNavigationButton extends NavigationButton {
    public SearchNavigationButton() {
        setIcon(FontAwesome.SEARCH);
        addStyleName(UIConstants.CIRCLE_BOX);
        this.addClickListener(navigationButtonClickEvent -> getNavigationManager().navigateTo(getSearchInputView()));
    }

    abstract protected SearchInputView getSearchInputView();
}
