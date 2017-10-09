package com.mycollab.module.esb

import com.google.common.eventbus.AsyncEventBus
import org.springframework.beans.factory.annotation.Autowired
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
open class GenericCommand {
    @Autowired protected lateinit var asyncEventBus: AsyncEventBus

    @PostConstruct
    fun registerHandler() {
        asyncEventBus.register(this)
    }

    @PreDestroy
    fun unregisterHandler() {
        asyncEventBus.unregister(this)
    }
}