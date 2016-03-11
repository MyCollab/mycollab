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
package com.esofthead.mycollab.spring;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;

/**
 * @author MyCollab Ltd
 * @since 5.1.0
 */
@Configuration
public class AppEventBus {
    private static Logger LOG = LoggerFactory.getLogger(AppEventBus.class);

    @Bean
    public AsyncEventBus asyncEventBus() {
        AsyncEventBus asyncEventBus = new AsyncEventBus(Executors.newCachedThreadPool(), new SubscriberExceptionHandler() {
            @Override
            public void handleException(Throwable throwable, SubscriberExceptionContext subscriberExceptionContext) {
                LOG.error("Error in event bus execution", throwable);
            }
        });
        return asyncEventBus;
    }
}
