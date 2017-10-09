package com.mycollab.vaadin.web.ui.chart;

/**
 * @author MyCollab Ltd
 * @since 5.2.0
 */
public class Key implements Comparable {
    private String key;
    private String displayName;

    public Key(String key, String displayName) {
        this.key = key;
        this.displayName = displayName;
    }

    public String getKey() {
        return key;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof Key)) {
            throw new IllegalArgumentException("Invalid param");
        } else {
            Key tmp = (Key) o;
            int result = key.compareTo(tmp.key);
            if (result != 0) {
                return displayName.compareTo(tmp.displayName);
            }
            return result;
        }
    }
}
