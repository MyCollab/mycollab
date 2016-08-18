/**
 * This file is part of mycollab-app-community.
 *
 * mycollab-app-community is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-app-community is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-app-community.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.server;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class DefaultServerRunner {

    public static void main(String[] args) throws Exception {
        new CommunityJettyBaseRunner().run(args);
    }
}
