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
package com.esofthead.mycollab.module.project.view.bug;

import com.esofthead.mycollab.cache.CleanCacheEvent;
import com.esofthead.mycollab.common.domain.MonitorItem;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.service.MonitorItemService;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.events.AssignmentEvent;
import com.esofthead.mycollab.module.project.events.BugEvent;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.project.ui.components.ProjectSubscribersComp;
import com.esofthead.mycollab.module.project.ui.form.ProjectFormAttachmentUploadField;
import com.esofthead.mycollab.module.tracker.domain.BugWithBLOBs;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.service.BugRelatedItemService;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.AdvancedEditBeanForm;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.esofthead.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.google.common.eventbus.AsyncEventBus;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.vaadin.jouni.restrain.Restrain;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.2.0
 */
public class BugAddWindow extends Window {
    public BugAddWindow(SimpleBug bug) {
        if (bug.getId() == null) {
            setCaption("New bug");
        } else {
            setCaption("Edit bug");
        }
        this.setWidth("800px");
        this.setModal(true);
        this.setResizable(false);

        EditForm editForm = new EditForm();
        editForm.setBean(bug);
        this.setContent(editForm);
    }

    private class EditForm extends AdvancedEditBeanForm<SimpleBug> {
        @Override
        public void setBean(final SimpleBug item) {
            this.setFormLayoutFactory(new FormLayoutFactory());
            this.setBeanFormFieldFactory(new BugEditFormFieldFactory(EditForm.this, item.getProjectid()));
            super.setBean(item);
        }

        class FormLayoutFactory implements IFormLayoutFactory {
            private static final long serialVersionUID = 1L;
            private GridFormLayoutHelper informationLayout;

            @Override
            public ComponentContainer getLayout() {
                VerticalLayout layout = new VerticalLayout();
                informationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(2, 9);
                informationLayout.getLayout().setMargin(false);
                informationLayout.getLayout().setSpacing(false);
                layout.addComponent(informationLayout.getLayout());

                MHorizontalLayout buttonControls = new MHorizontalLayout().withMargin(new MarginInfo(true, true, true, false));
                buttonControls.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);

