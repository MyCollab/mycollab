/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.web.ui.chart;

import com.mycollab.common.domain.GroupItem;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.RectangleInsets;
import org.jfree.util.Rotation;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.awt.*;
import java.text.AttributedString;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public abstract class PieChartWrapper<S extends SearchCriteria> extends GenericChartWrapper {
    private static final long serialVersionUID = 1L;

    private DefaultPieDataset pieDataSet;
    protected S searchCriteria;
    protected List<GroupItem> groupItems;
    private Class<? extends Enum<?>> enumKeyCls;

    public PieChartWrapper(final int width, final int height) {
        super(width, height);
    }

    public PieChartWrapper(Class<? extends Enum<?>> emumKey, final int width, final int height) {
        super(width, height);
        this.enumKeyCls = emumKey;
    }

    public void displayChart(final S criteria) {
        removeAllComponents();
        this.searchCriteria = criteria;
        this.groupItems = loadGroupItems();
        displayChart();
    }

    abstract protected List<GroupItem> loadGroupItems();

    @Override
    protected JFreeChart createChart() {
        // create the chart...
        pieDataSet = createDataset();
        final JFreeChart chart = ChartFactory.createPieChart3D("", // chart
                // title
                pieDataSet, // data
                false, // include legend
                true, // tooltips?
                false // URLs?
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.getTitle().setPaint(new Color(0x5E5E5E));
        chart.setBorderVisible(false);
        // set the background color for the chart...
        final PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setOutlineVisible(false);
        plot.setInsets(RectangleInsets.ZERO_INSETS);
        plot.setStartAngle(290);
        plot.setBackgroundPaint(Color.white);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.5f);
        plot.setNoDataMessage("No data to display");
        plot.setLabelGenerator(new JFreeChartLabelCustom());

        final List keys = pieDataSet.getKeys();
        for (int i = 0; i < keys.size(); i++) {
            final Comparable key = (Comparable) keys.get(i);
            int colorIndex = i % CHART_COLOR_STR.size();
            plot.setSectionPaint(key, Color.decode("0x" + CHART_COLOR_STR.get(colorIndex)));
        }
        // OPTIONAL CUSTOMISATION COMPLETED.
        return chart;
    }

    protected abstract DefaultPieDataset createDataset();

    class JFreeChartLabelCustom implements PieSectionLabelGenerator {
        @Override
        public String generateSectionLabel(PieDataset dataset, Comparable key) {
            String result = null;
            if (dataset != null) {
                int value = dataset.getValue(key).intValue();
                if (value == 0) {
                    return null;
                }

                if (enumKeyCls == null) {
                    if (key instanceof Key) {
                        return String.format("%s (%d)", StringUtils.trim(((Key) key).getDisplayName(), 20, true), value);
                    } else {
                        return String.format("%s (%d)", key.toString(), value);
                    }

                } else {
                    return String.format("%s (%d)", UserUIContext.getMessage(enumKeyCls, key.toString()), value);
                }
            }
            return result;
        }

        @Override
        public AttributedString generateAttributedSectionLabel(PieDataset dataset, Comparable key) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    @Override
    protected final ComponentContainer createLegendBox() {
        final CssLayout mainLayout = new CssLayout();
        mainLayout.addStyleName("legendBoxContent");
        mainLayout.setSizeUndefined();
        final List keys = pieDataSet.getKeys();

        for (int i = 0; i < keys.size(); i++) {
            MHorizontalLayout layout = new MHorizontalLayout().withMargin(new MarginInfo(false, false, false, true))
                    .withStyleName("inline-block").withWidthUndefined();
            layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

            final Comparable key = (Comparable) keys.get(i);
            int colorIndex = i % CHART_COLOR_STR.size();
            final String color = "<div style = \" width:13px;height:13px;background: #"
                    + CHART_COLOR_STR.get(colorIndex) + "\" />";
            final ELabel lblCircle = ELabel.html(color);

            String btnCaption;
            if (enumKeyCls == null) {
                if (key instanceof Key) {
                    btnCaption = String.format("%s (%d)", StringUtils.trim(((Key) key).getDisplayName(), 20, true),
                            pieDataSet.getValue(key).intValue());
                } else {
                    btnCaption = String.format("%s (%d)", key, pieDataSet.getValue(key).intValue());
                }
            } else {
                btnCaption = String.format("%s(%d)", UserUIContext.getMessage(enumKeyCls, key.toString()),
                        pieDataSet.getValue(key).intValue());
            }
            MButton btnLink = new MButton(StringUtils.trim(btnCaption, 25, true), clickEvent -> {
                if (key instanceof Key) {
                    clickLegendItem(((Key) key).getKey());
                } else {
                    clickLegendItem(key.toString());
                }
            }).withStyleName(WebThemes.BUTTON_LINK).withDescription(btnCaption);

            layout.with(lblCircle, btnLink);
            mainLayout.addComponent(layout);
        }
        mainLayout.setWidth("100%");
        return mainLayout;
    }

    abstract protected void clickLegendItem(String key);
}