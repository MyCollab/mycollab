package com.esofthead.mycollab.mobile.ui;

import java.io.Serializable;

import com.vaadin.ui.Field;
import com.vaadin.ui.Layout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 */
public interface IFormLayoutFactory extends Serializable {
	Layout getLayout();

	boolean attachField(Object propertyId, Field<?> field);
}
