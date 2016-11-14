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
package com.mycollab.vaadin.web.ui;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.mail.AttachmentSource;
import com.mycollab.module.mail.FileAttachmentSource;
import com.mycollab.module.mail.service.ExtMailService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class MailFormWindow extends MWindow {
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
        initLayout();
    }

    public MailFormWindow(List<String> lstMail) {
        this.lstMail = lstMail;
        initLayout();
    }

    private void initLayout() {
        withModal(true).withResizable(false).withWidth("830px").withCenter();
        initUI();
    }

    private void initButtonLinkCcBcc() {
        btnLinkCc = new Button("Add Cc");
        btnLinkCc.setStyleName(WebThemes.BUTTON_LINK);
        inputLayout.addComponent(btnLinkCc, 1, 0);
        inputLayout.setComponentAlignment(btnLinkCc, Alignment.MIDDLE_CENTER);

        btnLinkBcc = new Button("Add Bcc");
        btnLinkBcc.setStyleName(WebThemes.BUTTON_LINK);
        inputLayout.addComponent(btnLinkBcc, 2, 0);
        inputLayout.setComponentAlignment(btnLinkBcc, Alignment.MIDDLE_CENTER);

        btnLinkCc.addClickListener(clickEvent -> toggleCcLink());
        btnLinkBcc.addClickListener(clickEvent -> toggleBccLink());
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

        inputLayout.addComponent(createTextFieldMail("To:", tokenFieldMailTo), 0, 0);

        if (lstMail != null) {
            for (String mail : lstMail) {
                if (StringUtils.isNotBlank(mail)) {
                    if (mail.indexOf("<") > -1) {
                        String strMail = mail.substring(mail.indexOf("<") + 1, mail.lastIndexOf(">"));
                        if (strMail != null && !strMail.equalsIgnoreCase("null")) {

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

        final AttachmentPanel attachments = new AttachmentPanel();
        attachments.setWidth("500px");

        MButton cancelBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CANCEL), clickEvent -> close())
                .withStyleName(WebThemes.BUTTON_OPTION);

        MButton sendBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.ACTION_SEND_EMAIL), clickEvent -> {
            if (tokenFieldMailTo.getListRecipient().size() <= 0 || subject.getValue().equals("")) {
                NotificationUtil.showErrorNotification("To Email field and Subject field must be not empty! Please fulfil them before sending email.");
                return;
            }
            if (UserUIContext.getUser().getEmail() != null && UserUIContext.getUser().getEmail().length() > 0) {
                ExtMailService systemMailService = AppContextUtil.getSpringBean(ExtMailService.class);

                List<File> listFile = attachments.files();
                List<AttachmentSource> attachmentSource = null;
                if (listFile != null && listFile.size() > 0) {
                    attachmentSource = new ArrayList<>();
                    for (File file : listFile) {
                        attachmentSource.add(new FileAttachmentSource(file));
                    }
                }

                systemMailService.sendHTMLMail(UserUIContext.getUser().getEmail(), UserUIContext.getUser().getDisplayName(),
                        tokenFieldMailTo.getListRecipient(), tokenFieldMailCc.getListRecipient(),
                        tokenFieldMailBcc.getListRecipient(), subject.getValue(),
                        noteArea.getValue(), attachmentSource, true);
                close();
            } else {
                NotificationUtil.showErrorNotification("Your email is empty value, please fulfil it before sending email!");
            }
        }).withIcon(FontAwesome.SEND).withStyleName(WebThemes.BUTTON_ACTION);

        MHorizontalLayout controlsLayout = new MHorizontalLayout(attachments, cancelBtn, sendBtn).expand(attachments).withFullWidth();
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
