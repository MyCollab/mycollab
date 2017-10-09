package com.mycollab.vaadin.web.ui;

import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.Iterator;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ServiceMenu extends MHorizontalLayout {
    private static final long serialVersionUID = 1L;

    private static final String COMPONENT_STYLENAME = "service-menu";
    private static final String SELECTED_STYLENAME = "selected";

    public ServiceMenu() {
        this.setStyleName(COMPONENT_STYLENAME);
    }

    public Button addService(String serviceName, Resource linkIcon, ClickListener listener) {
        Button serviceBtn = new Button(serviceName, listener);
        serviceBtn.setIcon(linkIcon);
        this.with(serviceBtn);
        return serviceBtn;
    }

    public Button addService(String serviceName, ClickListener listener) {
        Button serviceBtn = new Button(serviceName, listener);
        this.with(serviceBtn);
        return serviceBtn;
    }

    public Button addService(String id, String serviceName, ClickListener listener) {
        Button serviceBtn = new Button(serviceName, listener);
        serviceBtn.setId(id);
        this.with(serviceBtn);
        return serviceBtn;
    }

    public void selectService(int index) {
        Iterator<Component> iterator = this.iterator();

        int i = 0;
        while (iterator.hasNext()) {
            Component comp = iterator.next();
            if (i == index) {
                comp.addStyleName(SELECTED_STYLENAME);
            } else {
                comp.removeStyleName(SELECTED_STYLENAME);
            }
            i++;
        }
    }

}
