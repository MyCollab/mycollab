/**
 * This file is part of mycollab-jackrabbit.
 *
 * mycollab-jackrabbit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-jackrabbit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-jackrabbit.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.springframework.extensions.jcr;

import javax.jcr.Session;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * This interceptor binds a new Jcr Session to the thread before a method call, closing and removing it
 * afterwards in case of any method outcome. If there already is a pre-bound Jcr Session (e.g. from
 * JcrTransactionManager, or from a surrounding JCR-intercepted method), the interceptor simply participates
 * in it.
 *
 * Application code must retrieve a JCR Session via the <code>JcrSessionFactoryUtils.getSession</code> method,
 * to be able to detect a thread-bound Jcr Session. It is preferable to use <code>getSession</code> with
 * allowCreate=false, if the code relies on the interceptor to provide proper session handling. Typically, the
 * code will look as follows:
 * 
 * <pre>
 *    public void doJcrAction() {
 *      Session session = JcrSessionFactoryUtils.getSession(this.jsf, false);
 *      try {
 *        ...
 *      }
 *      catch (RepositoryException ex) {
 *        throw JcrSessionFactoryUtils.convertJcrAccessException(ex);
 *      }
 *    }
 * </pre>
 * 
 * Note that the application must care about handling RepositoryExceptions itself, preferably via delegating
 * to the <code>JcrSessionFactoryUtils.convertJcrAccessException</code> method that converts them to
 * exceptions that are compatible with the <code>org.springframework.dao</code> exception hierarchy (like
 * JcrTemplate does).
 *
 * This class can be considered a declarative alternative to JcrTemplate's callback approach. The advantages
 * are:
 * <ul>
 * <li>no anonymous classes necessary for callback implementations;
 * <li>the possibility to throw any application exceptions from within data access code.
 * </ul>
 *
 * The drawbacks are:
 * <ul>
 * <li>the dependency on interceptor configuration;
 * <li>the delegating try/catch blocks.
 * </ul>
 * @author Costin Leau
 * @author Sergio Bossa
 * @author Salvatore Incandela
 */
public class JcrInterceptor extends JcrAccessor implements MethodInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(JcrInterceptor.class);

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        boolean existingTransaction = false;
        Session session = SessionFactoryUtils.getSession(getSessionFactory(), true);
        if (TransactionSynchronizationManager.hasResource(getSessionFactory())) {
            LOG.debug("Found thread-bound Session for JCR interceptor");
            existingTransaction = true;
        } else {
            LOG.debug("Using new Session for JCR interceptor");
            TransactionSynchronizationManager.bindResource(getSessionFactory(), getSessionFactory().getSessionHolder(session));
        }
        try {
            Object retVal = methodInvocation.proceed();
            // flushIfNecessary(session, existingTransaction);
            return retVal;
        } finally {
            if (existingTransaction) {
                LOG.debug("Not closing pre-bound JCR Session after interceptor");
            } else {
                TransactionSynchronizationManager.unbindResource(getSessionFactory());
                SessionFactoryUtils.releaseSession(session, getSessionFactory());
            }
        }
    }
}
