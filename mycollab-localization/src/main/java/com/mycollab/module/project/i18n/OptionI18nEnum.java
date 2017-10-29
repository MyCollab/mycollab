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
package com.mycollab.module.project.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;
import com.mycollab.core.MyCollabException;

/**
 * @author MyCollab Ltd.
 * @since 4.3.3
 */
public class OptionI18nEnum {
    public static Priority[] priorities = {Priority.Urgent, Priority.High, Priority.Medium, Priority.Low, Priority.None};

    public static BugSeverity[] bug_severities = {BugSeverity.Critical, BugSeverity.Major, BugSeverity.Minor, BugSeverity.Trivial};

    public static InvoiceStatus[] invoiceStatuses = {InvoiceStatus.Paid,
            InvoiceStatus.Sent, InvoiceStatus.Scheduled};

    @BaseName("project-milestone-status")
    @LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
    public enum MilestoneStatus {
        Future, Closed, InProgress
    }

    @BaseName("project-invoice-status")
    @LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
    public enum InvoiceStatus {
        Paid, Sent, Scheduled, All
    }

    @BaseName("project-task-priority")
    @LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
    public enum Priority {
        Urgent, High, Medium, Low, None
    }

    @BaseName("project-bug-severity")
    @LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
    public enum BugSeverity {
        Critical, Major, Minor, Trivial
    }

    @BaseName("project-bug-resolution")
    @LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
    public enum BugResolution {
        Fixed,
        Won_Fix,
        Duplicate,
        Invalid,
        CannotReproduce,
        InComplete,
        None
    }

    @BaseName("project-bug-related")
    @LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
    public enum BugRelation {
        Related, Duplicated, Block, DependsOn, Duplicate, Relation;

        public Enum getReverse() {
            if (this == Duplicated) {
                return Duplicate;
            } else if (this == Related) {
                return Relation;
            } else if (this == Block) {
                return DependsOn;
            } else {
                throw new MyCollabException("Not support");
            }
        }
    }

    @BaseName("project-risk-probability")
    @LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
    public enum RiskProbability {
        Certain,
        Likely,
        Possible,
        Unlikely,
        Rare
    }

    @BaseName("project-risk-consequence")
    @LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
    public enum RiskConsequence {
        Catastrophic,
        Critical,
        Marginal,
        Negligible
    }
}
