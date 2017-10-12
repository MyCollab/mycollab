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
import java.util.*

/**
 * Map contains all permissions in MyCollab, it is used to all permissions if
 * logged in user.
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
class PermissionMap : ValuedBean() {

    @JsonProperty("perMap")
    private val perMap = HashMap<String, Int>()

    /**
     * @param permissionItem
     * @param value
     */
    fun addPath(permissionItem: String, value: Int) {
        perMap.put(permissionItem, value)
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
    operator fun get(permissionItem: String): Int? {
        return perMap[permissionItem]
    }

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
    fun canBeFalse(permissionItem: String): Boolean {
        val value = perMap[permissionItem]
        return value != null && BooleanPermissionFlag.beFalse(value as Int?)
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
    fun toJsonString(): String {
        return JsonDeSerializer.toJson(this)
    }

    companion object {
        private val serialVersionUID = 1L

        /**
         * @param json
         * @return
         */
        @JvmStatic fun fromJsonString(json: String): PermissionMap {
            return JsonDeSerializer.fromJson(json, PermissionMap::class.java)
        }

        /**
         * @return
         */
        @JvmStatic fun buildAdminPermissionCollection(): PermissionMap {
            val permissionMap = PermissionMap()
            RolePermissionCollections.CRM_PERMISSIONS_ARR.forEach { (key) -> permissionMap.addPath(key, AccessPermissionFlag.ACCESS) }

            RolePermissionCollections.ACCOUNT_PERMISSION_ARR.forEach { (key) ->
                when (key) {
                    RolePermissionCollections.ACCOUNT_BILLING, RolePermissionCollections.ACCOUNT_THEME -> permissionMap.addPath(key, BooleanPermissionFlag.TRUE)
                    else -> permissionMap.addPath(key, AccessPermissionFlag.ACCESS)
                }
            }

            RolePermissionCollections.PROJECT_PERMISSION_ARR.forEach { (key) -> permissionMap.addPath(key, BooleanPermissionFlag.TRUE) }

            RolePermissionCollections.DOCUMENT_PERMISSION_ARR.forEach { (key) -> permissionMap.addPath(key, AccessPermissionFlag.ACCESS) }
            return permissionMap
        }

        /**
         * @return
         */
        @JvmStatic fun buildEmployeePermissionCollection(): PermissionMap {
            val permissionMap = PermissionMap()
            RolePermissionCollections.CRM_PERMISSIONS_ARR.forEach { permissionMap.addPath(it.key, AccessPermissionFlag.READ_ONLY) }

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

            RolePermissionCollections.DOCUMENT_PERMISSION_ARR.forEach { permissionMap.addPath(it.key, AccessPermissionFlag.READ_WRITE) }
            return permissionMap
        }

        /**
         * @return
         */
        @JvmStatic fun buildGuestPermissionCollection(): PermissionMap {
            val permissionMap = PermissionMap()
            RolePermissionCollections.CRM_PERMISSIONS_ARR.forEach { element -> permissionMap.addPath(element.key, AccessPermissionFlag.NO_ACCESS) }

            RolePermissionCollections.ACCOUNT_PERMISSION_ARR.forEach {
                when {
                    it.key == RolePermissionCollections.ACCOUNT_BILLING || it.key == RolePermissionCollections.ACCOUNT_THEME -> permissionMap.addPath(it.key, BooleanPermissionFlag.FALSE)
                    else -> permissionMap.addPath(it.key, AccessPermissionFlag.NO_ACCESS)
                }
            }

            RolePermissionCollections.PROJECT_PERMISSION_ARR.forEach { permissionMap.addPath(it.key, BooleanPermissionFlag.FALSE) }

            RolePermissionCollections.DOCUMENT_PERMISSION_ARR.forEach { permissionMap.addPath(it.key, AccessPermissionFlag.NO_ACCESS) }
            return permissionMap
        }
    }
}
