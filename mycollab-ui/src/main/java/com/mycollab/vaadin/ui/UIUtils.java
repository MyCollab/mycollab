/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
