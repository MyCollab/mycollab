package com.mycollab.vaadin.ui;

import java.io.Serializable;

/**
 * @param <B>
 * @author MyCollab Ltd.
 * @since 3.0
 */
public interface IBeanFieldGroupFieldFactory<B> extends Serializable {

    void setBean(B bean);

    void setBuffered(boolean isBuffered);

    boolean commit();
}
