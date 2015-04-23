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

package com.esofthead.mycollab.module.crm.ui.components;

import com.esofthead.mycollab.module.crm.i18n.CrmCommonI18nEnum;
import com.esofthead.mycollab.utils.FieldGroupFormatter;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.HistoryLogComponent;
import com.vaadin.ui.Window;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public abstract class HistoryLogWindow extends Window {
	private static final long serialVersionUID = 1L;

	private final HistoryLogComponent historyLogComponent;

	public HistoryLogWindow(final String module, final String type) {
		super(AppContext.getMessage(CrmCommonI18nEnum.DIALOG_CHANGE_LOG_TITLE));

		this.setWidth("700px");
		this.setModal(true);
		this.setResizable(false);

		this.historyLogComponent = new WrappedHistoryLogComponent(module, type);
		this.setContent(historyLogComponent);
		this.center();
	}

	public void loadHistory(int typeid) {
		historyLogComponent.loadHistory(typeid);
	}

	protected abstract FieldGroupFormatter buildFormatter();

	private class WrappedHistoryLogComponent extends HistoryLogComponent {
		private static final long serialVersionUID = 1L;

		public WrappedHistoryLogComponent(String module, String type) {
			super(module, type);
		}

		@Override
		protected FieldGroupFormatter buildFormatter() {
			return HistoryLogWindow.this.buildFormatter();
		}

	}
}
