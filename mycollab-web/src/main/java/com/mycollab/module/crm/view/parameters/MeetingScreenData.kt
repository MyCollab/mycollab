package com.mycollab.module.crm.view.parameters

import com.mycollab.module.crm.domain.MeetingWithBLOBs
import com.mycollab.vaadin.mvp.ScreenData

object MeetingScreenData {
    class Add(meeting: MeetingWithBLOBs) : ScreenData<MeetingWithBLOBs>(meeting)

    class Edit(meeting: MeetingWithBLOBs) : ScreenData<MeetingWithBLOBs>(meeting)

    class Read(params: Int?) : ScreenData<Int>(params)
}
