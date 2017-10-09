package com.mycollab.mobile.module.crm.view.campaign;

import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.mobile.module.crm.event.CampaignEvent;
import com.mycollab.mobile.module.crm.ui.AbstractListPageView;
import com.mycollab.mobile.ui.*;
import com.mycollab.module.crm.domain.SimpleCampaign;
import com.mycollab.module.crm.domain.criteria.CampaignSearchCriteria;
import com.mycollab.module.crm.i18n.CampaignI18nEnum;
import com.mycollab.module.crm.i18n.LeadI18nEnum;
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
public class CampaignListViewImpl extends AbstractListPageView<CampaignSearchCriteria, SimpleCampaign> implements IListView<CampaignSearchCriteria, SimpleCampaign> {
    private static final long serialVersionUID = -8743010493576179868L;

    public CampaignListViewImpl() {
        setCaption(UserUIContext.getMessage(CampaignI18nEnum.LIST));
    }

    @Override
    protected AbstractPagedBeanList<CampaignSearchCriteria, SimpleCampaign> createBeanList() {
        return new CampaignListDisplay();
    }

    @Override
    protected SearchInputField<CampaignSearchCriteria> createSearchField() {
        return null;
    }

    @Override
    public void onBecomingVisible() {
        super.onBecomingVisible();
        AppUI.addFragment("crm/campaign/list", UserUIContext.getMessage(LeadI18nEnum.LIST));
    }

    @Override
    protected Component buildRightComponent() {
        SearchNavigationButton searchBtn = new SearchNavigationButton() {
            @Override
            protected SearchInputView getSearchInputView() {
                return new CampaignSearchInputView();
            }
        };
        MButton newCampaignBtn = new MButton("", clickEvent -> EventBusFactory.getInstance().post(new CampaignEvent.GotoAdd(this, null)))
                .withIcon(FontAwesome.PLUS).withStyleName(UIConstants.CIRCLE_BOX)
                .withVisible(UserUIContext.canWrite(RolePermissionCollections.CRM_CAMPAIGN));
        return new MHorizontalLayout(searchBtn, newCampaignBtn).alignAll(Alignment.TOP_RIGHT);
    }

}
