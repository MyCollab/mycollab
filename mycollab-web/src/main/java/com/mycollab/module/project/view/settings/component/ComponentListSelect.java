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
package com.mycollab.module.project.view.settings.component;

import com.mycollab.vaadin.web.ui.IntegerKeyListSelect;

/**
 * @author MyCollab Ltd.
 * @since 4.5.0
 */
// TODO
public class ComponentListSelect extends IntegerKeyListSelect {
    private static final long serialVersionUID = 1L;

    public ComponentListSelect() {
//        this.setItemCaptionMode(ItemCaptionMode.EXPLICIT);
//        this.setMultiSelect(true);
//
//        ComponentSearchCriteria searchCriteria = new ComponentSearchCriteria();
//        searchCriteria.setStatus(StringSearchField.and(StatusI18nEnum.Open.name()));
//        searchCriteria.setProjectId(NumberSearchField.equal(CurrentProjectVariables.getProjectId()));
//
//        ComponentService componentService = AppContextUtil.getSpringBean(ComponentService.class);
//        List<Component> components = (List<Component>) componentService.findPageableListByCriteria(new BasicSearchRequest<>(searchCriteria));
//        for (Component component : components) {
//            this.addItem(component.getId());
//            this.setItemCaption(component.getId(), component.getName());
//        }

        this.setRows(4);
    }
}
