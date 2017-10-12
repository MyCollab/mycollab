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
package com.mycollab.module.crm.service.impl

import com.mycollab.db.persistence.ICrudGenericDAO
import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.db.persistence.service.DefaultService
import com.mycollab.module.crm.dao.ContractMapper
import com.mycollab.module.crm.dao.ContractMapperExt
import com.mycollab.module.crm.domain.Contract
import com.mycollab.module.crm.domain.criteria.ContractSearchCriteria
import com.mycollab.module.crm.service.ContractService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ContractServiceImpl(private val contractMapper: ContractMapper,
                          private val contractMapperExt: ContractMapperExt) : DefaultService<Int, Contract, ContractSearchCriteria>(), ContractService {

    override val crudMapper: ICrudGenericDAO<Int, Contract>
        get() = contractMapper as ICrudGenericDAO<Int, Contract>

    override val searchMapper: ISearchableDAO<ContractSearchCriteria>
        get() = contractMapperExt
}
