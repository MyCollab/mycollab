package com.mycollab.aspect

import java.util.HashMap

/**
 * @author MyCollab Ltd.
 * @since 5.0.1
 */
object ClassInfoMap {
    private val mapWrapper = mutableMapOf<Class<*>, ClassInfo>()

    @JvmStatic fun put(cls: Class<*>, classInfo: ClassInfo) {
        mapWrapper.put(cls, classInfo)
    }

    @JvmStatic fun getClassInfo(cls: Class<*>): ClassInfo? {
        return mapWrapper[cls]
    }

    @JvmStatic fun getModule(cls: Class<*>): String {
        return mapWrapper[cls]!!.module!!
    }

    @JvmStatic fun getType(cls: Class<*>): String {
        return mapWrapper[cls]!!.type!!
    }
}
