package com.mycollab.vaadin.web.ui;

import com.mycollab.module.file.StorageUtils;
import com.mycollab.vaadin.UserUIContext;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.vaadin.risto.stylecalendar.DateOptionsGenerator;
import org.vaadin.risto.stylecalendar.StyleCalendar;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class StyleCalendarExp extends VerticalLayout {
    private List<Date> selectedDateList = new ArrayList<>();
    private StyleCalendar styleCalendar = new StyleCalendar();

    private Button btnShowNextMonth;
    private Button btnShowNextYear;
    private Button btnShowPreviousMonth;
    private Button btnShowPreviousYear;
    private Label lbSelectedDate = new Label();

    public StyleCalendarExp() {
        this.setWidth("230px");
        this.setHeightUndefined();
        this.setSpacing(false);
        this.setStyleName("stylecalendar-ext");
        this.setMargin(new MarginInfo(true, false, false, false));

        styleCalendar.setRenderHeader(false);
        styleCalendar.setRenderWeekNumbers(false);
        styleCalendar.setImmediate(true);
        styleCalendar.setWidth("100%");
        setDateOptionsGenerator();

        btnShowNextYear = new Button();
        btnShowNextYear.setIcon(new ExternalResource(StorageUtils.generateAssetRelativeLink("icons/16/cal_year_next.png")));
        btnShowNextYear.setStyleName(WebThemes.BUTTON_LINK);

        btnShowNextMonth = new Button();
        btnShowNextMonth.setIcon(new ExternalResource(StorageUtils.generateAssetRelativeLink("icons/16/cal_month_next.png")));
        btnShowNextMonth.setStyleName(WebThemes.BUTTON_LINK);

        btnShowPreviousMonth = new Button();
        btnShowPreviousMonth.setIcon(new ExternalResource(StorageUtils.generateAssetRelativeLink("icons/16/cal_month_pre.png")));
        btnShowPreviousMonth.setStyleName(WebThemes.BUTTON_LINK);

        btnShowPreviousYear = new Button();
        btnShowPreviousYear.setIcon(new ExternalResource(StorageUtils.generateAssetRelativeLink("icons/16/cal_year_pre.png")));
        btnShowPreviousYear.setStyleName(WebThemes.BUTTON_LINK);

        lbSelectedDate.setValue(UserUIContext.formatDate(new Date()));
        lbSelectedDate.addStyleName("calendarDateLabel");
        lbSelectedDate.setWidth("80");

        HorizontalLayout layoutControl = new HorizontalLayout();
        layoutControl.setStyleName("calendarHeader");
        layoutControl.setWidth("100%");
        layoutControl.setHeight("35px");

        HorizontalLayout layoutButtonPrevious = new HorizontalLayout();
        layoutButtonPrevious.setSpacing(true);
        layoutButtonPrevious.addComponent(btnShowPreviousYear);
        layoutButtonPrevious.setComponentAlignment(btnShowPreviousYear, Alignment.MIDDLE_LEFT);
        layoutButtonPrevious.addComponent(btnShowPreviousMonth);
        layoutButtonPrevious.setComponentAlignment(btnShowPreviousMonth, Alignment.MIDDLE_LEFT);
        layoutControl.addComponent(layoutButtonPrevious);
        layoutControl.setComponentAlignment(layoutButtonPrevious, Alignment.MIDDLE_LEFT);

        layoutControl.addComponent(lbSelectedDate);
        layoutControl.setComponentAlignment(lbSelectedDate, Alignment.MIDDLE_CENTER);

        MHorizontalLayout layoutButtonNext = new MHorizontalLayout();
        layoutButtonNext.addComponent(btnShowNextMonth);
        layoutButtonNext.setComponentAlignment(btnShowNextMonth, Alignment.MIDDLE_RIGHT);
        layoutButtonNext.addComponent(btnShowNextYear);
        layoutButtonNext.setComponentAlignment(btnShowNextYear, Alignment.MIDDLE_RIGHT);
        layoutControl.addComponent(layoutButtonNext);
        layoutControl.setComponentAlignment(layoutButtonNext, Alignment.MIDDLE_RIGHT);

        this.addComponent(layoutControl);
        this.setComponentAlignment(layoutControl, Alignment.TOP_CENTER);

        this.addComponent(styleCalendar);
        this.setExpandRatio(styleCalendar, 1.0f);
    }

    public void setLabelTime(String date) {
        lbSelectedDate.setValue(date);
    }

    public void addSelectedDate(Date date) {
        if (date != null && !checkIfDateIsExistInSelectedDate(date)) {
            selectedDateList.add(date);
        }
    }

    private boolean checkIfDateIsExistInSelectedDate(Date date) {
        for (Date item : selectedDateList) {
            if (dateEquals(item, date)) {
                return true;
            }
        }
        return false;
    }

    private void setDateOptionsGenerator() {
        styleCalendar.setDateOptionsGenerator(new DateOptionsGenerator() {

            @Override
            public boolean isDateDisabled(Date date, StyleCalendar context) {
                return false;
            }

            @Override
            public String getTooltip(Date date, StyleCalendar context) {
                return null;
            }

            @Override
            public String getStyleName(Date date, StyleCalendar context) {
                for (Date redDate : selectedDateList) {
                    Calendar c1 = Calendar.getInstance();
                    c1.setTime(redDate);

                    if (dateEquals(date, redDate) && dateIsTodayOrBefore(redDate)) {
                        return "selected-ext";
                    }
                }
                return null;
            }
        });
    }

    private boolean dateIsTodayOrBefore(Date date) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.setTime(date);
        c2.setTime(styleCalendar.getShowingDate());

        return c1.get(Calendar.DATE) <= c2.getActualMaximum(Calendar.DAY_OF_MONTH) && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH);
    }

    private boolean dateEquals(Date first, Date second) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.setTime(first);
        c2.setTime(second);

        return c1.get(Calendar.DATE) == c2.get(Calendar.DATE)
                && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
                && c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR);
    }

    public void selectWeek(Date randomDay) {
        ArrayList<Date> daysInWeek = new ArrayList<Date>();
        daysInWeek.add(randomDay);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.setTime(randomDay);
        c2.setTime(randomDay);
        c2.add(Calendar.DATE, 1);

        while (c1.get(Calendar.WEEK_OF_YEAR) == c2.get(Calendar.WEEK_OF_YEAR)) {
            daysInWeek.add(c2.getTime());
            c2.add(Calendar.DATE, 1);
        }

        c2.setTime(randomDay);
        c2.add(Calendar.DATE, -1);

        while (c1.get(Calendar.WEEK_OF_YEAR) == c2.get(Calendar.WEEK_OF_YEAR)) {
            daysInWeek.add(c2.getTime());
            c2.add(Calendar.DATE, -1);
        }

        selectedDateList = daysInWeek;
        styleCalendar.setValue(randomDay);
    }

    public void selectDate(Date randomDay) {
        ArrayList<Date> oneDay = new ArrayList<Date>();
        oneDay.add(randomDay);

        selectedDateList = oneDay;
        styleCalendar.setValue(randomDay);
    }

    public StyleCalendar getStyleCalendar() {
        return styleCalendar;
    }

    public Button getBtnShowNextMonth() {
        return btnShowNextMonth;
    }

    public Button getBtnShowNextYear() {
        return btnShowNextYear;
    }

    public Button getBtnShowPreviousMonth() {
        return btnShowPreviousMonth;
    }

    public Button getBtnShowPreviousYear() {
        return btnShowPreviousYear;
    }
}
