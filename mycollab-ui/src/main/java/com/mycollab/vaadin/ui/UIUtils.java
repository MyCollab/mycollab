package com.mycollab.vaadin.ui;

import com.vaadin.ui.*;

import java.util.Collection;

/**
 * @author MyCollab Ltd
 * @since 5.1.1
 */
public class UIUtils {
    public static <T> T getRoot(Component container, Class<T> type) {
        HasComponents parent = container.getParent();
        while (parent != null) {
            if (type.isAssignableFrom(parent.getClass())) {
                return (T) parent;
            } else {
                parent = parent.getParent();
            }
        }
        return null;
    }

    public static boolean removeChildAssociate(Component container, Class type) {
        HasComponents parent = container.getParent();
        while (parent != null) {
            if (type.isAssignableFrom(parent.getClass()) && (parent instanceof ComponentContainer)) {
                ((ComponentContainer) parent).removeComponent(container);
                return true;
            } else {
                return removeChildAssociate(parent, type);
            }
        }
        return false;
    }

    public static Integer getBrowserWidth() {
        return UI.getCurrent().getPage().getBrowserWindowWidth();
    }

    public static Integer getBrowserHeight() {
        return UI.getCurrent().getPage().getBrowserWindowHeight();
    }

    public static void removeAllWindows() {
        Collection<Window> windows = UI.getCurrent().getWindows();
        for (Window window : windows) {
            window.close();
        }
    }
}
