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

package com.esofthead.mycollab.vaadin.ui;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.esofthead.mycollab.common.domain.AuditChangeItem;
import com.esofthead.mycollab.common.domain.SimpleAuditLog;
import com.esofthead.mycollab.common.domain.criteria.AuditLogSearchCriteria;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.service.AuditLogService;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.utils.FieldGroupFomatter;
import com.esofthead.mycollab.utils.FieldGroupFomatter.FieldDisplayHandler;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 */
public abstract class HistoryLogComponent extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	protected BeanList<AuditLogService, AuditLogSearchCriteria, SimpleAuditLog> logTable;
	private String module;
	private String type;
	private FieldGroupFomatter groupFormatter;

	public HistoryLogComponent(String module, String type) {
		this.module = module;
		this.type = type;

		logTable = new BeanList<AuditLogService, AuditLogSearchCriteria, SimpleAuditLog>(
				this,
				ApplicationContextUtil.getSpringBean(AuditLogService.class),
				HistoryLogRowDisplay.class);
		this.setWidth("100%");
		this.setHeightUndefined();
		this.setMargin(true);
		this.setStyleName("historylog-component");

		this.addComponent(logTable);
		groupFormatter = buildFormatter();
	}

	protected abstract FieldGroupFomatter buildFormatter();

	public void loadHistory(int typeid) {
		AuditLogSearchCriteria criteria = new AuditLogSearchCriteria();
		criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
		criteria.setModule(new StringSearchField(module));
		criteria.setType(new StringSearchField(type));
		criteria.setTypeid(new NumberSearchField(typeid));
		logTable.setSearchCriteria(criteria);
	}

	public class HistoryLogRowDisplay extends
			BeanList.RowDisplayHandler<SimpleAuditLog> {
		private static final long serialVersionUID = 1L;

		@Override
		public Component generateRow(SimpleAuditLog log, int rowIndex) {

			List<AuditChangeItem> changeItems = log.getChangeItems();
			if (CollectionUtils.isNotEmpty(changeItems)) {
				CssLayout layout = new CssLayout();
				layout.setWidth("100%");
				layout.setStyleName("activity-stream");

				GridLayout gridLayout = new GridLayout(3,
						changeItems.size() + 2);
				gridLayout.setWidth("100%");

				int visibleRows = 0;

				String strDate = "";

				for (int i = 0; i < changeItems.size(); i++) {
					AuditChangeItem item = changeItems.get(i);
					String fieldName = item.getField();

					FieldDisplayHandler fieldDisplayHandler = groupFormatter
							.getFieldDisplayHandler(fieldName);
					if (fieldDisplayHandler != null) {
						gridLayout.addComponent(
								new Label(AppContext
										.getMessage(fieldDisplayHandler
												.getDisplayName())), 0,
								visibleRows + 2);
						gridLayout.addComponent(fieldDisplayHandler.getFormat()
								.toVaadinComponent(item.getOldvalue()), 1,
								visibleRows + 2);
						gridLayout.addComponent(fieldDisplayHandler.getFormat()
								.toVaadinComponent(item.getNewvalue()), 2,
								visibleRows + 2);
						visibleRows++;
					}

					if (fieldName.equals("lastupdatedtime")) {
						strDate = item.getNewvalue();
					}
				}

				if (visibleRows == 0) {
					return null;
				} else {
					HorizontalLayout header = new HorizontalLayout();
					header.setWidth("100%");
					header.setSpacing(true);
					UserLink userLink = new UserLink(log.getPosteduser(),
							log.getPostedUserAvatarId(),
							log.getPostedUserFullName(), false);

					header.addComponent(userLink);
					header.setComponentAlignment(userLink,
							Alignment.MIDDLE_LEFT);

					Label lbDate = new Label("changed "
							+ DateTimeUtils.getStringDateFromNow(DateTimeUtils
									.convertDateByFormatW3C(strDate),
									AppContext.getUserLocale()));
					header.addComponent(lbDate);
					header.setComponentAlignment(lbDate, Alignment.MIDDLE_LEFT);
					header.setExpandRatio(lbDate, 1.0f);
					gridLayout.addComponent(header, 0, 0, 2, 0);

					gridLayout
							.addComponent(
									new Label(
											String.format(
													"<div style=\"font-weight: bold;\">%s</div>",
													AppContext
															.getMessage(GenericI18Enum.HISTORY_FIELD)),
											ContentMode.HTML), 0, 1);
					gridLayout
							.addComponent(
									new Label(
											String.format(
													"<div style=\"font-weight: bold;\">%s</div>",
													AppContext
															.getMessage(GenericI18Enum.HISTORY_OLD_VALUE)),
											ContentMode.HTML), 1, 1);
					gridLayout
							.addComponent(
									new Label(
											String.format(
													"<div style=\"font-weight: bold;\">%s</div>",
													AppContext
															.getMessage(GenericI18Enum.HISTORY_NEW_VALUE)),
											ContentMode.HTML), 2, 1);

					gridLayout.setRows(visibleRows + 2);
					layout.addComponent(gridLayout);
					return layout;
				}

			} else {
				return null;
			}

		}
	}
}
