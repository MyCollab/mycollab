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
package com.mycollab.vaadin.web.ui;

import com.hp.gagawa.java.elements.Img;
import com.mycollab.common.domain.MailRecipientField;
import com.mycollab.configuration.StorageFactory;
import com.mycollab.core.MyCollabException;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.module.user.domain.SimpleUser;
import com.mycollab.module.user.domain.criteria.UserSearchCriteria;
import com.mycollab.module.user.service.UserService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import org.vaadin.jouni.restrain.Restrain;
import org.vaadin.suggestfield.BeanSuggestionConverter;
import org.vaadin.suggestfield.SuggestField;
import org.vaadin.suggestfield.client.SuggestFieldSuggestion;
import org.vaadin.viritin.button.MButton;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class EmailTokenField extends CssLayout implements SuggestField.NewItemsHandler, SuggestField.SuggestionHandler,
        SuggestField.TokenHandler {
    private static final long serialVersionUID = 1L;

    private Set<String> inviteEmails;
    private SuggestField suggestField;
    private List<SimpleUser> candidateUsers;
    private String lastQuery = "";
    private boolean isFocusing = false;

    EmailTokenField() {
        super();
        inviteEmails = new HashSet<>();
        this.setWidth("100%");
        this.addStyleName("member-token");
        new Restrain(this).setMinHeight("50px");
        suggestField = new SuggestField();
        suggestField.setHeight("25px");
        suggestField.setWidth("300px");
        suggestField.setNewItemsAllowed(true);
        suggestField.setNewItemsHandler(this);
        suggestField.focus();
        suggestField.setImmediate(true);
        suggestField.setTokenMode(true);
        suggestField.setSuggestionHandler(this);
        suggestField.setSuggestionConverter(new UserSuggestionConverter());
        suggestField.setTokenHandler(this);
        suggestField.setMinimumQueryCharacters(1);
        suggestField.setPopupWidth(400);

        addComponent(suggestField);
        UserService userService = AppContextUtil.getSpringBean(UserService.class);
        UserSearchCriteria searchCriteria = new UserSearchCriteria();
        searchCriteria.setSaccountid(NumberSearchField.equal(MyCollabUI.getAccountId()));
        candidateUsers = userService.findAbsoluteListByCriteria(searchCriteria, 0, Integer.MAX_VALUE);
        suggestField.addBlurListener(blurEvent -> {
                    isFocusing = false;
                    if (!"".equals(lastQuery) && StringUtils.isValidEmail(lastQuery) && !inviteEmails.contains(lastQuery)) {
                        handleToken(lastQuery);
                    }
                }
        );

        suggestField.addFocusListener(focusEvent -> {
                    isFocusing = true;
                    lastQuery = "";
                }
        );
        this.addLayoutClickListener(layoutClickEvent -> {
            if (layoutClickEvent.getClickedComponent() == null) {
                suggestField.focus();
            }
        });
    }

    @Override
    public Object addNewItem(String value) {
        lastQuery = "";
        if (StringUtils.isValidEmail(value) && !inviteEmails.contains(value)) {
            return value;
        } else {
            NotificationUtil.showWarningNotification(value + " is not a valid email or it is already in the list");
        }
        return null;
    }

    public List<MailRecipientField> getListRecipients() {
        if (!"".equals(lastQuery) && StringUtils.isValidEmail(lastQuery) && !inviteEmails.contains(lastQuery)) {
            inviteEmails.add(lastQuery);
        }
        return inviteEmails.stream().map(mail -> new MailRecipientField(mail, mail))
                .collect(Collectors.toList());
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
        return new ArrayList<>(result);
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
                    NotificationUtil.showWarningNotification("Email " + user.getEmail() + " is already in the list");
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
        MButton btn = new MButton(email).withIcon(FontAwesome.TIMES).withStyleName("token-field");
        btn.addClickListener(clickEvent -> {
            EmailTokenField.this.removeComponent(btn);
            inviteEmails.remove(email);
        });
        return btn;
    }

    private Component generateToken(final SimpleUser user) {
        final Button btn = new Button("", FontAwesome.TIMES);
        btn.setCaptionAsHtml(true);
        btn.setCaption((new Img("", StorageFactory.getAvatarPath(user.getAvatarid(), 16))).write() + " " + user.getDisplayName());
        btn.addClickListener(clickEvent -> {
            EmailTokenField.this.removeComponent(btn);
            inviteEmails.remove(user.getEmail());
        });
        btn.setStyleName("token-field");
        return btn;
    }

    private class UserSuggestionConverter extends BeanSuggestionConverter {
        private UserSuggestionConverter() {
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