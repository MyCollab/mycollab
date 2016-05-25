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
package com.esofthead.mycollab.module.project.view.settings.component;

import com.esofthead.mycollab.configuration.StorageFactory;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.spring.AppContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.hp.gagawa.java.elements.Img;
import com.vaadin.event.FieldEvents;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import org.vaadin.suggestfield.BeanSuggestionConverter;
import org.vaadin.suggestfield.SuggestField;
import org.vaadin.suggestfield.client.SuggestFieldSuggestion;

import java.util.*;

/**
 * @author MyCollab Ltd
 * @since 5.2.1
 */
public class InviteUserTokenField extends CssLayout implements SuggestField.NewItemsHandler, SuggestField.SuggestionHandler,
        SuggestField.TokenHandler {
    private static final long serialVersionUID = 1L;

    private Set<String> inviteEmails;
    private SuggestField suggestField;
    private List<SimpleUser> candidateUsers;
    private String lastQuery = "";
    private boolean isFocusing = false;

    public InviteUserTokenField() {
        super();
        inviteEmails = new HashSet<>();
        this.setWidth("100%");
        this.addStyleName("member-token");
        suggestField = new SuggestField();
        suggestField.setHeight("25px");
        suggestField.setWidth("300px");
        suggestField.setNewItemsAllowed(true);
        suggestField.setNewItemsHandler(this);
        suggestField.setImmediate(true);
        suggestField.setTokenMode(true);
        suggestField.setSuggestionHandler(this);
        suggestField.setSuggestionConverter(new UserSuggestionConverter());
        suggestField.setTokenHandler(this);
        suggestField.setMinimumQueryCharacters(1);
        suggestField.setPopupWidth(400);

        addComponent(suggestField);
        ProjectMemberService prjMemberService = AppContextUtil.getSpringBean(ProjectMemberService.class);
        candidateUsers = prjMemberService.getUsersNotInProject(CurrentProjectVariables.getProjectId(), AppContext.getAccountId());
        suggestField.addBlurListener(new FieldEvents.BlurListener() {
                                         @Override
                                         public void blur(FieldEvents.BlurEvent blurEvent) {
                                             isFocusing = false;
                                             if (!"".equals(lastQuery) && StringUtils.isValidEmail(lastQuery) && !inviteEmails.contains(lastQuery)) {
                                                 handleToken(lastQuery);
                                             }
                                         }
                                     }
        );

        suggestField.addFocusListener(new FieldEvents.FocusListener() {
                                          @Override
                                          public void focus(FieldEvents.FocusEvent focusEvent) {
                                              isFocusing = true;
                                              lastQuery = "";
                                          }
                                      }
        );
    }

    @Override
    public Object addNewItem(String value) {
        lastQuery = "";
        if (StringUtils.isValidEmail(value) && !inviteEmails.contains(value)) {
            return value;
        } else {
            NotificationUtil.showNotification("Info", value + " is not a valid email or it is already in the list");
        }
        return null;
    }

    public Collection<String> getInviteEmails() {
        if (!"".equals(lastQuery) && StringUtils.isValidEmail(lastQuery) && !inviteEmails.contains(lastQuery)) {
            inviteEmails.add(lastQuery);
        }
        return inviteEmails;
    }

    @Override
    public List<Object> searchItems(String query) {
        if (StringUtils.isBlank(query) || !isFocusing) {
            return Collections.emptyList();
        }
        lastQuery = query;
        List<SimpleUser> result = new ArrayList<>();
        for (SimpleUser user : candidateUsers) {
            if (user.getEmail().contains(query) || user.getDisplayName().contains(query)) {
                result.add(user);
            }
        }
        return new ArrayList<Object>(result);
    }

    @Override
    public void handleToken(Object token) {
        if (token != null) {
            if (token instanceof String) {
                String address = (String) token;
                if (!inviteEmails.contains(address)) {
                    addToken(generateToken(address));
                    inviteEmails.add(address);
                }
            } else if (token instanceof SimpleUser) {
                SimpleUser user = (SimpleUser) token;
                if (!inviteEmails.contains(user.getEmail())) {
                    addToken(generateToken(user));
                    inviteEmails.add(user.getEmail());
                } else {
                    NotificationUtil.showNotification("Info", "Email " + user.getEmail() + " is already in the list");
                }
            } else {
                throw new MyCollabException("Do not support token type " + token);
            }
            lastQuery = "";
        }
    }

    private void addToken(Component button) {
        int index = getComponentIndex(suggestField);
        addComponent(button, index);
    }

    private Component generateToken(final String email) {
        final Button btn = new Button(email, FontAwesome.TIMES);
        btn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                InviteUserTokenField.this.removeComponent(btn);
                inviteEmails.remove(email);
            }
        });
        btn.addStyleName("token-field");
        return btn;
    }

    private Component generateToken(final SimpleUser user) {
        final Button btn = new Button("", FontAwesome.TIMES);
        btn.setCaptionAsHtml(true);
        btn.setCaption((new Img("", StorageFactory.getInstance().getAvatarPath(user.getAvatarid(), 16))).write() + " " + user.getDisplayName());
        btn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                InviteUserTokenField.this.removeComponent(btn);
                inviteEmails.remove(user.getEmail());
            }
        });
        btn.setStyleName("token-field");
        return btn;
    }

    private class UserSuggestionConverter extends BeanSuggestionConverter {
        public UserSuggestionConverter() {
            super(SimpleUser.class, "email", "displayName", "email");
        }

        @Override
        public Object toItem(SuggestFieldSuggestion suggestion) {
            SimpleUser result = null;
            for (SimpleUser bean : candidateUsers) {
                if (bean.getEmail().equals(suggestion.getId())) {
                    result = bean;
                    break;
                }
            }
            assert result != null : "This should not be happening";
            return result;
        }
    }
}
