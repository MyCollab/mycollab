package com.mycollab.mobile.ui;

import com.mycollab.vaadin.ui.FieldSelection;

/**
 * @author MyCollab Ltd.
 * @since 4.3.1
 */
public abstract class AbstractSelectionView<B> extends AbstractMobilePageView {
    private static final long serialVersionUID = 1L;

    protected FieldSelection<B> selectionField;

    public FieldSelection<B> getSelectionField() {
        return selectionField;
    }

    public void setSelectionField(FieldSelection<B> selectionField) {
        this.selectionField = selectionField;
    }

    public abstract void load();
}
