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
package com.esofthead.mycollab.mobile.module.crm.view.campaign;

import com.esofthead.mycollab.mobile.ui.DefaultPagedBeanList;
import com.esofthead.mycollab.mobile.ui.TableClickEvent;
import com.esofthead.mycollab.module.crm.domain.SimpleCampaign;
import com.esofthead.mycollab.module.crm.domain.criteria.CampaignSearchCriteria;
import com.esofthead.mycollab.module.crm.service.CampaignService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class CampaignListDisplay
		extends
		DefaultPagedBeanList<CampaignService, CampaignSearchCriteria, SimpleCampaign> {
	private static final long serialVersionUID = -2234454107835680053L;

	public CampaignListDisplay(String displayColumnId) {
		super(ApplicationContextUtil.getSpringBean(CampaignService.class),
				SimpleCampaign.class, displayColumnId);

		addGeneratedColumn("campaignname", new ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					final Object columnId) {
				final SimpleCampaign campaign = CampaignListDisplay.this
						.getBeanByIndex(itemId);
				final Button b = new Button(campaign.getCampaignname(),
						new Button.ClickListener() {
							private static final long serialVersionUID = 1L;

							@Override
							public void buttonClick(final ClickEvent event) {
								fireTableEvent(new TableClickEvent(
										CampaignListDisplay.this, campaign,
										"campaignname"));
							}
						});
				b.setWidth("100%");
				return b;
			}
		});
	}
}
