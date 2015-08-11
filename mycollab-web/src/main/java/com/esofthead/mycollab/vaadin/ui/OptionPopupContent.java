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


import com.vaadin.ui.*;

/**
 * @author MyCollab Ltd.
 * @since 5.0.3
 */
public class OptionPopupContent extends CustomComponent {
    public OptionPopupContent() {
        VerticalLayout root = new VerticalLayout();
        root.setStyleName(UIConstants.OPTION_POPUP_CONTENT);
        root.setWidth("200px");
        this.setCompositionRoot(root);
    }

    public OptionPopupContent withWidth(String width) {
        this.getCompositionRoot().setWidth(width);
        return this;
    }

    public void addOption(Component btn) {
        CssLayout wrap = new CssLayout();
        btn.setWidth("100%");
        btn.setStyleName("action");
        wrap.addStyleName("action-wrap");
        wrap.addComponent(btn);
        ((ComponentContainer) this.getCompositionRoot()).addComponent(wrap);
    }

    @Override
    public int getComponentCount() {
        Component root = getCompositionRoot();
        return root != null ? ((ComponentContainer)root).getComponentCount() : 0;
    }
}
