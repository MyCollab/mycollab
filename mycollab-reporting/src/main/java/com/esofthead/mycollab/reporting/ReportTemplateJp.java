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

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.hyperLink;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.template;

import java.awt.Color;
import java.util.Locale;

import net.sf.dynamicreports.report.builder.HyperLinkBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.style.PaddingBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.VerticalAlignment;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1.2
 * 
 */
class ReportTemplateJp extends AbstractReportTemplate {

	ReportTemplateJp() {
		rootStyle = stl.style().setPadding(2).setFontName("Takao");
		boldStyle = stl.style(rootStyle).bold();
		italicStyle = stl.style(rootStyle).italic();
		underlineStyle = stl.style(rootStyle).underline();
		boldCenteredStyle = stl.style(boldStyle).setAlignment(
				HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);

		PaddingBuilder padding = stl.padding();
		padding.setLeft(8);
		boldLeftStyle = stl
				.style(boldStyle)
				.setAlignment(HorizontalAlignment.LEFT,
						VerticalAlignment.MIDDLE).setPadding(padding);

		bold12CenteredStyle = stl.style(boldCenteredStyle).setFontSize(12);
		bold18CenteredStyle = stl.style(boldCenteredStyle).setFontSize(18);
		bold22CenteredStyle = stl.style(boldCenteredStyle).setFontSize(22);
		bold12TitleStyle = stl.style(boldLeftStyle).setFontSize(12);
		bold18TitleStyle = stl.style(boldLeftStyle).setFontSize(18);

		columnStyle = stl.style(rootStyle).setVerticalAlignment(
				VerticalAlignment.MIDDLE);
		columnTitleStyle = stl.style(columnStyle).setBorder(stl.pen1Point())
				.setHorizontalAlignment(HorizontalAlignment.CENTER)
				.setBackgroundColor(Color.LIGHT_GRAY).bold();
		groupStyle = stl.style(boldStyle).setHorizontalAlignment(
				HorizontalAlignment.LEFT);
		subtotalStyle = stl.style(boldStyle).setTopBorder(stl.pen1Point());

		StyleBuilder crosstabGroupStyle = stl.style(columnTitleStyle);
		StyleBuilder crosstabGroupTotalStyle = stl.style(columnTitleStyle)
				.setBackgroundColor(new Color(170, 170, 170));
		StyleBuilder crosstabGrandTotalStyle = stl.style(columnTitleStyle)
				.setBackgroundColor(new Color(140, 140, 140));
		StyleBuilder crosstabCellStyle = stl.style(columnStyle).setBorder(
				stl.pen1Point());

		reportTemplateBuilder = template().setLocale(Locale.ENGLISH)
				.setColumnStyle(columnStyle)
				.setColumnTitleStyle(columnTitleStyle)
				.setGroupStyle(groupStyle).setGroupTitleStyle(groupStyle)
				.setSubtotalStyle(subtotalStyle).highlightDetailEvenRows()
				.crosstabHighlightEvenRows()
				.setCrosstabGroupStyle(crosstabGroupStyle)
				.setCrosstabGroupTotalStyle(crosstabGroupTotalStyle)
				.setCrosstabGrandTotalStyle(crosstabGrandTotalStyle)
				.setCrosstabCellStyle(crosstabCellStyle);
	}

	/**
	 * Creates custom component which is possible to add to any report band
	 * component
	 */
	public ComponentBuilder<?, ?> createTitleComponent(String label) {
		HyperLinkBuilder link = hyperLink("http://www.mycollab.com");
		ComponentBuilder<?, ?> dynamicReportsComponent = cmp.horizontalList(
				cmp.image(
						ReportTemplateFactory.class.getClassLoader()
								.getResourceAsStream("images/logo.png"))
						.setFixedDimension(150, 28), cmp.verticalList(
						cmp.text(label)
								.setStyle(bold22CenteredStyle)
								.setHorizontalAlignment(
										HorizontalAlignment.LEFT),
						cmp.text("http://www.mycollab.com")
								.setStyle(italicStyle).setHyperLink(link)));

		return cmp.horizontalList().add(dynamicReportsComponent).newRow()
				.add(cmp.line()).newRow().add(cmp.verticalGap(10));
	}
}
