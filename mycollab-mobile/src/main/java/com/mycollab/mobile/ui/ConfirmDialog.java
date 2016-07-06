/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.mobile.ui;

import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;

import java.io.Serializable;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class ConfirmDialog extends Window {
    private static final long serialVersionUID = 4412730322944493887L;

    public interface CloseListener extends Serializable {
        void onClose(ConfirmDialog dialog);
    }

    private CloseListener closeListener;

    private boolean confirmed;

    public ConfirmDialog() {
        super();
        setStyleName("confirm-dialog");
        setWidth("280px");
        setClosable(false);
        setDraggable(false);
        setResizable(false);
    }

    public static ConfirmDialog show(final UI currentUI, final String message, final String okCaption, final String cancelCaption,
                                     final CloseListener listener) {
        ConfirmDialog d = new ConfirmDialog();
        d.constructUI(message, okCaption, cancelCaption);
        d.show(currentUI, listener, true);
        return d;
    }

    private void constructUI(final String message, final String okCaption, final String cancelCaption) {
        VerticalLayout layout = new VerticalLayout();
        layout.setWidth("100%");
        layout.setHeightUndefined();
        layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        VerticalLayout messageWrapper = new VerticalLayout();
        messageWrapper.setStyleName("message-wrapper");
        messageWrapper.setWidth("100%");
        messageWrapper.setMargin(true);

        final Label messageDisplay = new Label(message);
        messageDisplay.setWidth("100%");
        messageWrapper.addComponent(messageDisplay);
        layout.addComponent(messageWrapper);

        HorizontalLayout controlBtn = new HorizontalLayout();
        controlBtn.setWidth("100%");

        final Button okBtn = new Button(okCaption);
        okBtn.setWidth("100%");
        okBtn.setHeight("35px");
        final Button cancelBtn = new Button(cancelCaption);
        cancelBtn.setWidth("100%");
        cancelBtn.setHeight("35px");

        Button.ClickListener listener = new Button.ClickListener() {
            private static final long serialVersionUID = -8306231710367659086L;

            @Override
            public void buttonClick(ClickEvent event) {
                ConfirmDialog.this.setConfirmed(event.getButton() == okBtn);
                if (ConfirmDialog.this.getListener() != null) {
                    ConfirmDialog.this.getListener().onClose(ConfirmDialog.this);
                }
                ConfirmDialog.this.close();
            }

        };

        okBtn.addClickListener(listener);
        cancelBtn.addClickListener(listener);

        controlBtn.addComponent(cancelBtn);
        controlBtn.addComponent(okBtn);

        layout.addComponent(controlBtn);
        this.setContent(layout);
    }

    public final void show(final UI parentUI, final CloseListener listener,
                           final boolean modal) {
        closeListener = listener;
        center();
        setModal(modal);
        parentUI.addWindow(this);
    }

    public CloseListener getListener() {
        return closeListener;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean value) {
        confirmed = value;
    }
}
