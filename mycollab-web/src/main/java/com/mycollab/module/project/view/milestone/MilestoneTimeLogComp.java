package com.mycollab.module.project.view.milestone;

import com.mycollab.module.project.domain.SimpleMilestone;
import com.mycollab.module.project.service.ItemTimeLoggingService;
import com.mycollab.module.project.ui.components.TimeLogComp;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;

/**
 * @author MyCollab Ltd
 * @since 5.0.5
 */
public class MilestoneTimeLogComp extends TimeLogComp<SimpleMilestone> {
    private ItemTimeLoggingService itemLogService = AppContextUtil.getSpringBean(ItemTimeLoggingService.class);

    @Override
    protected Double getTotalBillableHours(SimpleMilestone bean) {
        return itemLogService.getTotalBillableHoursByMilestone(bean.getId(), AppUI.getAccountId());
    }

    @Override
    protected Double getTotalNonBillableHours(SimpleMilestone bean) {
        return itemLogService.getTotalNonBillableHoursByMilestone(bean.getId(), AppUI.getAccountId());
    }

    @Override
    protected Double getRemainedHours(SimpleMilestone bean) {
        return itemLogService.getRemainHoursByMilestone(bean.getId(), AppUI.getAccountId());
    }

    @Override
    protected boolean hasEditPermission() {
        return false;
    }

    @Override
    protected void showEditTimeWindow(SimpleMilestone bean) {

    }
}
