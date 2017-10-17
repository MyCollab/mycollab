package com.mycollab.form.service.impl

import com.mycollab.form.service.MasterFormService
import com.mycollab.form.view.builder.type.DynaForm
import org.springframework.stereotype.Service

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
@Service
class MasterFormServiceImpl : MasterFormService {

    override fun findCustomForm(sAccountId: Int, moduleName: String): DynaForm? = null

    override fun saveCustomForm(sAccountId: Int, moduleName: String, form: DynaForm) {
    }
}