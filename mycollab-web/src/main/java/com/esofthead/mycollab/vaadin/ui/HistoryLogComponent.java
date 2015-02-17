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

import com.esofthead.mycollab.common.domain.AuditChangeItem;
import com.esofthead.mycollab.common.domain.SimpleAuditLog;
import com.esofthead.mycollab.common.domain.criteria.AuditLogSearchCriteria;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.service.AuditLogService;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.utils.FieldGroupFomatter;
import com.esofthead.mycollab.utils.FieldGroupFomatter.FieldDisplayHandler;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.apache.commons.collections.CollectionUtils;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

import java.util.Date;
import java.util.List;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 */
public abstract class HistoryLogComponent extends MVerticalLayout {
	private static final long serialVersionUID = 1L;

	protected BeanList<AuditLogService, AuditLogSearchCriteria, SimpleAuditLog> logTable;
	private String module;
	private String type;
	private FieldGroupFomatter groupFormatter;

	public HistoryLogComponent(String module, String type) {
		this.module = module;
		this.type = type;

		logTable = new BeanList<>(
				this,
				ApplicationContextUtil.getSpringBean(AuditLogService.class),
				HistoryLogRowDisplay.class);

		this.withWidth("100%").withStyleName("historylog-component");
		this.setHeightUndefined();

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
		int numHistories = logTable.setSearchCriteria(criteria);

		Object parentComp = this.getParent();
		if (parentComp instanceof TabSheetLazyLoadComp) {
			((TabSheetLazyLoadComp)parentComp).getTab(this).setCaption(AppContext.getMessage(ProjectCommonI18nEnum
					.TAB_HISTORY, numHistories));
		}
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
				layout.setStyleName("list-row");

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
					MHorizontalLayout header = new MHorizontalLayout().withWidth("100%");
					UserLink userLink = new UserLink(log.getPosteduser(),
							log.getPostedUserAvatarId(),
							log.getPostedUserFullName(), false);

					header.with(userLink).withAlign(userLink, Alignment.MIDDLE_LEFT);

					Date changeDate = DateTimeUtils
							.convertDateByFormatW3C(strDate);
					Label lbDate = new Label("changed "
							+ DateTimeUtils.getPrettyDateValue(changeDate,
									AppContext.getUserLocale()));
					lbDate.setDescription(AppContext.formatDateTime(changeDate));
					header.with(lbDate).withAlign(lbDate, Alignment.MIDDLE_LEFT).expand(lbDate);
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
