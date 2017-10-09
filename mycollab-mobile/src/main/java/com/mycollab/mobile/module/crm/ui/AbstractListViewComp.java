package com.mycollab.mobile.module.crm.ui;

import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.mobile.ui.AbstractMobileMenuPageView;
import com.mycollab.mobile.ui.AbstractPagedBeanList;
import com.mycollab.mobile.ui.IListView;
import com.vaadin.ui.Component;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
public abstract class AbstractListViewComp<S extends SearchCriteria, B> extends AbstractMobileMenuPageView implements IListView<S, B> {
    private static final long serialVersionUID = 3603608419228750094L;

    protected AbstractPagedBeanList<S, B> itemList;

    public AbstractListViewComp() {
        this.itemList = createBeanTable();
        setContent(itemList);

        Component rightComponent = createRightComponent();
        if (rightComponent != null) {
            setRightComponent(rightComponent);
        }
    }

    @Override
    public AbstractPagedBeanList<S, B> getPagedBeanTable() {
        return this.itemList;
    }

    @Override
    public void onBecomingVisible() {
        super.onBecomingVisible();

        if (getPagedBeanTable().getSearchRequest() != null)
            getPagedBeanTable().refresh();
    }

    @Override
    protected void buildNavigateMenu() {

    }

    abstract protected AbstractPagedBeanList<S, B> createBeanTable();

    abstract protected Component createRightComponent();
}
