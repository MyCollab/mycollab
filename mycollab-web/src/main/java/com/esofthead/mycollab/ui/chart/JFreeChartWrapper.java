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
/**
 * This file is part of mycollab-web-community.
 *
 * mycollab-web-community is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web-community is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web-community.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.ui.chart;

import com.vaadin.server.*;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Embedded;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.graphics2d.svg.SVGGraphics2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author MyCollab Ltd
 * @since 5.1.1
 */
@SuppressWarnings("serial")
public class JFreeChartWrapper extends Embedded {

    public enum RenderingMode {
        SVG, PNG, AUTO
    }

    private static final Logger log = LoggerFactory.getLogger(JFreeChartWrapper.class);

    // 809x 500 ~g olden ratio
    private static final int DEFAULT_WIDTH = 809;
    private static final int DEFAULT_HEIGHT = 500;

    private final JFreeChart chart;
    private Resource res;
    private RenderingMode mode = RenderingMode.AUTO;
    private boolean gzipEnabled = false;
    private int graphWidthInPixels = -1;
    private int graphHeightInPixels = -1;
    private String aspectRatio = "none"; // stretch to fill whole space

    public JFreeChartWrapper(JFreeChart chartToBeWrapped) {
        chart = chartToBeWrapped;
        setWidth(DEFAULT_WIDTH, Unit.PIXELS);
        setHeight(DEFAULT_HEIGHT, Unit.PIXELS);
    }

    /**
     * Compress SVG charts in wrapper. It makes sense to put this on if the
     * server does not automatically compress responses.
     *
     * @param compress true to enable component level compression, default false
     */
    public void setGzipCompression(boolean compress) {
        this.gzipEnabled = compress;
    }

    private void setRenderingMode(RenderingMode newMode) {
        if (newMode == RenderingMode.PNG) {
            setType(TYPE_IMAGE);
        } else {
            setType(TYPE_OBJECT);
            setMimeType("image/svg+xml");
        }
        mode = newMode;
    }

    @Override
    public void attach() {
        super.attach();
        if (mode == RenderingMode.AUTO) {
            WebBrowser browser = Page.getCurrent().getWebBrowser();
            if (browser.isIE() && browser.getBrowserMajorVersion() < 9) {
                setRenderingMode(RenderingMode.PNG);
            } else {
                // all decent browsers support SVG
                setRenderingMode(RenderingMode.SVG);
            }
        }
        setResource("src", getSource());
    }

    @Override
    public void detach() {
        super.detach();
    }

    /**
     * This method may be used to tune rendering of the chart when using
     * relative sizes. Most commonly you should use just use common methods
     * inherited from {@link Sizeable} interface.
     * <p/>
     * Sets the pixel size of the area where the graph is rendered. Most commonly developer may need to fine tune the value when the {@link JFreeChartWrapper} has a relative size.
     *
     * @param width
     * @see JFreeChartWrapper#getGraphWidth()
     * @see #setSvgAspectRatio(String)
     */
    public void setGraphWidth(int width) {
        graphWidthInPixels = width;
    }

    /**
     * This method may be used to tune rendering of the chart when using
     * relative sizes. Most commonly you should use just use common methods
     * inherited from {@link Sizeable} interface.
     * <p/>
     * Sets the pixel size of the area where the graph is rendered. Most commonly developer may need to fine tune the value when the {@link JFreeChartWrapper} has a relative size.
     *
     * @param height
     * @see JFreeChartWrapper#getGraphHeigt()
     * @see #setSvgAspectRatio(String)
     */
    public void setGraphHeight(int height) {
        graphHeightInPixels = height;
    }

    /**
     * Gets the pixel width into which the graph is rendered. Unless explicitly
     * set, the value is derived from the components size, except when the
     * component has relative size.
     */
    public int getGraphWidth() {
        if (graphWidthInPixels > 0) {
            return graphWidthInPixels;
        }
        int width;
        float w = getWidth();
        if (w < 0) {
            return DEFAULT_WIDTH;
        }
        switch (getWidthUnits()) {
            case CM:
                width = (int) (w * 96 / 2.54);
                break;
            case INCH:
                width = (int) (w * 96);
                break;
            case PERCENTAGE:
                width = DEFAULT_WIDTH;
                break;
            default:
                width = (int) w;
                break;
        }
        return width;
    }

