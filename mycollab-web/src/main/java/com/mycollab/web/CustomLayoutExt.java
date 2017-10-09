package com.mycollab.web;

import com.mycollab.core.MyCollabException;
import com.vaadin.ui.CustomLayout;

/**
 * Dynamic load custom layout per classpath, not absolutely path
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class CustomLayoutExt extends CustomLayout {
    private static final long serialVersionUID = 1L;

    public CustomLayoutExt(String layoutId) {
        try {
            initTemplateContentsFromInputStream(CustomLayoutExt.class.getClassLoader().getResourceAsStream("layouts/" + layoutId + ".html"));
        } catch (Exception e) {
            this.setTemplateName(layoutId);
        }
    }

    public static CustomLayout createLayout(String layoutId) {
        try {
            return new CustomLayout(CustomLayoutExt.class.getClassLoader().getResourceAsStream("layouts/" + layoutId + ".html"));
        } catch (Exception e) {
            throw new MyCollabException(e);
        }
    }
}
