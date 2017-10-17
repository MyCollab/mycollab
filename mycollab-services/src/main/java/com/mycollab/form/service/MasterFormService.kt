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
package com.mycollab.form.service

import com.mycollab.core.cache.CacheEvict
import com.mycollab.core.cache.CacheKey
import com.mycollab.core.cache.Cacheable
import com.mycollab.db.persistence.service.IService
import com.mycollab.form.view.builder.type.DynaForm

/**
 * @author MyCollab Ltd
 * @since 1.0.0
 */
interface MasterFormService : IService {
    @Cacheable
    fun findCustomForm(@CacheKey sAccountId: Int, moduleName: String): DynaForm?

    @CacheEvict
    fun saveCustomForm(@CacheKey sAccountId: Int, moduleName: String, form: DynaForm)
}
