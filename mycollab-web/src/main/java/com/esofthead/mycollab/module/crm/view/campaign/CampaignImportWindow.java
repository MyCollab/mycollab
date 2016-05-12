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
package com.esofthead.mycollab.module.crm.view.campaign;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.iexporter.CSVObjectEntityConverter.FieldMapperDef;
import com.esofthead.mycollab.iexporter.csv.CSVDateFormatter;
import com.esofthead.mycollab.module.crm.domain.SimpleCampaign;
import com.esofthead.mycollab.module.crm.domain.criteria.CampaignSearchCriteria;
import com.esofthead.mycollab.module.crm.events.CampaignEvent;
import com.esofthead.mycollab.module.crm.service.CampaignService;
import com.esofthead.mycollab.module.crm.ui.components.EntityImportWindow;
import com.esofthead.mycollab.spring.AppContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;

import java.util.Arrays;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class CampaignImportWindow extends EntityImportWindow<SimpleCampaign> {
    private static final long serialVersionUID = 1L;

    public CampaignImportWindow() {
        super(false, "Import Campaign", AppContextUtil.getSpringBean(CampaignService.class), SimpleCampaign.class);
    }

    @Override
    protected List<FieldMapperDef> constructCSVFieldMapper() {
        FieldMapperDef[] fields = {
                new FieldMapperDef("campaignname", "Campaign Name"),
                new FieldMapperDef("startdate", "Start Date", new CSVDateFormatter()),
                new FieldMapperDef("enddate", "End Date", new CSVDateFormatter()),
                new FieldMapperDef("impressionnote", "Impression Note"),
                new FieldMapperDef("budget", "Budget"),
                new FieldMapperDef("actualcost", "Actual Cost"),
                new FieldMapperDef("expectedrevenue", "Expected Revenue"),
                new FieldMapperDef("expectedcost", "Expected Cost"),
                new FieldMapperDef("impression", "Impression"),
                new FieldMapperDef("status", "Status"),
                new FieldMapperDef("type", "Type"),
                new FieldMapperDef("assignuser", AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE))};
        return Arrays.asList(fields);
    }

    @Override
    protected void reloadWhenBackToListView() {
        EventBusFactory.getInstance().post(new CampaignEvent.GotoList(CampaignListView.class, new CampaignSearchCriteria()));
    }
}
