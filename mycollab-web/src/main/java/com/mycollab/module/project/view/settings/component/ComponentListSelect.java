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
package com.mycollab.module.project.view.settings.component;

import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.tracker.domain.Component;
import com.mycollab.module.tracker.domain.criteria.ComponentSearchCriteria;
import com.mycollab.module.tracker.service.ComponentService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.web.ui.IntegerKeyListSelect;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.5.0
 */
public class ComponentListSelect extends IntegerKeyListSelect {
    private static final long serialVersionUID = 1L;

    public ComponentListSelect() {
        this.setItemCaptionMode(ItemCaptionMode.EXPLICIT);
        this.setMultiSelect(true);

        ComponentSearchCriteria searchCriteria = new ComponentSearchCriteria();
        searchCriteria.setStatus(StringSearchField.and(StatusI18nEnum.Open.name()));
        searchCriteria.setProjectId(new NumberSearchField(SearchField.AND, CurrentProjectVariables.getProjectId()));

        ComponentService componentService = AppContextUtil.getSpringBean(ComponentService.class);
        List<Component> components = componentService.findPageableListByCriteria(new BasicSearchRequest<>(searchCriteria,
                0, Integer.MAX_VALUE));
        for (Component component : components) {
            this.addItem(component.getId());
            this.setItemCaption(component.getId(), component.getComponentname());
        }

        this.setRows(4);
    }
}
