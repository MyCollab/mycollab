package com.mycollab.common;

import com.mycollab.module.project.ProjectTypeConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * @author MyCollab Ltd
 * @since 5.2.5
 */
public class FontAwesomeUtils {
    private static final Map<String, String> resources;

    static {
        resources = new HashMap<>();
        resources.put(ProjectTypeConstants.BUG, Integer.toHexString('\uf188'));
        resources.put(ProjectTypeConstants.TASK, Integer.toHexString('\uf0ae'));
        resources.put(ProjectTypeConstants.RISK, Integer.toHexString('\uf132'));
        resources.put(ProjectTypeConstants.MILESTONE, Integer.toHexString('\uf11e'));
        resources.put(ProjectTypeConstants.PROJECT, Integer.toHexString('\uf133'));
    }

    public static String toHtml(String resId) {
        return "<span class=\"v-icon\" style=\"font-family: FontAwesome;\">&#x" + resources.get(resId) + ";" + "</span>";
    }
}
