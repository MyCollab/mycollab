/**
 * This file is part of mycollab-ui.
 *
 * mycollab-ui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-ui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-ui.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.vaadin.mvp;

import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.vaadin.ui.Image;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1.2
 * 
 */
public abstract class AbstractLazyPageView extends AbstractPageView implements
		LazyPageView {
	private static final long serialVersionUID = 1L;

	private boolean isRunning = false;
	private ProgressIndicator progressIndicator = null;

	@Override
	public void lazyLoadView() {
		if (!isRunning) {
			this.removeAllComponents();
			isRunning = true;
			progressIndicator = new ProgressIndicator();
			UI.getCurrent().addWindow(progressIndicator);
			UI.getCurrent().setPollInterval(300);
			new InitializerThread().start();
		}
	}

	abstract protected void displayView();

	class InitializerThread extends Thread {
		@Override
		public void run() {
			UI.getCurrent().access(new Runnable() {

				@Override
				public void run() {
					displayView();
					UI.getCurrent().setPollInterval(-1);
					progressIndicator.close();
					progressIndicator = null;
					isRunning = false;
				}

			});
		};
	}

	private static class ProgressIndicator extends Window {
		private static final long serialVersionUID = -6157950150738214354L;

		public ProgressIndicator() {
			super();
			this.setDraggable(false);
			this.setClosable(false);
			this.setResizable(false);
			this.setStyleName("lazyload-progress");
			this.center();
			this.setModal(true);

			Image loadingIcon = new Image(null,
					MyCollabResource.newResource("icons/lazy-load-icon.gif"));
			this.setContent(loadingIcon);
		}
	}
}
