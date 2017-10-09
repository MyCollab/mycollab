package com.mycollab.db.persistence

import org.apache.ibatis.annotations.Param

import java.io.Serializable

/**
 * @param <K>
 * @param <T>
 * @author MyCollab Ltd.
 * @since 1.0
</T></K> */
interface ICrudGenericDAO<K : Serializable, T> {

    /**
     * @param record
     */
    fun insert(record: T)

    /**
     * @param record
     * @return
     */
    fun updateByPrimaryKey(record: T): Int

    /**
     * @param record
     * @return
     */
    fun updateByPrimaryKeySelective(record: T): Int

    /**
     * @param record
     * @param primaryKeys
     */
    fun massUpdateWithSession(@Param("record") record: T, @Param("primaryKeys") primaryKeys: List<K>)

    /**
     * @param primaryKey
     * @return
     */
    fun selectByPrimaryKey(primaryKey: K): T

    /**
     * @param primaryKey
     * @return
     */
    fun deleteByPrimaryKey(primaryKey: K): Int

    /**
     * @param value
     * @return
     */
    fun insertAndReturnKey(value: T): Int

    /**
     * @param keys
     */
    fun removeKeysWithSession(keys: List<*>)

}
