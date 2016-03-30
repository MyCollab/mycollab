/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.project.service.ibatis;

import com.esofthead.mycollab.common.ModuleNameConstants;
import com.esofthead.mycollab.common.interceptor.aspect.ClassInfo;
import com.esofthead.mycollab.common.interceptor.aspect.ClassInfoMap;
import com.esofthead.mycollab.common.interceptor.aspect.Traceable;
import com.esofthead.mycollab.core.cache.CacheKey;
import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.ISearchableDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultService;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.dao.InvoiceMapper;
import com.esofthead.mycollab.module.project.dao.InvoiceMapperExt;
import com.esofthead.mycollab.module.project.domain.Invoice;
import com.esofthead.mycollab.module.project.domain.SimpleInvoice;
import com.esofthead.mycollab.module.project.domain.criteria.InvoiceSearchCriteria;
import com.esofthead.mycollab.module.project.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author MyCollab Ltd
 * @since 5.2.10
 */
@Service
@Traceable(nameField = "noid", extraFieldName = "projectid")
@Transactional
public class InvoiceServiceImpl extends DefaultService<Integer, Invoice, InvoiceSearchCriteria> implements InvoiceService {

    static {
        ClassInfoMap.put(InvoiceServiceImpl.class, new ClassInfo(ModuleNameConstants.PRJ, ProjectTypeConstants.INVOICE));
    }

    @Autowired
    private InvoiceMapper invoiceMapper;

    @Autowired
    private InvoiceMapperExt invoiceMapperExt;

    @Override
    public ISearchableDAO<InvoiceSearchCriteria> getSearchMapper() {
        return invoiceMapperExt;
    }

    @Override
    public ICrudGenericDAO<Integer, Invoice> getCrudMapper() {
        return invoiceMapper;
    }

    @Override
    public SimpleInvoice findById(Integer invoiceId, @CacheKey Integer sAccountId) {
        return invoiceMapperExt.findInvoiceById(invoiceId);
    }
}
