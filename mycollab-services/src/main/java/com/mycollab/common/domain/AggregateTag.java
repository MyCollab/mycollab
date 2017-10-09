package com.mycollab.common.domain;

/**
 * @author MyCollab Ltd
 * @since 5.2.9
 */
public class AggregateTag {
    private String name;
    private Integer count;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
