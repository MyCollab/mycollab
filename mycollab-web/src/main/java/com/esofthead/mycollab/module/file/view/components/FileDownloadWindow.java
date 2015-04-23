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
package com.esofthead.mycollab.module.file.view.components;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.utils.FileUtils;
import com.esofthead.mycollab.module.ecm.domain.Content;
import com.esofthead.mycollab.module.ecm.domain.Resource;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.resources.StreamDownloadResourceUtil;
import com.esofthead.mycollab.vaadin.resources.file.FileAssetsUtil;
import com.esofthead.mycollab.vaadin.ui.*;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class FileDownloadWindow extends Window {
    private static final long serialVersionUID = 1L;
    private final Content content;

    public FileDownloadWindow(final Content content) {
        super(content.getName());
        this.setWidth("400px");
        this.center();
        this.setResizable(false);
        this.setModal(true);

        this.content = content;
        this.constructBody();
    }

    private void constructBody() {
        final MVerticalLayout layout = new MVerticalLayout().withWidth("100%");
        CssLayout iconWrapper = new CssLayout();
        final FontIconLabel iconEmbed = new FontIconLabel(FileAssetsUtil.getFileIconResource(content.getName()));
        iconEmbed.addStyleName("icon-48px");
        iconWrapper.addComponent(iconEmbed);
        layout.with(iconWrapper).withAlign(iconWrapper, Alignment.MIDDLE_CENTER);

        final GridFormLayoutHelper info = new GridFormLayoutHelper(1, 4,
                "100%", "100px", Alignment.TOP_LEFT);
        info.getLayout().setWidth("100%");
        info.getLayout().setMargin(new MarginInfo(false, true, false, true));
        info.getLayout().setSpacing(false);

        if (this.content.getDescription() != null) {
            final Label desvalue = new Label();
            if (!this.content.getDescription().equals("")) {
                desvalue.setData(this.content.getDescription());
            } else {
                desvalue.setValue("&nbsp;");
                desvalue.setContentMode(ContentMode.HTML);
            }
            info.addComponent(desvalue, "Description", 0, 0);
        }

        UserService userService = ApplicationContextUtil.getSpringBean(UserService.class);
        SimpleUser user = userService.findUserByUserNameInAccount(content.getCreatedUser(), AppContext.getAccountId());
        if (user == null) {
            info.addComponent(new UserLink(AppContext.getUsername(), AppContext.getUserAvatarId(), AppContext.getUserDisplayName()), "Created by", 0, 1);
        } else {
            info.addComponent(new UserLink(user.getUsername(), user.getAvatarid(), user.getDisplayName()), "Created by", 0, 1);
        }


        final Label size = new Label(FileUtils.getVolumeDisplay(content.getSize()));
        info.addComponent(size, "Size", 0, 2);

        ELabel dateCreate = new ELabel().prettyDateTime(content.getCreated().getTime());
        info.addComponent(dateCreate, "Created date", 0, 3);

        layout.addComponent(info.getLayout());

        final MHorizontalLayout buttonControls = new MHorizontalLayout().withMargin(new MarginInfo(true, false, true, false));

        final Button downloadBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_DOWNLOAD));
        List<Resource> resources = new ArrayList<>();
        resources.add(content);

        StreamResource downloadResource = StreamDownloadResourceUtil
                .getStreamResourceSupportExtDrive(resources);

        FileDownloader fileDownloader = new FileDownloader(downloadResource);
        fileDownloader.extend(downloadBtn);
        downloadBtn.setIcon(FontAwesome.DOWNLOAD);
        downloadBtn.addStyleName(UIConstants.THEME_GREEN_LINK);

        buttonControls.with(downloadBtn).withAlign(downloadBtn,
                Alignment.MIDDLE_CENTER);

        final Button cancelBtn = new Button(
                AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL),
                new ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(final ClickEvent event) {
                        FileDownloadWindow.this.close();
                    }
                });
        cancelBtn.addStyleName(UIConstants.THEME_GRAY_LINK);
        buttonControls.with(cancelBtn).withAlign(cancelBtn,
                Alignment.MIDDLE_CENTER);
        layout.with(buttonControls).withAlign(buttonControls,
                Alignment.MIDDLE_CENTER);
        this.setContent(layout);
    }
}
