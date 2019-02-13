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
package com.mycollab.vaadin.web.ui;

import com.mycollab.vaadin.ui.ELabel;
import com.vaadin.ui.*;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 5.0.3
 */
public class OptionPopupContent extends CustomComponent {

    public OptionPopupContent() {
        this.setCompositionRoot(new MVerticalLayout().withSpacing(false).withStyleName(WebThemes.OPTION_POPUP_CONTENT));
    }

    public void removeOptions() {
        ((ComponentContainer) this.getCompositionRoot()).removeAllComponents();
    }

    public void addOption(Button btn) {
        CssLayout wrap = new CssLayout();
        btn.setWidth("100%");
        btn.setDescription(btn.getCaption());
        btn.addStyleName("action");
        wrap.addStyleName("action-wrap");
        wrap.addComponent(btn);
        ((ComponentContainer) this.getCompositionRoot()).addComponent(wrap);
    }

    public void addBlankOption(Component component) {
        ((ComponentContainer) this.getCompositionRoot()).addComponent(component);
    }

    public void addDangerOption(Component btn) {
        CssLayout wrap = new CssLayout();
        btn.setWidth("100%");
        btn.addStyleName("action");
        wrap.addStyleName("action-wrap danger");
        wrap.addComponent(btn);
        ((ComponentContainer) this.getCompositionRoot()).addComponent(wrap);
    }

    public void addSeparator() {
        ((ComponentContainer) this.getCompositionRoot()).addComponent(ELabel.hr());
    }

    public void addSection(String title) {
        Label sectionLbl = new Label(title);
        sectionLbl.setStyleName("section-header");
        ((ComponentContainer) this.getCompositionRoot()).addComponent(sectionLbl);
    }

    @Override
    public int getComponentCount() {
        Component root = getCompositionRoot();
        return root != null ? ((ComponentContainer) root).getComponentCount() : 0;
    }
}
