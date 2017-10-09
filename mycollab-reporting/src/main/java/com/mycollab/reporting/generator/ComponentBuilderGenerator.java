package com.mycollab.reporting.generator;

import com.mycollab.reporting.ReportStyles;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;

/**
 * @author MyCollab Ltd
 * @since 5.2.12
 */
public interface ComponentBuilderGenerator {
    ComponentBuilder getCompBuilder(ReportStyles reportStyles);
}
