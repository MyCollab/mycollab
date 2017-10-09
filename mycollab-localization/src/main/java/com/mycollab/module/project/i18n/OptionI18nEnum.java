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

    public static BugStatus[] bug_statuses = {BugStatus.Open, BugStatus.Verified, BugStatus.Resolved, BugStatus.ReOpen};

    public static BugSeverity[] bug_severities = {BugSeverity.Critical, BugSeverity.Major, BugSeverity.Minor, BugSeverity.Trivial};

    public static BugResolution[] bug_resolutions = {BugResolution.Fixed, BugResolution.Won_Fix, BugResolution.Duplicate,
            BugResolution.Invalid, BugResolution.CannotReproduce, BugResolution.InComplete};

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

    @BaseName("project-bug-status")
    @LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
    public enum BugStatus {
        Open, Verified, Resolved, ReOpen
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