                Button updateAllBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_UPDATE_OTHER_FIELDS), new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent clickEvent) {
                        EventBusFactory.getInstance().post(new BugEvent.GotoAdd(BugAddWindow.this, bean));
                        close();
                    }
                });
                updateAllBtn.addStyleName(UIConstants.BUTTON_LINK);

                Button saveBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_SAVE), new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent clickEvent) {
                        if (EditForm.this.validateForm()) {
                            BugService bugService = ApplicationContextUtil.getSpringBean(BugService.class);
                            Integer bugId;
                            if (bean.getId() == null) {
                                bugId = bugService.saveWithSession(bean, AppContext.getUsername());
                            } else {
                                bugService.updateWithSession(bean, AppContext.getUsername());
                                bugId = bean.getId();
                            }

                            AsyncEventBus asyncEventBus = ApplicationContextUtil.getSpringBean(AsyncEventBus.class);
                            // save component
                            BugEditFormFieldFactory bugEditFormFieldFactory = (BugEditFormFieldFactory) fieldFactory;
                            BugRelatedItemService bugRelatedItemService = ApplicationContextUtil.getSpringBean(BugRelatedItemService.class);
                            bugRelatedItemService.saveAffectedVersionsOfBug(bugId, bugEditFormFieldFactory.getAffectedVersionSelect().getSelectedItems());
                            bugRelatedItemService.saveComponentsOfBug(bugId, bugEditFormFieldFactory.getComponentSelect().getSelectedItems());
                            asyncEventBus.post(new CleanCacheEvent(AppContext.getAccountId(), new Class[]{BugService.class}));

                            ProjectFormAttachmentUploadField uploadField = bugEditFormFieldFactory.getAttachmentUploadField();
                            uploadField.saveContentsToRepo(bean.getProjectid(),
                                    ProjectTypeConstants.BUG, bugId);
                            EventBusFactory.getInstance().post(new BugEvent.NewBugAdded(BugAddWindow.this, bugId));
                            EventBusFactory.getInstance().post(new AssignmentEvent.NewAssignmentAdd(BugAddWindow.this,
                                    ProjectTypeConstants.BUG, bugId));
                            ProjectSubscribersComp subcribersComp = bugEditFormFieldFactory.getSubscribersComp();
                            List<String> followers = subcribersComp.getFollowers();
                            if (followers.size() > 0) {
                                List<MonitorItem> monitorItems = new ArrayList<>();
                                for (String follower : followers) {
                                    MonitorItem monitorItem = new MonitorItem();
                                    monitorItem.setMonitorDate(new GregorianCalendar().getTime());
                                    monitorItem.setSaccountid(AppContext.getAccountId());
                                    monitorItem.setType(ProjectTypeConstants.BUG);
                                    monitorItem.setTypeid(bugId);
                                    monitorItem.setUser(follower);
                                    monitorItem.setExtratypeid(bean.getProjectid());
                                    monitorItems.add(monitorItem);
                                }
                                MonitorItemService monitorItemService = ApplicationContextUtil.getSpringBean(MonitorItemService.class);
                                monitorItemService.saveMonitorItems(monitorItems);
                            }
                            close();
                        }
                    }
                });
                saveBtn.setIcon(FontAwesome.SAVE);
                saveBtn.setStyleName(UIConstants.BUTTON_ACTION);

                Button cancelBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL), new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent clickEvent) {
                        BugAddWindow.this.close();
                    }
                });
                cancelBtn.setStyleName(UIConstants.BUTTON_OPTION);
                buttonControls.with(updateAllBtn, cancelBtn, saveBtn);

                layout.addComponent(buttonControls);
                layout.setComponentAlignment(buttonControls, Alignment.MIDDLE_RIGHT);
                layout.addStyleName(UIConstants.SCROLLABLE_CONTAINER);
                new Restrain(layout).setMaxHeight("600px");
                return layout;
            }

            @Override
            public void attachField(Object propertyId, Field<?> field) {
                if (BugWithBLOBs.Field.summary.equalTo(propertyId)) {
                    informationLayout.addComponent(field, AppContext.getMessage(BugI18nEnum.FORM_SUMMARY), 0, 0, 2, "100%");
                } else if (BugWithBLOBs.Field.priority.equalTo(propertyId)) {
                    informationLayout.addComponent(field, AppContext.getMessage(BugI18nEnum.FORM_PRIORITY),
                            AppContext.getMessage(BugI18nEnum.FORM_PRIORITY_HELP), 0, 1);
                } else if (BugWithBLOBs.Field.assignuser.equalTo(propertyId)) {
                    informationLayout.addComponent(field, AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE), 1, 1);
                } else if (BugWithBLOBs.Field.severity.equalTo(propertyId)) {
                    informationLayout.addComponent(field, AppContext.getMessage(BugI18nEnum.FORM_SEVERITY), 0, 2);
                } else if (SimpleBug.Field.components.equalTo(propertyId)) {
                    informationLayout.addComponent(field, AppContext.getMessage(BugI18nEnum.FORM_COMPONENTS),
                            AppContext.getMessage(BugI18nEnum.FORM_COMPONENTS_HELP), 1, 2);
                } else if (BugWithBLOBs.Field.startdate.equalTo(propertyId)) {
                    informationLayout.addComponent(field, AppContext.getMessage(GenericI18Enum.FORM_START_DATE), 0, 3);
                } else if (SimpleBug.Field.affectedVersions.equalTo(propertyId)) {
                    informationLayout.addComponent(field, AppContext.getMessage(BugI18nEnum.FORM_AFFECTED_VERSIONS),
                            AppContext.getMessage(BugI18nEnum.FORM_AFFECTED_VERSIONS_HELP), 1, 3);
                } else if (BugWithBLOBs.Field.duedate.equalTo(propertyId)) {
                    informationLayout.addComponent(field, AppContext.getMessage(GenericI18Enum.FORM_DUE_DATE), 0, 4);
                } else if (BugWithBLOBs.Field.milestoneid.equalTo(propertyId)) {
                    informationLayout.addComponent(field, AppContext.getMessage(BugI18nEnum.FORM_PHASE), 1, 4);
                } else if (BugWithBLOBs.Field.summary.equalTo(propertyId)) {
                    informationLayout.addComponent(field, AppContext.getMessage(BugI18nEnum.FORM_SUMMARY), 0, 5, 2, "100%");
                } else if (BugWithBLOBs.Field.description.equalTo(propertyId)) {
                    informationLayout.addComponent(field, AppContext.getMessage(GenericI18Enum.FORM_DESCRIPTION), 0, 6, 2, "100%");
                } else if (BugWithBLOBs.Field.id.equalTo(propertyId)) {
                    informationLayout.addComponent(field, AppContext.getMessage(GenericI18Enum.FORM_ATTACHMENTS), 0, 7, 2, "100%");
                } else if (SimpleBug.Field.selected.equalTo(propertyId)) {
                    informationLayout.addComponent(field, "Notifiers", 0, 8, 2, "100%");
                }
            }
        }
    }
}
