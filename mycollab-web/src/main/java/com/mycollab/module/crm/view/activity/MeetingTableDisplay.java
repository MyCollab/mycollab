/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.view.activity;

import com.mycollab.common.GridFieldMeta;
import com.mycollab.module.crm.domain.SimpleMeeting;
import com.mycollab.module.crm.domain.criteria.MeetingSearchCriteria;
import com.mycollab.module.crm.service.MeetingService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.web.ui.table.DefaultPagedGrid;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
// TODO
public class MeetingTableDisplay extends DefaultPagedGrid<MeetingService, MeetingSearchCriteria, SimpleMeeting> {
    private static final long serialVersionUID = 1L;

    public MeetingTableDisplay(List<GridFieldMeta> displaycolumns) {
        super(AppContextUtil.getSpringBean(MeetingService.class), SimpleMeeting.class, displaycolumns);

//        this.addGeneratedColumn("subject", (source, itemId, columnId) -> {
//            final SimpleMeeting meeting = getBeanByIndex(itemId);
//
//            LabelLink b = new LabelLink(meeting.getSubject(), CrmLinkGenerator.generateMeetingPreviewLink(meeting.getId()));
//            b.addStyleName(WebThemes.LINK_COMPLETED);
//
//            if (CallStatus.Held.name().equals(meeting.getStatus())) {
//                b.addStyleName(WebThemes.LINK_COMPLETED);
//            } else {
//                if (meeting.getEnddate() != null && (meeting.getEnddate().before(new GregorianCalendar().getTime()))) {
//                    b.addStyleName(WebThemes.LINK_OVERDUE);
//                }
//            }
//            return b;
//        });
//
//        this.addGeneratedColumn("startdate", (source, itemId, columnId) -> {
//            final SimpleMeeting meeting = getBeanByIndex(itemId);
//            return new Label(UserUIContext.formatDateTime(meeting.getStartdate()));
//        });
//
//        this.addGeneratedColumn("status", (source, itemId, columnId) -> {
//            final SimpleMeeting meeting = getBeanByIndex(itemId);
//            return ELabel.i18n(meeting.getStatus(), CallStatus.class);
//        });
    }
}
