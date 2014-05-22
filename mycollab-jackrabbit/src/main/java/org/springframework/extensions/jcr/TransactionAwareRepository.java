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
package org.springframework.extensions.jcr;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.jcr.Credentials;
import javax.jcr.Repository;
import javax.jcr.Session;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;

/**
 * This FactoryBean exposes a proxy for a target JCR Repository, returning the current thread-bound Repository
 * (the Spring-managed transactional Repository or the single OpenPersistenceManagerInView Repository) on
 * <code>login()</code>, if any.
 * <p/>
 * Essentially, <code>login()</code> calls get seamlessly forwarded to
 * <code>SessionFactoryUtils.getSession</code>. Furthermore, <code>Session.logout</code> calls get forwarded
 * to <code>SessionFactoryUtils.releaseSession</code>.
 * <p/>
 * As the Session returned depends on the workspace and credentials given, this implementation accepts a
 * JcrSessionFactory as parameter (basically a wrapper for Repository, Credentials and Workspace properties).
 * The proxy will check the parameters and proxy the Repository for sessions that are retrieved with the
 * credentials and workspace name defined on the session factory. Sessions retrieved with different workspace,
 * credentials are not proxied.
 * <p/>
 * The main advantage of this proxy is that it allows DAOs to work with a plain JCR Repository reference,
 * while still participating in Spring's (or a J2EE server's) resource and transaction management. DAOs will
 * only rely on the JCR API in such a scenario, without any Spring dependencies. DAOs could seamlessly switch
 * between a JNDI Repository and this proxy for a local Repository receiving the reference through Dependency
 * Injection. This will work without any Spring API dependencies in the DAO code!
 * <p/>
 * It is usually preferable to write your JCR-based DAOs with Spring Extensions's JcrTemplate, offering
 * benefits such as consistent data access exceptions instead of RepositoryExceptions at the DAO layer.
 * However, Spring's resource and transaction management (and Dependency Injection) will work for DAOs written
 * against the plain JCR API too.
 * <p/>
 * Of course, you can still access the target Repository even when your DAOs go through this proxy, by
 * defining a bean reference that points directly at your target Repository bean.
 * @author Costin Leau
 * @author Sergio Bossa
 * @author Salvatore Incandela
 */
public class TransactionAwareRepository implements InitializingBean, FactoryBean<Repository> {

    private JcrSessionFactory sessionFactory;
    private Repository proxy;

    /**
     * allow creation of sessions if none is found on the current thread.
     */
    private boolean allowCreate = true;

    /**
     * allow creation of repository even if it doesn't support transactions.
     */
    private boolean allowNonTxRepository = false;

    /**
     * @see org.springframework.beans.factory.FactoryBean#getObject()
     */
    public Repository getObject() throws Exception {
        return proxy;
    }

    /**
     * @see org.springframework.beans.factory.FactoryBean#getObjectType()
     */
    public Class<Repository> getObjectType() {
        return Repository.class;
    }

    /**
     * @see org.springframework.beans.factory.FactoryBean#isSingleton()
     */
    public boolean isSingleton() {
        return true;
    }

    /**
     * @return Returns the allowCreate.
     */
    public boolean isAllowCreate() {
        return allowCreate;
    }

    /**
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        if (sessionFactory == null)
            throw new IllegalArgumentException("sessionFactory is required");
        // the rest of the properties are set by the setTargetFactory
        if (!allowNonTxRepository && !JcrUtils.supportsTransactions(sessionFactory.getRepository()))
            throw new IllegalArgumentException(sessionFactory.toString()
                    + " does NOT support transactions and allowNonTxRepository is false");
    }

    /**
     * Set whether the Repository proxy is allowed to create a non-transactional Session when no transactional
     * Session can be found for the current thread.
     * <p>
     * Default is "true". Can be turned off to enforce access to transactional Sessionss, which safely allows
     * for DAOs written to get a Session without explicit closing (i.e. a <code>Session.login()</code> call
     * without corresponding <code>Session.logout()</code> call).
     * @see SessionFactoryUtils#getSession(SessionFactory, boolean)
     * @param allowCreate The allowCreate to set.
     */
    public void setAllowCreate(boolean allowCreate) {
        this.allowCreate = allowCreate;
    }

    /**
     * @return Returns the allowNonTxRepository.
     */
    public boolean isAllowNonTxRepository() {
        return allowNonTxRepository;
    }

