/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.ecm.domain;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ExternalContent extends Content {
    private String storageName;

    private ExternalDrive externalDrive;

    private byte[] thumbnailBytes;

    public ExternalContent() {
        super();
    }

    public ExternalContent(String path) {
        super(path);
    }

    public String getStorageName() {
        return storageName;
    }

    public void setStorageName(String storageName) {
        this.storageName = storageName;
    }

    public ExternalDrive getExternalDrive() {
        return externalDrive;
    }

    public void setExternalDrive(ExternalDrive externalDrive) {
        this.externalDrive = externalDrive;
    }

    public byte[] getThumbnailBytes() {
        return thumbnailBytes;
    }

    public void setThumbnailBytes(byte[] thumbnailBytes) {
        this.thumbnailBytes = thumbnailBytes;
    }
}
