package com.mycollab.vaadin.ui;

import com.vaadin.util.ReflectTools;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.EventListener;

/**
 * @author MyCollab Ltd
 * @since 5.4.3
 */
public interface PropertyChangedListener extends EventListener, Serializable {
    Method viewInitMethod = ReflectTools.findMethod(PropertyChangedListener.class, "propertyChanged", PropertyChangedEvent.class);

    void propertyChanged(PropertyChangedEvent event);
}
