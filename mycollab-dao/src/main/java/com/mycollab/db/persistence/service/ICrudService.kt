package com.mycollab.db.persistence.service

import com.mycollab.core.cache.CacheEvict
import com.mycollab.core.cache.CacheKey
import com.mycollab.core.cache.Cacheable

import java.io.Serializable

/**
 * @param <K>
 * @param <T>
 * @author MyCollab Ltd.
 * @since 1.0
</T></K> */
interface ICrudService<K : Serializable, T> : IService {

    /**
     * @param record
     * @param username
     * @return
     */
    @CacheEvict
    fun saveWithSession(@CacheKey record: T, username: String?): Int

    /**
     * @param record
     * @param username
     * @return
     */
    @CacheEvict
    fun updateWithSession(@CacheKey record: T, username: String?): Int

    /**
     * @param record
     * @param username
     * @return
     */
    @CacheEvict
    fun updateSelectiveWithSession(@CacheKey record: T, username: String?): Int?

    /**
     * @param record
     * @param primaryKeys
     * @param accountId
     */
    @CacheEvict
    fun massUpdateWithSession(record: T, primaryKeys: List<K>, @CacheKey accountId: Int?)

    /**
     * @param primaryKey
     * @param sAccountId
     * @return
     */
    @Cacheable
    fun findByPrimaryKey(primaryKey: K, @CacheKey sAccountId: Int): T?

    /**
     * @param item
     * @param username
     * @param sAccountId
     */
    @CacheEvict
    fun removeWithSession(item: T, username: String?, @CacheKey sAccountId: Int)

    /**
     * @param items
     * @param username
     * @param sAccountId
     */
    @CacheEvict
    fun massRemoveWithSession(items: List<T>, username: String?, @CacheKey sAccountId: Int)
}
