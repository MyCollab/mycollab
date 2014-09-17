/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.module.project.view.task;

import com.esofthead.mycollab.mobile.ui.AbstractEditItemComp;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.i18n.TaskI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;

/**
 * @author MyCollab Ltd.
 *
 * @since 4.5.0
 */

@ViewComponent
public class TaskAddViewImpl extends AbstractEditItemComp<SimpleTask> implements
		TaskAddView {

	private static final long serialVersionUID = 6835605062072536907L;

	@Override
	protected String initFormTitle() {
		return (beanItem.getId() == null) ? AppContext
				.getMessage(TaskI18nEnum.FORM_NEW_TASK_TITLE) : beanItem
				.getTaskname();
	}

	@Override
	protected IFormLayoutFactory initFormLayoutFactory() {
		return new TaskFormLayoutFactory();
	}

	@Override
	protected AbstractBeanFieldGroupEditFieldFactory<SimpleTask> initBeanFormFieldFactory() {
		// TODO Auto-generated method stub
		return null;
	}

}
