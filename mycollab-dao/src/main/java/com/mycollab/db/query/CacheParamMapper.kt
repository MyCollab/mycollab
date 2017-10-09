package com.mycollab.db.query

/**
 * @author MyCollab Ltd
 * @since 5.3.1
 */
object CacheParamMapper {
    private val map = mutableMapOf<String, MutableMap<String, ValueParam>>()

    @JvmStatic
    fun <P : Param> register(type: String, displayName: Enum<*>?, param: P): P {
        var valueParamMap: MutableMap<String, ValueParam>? = map[type]
        if (valueParamMap == null) {
            valueParamMap = mutableMapOf<String, ValueParam>()
            map.put(type, valueParamMap)
        }
        valueParamMap.put(param.id, ValueParam(displayName, param))
        return param
    }

    @JvmStatic
    fun getValueParam(type: String, id: String): ValueParam? {
        val valueParamMap = map[type]
        return if (valueParamMap != null) valueParamMap[id] else null
    }

    class ValueParam internal constructor(val displayName: Enum<*>?, val param: Param)
}
