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

import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esofthead.mycollab.common.ModuleNameConstants;
import com.esofthead.mycollab.common.interceptor.aspect.Auditable;
import com.esofthead.mycollab.common.interceptor.aspect.Traceable;
import com.esofthead.mycollab.common.interceptor.aspect.Watchable;
import com.esofthead.mycollab.core.cache.CacheEvict;
import com.esofthead.mycollab.core.cache.CacheKey;
import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.ISearchableDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultService;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.dao.ContactCaseMapper;
import com.esofthead.mycollab.module.crm.dao.ContactLeadMapper;
import com.esofthead.mycollab.module.crm.dao.ContactMapper;
import com.esofthead.mycollab.module.crm.dao.ContactMapperExt;
import com.esofthead.mycollab.module.crm.dao.ContactOpportunityMapper;
import com.esofthead.mycollab.module.crm.domain.CampaignContact;
import com.esofthead.mycollab.module.crm.domain.Contact;
import com.esofthead.mycollab.module.crm.domain.ContactCase;
import com.esofthead.mycollab.module.crm.domain.ContactCaseExample;
import com.esofthead.mycollab.module.crm.domain.ContactLead;
import com.esofthead.mycollab.module.crm.domain.ContactLeadExample;
import com.esofthead.mycollab.module.crm.domain.ContactOpportunity;
import com.esofthead.mycollab.module.crm.domain.ContactOpportunityExample;
import com.esofthead.mycollab.module.crm.domain.SimpleCampaign;
import com.esofthead.mycollab.module.crm.domain.SimpleCase;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.esofthead.mycollab.module.crm.service.CampaignService;
import com.esofthead.mycollab.module.crm.service.ContactService;
import com.esofthead.mycollab.module.crm.service.LeadService;
import com.esofthead.mycollab.schedule.email.crm.ContactRelayEmailNotificationAction;
import com.esofthead.mycollab.spring.ApplicationContextUtil;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Service
@Transactional
@Traceable(module = ModuleNameConstants.CRM, type = CrmTypeConstants.CONTACT, nameField = "lastname")
@Auditable(module = ModuleNameConstants.CRM, type = CrmTypeConstants.CONTACT)
@Watchable(type = CrmTypeConstants.CONTACT, userFieldName = "assignuser", emailHandlerBean = ContactRelayEmailNotificationAction.class)
public class ContactServiceImpl extends
		DefaultService<Integer, Contact, ContactSearchCriteria> implements
		ContactService {

	@Autowired
	private ContactMapper contactMapper;
	@Autowired
	private ContactMapperExt contactMapperExt;
	@Autowired
	private ContactOpportunityMapper contactOpportunityMapper;
	@Autowired
	private ContactCaseMapper contactCaseMapper;
	@Autowired
	private ContactLeadMapper contactLeadMapper;

	@Override
	public ICrudGenericDAO<Integer, Contact> getCrudMapper() {
		return contactMapper;
	}

	@Override
	public ISearchableDAO<ContactSearchCriteria> getSearchMapper() {
		return contactMapperExt;
	}

	@Override
	public SimpleContact findById(int contactId, int sAccountId) {
		SimpleContact contact = contactMapperExt.findById(contactId);
		return contact;
	}

	@Override
	public void removeContactOpportunityRelationship(
			ContactOpportunity associateOpportunity, Integer sAccountId) {
		ContactOpportunityExample ex = new ContactOpportunityExample();
		ex.createCriteria()
				.andContactidEqualTo(associateOpportunity.getContactid())
				.andOpportunityidEqualTo(
						associateOpportunity.getOpportunityid());
		contactOpportunityMapper.deleteByExample(ex);
	}

	@Override
	public void saveContactOpportunityRelationship(
			List<ContactOpportunity> associateOpportunities, Integer accountId) {
		for (ContactOpportunity assoOpportunity : associateOpportunities) {
			ContactOpportunityExample ex = new ContactOpportunityExample();
			ex.createCriteria()
					.andContactidEqualTo(assoOpportunity.getContactid())
					.andOpportunityidEqualTo(assoOpportunity.getOpportunityid());
			if (contactOpportunityMapper.countByExample(ex) == 0) {
				assoOpportunity.setCreatedtime(new GregorianCalendar()
						.getTime());
				contactOpportunityMapper.insert(assoOpportunity);
			} else {
				contactOpportunityMapper.updateByExampleSelective(
						assoOpportunity, ex);
			}
		}
	}

	@Override
	public void saveContactCaseRelationship(List<ContactCase> associateCases,
			Integer accountId) {
		for (ContactCase associateCase : associateCases) {
			ContactCaseExample ex = new ContactCaseExample();
			ex.createCriteria()
					.andContactidEqualTo(associateCase.getContactid())
					.andCaseidEqualTo(associateCase.getCaseid());
			if (contactCaseMapper.countByExample(ex) == 0) {
				associateCase.setCreatedtime(new GregorianCalendar().getTime());
				contactCaseMapper.insert(associateCase);
			}
		}
	}

	@Override
	public void removeContactCaseRelationship(ContactCase associateCase,
			Integer sAccountId) {
		ContactCaseExample ex = new ContactCaseExample();
		ex.createCriteria().andContactidEqualTo(associateCase.getContactid())
				.andCaseidEqualTo(associateCase.getCaseid());
		contactCaseMapper.deleteByExample(ex);
	}

	@Override
	public void saveContactLeadRelationship(List<ContactLead> associateLeads,
			@CacheKey Integer accountId) {
		for (ContactLead associateLead : associateLeads) {
			ContactLeadExample ex = new ContactLeadExample();
			ex.createCriteria()
					.andContactidEqualTo(associateLead.getContactid())
					.andLeadidEqualTo(associateLead.getLeadid());
			if (contactLeadMapper.countByExample(ex) == 0) {
				contactLeadMapper.insert(associateLead);
			}
		}

	}

	@Override
	public SimpleContact findContactAssoWithConvertedLead(int leadId,
			@CacheKey int accountId) {
		return contactMapperExt.findContactAssoWithConvertedLead(leadId);
	}

	@Override
	public int saveWithSession(Contact contact, String username) {
		int result = super.saveWithSession(contact, username);
		if (contact.getExtraData() != null
				&& contact.getExtraData() instanceof SimpleCampaign) {
			CampaignContact associateContact = new CampaignContact();
			associateContact.setCampaignid(((SimpleCampaign) contact
					.getExtraData()).getId());
			associateContact.setContactid(contact.getId());
			associateContact.setCreatedtime(new GregorianCalendar().getTime());

			CampaignService campaignService = ApplicationContextUtil
					.getSpringBean(CampaignService.class);
			campaignService.saveCampaignContactRelationship(
					Arrays.asList(associateContact), contact.getSaccountid());
		} else if (contact.getExtraData() != null
				&& contact.getExtraData() instanceof SimpleOpportunity) {
			ContactOpportunity associateContact = new ContactOpportunity();
			associateContact.setContactid(contact.getId());
			associateContact.setOpportunityid(((SimpleOpportunity) contact
					.getExtraData()).getId());
			associateContact.setCreatedtime(new GregorianCalendar().getTime());

			this.saveContactOpportunityRelationship(
					Arrays.asList(associateContact), contact.getSaccountid());
		} else if (contact.getExtraData() != null
				&& contact.getExtraData() instanceof SimpleCase) {
			ContactCase associateCase = new ContactCase();
			associateCase.setContactid(contact.getId());
			associateCase.setCaseid(((SimpleCase) contact.getExtraData())
					.getId());
			associateCase.setCreatedtime(new GregorianCalendar().getTime());

			this.saveContactCaseRelationship(Arrays.asList(associateCase),
					contact.getSaccountid());
		}
		return result;
	}
}
