package com.mycollab.vaadin.web.ui;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import org.vaadin.peter.buttongroup.ButtonGroup;

import java.util.Iterator;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class ToggleButtonGroup extends ButtonGroup {
    private static final long serialVersionUID = 1L;

    private Button selectedBtn;

    public ToggleButtonGroup() {
        super();
        this.addStyleName("toggle-btn-group");
    }

    public ToggleButtonGroup(Button... buttons) {
        this();
        for (Button button: buttons) {
            addButton(button);
        }
    }

    @Override
    public Button addButton(Button button) {
        super.addButton(button);
        button.addClickListener(clickEvent -> {
            if (!clickEvent.getButton().equals(selectedBtn)) {
                selectedBtn = clickEvent.getButton();
                Iterator<Component> iterator = ToggleButtonGroup.this.iterator();
                while (iterator.hasNext()) {
                    iterator.next().removeStyleName(WebThemes.BTN_ACTIVE);
                }
                selectedBtn.addStyleName(WebThemes.BTN_ACTIVE);
            }
        });
        return button;
    }

    public ButtonGroup withDefaultButton(Button button) {
        Iterator<Component> iterator = ToggleButtonGroup.this.iterator();
        while (iterator.hasNext()) {
            Button currentBtn = (Button) iterator.next();
            if (currentBtn.equals(button)) {
                selectedBtn = button;
                selectedBtn.addStyleName(WebThemes.BTN_ACTIVE);
            } else {
                currentBtn.removeStyleName(WebThemes.BTN_ACTIVE);
            }
        }
        return this;
    }

}
