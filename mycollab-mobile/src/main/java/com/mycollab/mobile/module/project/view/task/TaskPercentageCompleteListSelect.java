package com.mycollab.mobile.module.project.view.task;

import com.mycollab.mobile.ui.ValueListSelect;

/**
 * @author MyCollab Ltd.
 * @since 4.5.0
 */
public class TaskPercentageCompleteListSelect extends ValueListSelect {
    private static final long serialVersionUID = 1L;

    public TaskPercentageCompleteListSelect() {
        super(false, 0d, 10d, 20d, 30d, 40d, 50d, 60d, 70d, 80d, 90d, 100d);
    }
}
