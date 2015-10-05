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
package com.esofthead.mycollab.module.project.ui.components;

import com.esofthead.mycollab.common.domain.SimpleActivityStream;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.ui.format.*;
import com.esofthead.mycollab.utils.AuditLogPrinter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectAuditLogStreamGenerator {
    private static final Map<String, AuditLogPrinter> auditPrinters;

    static {
        auditPrinters = new HashMap<>();
        auditPrinters.put(ProjectTypeConstants.BUG, new AuditLogPrinter(BugFieldFormatter.instance()));
        auditPrinters.put(ProjectTypeConstants.TASK, new AuditLogPrinter(TaskFieldFormatter.instance()));
        auditPrinters.put(ProjectTypeConstants.MILESTONE, new AuditLogPrinter(MilestoneFieldFormatter.instance()));
        auditPrinters.put(ProjectTypeConstants.RISK, new AuditLogPrinter(RiskFieldFormatter.instance()));
        auditPrinters.put(ProjectTypeConstants.PROBLEM, new AuditLogPrinter(ProblemFieldFormatter.instance()));
        auditPrinters.put(ProjectTypeConstants.BUG_COMPONENT, new AuditLogPrinter(ComponentFieldFormatter.instance()));
        auditPrinters.put(ProjectTypeConstants.BUG_VERSION, new AuditLogPrinter(VersionFieldFormatter.instance()));
    }

    public static String generatorDetailChangeOfActivity(SimpleActivityStream activityStream) {

        if (activityStream.getAssoAuditLog() != null) {
            AuditLogPrinter auditLogHandler = auditPrinters.get(activityStream.getType());
            if (auditLogHandler != null) {
                return auditLogHandler.generateChangeSet(activityStream.getAssoAuditLog());
            }

        }
        return "";
    }
}
