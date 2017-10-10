/**
 * mycollab-web - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.web.ui;

import com.mycollab.vaadin.ui.ELabel;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
public class BlockWidget extends MVerticalLayout {
    private static final long serialVersionUID = -8503014399083980294L;

    private ELabel titleLbl;
    private CssLayout bodyLayout;

    public BlockWidget(String title) {
        titleLbl = ELabel.h2("");
        super.addComponent(titleLbl);

        bodyLayout = new CssLayout();
        bodyLayout.setWidth("100%");
        super.addComponent(bodyLayout);
        this.setTitle(title);
    }

    public void setTitle(String title) {
        this.titleLbl.setValue(title);
    }

    public void addToBody(Component component) {
        bodyLayout.addComponent(component);
    }

    @Override
    public void addComponent(Component c) {
        this.addToBody(c);
    }

    @Override
    public void addComponentAsFirst(Component c) {
        bodyLayout.addComponentAsFirst(c);
    }

    @Override
    public void addComponent(Component c, int index) {
        bodyLayout.addComponent(c, index);
    }

    @Override
    public void removeComponent(Component c) {
        bodyLayout.removeComponent(c);
    }

    @Override
    public void replaceComponent(Component oldComponent, Component newComponent) {
        bodyLayout.replaceComponent(oldComponent, newComponent);
    }

    @Override
    public int getComponentIndex(Component component) {
        return bodyLayout.getComponentIndex(component);
    }

    @Override
    public Component getComponent(int index) throws IndexOutOfBoundsException {
        return bodyLayout.getComponent(index);
    }

    @Override
    public void addComponents(Component... components) {
        bodyLayout.addComponents(components);
    }

    @Override
    public void removeAllComponents() {
        bodyLayout.removeAllComponents();
    }
}
