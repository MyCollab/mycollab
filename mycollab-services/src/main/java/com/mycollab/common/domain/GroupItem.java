package com.mycollab.common.domain;

import com.mycollab.core.arguments.ValuedBean;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class GroupItem extends ValuedBean {
    private static final long serialVersionUID = 1L;

    private String groupid;
    private String groupname;
    private Double value;
    private String extraValue;
    private Integer countNum;

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public int getCountNum() {
        return countNum;
    }

    public void setCountNum(Integer countNum) {
        this.countNum = countNum;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getExtraValue() {
        return extraValue;
    }

    public void setExtraValue(String extraValue) {
        this.extraValue = extraValue;
    }
}
