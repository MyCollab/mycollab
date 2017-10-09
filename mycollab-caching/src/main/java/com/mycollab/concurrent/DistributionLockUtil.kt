package com.mycollab.concurrent

import com.mycollab.spring.AppContextUtil
import org.apache.commons.collections.map.AbstractReferenceMap
import org.apache.commons.collections.map.ReferenceMap
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.util.Collections
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
object DistributionLockUtil {
    private val LOG = LoggerFactory.getLogger(DistributionLockUtil::class.java)

    private val map = Collections.synchronizedMap(ReferenceMap(AbstractReferenceMap.WEAK, AbstractReferenceMap.WEAK))

    @JvmStatic fun getLock(lockName: String): Lock {
        return try {
            val lockService = AppContextUtil.getSpringBean(DistributionLockService::class.java)
            val lock = lockService.getLock(lockName)
            lock ?: getStaticDefaultLock(lockName)
        } catch (e: Exception) {
            LOG.warn("Can not get lock service", e)
            getStaticDefaultLock(lockName)
        }

    }

    @JvmStatic fun removeLock(lockName: String) {
        map.remove(lockName)
    }

    private fun getStaticDefaultLock(lockName: String): Lock {
        synchronized(map) {
            var lock: Lock? = map[lockName] as? Lock
            when (lock) {
                null -> {
                    lock = ReentrantLock()
                    map.put(lockName, lock)
                }
            }
            return lock!!
        }
    }
}
