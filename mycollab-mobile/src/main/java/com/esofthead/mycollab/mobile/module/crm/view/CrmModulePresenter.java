package com.esofthead.mycollab.mobile.module.crm.view;

import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.mobile.module.crm.events.AccountEvent;
import com.esofthead.mycollab.mobile.module.crm.ui.CrmGenericPresenter;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.vaadin.ui.ComponentContainer;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.2
 * 
 */
public class CrmModulePresenter extends CrmGenericPresenter<CrmModule> {
	private static final long serialVersionUID = -3370467477599009160L;

	public CrmModulePresenter() {
		super(CrmModule.class);
	}

	@Override
	protected void onGo(ComponentContainer navigator, ScreenData<?> data) {
		super.onGo(navigator, data);
		if (data == null) {
			EventBus.getInstance().fireEvent(
					new AccountEvent.GotoList(navigator, null));
		}
	}

}
