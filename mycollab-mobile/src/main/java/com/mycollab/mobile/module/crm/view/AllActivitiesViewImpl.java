package com.mycollab.mobile.module.crm.view;

import com.mycollab.common.domain.SimpleActivityStream;
import com.mycollab.common.domain.criteria.ActivityStreamSearchCriteria;
import com.mycollab.mobile.module.crm.ui.AbstractListPageView;
import com.mycollab.mobile.ui.AbstractPagedBeanList;
import com.mycollab.mobile.ui.IListView;
import com.mycollab.mobile.ui.SearchInputField;
import com.mycollab.vaadin.mvp.ViewComponent;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
@ViewComponent
public class AllActivitiesViewImpl extends AbstractListPageView<ActivityStreamSearchCriteria, SimpleActivityStream> implements IListView<ActivityStreamSearchCriteria, SimpleActivityStream> {
    private static final long serialVersionUID = 5251742381187041492L;

    public AllActivitiesViewImpl() {
        setCaption("Activities");
    }

    @Override
    protected AbstractPagedBeanList<ActivityStreamSearchCriteria, SimpleActivityStream> createBeanList() {
        return new ActivitiesStreamListDisplay();
    }

    @Override
    protected void onBecomingVisible() {
        super.onBecomingVisible();

    }

    @Override
    protected SearchInputField<ActivityStreamSearchCriteria> createSearchField() {
        return null;
    }
}
