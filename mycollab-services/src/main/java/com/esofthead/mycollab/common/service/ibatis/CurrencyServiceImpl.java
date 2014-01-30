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
package com.esofthead.mycollab.common.service.ibatis;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esofthead.mycollab.common.dao.CurrencyMapper;
import com.esofthead.mycollab.common.domain.Currency;
import com.esofthead.mycollab.common.domain.CurrencyExample;
import com.esofthead.mycollab.common.service.CurrencyService;
import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultCrudService;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Service
public class CurrencyServiceImpl extends DefaultCrudService<Integer, Currency>
		implements CurrencyService {

	private static List<Currency> currencies;

	@Autowired
	private CurrencyMapper currencyMapper;

	@Override
	public ICrudGenericDAO<Integer, Currency> getCrudMapper() {
		return currencyMapper;
	}

	@Override
	public List<Currency> getCurrencies() {
		if (currencies == null) {
			CurrencyExample ex = new CurrencyExample();
			currencies = currencyMapper.selectByExample(ex);
		}

		return currencies;
	}

	@Override
	public Currency getCurrency(int currencyid) {
		List<Currency> currencies2 = getCurrencies();
		for (Currency currency : currencies2) {
			if (currency.getId() == currencyid) {
				return currency;
			}
		}

		return null;
	}

}
