package com.mycollab.form.view.builder.type;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class TextDynaField extends AbstractDynaField {
    private int maxLength = Integer.MAX_VALUE;

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }
}
