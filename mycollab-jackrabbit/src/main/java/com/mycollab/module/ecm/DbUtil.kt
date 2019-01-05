package com.mycollab.module.ecm

object DbUtil {
    @JvmStatic
    fun getSchemaType(driverClassname: String): String = when(driverClassname) {
        "org.postgresql.Driver" -> "postgresql"
        else -> "mysql"
    }
}
