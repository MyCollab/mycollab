package com.mycollab.common.domain.criteria

import com.mycollab.common.i18n.ClientI18nEnum
import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.db.arguments.NumberSearchField
import com.mycollab.db.arguments.SearchCriteria
import com.mycollab.db.arguments.SetSearchField
import com.mycollab.db.arguments.StringSearchField
import com.mycollab.db.query.*
import com.mycollab.module.project.ProjectTypeConstants

class ClientSearchCriteria : SearchCriteria() {

    var name: StringSearchField? = null
    var assignUser: StringSearchField? = null
    var website: StringSearchField? = null
    var types: SetSearchField<String>? = null
    var industries: SetSearchField<String>? = null
    var assignUsers: SetSearchField<String>? = null
    var anyCity: StringSearchField? = null
    var anyPhone: StringSearchField? = null
    var anyAddress: StringSearchField? = null
    var anyMail: StringSearchField? = null
    var id: NumberSearchField? = null

    companion object {
        private val serialVersionUID = 1L

        @JvmField val p_name = CacheParamMapper.register(ProjectTypeConstants.CLIENT,
                ClientI18nEnum.FORM_ACCOUNT_NAME, StringParam("name", "m_client", "name"))

        @JvmField val p_website = CacheParamMapper.register(ProjectTypeConstants.CLIENT, ClientI18nEnum.FORM_WEBSITE,
                StringParam("website", "m_client", "website"))

        @JvmField val p_numemployees = CacheParamMapper.register(ProjectTypeConstants.CLIENT,
                ClientI18nEnum.FORM_EMPLOYEES, NumberParam("employees", "m_client", "numemployees"))

        @JvmField val p_assignee: PropertyListParam<*> = CacheParamMapper.register(ProjectTypeConstants.CLIENT, GenericI18Enum
                .FORM_ASSIGNEE, PropertyListParam<String>("assignuser", "m_client", "assignUser"))

        @JvmField val p_createdtime = CacheParamMapper.register(ProjectTypeConstants.CLIENT,
                GenericI18Enum.FORM_CREATED_TIME, DateParam("createdtime", "m_client", "createdTime"))

        @JvmField val p_lastupdatedtime = CacheParamMapper.register(ProjectTypeConstants.CLIENT,
                GenericI18Enum.FORM_LAST_UPDATED_TIME, DateParam("lastupdatedtime", "m_client", "lastUpdatedTime"))

        @JvmField val p_anyCity = CacheParamMapper.register(ProjectTypeConstants.CLIENT,
                ClientI18nEnum.FORM_ANY_CITY, CompositionStringParam("anyCity",
                StringParam("", "m_client", "city"),
                StringParam("", "m_client", "shippingCity")))

        @JvmField val p_anyPhone = CacheParamMapper.register(ProjectTypeConstants.CLIENT, ClientI18nEnum.FORM_ANY_PHONE,
                CompositionStringParam("anyPhone",
                        StringParam("", "m_client", "alternatePhone"),
                        StringParam("", "m_client", "phoneOffice")))
    }
}