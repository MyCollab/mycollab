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
