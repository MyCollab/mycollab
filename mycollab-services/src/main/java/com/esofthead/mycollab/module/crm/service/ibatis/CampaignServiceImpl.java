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
import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.ISearchableDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultService;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.dao.*;
import com.esofthead.mycollab.module.crm.domain.*;
import com.esofthead.mycollab.module.crm.domain.criteria.CampaignSearchCriteria;
import com.esofthead.mycollab.module.crm.service.CampaignService;
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
@Traceable(nameField = "campaignname")
@Watchable(userFieldName = "assignuser")
public class CampaignServiceImpl extends DefaultService<Integer, CampaignWithBLOBs, CampaignSearchCriteria> implements CampaignService {

    static {
        ClassInfoMap.put(CampaignServiceImpl.class, new ClassInfo(ModuleNameConstants.CRM, CrmTypeConstants.CAMPAIGN));
    }

    @Autowired
    private CampaignMapper campaignMapper;
    @Autowired
    private CampaignMapperExt campaignMapperExt;
    @Autowired
    private CampaignAccountMapper campaignAccountMapper;
    @Autowired
    private CampaignContactMapper campaignContactMapper;
    @Autowired
    private CampaignLeadMapper campaignLeadMapper;

    @Override
    public ICrudGenericDAO<Integer, CampaignWithBLOBs> getCrudMapper() {
        return campaignMapper;
    }

    @Override
    public ISearchableDAO<CampaignSearchCriteria> getSearchMapper() {
        return campaignMapperExt;
    }

    @Override
    public SimpleCampaign findById(Integer campaignId, Integer sAccountUd) {
        return campaignMapperExt.findById(campaignId);
    }

    @Override
    public Integer saveWithSession(CampaignWithBLOBs campaign, String username) {
        Integer result = super.saveWithSession(campaign, username);
        if (campaign.getExtraData() != null
                && campaign.getExtraData() instanceof SimpleLead) {
            CampaignLead associateLead = new CampaignLead();
            associateLead.setCampaignid(campaign.getId());
            associateLead.setLeadid(((SimpleLead) campaign.getExtraData())
                    .getId());
            associateLead.setCreatedtime(new GregorianCalendar().getTime());

            this.saveCampaignLeadRelationship(Arrays.asList(associateLead),
                    campaign.getSaccountid());
        }
        return result;
    }

    @Override
    public void saveCampaignAccountRelationship(
            List<CampaignAccount> associateAccounts, Integer sAccountId) {
        for (CampaignAccount associateAccount : associateAccounts) {
            CampaignAccountExample ex = new CampaignAccountExample();
            ex.createCriteria()
                    .andAccountidEqualTo(associateAccount.getAccountid())
                    .andCampaignidEqualTo(associateAccount.getCampaignid());
            if (campaignAccountMapper.countByExample(ex) == 0) {
                campaignAccountMapper.insert(associateAccount);
            }
        }
    }

    @Override
    public void removeCampaignAccountRelationship(
            CampaignAccount associateAccount, Integer sAccountId) {
        CampaignAccountExample ex = new CampaignAccountExample();
        ex.createCriteria()
                .andAccountidEqualTo(associateAccount.getAccountid())
                .andCampaignidEqualTo(associateAccount.getCampaignid());
        campaignAccountMapper.deleteByExample(ex);
    }

    @Override
    public void saveCampaignContactRelationship(
            List<CampaignContact> associateContacts, Integer sAccountId) {
        for (CampaignContact associateContact : associateContacts) {
            CampaignContactExample ex = new CampaignContactExample();
            ex.createCriteria()
                    .andCampaignidEqualTo(associateContact.getCampaignid())
                    .andContactidEqualTo(associateContact.getContactid());
            if (campaignContactMapper.countByExample(ex) == 0) {
                campaignContactMapper.insert(associateContact);
            }
        }
    }

    @Override
    public void removeCampaignContactRelationship(
            CampaignContact associateContact, Integer sAccountId) {
        CampaignContactExample ex = new CampaignContactExample();
        ex.createCriteria()
                .andCampaignidEqualTo(associateContact.getCampaignid())
                .andContactidEqualTo(associateContact.getContactid());
        campaignContactMapper.deleteByExample(ex);
    }

    @Override
    public void saveCampaignLeadRelationship(List<CampaignLead> associateLeads,
                                             Integer sAccountId) {
        for (CampaignLead associateLead : associateLeads) {
            CampaignLeadExample ex = new CampaignLeadExample();
            ex.createCriteria()
                    .andCampaignidEqualTo(associateLead.getCampaignid())
                    .andLeadidEqualTo(associateLead.getLeadid());
            if (campaignLeadMapper.countByExample(ex) == 0) {
                campaignLeadMapper.insert(associateLead);
            }
        }
    }

    @Override
    public void removeCampaignLeadRelationship(CampaignLead associateLead,
                                               Integer sAccountId) {
        CampaignLeadExample ex = new CampaignLeadExample();
        ex.createCriteria().andCampaignidEqualTo(associateLead.getCampaignid())
                .andLeadidEqualTo(associateLead.getLeadid());
        campaignLeadMapper.deleteByExample(ex);
    }
}
