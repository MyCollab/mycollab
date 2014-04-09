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

import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esofthead.mycollab.cache.CacheUtils;
import com.esofthead.mycollab.common.ModuleNameConstants;
import com.esofthead.mycollab.common.interceptor.aspect.Auditable;
import com.esofthead.mycollab.common.interceptor.aspect.Traceable;
import com.esofthead.mycollab.common.interceptor.aspect.Watchable;
import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.ISearchableDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultService;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.dao.AccountLeadMapper;
import com.esofthead.mycollab.module.crm.dao.AccountMapper;
import com.esofthead.mycollab.module.crm.dao.AccountMapperExt;
import com.esofthead.mycollab.module.crm.domain.Account;
import com.esofthead.mycollab.module.crm.domain.AccountLead;
import com.esofthead.mycollab.module.crm.domain.AccountLeadExample;
import com.esofthead.mycollab.module.crm.domain.SimpleAccount;
import com.esofthead.mycollab.module.crm.domain.criteria.AccountSearchCriteria;
import com.esofthead.mycollab.module.crm.service.AccountService;
import com.esofthead.mycollab.module.crm.service.LeadService;
import com.esofthead.mycollab.schedule.email.crm.AccountRelayEmailNotificationAction;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Service
@Transactional
@Traceable(module = ModuleNameConstants.CRM, type = CrmTypeConstants.ACCOUNT, nameField = "accountname")
@Auditable(module = ModuleNameConstants.CRM, type = CrmTypeConstants.ACCOUNT)
@Watchable(type = CrmTypeConstants.ACCOUNT, userFieldName = "assignuser", emailHandlerBean = AccountRelayEmailNotificationAction.class)
public class AccountServiceImpl extends
		DefaultService<Integer, Account, AccountSearchCriteria> implements
		AccountService {

	@Autowired
	protected AccountMapper accountMapper;
	@Autowired
	protected AccountMapperExt accountMapperExt;

	@Autowired
	protected AccountLeadMapper accountLeadMapper;

	@Override
	public ICrudGenericDAO<Integer, Account> getCrudMapper() {
		return accountMapper;
	}

	@Override
	public ISearchableDAO<AccountSearchCriteria> getSearchMapper() {
		return accountMapperExt;
	}

	@Override
	public SimpleAccount findById(int id, int accountId) {
		return accountMapperExt.findById(id);
	}

	@Override
	public void saveAccountLeadRelationship(List<AccountLead> associateLeads,
			Integer accountId) {
		for (AccountLead associateLead : associateLeads) {
			AccountLeadExample ex = new AccountLeadExample();
			ex.createCriteria()
					.andAccountidEqualTo(associateLead.getAccountid())
					.andLeadidEqualTo(associateLead.getLeadid());
			if (accountLeadMapper.countByExample(ex) == 0) {
				associateLead.setCreatetime(new GregorianCalendar().getTime());
				accountLeadMapper.insert(associateLead);
			}
		}

		cleanAccountLeadCaches(accountId);
	}

	@Override
	public void removeAccountLeadRelationship(AccountLead associateLead,
			Integer accountId) {
		AccountLeadExample ex = new AccountLeadExample();
		ex.createCriteria().andAccountidEqualTo(associateLead.getAccountid())
				.andLeadidEqualTo(associateLead.getLeadid());
		accountLeadMapper.deleteByExample(ex);

		cleanAccountLeadCaches(accountId);
	}

	private void cleanAccountLeadCaches(Integer accountId) {
		// Clean cache relate to account and lead
		CacheUtils.cleanCaches(accountId, LeadService.class,
				AccountService.class);
	}

	@Override
	public SimpleAccount findAccountAssoWithConvertedLead(int leadId,
			int accountId) {
		return accountMapperExt.findAccountAssoWithConvertedLead(leadId);
	}

}
