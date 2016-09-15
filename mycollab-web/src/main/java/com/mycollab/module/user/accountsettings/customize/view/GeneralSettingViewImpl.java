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
package com.mycollab.module.user.accountsettings.customize.view;

import com.mycollab.common.i18n.FileI18nEnum;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.configuration.StorageFactory;
import com.mycollab.core.MyCollabException;
import com.mycollab.core.UserInvalidInputException;
import com.mycollab.core.utils.DateTimeUtils;
import com.mycollab.core.utils.ImageUtil;
import com.mycollab.core.utils.TimezoneVal;
import com.mycollab.i18n.LocalizationHelper;
import com.mycollab.module.file.service.AccountFavIconService;
import com.mycollab.module.user.accountsettings.localization.AdminI18nEnum;
import com.mycollab.module.user.domain.SimpleBillingAccount;
import com.mycollab.module.user.service.BillingAccountService;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.AbstractPageView;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.AccountAssetsResolver;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.FormContainer;
import com.mycollab.vaadin.web.ui.ServiceMenu;
import com.mycollab.vaadin.web.ui.WebUIConstants;
import com.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.mycollab.web.CustomLayoutExt;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.vaadin.easyuploads.UploadField;
import org.vaadin.teemu.VaadinIcons;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Currency;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author MyCollab Ltd
 * @since 5.1.0
 */
@ViewComponent
public class GeneralSettingViewImpl extends AbstractPageView implements GeneralSettingView {
    private SimpleBillingAccount billingAccount;

    public GeneralSettingViewImpl() {
        this.setMargin(true);
    }

