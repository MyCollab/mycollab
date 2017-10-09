package com.mycollab.db.query

import org.apache.commons.lang3.builder.EqualsBuilder

import java.io.Serializable

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
abstract class Param @JvmOverloads constructor(val id: String = "") : Serializable {

    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }
        if (other === this) {
            return true
        }
        if (other.javaClass != javaClass) {
            return false
        }
        val item = other as Param?
        return EqualsBuilder().append(id, item!!.id).build()!!
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}