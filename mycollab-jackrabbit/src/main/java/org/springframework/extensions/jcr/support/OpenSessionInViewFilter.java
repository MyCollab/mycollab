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
package org.springframework.extensions.jcr.support;

import java.io.IOException;

import javax.jcr.Session;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.extensions.jcr.SessionFactory;
import org.springframework.extensions.jcr.SessionFactoryUtils;

/**
 * Servlet 2.3 Filter that binds a JCR Session to the thread for the entire processing of the request.
 * Intended for the "Open Session in View" pattern, i.e. to allow for lazy loading in web views despite the
 * original transactions already being completed.
 * <p>
 * This filter works similar to the AOP JcrInterceptor: It just makes JCR Sessions available via the thread.
 * It is suitable for non-transactional execution but also for business layer transactions via
 * JcrTransactionManager or JtaTransactionManager. In the latter case, Sessions pre-bound by this filter will
 * automatically be used for the transactions.
 * <p>
 * Looks up the SessionFactory in Spring's root web application context. Supports a "SessionFactoryBeanName"
 * filter init-param in <code>web.xml</code> ; the default bean name is "SessionFactory". Looks up the
 * SessionFactory on each request, to avoid initialization order issues (when using ContextLoaderServlet, the
 * root application context will get initialized <i>after</i> this filter).
 * @author Costin Leau
 * @author Sergio Bossa
 * @author Salvatore Incandela
 */
public class OpenSessionInViewFilter extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(OpenSessionInViewFilter.class);

    public static final String DEFAULT_JCR_SESSION_FACTORY_FACTORY_BEAN_NAME = "sessionFactory";

    private String SessionFactoryBeanName = DEFAULT_JCR_SESSION_FACTORY_FACTORY_BEAN_NAME;

    /**
     * Set the bean name of the SessionFactory to fetch from Spring's root application context. Default is
     * "SessionFactory".
     * @param SessionFactoryBeanName
     * @see #DEFAULT_JCR_SESSION_FACTORY_FACTORY_BEAN_NAME
     */
    public void setSessionFactoryBeanName(String SessionFactoryBeanName) {
        this.SessionFactoryBeanName = SessionFactoryBeanName;
    }

    /**
     * Return the bean name of the SessionFactory to fetch from Spring's root application context.
     * @return
     */
    protected String getSessionFactoryBeanName() {
        return SessionFactoryBeanName;
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        SessionFactory sf = lookupSessionFactory(request);

        Session session = null;
        boolean participate = false;

        if (TransactionSynchronizationManager.hasResource(sf)) {
            // Do not modify the Session: just set the participate
            // flag.
            participate = true;
        } else {
            LOG.debug("Opening JCR session in OpenSessionInViewFilter");
            session = SessionFactoryUtils.getSession(sf, true);
            TransactionSynchronizationManager.bindResource(sf, sf.getSessionHolder(session));
        }

        try {
            filterChain.doFilter(request, response);
        }

        finally {
            if (!participate) {
                TransactionSynchronizationManager.unbindResource(sf);
                LOG.debug("Closing JCR session in OpenSessionInViewFilter");
                SessionFactoryUtils.releaseSession(session, sf);
            }
        }
    }

    /**
     * Look up the SessionFactory that this filter should use, taking the current HTTP request as argument.
     * <p>
     * Default implementation delegates to the <code>lookupSessionFactory</code> without arguments.
     * @param request
     * @return the SessionFactory to use
     * @see #lookupSessionFactory()
     */
    protected SessionFactory lookupSessionFactory(HttpServletRequest request) {
        return lookupSessionFactory();
    }

    /**
     * Look up the SessionFactory that this filter should use. The default implementation looks for a bean
     * with the specified name in Spring's root application context.
     * @return the SessionFactory to use
     * @see #getSessionFactoryBeanName
     */
    protected SessionFactory lookupSessionFactory() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Using session factory '" + getSessionFactoryBeanName() + "' for OpenSessionInViewFilter");
        }
        WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        return wac.getBean(getSessionFactoryBeanName(), SessionFactory.class);
    }

}
