package com.mycollab.module.page.domain;

import java.io.Serializable;
import java.util.Calendar;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
public class PageVersion implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;

    private int index;

    private Calendar createdTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Calendar getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Calendar createdTime) {
        this.createdTime = createdTime;
    }
}
