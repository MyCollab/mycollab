/**
 * mycollab-services-community - Parent pom providing dependency and plugin management for applications
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
package com.mycollab.core.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.mycollab.security.PermissionMap;

public class JSonDeSerializerTest {
	@Test
	public void testSerializeArray() {
		String[][] twoArr = {{"Nguyen", "Hai"}, {"eSoftHead", "MyCollab"}};
		String json = JsonDeSerializer.toJson(twoArr);

		String[][] newVal = JsonDeSerializer.fromJson(json, String[][].class);
		assertThat(newVal.length).isEqualTo(2);
		assertThat(newVal[0][0]).isEqualTo("Nguyen");

	}

	@Test
	public void testSerializePermissionMap() {
		PermissionMap map = new PermissionMap();
		map.addPath("a", 1);
		map.addPath("b", 2);

		String json = JsonDeSerializer.toJson(map);

		PermissionMap permissionMap = JsonDeSerializer.fromJson(json, PermissionMap.class);
		assertThat(permissionMap.get("a")).isEqualTo(new Integer(1));
	}
}