    /**
     * Gets the pixel height into which the graph is rendered. Unless explicitly
     * set, the value is derived from the components size, except when the
     * component has relative size.
     */
    public int getGraphHeight() {
        if (graphHeightInPixels > 0) {
            return graphHeightInPixels;
        }
        int height;
        float w = getHeight();
        if (w < 0) {
            return DEFAULT_HEIGHT;
        }
        switch (getWidthUnits()) {
            case CM:
                height = (int) (w * 96 / 2.54);
                break;
            case INCH:
                height = (int) (w * 96);
                break;
            case PERCENTAGE:
                height = DEFAULT_HEIGHT;
                break;
            default:
                height = (int) w;
                break;
        }
        return height;
    }

    public String getSvgAspectRatio() {
        return aspectRatio;
    }

    /**
     * See SVG spec from W3 for more information.
     * Default is "none" (stretch), another common value is "xMidYMid" (stretch
     * proportionally, align middle of the area).
     *
     * @param svgAspectRatioSetting
     */
    public void setSvgAspectRatio(String svgAspectRatioSetting) {
        aspectRatio = svgAspectRatioSetting;
    }

    @Override
    public Resource getSource() {
        if (res == null) {
            StreamSource streamSource = new StreamResource.StreamSource() {
                private ByteArrayInputStream bytestream = null;

                ByteArrayInputStream getByteStream() {
                    if (chart != null && bytestream == null) {
                        int width = getGraphWidth();
                        int height = getGraphHeight();

                        if (mode == RenderingMode.SVG) {
                            // Create an instance of the SVG Generator
                            SVGGraphics2D svgGenerator = new SVGGraphics2D(width, height);

                            // draw the chart in the SVG generator
                            chart.draw(svgGenerator, new Rectangle(width, height));
                            // create an xml string in svg format from the drawing
                            String drawingSVG = svgGenerator.getSVGElement();
                            return new ByteArrayInputStream(drawingSVG.getBytes(StandardCharsets.UTF_8));
                        } else {
                            // Draw png to bytestream
                            try {
                                byte[] bytes = ChartUtilities.encodeAsPNG(chart.createBufferedImage(width, height));
                                bytestream = new ByteArrayInputStream(bytes);
                            } catch (Exception e) {
                                log.error("Error while generating PNG chart", e);
                            }

                        }

                    } else {
                        bytestream.reset();
                    }
                    return bytestream;
                }

                @Override
                public InputStream getStream() {
                    return getByteStream();
                }
            };

            res = new StreamResource(streamSource, String.format("graph%d", System.currentTimeMillis())) {

                @Override
                public int getBufferSize() {
                    if (getStreamSource().getStream() != null) {
                        try {
                            return getStreamSource().getStream().available();
                        } catch (IOException e) {
                            log.warn("Error while get stream info", e);
                            return 0;
                        }
                    } else {
                        return 0;
                    }
                }


                @Override
                public long getCacheTime() {
                    return 0;
                }

                @Override
                public String getFilename() {
                    if (mode == RenderingMode.PNG) {
                        return super.getFilename() + ".png";
                    } else {
                        return super.getFilename() + (gzipEnabled ? ".svgz" : ".svg");
                    }
                }

                @Override
                public DownloadStream getStream() {
                    DownloadStream downloadStream = new DownloadStream(
                            getStreamSource().getStream(), getMIMEType(), getFilename());
                    if (gzipEnabled && mode == RenderingMode.SVG) {
                        downloadStream.setParameter("Content-Encoding", "gzip");
                    }
                    return downloadStream;
                }

                @Override
                public String getMIMEType() {
                    if (mode == RenderingMode.PNG) {
                        return "image/png";
                    } else {
                        return "image/svg+xml";
                    }
                }
            };
        }
        return res;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void markAsDirty() {
        super.markAsDirty();
        res = null;
    }
}