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

import com.esofthead.mycollab.configuration.EmailConfiguration;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.module.mail.DefaultMailer;
import com.esofthead.mycollab.module.mail.IMailer;
import com.esofthead.mycollab.module.mail.NullMailer;
import com.esofthead.mycollab.module.mail.service.ExtMailService;
import org.springframework.stereotype.Service;

/**
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
public class ExtMailServiceImpl extends AbstractMailService implements
        ExtMailService {

    @Override
    public boolean isMailSetupValid() {
        EmailConfiguration emailConfiguration = SiteConfiguration
                .getRelayEmailConfiguration();
        return !(emailConfiguration.getHost().equals(""));
    }

    @Override
    protected IMailer getMailer() {
        EmailConfiguration emailConfiguration = SiteConfiguration
                .getRelayEmailConfiguration();
        if (emailConfiguration.getHost().equals("")) {
            return new NullMailer();
        }

        return new DefaultMailer(emailConfiguration);

    }

}
