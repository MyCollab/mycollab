package com.mycollab.module.ecm;

import javax.jcr.Node;

/**
 * Utility class relate to jackrabbit node processing.
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class NodesUtil {
    public static String getString(Node node, String property) {
        return getString(node, property, "");
    }

    public static String getString(Node node, String property, String defaultValue) {
        try {
            return node.getProperty(property).getString();
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
