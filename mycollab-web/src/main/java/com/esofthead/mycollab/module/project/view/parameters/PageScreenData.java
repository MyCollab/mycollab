package com.esofthead.mycollab.module.project.view.parameters;

import com.esofthead.mycollab.module.wiki.domain.Page;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.4.0
 *
 */
public class PageScreenData {

	public static class Read extends ScreenData<Integer> {

		public Read(Integer params) {
			super(params);
		}
	}

	public static class Add extends ScreenData<Page> {

		public Add(Page component) {
			super(component);
		}
	}

	public static class Edit extends ScreenData<Page> {

		public Edit(Page component) {
			super(component);
		}
	}

	public static class Search extends ScreenData<Object> {

		public Search() {
			super(null);
		}
	}
}
