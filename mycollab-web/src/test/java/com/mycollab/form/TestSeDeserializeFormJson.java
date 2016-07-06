/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.form;

import org.junit.Assert;
import org.junit.Test;

import com.mycollab.core.utils.JsonDeSerializer;
import com.mycollab.form.view.builder.AbstractDynaFieldBuilder;
import com.mycollab.form.view.builder.DynaSectionBuilder;
import com.mycollab.form.view.builder.TextDynaFieldBuilder;
import com.mycollab.form.view.builder.type.AbstractDynaField;
import com.mycollab.form.view.builder.type.TextDynaField;

public class TestSeDeserializeFormJson {

	@Test
	public void testFromJsonWithExcludeProps() {
		DynaSectionBuilder builder = new DynaSectionBuilder().header("example");

		AbstractDynaFieldBuilder<TextDynaField> val1Builder = new TextDynaFieldBuilder()
				.maxLength(22).fieldIndex(1).fieldName("field1")
				.displayName("Field 1");

		builder.fields(val1Builder);

		AbstractDynaField field = val1Builder.build();
		Assert.assertNotNull(field.getOwnSection());

		String expectedVal = "{\"maxLength\":22,\"mandatory\":false,\"required\":false,\"custom\":false,\"colSpan\":false}";
		Assert.assertEquals(expectedVal, JsonDeSerializer.toJson(field));
	}

	@Test
	public void testToJsonWithExcludeProps() {
		DynaSectionBuilder builder = new DynaSectionBuilder().header("example");

		AbstractDynaFieldBuilder<TextDynaField> val1Builder = new TextDynaFieldBuilder()
				.maxLength(22).fieldIndex(1).fieldName("field1")
				.displayName("Field 1");

		builder.fields(val1Builder);

		AbstractDynaField field = val1Builder.build();
		String jsonTxt = JsonDeSerializer.toJson(field);

		TextDynaField stringField = JsonDeSerializer.fromJson(jsonTxt, TextDynaField.class);
		Assert.assertEquals(22, stringField.getMaxLength());
		Assert.assertNull(stringField.getOwnSection());
	}
}
