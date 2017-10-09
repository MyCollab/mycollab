package com.mycollab.mobile.module.crm.view.cases;

import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.mobile.module.crm.event.CaseEvent;
import com.mycollab.mobile.module.crm.ui.AbstractListPageView;
import com.mycollab.mobile.ui.AbstractPagedBeanList;
import com.mycollab.mobile.ui.SearchInputField;
import com.mycollab.mobile.ui.SearchInputView;
import com.mycollab.mobile.ui.SearchNavigationButton;
import com.mycollab.module.crm.domain.SimpleCase;
import com.mycollab.module.crm.domain.criteria.CaseSearchCriteria;
import com.mycollab.module.crm.i18n.CaseI18nEnum;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.UIConstants;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
@ViewComponent
public class CaseListViewImpl extends AbstractListPageView<CaseSearchCriteria, SimpleCase> implements CaseListView {
    private static final long serialVersionUID = -2790165346072368795L;

    public CaseListViewImpl() {
        setCaption(UserUIContext.getMessage(CaseI18nEnum.LIST));
    }

    @Override
    protected AbstractPagedBeanList<CaseSearchCriteria, SimpleCase> createBeanList() {
        return new CaseListDisplay();
    }

    @Override
    protected SearchInputField<CaseSearchCriteria> createSearchField() {
        return null;
    }

    @Override
    public void onBecomingVisible() {
        super.onBecomingVisible();
        AppUI.addFragment("crm/cases/list", UserUIContext.getMessage(CaseI18nEnum.LIST));
    }

    @Override
    protected Component buildRightComponent() {
        SearchNavigationButton searchBtn = new SearchNavigationButton() {
            @Override
            protected SearchInputView getSearchInputView() {
                return new CaseSearchInputView();
            }
        };
        MButton newCaseBtn = new MButton("", clickEvent -> EventBusFactory.getInstance().post(new CaseEvent.GotoAdd(this, null)))
                .withIcon(FontAwesome.PLUS).withStyleName(UIConstants.CIRCLE_BOX)
                .withVisible(UserUIContext.canWrite(RolePermissionCollections.CRM_CASE));
        return new MHorizontalLayout(searchBtn, newCaseBtn).alignAll(Alignment.TOP_RIGHT);
    }
}
