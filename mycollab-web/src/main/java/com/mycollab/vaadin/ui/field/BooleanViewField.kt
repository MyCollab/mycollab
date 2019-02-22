package com.mycollab.vaadin.ui.field

import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.vaadin.UserUIContext
import com.mycollab.vaadin.ui.ELabel
import com.vaadin.ui.Component
import com.vaadin.ui.CustomField

/**
 * @author MyCollab Ltd
 * @since 7.0.0
 */
class BooleanViewField : CustomField<Boolean>() {

    private val label = ELabel()

    override fun initContent(): Component? = label

    override fun doSetValue(value: Boolean?) {
        label.value = if (value == true) UserUIContext.getMessage(GenericI18Enum.ACTION_YES) else UserUIContext.getMessage(GenericI18Enum.ACTION_NO)
    }

    override fun getValue(): Boolean? = null
}
