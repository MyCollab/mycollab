/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.reporting;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.hyperLink;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.definition.expression.DRIExpression;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1.2
 * 
 */
public class ComponentBuilderWrapper {
	public static ComponentBuilder buildHyperLink(DRIExpression titleExpr,
			DRIExpression hrefExpr) {
		return cmp
				.text(titleExpr)
				.setHyperLink(hyperLink(hrefExpr))
				.setStyle(
						ReportTemplateFactory.getTemplate(null).underlineStyle);
	}

	public static ComponentBuilder buildDateTimeText(DateTimeExpression dateExpr) {
		return cmp.text(dateExpr);
	}

	public static ComponentBuilder buildDateText(DateExpression dateExpr) {
		return cmp.text(dateExpr);
	}

	public static ComponentBuilder buildEmail(MailExpression mailExpr) {
		return cmp.text(mailExpr);
	}
}
