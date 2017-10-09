package com.mycollab.reporting;

import com.mycollab.core.reporting.NotInReport;
import net.sf.dynamicreports.report.definition.datatype.DRIDataType;
import net.sf.dynamicreports.report.exception.DRException;

import java.lang.reflect.Field;

import static net.sf.dynamicreports.report.builder.DynamicReports.type;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class DRIDataTypeFactory {

    public static <T extends DRIDataType<?, ?>> T detectType(Field field) throws DRException {
        if (field.getAnnotation(NotInReport.class) != null) {
            return null;
        }

        String dataType = field.getType().getName();
        return type.detectType(dataType);
    }
}