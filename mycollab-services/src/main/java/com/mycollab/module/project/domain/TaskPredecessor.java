package com.mycollab.module.project.domain;

/**
 * @author MyCollab Ltd
 * @since 5.1.3
 */
public class TaskPredecessor extends Predecessor {
    public static final String SS = "SS";
    public static final String FS = "FS";
    public static final String FF = "FF";
    public static final String SF = "SF";

    private Integer ganttIndex;

    public Integer getGanttIndex() {
        return ganttIndex;
    }

    public void setGanttIndex(Integer ganttIndex) {
        this.ganttIndex = ganttIndex;
    }
}
