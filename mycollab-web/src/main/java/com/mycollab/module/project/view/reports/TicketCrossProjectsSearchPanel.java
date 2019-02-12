package com.mycollab.module.project.view.reports;

import com.google.common.collect.Sets;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.SearchField;
import com.mycollab.db.query.ConstantValueInjector;
import com.mycollab.db.query.Param;
import com.mycollab.db.query.SearchFieldInfo;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria;
import com.mycollab.module.project.i18n.ProjectReportI18nEnum;
import com.mycollab.module.project.ui.components.UserProjectListSelect;
import com.mycollab.shell.event.ShellEvent;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.*;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.ArrayList;
import java.util.List;

import static com.mycollab.common.i18n.QueryI18nEnum.CONTAINS;
import static com.mycollab.common.i18n.QueryI18nEnum.IN;

/**
 * @author MyCollab Ltd
 * @since 7.0.0
 */
public class TicketCrossProjectsSearchPanel extends DefaultGenericSearchPanel<ProjectTicketSearchCriteria> {
    private ProjectTicketSearchCriteria searchCriteria;

    private static Param[] paramFields = new Param[]{
            /*ProjectTicketSearchCriteria.p_projectIds,*/
            ProjectTicketSearchCriteria.p_name, ProjectTicketSearchCriteria.p_startDate,
            ProjectTicketSearchCriteria.p_endDate, ProjectTicketSearchCriteria.p_dueDate};

    protected ComponentContainer buildSearchTitle() {
        return new MHorizontalLayout(ELabel.html(VaadinIcons.CALENDAR_CLOCK.getHtml() + " " + UserUIContext.getMessage(ProjectReportI18nEnum.REPORT_USERS_WORKLOAD))
                .withStyleName(ValoTheme.LABEL_H2, ValoTheme.LABEL_NO_MARGIN));
    }

    @Override
    protected SearchLayout<ProjectTicketSearchCriteria> createBasicSearchLayout() {
        return new TicketBasicSearchLayout();
    }

    @Override
    protected SearchLayout<ProjectTicketSearchCriteria> createAdvancedSearchLayout() {
        return new TicketAdvancedSearchLayout();
    }

    private class TicketBasicSearchLayout extends BasicSearchLayout<ProjectTicketSearchCriteria> {
        private static final long serialVersionUID = 1L;
        private TextField nameField;
        private CheckBox myItemCheckbox;

        private TicketBasicSearchLayout() {
            super(TicketCrossProjectsSearchPanel.this);
        }

        public void setNameField(String value) {
            nameField.setValue(value);
        }

        @Override
        public ComponentContainer constructBody() {
            MHorizontalLayout basicSearchBody = new MHorizontalLayout().withMargin(true);

            Label nameLbl = new Label(UserUIContext.getMessage(GenericI18Enum.FORM_NAME) + ":");
            basicSearchBody.with(nameLbl).withAlign(nameLbl, Alignment.MIDDLE_LEFT);

            nameField = new MTextField().withPlaceholder(UserUIContext.getMessage(GenericI18Enum.ACTION_QUERY_BY_TEXT))
                    .withWidth(WebUIConstants.DEFAULT_CONTROL_WIDTH);
            basicSearchBody.with(nameField).withAlign(nameField, Alignment.MIDDLE_CENTER);

            myItemCheckbox = new CheckBox(UserUIContext.getMessage(GenericI18Enum.OPT_MY_ITEMS));
            basicSearchBody.with(myItemCheckbox).withAlign(myItemCheckbox, Alignment.MIDDLE_CENTER);

            MButton searchBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SEARCH), clickEvent -> callSearchAction())
                    .withIcon(VaadinIcons.SEARCH).withStyleName(WebThemes.BUTTON_ACTION)
                    .withClickShortcut(ShortcutAction.KeyCode.ENTER);
            basicSearchBody.with(searchBtn).withAlign(searchBtn, Alignment.MIDDLE_LEFT);

            MButton cancelBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CLEAR), clickEvent -> nameField.setValue(""))
                    .withStyleName(WebThemes.BUTTON_OPTION);
            basicSearchBody.with(cancelBtn).withAlign(cancelBtn, Alignment.MIDDLE_CENTER);

            if (canSwitchToAdvanceLayout) {
                MButton advancedSearchBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_ADVANCED_SEARCH),
                        clickEvent -> moveToAdvancedSearchLayout()).withStyleName(WebThemes.BUTTON_LINK);
                basicSearchBody.with(advancedSearchBtn).withAlign(advancedSearchBtn, Alignment.MIDDLE_CENTER);
            }
            return basicSearchBody;
        }

        @Override
        protected ProjectTicketSearchCriteria fillUpSearchCriteria() {
            List<SearchFieldInfo<ProjectTicketSearchCriteria>> searchFieldInfos = new ArrayList<>();
            searchFieldInfos.add(new SearchFieldInfo(SearchField.AND, ProjectTicketSearchCriteria.p_name,
                    CONTAINS.name(), ConstantValueInjector.valueOf(nameField.getValue().trim())));
            if (myItemCheckbox.getValue()) {
                searchFieldInfos.add(new SearchFieldInfo(SearchField.AND, ProjectTicketSearchCriteria.p_assignee,
                        IN.name(), ConstantValueInjector.valueOf(Sets.newHashSet(UserUIContext.getUsername()))));
            }
            EventBusFactory.getInstance().post(new ShellEvent.AddQueryParam(this, searchFieldInfos));
            searchCriteria = SearchFieldInfo.buildSearchCriteria(ProjectTicketSearchCriteria.class, searchFieldInfos);

            return searchCriteria;
        }
    }

    private class TicketAdvancedSearchLayout extends DynamicQueryParamLayout<ProjectTicketSearchCriteria> {
        private static final long serialVersionUID = 1L;

        private TicketAdvancedSearchLayout() {
            super(TicketCrossProjectsSearchPanel.this, ProjectTypeConstants.TICKET);
        }

        @Override
        protected Class<ProjectTicketSearchCriteria> getType() {
            return ProjectTicketSearchCriteria.class;
        }

        @Override
        public Param[] getParamFields() {
            return paramFields;
        }

        @Override
        protected Component buildSelectionComp(String fieldId) {
            if ("projectid".equals(fieldId)) {
                return new UserProjectListSelect();
            }
            return null;
        }

        @Override
        protected ProjectTicketSearchCriteria fillUpSearchCriteria() {
            searchCriteria = super.fillUpSearchCriteria();
            return searchCriteria;
        }
    }
}
