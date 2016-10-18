/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.mobile.module.crm.view.activity;

import com.mycollab.mobile.ui.ValueListSelect;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class MeetingStatusListSelect extends ValueListSelect {
    private static final long serialVersionUID = 1L;

    public MeetingStatusListSelect() {
        super();
        setCaption(null);
        this.loadData("Planned", "Held", "Not Held");
    }
}
