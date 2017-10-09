package com.mycollab.vaadin.ui;

import com.mycollab.core.utils.StringUtils;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * @author MyCollab Ltd.
 * @since 5.0.3
 */
public class SafeHtmlLabel extends Label {
    public SafeHtmlLabel(String value) {
        super(StringUtils.formatRichText(value), ContentMode.HTML);
        this.addStyleName(UIConstants.LABEL_WORD_WRAP);
    }

    public SafeHtmlLabel(String value, int trimCharacters) {
        Document doc = Jsoup.parse(value);
        String content = doc.body().text();
        content = StringUtils.trim(content, trimCharacters);
        this.setValue(content);
        this.addStyleName(UIConstants.LABEL_WORD_WRAP);
    }
}
