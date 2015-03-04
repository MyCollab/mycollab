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
package com.esofthead.mycollab.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.esofthead.mycollab.common.domain.MailRecipientField;

/**
 * @author MyCollab Ltd.
 * @since 1.0.0
 */
public class ParsingUtils {
	public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	public static List<MailRecipientField> parseEmailField(String emailField)
			throws InvalidEmailException {

		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcherEmail;
		if (emailField == null || emailField.trim().equals("")) {
			throw new InvalidEmailException("The email is not valid!");
		} else {
			List<MailRecipientField> fields = new ArrayList<>();
			StringTokenizer emailTokenizer = new StringTokenizer(emailField, ",");
			while (emailTokenizer.hasMoreElements()) {
				String email = emailTokenizer.nextToken().trim();
				MailRecipientField mailField = null;
				if ((email.indexOf("<") > -1)
						&& (email.indexOf(">") == (email.length() - 1))
						&& (email.indexOf(">") > email.indexOf("<"))) {
					String name = email.substring(0, email.indexOf("<"));
					String mail = email.substring(email.indexOf("<") + 1,
							email.indexOf(">"));
					matcherEmail = pattern.matcher(mail);
					if (matcherEmail.matches() && !name.contains("@")) {
						mailField = new MailRecipientField(mail, name);
					}
				} else {
					matcherEmail = pattern.matcher(email);
					if (matcherEmail.matches()) {
						mailField = new MailRecipientField(email, email);
					}
				}
				if (mailField != null) {
					fields.add(mailField);
				} else {
					throw new InvalidEmailException("The email is not valid!");
				}
			}
			return fields;
		}
	}

	public static MailRecipientField getMailRecipient(String emailField)
			throws InvalidEmailException {
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcherEmail;
		if (emailField == null || emailField.trim().equals("")) {
			throw new InvalidEmailException("The email is not valid!");
		} else {
			MailRecipientField mailField = null;
			if ((emailField.indexOf("<") > -1)
					&& (emailField.indexOf(">") == (emailField.length() - 1))
					&& (emailField.indexOf(">") > emailField.indexOf("<"))) {
				String name = emailField.substring(0, emailField.indexOf("<"));
				String mail = emailField.substring(emailField.indexOf("<") + 1,
						emailField.indexOf(">"));
				matcherEmail = pattern.matcher(mail);
				if (matcherEmail.matches() && !name.contains("@")) {
					mailField = new MailRecipientField(mail, name);
				}
			} else {
				matcherEmail = pattern.matcher(emailField);
				if (matcherEmail.matches()) {
					mailField = new MailRecipientField(emailField);
				}
			}
			if (mailField != null) {
				return mailField;
			} else {
				throw new InvalidEmailException("The email is not valid!");
			}
		}
	}

	public static class InvalidEmailException extends Exception {
		private static final long serialVersionUID = 1L;

		public InvalidEmailException(String msg) {
			super(msg);
		}
	}
}
