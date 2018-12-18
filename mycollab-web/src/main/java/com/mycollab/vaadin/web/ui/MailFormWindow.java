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
package com.mycollab.vaadin.web.ui;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.mail.AttachmentSource;
import com.mycollab.module.mail.FileAttachmentSource;
import com.mycollab.module.mail.service.ExtMailService;
import com.mycollab.reporting.ReportTemplateExecutor;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.apache.commons.collections.CollectionUtils;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
// TODO
public class MailFormWindow extends MWindow {
    private static final long serialVersionUID = 1L;

    private EmailTokenField tokenFieldMailTo;
    private EmailTokenField tokenFieldMailCc = new EmailTokenField();
    private EmailTokenField tokenFieldMailBcc = new EmailTokenField();

    private GridLayout inputLayout;
    private Layout subjectField;
    private Layout ccField;
    private Layout bccField;

    private MButton btnLinkCc, btnLinkBcc;

    private boolean isAddCc = false;
    private boolean isAddBcc = false;

    private List<String> mails;

    private ReportTemplateExecutor reportTemplateExecutor;

    public MailFormWindow() {
        initLayout();
    }

    public MailFormWindow(List<String> mails) {
        this.mails = mails;
        initLayout();
    }

    public MailFormWindow(ReportTemplateExecutor reportTemplateExecutor) {
        this.reportTemplateExecutor = reportTemplateExecutor;
        initLayout();
    }

    private void initLayout() {
        withModal(true).withResizable(false).withWidth("830px").withCenter();
        initUI();
    }

    private void initButtonLinkCcBcc() {
        btnLinkCc = new MButton("Add Cc", clickEvent -> toggleCcLink()).withStyleName(WebThemes.BUTTON_LINK);
        inputLayout.addComponent(btnLinkCc, 1, 0);
        inputLayout.setComponentAlignment(btnLinkCc, Alignment.MIDDLE_CENTER);

        btnLinkBcc = new MButton("Add Bcc", clickEvent -> toggleBccLink()).withStyleName(WebThemes.BUTTON_LINK);
        inputLayout.addComponent(btnLinkBcc, 2, 0);
        inputLayout.setComponentAlignment(btnLinkBcc, Alignment.MIDDLE_CENTER);
    }

    private Layout createTextFieldMail(String title, Component component) {
        return new MHorizontalLayout(new ELabel(title).withWidth("80px"), component).expand(component).withFullWidth()
                .withDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
    }

    private Layout createTextFieldMailWithHelp(String title, Component component) {
        return new MHorizontalLayout(ELabel.html(title + "&nbsp;" + VaadinIcons.QUESTION_CIRCLE.getHtml())
                .withStyleName(WebThemes.INLINE_HELP).withDescription("Enter the user name or email, then press the enter button to finish the entry")
                .withWidth("80px"), component).expand(component).withFullWidth()
                .withDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
    }

    private void initUI() {
        MVerticalLayout mainLayout = new MVerticalLayout().withFullWidth();

        inputLayout = new GridLayout(3, 4);
        inputLayout.setSpacing(true);
        inputLayout.setWidth("100%");
        inputLayout.setColumnExpandRatio(0, 1.0f);

        mainLayout.addComponent(inputLayout);

        tokenFieldMailTo = new EmailTokenField();
        inputLayout.addComponent(createTextFieldMailWithHelp("To:", tokenFieldMailTo), 0, 0);

        if (mails != null) {
            mails.stream().filter(mail -> mail.indexOf("<") > 0).map(mail -> {
                String strMail = mail.substring(mail.indexOf("<") + 1, mail.lastIndexOf(">"));
                if (strMail != null && !strMail.equalsIgnoreCase("null")) {
                    return strMail;
                } else {
                    return "";
                }
            });
        }

        final MTextField subject = new MTextField().withRequiredIndicatorVisible(true).withFullWidth();
        subjectField = createTextFieldMail("Subject:", subject);
        inputLayout.addComponent(subjectField, 0, 1);

        initButtonLinkCcBcc();

        ccField = createTextFieldMailWithHelp("Cc:", tokenFieldMailCc);
        bccField = createTextFieldMailWithHelp("Bcc:", tokenFieldMailBcc);

        final RichTextArea noteArea = new RichTextArea();
        noteArea.setWidth("100%");
        noteArea.setHeight("200px");
        mainLayout.addComponent(noteArea);
        mainLayout.setComponentAlignment(noteArea, Alignment.MIDDLE_CENTER);

        final AttachmentPanel attachments = new AttachmentPanel();

        MButton cancelBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CANCEL), clickEvent -> close())
                .withStyleName(WebThemes.BUTTON_OPTION);

        MButton sendBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.ACTION_SEND_EMAIL), clickEvent -> {
            if (tokenFieldMailTo.getListRecipients().size() <= 0 || subject.getValue().equals("")) {
                NotificationUtil.showErrorNotification("To Email field and Subject field must be not empty! Please fulfil them before sending email.");
                return;
            }
            if (UserUIContext.getUser().getEmail() != null && UserUIContext.getUser().getEmail().length() > 0) {
                ExtMailService systemMailService = AppContextUtil.getSpringBean(ExtMailService.class);

                List<File> files = attachments.files();
                List<AttachmentSource> attachmentSource = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(files)) {
                    files.forEach(file -> attachmentSource.add(new FileAttachmentSource(file)));
                }

                if (reportTemplateExecutor != null) {
                    attachmentSource.add(new FileAttachmentSource(reportTemplateExecutor.getDefaultExportFileName(),
                            reportTemplateExecutor.exportStream()));
                }

                systemMailService.sendHTMLMail(UserUIContext.getUser().getEmail(), UserUIContext.getUser().getDisplayName(),
                        tokenFieldMailTo.getListRecipients(), tokenFieldMailCc.getListRecipients(),
                        tokenFieldMailBcc.getListRecipients(), subject.getValue(),
                        noteArea.getValue(), attachmentSource, true);
                close();
            } else {
                NotificationUtil.showErrorNotification("Your email is empty value, please fulfil it before sending email!");
            }
        }).withIcon(VaadinIcons.PAPERPLANE).withStyleName(WebThemes.BUTTON_ACTION);

        MHorizontalLayout controlsLayout = new MHorizontalLayout(cancelBtn, sendBtn)
                .withMargin(new MarginInfo(false, true, true, false));
        mainLayout.with(attachments);
        mainLayout.addStyleName(WebThemes.SCROLLABLE_CONTAINER);
