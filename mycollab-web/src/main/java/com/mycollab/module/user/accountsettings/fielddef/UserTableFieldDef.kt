package com.mycollab.module.user.accountsettings.fielddef

import com.mycollab.common.TableViewField
import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.module.user.accountsettings.localization.UserI18nEnum
import com.mycollab.module.user.domain.SimpleUser
import com.mycollab.module.user.domain.User
import com.mycollab.vaadin.web.ui.WebUIConstants

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object UserTableFieldDef {
    @JvmField
    val displayName = TableViewField(GenericI18Enum.FORM_NAME, SimpleUser.Field.displayName.name, WebUIConstants.TABLE_EX_LABEL_WIDTH)

    @JvmField
    val roleName = TableViewField(UserI18nEnum.FORM_ROLE, SimpleUser.Field.roleid.name, WebUIConstants.TABLE_EX_LABEL_WIDTH)

    @JvmField
    val email = TableViewField(GenericI18Enum.FORM_EMAIL, User.Field.email.name, WebUIConstants.TABLE_X_LABEL_WIDTH)

    @JvmField
    val birthday = TableViewField(UserI18nEnum.FORM_BIRTHDAY, User.Field.dateofbirth.name, WebUIConstants.TABLE_DATE_WIDTH)

    @JvmField
    val officePhone = TableViewField(UserI18nEnum.FORM_WORK_PHONE, User.Field.workphone.name, WebUIConstants.TABLE_M_LABEL_WIDTH)

    @JvmField
    val homePhone = TableViewField(UserI18nEnum.FORM_HOME_PHONE, User.Field.homephone.name, WebUIConstants.TABLE_M_LABEL_WIDTH)

    @JvmField
    val company = TableViewField(UserI18nEnum.FORM_COMPANY, User.Field.company.name, WebUIConstants.TABLE_M_LABEL_WIDTH)
}