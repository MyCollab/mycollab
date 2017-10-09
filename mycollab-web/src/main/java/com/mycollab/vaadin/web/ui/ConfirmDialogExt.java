package com.mycollab.vaadin.web.ui;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.dialogs.ConfirmDialog.Listener;
import org.vaadin.dialogs.DefaultConfirmDialogFactory;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class ConfirmDialogExt {
    public static ConfirmDialog show(UI parentWindow, String windowCaption, String message, String okCaption,
                            String cancelCaption, Listener listener) {
        ConfirmDialog.setFactory(new ConfirmDialogFactory());
        return ConfirmDialog.show(parentWindow, windowCaption, message, okCaption,
                cancelCaption, listener);
    }

    private static class ConfirmDialogFactory extends DefaultConfirmDialogFactory {
        private static final long serialVersionUID = 1L;

        @Override
        public ConfirmDialog create(String caption, String message, String okCaption, String cancelCaption, String notOkCaption) {
            ConfirmDialog d = super.create(caption, message, okCaption, cancelCaption, notOkCaption);

            d.getContent().setStyleName("custom-dialog");
            d.getContent().setHeightUndefined();
            d.setHeightUndefined();

            Button ok = d.getOkButton();
            ok.setStyleName(WebThemes.BUTTON_ACTION);

            HorizontalLayout buttons = (HorizontalLayout) ok.getParent();
            buttons.setHeightUndefined();

            Button cancelBtn = d.getCancelButton();
            cancelBtn.setStyleName(WebThemes.BUTTON_OPTION);
            cancelBtn.focus();

            return d;
        }
    }
}
