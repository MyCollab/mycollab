package com.mycollab.mobile.module.project.view.ticket;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.db.query.SearchFieldInfo;
import com.mycollab.db.query.SearchQueryInfo;
import com.mycollab.mobile.ui.SearchInputView;
import com.mycollab.mobile.ui.FormSectionBuilder;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria;
import com.mycollab.vaadin.UserUIContext;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.List;

import static com.mycollab.module.project.query.TicketQueryInfo.*;

/**
 * @author MyCollab Ltd
 * @since 5.4.3
 */
public class TicketSearchInputView extends SearchInputView<ProjectTicketSearchCriteria> {
    private MTextField nameField;
    private MVerticalLayout content;
    private ProjectTicketSearchCriteria criteria;

    @Override
    protected void onBecomingVisible() {
        super.onBecomingVisible();
        criteria = null;
        content = new MVerticalLayout().withMargin(false).withSpacing(false);
        nameField = new MTextField().withFullWidth().withInputPrompt(UserUIContext.getMessage(GenericI18Enum.ACTION_QUERY_BY_TEXT));
        content.with(nameField);

        content.with(FormSectionBuilder.build(UserUIContext.getMessage(GenericI18Enum.OPT_SHARED_TO_ME)).withStyleName("border-top"));
        addSharedSearchQueryInfo(allTasksQuery);
        addSharedSearchQueryInfo(allOpenTaskQuery);
        addSharedSearchQueryInfo(overdueTaskQuery);
        addSharedSearchQueryInfo(allClosedTaskQuery);
        addSharedSearchQueryInfo(myTasksQuery);
        addSharedSearchQueryInfo(tasksCreatedByMeQuery);
        addSharedSearchQueryInfo(newTasksThisWeekQuery);
        addSharedSearchQueryInfo(updateTasksThisWeekQuery);
        addSharedSearchQueryInfo(newTasksLastWeekQuery);
        addSharedSearchQueryInfo(updateTasksLastWeekQuery);
        setContent(content);
    }

    private void addSharedSearchQueryInfo(SearchQueryInfo<ProjectTicketSearchCriteria> queryInfo) {
        content.with(new MButton(queryInfo.getQueryName(), clickEvent -> {
            List<SearchFieldInfo<ProjectTicketSearchCriteria>> fieldInfos = queryInfo.getSearchFieldInfos();
            criteria = SearchFieldInfo.buildSearchCriteria(ProjectTicketSearchCriteria.class, fieldInfos);
            criteria.setTypes(CurrentProjectVariables.getRestrictedTicketTypes());
            criteria.setProjectIds(new SetSearchField(CurrentProjectVariables.getProjectId()));
            getNavigationManager().navigateBack();
        }));
    }

    @Override
    protected ProjectTicketSearchCriteria buildSearchCriteria() {
        if (criteria == null) {
            criteria = new ProjectTicketSearchCriteria();
            criteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
            criteria.setTypes(CurrentProjectVariables.getRestrictedTicketTypes());
            criteria.setName(StringSearchField.and(nameField.getValue()));
        }

        return criteria;
    }
}
