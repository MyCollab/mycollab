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
package com.mycollab.module.project.view.page;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.page.domain.Folder;
import com.mycollab.module.page.service.PageService;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.event.PageEvent;
import com.mycollab.module.project.i18n.PageI18nEnum;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.mycollab.vaadin.ui.AbstractFormLayoutFactory;
import com.mycollab.vaadin.ui.AdvancedEditBeanForm;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

import java.util.GregorianCalendar;

/**
 * @author MyCollab Ltd.
 * @since 5.0.2
 */
class GroupPageAddWindow extends MWindow {
    private static final long serialVersionUID = 1L;

    private Folder folder;

    GroupPageAddWindow(Folder editFolder) {
        super();
        MVerticalLayout content = new MVerticalLayout().withMargin(new MarginInfo(false, false, true, false));
        this.withModal(true).withResizable(false).withWidth("700px").withCenter().withContent(content);
        EditForm editForm = new EditForm();

        if (editFolder == null) {
            folder = new Folder();
            this.setCaption(UserUIContext.getMessage(PageI18nEnum.NEW_GROUP));
            String pagePath = CurrentProjectVariables.getCurrentPagePath();
            folder.setPath(pagePath + "/" + StringUtils.generateSoftUniqueId());
        } else {
            folder = editFolder;
            this.setCaption(UserUIContext.getMessage(PageI18nEnum.DETAIL_GROUP));
        }

        editForm.setBean(folder);
        content.addComponent(editForm);
    }

    GroupPageAddWindow() {
        this(null);
    }

    private class EditForm extends AdvancedEditBeanForm<Folder> {
        private static final long serialVersionUID = -1898444508905690238L;

        @Override
        public void setBean(final Folder item) {
            this.setFormLayoutFactory(new FormLayoutFactory());
            this.setBeanFormFieldFactory(new EditFormFieldFactory(EditForm.this));
            super.setBean(item);
        }

        class FormLayoutFactory extends AbstractFormLayoutFactory {
            private static final long serialVersionUID = 1L;

            private GridFormLayoutHelper informationLayout;

            @Override
            public AbstractComponent getLayout() {
                final VerticalLayout layout = new VerticalLayout();
                informationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(2, 2);
                layout.addComponent(informationLayout.getLayout());

                final MButton cancelBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CANCEL), clickEvent -> close())
                        .withStyleName(WebThemes.BUTTON_OPTION);

                MButton saveBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SAVE), clickEvent -> {
                    if (EditForm.this.validateForm()) {
                        PageService pageService = AppContextUtil.getSpringBean(PageService.class);
                        pageService.createFolder(folder, UserUIContext.getUsername());
                        folder.setCreatedTime(new GregorianCalendar());
                        folder.setCreatedUser(UserUIContext.getUsername());
                        GroupPageAddWindow.this.close();
                        EventBusFactory.getInstance().post(new PageEvent.GotoList(GroupPageAddWindow.this, folder.getPath()));
                    }
                }).withIcon(FontAwesome.SAVE).withStyleName(WebThemes.BUTTON_ACTION);
                saveBtn.setClickShortcut(ShortcutAction.KeyCode.ENTER);

                final MHorizontalLayout controlsBtn = new MHorizontalLayout(cancelBtn, saveBtn).withMargin(new MarginInfo(true, true, true, false));
                layout.addComponent(controlsBtn);
                layout.setComponentAlignment(controlsBtn, Alignment.MIDDLE_RIGHT);
                return layout;
            }

            @Override
            protected Component onAttachField(Object propertyId, Field<?> field) {
                if (propertyId.equals("name")) {
                    return informationLayout.addComponent(field, UserUIContext.getMessage(PageI18nEnum.FORM_GROUP), 0, 0);
                } else if (propertyId.equals("description")) {
                    return informationLayout.addComponent(field, UserUIContext.getMessage(GenericI18Enum.FORM_DESCRIPTION), 0, 1, 2, "100%");
                }
                return null;
            }
        }
    }

    private static class EditFormFieldFactory extends AbstractBeanFieldGroupEditFieldFactory<Folder> {
        private static final long serialVersionUID = 1L;

        EditFormFieldFactory(GenericBeanForm<Folder> form) {
            super(form);
        }

        @Override
        protected Field<?> onCreateField(final Object propertyId) {
            if (propertyId.equals("description")) {
                return new RichTextArea();
            }

            return null;
        }
    }
}
