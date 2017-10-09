package com.mycollab.mobile.ui;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.vaadin.UserUIContext;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.server.Resource;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Image;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
public class AttachmentPreviewView extends AbsoluteLayout implements IMobileView {
    private static final long serialVersionUID = -6489047489795500038L;

    private Resource currentResource;
    private Image previewImage;
    private NavigationButton backBtn;

    public AttachmentPreviewView() {
        CssLayout imgWrap = new CssLayout();
        imgWrap.setStyleName("image-wrap");
        imgWrap.setSizeFull();

        this.setStyleName("attachment-preview-view");
        this.setSizeFull();
        this.addComponent(imgWrap, "top: 0px left: 0px; z-index: 0;");

        backBtn = new NavigationButton(UserUIContext.getMessage(GenericI18Enum.M_BUTTON_BACK));
        backBtn.setStyleName("back-btn");

        this.addComponent(backBtn, "top: 15px; left: 15px; z-index: 1;");

        previewImage = new Image();
        imgWrap.addComponent(previewImage);
    }

    public AttachmentPreviewView(Resource previewImage) {
        this();
        setResource(previewImage);
    }

    public void setResource(Resource previewImage) {
        this.currentResource = previewImage;
        this.previewImage.setSource(currentResource);
    }

    public Resource getResource() {
        return this.currentResource;
    }

    @Override
    public void setPreviousComponent(Component comp) {
        backBtn.setTargetView(comp);
    }
}
