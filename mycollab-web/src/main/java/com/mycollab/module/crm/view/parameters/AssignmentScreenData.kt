package com.mycollab.module.crm.view.parameters

import com.mycollab.module.crm.domain.CrmTask
import com.mycollab.vaadin.mvp.ScreenData

object AssignmentScreenData {
    class Add(task: CrmTask) : ScreenData<CrmTask>(task)

    class Edit(task: CrmTask) : ScreenData<CrmTask>(task)

    class Read(params: Int?) : ScreenData<Int>(params)
}
