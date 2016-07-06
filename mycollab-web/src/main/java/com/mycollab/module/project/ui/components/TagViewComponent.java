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
package com.mycollab.module.project.ui.components;

import com.mycollab.common.domain.Tag;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.TagI18nEnum;
import com.mycollab.common.service.TagService;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.events.ProjectEvent;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.web.ui.ConfirmDialogExt;
import com.mycollab.vaadin.web.ui.UIConstants;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import org.vaadin.dialogs.ConfirmDialog;
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
        final MButton addTagBtn = new MButton(AppContext.getMessage(TagI18nEnum.ACTION_ADD))
                .withIcon(FontAwesome.PLUS_CIRCLE).withStyleName(UIConstants.BUTTON_LINK);
        addTagBtn.addClickListener(clickEvent -> {
            TagViewComponent.this.removeComponent(addTagBtn);
            TagViewComponent.this.addComponent(createSaveTagComp());
        });
        return addTagBtn;
    }

    private HorizontalLayout createSaveTagComp() {
        final MHorizontalLayout layout = new MHorizontalLayout();
        final SuggestField field = new SuggestField();
        field.setInputPrompt(AppContext.getMessage(TagI18nEnum.OPT_ENTER_TAG_NAME));
        field.setMinimumQueryCharacters(2);
        field.setSuggestionConverter(new TagSuggestionConverter());
        field.setSuggestionHandler(query -> {
            tagQuery = query;
            return handleSearchQuery(query);
        });

        MButton addBtn = new MButton(AppContext.getMessage(GenericI18Enum.BUTTON_ADD), clickEvent -> {
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
                NotificationUtil.showWarningNotification(AppContext.getMessage(TagI18nEnum.ERROR_TAG_NAME_HAS_MORE_2_CHARACTERS));
            }
            tagQuery = "";
        }).withStyleName(UIConstants.BUTTON_ACTION);
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
        TagBlock(final Tag tag) {
            this.setStyleName("tag-block");
            MButton tagLink = new MButton(tag.getName(),
                    clickEvent -> EventBusFactory.getInstance().post(new ProjectEvent.GotoTagListView(this, tag)))
                    .withStyleName(UIConstants.BUTTON_LINK);
            this.addComponent(tagLink);
            MButton deleteBtn = new MButton(FontAwesome.TIMES, clickEvent -> {
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
            }).withDescription(AppContext.getMessage(TagI18nEnum.ACTION_DELETE)).withStyleName("remove-btn-sup");
            this.addComponent(deleteBtn);
        }
    }
}