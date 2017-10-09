package com.mycollab.vaadin.web.ui;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.utils.DateTimeUtils;
import com.mycollab.vaadin.UserUIContext;
import org.vaadin.risto.stylecalendar.StyleCalendarField;

import java.util.Date;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class WeeklyCalendarFieldExp extends StyleCalendarField {
    private static final long serialVersionUID = 1L;

    public void setPopupClose() {
        this.setShowPopup(false);
    }

    protected String getPaintValue() {
        Object value = getValue();

        if (value == null) {
            if (getNullRepresentation() != null) {
                return getNullRepresentation();
            } else {
                return UserUIContext.getMessage(GenericI18Enum.OPT_UNDEFINED);
            }
        } else {
            Date selectedDate = (Date) value;
            Date[] bounceDateofWeek = DateTimeUtils.getBounceDatesOfWeek(selectedDate);
            return UserUIContext.formatDate(bounceDateofWeek[0]) + " - " + UserUIContext.formatDate(bounceDateofWeek[1]);
        }
    }
}
