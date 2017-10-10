/**
 * mycollab-services - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.ecm.domain;

import com.mycollab.core.arguments.NotBindable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Calendar;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class Resource implements Comparable<Resource> {

    @NotBindable
    private boolean selected = false;

    private String uuid = "";
    private String createdBy = "";
    private Calendar created;
    private String path = "";
    private String description;

    // length is Kilobyte value
    private Long size = 0L;
    private String createdUser;
    private String name;

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Calendar getCreated() {
        return created;
    }

    public void setCreated(Calendar created) {
        this.created = created;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        if (name == null) {
            int index = path.lastIndexOf("/");
            return path.substring(index + 1);
        } else {
            return name;
        }
    }

    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public int compareTo(Resource arg0) {
        if (this instanceof Folder && arg0 instanceof Content) {
            return -1;
        } else if (this instanceof Folder && arg0 instanceof Folder) {
            if (this.getCreated() != null && arg0.getCreated() != null) {
                return this.getCreated().getTime()
                        .compareTo(arg0.getCreated().getTime());
            } else {
                return this.getName().compareTo(arg0.getName());
            }
        } else {
            return 1;
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(1, 101).append(getPath()).build();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        Resource res = (Resource) obj;
        return new EqualsBuilder().append(getPath(), res.getPath()).build();
    }

    public boolean isExternalResource() {
        return ((this instanceof ExternalFolder) || (this instanceof ExternalContent));
    }
}
