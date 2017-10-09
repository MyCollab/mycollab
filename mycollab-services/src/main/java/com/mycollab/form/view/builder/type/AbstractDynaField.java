package com.mycollab.form.view.builder.type;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class AbstractDynaField implements Comparable<AbstractDynaField> {
    public static final String TEXT_FIELD_1 = "text1";
    public static final String TEXT_FIELD_2 = "text2";
    public static final String TEXT_FIELD_3 = "text3";
    public static final String TEXT_FIELD_4 = "text4";
    public static final String TEXT_FIELD_5 = "text5";

    public static final String[] TEXT_FIELD_ARR = {TEXT_FIELD_1, TEXT_FIELD_2,
            TEXT_FIELD_3, TEXT_FIELD_4, TEXT_FIELD_5};

    public static final String TEXTAREA_FIELD_1 = "textarea1";
    public static final String TEXTAREA_FIELD_2 = "textarea2";
    public static final String TEXTAREA_FIELD_3 = "textarea3";
    public static final String TEXTAREA_FIELD_4 = "textarea4";
    public static final String TEXTAREA_FIELD_5 = "textarea5";

    @JsonIgnore
    private int fieldIndex;

    @JsonIgnore
    private String fieldName;

    @JsonIgnore
    private Enum displayName;

    @JsonIgnore
    private Enum contextHelp;

    @JsonIgnore
    private Boolean isMandatory = false;

    @JsonIgnore
    private Boolean isRequired = false;

    @JsonIgnore
    private Boolean isCustom = false;

    @JsonIgnore
    private Boolean isColSpan = false;

    @JsonIgnore
    private DynaSection ownSection;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Enum getContextHelp() {
        return contextHelp;
    }

    public void setContextHelp(Enum contextHelp) {
        this.contextHelp = contextHelp;
    }

    public int getFieldIndex() {
        return fieldIndex;
    }

    public void setFieldIndex(int fieldIndex) {
        this.fieldIndex = fieldIndex;
    }

    public Boolean isRequired() {
        return isRequired;
    }

    public void setRequired(Boolean isRequired) {
        this.isRequired = isRequired;
    }

    public DynaSection getOwnSection() {
        return ownSection;
    }

    public void setOwnSection(DynaSection ownSection) {
        this.ownSection = ownSection;
    }

    public Enum getDisplayName() {
        return displayName;
    }

    public void setDisplayName(Enum displayName) {
        this.displayName = displayName;
    }

    public Boolean isCustom() {
        return isCustom;
    }

    public void setCustom(Boolean isCustom) {
        this.isCustom = isCustom;
    }

    public Boolean isMandatory() {
        return isMandatory;
    }

    public void setMandatory(Boolean isMandatory) {
        this.isMandatory = isMandatory;
    }

    public Boolean isColSpan() {
        return isColSpan;
    }

    public void setColSpan(Boolean isColSpan) {
        this.isColSpan = isColSpan;
    }

    @Override
    public int compareTo(AbstractDynaField paramT) {
        return (this.fieldIndex - paramT.fieldIndex);
    }
}
