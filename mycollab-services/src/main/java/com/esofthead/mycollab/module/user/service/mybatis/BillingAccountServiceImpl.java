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
package com.esofthead.mycollab.module.user.service.mybatis;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.DeploymentMode;
import com.esofthead.mycollab.core.cache.CacheKey;
import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultCrudService;
import com.esofthead.mycollab.module.user.dao.BillingAccountMapper;
import com.esofthead.mycollab.module.user.dao.BillingAccountMapperExt;
import com.esofthead.mycollab.module.user.domain.BillingAccount;
import com.esofthead.mycollab.module.user.domain.BillingAccountExample;
import com.esofthead.mycollab.module.user.domain.SimpleBillingAccount;
import com.esofthead.mycollab.module.user.service.BillingAccountService;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Service
public class BillingAccountServiceImpl extends
		DefaultCrudService<Integer, BillingAccount> implements
		BillingAccountService {

	@Autowired
	private BillingAccountMapper billingAccountMapper;

	@Autowired
	private BillingAccountMapperExt billingAccountMapperExt;

	@Override
	public ICrudGenericDAO<Integer, BillingAccount> getCrudMapper() {
		return billingAccountMapper;
	}

	@Override
	public SimpleBillingAccount getBillingAccountById(int accountId) {
		return billingAccountMapperExt.getBillingAccountById(accountId);
	}

	@Override
	public BillingAccount getAccountByDomain(String domain) {
		BillingAccountExample ex = new BillingAccountExample();

		if (SiteConfiguration.getDeploymentMode() == DeploymentMode.site) {
			ex.createCriteria().andSubdomainEqualTo(domain);
		}

		List<BillingAccount> accounts = billingAccountMapper
				.selectByExample(ex);
		if ((accounts == null) || accounts.size() == 0) {
			return null;
		} else {
			return accounts.get(0);
		}
	}

	@Override
	public BillingAccount getAccountById(@CacheKey Integer accountId) {
		BillingAccountExample ex = new BillingAccountExample();

		if (SiteConfiguration.getDeploymentMode() == DeploymentMode.site) {
			ex.createCriteria().andIdEqualTo(accountId);
		}

		List<BillingAccount> accounts = billingAccountMapper
				.selectByExample(ex);
		if ((accounts == null) || accounts.size() == 0) {
			return null;
		} else {
			return accounts.get(0);
		}
	}

}
