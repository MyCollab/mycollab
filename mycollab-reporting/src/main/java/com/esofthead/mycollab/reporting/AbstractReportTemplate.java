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

import com.esofthead.mycollab.reporting.generator.ComponentBuilderGenerator;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.LineBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;

import java.awt.*;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;

/**
 * @author MyCollab Ltd.
 * @since 4.1.2
 */
public abstract class AbstractReportTemplate {
    private Color borderColor = new Color(233, 233, 233);
    private Color metaColor = new Color(153, 153, 153);

    private StyleBuilder rootStyle;
    private StyleBuilder boldStyle;
    private StyleBuilder italicStyle;
    private StyleBuilder underlineStyle;
    private StyleBuilder boldCenteredStyle;
    private StyleBuilder h2Style;
    private StyleBuilder h3Style;
    private StyleBuilder columnTitleStyle;
    private StyleBuilder formCaptionStyle;
    private StyleBuilder borderStyle;
    private StyleBuilder metaInfoStyle;

    public AbstractReportTemplate() {
        rootStyle = stl.style().setPadding(4);
        boldStyle = stl.style(rootStyle).bold();
        italicStyle = stl.style(rootStyle).italic();
        underlineStyle = stl.style(rootStyle).underline();
        boldCenteredStyle = stl.style(boldStyle).setTextAlignment(HorizontalTextAlignment.LEFT, VerticalTextAlignment.MIDDLE);
        borderStyle = stl.style(rootStyle).setBorder(stl.pen1Point().setLineColor(borderColor));
        metaInfoStyle = stl.style(rootStyle).setForegroundColor(metaColor);
        formCaptionStyle = stl.style(rootStyle).setForegroundColor(metaColor)
                .setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT).setVerticalTextAlignment(VerticalTextAlignment.TOP);
        h2Style = stl.style(rootStyle).setFontSize(18);
        h3Style = stl.style(rootStyle).setFontSize(14);

        columnTitleStyle = stl.style(rootStyle).setVerticalTextAlignment(VerticalTextAlignment.MIDDLE)
                .setHorizontalTextAlignment(HorizontalTextAlignment.LEFT).setBorder(stl.pen1Point())
                .setBackgroundColor(Color.LIGHT_GRAY);
    }

    public StyleBuilder getUnderlineStyle() {
        return underlineStyle;
    }

    public StyleBuilder getBoldCenteredStyle() {
        return boldCenteredStyle;
    }

    public StyleBuilder getItalicStyle() {
        return italicStyle;
    }

    public StyleBuilder getBorderStyle() {
        return borderStyle;
    }

    public StyleBuilder getMetaInfoStyle() {
        return metaInfoStyle;
    }

    public StyleBuilder getFormCaptionStyle() {
        return formCaptionStyle;
    }

    public StyleBuilder getH2Style() {
        return h2Style;
    }

    public StyleBuilder getH3Style() {
        return h3Style;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public StyleBuilder getColumnTitleStyle() {
        return columnTitleStyle;
    }

    public StyleBuilder getRootStyle() {
        return rootStyle;
    }

    public ComponentBuilder buildCompBuilder(ComponentBuilderGenerator value) {
        return value.getCompBuilder(this);
    }

    public LineBuilder line() {
        return cmp.line().setPen(stl.pen().setLineColor(borderColor));
    }
}
