package com.mycollab.mobile.module.project.view.message;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.mobile.ui.SearchInputView;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.domain.criteria.MessageSearchCriteria;
import com.mycollab.vaadin.UserUIContext;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.4.3
 */
public class MessageSearchInputView extends SearchInputView<MessageSearchCriteria> {
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
    protected MessageSearchCriteria buildSearchCriteria() {
        MessageSearchCriteria criteria = new MessageSearchCriteria();
        criteria.setProjectids(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
        criteria.setMessage(StringSearchField.and(nameField.getValue()));
        return criteria;
    }
}
