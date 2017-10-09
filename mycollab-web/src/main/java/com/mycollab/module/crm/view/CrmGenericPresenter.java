package com.mycollab.module.crm.view;

import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.module.crm.event.CrmEvent;
import com.mycollab.vaadin.mvp.PageView;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class CrmGenericPresenter<V extends PageView> extends AbstractPresenter<V> {
    private static final long serialVersionUID = 1L;

    public CrmGenericPresenter(Class<V> viewClass) {
        super(viewClass);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        CrmModule crmModule = (CrmModule) container;
        crmModule.setContent(view);
    }

    @Override
    protected void onErrorStopChain(Throwable throwable) {
        super.onErrorStopChain(throwable);
        if (!(this instanceof CrmHomePresenter)) {
            EventBusFactory.getInstance().post(new CrmEvent.GotoHome(this, null));
        }
    }
}
