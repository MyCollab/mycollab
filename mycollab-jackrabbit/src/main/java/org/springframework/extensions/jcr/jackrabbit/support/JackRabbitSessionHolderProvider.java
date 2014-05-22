/**
 * Copyright 2009-2012 the original author or authors
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.springframework.extensions.jcr.jackrabbit.support;

import javax.jcr.Session;

import org.springframework.extensions.jcr.SessionHolder;
import org.springframework.extensions.jcr.SessionHolderProvider;

/**
 * JackRabbit specific session holder. This holder should be used with
 * OpenSessionInViewFilter/Interceptor or JcrInterceptor with JackRabbit if
 * transactional support is required. The default session holder however it is
 * approapritate but transactions will not be supported.
 * 
 * @author Costin Leau
 * @author Sergio Bossa 
 * @author Salvatore Incandela
 * 
 */
public class JackRabbitSessionHolderProvider implements SessionHolderProvider {

	/**
	 * @see org.springframework.extensions.jcr.SessionHolderProvider#acceptsRepository(java.lang.String)
	 */
	public boolean acceptsRepository(String repositoryName) {
		return "Jackrabbit".equals(repositoryName);
	}

	/**
	 * @see org.springframework.extensions.jcr.SessionHolderProvider#createSessionHolder(javax.jcr.Session)
	 */
	public SessionHolder createSessionHolder(Session session) {
		return new UserTxSessionHolder(session);
	}

}
