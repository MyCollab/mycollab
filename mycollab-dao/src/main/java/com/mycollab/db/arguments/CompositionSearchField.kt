package com.mycollab.db.arguments

import java.util.ArrayList

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
class CompositionSearchField(oper: String) : SearchField() {

    private var fields: MutableList<SearchField>? = null

    init {
        this.operation = oper
    }

    fun getFields(): List<SearchField>? {
        return fields
    }

    fun setFields(fields: MutableList<SearchField>) {
        this.fields = fields
    }

    fun addField(field: SearchField) {
        if (fields == null) {
            fields = ArrayList()
        }

        fields!!.add(field)
    }

    companion object {
        private val serialVersionUID = 1L
    }
}
