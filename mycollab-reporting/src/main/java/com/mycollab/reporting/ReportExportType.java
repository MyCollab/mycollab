package com.mycollab.reporting;

import com.mycollab.core.MyCollabException;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public enum ReportExportType {
    CSV, EXCEL, PDF, DOCX;

    public String getDefaultFileName() {
        switch (this) {
            case CSV:
                return "export.csv";
            case PDF:
                return "export.pdf";
            case EXCEL:
                return "export.xlsx";
            case DOCX:
                return "docx";
            default:
                throw new MyCollabException("Do not support export type " + this);
        }
    }
}
