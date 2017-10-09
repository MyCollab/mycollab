package com.mycollab.module.crm.view.setting;

import com.mycollab.form.view.builder.type.DynaSection;
import com.mycollab.vaadin.mvp.PageView;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface ICrmCustomView extends PageView {
    void display(String moduleName);

    void addActiveSection(DynaSection section);

    DynaSection[] getActiveSections();

    String getCandidateTextFieldName();

    void refreshSectionLayout(DynaSection section);
}
