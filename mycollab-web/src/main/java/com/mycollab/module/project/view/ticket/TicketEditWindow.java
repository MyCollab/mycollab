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
package com.mycollab.module.project.view.ticket;

import com.mycollab.vaadin.ui.UIUtils;
import org.vaadin.viritin.layouts.MWindow;

/**
 * @author MyCollab Ltd
 * @since 5.4.4
 */
public class TicketEditWindow extends MWindow {
    TicketEditWindow(String type, Integer typeId) {
        withModal(false).withWidth("900px").withHeight(UIUtils.getBrowserHeight() + "px")
                .withPosition(UIUtils.getBrowserWidth() - 900, 0);
        addBlurListener(blurEvent -> close());
    }
}
