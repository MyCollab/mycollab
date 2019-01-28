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
package com.mycollab.module.project.view.page;

import com.hp.gagawa.java.elements.*;
import com.mycollab.common.i18n.DayI18nEnum;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.utils.DateTimeUtils;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.html.DivLessFormatter;
import com.mycollab.module.file.StorageUtils;
import com.mycollab.module.page.domain.Page;
import com.mycollab.module.page.domain.PageVersion;
import com.mycollab.module.page.service.PageService;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectLinkGenerator;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.SimpleProjectMember;
import com.mycollab.module.project.i18n.PageI18nEnum;
import com.mycollab.module.project.service.ProjectMemberService;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.ui.components.ProjectActivityComponent;
import com.mycollab.module.project.ui.components.ProjectPreviewFormControlsGenerator;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.TooltipHelper;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.resources.LazyStreamSource;
import com.mycollab.vaadin.resources.OnDemandFileDownloader;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.AbstractPreviewItemComp;
import com.mycollab.vaadin.web.ui.AdvancedPreviewBeanForm;
import com.mycollab.vaadin.web.ui.ReadViewLayout;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.*;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Calendar;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
@ViewComponent
public class PageReadViewImpl extends AbstractPreviewItemComp<Page> implements PageReadView {
    private static final long serialVersionUID = 1L;

    private ProjectActivityComponent commentListComp;
    private PageVersionSelectionBox pageVersionsSelection;

    private PageVersion selectedVersion;
    private PageService pageService;

    public PageReadViewImpl() {
        super(UserUIContext.getMessage(PageI18nEnum.DETAIL), ProjectAssetsManager.getAsset(ProjectTypeConstants.PAGE), new PagePreviewFormLayout());
        pageService = AppContextUtil.getSpringBean(PageService.class);
    }

    @Override
    public Page getItem() {
        return beanItem;
    }

    @Override
    protected void initRelatedComponents() {
        commentListComp = new ProjectActivityComponent(ProjectTypeConstants.PAGE, CurrentProjectVariables.getProjectId());
    }

    @Override
    protected void onPreviewItem() {
        ((PagePreviewFormLayout) previewLayout).displayPageInfo(beanItem);
        commentListComp.loadActivities(beanItem.getPath());
        pageVersionsSelection.displayVersions(beanItem.getPath());
    }

    @Override
    protected String initFormTitle() {
        return beanItem.getSubject();
    }

    @Override
    protected AdvancedPreviewBeanForm<Page> initPreviewForm() {
        return new PagePreviewForm();
    }

    @Override
    public HasPreviewFormHandlers<Page> getPreviewFormHandlers() {
        return previewForm;
    }

    @Override
    protected HorizontalLayout createButtonControls() {
        ProjectPreviewFormControlsGenerator<Page> pagesPreviewForm = new ProjectPreviewFormControlsGenerator<>(previewForm);
        HorizontalLayout buttonControls = pagesPreviewForm.createButtonControls(
                ProjectPreviewFormControlsGenerator.ADD_BTN_PRESENTED
                        | ProjectPreviewFormControlsGenerator.EDIT_BTN_PRESENTED
                        | ProjectPreviewFormControlsGenerator.DELETE_BTN_PRESENTED,
                ProjectRolePermissionCollections.PAGES);

        MButton exportPdfBtn = new MButton("").withIcon(VaadinIcons.FILE_O).withStyleName(WebThemes
                .BUTTON_OPTION).withDescription(UserUIContext.getMessage(GenericI18Enum.BUTTON_EXPORT_PDF));

        OnDemandFileDownloader fileDownloader = new OnDemandFileDownloader(new LazyStreamSource() {
            @Override
            protected StreamResource.StreamSource buildStreamSource() {
                return new PageReportStreamSource(beanItem);
            }

            @Override
            public String getFilename() {
                return "Document.pdf";
            }
        });
        fileDownloader.extend(exportPdfBtn);

        pagesPreviewForm.insertToControlBlock(exportPdfBtn);

        pageVersionsSelection = new PageVersionSelectionBox();
        pagesPreviewForm.insertToControlBlock(pageVersionsSelection);
        return buttonControls;
    }

