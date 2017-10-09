package com.mycollab.form.view.builder.type;

import com.mycollab.core.MyCollabException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class DynaSection implements Comparable<DynaSection> {
    private Enum header;
    private int orderIndex;
    private boolean isDeletedSection = false;
    private LayoutType layoutType;
    private List<AbstractDynaField> fields = new ArrayList<>();
    private DynaForm parentForm;

    public Enum getHeader() {
        return header;
    }

    public void setHeader(Enum header) {
        this.header = header;
    }

    public LayoutType getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(LayoutType layoutType) {
        this.layoutType = layoutType;
    }

    public int getFieldCount() {
        return fields.size();
    }

    public DynaSection fields(AbstractDynaField... dynaFields) {
        for (AbstractDynaField dynaField: dynaFields) {
            fields.add(dynaField);
            dynaField.setOwnSection(this);
        }

        return this;
    }

    public AbstractDynaField getField(int index) {
        return fields.get(index);
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public boolean isDeletedSection() {
        return isDeletedSection;
    }

    public void setDeletedSection(boolean isDeletedSection) {
        this.isDeletedSection = isDeletedSection;
    }

    public enum LayoutType {
        ONE_COLUMN, TWO_COLUMN;

        public static LayoutType from(Integer value) {
            if (value == 1) {
                return ONE_COLUMN;
            } else if (value == 2) {
                return TWO_COLUMN;
            } else {
                throw new MyCollabException("Do not convert layout type from value " + value);
            }
        }

        public static Integer to(LayoutType type) {
            if (LayoutType.ONE_COLUMN == type) {
                return 1;
            } else {
                return 2;
            }
        }
    }

    public DynaForm getParentForm() {
        return parentForm;
    }

    public void setParentForm(DynaForm parentForm) {
        this.parentForm = parentForm;
    }

    @Override
    public int compareTo(DynaSection o) {
        return (orderIndex - o.orderIndex);
    }
}
