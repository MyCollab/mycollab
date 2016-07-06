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
package com.mycollab.module.crm.domain.criteria;

import com.mycollab.common.CountryValueFactory;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.module.crm.CrmDataTypeFactory;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.i18n.AccountI18nEnum;
import com.mycollab.db.query.*;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class AccountSearchCriteria extends SearchCriteria {
    private static final long serialVersionUID = 1L;

    public static final StringParam p_accountName = CacheParamMapper.register(CrmTypeConstants.ACCOUNT,
            AccountI18nEnum.FORM_ACCOUNT_NAME, new StringParam("name", "m_crm_account", "accountName"));

    public static final StringParam p_website = CacheParamMapper.register(CrmTypeConstants.ACCOUNT, AccountI18nEnum.FORM_WEBSITE,
            new StringParam("website", "m_crm_account", "website"));

    public static final NumberParam p_numemployees = CacheParamMapper.register(CrmTypeConstants.ACCOUNT,
            AccountI18nEnum.FORM_EMPLOYEES, new NumberParam("employees", "m_crm_account", "numemployees"));

    public static final PropertyListParam p_assignee = CacheParamMapper.register(CrmTypeConstants.ACCOUNT, GenericI18Enum
            .FORM_ASSIGNEE, new PropertyListParam("assignuser", "m_crm_account", "assignUser"));

    public static final DateParam p_createdtime = CacheParamMapper.register(CrmTypeConstants.ACCOUNT,
            GenericI18Enum.FORM_CREATED_TIME, new DateParam("createdtime", "m_crm_account", "createdTime"));

    public static final DateParam p_lastupdatedtime = CacheParamMapper.register(CrmTypeConstants.ACCOUNT,
            GenericI18Enum.FORM_LAST_UPDATED_TIME, new DateParam("lastupdatedtime", "m_crm_account", "lastUpdatedTime"));

    public static final CompositionStringParam p_anyCity = CacheParamMapper.register(CrmTypeConstants.ACCOUNT,
            AccountI18nEnum.FORM_ANY_CITY, new CompositionStringParam("anyCity",
                    new StringParam("", "m_crm_account", "city"),
                    new StringParam("", "m_crm_account", "shippingCity")));

    public static final CompositionStringParam p_anyPhone = CacheParamMapper.register(CrmTypeConstants.ACCOUNT, AccountI18nEnum.FORM_ANY_PHONE,
            new CompositionStringParam("anyPhone",
                    new StringParam("", "m_crm_account", "alternatePhone"),
                    new StringParam("", "m_crm_account", "phoneOffice")));

    public static final StringListParam p_industries = CacheParamMapper.register(CrmTypeConstants.ACCOUNT,
            AccountI18nEnum.FORM_INDUSTRY, new StringListParam("industry", "m_crm_account", "industry",
                    Arrays.asList(CrmDataTypeFactory.getAccountIndustryList())));

    public static final I18nStringListParam p_types = CacheParamMapper.register(CrmTypeConstants.ACCOUNT, GenericI18Enum.FORM_TYPE,
            new I18nStringListParam("type", "m_crm_account", "type",
                    CrmDataTypeFactory.getAccountTypeList()));

    public static final Param p_billingCountry = CacheParamMapper.register(CrmTypeConstants.ACCOUNT,
            AccountI18nEnum.FORM_BILLING_COUNTRY, new StringListParam("billingCountry", "m_crm_account", "billingCountry",
                    Arrays.asList(CountryValueFactory.getCountryList())));

    public static final Param p_shippingCountry = CacheParamMapper.register(CrmTypeConstants.ACCOUNT,
            AccountI18nEnum.FORM_SHIPPING_COUNTRY, new StringListParam("shippingCountry", "m_crm_account", "shippingCountry",
                    Arrays.asList(CountryValueFactory.getCountryList())));

    private StringSearchField accountname;
    private StringSearchField assignUser;
    private StringSearchField website;
    private SetSearchField<String> types;
    private SetSearchField<String> industries;
    private SetSearchField<String> assignUsers;
    private StringSearchField anyCity;
    private StringSearchField anyPhone;
    private StringSearchField anyAddress;
    private StringSearchField anyMail;
    private NumberSearchField id;
    private NumberSearchField campaignId;

    public StringSearchField getAnyMail() {
        return anyMail;
    }

    public void setAnyMail(StringSearchField anyMail) {
        this.anyMail = anyMail;
    }

    public StringSearchField getAnyAddress() {
        return anyAddress;
    }

    public void setAnyAddress(StringSearchField anyAddress) {
        this.anyAddress = anyAddress;
    }

    public StringSearchField getAccountname() {
        return accountname;
    }

    public void setAccountname(StringSearchField accountname) {
        this.accountname = accountname;
    }

    public StringSearchField getAssignUser() {
        return assignUser;
    }

    public void setAssignUser(StringSearchField assignUser) {
        this.assignUser = assignUser;
    }

    public void setAnyCity(StringSearchField anyCity) {
        this.anyCity = anyCity;
    }

    public StringSearchField getAnyCity() {
        return anyCity;
    }

    public StringSearchField getWebsite() {
        return website;
    }

    public void setWebsite(StringSearchField website) {
        this.website = website;
    }

    public SetSearchField<String> getTypes() {
        return types;
    }

    public void setTypes(SetSearchField<String> types) {
        this.types = types;
    }

    public SetSearchField<String> getIndustries() {
        return industries;
    }

    public void setIndustries(SetSearchField<String> industries) {
        this.industries = industries;
    }

    public SetSearchField<String> getAssignUsers() {
        return assignUsers;
    }

    public void setAssignUsers(SetSearchField<String> assignUsers) {
        this.assignUsers = assignUsers;
    }

    public StringSearchField getAnyPhone() {
        return anyPhone;
    }

    public void setAnyPhone(StringSearchField anyPhone) {
        this.anyPhone = anyPhone;
    }

    public NumberSearchField getId() {
        return id;
    }

    public void setId(NumberSearchField id) {
        this.id = id;
    }

    public NumberSearchField getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(NumberSearchField campaignId) {
        this.campaignId = campaignId;
    }
}
