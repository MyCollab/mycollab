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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.jcr.Credentials;
import javax.jcr.NamespaceRegistry;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Workspace;
import javax.jcr.observation.ObservationManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.extensions.jcr.support.GenericSessionHolderProvider;
import org.springframework.util.Assert;

/**
 * Jcr Session Factory. This class is just a simple wrapper around the
 * repository which facilitates session retrieval through a central point. No
 * exception conversion from Jcr Repository exceptions to Spring DAO exceptions
 * is done.
 * 
 * <p/>
 * The session factory is able to add event listener definitions for each
 * session and some utility methods.<br/>
 * Note that for added functionality (like JackRabbit SessionListener) you can
 * use the decorators package (available from JackRabbit).
 * 
 * <p/>
 * This factory beans allows registration for namespaces. By default, newly
 * registered namespaces will <strong>not</strong> be unregistered once the
 * FactoryBean is destroyed and existing namespaces registered under the same
 * suffix will <strong>not</strong> be overwritten since most JCR
 * implementations do not support these features.
 * 
 * One can change this behavior using the
 * {@link #setForceNamespacesRegistration(boolean)},
 * {@link #setKeepNewNamespaces(boolean) and
 * 
 * @link #skipRegisteredNamespace(boolean)} methods.
 * 
 *       If 'forceNamespacesRegistration' is true and 'keepNewNamespaces' false,
 *       the overwritten namespaces are registered back when the factory is
 *       destroyed.
 * 
 * @author Costin Leau
 * @author Brian Moseley <bcm@osafoundation.org>
 * @author Sergio Bossa
 * @author Salvatore Incandela
 * 
 */
public class JcrSessionFactory implements InitializingBean, DisposableBean, SessionFactory {

    private static final Logger LOG = LoggerFactory.getLogger(JcrSessionFactory.class);

    private Repository repository;

    private String workspaceName;

    private Credentials credentials;

    private EventListenerDefinition eventListeners[] = new EventListenerDefinition[] {};

    private Properties namespaces;

    private Map<String, String> overwrittenNamespaces;

    private boolean forceNamespacesRegistration = false;

    private boolean keepNewNamespaces = true;

    private boolean skipExistingNamespaces = true;

    /**
     * session holder provider manager - optional.
     */
    private SessionHolderProviderManager sessionHolderProviderManager;

    /**
     * session holder provider - determined and used internally.
     */
    private SessionHolderProvider sessionHolderProvider;

    /**
     * Constructor with all the required fields.
     * @param repository
     * @param workspaceName
     * @param credentials
     */
    public JcrSessionFactory(Repository repository, String workspaceName, Credentials credentials) {
        this(repository, workspaceName, credentials, null);
    }

    /**
     * Constructor containing all the fields available.
     * @param repository
     * @param workspaceName
     * @param credentials
     * @param sessionHolderProviderManager
     */
    public JcrSessionFactory(Repository repository, String workspaceName, Credentials credentials, SessionHolderProviderManager sessionHolderProviderManager) {
        this.repository = repository;
        this.workspaceName = workspaceName;
        this.credentials = credentials;
        this.sessionHolderProviderManager = sessionHolderProviderManager;
    }

    /**
     * Empty constructor.
     */
    public JcrSessionFactory() {
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(getRepository(), "repository is required");

        if (eventListeners != null && eventListeners.length > 0 && !JcrUtils.supportsObservation(getRepository()))
            throw new IllegalArgumentException("repository " + getRepositoryInfo() + " does NOT support Observation; remove Listener definitions");

        registerNamespaces();
        registerNodeTypes();

        // determine the session holder provider
        if (sessionHolderProviderManager == null) {
            if (LOG.isDebugEnabled())
                LOG.debug("no session holder provider manager set; using the default one");
            sessionHolderProvider = new GenericSessionHolderProvider();
        } else
            sessionHolderProvider = sessionHolderProviderManager.getSessionProvider(getRepository());
    }

    /**
     * Hook for registering node types on the underlying repository. Since this process is not covered by the
     * spec, each implementation requires its own subclass. By default, this method doesn't do anything.
     * @throws Exception
     */
    protected void registerNodeTypes() throws Exception {
        // do nothing
    }

    /**
     * Hook for un-registering node types on the underlying repository. Since this process is not covered by
     * the spec, each implementation requires its own subclass. By default, this method doesn't do anything.
     * @throws Exception
     */
    protected void unregisterNodeTypes() throws Exception {
        // do nothing
    }

