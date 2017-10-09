package com.mycollab.module.crm.view.account;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.crm.domain.SimpleAccount;
import com.mycollab.module.crm.service.AccountService;
import com.mycollab.module.crm.ui.components.CrmAssetsUtil;
import com.mycollab.module.file.PathUtils;
import com.mycollab.module.file.service.EntityUploaderService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.web.ui.ImagePreviewCropWindow;
import com.mycollab.vaadin.web.ui.UploadImageField;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

import java.awt.image.BufferedImage;

/**
 * @author MyCollab Ltd
 * @since 5.4.8
 */
public class AccountLogoUpdateWindow extends MWindow implements ImagePreviewCropWindow.ImageSelectionCommand {
    private SimpleAccount account;

    public AccountLogoUpdateWindow(SimpleAccount account) {
        super(UserUIContext.getMessage(GenericI18Enum.OPT_UPLOAD_IMAGE));
        this.account = account;
        withModal(true).withResizable(false).withWidth("200px").withCenter();
        Component accountIcon = CrmAssetsUtil.accountLogoComp(account, 100);
        accountIcon.setWidthUndefined();
        final UploadImageField avatarUploadField = new UploadImageField(this);
        withContent(new MVerticalLayout(accountIcon, avatarUploadField)
                .withDefaultComponentAlignment(Alignment.TOP_CENTER));
    }

    @Override
    public void process(BufferedImage image) {
        EntityUploaderService entityUploaderService = AppContextUtil.getSpringBean(EntityUploaderService.class);
        String newLogoId = entityUploaderService.upload(image, PathUtils.getEntityLogoPath(AppUI.getAccountId()),
                account.getAvatarid(), UserUIContext.getUsername(), AppUI.getAccountId(),
                new Integer[]{16, 32, 48, 64, 100});
        AccountService accountService = AppContextUtil.getSpringBean(AccountService.class);
        account.setAvatarid(newLogoId);
        accountService.updateSelectiveWithSession(account, UserUIContext.getUsername());
        Page.getCurrent().getJavaScript().execute("window.location.reload();");
    }
}
