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
package com.esofthead.mycollab.shell.view;

import com.esofthead.mycollab.vaadin.AsyncInvoker;
import com.esofthead.mycollab.vaadin.ui.ELabel;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.sliderpanel.SliderPanel;
import org.vaadin.sliderpanel.SliderPanelBuilder;
import org.vaadin.sliderpanel.client.SliderMode;
import org.vaadin.sliderpanel.client.SliderPanelListener;
import org.vaadin.sliderpanel.client.SliderTabPosition;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * @author MyCollab Ltd
 * @since 5.2.10
 */
public class CommunitySliderPanel {

    static SliderPanel buildCommunitySliderPanel() {
        final CommunitySliderContent sliderContent = new CommunitySliderContent();
        SliderPanel sliderPanel = new SliderPanelBuilder(sliderContent).caption("Community")
                .flowInContent(true).mode(SliderMode.RIGHT).tabPosition(SliderTabPosition.MIDDLE).build();
        sliderPanel.addListener(new SliderPanelListener() {
            @Override
            public void onToggle(boolean b) {
                if (b) {
                    sliderContent.loadContents();
                }
            }
        });
        return sliderPanel;
    }

    private static class CommunitySliderContent extends MVerticalLayout {
        private MVerticalLayout content;
        private DateTime lastTimeAccess;

        CommunitySliderContent() {
            withWidth("500px").withStyleName("community");
            Div blogLink = new Div().appendText(FontAwesome.INSTITUTION.getHtml() + " ").appendChild(new A("https://www" +
                    ".mycollab.com").appendText("Blog").setTarget("_blank"));
            with(new ELabel(blogLink.write(), ContentMode.HTML).withStyleName(ValoTheme.LABEL_H2));
            content = new MVerticalLayout();
            with(content);
        }

        void loadContents() {
            DateTime now = new DateTime();
            if (lastTimeAccess == null || (now.getMillis() - lastTimeAccess.getMillis()) > 86400000) {
                content.removeAllComponents();
                content.addComponent(new Label("Waiting to load feeds"));
                lastTimeAccess = now;
                AsyncInvoker.access(new AsyncInvoker.PageCommand() {
                    @Override
                    public void run() {
                        try {
                            SyndFeed feed = getSyndFeedForUrl("https://www.mycollab.com/feed/atom/");
                            final List<SyndEntry> entries = feed.getEntries();
                            if (CollectionUtils.isNotEmpty(entries)) {
                                content.removeAllComponents();
                                for (SyndEntry entry : entries) {
                                    Div link = new Div().appendText(FontAwesome.INBOX.getHtml() + " ").appendChild(new A(entry
                                            .getLink()).appendText(entry.getTitle()).setTarget("_blank"));
                                    content.with(new ELabel(link.write(), ContentMode.HTML).withStyleName("title"));
                                    Div description = new Div().appendText(entry.getDescription().getValue());
                                    content.addComponent(new ELabel(description.write(), ContentMode.HTML).withStyleName("desc"));
                                }
                            }
                        } catch (Exception e) {
                            with(new Label("Can not load MyCollab's feeds"));
                        }
                    }
                });
            }
        }

        private SyndFeed getSyndFeedForUrl(String url) throws IOException, IllegalArgumentException, FeedException {
            SyndFeed feed = null;
            InputStream is = null;

            try {
                URLConnection openConnection = new URL(url).openConnection();
                is = new URL(url).openConnection().getInputStream();
                if ("gzip".equals(openConnection.getContentEncoding())) {
                    is = new GZIPInputStream(is);
                }
                InputSource source = new InputSource(is);
                SyndFeedInput input = new SyndFeedInput();
                feed = input.build(source);

            } finally {
                if (is != null) is.close();
            }

            return feed;
        }
    }
}
