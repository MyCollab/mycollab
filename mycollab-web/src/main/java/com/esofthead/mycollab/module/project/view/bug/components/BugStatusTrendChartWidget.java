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
package com.esofthead.mycollab.module.project.view.bug.components;

import com.esofthead.mycollab.common.domain.GroupItem;
import com.esofthead.mycollab.common.domain.criteria.TimelineTrackingSearchCriteria;
import com.esofthead.mycollab.common.service.TimelineTrackingService;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.spring.AppContextUtil;
import com.esofthead.mycollab.ui.chart.GenericChartWrapper;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.web.ui.Depot;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleInsets;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.2.2
 */
public class BugStatusTrendChartWidget extends Depot {
    private static final DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd").withZone(DateTimeZone.UTC);

    public BugStatusTrendChartWidget() {
        super(AppContext.getMessage(ProjectCommonI18nEnum.OPT_RESOLVING_TREND_IN_DURATION, 30), new MVerticalLayout());
        setContentBorder(true);
    }

    public void display(TimelineTrackingSearchCriteria searchCriteria) {
        MVerticalLayout content = (MVerticalLayout) getContent();
        content.removeAllComponents();
        BugStatusChartWrapper chartWrapper = new BugStatusChartWrapper();
        content.addComponent(chartWrapper);
        chartWrapper.display(searchCriteria);
    }

    private static class BugStatusChartWrapper extends GenericChartWrapper {
        private TimelineTrackingService timelineTrackingService;
        private Map<String, List<GroupItem>> groupItems;
        private TimeSeriesCollection dataset;

        public BugStatusChartWrapper() {
            super(350, 280);
            timelineTrackingService = AppContextUtil.getSpringBean(TimelineTrackingService.class);
        }

        @Override
        protected JFreeChart createChart() {
            dataset = new TimeSeriesCollection();
            if (groupItems != null) {
                Set<Map.Entry<String, List<GroupItem>>> entries = groupItems.entrySet();
                Map<Date, Integer> openMap = new HashMap<>(30);
                for (Map.Entry<String, List<GroupItem>> entry : entries) {
                    if (OptionI18nEnum.BugStatus.Verified.name().equals(entry.getKey())) {
                        TimeSeries series = new TimeSeries(entry.getKey());
                        for (GroupItem item : entry.getValue()) {
                            series.add(new Day(formatter.parseDateTime(item.getGroupname()).toDate()), item.getValue());
                        }
                        dataset.addSeries(series);
                    } else {
                        for (GroupItem item : entry.getValue()) {
                            Date date = formatter.parseDateTime(item.getGroupname()).toDate();
                            Integer val = openMap.get(date);
                            if (val == null) {
                                openMap.put(date, item.getValue().intValue());
                            } else {
                                openMap.put(date, val + item.getValue().intValue());
                            }
                        }
                    }
                }

                TimeSeries series = new TimeSeries("Unverified");
                for (Map.Entry<Date, Integer> entry : openMap.entrySet()) {
                    series.add(new Day(entry.getKey()), entry.getValue());
                }
                dataset.addSeries(series);

                JFreeChart chart = ChartFactory.createTimeSeriesChart("", "", "", dataset, false, true, false);
                chart.setBackgroundPaint(Color.white);

                XYPlot plot = (XYPlot) chart.getPlot();
                plot.setBackgroundPaint(Color.white);
                plot.setDomainGridlinePaint(Color.lightGray);
                plot.setRangeGridlinePaint(Color.cyan);
                plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
                plot.setDomainCrosshairVisible(true);
                plot.setRangeCrosshairVisible(true);
                XYItemRenderer r = plot.getRenderer();
                if (r instanceof XYLineAndShapeRenderer) {
                    XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
                    renderer.setBaseShapesVisible(true);
                    renderer.setBaseShapesFilled(true);
                    renderer.setDrawSeriesLineAsPath(true);
                    for (int i = 0; i < entries.size(); i++) {
                        int colorIndex = i % CHART_COLOR_STR.size();
                        renderer.setSeriesPaint(i, Color.decode("0x" + CHART_COLOR_STR.get(colorIndex)));
                    }
                }
                NumberAxis valueAxis = (NumberAxis) plot.getRangeAxis();
                valueAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
                return chart;
            } else {
                return ChartFactory.createTimeSeriesChart("", "", "", new TimeSeriesCollection(), false, true, false);
            }
        }

        @Override
        protected final ComponentContainer createLegendBox() {
            final CssLayout mainLayout = new CssLayout();
            mainLayout.addStyleName("legendBoxContent");
            mainLayout.setWidth("100%");
            final List series = dataset.getSeries();

            for (int i = 0; i < series.size(); i++) {
                final MHorizontalLayout layout = new MHorizontalLayout().withMargin(new MarginInfo(false, false, false, true));
                layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

                final TimeSeries key = (TimeSeries) series.get(i);
                int colorIndex = i % CHART_COLOR_STR.size();
                final String color = "<div style = \" width:13px;height:13px;background: #"
                        + CHART_COLOR_STR.get(colorIndex) + "\" />";
                final Label lblCircle = new Label(color);
                lblCircle.setContentMode(ContentMode.HTML);
                String captionBtn = AppContext.getMessage(OptionI18nEnum.BugStatus.class, (String) key.getKey());
                final Button btnLink = new Button(StringUtils.trim(captionBtn, 25, true));
                btnLink.setDescription(captionBtn);
                btnLink.addStyleName(UIConstants.BUTTON_LINK);
                layout.with(lblCircle, btnLink);
                mainLayout.addComponent(layout);
            }

            return mainLayout;
        }

        void display(TimelineTrackingSearchCriteria searchCriteria) {
            searchCriteria.setType(StringSearchField.and(ProjectTypeConstants.BUG));
            searchCriteria.setFieldgroup(StringSearchField.and("status"));
            LocalDate endDate = new LocalDate(new GregorianCalendar().getTime());
            LocalDate startDate = endDate.minusDays(30);
            groupItems = timelineTrackingService.findTimelineItems("status", Arrays.asList(
                    OptionI18nEnum.BugStatus.Open.name(), OptionI18nEnum.BugStatus.ReOpen.name(),
                    OptionI18nEnum.BugStatus.Resolved.name(),
                    OptionI18nEnum.BugStatus.Verified.name()),
                    startDate.toDate(), endDate.toDate(), searchCriteria);
            displayChart();
        }
    }
}
