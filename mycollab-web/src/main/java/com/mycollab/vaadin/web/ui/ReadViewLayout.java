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
package com.mycollab.vaadin.web.ui;

import com.mycollab.core.MyCollabException;
import com.mycollab.web.CustomLayoutExt;
import com.vaadin.ui.Component;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public abstract class ReadViewLayout extends CustomLayoutExt {
    private static final long serialVersionUID = 1L;

    public ReadViewLayout() {
        super("readView");
    }

    public void addHeader(Component header) {
        this.addComponent(header, "readViewHeader");
    }

    public void addBody(Component body) {
        this.addComponent(body, "readViewBody");
    }

    public void addBottomControls(Component bottomControls) {
        this.addComponent(bottomControls, "readViewBottomControls");
    }

    public void addTitleStyleName(String styleName) {
        throw new MyCollabException("Must be implemented in the sub class");
    }

    public void removeTitleStyleName(String styleName) {
        throw new MyCollabException("Must be implemented in the sub class");
    }
}
