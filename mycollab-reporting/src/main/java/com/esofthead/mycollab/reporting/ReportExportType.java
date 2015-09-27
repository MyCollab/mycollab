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

import com.esofthead.mycollab.core.MyCollabException;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public enum ReportExportType {
    CSV, EXCEL, PDF;

    public String getDefaultFileName() {
        switch (this) {
            case CSV:
                return "export.csv";
            case PDF:
                return "export.pdf";
            case EXCEL:
                return "export.xlsx";
            default:
                throw new MyCollabException("Do not support export type " + this);
        }
    }
}
