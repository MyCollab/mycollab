/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.mail.service.impl;

import com.esofthead.mycollab.common.dao.RelayEmailMapper;
import com.esofthead.mycollab.common.domain.RelayEmailExample;
import com.esofthead.mycollab.common.domain.RelayEmailWithBLOBs;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.utils.JsonDeSerializer;
import com.esofthead.mycollab.module.mail.service.MailRelayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MailRelayServiceImpl implements MailRelayService {

	@Autowired
	private RelayEmailMapper relayEmailMapper;

	@Override
	public void saveRelayEmail(String[] toNames, String[] toEmails,
			String subject, String bodyContent) {
		RelayEmailWithBLOBs relayEmail = new RelayEmailWithBLOBs();
		relayEmail.setFromemail(SiteConfiguration.getNoReplyEmail());
		relayEmail.setFromname(SiteConfiguration.getDefaultSiteName());

		String recipientList = JsonDeSerializer.toJson(new String[][] {
				toEmails, toNames });
		relayEmail.setRecipients(recipientList);
		relayEmail.setSubject(subject);
		relayEmail.setBodycontent(bodyContent);
		relayEmail.setSaccountid(1);

		relayEmailMapper.insert(relayEmail);
	}

	@Override
	public List<RelayEmailWithBLOBs> getRelayEmails() {
		RelayEmailExample ex = new RelayEmailExample();
		return relayEmailMapper.selectByExampleWithBLOBs(ex);
	}

	@Override
	public void cleanEmails() {
		relayEmailMapper.deleteByExample(new RelayEmailExample());
	}

}
