package com.mycollab.module.project.domain;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class SimpleStandupReport extends StandupReportWithBLOBs {
    private static final long serialVersionUID = 1L;

    private String logByAvatarId;
    private String logByFullName;

    public String getLogByAvatarId() {
        return logByAvatarId;
    }

    public void setLogByAvatarId(String logByAvatarId) {
        this.logByAvatarId = logByAvatarId;
    }

    public String getLogByFullName() {
        return logByFullName;
    }

    public void setLogByFullName(String logByFullName) {
        this.logByFullName = logByFullName;
    }
}
