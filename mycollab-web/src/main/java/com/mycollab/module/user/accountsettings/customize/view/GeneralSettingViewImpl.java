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
package com.mycollab.module.user.accountsettings.customize.view;

import com.mycollab.common.i18n.FileI18nEnum;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.ShellI18nEnum;
import com.mycollab.configuration.ServerConfiguration;
import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.core.utils.DateTimeUtils;
import com.mycollab.core.utils.TimezoneVal;
import com.mycollab.i18n.LocalizationHelper;
import com.mycollab.module.file.StorageUtils;
import com.mycollab.module.user.accountsettings.localization.AdminI18nEnum;
import com.mycollab.module.user.domain.SimpleBillingAccount;
import com.mycollab.module.user.service.BillingAccountService;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.AbstractVerticalPageView;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.AccountAssetsResolver;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.FormContainer;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.web.ui.ServiceMenu;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.mycollab.web.CustomLayoutExt;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.time.LocalDateTime;
import java.util.Currency;

/**
 * @author MyCollab Ltd
 * @since 5.1.0
 */
// TODO
@ViewComponent
public class GeneralSettingViewImpl extends AbstractVerticalPageView implements GeneralSettingView {
    private SimpleBillingAccount billingAccount;

    public GeneralSettingViewImpl() {
        this.setMargin(true);
    }

    @Override
    public void displayView() {
        removeAllComponents();

        billingAccount = AppUI.getBillingAccount();
        FormContainer formContainer = new FormContainer();
        this.addComponent(formContainer);

        MHorizontalLayout generalSettingHeader = new MHorizontalLayout();
        Label headerLbl = new Label(UserUIContext.getMessage(AdminI18nEnum.OPT_GENERAL_SETTINGS));

        MButton editBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_EDIT), clickEvent -> UI.getCurrent().addWindow(new AccountInfoChangeWindow()))
                .withStyleName(WebThemes.BUTTON_LINK);

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

        LocalDateTime now = LocalDateTime.now();
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

        if (!SiteConfiguration.isDemandEdition()) {
            buildLanguageUpdatePanel();
        }
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
//        final UploadField logoUploadField = new UploadField() {
//            private static final long serialVersionUID = 1L;
//
//            @Override
//            protected void updateDisplay() {
//                byte[] imageData = (byte[]) this.getValue();
//                String mimeType = this.getLastMimeType();
//                if (mimeType.equals("image/jpeg")) {
//                    imageData = ImageUtil.convertJpgToPngFormat(imageData);
//                    if (imageData == null) {
//                        throw new UserInvalidInputException(UserUIContext.getMessage(FileI18nEnum.ERROR_INVALID_SUPPORTED_IMAGE_FORMAT));
//                    } else {
//                        mimeType = "image/png";
//                    }
//                }
//
//                if (mimeType.equals("image/png")) {
//                    UI.getCurrent().addWindow(new LogoEditWindow(imageData));
//                } else {
//                    throw new UserInvalidInputException(UserUIContext.getMessage(FileI18nEnum.ERROR_UPLOAD_INVALID_SUPPORTED_IMAGE_FORMAT));
//                }
//            }
//        };
//        logoUploadField.setButtonCaption(UserUIContext.getMessage(GenericI18Enum.ACTION_CHANGE));
//        logoUploadField.addStyleName("upload-field");
//        logoUploadField.setSizeUndefined();
//        logoUploadField.setFieldType(UploadField.FieldType.BYTE_ARRAY);
//        logoUploadField.setVisible(UserUIContext.canBeYes(RolePermissionCollections.ACCOUNT_THEME));

        MButton resetButton = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_RESET), clickEvent -> {
            BillingAccountService billingAccountService = AppContextUtil.getSpringBean(BillingAccountService.class);
            billingAccount.setLogopath(null);
            billingAccountService.updateWithSession(billingAccount, UserUIContext.getUsername());
            Page.getCurrent().getJavaScript().execute("window.location.reload();");
        }).withStyleName(WebThemes.BUTTON_OPTION);
        resetButton.setVisible(UserUIContext.canBeYes(RolePermissionCollections.ACCOUNT_THEME));

//        buttonControls.with(resetButton, logoUploadField);
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
        final Image favIconRes = new Image("", new ExternalResource(StorageUtils.getFavIconPath(billingAccount.getId(),
                billingAccount.getFaviconpath())));

        MHorizontalLayout buttonControls = new MHorizontalLayout().withMargin(new MarginInfo(true, false, false, false));
        buttonControls.setDefaultComponentAlignment(Alignment.BOTTOM_LEFT);
