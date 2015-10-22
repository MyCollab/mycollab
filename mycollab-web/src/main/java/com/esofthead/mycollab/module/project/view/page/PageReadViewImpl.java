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
package com.esofthead.mycollab.module.project.view.page;

import com.esofthead.mycollab.common.i18n.DayI18nEnum;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.i18n.WikiI18nEnum;
import com.esofthead.mycollab.configuration.StorageFactory;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.html.DivLessFormatter;
import com.esofthead.mycollab.module.page.domain.Page;
import com.esofthead.mycollab.module.page.domain.PageVersion;
import com.esofthead.mycollab.module.page.service.PageService;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.i18n.Page18InEnum;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.module.project.ui.components.AbstractPreviewItemComp;
import com.esofthead.mycollab.module.project.ui.components.CommentDisplay;
import com.esofthead.mycollab.module.project.ui.components.ProjectViewHeader;
import com.esofthead.mycollab.schedule.email.project.ProjectPageRelayEmailNotificationAction;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.utils.TooltipHelper;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.*;
import com.esofthead.mycollab.vaadin.ui.form.field.I18nFormViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.RichTextViewField;
import com.hp.gagawa.java.elements.*;
import com.lowagie.text.DocumentException;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
@ViewComponent
public class PageReadViewImpl extends AbstractPreviewItemComp<Page> implements PageReadView {
    private static final long serialVersionUID = 1L;
    private static final String XHTML_PAGE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">"
            + "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><title>%s</title></head><body><h1>%s</h1><br/>%s" +
            "</body></html>";

    private static final Logger LOG = LoggerFactory.getLogger(PageReadViewImpl.class);

    private CommentDisplay commentListComp;
    private PageVersionSelectionBox pageVersionsSelection;

    private PageVersion selectedVersion;
    private PageService pageService;

    public PageReadViewImpl() {
        super(new MHorizontalLayout().withMargin(true), new PagePreviewFormLayout());
        pageService = ApplicationContextUtil.getSpringBean(PageService.class);
        constructHeader();
    }

    private void constructHeader() {
        pageVersionsSelection = new PageVersionSelectionBox();

        ProjectViewHeader headerLbl = new ProjectViewHeader(ProjectTypeConstants.PAGE, AppContext.getMessage(Page18InEnum.VIEW_READ_TITLE));
        headerLbl.setWidthUndefined();
        headerLbl.setStyleName(UIConstants.HEADER_TEXT);

        ((MHorizontalLayout) header).addComponent(headerLbl, 0);
        ((MHorizontalLayout) header).addComponent(pageVersionsSelection, 1);
        ((MHorizontalLayout) header).withWidth("100%").withStyleName("hdr-view").expand(pageVersionsSelection).alignAll(Alignment.MIDDLE_LEFT);
    }

    @Override
    public Page getItem() {
        return beanItem;
    }

    @Override
    protected void initRelatedComponents() {
        commentListComp = new CommentDisplay(ProjectTypeConstants.PAGE,
                CurrentProjectVariables.getProjectId(), ProjectPageRelayEmailNotificationAction.class);
        commentListComp.setWidth("100%");
        commentListComp.setMargin(true);
    }

    @Override
    protected void onPreviewItem() {
        ((PagePreviewFormLayout) previewLayout).displayPageInfo(beanItem);
        commentListComp.loadComments(beanItem.getPath());
        pageVersionsSelection.displayVersions(beanItem.getPath());
    }

    @Override
    protected String initFormTitle() {
        return beanItem.getSubject();
    }

    @Override
    protected AdvancedPreviewBeanForm<Page> initPreviewForm() {
        return new AdvancedPreviewBeanForm<>();
    }

    @Override
    protected IFormLayoutFactory initFormLayoutFactory() {
        return new PageReadFormLayout();
    }

    @Override
    public HasPreviewFormHandlers<Page> getPreviewFormHandlers() {
        return previewForm;
    }

