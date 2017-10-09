package com.mycollab.module.user.dao;

import com.mycollab.module.user.domain.SimpleBillingAccount;

public interface BillingAccountMapperExt {

    SimpleBillingAccount getBillingAccountById(Integer accountId);

    SimpleBillingAccount getAccountByDomain(String domainName);

    SimpleBillingAccount getDefaultAccountByDomain();
}
