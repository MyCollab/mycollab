package com.esofthead.mycollab.schedule.email;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.esofthead.mycollab.schedule.email.format.DefaultFieldFormat;
import com.esofthead.mycollab.schedule.email.format.FieldFormat;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class ItemFieldMapper {
	protected Map<String, FieldFormat> fieldNameMap = new LinkedHashMap<String, FieldFormat>();

	public void put(String fieldname, String displayName) {
		fieldNameMap.put(fieldname, new DefaultFieldFormat(fieldname,
				displayName));
	}

	public void put(String fieldname, FieldFormat format) {
		fieldNameMap.put(fieldname, format);
	}

	public boolean hasField(String fieldName) {
		return fieldNameMap.containsKey(fieldName);
	}

	public FieldFormat getFieldLabel(String fieldName) {
		return fieldNameMap.get(fieldName);
	}

	public Set<String> keySet() {
		return fieldNameMap.keySet();
	}
}
