package com.mycollab.vaadin.web.ui;

import com.mycollab.vaadin.ui.PropertyChangedEvent;
import com.mycollab.vaadin.ui.PropertyChangedListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.PopupView;
import org.vaadin.jouni.restrain.Restrain;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.1.3
 */
public class LazyPopupView extends PopupView {

    public LazyPopupView(String valueAsHtml) {
        super(new PopupContent(valueAsHtml));
        addPopupVisibilityListener(popupVisibilityEvent -> {
            if (popupVisibilityEvent.isPopupVisible()) {
                doShow();
            } else {
                doHide();
            }
        });
        ((PopupContent)getContent()).setDimensionConstraint(getConstraintWidth(), getConstraintHeight());
        this.setStyleName("block-popupedit");
        this.setHideOnMouseOut(false);
    }

    protected String getConstraintWidth() {
        return "700px";
    }

    protected String getConstraintHeight() {
        return "600px";
    }

    public MVerticalLayout getWrapContent() {
        PopupView.Content content = getContent();
        return (MVerticalLayout) content.getPopupComponent();
    }

    public void setMinimizedValueAsHTML(String valueAsHtml) {
        PopupContent content = (PopupContent) getContent();
        content.setMinimizedValueAsHTML(valueAsHtml);
    }

    public void addPropertyChangeListener(PropertyChangedListener listener) {
        addListener("propertyChangeEvent", PropertyChangedEvent.class, listener, PropertyChangedListener.viewInitMethod);
    }

    protected void doShow() {
    }

    protected void doHide() {
    }

    private static class PopupContent implements PopupView.Content {
        private String valueAsHtml;
        private MVerticalLayout content;

        PopupContent(String valueAsHtml) {
            this.valueAsHtml = valueAsHtml;
            content = new MVerticalLayout().withFullHeight().withFullWidth();
        }

        void setDimensionConstraint(String width, String height) {
            new Restrain(content).setMaxWidth(width).setMaxHeight(height);
        }

        void setMinimizedValueAsHTML(String valueAsHtml) {
            this.valueAsHtml = valueAsHtml;
        }

        @Override
        public String getMinimizedValueAsHTML() {
            return valueAsHtml;
        }

        @Override
        public Component getPopupComponent() {
            return content;
        }
    }
}
