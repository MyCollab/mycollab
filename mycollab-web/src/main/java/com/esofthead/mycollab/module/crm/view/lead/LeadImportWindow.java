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
package com.esofthead.mycollab.module.crm.view.lead;

import java.util.Arrays;
import java.util.List;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.iexporter.CSVObjectEntityConverter.FieldMapperDef;
import com.esofthead.mycollab.iexporter.csv.CSVBooleanFormatter;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.esofthead.mycollab.module.crm.events.LeadEvent;
import com.esofthead.mycollab.module.crm.service.LeadService;
import com.esofthead.mycollab.module.crm.ui.components.EntityImportWindow;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class LeadImportWindow extends EntityImportWindow<SimpleLead> {
	private static final long serialVersionUID = 1L;

	public LeadImportWindow() {
		super(false, "Import Leads", ApplicationContextUtil
				.getSpringBean(LeadService.class), SimpleLead.class);
	}

	@Override
	protected List<FieldMapperDef> constructCSVFieldMapper() {
		FieldMapperDef[] fields = {
				new FieldMapperDef("leadsourcedesc", "Leader Source"),
				new FieldMapperDef("statusdesc", "Status"),
				new FieldMapperDef("referredby", "Referred By"),
				new FieldMapperDef("prefixname", "Prefix Name"),
				new FieldMapperDef("firstname", "First Name"),
				new FieldMapperDef("lastname", "Last Name"),
				new FieldMapperDef("accountname", "Account Name"),
				new FieldMapperDef("title", "Title"),
				new FieldMapperDef("department", "Department"),
				new FieldMapperDef("iscallable", "Callable",
						new CSVBooleanFormatter()),
				new FieldMapperDef("officephone", "Office Phone"),
				new FieldMapperDef("homephone", "Home phone"),
				new FieldMapperDef("otherphone", "Other Phone"),
				new FieldMapperDef("mobile", "Mobile"),
				new FieldMapperDef("fax", "Fax"),
				new FieldMapperDef("assignuser",
						AppContext
								.getMessage(GenericI18Enum.FORM_ASSIGNEE)),
				new FieldMapperDef("status", "Status"),
				new FieldMapperDef("source", "Source"),
				new FieldMapperDef("website", "Website"),
				new FieldMapperDef("industry", "Industry"),
				new FieldMapperDef("noemployees", "NoEmployess"),
				new FieldMapperDef("email", "Email"),
				new FieldMapperDef("primaddress", "Address"),
				new FieldMapperDef("primcity", "City"),
				new FieldMapperDef("primstate", "State"),
				new FieldMapperDef("primpostalcode", "Postal Code"),
				new FieldMapperDef("primcountry", "Country"),
				new FieldMapperDef("otheraddress", "Other Address"),
				new FieldMapperDef("othercity", "Other City"),
				new FieldMapperDef("otherstate", "Other State"),
				new FieldMapperDef("otherpostalcode", "Other Postal Code"),
				new FieldMapperDef("othercountry", "Other Country"),
				new FieldMapperDef("description", "Description") };
		return Arrays.asList(fields);
	}

	@Override
	protected void reloadWhenBackToListView() {
		EventBusFactory.getInstance().post(
				new LeadEvent.GotoList(LeadListView.class,
						new LeadSearchCriteria()));
	}
}
