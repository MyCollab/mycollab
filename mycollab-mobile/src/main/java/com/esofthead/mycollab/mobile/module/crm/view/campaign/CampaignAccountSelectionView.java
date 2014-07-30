/**
 * 
 */
package com.esofthead.mycollab.mobile.module.crm.view.campaign;

import com.esofthead.mycollab.mobile.module.crm.ui.AbstractRelatedItemSelectionView;
import com.esofthead.mycollab.mobile.module.crm.ui.AbstractRelatedListView;
import com.esofthead.mycollab.mobile.module.crm.view.account.AccountListDisplay;
import com.esofthead.mycollab.mobile.ui.AbstractPagedBeanList;
import com.esofthead.mycollab.module.crm.domain.SimpleAccount;
import com.esofthead.mycollab.module.crm.domain.criteria.AccountSearchCriteria;
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
		super("Select Campaigns", relatedListView);
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
