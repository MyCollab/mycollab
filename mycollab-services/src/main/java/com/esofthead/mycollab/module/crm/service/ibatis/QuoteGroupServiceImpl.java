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
package com.esofthead.mycollab.module.crm.service.ibatis;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultCrudService;
import com.esofthead.mycollab.module.crm.dao.QuoteGroupProductMapper;
import com.esofthead.mycollab.module.crm.dao.QuoteGroupProductMapperExt;
import com.esofthead.mycollab.module.crm.domain.QuoteGroupProduct;
import com.esofthead.mycollab.module.crm.domain.QuoteGroupProductExample;
import com.esofthead.mycollab.module.crm.service.QuoteGroupProductService;

@Service
@Transactional
public class QuoteGroupServiceImpl extends
		DefaultCrudService<Integer, QuoteGroupProduct> implements
		QuoteGroupProductService {
	
	@Autowired
	private QuoteGroupProductMapper quoteGroupProductMapper;

	@Autowired
	private QuoteGroupProductMapperExt quoteGroupProductMapperExt;
	
	@Override
	public ICrudGenericDAO<Integer, QuoteGroupProduct> getCrudMapper() {
		return quoteGroupProductMapper;
	}

	@Override
	public List<QuoteGroupProduct> findQuoteGroupByQuoteId(int quoteid) {
		QuoteGroupProductExample ex = new QuoteGroupProductExample();
		ex.createCriteria().andQuoteidEqualTo(quoteid);

		return quoteGroupProductMapper.selectByExample(ex);
	}

	@Override
	public void deleteQuoteGroupByQuoteId(int quoteid) {
		QuoteGroupProductExample ex = new QuoteGroupProductExample();
		ex.createCriteria().andQuoteidEqualTo(quoteid);

		quoteGroupProductMapper.deleteByExample(ex);

	}

	@Override
	public int insertQuoteGroupExt(QuoteGroupProduct record) {
		quoteGroupProductMapperExt.insertQuoteGroupExt(record);
		return record.getId();
	}

}
