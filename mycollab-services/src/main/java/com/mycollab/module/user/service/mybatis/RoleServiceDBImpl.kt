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
package com.mycollab.module.user.service.mybatis

import com.mycollab.db.persistence.ICrudGenericDAO
import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.db.persistence.service.DefaultService
import com.mycollab.module.user.dao.RoleMapper
import com.mycollab.module.user.dao.RoleMapperExt
import com.mycollab.module.user.dao.RolePermissionMapper
import com.mycollab.module.user.domain.*
import com.mycollab.module.user.domain.criteria.RoleSearchCriteria
import com.mycollab.module.user.service.RoleService
import com.mycollab.security.PermissionMap
import org.apache.commons.collections.CollectionUtils
import org.springframework.stereotype.Service

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
class RoleServiceDBImpl(private val roleMapper: RoleMapper,
                        private val roleMapperExt: RoleMapperExt,
                        private val rolePermissionMapper: RolePermissionMapper) : DefaultService<Int, Role, RoleSearchCriteria>(), RoleService {

    override val crudMapper: ICrudGenericDAO<Int, Role>
        get() = roleMapper as ICrudGenericDAO<Int, Role>

    override val searchMapper: ISearchableDAO<RoleSearchCriteria>
        get() = roleMapperExt

    override fun saveWithSession(record: Role, username: String?): Int {
        if (java.lang.Boolean.TRUE == record.isdefault) {
            setAllRoleNotDefault(record.saccountid)
        }
        return super.saveWithSession(record, username)
    }

    private fun setAllRoleNotDefault(sAccountId: Int) {
        val updateRecord = Role()
        updateRecord.isdefault = java.lang.Boolean.FALSE
        val ex = RoleExample()
        ex.createCriteria().andSaccountidEqualTo(sAccountId)
        roleMapper.updateByExampleSelective(updateRecord, ex)
    }

    override fun updateWithSession(record: Role, username: String?): Int {
        if (java.lang.Boolean.TRUE == record.isdefault) {
            setAllRoleNotDefault(record.saccountid)
        }
        return super.updateWithSession(record, username)
    }

    override fun savePermission(roleId: Int?, permissionMap: PermissionMap, sAccountId: Int) {
        val perVal = permissionMap.toJsonString()

        val ex = RolePermissionExample()
        ex.createCriteria().andRoleidEqualTo(roleId)

        val rolePer = RolePermission()
        rolePer.roleid = roleId
        rolePer.roleval = perVal

        val data = rolePermissionMapper.countByExample(ex)
        when {
            data > 0 -> rolePermissionMapper.updateByExampleSelective(rolePer, ex)
            else -> rolePermissionMapper.insert(rolePer)
        }
    }

    override fun findById(roleId: Int, sAccountId: Int): SimpleRole? = roleMapperExt.findById(roleId)

    override fun getDefaultRoleId(sAccountId: Int): Int? {
        var ex = RoleExample()
        ex.createCriteria().andIsdefaultEqualTo(java.lang.Boolean.TRUE).andSaccountidEqualTo(sAccountId)
        var roles = roleMapper.selectByExample(ex)
        return when {
            CollectionUtils.isNotEmpty(roles) -> roles[0].id
            else -> {
                ex = RoleExample()
                ex.createCriteria().andRolenameEqualTo(SimpleRole.GUEST).andSaccountidEqualTo(sAccountId)
                roles = roleMapper.selectByExample(ex)
                if (CollectionUtils.isNotEmpty(roles)) roles[0].id else null
            }
        }
    }
}
