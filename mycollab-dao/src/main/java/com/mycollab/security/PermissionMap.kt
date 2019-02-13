/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.security

import com.fasterxml.jackson.annotation.JsonProperty
import com.mycollab.core.arguments.ValuedBean
import com.mycollab.core.utils.JsonDeSerializer
import com.mycollab.core.utils.StringUtils
import org.slf4j.LoggerFactory

/**
 * Map contains all permissions in MyCollab, it is used to all permissions if
 * logged in user.
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
class PermissionMap : ValuedBean() {

    @JsonProperty("perMap")
    private val perMap = mutableMapOf<String, Int>()

    /**
     * @param permissionItem
     * @param value
     */
    fun addPath(permissionItem: String, value: Int) {
        perMap[permissionItem] = value
    }

    /**
     * @param permissionItem
     * @return
     */
    fun getPermissionFlag(permissionItem: String): Int? {
        val value = perMap[permissionItem]
        return value ?: AccessPermissionFlag.NO_ACCESS
    }

    /**
     * @param permissionItem
     * @return
     */
    operator fun get(permissionItem: String): Int? = perMap[permissionItem]

    /**
     * @param permissionItem
     * @return
     */
    fun canBeYes(permissionItem: String): Boolean {
        val value = perMap[permissionItem]
        return value != null && BooleanPermissionFlag.beTrue(value as Int?)
    }

    /**
     * @param permissionItem
     * @return
     */
    fun canRead(permissionItem: String): Boolean {
        val value = perMap[permissionItem]
        return value != null && AccessPermissionFlag.canRead((value as Int?)!!)
    }

    /**
     * @param permissionItem
     * @return
     */
    fun canWrite(permissionItem: String): Boolean {
        val value = perMap[permissionItem]
        return value != null && AccessPermissionFlag.canWrite((value as Int?)!!)
    }

    /**
     * @param permissionItem
     * @return
     */
    fun canAccess(permissionItem: String): Boolean {
        val value = perMap[permissionItem]
        return value != null && AccessPermissionFlag.canAccess((value as Int?)!!)
    }

    /**
     * @return
     */
    fun toJsonString(): String = JsonDeSerializer.toJson(this)

    companion object {
        private val serialVersionUID = 1L

        private val LOG = LoggerFactory.getLogger(PermissionMap::class.java)

        @JvmField
        val ADMIN_ROLE_MAP = buildAdminPermissionCollection()

        @JvmField
        val EMPLOYEE_ROLE_MAP = buildEmployeePermissionCollection()

        @JvmField
        val GUESS_ROLE_MAP = buildGuestPermissionCollection()


        /**
         * @param json
         * @return
         */
        @JvmStatic
        fun fromJsonString(json: String?): PermissionMap {
            return if (StringUtils.isBlank(json)) PermissionMap()
            else try {
                JsonDeSerializer.fromJson(json, PermissionMap::class.java)
            } catch (e: Exception) {
                LOG.error("Error to get permission", e)
                PermissionMap()
            }
        }

        /**
         * @return
         */
        private fun buildAdminPermissionCollection(): PermissionMap {
            val permissionMap = PermissionMap()

            RolePermissionCollections.ACCOUNT_PERMISSION_ARR.forEach { (key) ->
                when (key) {
                    RolePermissionCollections.ACCOUNT_BILLING, RolePermissionCollections.ACCOUNT_THEME -> permissionMap.addPath(key, BooleanPermissionFlag.TRUE)
                    else -> permissionMap.addPath(key, AccessPermissionFlag.ACCESS)
                }
            }

            RolePermissionCollections.PROJECT_PERMISSION_ARR.forEach { (key) -> permissionMap.addPath(key, BooleanPermissionFlag.TRUE) }
            return permissionMap
        }

        /**
         * @return
         */
        private fun buildEmployeePermissionCollection(): PermissionMap {
            val permissionMap = PermissionMap()

            RolePermissionCollections.ACCOUNT_PERMISSION_ARR.forEach {
                when {
                    it.key == RolePermissionCollections.ACCOUNT_BILLING || it.key == RolePermissionCollections.ACCOUNT_THEME -> permissionMap.addPath(it.key, BooleanPermissionFlag.FALSE)
                    else -> permissionMap.addPath(it.key, AccessPermissionFlag.READ_ONLY)
                }
            }

            RolePermissionCollections.PROJECT_PERMISSION_ARR.forEach {
                when {
                    it.key == RolePermissionCollections.CREATE_NEW_PROJECT -> permissionMap.addPath(it.key, BooleanPermissionFlag.TRUE)
                    else -> permissionMap.addPath(it.key, BooleanPermissionFlag.FALSE)
                }
            }
            return permissionMap
        }

        /**
         * @return
         */
        private fun buildGuestPermissionCollection(): PermissionMap {
            val permissionMap = PermissionMap()

            RolePermissionCollections.ACCOUNT_PERMISSION_ARR.forEach {
                when {
                    it.key == RolePermissionCollections.ACCOUNT_BILLING || it.key == RolePermissionCollections.ACCOUNT_THEME -> permissionMap.addPath(it.key, BooleanPermissionFlag.FALSE)
                    else -> permissionMap.addPath(it.key, AccessPermissionFlag.NO_ACCESS)
                }
            }

            RolePermissionCollections.PROJECT_PERMISSION_ARR.forEach { permissionMap.addPath(it.key, BooleanPermissionFlag.FALSE) }
            return permissionMap
        }
    }
}
