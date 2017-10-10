/**
 * mycollab-dao - Parent pom providing dependency and plugin management for applications
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
package com.mycollab.security

/**
 * Access permission flag
 *
 * @author MyCollab Ltd
 * @since 1.0
 */
class AccessPermissionFlag : PermissionFlag() {
    companion object {
        @JvmField
        val NO_ACCESS = 0

        @JvmField
        val READ_ONLY = 1

        @JvmField
        val READ_WRITE = 2

        @JvmField
        val ACCESS = 4

        /**
         * Check whether `flag` implies read permission
         *
         * @param flag
         * @return true of `flag` implies read permission
         */
        fun canRead(flag: Int): Boolean {
            return flag and READ_ONLY == READ_ONLY
                    || flag and READ_WRITE == READ_WRITE
                    || flag and ACCESS == ACCESS
        }

        /**
         * Check whether `flag` implies write permission
         *
         * @param flag
         * @return true of `flag` implies write permission
         */
        fun canWrite(flag: Int): Boolean {
            return flag and READ_WRITE == READ_WRITE || flag and ACCESS == ACCESS
        }

        /**
         * Check whether `flag` implies access permission
         *
         * @param flag
         * @return true of `flag` implies access permission
         */
        fun canAccess(flag: Int): Boolean {
            return flag and ACCESS == ACCESS
        }
    }
}
