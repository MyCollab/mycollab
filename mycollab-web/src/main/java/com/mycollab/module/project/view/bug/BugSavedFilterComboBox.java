package com.mycollab.module.project.view.bug;

import com.mycollab.db.arguments.SearchField;
import com.mycollab.db.query.*;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.i18n.BugI18nEnum;
import com.mycollab.module.project.i18n.OptionI18nEnum.BugStatus;
import com.mycollab.module.project.query.CurrentProjectIdInjector;
import com.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.web.ui.SavedFilterComboBox;
import org.joda.time.LocalDate;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static com.mycollab.common.i18n.QueryI18nEnum.StringI18nEnum;

/**
 * @author MyCollab Ltd
 * @since 5.2.1
 */
public class BugSavedFilterComboBox extends SavedFilterComboBox {
    public static final String ALL_BUGS = "ALL_BUGS";
    public static final String OPEN_BUGS = "OPEN_BUGS";
    public static final String OVERDUE_BUGS = "OVERDUE_BUGS";
    public static final String MY_BUGS = "MY_BUGS";
    public static final String BUGS_CREATED_BY_ME = "BUGS_CREATED_BY_ME";
    public static final String NEW_THIS_WEEK = "NEW_THIS_WEEK";
    public static final String UPDATE_THIS_WEEK = "UPDATE_THIS_WEEK";
    public static final String NEW_LAST_WEEK = "NEW_LAST_WEEK";
    public static final String UPDATE_LAST_WEEK = "UPDATE_LAST_WEEK";
    public static final String WAITING_FOR_APPROVAL = "WAITING_FOR_APPROVAL";

    public BugSavedFilterComboBox() {
        super(ProjectTypeConstants.BUG);

        SearchQueryInfo allBugsQuery = new SearchQueryInfo(ALL_BUGS, UserUIContext.getMessage(BugI18nEnum.VAL_ALL_BUGS),
                SearchFieldInfo.inCollection(BugSearchCriteria.p_projectIds, new CurrentProjectIdInjector()));

        SearchQueryInfo allOpenBugsQuery = new SearchQueryInfo(OPEN_BUGS, UserUIContext.getMessage(BugI18nEnum.VAL_ALL_OPEN_BUGS),
                SearchFieldInfo.inCollection(
                        BugSearchCriteria.p_status, ConstantValueInjector.valueOf(String.class,
                                Arrays.asList(BugStatus.Open.name(), BugStatus.ReOpen.name()))));

        SearchQueryInfo overdueTaskQuery = new SearchQueryInfo(OVERDUE_BUGS, UserUIContext.getMessage(BugI18nEnum.VAL_OVERDUE_BUGS),
                new SearchFieldInfo(SearchField.AND, BugSearchCriteria.p_duedate, DateParam.BEFORE,
                        new VariableInjector() {
                            @Override
                            public Object eval() {
                                return new LocalDate().toDate();
                            }

                            @Override
                            public Class getType() {
                                return Date.class;
                            }

                            @Override
                            public boolean isArray() {
                                return false;
                            }

                            @Override
                            public boolean isCollection() {
                                return false;
                            }
                        }),
                new SearchFieldInfo(SearchField.AND, new StringParam("id-status",
                        "m_tracker_bug", "status"), StringI18nEnum.IS_NOT.name(),
                        ConstantValueInjector.valueOf(BugStatus.Verified.name())));

        SearchQueryInfo myBugsQuery = new SearchQueryInfo(MY_BUGS, UserUIContext.getMessage(BugI18nEnum.VAL_MY_BUGS),
                SearchFieldInfo.inCollection(BugSearchCriteria.p_assignee, ConstantValueInjector.valueOf(String.class,
                        Collections.singletonList(UserUIContext.getUsername()))));

        SearchQueryInfo bugsCreatedByMeQuery = new SearchQueryInfo(MY_BUGS, UserUIContext.getMessage(BugI18nEnum.VAL_BUGS_CREATED_BY_ME),
                SearchFieldInfo.inCollection(BugSearchCriteria.p_createdUser, ConstantValueInjector.valueOf(String.class,
                        Collections.singletonList(UserUIContext.getUsername()))));

        SearchQueryInfo newBugsThisWeekQuery = new SearchQueryInfo(NEW_THIS_WEEK, UserUIContext.getMessage(BugI18nEnum.VAL_NEW_THIS_WEEK),
                SearchFieldInfo.inDateRange(BugSearchCriteria.p_createddate, VariableInjector.THIS_WEEK));

        SearchQueryInfo updateBugsThisWeekQuery = new SearchQueryInfo(UPDATE_THIS_WEEK, UserUIContext.getMessage(BugI18nEnum.VAL_UPDATE_THIS_WEEK),
                SearchFieldInfo.inDateRange(BugSearchCriteria.p_lastupdatedtime, VariableInjector.THIS_WEEK));

        SearchQueryInfo newBugsLastWeekQuery = new SearchQueryInfo(NEW_LAST_WEEK, UserUIContext.getMessage(BugI18nEnum.VAL_NEW_LAST_WEEK),
                SearchFieldInfo.inDateRange(BugSearchCriteria.p_createddate, VariableInjector.LAST_WEEK));

        SearchQueryInfo updateBugsLastWeekQuery = new SearchQueryInfo(UPDATE_LAST_WEEK, UserUIContext.getMessage(BugI18nEnum.VAL_UPDATE_LAST_WEEK),
                SearchFieldInfo.inDateRange(BugSearchCriteria.p_lastupdatedtime, VariableInjector.LAST_WEEK));

        SearchQueryInfo waitForApproveQuery = new SearchQueryInfo(WAITING_FOR_APPROVAL, UserUIContext.getMessage(BugI18nEnum.VAL_WAITING_APPROVAL),
                SearchFieldInfo.inCollection(BugSearchCriteria.p_status, ConstantValueInjector.valueOf(String.class,
                        Arrays.asList(BugStatus.Resolved.name()))));

        this.addSharedSearchQueryInfo(allBugsQuery);
        this.addSharedSearchQueryInfo(allOpenBugsQuery);
        this.addSharedSearchQueryInfo(overdueTaskQuery);
        this.addSharedSearchQueryInfo(myBugsQuery);
        this.addSharedSearchQueryInfo(bugsCreatedByMeQuery);
        this.addSharedSearchQueryInfo(newBugsThisWeekQuery);
        this.addSharedSearchQueryInfo(updateBugsThisWeekQuery);
        this.addSharedSearchQueryInfo(newBugsLastWeekQuery);
        this.addSharedSearchQueryInfo(updateBugsLastWeekQuery);
        this.addSharedSearchQueryInfo(waitForApproveQuery);
    }

    public void setTotalCountNumber(int countNumber) {
        componentsText.setReadOnly(false);
        componentsText.setValue(selectedQueryName + " (" + countNumber + ")");
        componentsText.setReadOnly(true);
    }
}
