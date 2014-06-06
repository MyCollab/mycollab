/**
 * This file is part of mycollab-reporting.
 *
 * mycollab-reporting is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-reporting is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-reporting.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.reporting;

import net.sf.dynamicreports.report.builder.ReportTemplateBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1.2
 * 
 */
public abstract class AbstractReportTemplate {
	protected StyleBuilder rootStyle;
	protected StyleBuilder boldStyle;
	protected StyleBuilder italicStyle;
	protected StyleBuilder underlineStyle;
	protected StyleBuilder boldCenteredStyle;
	protected StyleBuilder boldLeftStyle;
	protected StyleBuilder bold18CenteredStyle;
	protected StyleBuilder bold22CenteredStyle;
	protected StyleBuilder bold12TitleStyle;
	protected StyleBuilder bold18TitleStyle;
	protected StyleBuilder columnTitleStyle;
	protected StyleBuilder columnStyle;
	protected StyleBuilder bold12CenteredStyle;
	protected StyleBuilder groupStyle;
	protected StyleBuilder subtotalStyle;
	protected ReportTemplateBuilder reportTemplateBuilder;

	public abstract ComponentBuilder<?, ?> createTitleComponent(String label);

	public StyleBuilder getRootStyle() {
		return rootStyle;
	}

	public StyleBuilder getBoldStyle() {
		return boldStyle;
	}

	public StyleBuilder getItalicStyle() {
		return italicStyle;
	}

	public StyleBuilder getUnderlineStyle() {
		return underlineStyle;
	}

	public StyleBuilder getBoldCenteredStyle() {
		return boldCenteredStyle;
	}

	public StyleBuilder getBoldLeftStyle() {
		return boldLeftStyle;
	}

	public StyleBuilder getBold18CenteredStyle() {
		return bold18CenteredStyle;
	}

	public StyleBuilder getBold22CenteredStyle() {
		return bold22CenteredStyle;
	}

	public StyleBuilder getBold12TitleStyle() {
		return bold12TitleStyle;
	}

	public StyleBuilder getBold18TitleStyle() {
		return bold18TitleStyle;
	}

	public StyleBuilder getColumnTitleStyle() {
		return columnTitleStyle;
	}

	public StyleBuilder getColumnStyle() {
		return columnStyle;
	}

	public StyleBuilder getBold12CenteredStyle() {
		return bold12CenteredStyle;
	}

	public StyleBuilder getGroupStyle() {
		return groupStyle;
	}

	public StyleBuilder getSubtotalStyle() {
		return subtotalStyle;
	}

	public ReportTemplateBuilder getReportTemplateBuilder() {
		return reportTemplateBuilder;
	}
}
