package com.esofthead.mycollab.mobile.ui;

import java.io.Serializable;

/**
 * 
 * @author MyCollab Ltd.
 * @since 3.0
 * 
 * @param <B>
 */
public interface IBeanFieldGroupFieldFactory<B> extends Serializable {

	void setBean(B bean);

	void commit();
}
