package com.mycollab.form.view.builder.type;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class IntDynaField extends AbstractDynaField {
    private int maxValue = Integer.MAX_VALUE;

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }
}
