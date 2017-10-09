package com.mycollab.module.tracker.domain;

/**
 * @author MyCollab Ltd
 * @since 5.1.0
 */
public class SimpleRelatedBug {
    public static final String AFFVERSION = "AffVersion";
    public static final String FIXVERSION = "FixVersion";
    public static final String COMPONENT = "Component";

    private Integer id;
    private BugWithBLOBs relatedBug;
    private String relatedType;
    private Boolean isRelated;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BugWithBLOBs getRelatedBug() {
        return relatedBug;
    }

    public void setRelatedBug(BugWithBLOBs relatedBug) {
        this.relatedBug = relatedBug;
    }

    public String getRelatedType() {
        return relatedType;
    }

    public void setRelatedType(String relatedType) {
        this.relatedType = relatedType;
    }

    public Boolean getRelated() {
        return isRelated;
    }

    public void setRelated(Boolean related) {
        isRelated = related;
    }
}
