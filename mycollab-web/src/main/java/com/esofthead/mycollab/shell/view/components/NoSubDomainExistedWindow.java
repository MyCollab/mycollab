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
package com.esofthead.mycollab.shell.view.components;

import com.esofthead.mycollab.common.i18n.ShellI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.ELabel;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class NoSubDomainExistedWindow extends MVerticalLayout {
    private static final long serialVersionUID = 1L;

    public NoSubDomainExistedWindow(final String domain) {
        this.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        final Label titleIcon = ELabel.fontIcon(FontAwesome.EXCLAMATION_CIRCLE).withStyleName("warning-icon",
                ValoTheme.LABEL_NO_MARGIN).withWidthUndefined();

        Label warningMsg = new ELabel(AppContext.getMessage(ShellI18nEnum.ERROR_NO_SUB_DOMAIN, domain)).withWidthUndefined();

        Button backToHome = new Button(AppContext.getMessage(ShellI18nEnum.BUTTON_BACK_TO_HOME_PAGE),
                clickEvent -> getUI().getPage().setLocation("https://www.mycollab.com"));
        backToHome.addStyleName(UIConstants.BUTTON_ACTION);
        this.with(titleIcon, warningMsg, backToHome);
    }
}