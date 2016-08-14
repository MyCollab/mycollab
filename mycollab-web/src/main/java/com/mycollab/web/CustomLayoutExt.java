/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.web;

import com.mycollab.core.MyCollabException;
import com.vaadin.ui.CustomLayout;

/**
 * Dynamic load custom layout per classpath, not absolutely path
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class CustomLayoutExt extends CustomLayout {
    private static final long serialVersionUID = 1L;

    public CustomLayoutExt(String layoutId) {
        super();

        try {
            initTemplateContentsFromInputStream(CustomLayoutExt.class.getClassLoader().getResourceAsStream("layouts/" + layoutId + ".html"));
        } catch (Exception e) {
            this.setTemplateName(layoutId);
        }
    }

    public static CustomLayout createLayout(String layoutId) {
        try {
            return new CustomLayout(CustomLayoutExt.class.getClassLoader().getResourceAsStream("layouts/" + layoutId + ".html"));
        } catch (Exception e) {
            throw new MyCollabException(e);
        }
    }
}
