package com.mycollab.module.crm.view.parameters

import com.mycollab.module.crm.domain.CallWithBLOBs
import com.mycollab.vaadin.mvp.ScreenData

object CallScreenData {
    class Add(call: CallWithBLOBs) : ScreenData<CallWithBLOBs>(call)

    class Edit(call: CallWithBLOBs) : ScreenData<CallWithBLOBs>(call)

    class Read(params: Int?) : ScreenData<Int>(params)
}
