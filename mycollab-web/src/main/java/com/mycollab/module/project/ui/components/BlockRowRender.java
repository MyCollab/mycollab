package com.mycollab.module.project.ui.components;

import com.mycollab.vaadin.ui.UIUtils;
import com.vaadin.ui.ComponentContainer;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.4.3
 */
public class BlockRowRender extends MVerticalLayout {

    public void selfRemoved() {
        IBlockContainer blockContainer = UIUtils.getRoot(this, IBlockContainer.class);
        ComponentContainer container = (ComponentContainer) getParent();
        if (container != null) {
            container.removeComponent(this);
        }
        if (blockContainer != null) {
            blockContainer.refresh();
        }
    }
}
