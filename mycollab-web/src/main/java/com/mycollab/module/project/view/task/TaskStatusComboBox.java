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
package com.mycollab.module.project.view.task;

import com.mycollab.common.domain.OptionVal;
import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.mycollab.common.service.OptionValService;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.ui.OptionValComboBox;

import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.1.1
 */
// TODO
public class TaskStatusComboBox extends OptionValComboBox {

    public TaskStatusComboBox() {
        super(StatusI18nEnum.class);
        OptionValService optionValService = AppContextUtil.getSpringBean(OptionValService.class);
        List<OptionVal> options = optionValService.findOptionVals(ProjectTypeConstants.TASK,
                CurrentProjectVariables.getProjectId(), AppUI.getAccountId());
//        options.forEach(this::addEntry);
    }

//    @Override
//    public void setPropertyDataSource(Property newDataSource) {
//        Object value = newDataSource.getValue();
//        if (value == null) {
//            newDataSource.setValue(StatusI18nEnum.Open.name());
//        }
//        super.setPropertyDataSource(newDataSource);
//    }
}