//        final UploadField favIconUploadField = new UploadField() {
//            private static final long serialVersionUID = 1L;
//
//            @Override
//            protected void updateDisplay() {
//                byte[] imageData = (byte[]) this.getValue();
//                String mimeType = this.getLastMimeType();
//                if (mimeType.equals("image/jpeg")) {
//                    imageData = ImageUtil.convertJpgToPngFormat(imageData);
//                    if (imageData == null) {
//                        throw new UserInvalidInputException(UserUIContext.getMessage(FileI18nEnum.ERROR_INVALID_SUPPORTED_IMAGE_FORMAT));
//                    } else {
//                        mimeType = "image/png";
//                    }
//                }
//
//                if (mimeType.equals("image/png")) {
//                    try {
//                        AccountFavIconService favIconService = AppContextUtil.getSpringBean(AccountFavIconService.class);
//                        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageData));
//                        String newFavIconPath = favIconService.upload(UserUIContext.getUsername(), image, AppUI.getAccountId());
//                        favIconRes.setSource(new ExternalResource(StorageUtils.getFavIconPath(billingAccount.getId(),
//                                newFavIconPath)));
//                        Page.getCurrent().getJavaScript().execute("window.location.reload();");
//                    } catch (IOException e) {
//                        throw new MyCollabException(e);
//                    }
//                } else {
//                    throw new UserInvalidInputException(UserUIContext.getMessage(FileI18nEnum.ERROR_UPLOAD_INVALID_SUPPORTED_IMAGE_FORMAT));
//                }
//            }
//        };
//        favIconUploadField.setButtonCaption(UserUIContext.getMessage(GenericI18Enum.ACTION_CHANGE));
//        favIconUploadField.addStyleName("upload-field");
//        favIconUploadField.setSizeUndefined();
//        favIconUploadField.setFieldType(UploadField.FieldType.BYTE_ARRAY);
//        favIconUploadField.setVisible(UserUIContext.canBeYes(RolePermissionCollections.ACCOUNT_THEME));

        MButton resetButton = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_RESET), clickEvent -> {
            BillingAccountService billingAccountService = AppContextUtil.getSpringBean(BillingAccountService.class);
            billingAccount.setFaviconpath(null);
            billingAccountService.updateWithSession(billingAccount, UserUIContext.getUsername());
            Page.getCurrent().getJavaScript().execute("window.location.reload();");
        }).withStyleName(WebThemes.BUTTON_OPTION);
        resetButton.setVisible(UserUIContext.canBeYes(RolePermissionCollections.ACCOUNT_THEME));

//        buttonControls.with(resetButton, favIconUploadField);
        rightPanel.with(favIconRes, buttonControls);
        layout.with(leftPanel, rightPanel).expand(rightPanel);
        formContainer.addSection("Favicon", layout);
        this.with(formContainer);
    }

    private void buildLanguageUpdatePanel() {
        FormContainer formContainer = new FormContainer();
        MHorizontalLayout layout = new MHorizontalLayout().withFullWidth().withMargin(new MarginInfo(true));
        MVerticalLayout leftPanel = new MVerticalLayout().withMargin(false);
        Label logoDesc = new Label(UserUIContext.getMessage(ShellI18nEnum.OPT_LANGUAGE_DOWNLOAD));
        leftPanel.with(logoDesc).withWidth("250px");
        MVerticalLayout rightPanel = new MVerticalLayout().withMargin(false);
        MButton downloadBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_DOWNLOAD))
                .withStyleName(WebThemes.BUTTON_ACTION).withIcon(VaadinIcons.DOWNLOAD);
        ServerConfiguration serverConfiguration = AppContextUtil.getSpringBean(ServerConfiguration.class);
        BrowserWindowOpener opener = new BrowserWindowOpener(serverConfiguration.getApiUrl("localization/translations"));
        opener.extend(downloadBtn);
        rightPanel.with(downloadBtn, new ELabel(UserUIContext.getMessage(ShellI18nEnum
                .OPT_UPDATE_LANGUAGE_INSTRUCTION)).withStyleName(UIConstants.META_INFO));
        layout.with(leftPanel, rightPanel).expand(rightPanel);
        formContainer.addSection("Languages", layout);
        this.with(formContainer);
    }
}
