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

import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;

/**
 * @author MyCollab Ltd.
 *
 * @since 4.4.0
 */
public class BlockWidget extends CssLayout {
	private static final long serialVersionUID = -8503014399083980294L;

	private final Label title;
	private final CssLayout body;

	public BlockWidget() {
		super();
		this.setStyleName("block-widget");
		title = new Label();
		title.setStyleName("block-header");
		super.addComponent(title);

		body = new CssLayout();
		body.setWidth("100%");
		body.setStyleName("block-body");
		super.addComponent(body);
	}

	public BlockWidget(String title) {
		this();
		this.setTitle(title);
	}

	public void setTitle(String title) {
		this.title.setValue(title);
	}

	public void addToBody(Component component) {
		this.body.addComponent(component);
	}

	@Override
	public void addComponent(Component c) {
		this.addToBody(c);
	}

	@Override
	public void addComponentAsFirst(Component c) {
		this.body.addComponentAsFirst(c);
	}

	@Override
	public void addComponent(Component c, int index) {
		this.body.addComponent(c, index);
	}

	@Override
	public void removeComponent(Component c) {
		this.body.removeComponent(c);
	}

	@Override
	public void replaceComponent(Component oldComponent, Component newComponent) {
		this.body.replaceComponent(oldComponent, newComponent);
	}

	@Override
	public int getComponentIndex(Component component) {
		return this.body.getComponentIndex(component);
	}

	@Override
	public Component getComponent(int index) throws IndexOutOfBoundsException {
		return this.body.getComponent(index);
	}

	@Override
	public void addComponents(Component... components) {
		this.body.addComponents(components);
	}

	@Override
	public void removeAllComponents() {
		this.body.removeAllComponents();
	}

}
