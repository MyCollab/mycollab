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
import com.mycollab.module.tracker.domain.Version;
import com.mycollab.module.tracker.domain.criteria.VersionSearchCriteria;
import com.mycollab.module.tracker.service.VersionService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.web.ui.MultiSelectComp;
import com.vaadin.data.Property;
import com.vaadin.ui.UI;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class VersionMultiSelectField extends MultiSelectComp {
    private static final long serialVersionUID = 1L;

    public VersionMultiSelectField() {
        super("versionname", true);
    }

    @Override
    protected List<Version> createData() {
        VersionSearchCriteria searchCriteria = new VersionSearchCriteria();
        searchCriteria.setStatus(StringSearchField.and(StatusI18nEnum.Open.name()));
        searchCriteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
        VersionService versionService = AppContextUtil.getSpringBean(VersionService.class);
        return versionService.findPageableListByCriteria(new BasicSearchRequest<>(searchCriteria));
    }

    @Override
    protected void requestAddNewComp() {
        UI.getCurrent().addWindow(new VersionAddWindow());
    }

    @Override
    public void setPropertyDataSource(Property newDataSource) {
        List<Version> versions = (List<Version>) newDataSource.getValue();
        if (versions != null) {
            this.setSelectedItems(versions);
        }
        super.setPropertyDataSource(newDataSource);
    }

    @Override
    public Class<?> getType() {
        return Object.class;
    }
}
