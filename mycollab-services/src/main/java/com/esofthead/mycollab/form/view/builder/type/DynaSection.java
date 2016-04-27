/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.form.view.builder.type;

import com.esofthead.mycollab.core.MyCollabException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class DynaSection implements Comparable<DynaSection> {
    private String header;
    private int orderIndex;
    private boolean isDeletedSection = false;
    private LayoutType layoutType;
    private List<AbstractDynaField> fields = new ArrayList<>();
    private DynaForm parentForm;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
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

    public void addField(AbstractDynaField field) {
        fields.add(field);
        field.setOwnSection(this);
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
