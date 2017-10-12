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
package com.mycollab.module.crm.view.cases;

import com.mycollab.module.crm.domain.SimpleActivity;
import com.mycollab.module.crm.domain.SimpleCase;
import com.mycollab.module.crm.domain.SimpleContact;
import com.mycollab.vaadin.event.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.IPreviewView;
import com.mycollab.vaadin.ui.IRelatedListHandlers;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public interface CaseReadView extends IPreviewView<SimpleCase> {
    HasPreviewFormHandlers<SimpleCase> getPreviewFormHandlers();

    IRelatedListHandlers<SimpleActivity> getRelatedActivityHandlers();

    IRelatedListHandlers<SimpleContact> getRelatedContactHandlers();
}
