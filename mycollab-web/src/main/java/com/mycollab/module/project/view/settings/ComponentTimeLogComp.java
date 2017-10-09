package com.mycollab.module.project.view.settings;

import com.mycollab.module.project.service.ItemTimeLoggingService;
import com.mycollab.module.project.ui.components.TimeLogComp;
import com.mycollab.module.tracker.domain.SimpleComponent;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;

/**
 * @author MyCollab Ltd
 * @since 5.0.5
 */
public class ComponentTimeLogComp extends TimeLogComp<SimpleComponent> {
    private ItemTimeLoggingService itemTimeLoggingService = AppContextUtil.getSpringBean(ItemTimeLoggingService.class);

    @Override
    protected Double getTotalBillableHours(SimpleComponent bean) {
        return itemTimeLoggingService.getTotalBillableHoursByComponent(bean.getId(), AppUI.getAccountId());
    }

    @Override
    protected Double getTotalNonBillableHours(SimpleComponent bean) {
        return itemTimeLoggingService.getTotalNonBillableHoursByComponent(bean.getId(), AppUI.getAccountId());
    }

    @Override
    protected Double getRemainedHours(SimpleComponent bean) {
        return itemTimeLoggingService.getRemainHoursByComponent(bean.getId(), AppUI.getAccountId());
    }

    @Override
    protected boolean hasEditPermission() {
        return false;
    }

    @Override
    protected void showEditTimeWindow(SimpleComponent bean) {

    }
}
