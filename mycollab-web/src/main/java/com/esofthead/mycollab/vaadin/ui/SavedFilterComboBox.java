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

import com.esofthead.mycollab.common.domain.SaveSearchResultWithBLOBs;
import com.esofthead.mycollab.common.domain.criteria.SaveSearchResultCriteria;
import com.esofthead.mycollab.common.service.SaveSearchResultService;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.ui.ComboBox;

import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.1.1
 */
public class SavedFilterComboBox extends ComboBox {

    public SavedFilterComboBox(String type) {
        this.setItemCaptionMode(ItemCaptionMode.EXPLICIT);
        SaveSearchResultCriteria searchCriteria = new SaveSearchResultCriteria();
        searchCriteria.setType(new StringSearchField(type));
        searchCriteria.setCreateUser(new StringSearchField(AppContext.getUsername()));
        searchCriteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));

        SaveSearchResultService saveSearchResultService = ApplicationContextUtil.getSpringBean(SaveSearchResultService.class);
        List<SaveSearchResultWithBLOBs> result = saveSearchResultService.findPagableListByCriteria(new SearchRequest<>(
                searchCriteria, 0, Integer.MAX_VALUE));

        for (SaveSearchResultWithBLOBs searchResult : result) {
            this.addItem(searchResult);
            this.setItemCaption(searchResult, searchResult.getQueryname());
        }
    }
}
