package com.mycollab.module.crm.fielddef

import com.mycollab.common.TableViewField
import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.module.crm.i18n.ContactI18nEnum
import com.mycollab.vaadin.web.ui.WebUIConstants

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object ContactTableFieldDef {
    @JvmField
    val selected = TableViewField(null, "selected", WebUIConstants.TABLE_CONTROL_WIDTH)

    @JvmField
    val action = TableViewField(null, "id", WebUIConstants.TABLE_ACTION_CONTROL_WIDTH)

    @JvmField
    val name = TableViewField(GenericI18Enum.FORM_NAME, "contactName", WebUIConstants.TABLE_X_LABEL_WIDTH)

    @JvmField
    val account = TableViewField(ContactI18nEnum.FORM_ACCOUNTS, "accountName", WebUIConstants.TABLE_EX_LABEL_WIDTH)

    @JvmField
    val decisionRole = TableViewField(ContactI18nEnum.FORM_DECISION_ROLE, "decisionRole", WebUIConstants.TABLE_M_LABEL_WIDTH)

    @JvmField
    val title = TableViewField(ContactI18nEnum.FORM_TITLE, "title", WebUIConstants.TABLE_M_LABEL_WIDTH)

    @JvmField
    val department = TableViewField(ContactI18nEnum.FORM_DEPARTMENT, "department", WebUIConstants.TABLE_M_LABEL_WIDTH)

    @JvmField
    val email = TableViewField(GenericI18Enum.FORM_EMAIL, "email", WebUIConstants.TABLE_EMAIL_WIDTH)

    @JvmField
    val assistant = TableViewField(ContactI18nEnum.FORM_ASSISTANT, "assistant", WebUIConstants.TABLE_X_LABEL_WIDTH)

    @JvmField
    val assistantPhone = TableViewField(ContactI18nEnum.FORM_ASSISTANT_PHONE, "assistantphone", WebUIConstants.TABLE_X_LABEL_WIDTH)

    @JvmField
    val phoneOffice = TableViewField(ContactI18nEnum.FORM_OFFICE_PHONE, "officephone", WebUIConstants.TABLE_M_LABEL_WIDTH)

    @JvmField
    val mobile = TableViewField(ContactI18nEnum.FORM_MOBILE, "mobile", WebUIConstants.TABLE_M_LABEL_WIDTH)

    @JvmField
    val fax = TableViewField(ContactI18nEnum.FORM_FAX, "fax", WebUIConstants.TABLE_M_LABEL_WIDTH)

    @JvmField
    val birthday = TableViewField(ContactI18nEnum.FORM_BIRTHDAY, "birthday", WebUIConstants.TABLE_DATE_WIDTH)

    @JvmField
    val isCallable = TableViewField(ContactI18nEnum.FORM_IS_CALLABLE, "iscallable", WebUIConstants.TABLE_S_LABEL_WIDTH)

    @JvmField
    val assignUser = TableViewField(GenericI18Enum.FORM_ASSIGNEE, "assignUserFullName", WebUIConstants.TABLE_X_LABEL_WIDTH)
}