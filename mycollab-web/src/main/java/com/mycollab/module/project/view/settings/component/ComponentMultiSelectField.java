/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view.settings.component;

import com.mycollab.common.i18n.OptionI18nEnum;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.tracker.domain.Component;
import com.mycollab.module.tracker.domain.criteria.ComponentSearchCriteria;
import com.mycollab.module.tracker.service.ComponentService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.web.ui.MultiSelectComp;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.ui.UI;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ComponentMultiSelectField extends MultiSelectComp<Component> {
    private static final long serialVersionUID = 1L;

    public ComponentMultiSelectField() {
        super("name", true);
        this.setWidth(WebThemes.FORM_CONTROL_WIDTH);
    }

    @Override
    protected List<Component> createData() {
        ComponentSearchCriteria searchCriteria = new ComponentSearchCriteria();
        searchCriteria.setStatus(StringSearchField.and(OptionI18nEnum.StatusI18nEnum.Open.name()));
        searchCriteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));

        ComponentService componentService = AppContextUtil.getSpringBean(ComponentService.class);
        return (List<Component>) componentService.findPageableListByCriteria(new BasicSearchRequest<>(searchCriteria));
    }

    @Override
    protected void requestAddNewComp() {
        UI.getCurrent().addWindow(new ComponentAddWindow());
    }

    @Override
    protected void doSetValue(List<Component> value) {
        setSelectedItems(value);
    }

    @Override
    public List<Component> getValue() {
        return selectedItems;
    }
}
