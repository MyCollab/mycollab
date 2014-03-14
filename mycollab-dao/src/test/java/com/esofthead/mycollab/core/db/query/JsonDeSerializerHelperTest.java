package com.esofthead.mycollab.core.db.query;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class JsonDeSerializerHelperTest {

	@Test
	public void testDeSerialize() {
		SearchFieldInfo fieldInfo1 = new SearchFieldInfo("a", new StringParam(
				"1", "a", "b", "c"), "x", new Integer(1));

		SearchFieldInfo fieldInfo2 = new SearchFieldInfo("b", new NumberParam(
				"2", "x", "y", "z"), "a", new Integer(2));

		SearchFieldInfo fieldInfo3 = new SearchFieldInfo("c",
				new PropertyListParam("1", "a", "x", "c"), "x", Arrays.asList(
						"1", "2"));

		List<SearchFieldInfo> list = Arrays.asList(fieldInfo1, fieldInfo2,
				fieldInfo3);
		String jsonText = JsonDeSerializerHelper.toJson(list);
		System.out.println("Json text: " + jsonText);

		List<SearchFieldInfo> list2 = JsonDeSerializerHelper.fromJson(jsonText);
		Assert.assertEquals(3, list2.size());

		SearchFieldInfo newFieldInfo3 = list2.get(2);
		Assert.assertEquals(2, ((Collection) newFieldInfo3.getValue()).size());
	}
}
