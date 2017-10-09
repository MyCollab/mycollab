package com.mycollab.module.crm.view.activity;

import com.mycollab.module.crm.domain.CallWithBLOBs;
import com.mycollab.vaadin.event.HasEditFormHandlers;
import com.mycollab.vaadin.mvp.IFormAddView;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public interface CallAddView extends IFormAddView<CallWithBLOBs> {

    HasEditFormHandlers<CallWithBLOBs> getEditFormHandlers();
}
