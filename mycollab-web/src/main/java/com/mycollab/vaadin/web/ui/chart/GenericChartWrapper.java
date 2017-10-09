package com.mycollab.vaadin.web.ui.chart;

import com.mycollab.vaadin.mvp.PageView;
import com.google.common.collect.ImmutableList;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import org.jfree.chart.JFreeChart;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public abstract class GenericChartWrapper extends CssLayout implements PageView {
    private static final long serialVersionUID = 1L;

    protected static final List<String> CHART_COLOR_STR = ImmutableList.copyOf(new String[]{ColorConstants.BLUE,
            ColorConstants.GREEN, ColorConstants.ORANGE, ColorConstants.BLACK,
            ColorConstants.DARK_ORANGE, ColorConstants.LIGHT_BLUE,
            ColorConstants.GRAY, ColorConstants.BRIGHT_TURQUOISE,
            ColorConstants.LIGHT_GRAY, ColorConstants.CHERRY,
            ColorConstants.CONGO_PINK, ColorConstants.COFFFE,
            ColorConstants.COPPER, ColorConstants.RED,
            ColorConstants.LIGHTER_GREEN, ColorConstants.INDIAN_RED,
            ColorConstants.LAVENDER, ColorConstants.LEMON,
            ColorConstants.BROWN, ColorConstants.LIVER, ColorConstants.LION});

    private int height;
    private int width;

    public GenericChartWrapper(final int width, final int height) {
        this.width = width;
        this.height = height;
        this.setWidth("100%");
    }

    abstract protected JFreeChart createChart();

    protected abstract ComponentContainer createLegendBox();

    final protected void displayChart() {
        removeAllComponents();
        final JFreeChart chart = createChart();
        final JFreeChartWrapper chartWrapper = new JFreeChartWrapper(chart);

        final CssLayout borderWrap = new CssLayout();
        borderWrap.addComponent(chartWrapper);
        borderWrap.setStyleName("chart-wrapper");
        borderWrap.setHeight(height + "px");
        borderWrap.setWidth(width + "px");
        chartWrapper.setHeight(height + "px");
        chartWrapper.setWidth(width + "px");
        chartWrapper.setGraphHeight(height);
        chartWrapper.setGraphWidth(width);
        this.addComponent(borderWrap);
        final Component legendBox = createLegendBox();
        if (legendBox != null) {
            this.addComponent(legendBox);
        }
    }

    @Override
    public final <E> void addViewListener(ViewListener<E> listener) {

    }
}
