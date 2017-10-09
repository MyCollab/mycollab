package com.mycollab.module.tracker.domain;

public class SimpleVersion extends Version {
    private static final long serialVersionUID = 1L;

    private Integer numOpenBugs;

    private Integer numBugs;

    private String projectName;

    public Integer getNumOpenBugs() {
        return numOpenBugs;
    }

    public void setNumOpenBugs(Integer numOpenBugs) {
        this.numOpenBugs = numOpenBugs;
    }

    public Integer getNumBugs() {
        return numBugs;
    }

    public void setNumBugs(Integer numBugs) {
        this.numBugs = numBugs;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public enum Field {
        numOpenBugs, numBugs;

        public boolean equalTo(Object value) {
            return name().equals(value);
        }
    }
}
