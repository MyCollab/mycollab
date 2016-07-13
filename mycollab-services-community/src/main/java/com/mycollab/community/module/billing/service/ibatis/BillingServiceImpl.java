/**
 * This file is part of mycollab-services-community.
 *
 * mycollab-services-community is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services-community is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services-community.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.community.module.billing.service.ibatis;

import com.mycollab.common.domain.CustomerFeedbackWithBLOBs;
import com.mycollab.core.MyCollabException;
import com.mycollab.core.cache.CacheKey;
import com.mycollab.module.billing.service.BillingService;
import com.mycollab.module.user.dao.BillingAccountMapper;
import com.mycollab.module.user.dao.BillingPlanMapper;
import com.mycollab.module.user.domain.BillingAccount;
import com.mycollab.module.user.domain.BillingAccountWithOwners;
import com.mycollab.module.user.domain.BillingPlan;
import com.mycollab.module.user.domain.BillingPlanExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(value = "billingService")
public class BillingServiceImpl implements BillingService {

    @Autowired
    private BillingPlanMapper billingPlanMapper;

    @Autowired
    private BillingAccountMapper billingAccountMapper;

    @Override
    @Transactional
    public void registerAccount(String subDomain, int billingPlanId, String username,
                                String password, String email, String timezoneId, boolean isEmailVerified) {
        throw new MyCollabException("This feature is not supported except onsite mode");
    }

    @Override
    public List<String> getSubDomainsOfUser(String username) {
        throw new MyCollabException("This feature is not supported except onsite mode");
    }

    @Override
    public List<BillingPlan> getAvailablePlans() {
        throw new MyCollabException("This feature is not supported except onsite mode");
    }

    @Override
    public void updateBillingPlan(Integer accountId, int newBillingPlanId) {
        throw new MyCollabException("This feature is not supported except onsite mode");
    }

    @Override
    public void cancelAccount(Integer accountId, CustomerFeedbackWithBLOBs feedback) {
        throw new MyCollabException("This feature is not supported except onsite mode");
    }

    @Override
    public BillingPlan getFreeBillingPlan() {
        BillingPlanExample ex = new BillingPlanExample();
        ex.createCriteria().andBillingtypeEqualTo("Free");
        List<BillingPlan> billingPlans = billingPlanMapper.selectByExample(ex);
        if (billingPlans != null && billingPlans.size() == 1) {
            return billingPlans.get(0);
        } else {
            throw new MyCollabException("Can not query free billing plan");
        }
    }

    @Override
    public BillingPlan findBillingPlan(@CacheKey Integer sAccountId) {
        BillingAccount billingAccount = billingAccountMapper.selectByPrimaryKey(sAccountId);
        if (billingAccount != null) {
            Integer billingplanid = billingAccount.getBillingplanid();
            return billingPlanMapper.selectByPrimaryKey(billingplanid);
        }
        return null;
    }

    @Override
    public List<BillingAccountWithOwners> getTrialAccountsWithOwners() {
        return null;
    }

}