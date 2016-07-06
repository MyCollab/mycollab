/**
 * This file is part of mycollab-core.
 *
 * mycollab-core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-core.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.core;

/**
 * @author MyCollab Ltd
 * @since 5.1.3
 */
public class NewUpdateAvailableNotification extends AbstractNotification {
    private String version;
    private String autoDownloadLink;
    private String manualDownloadLink;
    private String installerFile;

    public NewUpdateAvailableNotification(String version, String autoDownloadLink, String manualDownloadLink, String installerFile) {
        super(SCOPE_GLOBAL, NEWS);
        this.version = version;
        this.autoDownloadLink = autoDownloadLink;
        this.manualDownloadLink = manualDownloadLink;
        this.installerFile = installerFile;
    }

    public String getVersion() {
        return version;
    }

    public String getAutoDownloadLink() {
        return autoDownloadLink;
    }

    public String getManualDownloadLink() {
        return manualDownloadLink;
    }

    public String getInstallerFile() {
        return installerFile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NewUpdateAvailableNotification)) return false;

        NewUpdateAvailableNotification that = (NewUpdateAvailableNotification) o;

        if (!version.equals(that.version)) return false;
        if (!autoDownloadLink.equals(that.autoDownloadLink)) return false;
        if (!manualDownloadLink.equals(that.manualDownloadLink)) return false;
        return !(installerFile != null ? !installerFile.equals(that.installerFile) : that.installerFile != null);

    }

    @Override
    public int hashCode() {
        int result = version.hashCode();
        result = 31 * result + autoDownloadLink.hashCode();
        result = 31 * result + manualDownloadLink.hashCode();
        result = 31 * result + (installerFile != null ? installerFile.hashCode() : 0);
        return result;
    }
}
