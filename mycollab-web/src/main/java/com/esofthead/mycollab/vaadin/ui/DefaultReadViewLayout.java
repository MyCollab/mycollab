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
package com.esofthead.mycollab.vaadin.ui;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Label;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.6.0
 */
public class DefaultReadViewLayout extends ReadViewLayout {
    private Label titleLbl;

    public DefaultReadViewLayout(String title) {
        super();
        this.addHeader(buildHeader(title));
    }

    private ComponentContainer buildHeader(String title) {
        MHorizontalLayout header = new MHorizontalLayout().withWidth("100%");

        this.titleLbl = new Label();
        this.titleLbl.setStyleName("headerName");

        header.with(titleLbl).expand(titleLbl);

        if (StringUtils.isBlank(title)) {
            this.setTitle("Undefined");
        } else {
            this.setTitle(title);
        }
        return header;
    }

    @Override
    public void clearTitleStyleName() {
        this.titleLbl.setStyleName("headerName");
    }

    @Override
    public void addTitleStyleName(final String styleName) {
        this.titleLbl.addStyleName(styleName);
    }

    @Override
    public void setTitleStyleName(final String styleName) {
        this.titleLbl.setStyleName(styleName);
    }

    @Override
    public void removeTitleStyleName(final String styleName) {
        this.titleLbl.removeStyleName(styleName);
    }

    @Override
    public void setTitle(final String title) {
        this.titleLbl.setValue(title);
    }
}
