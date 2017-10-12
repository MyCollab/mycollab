/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.ui.components;

import com.mycollab.common.domain.Tag;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.TagI18nEnum;
import com.mycollab.common.service.TagService;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.event.ProjectEvent;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.web.ui.ConfirmDialogExt;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import org.vaadin.suggestfield.SuggestField;
import org.vaadin.suggestfield.client.SuggestFieldSuggestion;
import org.vaadin.viritin.button.MButton;
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
    private boolean canAddNewTag;

    private TagService tagService;
    private String tagQuery = "";

    public TagViewComponent(boolean canAddNewTag) {
        tagService = AppContextUtil.getSpringBean(TagService.class);
        this.setStyleName("project-tag-comp");
        this.canAddNewTag = canAddNewTag;
    }

    public void display(String type, int typeId) {
        this.removeAllComponents();
        this.type = type;
        this.typeId = typeId;

        List<Tag> tags = tagService.findTags(type, typeId + "", AppUI.getAccountId());
        if (tags != null) {
            tags.stream().map(TagBlock::new).forEach(this::addComponent);
        }

        if (canAddNewTag) {
            this.addComponent(createAddTagBtn());
        }
    }

    private Button createAddTagBtn() {
        final MButton addTagBtn = new MButton(UserUIContext.getMessage(TagI18nEnum.ACTION_ADD))
                .withIcon(FontAwesome.PLUS_CIRCLE).withStyleName(WebThemes.BUTTON_LINK);
        addTagBtn.addClickListener(clickEvent -> {
            removeComponent(addTagBtn);
            addComponent(createSaveTagComp());
        });
        return addTagBtn;
    }

    private HorizontalLayout createSaveTagComp() {
        final MHorizontalLayout layout = new MHorizontalLayout();
        final SuggestField field = new SuggestField();
        field.setInputPrompt(UserUIContext.getMessage(TagI18nEnum.OPT_ENTER_TAG_NAME));
        field.setMinimumQueryCharacters(2);
        field.setSuggestionConverter(new TagSuggestionConverter());
        field.setSuggestionHandler(query -> {
            tagQuery = query;
            return handleSearchQuery(query);
        });

        MButton addBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_ADD), clickEvent -> {
            String tagName = (field.getValue() == null) ? tagQuery : field.getValue().toString().trim();
            if (!tagName.equals("")) {
                Tag tag = new Tag();
                tag.setName(tagName);
                tag.setType(type);
                tag.setTypeid(typeId + "");
                tag.setSaccountid(AppUI.getAccountId());
                tag.setExtratypeid(CurrentProjectVariables.getProjectId());
                int result = tagService.saveWithSession(tag, UserUIContext.getUsername());
                if (result > 0) {
                    this.removeComponent(layout);
                    addComponent(new TagBlock(tag));
                    addComponent(createAddTagBtn());
                } else {
                    removeComponent(layout);
                    addComponent(createAddTagBtn());
                }
            } else {
                NotificationUtil.showWarningNotification(UserUIContext.getMessage(TagI18nEnum.ERROR_TAG_NAME_HAS_MORE_2_CHARACTERS));
            }
            tagQuery = "";
        }).withStyleName(WebThemes.BUTTON_ACTION);
        layout.with(field, addBtn);
        return layout;
    }

    private List<Object> handleSearchQuery(String query) {
        if ("".equals(query) || query == null) {
            return Collections.emptyList();
        }
        List<Tag> suggestedTags = tagService.findTagsInAccount(query, new String[]{ProjectTypeConstants.BUG,
                        ProjectTypeConstants.TASK, ProjectTypeConstants.MILESTONE, ProjectTypeConstants.RISK},
                AppUI.getAccountId());
        return new ArrayList<>(suggestedTags);
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
        TagBlock(Tag tag) {
            this.setStyleName("tag-block");
            MButton tagLink = new MButton(tag.getName(),
                    clickEvent -> EventBusFactory.getInstance().post(new ProjectEvent.GotoTagListView(this, tag)))
                    .withStyleName(WebThemes.BUTTON_LINK);
            this.addComponent(tagLink);
            if (canAddNewTag) {
                MButton deleteBtn = new MButton(FontAwesome.TIMES, clickEvent -> {
                    ConfirmDialogExt.show(UI.getCurrent(),
                            UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, AppUI.getSiteName()),
                            UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                            UserUIContext.getMessage(GenericI18Enum.BUTTON_YES),
                            UserUIContext.getMessage(GenericI18Enum.BUTTON_NO),
                            confirmDialog -> {
                                if (confirmDialog.isConfirmed()) {
                                    tagService.removeWithSession(tag, UserUIContext.getUsername(), AppUI.getAccountId());
                                    TagViewComponent.this.removeComponent(TagBlock.this);
                                }
                            });
                }).withDescription(UserUIContext.getMessage(TagI18nEnum.ACTION_DELETE)).withStyleName("remove-btn-sup");
                this.addComponent(deleteBtn);
            }
        }
    }
}