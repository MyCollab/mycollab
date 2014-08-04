package com.esofthead.mycollab.module.project.view.risk;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.esofthead.mycollab.module.project.i18n.RiskI18nEnum;
import com.esofthead.mycollab.module.project.ui.format.ProjectMemberHistoryFieldFormat;
import com.esofthead.mycollab.utils.FieldGroupFomatter;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.3.3
 *
 */
public class RiskFormatter extends FieldGroupFomatter {
	public static final RiskFormatter instance;

	static {
		instance = new RiskFormatter();
	}

	public RiskFormatter() {
		super();

		this.generateFieldDisplayHandler("riskname", RiskI18nEnum.FORM_NAME);
		this.generateFieldDisplayHandler("description",
				RiskI18nEnum.FORM_DESCRIPTION);
		this.generateFieldDisplayHandler("raisedbyuser",
				RiskI18nEnum.FORM_RAISED_BY,
				new ProjectMemberHistoryFieldFormat());
		this.generateFieldDisplayHandler("assigntouser",
				GenericI18Enum.FORM_ASSIGNEE,
				new ProjectMemberHistoryFieldFormat());
		this.generateFieldDisplayHandler("consequence",
				RiskI18nEnum.FORM_CONSEQUENCE);

		this.generateFieldDisplayHandler("datedue", RiskI18nEnum.FORM_DATE_DUE,
				FieldGroupFomatter.DATE_FIELD);
		this.generateFieldDisplayHandler("probalitity",
				RiskI18nEnum.FORM_PROBABILITY);
		this.generateFieldDisplayHandler("status", RiskI18nEnum.FORM_STATUS,
				new I18nHistoryFieldFormat(StatusI18nEnum.class));
		this.generateFieldDisplayHandler("level", RiskI18nEnum.FORM_RATING);
		this.generateFieldDisplayHandler("response", RiskI18nEnum.FORM_RESPONSE);
	}
}
