package com.mycollab.module.crm.view.activity;

import com.mycollab.common.TableViewField;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.crm.domain.SimpleActivity;
import com.mycollab.module.crm.domain.criteria.ActivitySearchCriteria;
import com.mycollab.module.crm.i18n.ActivityI18nEnum;
import com.mycollab.module.crm.ui.components.AbstractListItemComp;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.DefaultMassItemActionHandlerContainer;
import com.mycollab.vaadin.web.ui.DefaultGenericSearchPanel;
import com.mycollab.vaadin.web.ui.WebUIConstants;
import com.mycollab.vaadin.web.ui.table.AbstractPagedBeanTable;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
@ViewComponent
public class ActivityListViewImpl extends AbstractListItemComp<ActivitySearchCriteria, SimpleActivity> implements ActivityListView {
    private static final long serialVersionUID = 1L;

    @Override
    protected void buildExtraControls() {
        // do nothing
    }

    @Override
    public void showNoItemView() {

    }

    @Override
    protected DefaultGenericSearchPanel<ActivitySearchCriteria> createSearchPanel() {
        return new ActivitySearchPanel();
    }

    @Override
    protected AbstractPagedBeanTable<ActivitySearchCriteria, SimpleActivity> createBeanTable() {
        return new ActivityTableDisplay(
                new TableViewField(null, "selected", WebUIConstants.TABLE_CONTROL_WIDTH),
                Arrays.asList(
                        new TableViewField(ActivityI18nEnum.FORM_SUBJECT, "subject", WebUIConstants.TABLE_EX_LABEL_WIDTH),
                        new TableViewField(GenericI18Enum.FORM_STATUS, "status", WebUIConstants.TABLE_S_LABEL_WIDTH),
                        new TableViewField(GenericI18Enum.FORM_START_DATE, "startDate", WebUIConstants.TABLE_DATE_TIME_WIDTH),
                        new TableViewField(GenericI18Enum.FORM_END_DATE, "endDate", WebUIConstants.TABLE_DATE_TIME_WIDTH)));
    }

    @Override
    protected DefaultMassItemActionHandlerContainer createActionControls() {
        DefaultMassItemActionHandlerContainer container = new DefaultMassItemActionHandlerContainer();
        if (UserUIContext.canAccess(RolePermissionCollections.CRM_CALL)
                || UserUIContext.canAccess(RolePermissionCollections.CRM_MEETING)
                || UserUIContext.canAccess(RolePermissionCollections.CRM_TASK)) {
            container.addDeleteActionItem();
        }

        container.addMailActionItem();
        container.addDownloadPdfActionItem();
        container.addDownloadExcelActionItem();
        container.addDownloadCsvActionItem();

        return container;
    }
}
