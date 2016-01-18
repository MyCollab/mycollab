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
package com.esofthead.mycollab.module.project.ui.form;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.common.domain.SimpleMonitorItem;
import com.esofthead.mycollab.common.domain.criteria.MonitorSearchCriteria;
import com.esofthead.mycollab.common.service.MonitorItemService;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.arguments.ValuedBean;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectMemberStatusConstants;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.vaadin.data.Property;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomField;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.5.3
 *
 * @param <V>
 */
@SuppressWarnings("rawtypes")
public class ProjectFormWatcherSelectField<V extends ValuedBean> extends
		CustomField {
	private static final Logger LOG = LoggerFactory
			.getLogger(ProjectFormWatcherSelectField.class);

	private static final long serialVersionUID = 1L;

	private ProjectMemberService memberService;
	private MonitorItemService monitorItemService;

	private CssLayout wrapper = new CssLayout();

	private String type;
	private Integer typeId = null;
	private boolean checkAll = false;

	private List<SimpleProjectMember> selectedMembers = new ArrayList<SimpleProjectMember>();
	private List<SimpleProjectMember> projectMembers;

	public ProjectFormWatcherSelectField(V bean, String type, boolean checkAll) {
		this(bean, type);
		this.checkAll = true;
	}

	public ProjectFormWatcherSelectField(V bean, String type) {
		super();
		this.memberService = ApplicationContextUtil
				.getSpringBean(ProjectMemberService.class);
		this.monitorItemService = ApplicationContextUtil
				.getSpringBean(MonitorItemService.class);
		this.type = type;
		try {
			this.typeId = (Integer) PropertyUtils.getProperty(bean, "id");
		} catch (IllegalAccessException | InvocationTargetException
				| NoSuchMethodException e) {
			LOG.error("Error", e);
		}
	}

	@Override
	public Class<?> getType() {
		return Object.class;
	}

	public void checkItem(String name) {
		for (int i = 0; i < wrapper.getComponentCount(); i++) {
			CheckBox checkBox = ((CheckBox) wrapper.getComponent(i));
			if (checkBox.getCaption().equals(name)) {
				checkBox.setValue(true);
				break;
			}
		}
	}

	@Override
	protected Component initContent() {
		wrapper.setWidth("100%");
		final CheckBox selectAllCheckbox = new CheckBox("All");
		selectAllCheckbox.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				boolean isSelected = (Boolean) event.getProperty().getValue();
				for (int i = 0; i < wrapper.getComponentCount(); i++) {
					((CheckBox) wrapper.getComponent(i)).setValue(isSelected);
				}
			}
		});
		selectAllCheckbox.addStyleName("watcher-field");
		wrapper.addComponent(selectAllCheckbox);

		List<SimpleMonitorItem> preselectedMonitorItems = getPreselectedMonitorItems();
		projectMembers = getActiveMembers();
		for (int i = 0; i < projectMembers.size(); i++) {
			final SimpleProjectMember member = projectMembers.get(i);
			String fullname = member.getMemberFullName();

			CheckBox checkbox = new CheckBox(fullname);
			checkbox.setIcon(UserAvatarControlFactory.createAvatarResource(
					member.getMemberAvatarId(), 16));
			checkbox.addStyleName("watcher-field");
			checkbox.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(Property.ValueChangeEvent event) {
					if ((Boolean) event.getProperty().getValue()) {
						selectedMembers.add(member);
						if (selectedMembers.size() == projectMembers.size()) {
							selectAllCheckbox.setValue(true);
						}
					} else {
						selectedMembers.remove(member);
						selectAllCheckbox.setValue(false);
					}
				}
			});
			if (member.getUsername().equals(AppContext.getUsername())) {
				checkbox.setValue(true);
			}
			wrapper.addComponent(checkbox);

			if (checkAll) {
				checkbox.setValue(true);
			} else {
				for (SimpleMonitorItem monitorItem : preselectedMonitorItems) {
					if (member.getUsername().equals(monitorItem.getUser())) {
						checkbox.setValue(true);
					}
				}
			}
		}
		return wrapper;
	}

	private List<SimpleProjectMember> getActiveMembers() {
		ProjectMemberSearchCriteria criteria = new ProjectMemberSearchCriteria();
		criteria.setProjectId(new NumberSearchField(CurrentProjectVariables
				.getProjectId()));
		criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
		criteria.setStatus(StringSearchField.and(
				ProjectMemberStatusConstants.ACTIVE));
		return memberService
				.findPagableListByCriteria(new SearchRequest<ProjectMemberSearchCriteria>(
						criteria, 0, Integer.MAX_VALUE));
	}

	private List<SimpleMonitorItem> getPreselectedMonitorItems() {
		if (typeId == null) {
			return new ArrayList<SimpleMonitorItem>();
		}
		MonitorSearchCriteria criteria = new MonitorSearchCriteria();
		criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
		criteria.setType(StringSearchField.and(type));
		criteria.setTypeId(new NumberSearchField(typeId));
		return monitorItemService
				.findPagableListByCriteria(new SearchRequest<MonitorSearchCriteria>(
						criteria, 0, Integer.MAX_VALUE));
	}

	public List<SimpleMonitorItem> getSelectedMonitorItems() {
		List<SimpleMonitorItem> selectedMonitorItems = new ArrayList<SimpleMonitorItem>();
		if (selectedMembers.size() == projectMembers.size()) {
			SimpleMonitorItem monitorItem = new SimpleMonitorItem();
			monitorItem.setMonitorDate(new GregorianCalendar().getTime());
			monitorItem.setType(type);
			monitorItem.setTypeid(typeId);
			monitorItem.setExtratypeid(CurrentProjectVariables.getProjectId());
			monitorItem.setUser(null);
			monitorItem.setSaccountid(AppContext.getAccountId());
			selectedMonitorItems.add(monitorItem);
		} else {
			for (SimpleProjectMember member : selectedMembers) {
				SimpleMonitorItem monitorItem = new SimpleMonitorItem();
				monitorItem.setMonitorDate(new GregorianCalendar().getTime());
				monitorItem.setType(type);
				monitorItem.setTypeid(typeId);
				monitorItem.setExtratypeid(CurrentProjectVariables
						.getProjectId());
				monitorItem.setUser(member.getUsername());
				monitorItem.setSaccountid(AppContext.getAccountId());
				selectedMonitorItems.add(monitorItem);
			}
		}
		return selectedMonitorItems;
	}
}
