package com.esofthead.mycollab.module.project.view.problem;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.esofthead.mycollab.module.project.i18n.ProblemI18nEnum;
import com.esofthead.mycollab.module.project.ui.format.ProjectMemberHistoryFieldFormat;
import com.esofthead.mycollab.utils.FieldGroupFomatter;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.3.3
 *
 */
public class ProblemFormatter extends FieldGroupFomatter {
	public static final ProblemFormatter instance;

	static {
		instance = new ProblemFormatter();
	}

	public ProblemFormatter() {
		super();

		this.generateFieldDisplayHandler("issuename", ProblemI18nEnum.FORM_NAME);
		this.generateFieldDisplayHandler("description",
				GenericI18Enum.FORM_DESCRIPTION);
		this.generateFieldDisplayHandler("raisedbyuser",
				ProblemI18nEnum.FORM_RAISED_BY,
				new ProjectMemberHistoryFieldFormat());
		this.generateFieldDisplayHandler("assigntouser",
				GenericI18Enum.FORM_ASSIGNEE,
				new ProjectMemberHistoryFieldFormat());
		this.generateFieldDisplayHandler("impact", ProblemI18nEnum.FORM_IMPACT);

		this.generateFieldDisplayHandler("datedue",
				ProblemI18nEnum.FORM_DATE_DUE, FieldGroupFomatter.DATE_FIELD);
		this.generateFieldDisplayHandler("priority",
				ProblemI18nEnum.FORM_PRIORITY);
		this.generateFieldDisplayHandler("status", ProblemI18nEnum.FORM_STATUS,
				new I18nHistoryFieldFormat(StatusI18nEnum.class));
		this.generateFieldDisplayHandler("level", ProblemI18nEnum.FORM_RATING);
		this.generateFieldDisplayHandler("resolution",
				ProblemI18nEnum.FORM_RESOLUTION);
	}
}
