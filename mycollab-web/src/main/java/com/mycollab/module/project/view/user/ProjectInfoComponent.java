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
package com.mycollab.module.project.view.user;

import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.html.DivLessFormatter;
import com.mycollab.module.file.StorageUtils;
import com.mycollab.module.project.ProjectLinkGenerator;
import com.mycollab.module.project.domain.SimpleProject;
import com.mycollab.module.project.event.ProjectEvent;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.module.project.i18n.ProjectI18nEnum;
import com.mycollab.module.project.ui.ProjectAssetsUtil;
import com.mycollab.module.project.view.ProjectBreadcrumb;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.1.2
 */
public class ProjectInfoComponent extends MHorizontalLayout {

    public ProjectInfoComponent(SimpleProject project) {
        this.withMargin(false).withFullWidth().withStyleName("top-panel").withId("tab-content-header");
        Component projectIcon = ProjectAssetsUtil.editableProjectLogoComp(project.getShortname(), project.getId(), project.getAvatarid(), 32);
        this.with(projectIcon).withAlign(projectIcon, Alignment.TOP_LEFT);

        ProjectBreadcrumb breadCrumb = ViewManager.getCacheComponent(ProjectBreadcrumb.class);
        breadCrumb.setProject(project);
        MVerticalLayout headerLayout = new MVerticalLayout().withSpacing(false).withMargin(new MarginInfo(false, true, false, true));

        MHorizontalLayout footer = new MHorizontalLayout().withStyleName(WebThemes.META_INFO, WebThemes.FLEX_DISPLAY);
        footer.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        headerLayout.with(breadCrumb, footer);

        if (project.getMemlead() != null) {
            Div leadAvatar = new DivLessFormatter().appendChild(new Img("", StorageUtils.getAvatarPath
                            (project.getLeadAvatarId(), 16)).setCSSClass(WebThemes.CIRCLE_BOX), DivLessFormatter.EMPTY_SPACE,
                    new A(ProjectLinkGenerator.generateProjectMemberLink(project.getId(), project.getMemlead()))
                            .appendText(StringUtils.trim(project.getLeadFullName(), 30, true)))
                    .setTitle(project.getLeadFullName());
            ELabel leadLbl = ELabel.html(UserUIContext.getMessage(ProjectI18nEnum.FORM_LEADER) + ": " + leadAvatar.write()).withUndefinedWidth();
            footer.with(leadLbl);
        }
        if (project.getHomepage() != null) {
            ELabel homepageLbl = ELabel.html(VaadinIcons.GLOBE.getHtml() + " " + new A(project.getHomepage())
                    .appendText(project.getHomepage()).setTarget("_blank").write())
                    .withStyleName(ValoTheme.LABEL_SMALL).withUndefinedWidth();
            homepageLbl.setDescription(UserUIContext.getMessage(ProjectI18nEnum.FORM_HOME_PAGE));
        }

        if (project.getClientid() != null && !SiteConfiguration.isCommunityEdition()) {
            Div clientDiv = new Div();
            if (project.getClientAvatarId() == null) {
                clientDiv.appendText(VaadinIcons.INSTITUTION.getHtml() + " ");
            } else {
                Img clientImg = new Img("", StorageUtils.getEntityLogoPath(AppUI.getAccountId(), project.getClientAvatarId(), 16))
                        .setCSSClass(WebThemes.CIRCLE_BOX);
                clientDiv.appendChild(clientImg).appendChild(DivLessFormatter.EMPTY_SPACE);
            }
            clientDiv.appendChild(new A(ProjectLinkGenerator.generateClientPreviewLink(project.getClientid()))
                    .appendText(StringUtils.trim(project.getClientName(), 30, true)));
            ELabel accountBtn = ELabel.html(clientDiv.write()).withStyleName(WebThemes.BUTTON_LINK)
                    .withUndefinedWidth();
            footer.addComponents(accountBtn);
        }

        if (!SiteConfiguration.isCommunityEdition()) {
            MButton tagBtn = new MButton(UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_TAG),
                    clickEvent -> EventBusFactory.getInstance().post(new ProjectEvent.GotoTagListView(this, null)))
                    .withIcon(VaadinIcons.TAGS).withStyleName(WebThemes.BUTTON_SMALL_PADDING, WebThemes.BUTTON_LINK);
            footer.addComponents(tagBtn);

            MButton favoriteBtn = new MButton(UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_FAVORITES),
                    clickEvent -> EventBusFactory.getInstance().post(new ProjectEvent.GotoFavoriteView(this, null)))
                    .withIcon(VaadinIcons.STAR).withStyleName(WebThemes.BUTTON_SMALL_PADDING, WebThemes.BUTTON_LINK);
            footer.addComponents(favoriteBtn);
        }

        this.with(headerLayout).expand(headerLayout);
    }
}
