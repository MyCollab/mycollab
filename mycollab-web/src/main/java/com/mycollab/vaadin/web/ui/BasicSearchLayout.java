package com.mycollab.vaadin.web.ui;

import com.mycollab.db.arguments.SearchCriteria;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;

/**
 * @author MyCollab Ltd
 * @since 5.4.3
 */
abstract public class BasicSearchLayout<S extends SearchCriteria> extends SearchLayout<S> {
    private static final long serialVersionUID = 1L;
    protected ComponentContainer header;
    protected ComponentContainer body;

    public BasicSearchLayout(DefaultGenericSearchPanel<S> parent) {
        super(parent, "basicSearch");
        this.initLayout();
    }

    private void initLayout() {
        header = this.constructHeader();
        body = this.constructBody();
        if (header != null) {
            this.addComponent(header, "basicSearchHeader");
        }

        this.addComponent(body, "basicSearchBody");
    }

    @Override
    protected void addHeaderRight(Component c) {
        if (header == null)
            return;

        header.addComponent(c);
    }

    private ComponentContainer constructHeader() {
        return ((DefaultGenericSearchPanel)searchPanel).constructHeader();
    }

    abstract public ComponentContainer constructBody();
}
