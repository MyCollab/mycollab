package com.mycollab.mobile.module.crm.view.lead;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.mobile.ui.SearchInputView;
import com.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.mycollab.vaadin.UserUIContext;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.4.9
 */
public class LeadSearchInputView extends SearchInputView<LeadSearchCriteria> {
    private MTextField nameField;

    @Override
    protected void onBecomingVisible() {
        super.onBecomingVisible();
        MVerticalLayout content = new MVerticalLayout();
        nameField = new MTextField().withFullWidth().withInputPrompt(UserUIContext.getMessage(GenericI18Enum.ACTION_QUERY_BY_TEXT));
        content.with(nameField);

        setContent(content);
    }

    @Override
    protected LeadSearchCriteria buildSearchCriteria() {
        LeadSearchCriteria criteria = new LeadSearchCriteria();
        criteria.setLeadName(StringSearchField.and(nameField.getValue()));
        return criteria;
    }
}
