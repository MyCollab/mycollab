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

import com.vaadin.ui.Component;
import com.vaadin.ui.PopupView;
import org.vaadin.jouni.restrain.Restrain;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.1.3
 */
public class LazyPopupView extends PopupView {

    public LazyPopupView(String valueAsHtml) {
        super(new PopupContent(valueAsHtml));
        addPopupVisibilityListener(popupVisibilityEvent -> {
            if (popupVisibilityEvent.isPopupVisible()) {
                doShow();
            } else {
                doHide();
            }
        });
        this.setStyleName("block-popupedit");
        this.setHideOnMouseOut(false);
    }

    final public MVerticalLayout getWrapContent() {
        PopupView.Content content = getContent();
        MVerticalLayout layout = (MVerticalLayout) content.getPopupComponent();
        return layout;
    }

    final public void setMinimizedValueAsHTML(String valueAsHtml) {
        PopupContent content = (PopupContent) getContent();
        content.setMinimizedValueAsHTML(valueAsHtml);
    }

    protected void doShow() {
    }

    protected void doHide() {
    }

    private static class PopupContent implements PopupView.Content {
        private String valueAsHtml;
        private MVerticalLayout content;

        public PopupContent(String valueAsHtml) {
            this.valueAsHtml = valueAsHtml;
            content = new MVerticalLayout();
            new Restrain(content).setMaxHeight("600px").setMaxWidth("600px");
        }

        public void setMinimizedValueAsHTML(String valueAsHtml) {
            this.valueAsHtml = valueAsHtml;
        }

        @Override
        public String getMinimizedValueAsHTML() {
            return valueAsHtml;
        }

        @Override
        public Component getPopupComponent() {
            return content;
        }
    }
}
