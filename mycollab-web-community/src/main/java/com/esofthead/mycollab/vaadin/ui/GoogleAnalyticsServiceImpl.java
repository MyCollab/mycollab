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
package com.esofthead.mycollab.vaadin.ui;

import com.vaadin.ui.UI;
import org.springframework.stereotype.Service;

/**
 * @author MyCollab Ltd.
 * @since 5.0.2
 */
@Service
public class GoogleAnalyticsServiceImpl implements GoogleAnalyticsService {
    @Override
    public void registerUI(UI ui) {

    }

    @Override
    public void trackPageView(String pageId) {

    }
}