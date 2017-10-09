package com.mycollab.module.project.esb

import com.google.common.eventbus.AllowConcurrentEvents
import com.google.common.eventbus.Subscribe
import com.mycollab.common.dao.OptionValMapper
import com.mycollab.common.domain.OptionVal
import com.mycollab.common.domain.OptionValExample
import com.mycollab.module.esb.GenericCommand
import org.springframework.stereotype.Component
import java.util.*

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
@Component
class AddProjectCommand(private val optionValMapper: OptionValMapper) : GenericCommand() {

    @AllowConcurrentEvents
    @Subscribe
    fun addProject(event: AddProjectEvent) {
        val ex = OptionValExample()
        ex.createCriteria().andIsdefaultEqualTo(true).andSaccountidEqualTo(event.accountId)

        val defaultOptions = optionValMapper.selectByExample(ex)
        defaultOptions.forEach { option ->
            val prjOption = OptionVal()
            prjOption.createdtime = GregorianCalendar().time
            prjOption.description = option.description
            prjOption.extraid = event.projectId
            prjOption.isdefault = false
            prjOption.saccountid = event.accountId
            prjOption.type = option.type
            prjOption.typeval = option.typeval
            prjOption.fieldgroup = option.fieldgroup
            prjOption.refoption = option.id
            prjOption.color = "fdde86"
            optionValMapper.insert(prjOption)
        }
    }
}