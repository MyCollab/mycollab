package com.mycollab.module.crm.view.activity;

import com.mycollab.common.TableViewField;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.crm.domain.SimpleActivity;
import com.mycollab.module.crm.domain.criteria.ActivitySearchCriteria;
import com.mycollab.module.crm.i18n.ActivityI18nEnum;
import com.mycollab.module.crm.i18n.CallI18nEnum;
import com.mycollab.module.crm.i18n.MeetingI18nEnum;
import com.mycollab.module.crm.i18n.TaskI18nEnum;
import com.mycollab.module.crm.ui.components.RelatedListComp;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.mycollab.vaadin.web.ui.WebUIConstants;
import com.vaadin.server.FontAwesome;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class ActivityRelatedItemListComp extends RelatedListComp<SimpleActivity, ActivitySearchCriteria> {
    private static final long serialVersionUID = 1L;

    private final boolean allowCreateNew;

    public ActivityRelatedItemListComp(final boolean allowCreateNew) {
        this.allowCreateNew = allowCreateNew;
        initUI();
    }

    private void initUI() {
        if (allowCreateNew) {
            final MButton newTaskBtn = new MButton(UserUIContext.getMessage(TaskI18nEnum.NEW), clickEvent -> fireNewRelatedItem("task"))
                    .withIcon(FontAwesome.PLUS).withStyleName(WebThemes.BUTTON_ACTION)
                    .withVisible(UserUIContext.canWrite(RolePermissionCollections.CRM_TASK));

            final MButton newCallBtn = new MButton(UserUIContext.getMessage(CallI18nEnum.NEW), clickEvent -> fireNewRelatedItem("call"))
                    .withIcon(FontAwesome.PLUS).withStyleName(WebThemes.BUTTON_ACTION)
                    .withVisible(UserUIContext.canWrite(RolePermissionCollections.CRM_CALL));

            final MButton newMeetingBtn = new MButton(UserUIContext.getMessage(MeetingI18nEnum.NEW), clickEvent -> fireNewRelatedItem("meeting"))
                    .withIcon(FontAwesome.PLUS).withStyleName(WebThemes.BUTTON_ACTION)
                    .withVisible(UserUIContext.canWrite(RolePermissionCollections.CRM_MEETING));

            this.addComponent(new MHorizontalLayout(newTaskBtn, newCallBtn, newMeetingBtn));
            this.addStyleName("activity-related-content");
        }

        tableItem = new ActivityTableDisplay(Arrays.asList(
                new TableViewField(ActivityI18nEnum.FORM_SUBJECT, "subject", WebUIConstants.TABLE_EX_LABEL_WIDTH),
                new TableViewField(GenericI18Enum.FORM_STATUS, "status", WebUIConstants.TABLE_S_LABEL_WIDTH),
                new TableViewField(GenericI18Enum.FORM_START_DATE, "startDate", WebUIConstants.TABLE_DATE_TIME_WIDTH),
                new TableViewField(GenericI18Enum.FORM_END_DATE, "endDate", WebUIConstants.TABLE_DATE_TIME_WIDTH)));

        this.addComponent(tableItem);
    }

    @Override
    public void refresh() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
