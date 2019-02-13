/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.ui;

import com.vaadin.data.HasValue;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.addons.stackpanel.StackPanel;

/**
 * @author MyCollab Ltd
 * @since 5.1.1
 */
public class UIUtils {
    private static Logger LOG = LoggerFactory.getLogger(UIUtils.class);

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
        if (parent != null) {
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

    public static void makeStackPanel(Panel panel) {
        StackPanel stackPanel = StackPanel.extend(panel);
        stackPanel.setToggleDownIcon(VaadinIcons.ANGLE_RIGHT);
        stackPanel.setToggleUpIcon(VaadinIcons.ANGLE_DOWN);
    }

    public static HasValue<?> getComponent(String compClassName) {
        try {
            return (HasValue<?>) Class.forName(compClassName).newInstance();
        } catch (Exception e) {
            LOG.warn("Can not init component " + compClassName, e);
            return null;
        }
    }
}
