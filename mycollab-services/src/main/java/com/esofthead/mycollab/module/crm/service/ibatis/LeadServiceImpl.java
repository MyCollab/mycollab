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

import com.esofthead.mycollab.common.ModuleNameConstants;
import com.esofthead.mycollab.common.interceptor.aspect.ClassInfo;
import com.esofthead.mycollab.common.interceptor.aspect.ClassInfoMap;
import com.esofthead.mycollab.common.interceptor.aspect.Traceable;
import com.esofthead.mycollab.common.interceptor.aspect.Watchable;
import com.esofthead.mycollab.core.cache.CacheKey;
import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.ISearchableDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultService;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.dao.LeadMapper;
import com.esofthead.mycollab.module.crm.dao.LeadMapperExt;
import com.esofthead.mycollab.module.crm.domain.*;
import com.esofthead.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.esofthead.mycollab.module.crm.service.*;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.GregorianCalendar;

@Service
@Transactional
@Traceable(nameField = "lastname")
@Watchable(userFieldName = "assignuser")
public class LeadServiceImpl extends DefaultService<Integer, Lead, LeadSearchCriteria> implements LeadService {
    static {
        ClassInfoMap.put(LeadServiceImpl.class, new ClassInfo(ModuleNameConstants.CRM, CrmTypeConstants.LEAD));
    }

    private static final Logger LOG = LoggerFactory.getLogger(LeadServiceImpl.class);

    @Autowired
    private LeadMapper leadMapper;
    @Autowired
    private LeadMapperExt leadMapperExt;

    @Override
    public ICrudGenericDAO<Integer, Lead> getCrudMapper() {
        return leadMapper;
    }

    @Override
    public ISearchableDAO<LeadSearchCriteria> getSearchMapper() {
        return leadMapperExt;
    }

    @Override
    public SimpleLead findById(Integer leadId, Integer sAccountId) {
        return leadMapperExt.findById(leadId);
    }

    @Override
    public Integer saveWithSession(Lead lead, String username) {
        Integer result = super.saveWithSession(lead, username);
        if (lead.getExtraData() != null && (lead.getExtraData() instanceof SimpleCampaign)) {
            CampaignLead associateLead = new CampaignLead();
            associateLead.setCampaignid(((SimpleCampaign) lead.getExtraData()).getId());
            associateLead.setLeadid(lead.getId());
            associateLead.setCreatedtime(new GregorianCalendar().getTime());

            CampaignService campaignService = ApplicationContextUtil.getSpringBean(CampaignService.class);
            campaignService.saveCampaignLeadRelationship(Arrays.asList(associateLead), lead.getSaccountid());
        } else if (lead.getExtraData() != null && lead.getExtraData() instanceof SimpleOpportunity) {
            OpportunityLead associateLead = new OpportunityLead();
            associateLead.setOpportunityid(((SimpleOpportunity) lead.getExtraData()).getId());
            associateLead.setLeadid(lead.getId());
            associateLead.setCreatedtime(new GregorianCalendar().getTime());

            OpportunityService opportunityService = ApplicationContextUtil
                    .getSpringBean(OpportunityService.class);
            opportunityService.saveOpportunityLeadRelationship(
                    Arrays.asList(associateLead), lead.getSaccountid());
        }
        return result;
    }

    @Override
    public void convertLead(SimpleLead lead, Opportunity opportunity, String convertUser) {
        LOG.debug("Create new account and save it");
        Account account = new Account();
        account.setAccountname(lead.getAccountname());
        account.setNumemployees(lead.getNoemployees());
        account.setIndustry(lead.getIndustry());
        account.setEmail(lead.getEmail());
        account.setPhoneoffice(lead.getOfficephone());
        account.setFax(lead.getFax());
        account.setWebsite(lead.getWebsite());
        account.setAssignuser(lead.getAssignuser());
        account.setDescription(lead.getDescription());
        account.setSaccountid(lead.getSaccountid());

        AccountService accountService = ApplicationContextUtil.getSpringBean(AccountService.class);
        Integer accountId = accountService.saveWithSession(account, convertUser);

        LOG.debug("Create account lead relationship");
        AccountLead accLead = new AccountLead();
        accLead.setAccountid(accountId);
        accLead.setLeadid(lead.getId());
        accLead.setIsconvertrel(true);

        accountService.saveAccountLeadRelationship(Arrays.asList(accLead),
                lead.getSaccountid());

        LOG.debug("Create new contact and save it");
        Contact contact = new Contact();
        contact.setPrefix(lead.getPrefixname());
        contact.setFirstname(lead.getFirstname());
        contact.setLastname(lead.getLastname());
        contact.setTitle(lead.getTitle());
        contact.setDepartment(lead.getDepartment());
        contact.setAccountid(accountId);
        contact.setSaccountid(lead.getSaccountid());
        contact.setLeadsource(lead.getSource());
        contact.setAssignuser(lead.getAssignuser());
        contact.setOfficephone(lead.getOfficephone());
        contact.setEmail(lead.getEmail());
        contact.setSaccountid(lead.getSaccountid());

        ContactService contactService = ApplicationContextUtil
                .getSpringBean(ContactService.class);
        Integer contactId = contactService.saveWithSession(contact, convertUser);
        LOG.debug("Create contact lead relationship");
        ContactLead contactLead = new ContactLead();
        contactLead.setContactid(contactId);
        contactLead.setLeadid(lead.getId());
        contactLead.setIsconvertrel(true);
        contactService.saveContactLeadRelationship(Arrays.asList(contactLead),
                lead.getSaccountid());

        if (opportunity != null) {
            opportunity.setAccountid(accountId);
            opportunity.setSaccountid(lead.getSaccountid());
            OpportunityService opportunityService = ApplicationContextUtil.getSpringBean(OpportunityService.class);
            int opportunityId = opportunityService.saveWithSession(opportunity,
                    convertUser);

            LOG.debug("Create new opportunity contact relationship");
            ContactOpportunity oppContact = new ContactOpportunity();
            oppContact.setContactid(contactId);
            oppContact.setOpportunityid(opportunityId);
            contactService.saveContactOpportunityRelationship(
                    Arrays.asList(oppContact), lead.getSaccountid());

            LOG.debug("Create new opportunity lead relationship");
            OpportunityLead oppLead = new OpportunityLead();
            oppLead.setLeadid(lead.getId());
            oppLead.setOpportunityid(opportunityId);
            oppLead.setIsconvertrel(true);
            oppLead.setCreatedtime(new GregorianCalendar().getTime());
            opportunityService.saveOpportunityLeadRelationship(
                    Arrays.asList(oppLead), lead.getSaccountid());
        }

    }

    @Override
    public SimpleLead findConvertedLeadOfAccount(Integer accountId, @CacheKey Integer sAccountId) {
        return leadMapperExt.findConvertedLeadOfAccount(accountId);
    }

    @Override
    public SimpleLead findConvertedLeadOfContact(Integer contactId, @CacheKey Integer sAccountId) {
        return leadMapperExt.findConvertedLeadOfContact(contactId);
    }

    @Override
    public SimpleLead findConvertedLeadOfOpportunity(Integer opportunity, @CacheKey Integer sAccountId) {
        return leadMapperExt.findConvertedLeadOfOpportunity(opportunity);
    }
}
