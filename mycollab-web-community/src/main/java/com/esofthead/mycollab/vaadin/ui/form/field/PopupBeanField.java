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
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.FontIcon;
import com.vaadin.ui.*;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.1.2
 */
public class PopupBeanField extends PopupView {
    public PopupBeanField(final FontIcon icon, final String valueAsHtml) {
        this(icon.getHtml() + " " + StringUtils.trim(valueAsHtml, 20, true));
    }

    public PopupBeanField(final String valueAsHtml) {
        super(new Content() {
            @Override
            public String getMinimizedValueAsHTML() {
                return valueAsHtml;
            }

            @Override
            public Component getPopupComponent() {
                MVerticalLayout layout = new MVerticalLayout();
                layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
                Label infoLbl =  new Label(AppContext.getMessage(GenericI18Enum.NOTIFICATION_FEATURE_NOT_AVAILABLE_IN_VERSION));

                Button requestFeatureBtn = new Button("Request the premium edition");
                requestFeatureBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
                BrowserWindowOpener opener = new BrowserWindowOpener("mailto:support@mycollab.com");
                opener.extend(requestFeatureBtn);

                layout.with(infoLbl, requestFeatureBtn);
                return layout;
            }
        });
        this.setStyleName("block-popupedit");
    }
}
