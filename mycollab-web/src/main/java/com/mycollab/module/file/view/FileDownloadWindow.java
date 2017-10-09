package com.mycollab.module.file.view;

import com.mycollab.common.i18n.FileI18nEnum;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.utils.FileUtils;
import com.mycollab.module.ecm.domain.Content;
import com.mycollab.module.ecm.domain.Resource;
import com.mycollab.module.user.domain.SimpleUser;
import com.mycollab.module.user.service.UserService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.resources.StreamDownloadResourceUtil;
import com.mycollab.vaadin.resources.file.FileAssetsUtil;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.UserLink;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class FileDownloadWindow extends MWindow {
    private static final long serialVersionUID = 1L;
    private final Content content;

    FileDownloadWindow(final Content content) {
        super(content.getName());
        withModal(true).withResizable(false).withCenter().withWidth("500px");

        this.content = content;
        this.constructBody();
    }

    private void constructBody() {
        final MVerticalLayout layout = new MVerticalLayout().withFullWidth();
        CssLayout iconWrapper = new CssLayout();
        final ELabel iconEmbed = ELabel.fontIcon(FileAssetsUtil.getFileIconResource(content.getName()));
        iconEmbed.addStyleName("icon-48px");
        iconWrapper.addComponent(iconEmbed);
        layout.with(iconWrapper).withAlign(iconWrapper, Alignment.MIDDLE_CENTER);

        final GridFormLayoutHelper infoLayout = GridFormLayoutHelper.defaultFormLayoutHelper(1, 4);

        if (content.getDescription() != null) {
            final Label descLbl = new Label();
            if (!content.getDescription().equals("")) {
                descLbl.setData(content.getDescription());
            } else {
                descLbl.setValue("&nbsp;");
                descLbl.setContentMode(ContentMode.HTML);
            }
            infoLayout.addComponent(descLbl, UserUIContext.getMessage(GenericI18Enum.FORM_DESCRIPTION), 0, 0);
        }

        UserService userService = AppContextUtil.getSpringBean(UserService.class);
        SimpleUser user = userService.findUserByUserNameInAccount(content.getCreatedUser(), AppUI.getAccountId());
        if (user == null) {
            infoLayout.addComponent(new UserLink(UserUIContext.getUsername(), UserUIContext.getUserAvatarId(),
                    UserUIContext.getUserDisplayName()), UserUIContext.getMessage(GenericI18Enum.OPT_CREATED_BY), 0, 1);
        } else {
            infoLayout.addComponent(new UserLink(user.getUsername(), user.getAvatarid(), user.getDisplayName()),
                    UserUIContext.getMessage(GenericI18Enum.OPT_CREATED_BY), 0, 1);
        }

        final Label size = new Label(FileUtils.getVolumeDisplay(content.getSize()));
        infoLayout.addComponent(size, UserUIContext.getMessage(FileI18nEnum.OPT_SIZE), 0, 2);

        ELabel dateCreate = new ELabel().prettyDateTime(content.getCreated().getTime());
        infoLayout.addComponent(dateCreate, UserUIContext.getMessage(GenericI18Enum.FORM_CREATED_TIME), 0, 3);

        layout.addComponent(infoLayout.getLayout());

        MButton downloadBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_DOWNLOAD))
                .withIcon(FontAwesome.DOWNLOAD).withStyleName(WebThemes.BUTTON_ACTION);
        List<Resource> resources = new ArrayList<>();
        resources.add(content);

        StreamResource downloadResource = StreamDownloadResourceUtil.getStreamResourceSupportExtDrive(resources);

        FileDownloader fileDownloader = new FileDownloader(downloadResource);
        fileDownloader.extend(downloadBtn);

        MButton cancelBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CANCEL), clickEvent -> close())
                .withStyleName(WebThemes.BUTTON_OPTION);
        final MHorizontalLayout buttonControls = new MHorizontalLayout(cancelBtn, downloadBtn);
        layout.with(buttonControls).withAlign(buttonControls, Alignment.MIDDLE_RIGHT);
        this.setContent(layout);
    }
}
