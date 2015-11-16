/**
 * This file is part of mycollab-config.
 *
 * mycollab-config is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-config is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-config.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.configuration;


import com.esofthead.mycollab.core.MyCollabVersion;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public abstract class MyCollabAssets {
    private static MyCollabAssets impl;

    static {
        if (SiteConfiguration.isDemandEdition()) {
            impl = new S3();
        } else {
            impl = new Local();
        }
    }

    protected abstract String generateAssetLink(String resourceId);

    public static String newAssetLink(String resourceId) {
        return impl.generateAssetLink(resourceId);
    }

    public static class S3 extends MyCollabAssets {

        @Override
        protected String generateAssetLink(String resourceId) {
            return SiteConfiguration.getCdnUrl() + resourceId;
        }
    }

    public static class Local extends MyCollabAssets {
        @Override
        protected String generateAssetLink(String resourceId) {
            return String.format("%s%s?v=%s", SiteConfiguration.getCdnUrl(), resourceId, MyCollabVersion.getVersion());
        }

    }
}