    /**
     * Register the namespaces.
     * @throws Exception
     */
    protected void registerNamespaces() throws Exception {

        if (namespaces == null || namespaces.isEmpty())
            return;

        if (LOG.isDebugEnabled())
            LOG.debug("registering custom namespaces " + namespaces);

        Session session = getBareSession();
        NamespaceRegistry registry = session.getWorkspace().getNamespaceRegistry();

        // do the lookup, so we avoid exceptions
        String[] prefixes = registry.getPrefixes();
        // sort the array
        Arrays.sort(prefixes);

        // unregister namespaces if told so
        if (forceNamespacesRegistration) {

            // save the old namespace only if it makes sense
            if (!keepNewNamespaces)
                overwrittenNamespaces = new HashMap<String, String>(namespaces.size());

            // search occurrences
            for (Object key : namespaces.keySet()) {
                String prefix = (String) key;
                int position = Arrays.binarySearch(prefixes, prefix);
                if (position >= 0) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("prefix " + prefix + " was already registered; unregistering it");
                    }
                    if (!keepNewNamespaces) {
                        // save old namespace
                        overwrittenNamespaces.put(prefix, registry.getURI(prefix));
                    }
                    registry.unregisterNamespace(prefix);
                    // postpone registration for later
                }
            }
        }

        // do the registration
        for (Map.Entry<?, ?> entry : namespaces.entrySet()) {
            //            Map.Entry<String, String> namespace = (Map.Entry<String, String>) entry;
            String prefix = (String) entry.getKey();
            String ns = (String) entry.getValue();

            int position = Arrays.binarySearch(prefixes, prefix);

            if (skipExistingNamespaces && position >= 0) {
                LOG.debug("namespace already registered under [" + prefix + "]; skipping registration");
            } else {
                LOG.debug("registering namespace [" + ns + "] under [" + prefix + "]");
                registry.registerNamespace(prefix, ns);
            }
        }

        session.logout();
    }

    /**
     * @see org.springframework.beans.factory.DisposableBean#destroy()
     */
    @Override
    public void destroy() throws Exception {
        unregisterNodeTypes();
        unregisterNamespaces();
    }

    /**
     * Removes the namespaces.
     */
    protected void unregisterNamespaces() throws Exception {

        if (namespaces == null || namespaces.isEmpty() || keepNewNamespaces)
            return;

        if (LOG.isDebugEnabled())
            LOG.debug("unregistering custom namespaces " + namespaces);

        NamespaceRegistry registry = getSession().getWorkspace().getNamespaceRegistry();

        for (Object key : namespaces.keySet()) {
            String prefix = (String) key;
            registry.unregisterNamespace(prefix);
        }

        if (forceNamespacesRegistration) {
            if (LOG.isDebugEnabled())
                LOG.debug("reverting back overwritten namespaces " + overwrittenNamespaces);
            if (overwrittenNamespaces != null)
                for (Map.Entry<String, String> entry : overwrittenNamespaces.entrySet()) {
                    Map.Entry<String, String> namespace = entry;
                    registry.registerNamespace(namespace.getKey(), namespace.getValue());
                }
        }
    }

    protected Session getBareSession() throws RepositoryException {
        Session session = repository.login(credentials, workspaceName);
        return session;
    }

    /**
     * @see org.springframework.extensions.jcr.SessionFactory#getSession()
     */
    @Override
    public Session getSession() throws RepositoryException {
        Session session = repository.login(credentials, workspaceName);
        return addListeners(session);
    }

    /**
     * @see org.springframework.extensions.jcr.SessionFactory#getSessionHolder(javax.jcr.Session)
     */
    @Override
    public SessionHolder getSessionHolder(Session session) {
        return sessionHolderProvider.createSessionHolder(session);
    }

    /**
     * Hook for adding listeners to the newly returned session. We have to treat exceptions manually and can't
     * reply on the template.
     * @param session JCR session
     * @return the listened session
     * @throws javax.jcr.RepositoryException
     */
    protected Session addListeners(Session session) throws RepositoryException {
        if (eventListeners != null && eventListeners.length > 0) {
            Workspace ws = session.getWorkspace();
            ObservationManager manager = ws.getObservationManager();
            if (LOG.isDebugEnabled())
                LOG.debug("adding listeners " + Arrays.asList(eventListeners).toString() + " for session " + session);

            for (int i = 0; i < eventListeners.length; i++) {
                manager.addEventListener(eventListeners[i].getListener(), eventListeners[i].getEventTypes(), eventListeners[i].getAbsPath(),
                        eventListeners[i].isDeep(), eventListeners[i].getUuid(), eventListeners[i].getNodeTypeName(), eventListeners[i].isNoLocal());
            }
        }
        return session;
    }

    /**
     * @return Returns the repository.
     */
    public Repository getRepository() {
        return repository;
    }

    /**
     * @param repository The repository to set.
     */
    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    /**
     * @param workspaceName The workspaceName to set.
     */
    public void setWorkspaceName(String workspaceName) {
        this.workspaceName = workspaceName;
    }

    /**
     * @param credentials The credentials to set.
     */
    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof JcrSessionFactory)
            return (this.hashCode() == obj.hashCode());
        return false;

    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int result = 17;
        result = 37 * result + repository.hashCode();
        // add the optional params (can be null)
        if (credentials != null)
            result = 37 * result + credentials.hashCode();
        if (workspaceName != null)
            result = 37 * result + workspaceName.hashCode();

        return result;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("SessionFactory for ");
        buffer.append(getRepositoryInfo());
        buffer.append("|workspace=");
        buffer.append(workspaceName);
        return buffer.toString();
    }

    /**
     * @return Returns the eventListenerDefinitions.
     */
    public EventListenerDefinition[] getEventListeners() {
        return eventListeners;
    }

    /**
     * @param eventListenerDefinitions The eventListenerDefinitions to set.
     */
    public void setEventListeners(EventListenerDefinition[] eventListenerDefinitions) {
        this.eventListeners = eventListenerDefinitions;
    }

    /**
     * A toString representation of the Repository.
     * @return
     */
    private String getRepositoryInfo() {
        // in case toString() is called before afterPropertiesSet()
        if (getRepository() == null)
            return "<N/A>";

        StringBuffer buffer = new StringBuffer();
        buffer.append(getRepository().getDescriptor(Repository.REP_NAME_DESC));
        buffer.append(" ");
        buffer.append(getRepository().getDescriptor(Repository.REP_VERSION_DESC));
        return buffer.toString();
    }

    /**
     * @return Returns the namespaces.
     */
    public Properties getNamespaces() {
        return namespaces;
    }

    /**
     * @param namespaces The namespaces to set.
     */
    public void setNamespaces(Properties namespaces) {
        this.namespaces = namespaces;
    }

    /**
     * Used internally.
     * @return Returns the sessionHolderProvider.
     */
    protected SessionHolderProvider getSessionHolderProvider() {
        return sessionHolderProvider;
    }

    /**
     * Used internally.
     * @param sessionHolderProvider The sessionHolderProvider to set.
     */
    protected void setSessionHolderProvider(SessionHolderProvider sessionHolderProvider) {
        this.sessionHolderProvider = sessionHolderProvider;
    }

    /**
     * @return Returns the sessionHolderProviderManager.
     */
    public SessionHolderProviderManager getSessionHolderProviderManager() {
        return sessionHolderProviderManager;
    }

    /**
     * @param sessionHolderProviderManager The sessionHolderProviderManager to set.
     */
    public void setSessionHolderProviderManager(SessionHolderProviderManager sessionHolderProviderManager) {
        this.sessionHolderProviderManager = sessionHolderProviderManager;
    }

    /**
     * Indicate if the given namespace registrations will be kept (the default) when the application context
     * closes down or if they will be unregistered. If unregistered, the namespace mappings that were
     * overriden are registered back to the repository.
     * @see #forceNamespacesRegistration
     * @param keepNamespaces The keepNamespaces to set.
     */
    public void setKeepNewNamespaces(boolean keepNamespaces) {
        this.keepNewNamespaces = keepNamespaces;
    }

    /**
     * Indicate if the given namespace registrations will override the namespace already registered in the
     * repository under the same prefix. This will cause unregistration for the namespaces that will be
     * modified.
     * <p/>
     * However, depending on the {@link #setKeepNewNamespaces(boolean)} setting, the old namespaces can be
     * registered back once the application context is destroyed. False by default.
     * @param forceNamespacesRegistration The forceNamespacesRegistration to set.
     */
    public void setForceNamespacesRegistration(boolean forceNamespacesRegistration) {
        this.forceNamespacesRegistration = forceNamespacesRegistration;
    }

    /**
     * Indicate if the given namespace registrations will skip already registered namespaces or not. If true
     * (default), the new namespace will not be registered and the old namespace kept in place. If not
     * skipped, registration of new namespaces will fail if there are already namespace registered under the
     * same prefix.
     * <p/>
     * This flag is required for JCR implementations which do not support namespace unregistration which
     * render the {@link #setForceNamespacesRegistration(boolean)} method useless (as namespace registration
     * cannot be forced).
     * @param skipRegisteredNamespace The skipRegisteredNamespace to set.
     */
    public void setSkipExistingNamespaces(boolean skipRegisteredNamespace) {
        this.skipExistingNamespaces = skipRegisteredNamespace;
    }

    /**
     * @return Returns the forceNamespacesRegistration.
     */
    public boolean isForceNamespacesRegistration() {
        return forceNamespacesRegistration;
    }

    /**
     * @return Returns the keepNewNamespaces.
     */
    public boolean isKeepNewNamespaces() {
        return keepNewNamespaces;
    }

    /**
     * @return Returns the skipExistingNamespaces.
     */
    public boolean isSkipExistingNamespaces() {
        return skipExistingNamespaces;
    }

    /**
     * @return Returns the credentials.
     */
    public Credentials getCredentials() {
        return credentials;
    }

    /**
     * @return Returns the workspaceName.
     */
    public String getWorkspaceName() {
        return workspaceName;
    }

}