    @Override
    protected AbstractBeanFieldGroupViewFieldFactory<Page> initBeanFormFieldFactory() {
        return new PageReadFormFieldFactory(previewForm);
    }

    @Override
    protected ComponentContainer createButtonControls() {
        ProjectPreviewFormControlsGenerator<Page> pagesPreviewForm = new ProjectPreviewFormControlsGenerator<>(
                previewForm);
        HorizontalLayout topPanel = pagesPreviewForm.createButtonControls(
                ProjectPreviewFormControlsGenerator.ADD_BTN_PRESENTED
                        | ProjectPreviewFormControlsGenerator.EDIT_BTN_PRESENTED
                        | ProjectPreviewFormControlsGenerator.DELETE_BTN_PRESENTED,
                ProjectRolePermissionCollections.PAGES);

        Button exportPdfBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_EXPORT_PDF), FontAwesome.EXTERNAL_LINK);
        exportPdfBtn.setStyleName(UIConstants.THEME_GREEN_LINK);

        FileDownloader fileDownloader = new FileDownloader(getPDFStream());
        fileDownloader.extend(exportPdfBtn);

        pagesPreviewForm.insertToControlBlock(exportPdfBtn);

        return topPanel;
    }

    private StreamResource getPDFStream() {
        return new StreamResource(new StreamSource() {
            private static final long serialVersionUID = 1L;

            public InputStream getStream() {
                try {
                    return new FileInputStream(writePdf());
                } catch (Exception e) {
                    LOG.error("Error while export PDF", e);
                    return null;
                }
            }
        }, "Document.pdf");
    }

    private File writePdf() throws IOException, DocumentException {
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(String.format(XHTML_PAGE, beanItem.getSubject(), beanItem.getSubject(), beanItem.getContent()));
        renderer.layout();
        File file = File.createTempFile(beanItem.getSubject(), "pdf");
        file.deleteOnExit();
        try (OutputStream os = new FileOutputStream(file)) {
            renderer.createPDF(os);
        }
        return file;
    }

    @Override
    protected ComponentContainer createBottomPanel() {
        TabSheetLazyLoadComponent tabContainer = new TabSheetLazyLoadComponent();
        tabContainer.addTab(this.commentListComp, AppContext.getMessage(GenericI18Enum.TAB_COMMENT), FontAwesome.COMMENTS);
        return tabContainer;
    }

    @Override
    protected String getType() {
        return ProjectTypeConstants.PAGE;
    }

    private static class PageReadFormFieldFactory extends AbstractBeanFieldGroupViewFieldFactory<Page> {
        private static final long serialVersionUID = 1L;

        public PageReadFormFieldFactory(GenericBeanForm<Page> form) {
            super(form);
        }

        @Override
        protected Field<?> onCreateField(java.lang.Object propertyId) {
            if (propertyId.equals("status")) {
                return new I18nFormViewField(attachForm.getBean().getStatus(), WikiI18nEnum.class);
            } else if (propertyId.equals("content")) {
                return new RichTextViewField(attachForm.getBean().getContent());
            }
            return null;
        }
    }

    private static class PageReadFormLayout implements IFormLayoutFactory {
        private static final long serialVersionUID = 1L;

        private MVerticalLayout layout;

        @Override
        public ComponentContainer getLayout() {
            layout = new MVerticalLayout().withStyleName("page-read-layout").withWidth("100%");
            return layout;
        }

        @Override
        public void attachField(java.lang.Object propertyId, Field<?> field) {
            if (propertyId.equals("content")) {
                layout.addComponent(field);
            }
        }
    }

    private static class PagePreviewFormLayout extends ReadViewLayout {
        void displayPageInfo(Page beanItem) {
            MVerticalLayout header = new MVerticalLayout().withMargin(false);
            Label titleLbl = new Label(beanItem.getSubject());
            titleLbl.setStyleName("headerName");
            header.with(titleLbl);
            Div footer = new Div().setStyle("width:100%").setCSSClass("footer2");
            Span lastUpdatedTimeTxt = new Span().appendText(AppContext.getMessage(DayI18nEnum.LAST_UPDATED_ON,
                    AppContext.formatPrettyTime(beanItem.getLastUpdatedTime().getTime())))
                    .setTitle(AppContext.formatDateTime(beanItem.getLastUpdatedTime().getTime()));
            String uid = UUID.randomUUID().toString();
            ProjectMemberService projectMemberService = ApplicationContextUtil
                    .getSpringBean(ProjectMemberService.class);
            SimpleProjectMember member = projectMemberService.findMemberByUsername(beanItem.getCreatedUser(),
                    CurrentProjectVariables.getProjectId(), AppContext.getAccountId());
            if (member != null) {
                Img userAvatar = new Img("", StorageFactory.getInstance().getAvatarPath(member.getMemberAvatarId(), 16));
                A userLink = new A().setId("tag" + uid).setHref(ProjectLinkBuilder.generateProjectMemberFullLink(member
                        .getProjectid(), member.getUsername())).appendText(StringUtils.trim(member.getMemberFullName(), 30, true));
                userLink.setAttribute("onmouseover", TooltipHelper.userHoverJsFunction(uid, member.getUsername()));
                userLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction(uid));
                footer.appendChild(lastUpdatedTimeTxt, new Text("&nbsp;-&nbsp;Created by: "), userAvatar, DivLessFormatter.EMPTY_SPACE(), userLink,
                        DivLessFormatter.EMPTY_SPACE(), TooltipHelper.buildDivTooltipEnable(uid));
            } else {
                footer.appendChild(lastUpdatedTimeTxt);
            }

            header.addComponent(new Label(footer.write(), ContentMode.HTML));
            this.addHeader(header);
        }

        @Override
        public void setTitle(String title) {
        }
    }

    private class PageVersionSelectionBox extends CustomComponent {
        private static final long serialVersionUID = 1L;

        private HorizontalLayout content;

        public PageVersionSelectionBox() {
            content = new HorizontalLayout();
            this.setCompositionRoot(content);
        }

        void displayVersions(String path) {
            List<PageVersion> pageVersions = pageService.getPageVersions(path);
            if (pageVersions.size() > 0) {
                final ComboBox pageSelection = new ComboBox();
                content.addComponent(pageSelection);
                pageSelection.setNullSelectionAllowed(false);
                pageSelection.setStyleName("version-selection-box");
                pageSelection.setTextInputAllowed(false);

                pageSelection.addValueChangeListener(new ValueChangeListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
                        selectedVersion = (PageVersion) pageSelection.getValue();
                        if (selectedVersion != null) {
                            Page page = pageService.getPageByVersion(beanItem.getPath(), selectedVersion.getName());
                            page.setPath(beanItem.getPath());
                            previewForm.setBean(page);
                            previewLayout.setTitle(page.getSubject());
                            ((PagePreviewFormLayout) previewLayout).displayPageInfo(page);
                        }
                    }
                });

                pageSelection.setItemCaptionMode(ItemCaptionMode.EXPLICIT);
                pageSelection.setNullSelectionAllowed(false);

                for (int i = 0; i < pageVersions.size(); i++) {
                    PageVersion version = pageVersions.get(i);
                    pageSelection.addItem(version);
                    pageSelection.setItemCaption(version, getVersionDisplay(version, i));
                }

                if (pageVersions.size() > 0) {
                    pageSelection.setValue(pageVersions.get(pageVersions.size() - 1));
                }
            }
        }

        String getVersionDisplay(PageVersion version, int index) {
            String vFormat = "%s (%s)";
            Calendar createdTime = version.getCreatedTime();
            String date = AppContext.formatDateTime(createdTime.getTime());
            return String.format(vFormat, "V" + (index + 1), date);
        }
    }
}