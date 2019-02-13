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
import com.mycollab.common.UrlEncodeDecoder;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.html.DivLessFormatter;
import com.mycollab.module.file.StorageUtils;
import com.mycollab.module.project.ProjectLinkGenerator;
import com.mycollab.module.project.domain.SimpleProject;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.module.project.i18n.ProjectI18nEnum;
import com.mycollab.module.project.ui.ProjectAssetsUtil;
import com.mycollab.module.project.view.ProjectBreadcrumb;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.1.2
 */
public class ProjectInfoComponent extends MHorizontalLayout {

    public ProjectInfoComponent(SimpleProject project) {
        this.withFullWidth().withStyleName("top-panel").withId("tab-content-header");
        Component projectIcon = ProjectAssetsUtil.editableProjectLogoComp(project.getShortname(), project.getId(), project.getAvatarid(), 64);
        this.with(projectIcon).withAlign(projectIcon, Alignment.TOP_LEFT);

        ProjectBreadcrumb breadCrumb = ViewManager.getCacheComponent(ProjectBreadcrumb.class);
        breadCrumb.setProject(project);
        MVerticalLayout headerLayout = new MVerticalLayout().withSpacing(false).withMargin(new MarginInfo(false, true, false, false));

        MCssLayout footer = new MCssLayout().withStyleName(WebThemes.META_INFO, WebThemes.FLEX_DISPLAY);
        headerLayout.with(breadCrumb, footer);

        if (StringUtils.isNotBlank(project.getMemlead())) {
            Div leadAvatar = new DivLessFormatter().appendChild(new Img("", StorageUtils.getAvatarPath
                            (project.getLeadAvatarId(), 16)).setCSSClass(WebThemes.CIRCLE_BOX), DivLessFormatter.EMPTY_SPACE,
                    new A(ProjectLinkGenerator.generateProjectMemberLink(project.getId(), project.getMemlead()))
                            .appendText(StringUtils.trim(project.getLeadFullName(), 30, true)))
                    .setTitle(project.getLeadFullName());
            ELabel leadLbl = ELabel.html(UserUIContext.getMessage(ProjectI18nEnum.FORM_LEADER) + ": " + leadAvatar.write()).withUndefinedWidth();
            footer.add(leadLbl, ELabel.html("&nbsp;&nbsp;&nbsp;&nbsp;"));
        }
        if (StringUtils.isNotBlank(project.getHomepage())) {
            ELabel homepageLbl = ELabel.html(VaadinIcons.GLOBE.getHtml() + " " + new A(project.getHomepage())
                    .appendText(project.getHomepage()).setTarget("_blank").write())
                    .withStyleName(ValoTheme.LABEL_SMALL).withUndefinedWidth();
            homepageLbl.setDescription(UserUIContext.getMessage(ProjectI18nEnum.FORM_HOME_PAGE));
            footer.add(homepageLbl, ELabel.html("&nbsp;&nbsp;&nbsp;&nbsp;"));
        }

        if (project.getPlanstartdate() != null) {
            ELabel dateLbl = ELabel.html(VaadinIcons.TIME_FORWARD.getHtml() + " " + UserUIContext.formatDate(project.getPlanstartdate()))
                    .withStyleName(ValoTheme.LABEL_SMALL).withDescription(UserUIContext.getMessage(GenericI18Enum.FORM_START_DATE)).withUndefinedWidth();
            footer.add(dateLbl, ELabel.html("&nbsp;&nbsp;&nbsp;&nbsp;"));
        }

        if (project.getPlanenddate() != null) {
            ELabel dateLbl = ELabel.html(VaadinIcons.TIME_BACKWARD.getHtml() + " " + UserUIContext.formatDate(project.getPlanenddate()))
                    .withStyleName(ValoTheme.LABEL_SMALL).withDescription(UserUIContext.getMessage(GenericI18Enum.FORM_END_DATE)).withUndefinedWidth();
            footer.add(dateLbl, ELabel.html("&nbsp;&nbsp;&nbsp;&nbsp;"));
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
            ELabel clientLink = ELabel.html(clientDiv.write()).withStyleName(WebThemes.BUTTON_LINK)
                    .withUndefinedWidth();
            footer.add(clientLink, ELabel.html("&nbsp;&nbsp;&nbsp;&nbsp;"));
        }

        if (!SiteConfiguration.isCommunityEdition()) {
            A tagLink = new A("#project/tag/" + UrlEncodeDecoder.encode(project.getId()))
                    .appendText(VaadinIcons.TAGS.getHtml() + " " + UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_TAG));
            footer.add(ELabel.html(tagLink.write()), ELabel.html("&nbsp;&nbsp;&nbsp;&nbsp;"));

            A favoriteLink = new A("#project/favorite/" + UrlEncodeDecoder.encode(project.getId()))
                    .appendText(VaadinIcons.STAR.getHtml() + " " + UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_FAVORITES));
            footer.addComponent(ELabel.html(favoriteLink.write()));
        }

        this.with(headerLayout).expand(headerLayout);
    }
}
