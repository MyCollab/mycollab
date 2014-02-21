package com.esofthead.mycollab.core.db.query;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class PropertyParam extends Param {
	public static String IS = "is";
	public static String IS_NOT = "isn't";

	public static String[] OPTIONS = { IS, IS_NOT };
	
	public PropertyParam(String id, String displayName) {
		super(id, displayName);
	}
}
