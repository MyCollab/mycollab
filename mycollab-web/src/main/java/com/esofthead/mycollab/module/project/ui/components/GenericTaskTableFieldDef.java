package com.esofthead.mycollab.module.project.ui.components;

import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.table.TableViewField;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class GenericTaskTableFieldDef {
	public static TableViewField name = new TableViewField("", "name",
			UIConstants.TABLE_EX_LABEL_WIDTH);

	public static TableViewField assignUser = new TableViewField("",
			"assignUser", UIConstants.TABLE_EX_LABEL_WIDTH);
}
