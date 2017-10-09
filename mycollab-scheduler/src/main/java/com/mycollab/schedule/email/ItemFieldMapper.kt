package com.mycollab.schedule.email

import com.mycollab.schedule.email.format.DefaultFieldFormat
import com.mycollab.schedule.email.format.FieldFormat

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
open class ItemFieldMapper {
    private val fieldNameMap: MutableMap<String, FieldFormat> = mutableMapOf()

    fun put(fieldName: Enum<*>, displayName: Enum<*>) {
        fieldNameMap.put(fieldName.name, DefaultFieldFormat(fieldName.name, displayName))
    }

    fun put(fieldName: Enum<*>, displayName: Enum<*>, isColSpan: Boolean) {
        fieldNameMap.put(fieldName.name, DefaultFieldFormat(fieldName.name, displayName, isColSpan))
    }

    fun put(fieldName: Enum<*>, format: FieldFormat) {
        fieldNameMap.put(fieldName.name, format)
    }

    fun keySet(): Set<String> = fieldNameMap.keys

    fun hasField(fieldName: String): Boolean = fieldNameMap.contains(fieldName)

    fun getField(fieldName: String): FieldFormat? = fieldNameMap[fieldName]
}