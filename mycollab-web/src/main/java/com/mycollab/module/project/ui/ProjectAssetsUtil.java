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
package com.mycollab.module.project.ui;

import com.mycollab.configuration.StorageFactory;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.module.crm.domain.SimpleAccount;
import com.mycollab.module.file.PathUtils;
import com.mycollab.module.project.i18n.ClientI18nEnum;
import com.mycollab.module.project.i18n.OptionI18nEnum.MilestoneStatus;
import com.mycollab.module.project.i18n.ProjectI18nEnum;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.UIConstants;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author MyCollab Ltd.
 * @since 5.0.0
 */
public class ProjectAssetsUtil {

    public static FontAwesome getPhaseIcon(String status) {
        if (MilestoneStatus.Closed.name().equals(status)) {
            return FontAwesome.MINUS_CIRCLE;
        } else if (MilestoneStatus.Future.name().equals(status)) {
            return FontAwesome.CLOCK_O;
        } else {
            return FontAwesome.SPINNER;
        }
    }

    public static Component buildProjectLogo(String projectShortname, Integer projectId, String projectAvatarId, int size) {
        AbstractComponent wrapper;
        if (!StringUtils.isBlank(projectAvatarId)) {
            wrapper = new Image(null, new ExternalResource(StorageFactory.getResourcePath
                    (String.format("%s/%s_%d.png", PathUtils.getProjectLogoPath(MyCollabUI.getAccountId(), projectId),
                            projectAvatarId, size))));
        } else {
            ELabel projectIcon = new ELabel(projectShortname).withStyleName(UIConstants.TEXT_ELLIPSIS, ValoTheme.LABEL_LARGE, "center");
            wrapper = new VerticalLayout();
            ((VerticalLayout) wrapper).addComponent(projectIcon);
            ((VerticalLayout) wrapper).setComponentAlignment(projectIcon, Alignment.MIDDLE_CENTER);
        }
        wrapper.setWidth(size, Sizeable.Unit.PIXELS);
        wrapper.setHeight(size, Sizeable.Unit.PIXELS);
        wrapper.addStyleName(UIConstants.CIRCLE_BOX);
        wrapper.setDescription(UserUIContext.getMessage(ProjectI18nEnum.OPT_CHANGE_LOGO_HELP, UserUIContext.getMessage(ProjectI18nEnum.EDIT)));
        return wrapper;
    }

    public static Component buildClientLogo(SimpleAccount account, int size) {
        AbstractComponent wrapper;
        if (!StringUtils.isBlank(account.getAvatarid())) {
            wrapper = new Image(null, new ExternalResource(StorageFactory.getEntityLogoPath(MyCollabUI
                    .getAccountId(), account.getAvatarid(), 100)));
        } else {
            String accountName = account.getAccountname();
            accountName = (accountName.length() > 3) ? accountName.substring(0, 3) : accountName;
            ELabel projectIcon = new ELabel(accountName).withStyleName(UIConstants.TEXT_ELLIPSIS, "center");
            wrapper = new VerticalLayout();
            ((VerticalLayout) wrapper).addComponent(projectIcon);
            ((VerticalLayout) wrapper).setComponentAlignment(projectIcon, Alignment.MIDDLE_CENTER);
        }
        wrapper.setWidth(size, Sizeable.Unit.PIXELS);
        wrapper.setHeight(size, Sizeable.Unit.PIXELS);
        wrapper.addStyleName(UIConstants.CIRCLE_BOX);
        wrapper.setDescription(UserUIContext.getMessage(ClientI18nEnum.OPT_CHANGE_LOGO_HELP, UserUIContext.getMessage(ClientI18nEnum.EDIT)));
        return wrapper;
    }
}
