package com.esofthead.mycollab.reporting;

import net.sf.dynamicreports.report.definition.ReportParameters;

import com.esofthead.mycollab.module.user.domain.User;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1.2
 * 
 */
public class AssigneeExpression extends AbstractFieldExpression {
	private static final long serialVersionUID = 1L;

	public AssigneeExpression(String field) {
		super(field);
	}

	@Override
	public String evaluate(ReportParameters reportParameters) {
		String stringValue = reportParameters.getFieldValue(field).toString();
		UserService service = ApplicationContextUtil
				.getSpringBean(UserService.class);
		User user = service.findUserByUserName(stringValue);
		if (user != null) {
			return user.getFirstname()
					+ ((user.getMiddlename() != null) ? " "
							+ user.getMiddlename() : "") + " "
					+ user.getLastname();
		} else {
			return stringValue;
		}
	}

}
