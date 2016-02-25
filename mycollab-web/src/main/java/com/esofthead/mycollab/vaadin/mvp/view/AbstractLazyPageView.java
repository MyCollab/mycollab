/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.vaadin.mvp.view;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.vaadin.AsyncInvoker;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.LazyPageView;
import com.hp.gagawa.java.elements.Div;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

/**
 * @author MyCollab Ltd.
 * @since 4.1.2
 */
public abstract class AbstractLazyPageView extends AbstractPageView implements LazyPageView {
    private static final long serialVersionUID = 1L;

    private boolean isRunning = false;
    private ProgressIndicator progressIndicator = null;

    @Override
    public void lazyLoadView() {
        if (!isRunning) {
            this.removeAllComponents();
            isRunning = true;
            AsyncInvoker.access(new AsyncInvoker.PageCommand() {
                @Override
                public void run() {
                    progressIndicator = new ProgressIndicator();
                    getUI().addWindow(progressIndicator);
                }

                @Override
                public void postRun() {
                    try {
                        displayView();
                    } catch (Exception e) {
                        throw new MyCollabException(e);
                    }
                }

                @Override
                public void cleanUp() {
                    getUI().removeWindow(progressIndicator);
                    isRunning = false;
                }
            });
        }
    }

    abstract protected void displayView();

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

            Div div = new Div().appendChild(new Div().setCSSClass("sk-cube sk-cube1"))
                    .appendChild(new Div().setCSSClass("sk-cube sk-cube2"))
                    .appendChild(new Div().setCSSClass("sk-cube sk-cube3"))
                    .appendChild(new Div().setCSSClass("sk-cube sk-cube4"))
                    .appendChild(new Div().setCSSClass("sk-cube sk-cube5"))
                    .appendChild(new Div().setCSSClass("sk-cube sk-cube6"))
                    .appendChild(new Div().setCSSClass("sk-cube sk-cube7"))
                    .appendChild(new Div().setCSSClass("sk-cube sk-cube8"))
                    .appendChild(new Div().setCSSClass("sk-cube sk-cube9"));
            Label loadingIcon = new Label(div.write(), ContentMode.HTML);
            loadingIcon.addStyleName("sk-cube-grid");
            this.setContent(loadingIcon);
        }
    }
}
