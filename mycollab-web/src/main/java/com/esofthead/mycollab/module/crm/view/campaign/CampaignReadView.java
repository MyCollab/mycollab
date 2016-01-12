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

import com.esofthead.mycollab.module.crm.domain.*;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.IPreviewView;
import com.esofthead.mycollab.vaadin.ui.IRelatedListHandlers;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public interface CampaignReadView extends IPreviewView<SimpleCampaign> {
    HasPreviewFormHandlers<SimpleCampaign> getPreviewFormHandlers();

    IRelatedListHandlers<SimpleActivity> getRelatedActivityHandlers();

    IRelatedListHandlers<SimpleAccount> getRelatedAccountHandlers();

    IRelatedListHandlers<SimpleContact> getRelatedContactHandlers();

    IRelatedListHandlers<SimpleLead> getRelatedLeadHandlers();
}
