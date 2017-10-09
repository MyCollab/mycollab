package com.mycollab.vaadin.web.ui;

import com.mycollab.web.CustomLayoutExt;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.UI;

import java.util.concurrent.locks.Lock;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class ProgressBarIndicator extends CustomLayoutExt {
    private static final long serialVersionUID = 1L;

    private ProgressBar progressIndicator;
    private Label progressStatusLabel;

    public ProgressBarIndicator() {
        super("progressBar");
        this.progressIndicator = new ProgressBar();
        this.progressIndicator.setWidth("100%");
        this.progressStatusLabel = new Label();
        this.progressStatusLabel.setWidth("100%");
        this.progressStatusLabel.setHeight("100%");
        this.addComponent(this.progressIndicator, "progressbar-container");
        this.addComponent(this.progressStatusLabel, "progressbar-status");
    }

    public ProgressBarIndicator(int total, int remaining) {
        this(total, remaining, true);
    }

    public ProgressBarIndicator(int total, int remaining, Boolean displayPercentage) {
        this();
        float value = (total != 0) ? ((float) (total - remaining) / total) : 0;
        progressIndicator.setValue(value);
        if (displayPercentage) {
            if (total > 0) {
                this.progressStatusLabel.setValue(String.format("%.0f", value * 100) + "%");
            } else {
                this.progressStatusLabel = new Label("100%");
            }
        } else {
            this.progressStatusLabel.setValue(String.valueOf(total - remaining) + " / " + String.valueOf(total));
        }
    }

    public void setProgressValue(float value) {
        Lock lock = UI.getCurrent().getSession().getLockInstance();
        lock.lock();
        this.progressIndicator.setValue(value);
        this.progressStatusLabel.setValue(String.format("%.0f", value * 100) + "%");
        lock.unlock();
    }
}
