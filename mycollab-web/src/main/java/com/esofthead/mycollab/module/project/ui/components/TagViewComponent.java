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
package com.esofthead.mycollab.module.project.ui.components;

import com.esofthead.mycollab.common.domain.Tag;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.service.TagService;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.events.ProjectEvent;
import com.esofthead.mycollab.spring.AppContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.esofthead.mycollab.vaadin.web.ui.ConfirmDialogExt;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.suggestfield.SuggestField;
import org.vaadin.suggestfield.client.SuggestFieldSuggestion;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 5.0.1
 */
public class TagViewComponent extends CssLayout {
    private String type;
    private int typeId;

    private TagService tagService;
    private String tagQuery = "";

    public TagViewComponent() {
        tagService = AppContextUtil.getSpringBean(TagService.class);
        this.setStyleName("project-tag-comp");
    }

    public void display(String type, int typeId) {
        this.removeAllComponents();
        this.type = type;
        this.typeId = typeId;

        List<Tag> tags = tagService.findTags(type, typeId + "", AppContext.getAccountId());
        for (Tag tag : tags) {
            this.addComponent(new TagBlock(tag));
        }

        this.addComponent(createAddTagBtn());
    }

    private Button createAddTagBtn() {
        final Button addTagBtn = new Button("Add tag", FontAwesome.PLUS_CIRCLE);
        addTagBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                TagViewComponent.this.removeComponent(addTagBtn);
                TagViewComponent.this.addComponent(createSaveTagComp());
            }
        });
        addTagBtn.setStyleName(UIConstants.BUTTON_LINK);
        return addTagBtn;
    }

    private HorizontalLayout createSaveTagComp() {
        final MHorizontalLayout layout = new MHorizontalLayout();
        final SuggestField field = new SuggestField();
        field.setInputPrompt("Enter tag name");
        field.setMinimumQueryCharacters(2);
        field.setSuggestionConverter(new TagSuggestionConverter());
        field.setSuggestionHandler(new SuggestField.SuggestionHandler() {
            @Override
            public List<Object> searchItems(String query) {
                tagQuery = query;
                return handleSearchQuery(query);
            }
        });

        Button addBtn = new Button("Add", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                String tagName = (field.getValue() == null) ? tagQuery : field.getValue().toString().trim();
                if (!tagName.equals("")) {
                    Tag tag = new Tag();
                    tag.setName(tagName);
                    tag.setType(type);
                    tag.setTypeid(typeId + "");
                    tag.setSaccountid(AppContext.getAccountId());
                    tag.setExtratypeid(CurrentProjectVariables.getProjectId());
                    int result = tagService.saveWithSession(tag, AppContext.getUsername());
                    if (result > 0) {
                        TagViewComponent.this.removeComponent(layout);
                        TagViewComponent.this.addComponent(new TagBlock(tag));
                        TagViewComponent.this.addComponent(createAddTagBtn());
                    } else {
                        TagViewComponent.this.removeComponent(layout);
                        TagViewComponent.this.addComponent(createAddTagBtn());
                    }
                } else {
                    NotificationUtil.showWarningNotification("The tag value must have more than 2 characters");
                }
                tagQuery = "";
            }
        });
        addBtn.setStyleName(UIConstants.BUTTON_ACTION);
        layout.with(field, addBtn);
        return layout;
    }

    private List<Object> handleSearchQuery(String query) {
        if ("".equals(query) || query == null) {
            return Collections.emptyList();
        }
        List<Tag> suggestedTags = tagService.findTagsInAccount(query, new String[]{ProjectTypeConstants.BUG,
                        ProjectTypeConstants.TASK, ProjectTypeConstants.MILESTONE, ProjectTypeConstants.RISK},
                AppContext.getAccountId());
        return new ArrayList<Object>(suggestedTags);
    }

    private static class TagSuggestionConverter implements SuggestField.SuggestionConverter {
        @Override
        public SuggestFieldSuggestion toSuggestion(Object item) {
            assert (item != null) : "Item cannot be null";
            String value;
            if (item instanceof Tag) {
                value = ((Tag) item).getName();
            } else {
                value = item.toString();
            }
            return new SuggestFieldSuggestion(value, value, value);
        }

        @Override
        public Object toItem(SuggestFieldSuggestion suggestion) {
            assert (suggestion != null) : "Suggestion cannot be null";
            return suggestion.getId();
        }
    }

    private class TagBlock extends CssLayout {
        TagBlock(final Tag tag) {
            this.setStyleName("tag-block");
            Button tagLink = new Button(tag.getName(), new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    EventBusFactory.getInstance().post(new ProjectEvent.GotoTagListView(TagBlock.this, tag));
                }
            });
            tagLink.setStyleName(UIConstants.BUTTON_LINK);
            this.addComponent(tagLink);
            Button deleteBtn = new Button(FontAwesome.TIMES);
            deleteBtn.setDescription("Delete tag");
            deleteBtn.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    ConfirmDialogExt.show(UI.getCurrent(),
                            AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, AppContext.getSiteName()),
                            AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                            AppContext.getMessage(GenericI18Enum.BUTTON_YES),
                            AppContext.getMessage(GenericI18Enum.BUTTON_NO),
                            new ConfirmDialog.Listener() {
                                private static final long serialVersionUID = 1L;

                                @Override
                                public void onClose(ConfirmDialog dialog) {
                                    if (dialog.isConfirmed()) {
                                        tagService.removeWithSession(tag, AppContext.getUsername(), AppContext.getAccountId());
                                        TagViewComponent.this.removeComponent(TagBlock.this);
                                    }
                                }
                            });
                }
            });
            deleteBtn.setStyleName("remove-btn-sup");
            this.addComponent(deleteBtn);
        }
    }
}