//        new Restrain(mainLayout).setMaxHeight((UIUtils.getBrowserHeight() - 180) + "px");
        this.setContent(new MVerticalLayout(mainLayout, controlsLayout).withMargin(false)
                .withSpacing(false).withAlign(controlsLayout, Alignment.TOP_RIGHT));
    }

    private void checkToReInitCcBcc() {
        if ((!isAddCc) && (!isAddBcc)) {
            inputLayout.removeComponent(btnLinkCc);
            inputLayout.removeComponent(btnLinkBcc);
            initButtonLinkCcBcc();
            inputLayout.removeComponent(subjectField);
            inputLayout.removeComponent(0, 1);
            inputLayout.addComponent(subjectField, 0, 1);
        }
    }

    private void toggleCcLink() {
        removeAllInputField();
        if (!isAddCc) {
            btnLinkCc.setCaption("Remove Cc");
            if (!isAddBcc) {
                inputLayout.addComponent(ccField, 0, 1);
                inputLayout.addComponent(btnLinkCc, 1, 1);
                inputLayout.addComponent(btnLinkBcc, 2, 1);
                inputLayout.addComponent(subjectField, 0, 2);
            } else {
                addFullInputFieldByOrder();
            }
        } else {
            btnLinkCc.setCaption("Add Cc");

            if (isAddBcc) {
                inputLayout.addComponent(bccField, 0, 1);
                inputLayout.addComponent(btnLinkBcc, 1, 1);
                inputLayout.addComponent(btnLinkCc, 2, 1);
                inputLayout.addComponent(subjectField, 0, 2);
            } else {
                inputLayout.addComponent(btnLinkBcc, 1, 0);
                inputLayout.addComponent(btnLinkCc, 2, 0);
                inputLayout.addComponent(subjectField, 0, 1);
            }
        }

        inputLayout.setComponentAlignment(btnLinkBcc, Alignment.MIDDLE_CENTER);
        inputLayout.setComponentAlignment(btnLinkCc, Alignment.MIDDLE_CENTER);

        isAddCc = !isAddCc;

        checkToReInitCcBcc();
    }

    private void addFullInputFieldByOrder() {
        inputLayout.addComponent(ccField, 0, 1);
        inputLayout.addComponent(btnLinkCc, 1, 1);

        inputLayout.addComponent(bccField, 0, 2);
        inputLayout.addComponent(btnLinkBcc, 1, 2);

        inputLayout.addComponent(subjectField, 0, 3);
    }

    private void removeAllInputField() {
        inputLayout.removeComponent(btnLinkCc);
        inputLayout.removeComponent(ccField);
        inputLayout.removeComponent(subjectField);
        inputLayout.removeComponent(bccField);
        inputLayout.removeComponent(btnLinkBcc);
    }

    private void toggleBccLink() {
        removeAllInputField();

        if (!isAddBcc) {
            btnLinkBcc.setCaption("Remove Bcc");
            if (!isAddCc) {
                inputLayout.addComponent(bccField, 0, 1);
                inputLayout.addComponent(btnLinkCc, 1, 1);
                inputLayout.addComponent(btnLinkBcc, 2, 1);
                inputLayout.addComponent(subjectField, 0, 2);
            } else {
                addFullInputFieldByOrder();
            }
        } else {
            btnLinkBcc.setCaption("Add Bcc");

            if (isAddCc) {
                inputLayout.addComponent(ccField, 0, 1);
                inputLayout.addComponent(btnLinkCc, 1, 1);
                inputLayout.addComponent(btnLinkBcc, 2, 1);
                inputLayout.addComponent(subjectField, 0, 2);
            } else {
                inputLayout.addComponent(btnLinkBcc, 1, 0);
                inputLayout.addComponent(btnLinkCc, 2, 0);
                inputLayout.addComponent(subjectField, 0, 1);
            }
        }
        inputLayout.setComponentAlignment(btnLinkBcc, Alignment.MIDDLE_CENTER);
        inputLayout.setComponentAlignment(btnLinkCc, Alignment.MIDDLE_CENTER);
        isAddBcc = !isAddBcc;
        checkToReInitCcBcc();
    }
}
