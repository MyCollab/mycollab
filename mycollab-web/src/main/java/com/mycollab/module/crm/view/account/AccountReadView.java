/**
 * mycollab-web - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.view.account;

import com.mycollab.vaadin.event.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.IPreviewView;
import com.mycollab.vaadin.ui.IRelatedListHandlers;
import com.mycollab.module.crm.domain.*;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public interface AccountReadView extends IPreviewView<SimpleAccount> {

    HasPreviewFormHandlers<SimpleAccount> getPreviewFormHandlers();

    IRelatedListHandlers<SimpleContact> getRelatedContactHandlers();

    IRelatedListHandlers<SimpleOpportunity> getRelatedOpportunityHandlers();

    IRelatedListHandlers<SimpleLead> getRelatedLeadHandlers();

    IRelatedListHandlers<SimpleCase> getRelatedCaseHandlers();

    IRelatedListHandlers<SimpleActivity> getRelatedActivityHandlers();
}
