package com.mycollab.core.arguments;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ValuedBean implements Cloneable, Serializable {
    private static final long serialVersionUID = 1L;

    @JsonIgnore
    @NotBindable
    private boolean selected = false;

    @JsonIgnore
    @NotBindable
    private Object extraData;

    public Object getExtraData() {
        return extraData;
    }

    public void setExtraData(Object extraData) {
        this.extraData = extraData;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Object copy() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
