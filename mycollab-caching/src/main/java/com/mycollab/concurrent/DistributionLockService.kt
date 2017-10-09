package com.mycollab.concurrent

import java.util.concurrent.locks.Lock

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
interface DistributionLockService {
    fun getLock(lockName: String): Lock?
}
