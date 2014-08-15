package com.esofthead.mycollab.mobile.module.project.view.parameters;

import com.esofthead.mycollab.module.project.domain.Project;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
public class ProjectScreenData {
    public static class Goto extends ScreenData<Integer> {

        public Goto(Integer params) {
            super(params);
        }
    }

    public static class Edit extends ScreenData<Integer> {

        public Edit(Integer params) {
            super(params);
        }
    }
}
