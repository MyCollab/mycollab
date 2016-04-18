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
package com.esofthead.mycollab.module.project.ui;

import com.esofthead.mycollab.configuration.StorageFactory;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.file.PathUtils;
import com.esofthead.mycollab.module.project.domain.Project;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.ELabel;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import org.vaadin.jouni.restrain.Restrain;

/**
 * @author MyCollab Ltd.
 * @since 5.0.0
 */
public class ProjectAssetsUtil {

    public static FontAwesome getPhaseIcon(String status) {
        if (OptionI18nEnum.MilestoneStatus.Closed.name().equals(status)) {
            return FontAwesome.MINUS_CIRCLE;
        } else if (OptionI18nEnum.MilestoneStatus.Future.name().equals(status)) {
            return FontAwesome.CLOCK_O;
        } else {
            return FontAwesome.SPINNER;
        }
    }

    public static final Component buildProjectLogo(Project project, int size) {
        AbstractComponent wrapper;
        if (!StringUtils.isBlank(project.getAvatarid())) {
            wrapper = new Image(null, new ExternalResource(StorageFactory.getInstance().getResourcePath
                    (String.format("%s/%s_%d.png", PathUtils.getProjectLogoPath(AppContext.getAccountId(), project
                            .getId()), project.getAvatarid(), size))));
        } else {
            ELabel projectIcon = new ELabel(project.getShortname()).withStyleName(UIConstants.TEXT_ELLIPSIS, "center");
            wrapper = new VerticalLayout();
            ((VerticalLayout) wrapper).addComponent(projectIcon);
            ((VerticalLayout) wrapper).setComponentAlignment(projectIcon, Alignment.MIDDLE_CENTER);
        }
        wrapper.setWidth(size, Sizeable.Unit.PIXELS);
        wrapper.setHeight(size, Sizeable.Unit.PIXELS);
        wrapper.addStyleName(UIConstants.CIRCLE_BOX);
        wrapper.setDescription("To change the project logo, go to project menu, select 'Edit Project' and upload the " +
                "new project logo");
        return wrapper;
    }
}
