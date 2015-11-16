/**
 * This file is part of mycollab-web-community.
 *
 * mycollab-web-community is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web-community is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web-community.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.vaadin.ui.form.field;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.web.AdWindow;
import com.vaadin.server.FontIcon;
import com.vaadin.ui.*;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
public class PopupFieldBuilder {
    private String captionHtml;
    private String description = "Edit";
    private PopupView view;

    public PopupFieldBuilder withCaptionAndIcon(FontIcon icon, String caption) {
        captionHtml = icon.getHtml() + " " + StringUtils.trim(caption, 20, true);
        return this;
    }

    public PopupFieldBuilder withCaption(String caption) {
        this.captionHtml = caption;
        return this;
    }

    public PopupFieldBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public PopupView build() {
        view = new PopupView(new PopupView.Content() {
            @Override
            public String getMinimizedValueAsHTML() {
                return captionHtml;
            }

            @Override
            public Component getPopupComponent() {
                MVerticalLayout layout = new MVerticalLayout();
                layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
                Label infoLbl = new Label(AppContext.getMessage(GenericI18Enum.NOTIFICATION_FEATURE_NOT_AVAILABLE_IN_VERSION));

                Button requestFeatureBtn = new Button("Buy the premium edition", new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        UI.getCurrent().addWindow(new AdWindow());
                        view.setPopupVisible(false);
                    }
                });
                requestFeatureBtn.addStyleName(UIConstants.BUTTON_ACTION);

                layout.with(infoLbl, requestFeatureBtn);
                return layout;
            }
        });
        view.setDescription(description);
        view.setStyleName("block-popupedit");
        return view;
    }
}
