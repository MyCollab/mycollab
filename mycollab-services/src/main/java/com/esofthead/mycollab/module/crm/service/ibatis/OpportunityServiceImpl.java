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
import com.esofthead.mycollab.common.domain.GroupItem;
import com.esofthead.mycollab.common.interceptor.aspect.ClassInfo;
import com.esofthead.mycollab.common.interceptor.aspect.ClassInfoMap;
import com.esofthead.mycollab.common.interceptor.aspect.Traceable;
import com.esofthead.mycollab.common.interceptor.aspect.Watchable;
import com.esofthead.mycollab.core.cache.CacheKey;
import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.ISearchableDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultService;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.dao.OpportunityLeadMapper;
import com.esofthead.mycollab.module.crm.dao.OpportunityMapper;
import com.esofthead.mycollab.module.crm.dao.OpportunityMapperExt;
import com.esofthead.mycollab.module.crm.domain.*;
import com.esofthead.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.esofthead.mycollab.module.crm.service.ContactService;
import com.esofthead.mycollab.module.crm.service.OpportunityService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
@Transactional
@Traceable(nameField = "opportunityname")
@Watchable(userFieldName = "assignuser")
public class OpportunityServiceImpl extends DefaultService<Integer, Opportunity, OpportunitySearchCriteria> implements OpportunityService {

    static {
        ClassInfoMap.put(OpportunityServiceImpl.class, new ClassInfo(ModuleNameConstants.CRM, CrmTypeConstants.OPPORTUNITY));
    }

    @Autowired
    private OpportunityMapper opportunityMapper;
    @Autowired
    private OpportunityMapperExt opportunityMapperExt;
    @Autowired
    private OpportunityLeadMapper opportunityLeadMapper;

    @Override
    public ICrudGenericDAO<Integer, Opportunity> getCrudMapper() {
        return opportunityMapper;
    }

    @Override
    public ISearchableDAO<OpportunitySearchCriteria> getSearchMapper() {
        return opportunityMapperExt;
    }

    @Override
    public SimpleOpportunity findById(Integer opportunityId, Integer sAccountId) {
        return opportunityMapperExt.findById(opportunityId);
    }

    @Override
    public Integer saveWithSession(Opportunity opportunity, String username) {
        Integer result = super.saveWithSession(opportunity, username);
        if ((opportunity.getExtraData() != null) && (opportunity.getExtraData() instanceof SimpleContact)) {
            ContactOpportunity associateOpportunity = new ContactOpportunity();
            associateOpportunity.setOpportunityid(opportunity.getId());
            associateOpportunity.setContactid(((SimpleContact) opportunity
                    .getExtraData()).getId());
            associateOpportunity.setCreatedtime(new GregorianCalendar()
                    .getTime());
            ContactService contactService = ApplicationContextUtil
                    .getSpringBean(ContactService.class);
            contactService.saveContactOpportunityRelationship(
                    Arrays.asList(associateOpportunity),
                    opportunity.getSaccountid());
        }
        return result;
    }

    @Override
    public List<GroupItem> getSalesStageSummary(OpportunitySearchCriteria criteria) {
        return opportunityMapperExt.getSalesStageSummary(criteria);
    }

    @Override
    public List<GroupItem> getLeadSourcesSummary(OpportunitySearchCriteria criteria) {
        return opportunityMapperExt.getLeadSourcesSummary(criteria);
    }

    @Override
    public List<GroupItem> getPipeline(@CacheKey OpportunitySearchCriteria criteria) {
        return opportunityMapperExt.getPipeline(criteria);
    }

    @Override
    public void saveOpportunityLeadRelationship(
            List<OpportunityLead> associateLeads, Integer sAccountId) {
        for (OpportunityLead associateLead : associateLeads) {
            OpportunityLeadExample ex = new OpportunityLeadExample();
            ex.createCriteria().andOpportunityidEqualTo(associateLead.getOpportunityid())
                    .andLeadidEqualTo(associateLead.getLeadid());
            if (opportunityLeadMapper.countByExample(ex) == 0) {
                opportunityLeadMapper.insert(associateLead);
            }
        }
    }

    @Override
    public void removeOpportunityLeadRelationship(OpportunityLead associateLead, Integer sAccountId) {
        OpportunityLeadExample ex = new OpportunityLeadExample();
        ex.createCriteria().andOpportunityidEqualTo(associateLead.getOpportunityid())
                .andLeadidEqualTo(associateLead.getLeadid());
        opportunityLeadMapper.deleteByExample(ex);
    }

    @Override
    public SimpleOpportunity findOpportunityAssoWithConvertedLead(Integer leadId, @CacheKey Integer accountId) {
        return opportunityMapperExt.findOpportunityAssoWithConvertedLead(leadId);
    }
}
