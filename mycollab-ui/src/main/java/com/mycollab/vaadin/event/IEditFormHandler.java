package com.mycollab.vaadin.event;

import java.io.Serializable;

/**
 * @param <T>
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface IEditFormHandler<T> extends Serializable {
    /**
     * @param bean
     */
    void onSave(T bean);

    /**
     * @param bean
     */
    void onSaveAndNew(T bean);

    /**
     *
     */
    void onCancel();
}
