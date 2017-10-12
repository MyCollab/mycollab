/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.mobile.module.crm.view.contact;

import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.mobile.module.crm.event.ContactEvent;
import com.mycollab.mobile.module.crm.ui.AbstractListPageView;
import com.mycollab.mobile.ui.AbstractPagedBeanList;
import com.mycollab.mobile.ui.SearchInputField;
import com.mycollab.mobile.ui.SearchInputView;
import com.mycollab.mobile.ui.SearchNavigationButton;
import com.mycollab.module.crm.domain.SimpleContact;
import com.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.mycollab.module.crm.i18n.ContactI18nEnum;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.UIConstants;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
@ViewComponent
public class ContactListViewImpl extends AbstractListPageView<ContactSearchCriteria, SimpleContact> implements ContactListView {
    private static final long serialVersionUID = 8271856163726726780L;

    public ContactListViewImpl() {
        setCaption(UserUIContext.getMessage(ContactI18nEnum.LIST));
    }

    @Override
    protected AbstractPagedBeanList<ContactSearchCriteria, SimpleContact> createBeanList() {
        return new ContactListDisplay();
    }

    @Override
    protected SearchInputField<ContactSearchCriteria> createSearchField() {
        return null;
    }

    @Override
    public void onBecomingVisible() {
        super.onBecomingVisible();
        AppUI.addFragment("crm/contact/list", UserUIContext.getMessage(ContactI18nEnum.LIST));
    }

    @Override
    protected Component buildRightComponent() {
        SearchNavigationButton searchBtn = new SearchNavigationButton() {
            @Override
            protected SearchInputView getSearchInputView() {
                return new ContactSearchInputView();
            }
        };
        MButton newContactBtn = new MButton("", clickEvent -> EventBusFactory.getInstance().post(new ContactEvent.GotoAdd(this, null)))
                .withIcon(FontAwesome.PLUS).withStyleName(UIConstants.CIRCLE_BOX)
                .withVisible(UserUIContext.canWrite(RolePermissionCollections.CRM_CONTACT));
        return new MHorizontalLayout(searchBtn, newContactBtn).alignAll(Alignment.TOP_RIGHT);
    }
}