    @Override
    protected ComponentContainer createBottomPanel() {
        return commentListComp;
    }

    @Override
    protected String getType() {
        return ProjectTypeConstants.PAGE;
    }

    private static class PagePreviewFormLayout extends ReadViewLayout {
        void displayPageInfo(Page beanItem) {
            MVerticalLayout header = new MVerticalLayout().withMargin(false);
            ELabel titleLbl = ELabel.h3(beanItem.getSubject());
            header.with(titleLbl);
            Div footer = new Div().setStyle("width:100%").setCSSClass(WebThemes.META_INFO);
            Span lastUpdatedTimeTxt = new Span().appendText(UserUIContext.getMessage(DayI18nEnum.LAST_UPDATED_ON,
                    UserUIContext.formatPrettyTime(DateTimeUtils.toLocalDateTime(beanItem.getLastUpdatedTime()))))
                    .setTitle(UserUIContext.formatDateTime(DateTimeUtils.toLocalDateTime(beanItem.getLastUpdatedTime())));

            ProjectMemberService projectMemberService = AppContextUtil.getSpringBean(ProjectMemberService.class);
            SimpleProjectMember member = projectMemberService.findMemberByUsername(beanItem.getCreatedUser(),
                    CurrentProjectVariables.getProjectId(), AppUI.getAccountId());
            if (member != null) {
                Img userAvatar = new Img("", StorageUtils.getAvatarPath(member.getMemberAvatarId(), 16))
                        .setCSSClass(WebThemes.CIRCLE_BOX);
                A userLink = new A().setId("tag" + TooltipHelper.TOOLTIP_ID).
                        setHref(ProjectLinkGenerator.generateProjectMemberLink(member.getProjectid(),
                                member.getUsername())).
                        appendText(StringUtils.trim(member.getMemberFullName(), 30, true));
                userLink.setAttribute("onmouseover", TooltipHelper.userHoverJsFunction(member.getUsername()));
                userLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction());
                footer.appendChild(lastUpdatedTimeTxt, new Text("&nbsp;-&nbsp;" + UserUIContext.getMessage
                                (GenericI18Enum.OPT_CREATED_BY) + ": "), userAvatar,
                        DivLessFormatter.EMPTY_SPACE, userLink, DivLessFormatter.EMPTY_SPACE);
            } else {
                footer.appendChild(lastUpdatedTimeTxt);
            }

            header.addComponent(ELabel.html(footer.write()));
            this.addHeader(header);
        }
    }

    private class PageVersionSelectionBox extends CustomComponent {
        private static final long serialVersionUID = 1L;

        private HorizontalLayout content;

        private PageVersionSelectionBox() {
            content = new HorizontalLayout();
            this.setCompositionRoot(content);
        }

        void displayVersions(String path) {
            List<PageVersion> versions = pageService.getPageVersions(path);
            if (versions.size() > 0) {
                final ComboBox<PageVersion> pageSelection = new ComboBox<>();
                content.addComponent(pageSelection);
                pageSelection.setEmptySelectionAllowed(false);
                pageSelection.setTextInputAllowed(false);

                pageSelection.addValueChangeListener(valueChangeEvent -> {
                    selectedVersion = pageSelection.getValue();
                    if (selectedVersion != null) {
                        Page page = pageService.getPageByVersion(beanItem.getPath(), selectedVersion.getName());
                        page.setPath(beanItem.getPath());
                        previewForm.setBean(page);
                        ((PagePreviewFormLayout) previewLayout).displayPageInfo(page);
                    }
                });

                pageSelection.setEmptySelectionAllowed(false);
                pageSelection.setItems(versions);
                pageSelection.setItemCaptionGenerator((ItemCaptionGenerator<PageVersion>) this::getVersionDisplay);

                if (versions.size() > 0) {
                    pageSelection.setValue(versions.get(versions.size() - 1));
                }
            }
        }

        String getVersionDisplay(PageVersion version) {
            Calendar createdTime = version.getCreatedTime();
            return UserUIContext.formatDateTime(DateTimeUtils.toLocalDateTime(createdTime));
        }
    }
}