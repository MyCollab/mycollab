/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.reporting.generator;

import com.mycollab.reporting.ReportStyles;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.definition.expression.DRIExpression;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.hyperLink;

/**
 * @author MyCollab Ltd.
 * @since 4.3.1
 */
public class HyperlinkBuilderGenerator implements ComponentBuilderGenerator {
    private DRIExpression title;
    private DRIExpression href;
    private StyleBuilder style;

    public HyperlinkBuilderGenerator(DRIExpression title, DRIExpression href) {
        this.title = title;
        this.href = href;
    }

    public DRIExpression getTitle() {
        return title;
    }

    public DRIExpression getHref() {
        return href;
    }

    public StyleBuilder getStyle() {
        return style;
    }

    public HyperlinkBuilderGenerator setStyle(StyleBuilder style) {
        this.style = style;
        return this;
    }

    @Override
    public ComponentBuilder getCompBuilder(ReportStyles reportStyles) {
        ComponentBuilder compBuilder = cmp.text(title).setHyperLink(hyperLink(href)).setStyle(reportStyles.getUnderlineStyle());
        if (style != null) {
            compBuilder.setStyle(style);
        }
        return compBuilder;
    }
}
