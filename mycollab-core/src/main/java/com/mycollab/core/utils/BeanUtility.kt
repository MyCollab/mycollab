package com.mycollab.core.utils

import com.rits.cloning.Cloner
import org.apache.commons.lang.builder.ToStringBuilder
import org.apache.commons.lang.builder.ToStringStyle

/**
 * Utility class to print bean properties. This class is used for debug purpose
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
object BeanUtility {

    @JvmStatic fun printBeanObj(bean: Any): String {
        return ToStringBuilder.reflectionToString(bean, ToStringStyle.SHORT_PREFIX_STYLE)
    }

    @JvmStatic fun <B> deepClone(b: B): B {
        return Cloner().deepClone(b)
    }
}
