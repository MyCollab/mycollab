package com.mycollab.community.module.crm.view.setting;

import com.mycollab.form.view.builder.type.DynaSection;
import com.mycollab.module.crm.view.setting.ICrmCustomView;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.web.ui.NotPresentedView;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
@ViewComponent
public class CrmCustomViewImpl extends NotPresentedView implements ICrmCustomView {
    private static final long serialVersionUID = 1L;

    @Override
    public void display(String moduleName) {

    }

    @Override
    public void addActiveSection(DynaSection section) {

    }

    @Override
    public DynaSection[] getActiveSections() {
        return null;
    }

    @Override
    public String getCandidateTextFieldName() {
        return null;
    }

    @Override
    public void refreshSectionLayout(DynaSection section) {

    }
}
