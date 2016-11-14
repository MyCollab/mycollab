/**
 * This file is part of mycollab-ui.
 *
 * mycollab-ui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-ui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-ui.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.mvp;

import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.SingleComponentContainer;

/**
 * @author MyCollab Ltd
 * @since 5.4.5
 */
public class AbstractSingleContainerPageView extends CustomComponent implements PageView, SingleComponentContainer {

    private CssLayout contentLayout;

    public AbstractSingleContainerPageView() {
        contentLayout = new CssLayout();
        contentLayout.setSizeFull();
        setCompositionRoot(contentLayout);
        setSizeFull();
    }

    @Override
    public Component getContent() {
        return contentLayout.getComponent(0);
    }

    @Override
    public void setContent(Component component) {
        contentLayout.removeAllComponents();
        contentLayout.addComponent(component);
    }

    @Override
    public void addComponentAttachListener(ComponentAttachListener componentAttachListener) {
        contentLayout.addComponentAttachListener(componentAttachListener);
    }

    @Override
    public void removeComponentAttachListener(ComponentAttachListener componentAttachListener) {

    }

    @Override
    public void addComponentDetachListener(ComponentDetachListener componentDetachListener) {

    }

    @Override
    public void removeComponentDetachListener(ComponentDetachListener componentDetachListener) {

    }

    @Override
    public void addViewListener(ViewListener listener) {
        addListener(ViewEvent.VIEW_IDENTIFIER(), ViewEvent.class, listener, ViewListener.viewInitMethod);
    }
}
