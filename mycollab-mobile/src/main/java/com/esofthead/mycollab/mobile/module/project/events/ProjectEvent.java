package com.esofthead.mycollab.mobile.module.project.events;

import com.esofthead.mycollab.eventmanager.ApplicationEvent;

/**
 * @author MyCollab Ltd.
 * 
 * @since 4.4.0
 *
 */
public class ProjectEvent {
	public static class GotoProjectList extends ApplicationEvent {
		private static final long serialVersionUID = -3090515127947058922L;

		public GotoProjectList(Object source, Object data) {
			super(source, data);
		}

	}

    public static class GotoMyProject extends ApplicationEvent {

        private static final long serialVersionUID = -4439780369902098133L;

        public GotoMyProject(Object source, Object data) {
            super(source, data);
        }
    }
}
