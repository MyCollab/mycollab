package com.mycollab.mobile.ui;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.MyCollabException;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.FieldSelection;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;

/**
 * @author MyCollab Ltd.
 * @since 4.3.1
 */
public abstract class AbstractSelectionCustomField<T, B> extends CustomField<T> implements FieldSelection<B> {
    private static final long serialVersionUID = 1L;

    protected NavigationButton navButton;
    protected B beanItem;

    public AbstractSelectionCustomField(Class<? extends AbstractSelectionView<B>> targetSelectionView) {
        try {
            final AbstractSelectionView<B> selectionView = targetSelectionView.newInstance();
            selectionView.setSelectionField(this);
            navButton = new NavigationButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SELECT), selectionView);
            navButton.setTargetView(selectionView);
            navButton.setWidth("100%");
            navButton.addClickListener(navigationButtonClickEvent -> selectionView.load());
        } catch (SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException e) {
            throw new MyCollabException(e);
        }
    }

    @Override
    protected Component initContent() {
        return navButton;
    }
}
