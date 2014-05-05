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
package com.esofthead.mycollab.vaadin.ui;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.tokenfield.TokenField;

import com.esofthead.mycollab.common.domain.MailRecipientField;
import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.core.utils.EmailValidator;
import com.esofthead.mycollab.utils.ParsingUtils;
import com.esofthead.mycollab.utils.ParsingUtils.InvalidEmailException;
import com.esofthead.mycollab.vaadin.AppContext;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@SuppressWarnings("serial")
public class EmailTokenField extends TokenField {

	private final EmailValidator emailValidate = new EmailValidator();

	private final List<MailRecipientField> lstMailToken = new ArrayList<MailRecipientField>();

	public EmailTokenField() {
		super();

		this.setStyleName(TokenField.STYLE_TOKENFIELD);
		this.setInputWidth("544px");
		this.setRememberNewTokens(false);
	}

	@Override
	protected void onTokenClick(Object tokenId) {
		super.onTokenClick(tokenId);
		int index = -1;
		try {
			index = getItemIndexInListToEmail(ParsingUtils
					.getMailRecipient(tokenId.toString()));
		} catch (InvalidEmailException e) {
			e.printStackTrace();
		}
		if (index > -1) {
			lstMailToken.remove(index);
		}
	}

	@Override
	public void addToken(Object tokenId) {
		super.addToken(tokenId);
		try {
			lstMailToken.add(ParsingUtils.getMailRecipient(tokenId.toString()));
		} catch (InvalidEmailException e) {
			e.printStackTrace();
		}
	}

	private int getItemIndexInListToEmail(MailRecipientField item) {
		for (int i = 0; i < lstMailToken.size(); i++) {
			MailRecipientField recipient = lstMailToken.get(i);
			if (item.getEmail().equals(recipient.getEmail())
					&& item.getName().equals(recipient.getName())) {
				return i;
			}
		}
		return -1;
	}

	@Override
	protected void onTokenInput(Object tokenId) {
		String[] tokens = ((String) tokenId).split(",");
		for (int i = 0; i < tokens.length; i++) {
			String token = tokens[i].trim();
			if (token.length() > 0) {
				if (emailValidate.validate(token)) {
					super.onTokenInput(token);
				} else {
					NotificationUtil
							.showWarningNotification(AppContext
									.getMessage(GenericI18Enum.WARNING_NOT_VALID_EMAIL));
				}
			}
		}
	}

	public void setInputStyle(String styleName) {
		cb.setStyleName(styleName);
	}

	public void removeAllRecipients() {

		for (MailRecipientField recipient : lstMailToken) {
			removeToken(recipient.getEmail());
		}

		lstMailToken.removeAll(lstMailToken);
	}

	public List<MailRecipientField> getListRecipient() {
		return lstMailToken;
	}

}
