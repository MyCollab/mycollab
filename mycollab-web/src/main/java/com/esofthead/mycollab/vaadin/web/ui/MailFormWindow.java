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
package com.esofthead.mycollab.vaadin.web.ui;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.module.mail.EmailAttachementSource;
import com.esofthead.mycollab.module.mail.FileEmailAttachmentSource;
import com.esofthead.mycollab.module.mail.service.ExtMailService;
import com.esofthead.mycollab.spring.AppContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.easyuploads.MultiFileUploadExt;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class MailFormWindow extends Window {
    private static final long serialVersionUID = 1L;

    private EmailTokenField tokenFieldMailTo;
    private EmailTokenField tokenFieldMailCc = new EmailTokenField();
    private EmailTokenField tokenFieldMailBcc = new EmailTokenField();
    private GridLayout inputLayout;
    private Layout subjectField;
    private Layout ccField;
    private Layout bccField;

    private Button btnLinkCc;
    private Button btnLinkBcc;

    private boolean isAddCc = false;
    private boolean isAddBcc = false;

    private List<String> lstMail;

    public MailFormWindow() {
        this.setModal(true);
        initLayout();
    }

    public MailFormWindow(List<String> lstMail) {
        this.setModal(true);
        this.lstMail = lstMail;
        initLayout();
    }

    private void initLayout() {
        this.setWidth("830px");
        this.setHeightUndefined();
        initUI();
        center();
        this.setModal(true);
        this.setResizable(false);
    }

    @SuppressWarnings("serial")
    private void initButtonLinkCcBcc() {
        btnLinkCc = new Button("Add Cc");
        btnLinkCc.setStyleName(UIConstants.BUTTON_LINK);
        inputLayout.addComponent(btnLinkCc, 1, 0);
        inputLayout.setComponentAlignment(btnLinkCc, Alignment.MIDDLE_CENTER);

        btnLinkBcc = new Button("Add Bcc");
        btnLinkBcc.setStyleName(UIConstants.BUTTON_LINK);
        inputLayout.addComponent(btnLinkBcc, 2, 0);
        inputLayout.setComponentAlignment(btnLinkBcc, Alignment.MIDDLE_CENTER);

        btnLinkCc.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                buttonLinkCcClick(event);
            }
        });

        btnLinkBcc.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                butonLinkBccClick(event);
            }
        });
    }

    private Layout createTextFieldMail(String title, Component component) {
        HorizontalLayout layout = new HorizontalLayout();
        Label lbTitle = new Label(title);
        lbTitle.setWidth("60px");
        lbTitle.setStyleName("lbmail");
        layout.addComponent(lbTitle);
        layout.setComponentAlignment(lbTitle, Alignment.MIDDLE_RIGHT);
        layout.addComponent(component);
        layout.setComponentAlignment(component, Alignment.MIDDLE_LEFT);
        layout.setWidth("100%");
        layout.setExpandRatio(component, 1.0f);
        return layout;
    }

    private void initUI() {
        GridLayout mainLayout = new GridLayout(1, 5);
        mainLayout.setWidth("100%");
        mainLayout.setMargin(true);
        mainLayout.setSpacing(true);

        CssLayout inputPanel = new CssLayout();
        inputPanel.setWidth("100%");
        inputPanel.setStyleName("mail-panel");

        inputLayout = new GridLayout(3, 4);
        inputLayout.setSpacing(true);
        inputLayout.setWidth("100%");
        inputLayout.setColumnExpandRatio(0, 1.0f);

        inputPanel.addComponent(inputLayout);

        mainLayout.addComponent(inputPanel);

        tokenFieldMailTo = new EmailTokenField();

        inputLayout.addComponent(createTextFieldMail("To:", tokenFieldMailTo),
                0, 0);

        if (lstMail != null) {
            for (String mail : lstMail) {
                if (StringUtils.isNotBlank(mail)) {
                    if (mail.indexOf("<") > -1) {
                        String strMail = mail.substring(mail.indexOf("<") + 1,
                                mail.lastIndexOf(">"));
                        if (strMail != null
                                && !strMail.equalsIgnoreCase("null")) {

                        }
                    } else {

                    }
                }
            }
        }

        final TextField subject = new TextField();
        subject.setRequired(true);
        subject.setWidth("100%");
        subjectField = createTextFieldMail("Subject:", subject);
        inputLayout.addComponent(subjectField, 0, 1);

        initButtonLinkCcBcc();

        ccField = createTextFieldMail("Cc:", tokenFieldMailCc);
        bccField = createTextFieldMail("Bcc:", tokenFieldMailBcc);

        final RichTextArea noteArea = new RichTextArea();
        noteArea.setWidth("100%");
        noteArea.setHeight("200px");
        mainLayout.addComponent(noteArea, 0, 1);
        mainLayout.setComponentAlignment(noteArea, Alignment.MIDDLE_CENTER);

        HorizontalLayout controlsLayout = new HorizontalLayout();
        controlsLayout.setWidth("100%");

        final AttachmentPanel attachments = new AttachmentPanel();
        attachments.setWidth("500px");

        MultiFileUploadExt uploadExt = new MultiFileUploadExt(attachments);
        uploadExt.addComponent(attachments);

        controlsLayout.addComponent(uploadExt);
        controlsLayout.setExpandRatio(uploadExt, 1.0f);

        controlsLayout.setSpacing(true);

        Button cancelBtn = new Button(
                AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL),
                new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(ClickEvent event) {
                        MailFormWindow.this.close();
                    }
                });

        cancelBtn.setStyleName(UIConstants.BUTTON_OPTION);
        controlsLayout.addComponent(cancelBtn);
        controlsLayout.setComponentAlignment(cancelBtn, Alignment.MIDDLE_RIGHT);

        Button sendBtn = new Button("Send", new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {

                if (tokenFieldMailTo.getListRecipient().size() <= 0
                        || subject.getValue().equals("")) {
                    NotificationUtil
                            .showErrorNotification("To Email field and Subject field must be not empty! Please fulfil them before sending email.");
                    return;
                }
                if (AppContext.getUser().getEmail() != null
                        && AppContext.getUser().getEmail().length() > 0) {
                    ExtMailService systemMailService = AppContextUtil
                            .getSpringBean(ExtMailService.class);

                    List<File> listFile = attachments.files();
                    List<EmailAttachementSource> emailAttachmentSource = null;
                    if (listFile != null && listFile.size() > 0) {
                        emailAttachmentSource = new ArrayList<>();
                        for (File file : listFile) {
                            emailAttachmentSource
                                    .add(new FileEmailAttachmentSource(file));
                        }
                    }

                    systemMailService.sendHTMLMail(AppContext.getUser().getEmail(), AppContext.getUser().getDisplayName(),
                            tokenFieldMailTo.getListRecipient(), tokenFieldMailCc.getListRecipient(),
                            tokenFieldMailBcc.getListRecipient(), subject.getValue(),
                            noteArea.getValue(), emailAttachmentSource);
                    MailFormWindow.this.close();
                } else {
                    NotificationUtil
                            .showErrorNotification("Your email is empty value, please fulfil it before sending email!");
                }
            }
        });
        sendBtn.setIcon(FontAwesome.SEND);
        sendBtn.setStyleName(UIConstants.BUTTON_ACTION);
        controlsLayout.addComponent(sendBtn);
        controlsLayout.setComponentAlignment(sendBtn, Alignment.MIDDLE_RIGHT);
        mainLayout.addComponent(controlsLayout, 0, 2);

        this.setContent(mainLayout);
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

    private void buttonLinkCcClick(ClickEvent event) {
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

    private void butonLinkBccClick(ClickEvent event) {
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
