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
/**
 * 
 */
package com.esofthead.mycollab.mobile.module.crm.view.campaign;

import com.esofthead.mycollab.mobile.module.crm.ui.AbstractRelatedItemSelectionView;
import com.esofthead.mycollab.mobile.module.crm.view.account.AccountListDisplay;
import com.esofthead.mycollab.mobile.ui.AbstractPagedBeanList;
import com.esofthead.mycollab.mobile.ui.AbstractRelatedListView;
import com.esofthead.mycollab.module.crm.domain.SimpleAccount;
import com.esofthead.mycollab.module.crm.domain.criteria.AccountSearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.AccountI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;

/**
 * @author MyCollab Inc.
 * 
 * @since 4.3.1
 */
public class CampaignAccountSelectionView extends
		AbstractRelatedItemSelectionView<SimpleAccount, AccountSearchCriteria> {

	private static final long serialVersionUID = -801602909364348692L;

	public CampaignAccountSelectionView(
			AbstractRelatedListView<SimpleAccount, AccountSearchCriteria> relatedListView) {
		super(AppContext.getMessage(AccountI18nEnum.M_TITLE_SELECT_ACCOUNTS),
				relatedListView);
	}

	@Override
	protected void initUI() {
		this.itemList = new AccountListDisplay();
		this.itemList
				.setRowDisplayHandler(new AbstractPagedBeanList.RowDisplayHandler<SimpleAccount>() {

					@Override
					public Component generateRow(final SimpleAccount obj,
							int rowIndex) {
						final SelectableButton b = new SelectableButton(obj
								.getAccountname());
						if (selections.contains(obj))
							b.select();

						b.addClickListener(new Button.ClickListener() {
							private static final long serialVersionUID = 3421125241167437144L;

							@Override
							public void buttonClick(Button.ClickEvent event) {
								if (b.isSelected())
									selections.add(obj);
								else
									selections.remove(obj);
							}
						});
						return b;
					}

				});
	}

}
