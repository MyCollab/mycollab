package com.mycollab.vaadin.web.ui;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.vaadin.UserUIContext;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.2.0
 */
public abstract class DepotWithChart extends Depot {
    private MButton toggleViewBtn;
    private boolean isPlainMode = true;

    public DepotWithChart() {
        super("", new MVerticalLayout());

        toggleViewBtn = new MButton("", clickEvent -> {
            isPlainMode = !isPlainMode;
            if (isPlainMode) {
                toggleViewBtn.setIcon(FontAwesome.BAR_CHART_O);
                toggleViewBtn.setDescription(UserUIContext.getMessage(GenericI18Enum.OPT_CHART_MODE));
                displayPlainMode();
            } else {
                toggleViewBtn.setIcon(FontAwesome.LIST);
                toggleViewBtn.setDescription(UserUIContext.getMessage(GenericI18Enum.OPT_SIMPLE_MODE));
                displayChartMode();
            }
        }).withIcon(FontAwesome.BAR_CHART_O).withStyleName(WebThemes.BUTTON_ICON_ONLY);
        toggleViewBtn.setDescription(UserUIContext.getMessage(GenericI18Enum.OPT_SIMPLE_MODE));
        addHeaderElement(toggleViewBtn);
        setContentBorder(true);
        this.setMargin(new MarginInfo(false, false, true, false));
    }

    abstract protected void displayPlainMode();

    abstract protected void displayChartMode();
}
