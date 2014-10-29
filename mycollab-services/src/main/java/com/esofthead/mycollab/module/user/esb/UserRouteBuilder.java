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
package com.esofthead.mycollab.module.user.esb;

import org.apache.camel.ExchangePattern;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Component
@Profile("!test")
public class UserRouteBuilder extends SpringRouteBuilder {

	@Autowired
	private UserRemovedCommand userRemovedCommand;

	@Override
	public void configure() throws Exception {
		from(UserEndpoints.USER_REMOVE_ENDPOINT).setExchangePattern(
				ExchangePattern.InOnly).to("seda:userDelete.queue");
		from("seda:userDelete.queue").threads().bean(userRemovedCommand,
				"userRemoved(String, int)");

	}

}
