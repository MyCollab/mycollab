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
package com.esofthead.mycollab.module.file;

/**
 * @author MyCollab Ltd
 * @since 5.0.10
 */
public class PathUtils {
    public static String buildPath(Integer sAccountId, String objectPath) {
        return ((sAccountId == null) ? "" : sAccountId + "/") + objectPath;
    }

    public static String buildLogoPath(Integer sAccountId, String logoFileName, Integer logoSize) {
        return String.format("%s/.assets/%s_%d.png", sAccountId, logoFileName, logoSize);
    }

    public static String buildFavIconPath(Integer sAccountId, String favIconFileName) {
        return String.format("%s/.assets/%s.ico", sAccountId, favIconFileName);
    }
}