    /**
     * Set whether the Repository proxy is accepted even if it does not support transactions which allows the
     * functionality of thread-bound session but without the tx support. Such an option exists because
     * transaction support is optional for JSR-170 implementations.
     * <p>
     * Default is "true". Can be turned off to enforce only transactional Sessions, which safely allows for
     * DAOs written to get a Session without explicit closing (i.e. a <code>Session.login()</code> call
     * without corresponding <code>Session.logout()</code> call).
     * @param allowNonTxRepository The allowNonTxRepository to set.
     */
    public void setAllowNonTxRepository(boolean allowNonTxRepository) {
        this.allowNonTxRepository = allowNonTxRepository;
    }

    /**
     * Set the target JCR Repository that this proxy should delegate to wrapped in a JcrSessionFactory object
     * along with the credentials and workspace.
     * @param target
     */
    public void setTargetFactory(JcrSessionFactory target) {
        this.sessionFactory = target;
        this.proxy = (Repository) Proxy.newProxyInstance(Repository.class.getClassLoader(),
                new Class[] { Repository.class }, new TransactionAwareRepositoryInvocationHandler());
    }

    /**
     * Return the target JCR Repository that this proxy delegates to.
     * @return
     */
    public Repository getTargetRepository() {
        return this.sessionFactory.getRepository();
    }

    /**
     * Invocation handler that delegates login calls on the Repository proxy to SessionFactoryUtils for being
     * aware of thread-bound transactions.
     */
    private class TransactionAwareRepositoryInvocationHandler implements InvocationHandler {

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            // Invocation on Repository interface coming in...
            // check method invocation
            if (method.getName().equals("login")) {
                boolean matched = false;

                // check method signature
                Class<?>[] paramTypes = method.getParameterTypes();

                // a. login()
                if (paramTypes.length == 0) {
                    // match the sessionFactory definitions
                    matched = (sessionFactory.getWorkspaceName() == null && sessionFactory.getCredentials() == null);
                } else if (paramTypes.length == 1) {
                    // b. login(java.lang.String workspaceName)
                    if (paramTypes[0] == String.class)
                        matched = ObjectUtils.nullSafeEquals(args[0], sessionFactory.getWorkspaceName());
                    // c. login(Credentials credentials)
                    if (Credentials.class.isAssignableFrom(paramTypes[0]))
                        matched = ObjectUtils.nullSafeEquals(args[0], sessionFactory.getCredentials());
                } else if (paramTypes.length == 2) {
                    // d. login(Credentials credentials, java.lang.String
                    // workspaceName)
                    matched = ObjectUtils.nullSafeEquals(args[0], sessionFactory.getCredentials())
                            && ObjectUtils.nullSafeEquals(args[1], sessionFactory.getWorkspaceName());
                }

                if (matched) {
                    Session session = SessionFactoryUtils.getSession(sessionFactory, isAllowCreate());
                    Class<?>[] ifcs = ClassUtils.getAllInterfaces(session);
                    return Proxy.newProxyInstance(getClass().getClassLoader(), ifcs,
                            new TransactionAwareInvocationHandler(session, sessionFactory));
                }
            } else if (method.getName().equals("equals")) {
                // Only consider equal when proxies are identical.
                return (proxy == args[0] ? Boolean.TRUE : Boolean.FALSE);
            } else if (method.getName().equals("hashCode")) {
                // Use hashCode of Repository proxy.
                return hashCode();
            }

            Repository target = getTargetRepository();

            // Invoke method on target Repository.
            try {
                return method.invoke(target, args);
            } catch (InvocationTargetException ex) {
                throw ex.getTargetException();
            }
        }
    }

    /**
     * Invocation handler that delegates close calls on Sessions to SessionFactoryUtils for being aware of
     * thread-bound transactions.
     */
    private static class TransactionAwareInvocationHandler implements InvocationHandler {

        private final Session target;
        private final SessionFactory sessionFactory;

        public TransactionAwareInvocationHandler(Session target, SessionFactory sessionFactory) {
            this.target = target;
            this.sessionFactory = sessionFactory;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            // Invocation on Session interface coming in...

            if (method.getName().equals("equals")) {
                // Only consider equal when proxies are identical.
                return (proxy == args[0] ? Boolean.TRUE : Boolean.FALSE);
            } else if (method.getName().equals("hashCode")) {
                // Use hashCode of Session proxy.
                return hashCode();
            } else if (method.getName().equals("logout")) {
                // Handle close method: only close if not within a transaction.
                if (this.sessionFactory != null) {
                    SessionFactoryUtils.releaseSession(this.target, this.sessionFactory);
                }
                return null;
            }

            // Invoke method on target Session.
            try {
                return method.invoke(this.target, args);
            } catch (InvocationTargetException ex) {
                throw ex.getTargetException();
            }
        }
    }
}
