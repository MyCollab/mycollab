package com.mycollab.html

import com.hp.gagawa.java.elements.Div
import com.hp.gagawa.java.elements.Text

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class DivLessFormatter() : Div() {
    override fun write(): String {
        val b = StringBuilder()
        children.forEach { b.append(it.write()) }
        return b.toString()
    }

    companion object {
        @JvmField val EMPTY_SPACE = Text("&nbsp;")
    }
}