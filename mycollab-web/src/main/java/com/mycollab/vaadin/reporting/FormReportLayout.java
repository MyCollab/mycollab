package com.mycollab.vaadin.reporting;

import com.mycollab.form.view.builder.type.DynaForm;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author MyCollab Ltd
 * @since 5.2.11
 */
public class FormReportLayout {
    private String moduleName;
    private DynaForm dynaForm;
    private String titleField;
    private Set<String> excludeFields;

    public FormReportLayout(String moduleName, String titleField, DynaForm defaultForm, String... excludeFieldArr) {
        this.moduleName = moduleName;
        this.dynaForm = defaultForm;
        this.titleField = titleField;
        if (excludeFieldArr.length > 0) {
            this.excludeFields = new HashSet<>(Arrays.asList(excludeFieldArr));
        } else {
            this.excludeFields = new HashSet<>();
        }
    }

    public String getModuleName() {
        return moduleName;
    }

    public DynaForm getDynaForm() {
        return dynaForm;
    }

    public String getTitleField() {
        return titleField;
    }

    public Set<String> getExcludeFields() {
        return excludeFields;
    }
}
