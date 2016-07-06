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
