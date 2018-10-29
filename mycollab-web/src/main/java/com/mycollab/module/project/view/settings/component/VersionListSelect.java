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

import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.tracker.domain.Version;
import com.mycollab.module.tracker.domain.criteria.VersionSearchCriteria;
import com.mycollab.module.tracker.service.VersionService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.web.ui.IntegerKeyListSelect;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.5.0
 */
// TODO
public class VersionListSelect extends IntegerKeyListSelect {
    private static final long serialVersionUID = 1L;

    public VersionListSelect() {
//        this.setItemCaptionMode(ItemCaptionMode.EXPLICIT);
//        this.setMultiSelect(true);

        VersionSearchCriteria searchCriteria = new VersionSearchCriteria();
        searchCriteria.setStatus(StringSearchField.and(StatusI18nEnum.Open.name()));
        searchCriteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));

        VersionService versionService = AppContextUtil.getSpringBean(VersionService.class);
        List<Version> versions = (List<Version>) versionService.findPageableListByCriteria(new BasicSearchRequest<>(searchCriteria));
        for (Version version : versions) {
//            this.addItem(version.getId());
//            this.setItemCaption(version.getId(), version.getName());
        }

        this.setRows(4);
    }
}
