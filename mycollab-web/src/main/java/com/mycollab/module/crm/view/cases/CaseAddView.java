package com.mycollab.module.crm.view.cases;

import com.mycollab.module.crm.domain.SimpleCase;
import com.mycollab.vaadin.event.HasEditFormHandlers;
import com.mycollab.vaadin.mvp.IFormAddView;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public interface CaseAddView extends IFormAddView<SimpleCase> {
    HasEditFormHandlers<SimpleCase> getEditFormHandlers();
}
