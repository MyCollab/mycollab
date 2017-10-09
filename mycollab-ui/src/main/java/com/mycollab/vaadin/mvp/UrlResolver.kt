package com.mycollab.vaadin.mvp

import com.mycollab.core.MyCollabException
import com.mycollab.core.ResourceNotFoundException
import com.mycollab.vaadin.ui.NotificationUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
abstract class UrlResolver {
    companion object {
        val LOG: Logger = LoggerFactory.getLogger(UrlResolver::class.java)
    }

    private  var subResolvers: MutableMap<String, UrlResolver>? = null
    @JvmField protected var defaultUrlResolver: UrlResolver? = null

    /**
     * @param params
     */
    open protected fun handlePage(vararg params: String) {}

    abstract protected fun defaultPageErrorHandler()

    fun addSubResolver(key: String, subResolver: UrlResolver) {
        if (subResolvers == null) {
            subResolvers = mutableMapOf()
        }
        subResolvers?.put(key, subResolver)
    }

    fun getSubResolver(key: String): UrlResolver? = subResolvers?.get(key)

    open fun handle(vararg params: String) {
        try {
            if (params.isNotEmpty()) {
                var key = params[0]
                val index = key.indexOf('?')
                if (index > -1) {
                    key = key.substring(0, index)
                }
                if (subResolvers == null) {
                    handlePage(*params)
                } else {
                    val urlResolver = subResolvers?.get(key)
                    when (urlResolver) {
                        null -> defaultUrlResolver?.handle(*params) ?: throw ResourceNotFoundException("Can not register resolver key $key for Resolver: $this")
                        else -> urlResolver.handle(*params.drop(1).toTypedArray())

                    }
                }
            } else {
                defaultUrlResolver?.handle(*params) ?: handlePage()
            }
        } catch (e: Exception) {
            LOG.error("Error while navigation ${Arrays.toString(params)} for the resolver $this", e)
            defaultPageErrorHandler()
            NotificationUtil.showRecordNotExistNotification()
        }
    }
}