    @Override
    public void displayView() {
        removeAllComponents();

        billingAccount = MyCollabUI.getBillingAccount();
        FormContainer formContainer = new FormContainer();
        this.addComponent(formContainer);

        MHorizontalLayout generalSettingHeader = new MHorizontalLayout();
        Label headerLbl = new Label(UserUIContext.getMessage(AdminI18nEnum.OPT_GENERAL_SETTINGS));

        MButton editBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_EDIT), clickEvent -> UI.getCurrent().addWindow(new AccountInfoChangeWindow()))
                .withStyleName(WebUIConstants.BUTTON_LINK);

        generalSettingHeader.with(headerLbl, editBtn).alignAll(Alignment.MIDDLE_LEFT);

        GridFormLayoutHelper gridFormLayoutHelper = GridFormLayoutHelper.defaultFormLayoutHelper(2, 5, "200px");
        gridFormLayoutHelper.addComponent(new Label(billingAccount.getSitename()),
                UserUIContext.getMessage(AdminI18nEnum.FORM_SITE_NAME), 0, 0);
        gridFormLayoutHelper.addComponent(new Label(String.format("https://%s.mycollab.com", billingAccount
                .getSubdomain())), UserUIContext.getMessage(AdminI18nEnum.FORM_SITE_ADDRESS), 0, 1);
        gridFormLayoutHelper.addComponent(new Label(TimezoneVal.getDisplayName(UserUIContext.getUserLocale(),
                billingAccount.getDefaulttimezone())), UserUIContext.getMessage(AdminI18nEnum.FORM_DEFAULT_TIMEZONE), 0, 2);
        Currency defaultCurrency = billingAccount.getCurrencyInstance();
        gridFormLayoutHelper.addComponent(new ELabel(defaultCurrency.getDisplayName(UserUIContext.getUserLocale())),
                UserUIContext.getMessage(AdminI18nEnum.FORM_DEFAULT_CURRENCY), 0, 3);

        Date now = new GregorianCalendar().getTime();
        String defaultFullDateFormat = billingAccount.getDateFormatInstance();
        gridFormLayoutHelper.addComponent(new Label(String.format("%s (%s)",
                DateTimeUtils.formatDate(now, billingAccount.getDateFormatInstance(), UserUIContext.getUserLocale()), defaultFullDateFormat)),
                UserUIContext.getMessage(AdminI18nEnum.FORM_DEFAULT_YYMMDD_FORMAT),
                UserUIContext.getMessage(GenericI18Enum.FORM_DATE_FORMAT_HELP), 1, 0);

        String defaultShortDateFormat = billingAccount.getShortDateFormatInstance();
        gridFormLayoutHelper.addComponent(new Label(String.format("%s (%s)",
                DateTimeUtils.formatDate(now, billingAccount.getShortDateFormatInstance(), UserUIContext.getUserLocale()),
                defaultShortDateFormat)), UserUIContext.getMessage(AdminI18nEnum.FORM_DEFAULT_MMDD_FORMAT),
                UserUIContext.getMessage(GenericI18Enum.FORM_DATE_FORMAT_HELP), 1, 1);

        String defaultLongDateFormat = billingAccount.getLongDateFormatInstance();
        gridFormLayoutHelper.addComponent(new Label(String.format("%s (%s)",
                DateTimeUtils.formatDate(now, billingAccount.getLongDateFormatInstance(), UserUIContext.getUserLocale()), defaultLongDateFormat)),
                UserUIContext.getMessage(AdminI18nEnum.FORM_DEFAULT_HUMAN_DATE_FORMAT),
                UserUIContext.getMessage(GenericI18Enum.FORM_DATE_FORMAT_HELP), 1, 2);

        gridFormLayoutHelper.addComponent(new Label(LocalizationHelper.getLocaleInstance(billingAccount
                        .getDefaultlanguagetag()).getDisplayLanguage(UserUIContext.getUserLocale())),
                UserUIContext.getMessage(AdminI18nEnum.FORM_DEFAULT_LANGUAGE), 1, 3);

        gridFormLayoutHelper.addComponent(new Label(UserUIContext.getMessage(LocalizationHelper.localizeYesNo(billingAccount.getDisplayemailpublicly()))),
                UserUIContext.getMessage(AdminI18nEnum.FORM_SHOW_EMAIL_PUBLICLY),
                UserUIContext.getMessage(AdminI18nEnum.FORM_SHOW_EMAIL_PUBLICLY_HELP), 0, 4, 2, "100%");


        formContainer.addSection(new CssLayout(generalSettingHeader), gridFormLayoutHelper.getLayout());

        buildLogoPanel();
        buildShortcutIconPanel();
    }

    private void buildLogoPanel() {
        FormContainer formContainer = new FormContainer();
        MHorizontalLayout layout = new MHorizontalLayout().withFullWidth().withMargin(true);
        MVerticalLayout leftPanel = new MVerticalLayout().withMargin(false);
        Label logoDesc = new Label(UserUIContext.getMessage(AdminI18nEnum.OPT_LOGO_FORMAT_DESCRIPTION));
        leftPanel.with(logoDesc).withWidth("250px");

        MVerticalLayout rightPanel = new MVerticalLayout().withMargin(false);
        CustomLayout previewLayout = CustomLayoutExt.createLayout("topNavigation");
        previewLayout.setStyleName("example-block");
        previewLayout.setHeight("40px");
        previewLayout.setWidth("520px");

        Button currentLogo = AccountAssetsResolver.createAccountLogoImageComponent(billingAccount.getLogopath(), 150);
        previewLayout.addComponent(currentLogo, "mainLogo");
        final ServiceMenu serviceMenu = new ServiceMenu();

        Button.ClickListener clickListener = new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final Button.ClickEvent event) {

                for (Component comp : serviceMenu) {
                    if (comp != event.getButton()) {
                        comp.removeStyleName("selected");
                    }
                }
                event.getButton().addStyleName("selected");
            }
        };

        serviceMenu.addService(UserUIContext.getMessage(GenericI18Enum.MODULE_CRM), VaadinIcons.MONEY, clickListener);
        serviceMenu.addService(UserUIContext.getMessage(GenericI18Enum.MODULE_PROJECT), VaadinIcons.TASKS, clickListener);
        serviceMenu.addService(UserUIContext.getMessage(GenericI18Enum.MODULE_DOCUMENT), VaadinIcons.SUITCASE, clickListener);
        serviceMenu.selectService(0);

        previewLayout.addComponent(serviceMenu, "serviceMenu");

        MHorizontalLayout buttonControls = new MHorizontalLayout().withMargin(new MarginInfo(true, false, false, false));
        buttonControls.setDefaultComponentAlignment(Alignment.TOP_LEFT);
        final UploadField logoUploadField = new UploadField() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void updateDisplay() {
                byte[] imageData = (byte[]) this.getValue();
                String mimeType = this.getLastMimeType();
                if (mimeType.equals("image/jpeg")) {
                    imageData = ImageUtil.convertJpgToPngFormat(imageData);
                    if (imageData == null) {
                        throw new UserInvalidInputException(UserUIContext.getMessage(FileI18nEnum.ERROR_INVALID_SUPPORTED_IMAGE_FORMAT));
                    } else {
                        mimeType = "image/png";
                    }
                }

                if (mimeType.equals("image/png")) {
                    UI.getCurrent().addWindow(new LogoEditWindow(imageData));
                } else {
                    throw new UserInvalidInputException(UserUIContext.getMessage(FileI18nEnum.ERROR_UPLOAD_INVALID_SUPPORTED_IMAGE_FORMAT));
                }
            }
        };
        logoUploadField.setButtonCaption(UserUIContext.getMessage(GenericI18Enum.ACTION_CHANGE));
        logoUploadField.addStyleName("upload-field");
        logoUploadField.setSizeUndefined();
        logoUploadField.setFieldType(UploadField.FieldType.BYTE_ARRAY);
        logoUploadField.setVisible(UserUIContext.canBeYes(RolePermissionCollections.ACCOUNT_THEME));

        MButton resetButton = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_RESET), clickEvent -> {
            BillingAccountService billingAccountService = AppContextUtil.getSpringBean(BillingAccountService.class);
            billingAccount.setLogopath(null);
            billingAccountService.updateWithSession(billingAccount, UserUIContext.getUsername());
            Page.getCurrent().getJavaScript().execute("window.location.reload();");
        }).withStyleName(WebUIConstants.BUTTON_OPTION);
        resetButton.setVisible(UserUIContext.canBeYes(RolePermissionCollections.ACCOUNT_THEME));

        buttonControls.with(logoUploadField, resetButton);
        rightPanel.with(previewLayout, buttonControls);
        layout.with(leftPanel, rightPanel).expand(rightPanel);
        formContainer.addSection("Logo", layout);
        this.with(formContainer);
    }

    private void buildShortcutIconPanel() {
        FormContainer formContainer = new FormContainer();
        MHorizontalLayout layout = new MHorizontalLayout().withFullWidth().withMargin(new MarginInfo(true));
        MVerticalLayout leftPanel = new MVerticalLayout().withMargin(false);
        Label logoDesc = new Label(UserUIContext.getMessage(FileI18nEnum.OPT_FAVICON_FORMAT_DESCRIPTION));
        leftPanel.with(logoDesc).withWidth("250px");
        MVerticalLayout rightPanel = new MVerticalLayout().withMargin(false);
        final Image favIconRes = new Image("", new ExternalResource(StorageFactory.getFavIconPath(billingAccount.getId(),
                billingAccount.getFaviconpath())));

        MHorizontalLayout buttonControls = new MHorizontalLayout().withMargin(new MarginInfo(true, false, false, false));
        buttonControls.setDefaultComponentAlignment(Alignment.BOTTOM_LEFT);
        final UploadField favIconUploadField = new UploadField() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void updateDisplay() {
                byte[] imageData = (byte[]) this.getValue();
                String mimeType = this.getLastMimeType();
                if (mimeType.equals("image/jpeg")) {
                    imageData = ImageUtil.convertJpgToPngFormat(imageData);
                    if (imageData == null) {
                        throw new UserInvalidInputException(UserUIContext.getMessage(FileI18nEnum.ERROR_INVALID_SUPPORTED_IMAGE_FORMAT));
                    } else {
                        mimeType = "image/png";
                    }
                }

                if (mimeType.equals("image/png")) {
                    try {
                        AccountFavIconService favIconService = AppContextUtil.getSpringBean(AccountFavIconService.class);
                        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageData));
                        String newFavIconPath = favIconService.upload(UserUIContext.getUsername(), image, MyCollabUI
                                .getAccountId());
                        favIconRes.setSource(new ExternalResource(StorageFactory.getFavIconPath(billingAccount.getId(),
                                newFavIconPath)));
                        Page.getCurrent().getJavaScript().execute("window.location.reload();");
                    } catch (IOException e) {
                        throw new MyCollabException(e);
                    }
                } else {
                    throw new UserInvalidInputException(UserUIContext.getMessage(FileI18nEnum.ERROR_UPLOAD_INVALID_SUPPORTED_IMAGE_FORMAT));
                }
            }
        };
        favIconUploadField.setButtonCaption(UserUIContext.getMessage(GenericI18Enum.ACTION_CHANGE));
        favIconUploadField.addStyleName("upload-field");
        favIconUploadField.setSizeUndefined();
        favIconUploadField.setFieldType(UploadField.FieldType.BYTE_ARRAY);
        favIconUploadField.setVisible(UserUIContext.canBeYes(RolePermissionCollections.ACCOUNT_THEME));

        MButton resetButton = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_RESET), clickEvent -> {
            BillingAccountService billingAccountService = AppContextUtil.getSpringBean(BillingAccountService.class);
            billingAccount.setFaviconpath(null);
            billingAccountService.updateWithSession(billingAccount, UserUIContext.getUsername());
            Page.getCurrent().getJavaScript().execute("window.location.reload();");
        }).withStyleName(WebUIConstants.BUTTON_OPTION);
        resetButton.setVisible(UserUIContext.canBeYes(RolePermissionCollections.ACCOUNT_THEME));

        buttonControls.with(favIconUploadField, resetButton);
        rightPanel.with(favIconRes, buttonControls);
        layout.with(leftPanel, rightPanel).expand(rightPanel);
        formContainer.addSection("Favicon", layout);
        this.with(formContainer);
    }
}
