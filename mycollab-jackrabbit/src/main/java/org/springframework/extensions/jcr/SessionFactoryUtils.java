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

import java.io.IOException;

import javax.jcr.AccessDeniedException;
import javax.jcr.InvalidItemStateException;
import javax.jcr.InvalidSerializedDataException;
import javax.jcr.ItemExistsException;
import javax.jcr.ItemNotFoundException;
import javax.jcr.LoginException;
import javax.jcr.MergeException;
import javax.jcr.NamespaceException;
import javax.jcr.NoSuchWorkspaceException;
import javax.jcr.PathNotFoundException;
import javax.jcr.ReferentialIntegrityException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.ValueFormatException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.query.InvalidQueryException;
import javax.jcr.version.VersionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

/**
 * FactoryBean for instantiating a Java Content Repository. This abstract class adds custom functionality
 * subclasses handling only the configuration issues.
 * @author Costin Leau
 * @author Sergio Bossa
 * @author Salvatore Incandela
 */
public abstract class SessionFactoryUtils {

    private static final Logger LOG = LoggerFactory.getLogger(SessionFactoryUtils.class);

    /**
     * Get a JCR Session for the given Repository. Is aware of and will return any existing corresponding
     * Session bound to the current thread, for example when using JcrTransactionManager. Same as
     * <code>getSession</code> but throws the original Repository.
     * @param sessionFactory Jcr Repository to create session with
     * @param allowCreate if a non-transactional Session should be created when no transactional Session can
     *            be found for the current thread
     * @throws RepositoryException
     * @return
     */
    public static Session doGetSession(SessionFactory sessionFactory, boolean allowCreate) throws RepositoryException {
        Assert.notNull(sessionFactory, "No sessionFactory specified");

        // check if there is any transaction going on
        SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager.getResource(sessionFactory);
        if (sessionHolder != null && sessionHolder.getSession() != null)
            return sessionHolder.getSession();

        if (!allowCreate && !TransactionSynchronizationManager.isSynchronizationActive()) {
            throw new IllegalStateException("No session bound to thread, " + "and configuration does not allow creation of non-transactional one here");
        }

        LOG.debug("Opening JCR Session");
        Session session = sessionFactory.getSession();

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            LOG.debug("Registering transaction synchronization for JCR session");
            // Use same session for further JCR actions within the transaction
            // thread object will get removed by synchronization at transaction
            // completion.
            sessionHolder = sessionFactory.getSessionHolder(session);
            sessionHolder.setSynchronizedWithTransaction(true);
            TransactionSynchronizationManager.registerSynchronization(new JcrSessionSynchronization(sessionHolder, sessionFactory));
            TransactionSynchronizationManager.bindResource(sessionFactory, sessionHolder);
        }

        return session;
    }

    /**
     * Get a JCR Session for the given Repository. Is aware of and will return any existing corresponding
     * Session bound to the current thread, for example when using JcrTransactionManager. Will create a new
     * Session otherwise, if allowCreate is true. This is the getSession method used by typical data access
     * code, in combination with releaseSession called when done with the Session. Note that JcrTemplate
     * allows to write data access code without caring about such resource handling. Supports synchronization
     * with both Spring-managed JTA transactions (i.e. JtaTransactionManager) and non-Spring JTA transactions
     * (i.e. plain JTA or EJB CMT).
     * @param sessionFactory JCR Repository to create session with
     * @param allowCreate if a non-transactional Session should be created when no transactional Session can
     *            be found for the current thread
     * @throws DataAccessException
     * @return
     */
    public static Session getSession(SessionFactory sessionFactory, boolean allowCreate) throws DataAccessException {
        try {
            return doGetSession(sessionFactory, allowCreate);
        } catch (RepositoryException ex) {
            throw new DataAccessResourceFailureException("Could not open Jcr Session", ex);
        }
    }

    /**
     * Return whether the given JCR Session is thread-bound that is, bound to the current thread by Spring's
     * transaction facilities (which is used as a thread-bounding utility class).
     * @param session the JCR Session to check
     * @param sessionFactory the JCR SessionFactory that the Session was created with (can be null)
     * @return whether the Session is transactional
     */
    public static boolean isSessionThreadBound(Session session, SessionFactory sessionFactory) {
        if (sessionFactory == null) {
            return false;
        }
        SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager.getResource(sessionFactory);
        return (sessionHolder != null && session == sessionHolder.getSession());
    }

    /**
     * Close the given Session, created via the given repository, if it is not managed externally (i.e. not
     * bound to the thread).
     * @param session the Jcr Session to close
     * @param sessionFactory JcrSessionFactory that the Session was created with (can be null)
     */
    public static void releaseSession(Session session, SessionFactory sessionFactory) {
        if (session == null) {
            return;
        }
        // Only close non thread bound Sessions.
        if (!isSessionThreadBound(session, sessionFactory)) {
            LOG.debug("Closing JCR Session");
            session.logout();
        }
    }

    /**
     * Jcr exception translator - it converts specific JSR-170 checked exceptions into unchecked Spring DA
     * exception.
     * @author Guillaume Bort <guillaume.bort@zenexity.fr>
     * @author Costin Leau
     * @author Sergio Bossa
     * @author Salvatore Incandela
     * @param ex
     * @return
     */
    public static DataAccessException translateException(RepositoryException ex) {
        if (ex instanceof AccessDeniedException) {
            return new DataRetrievalFailureException("Access denied to this data", ex);
        }
        if (ex instanceof ConstraintViolationException) {
            return new DataIntegrityViolationException("Constraint has been violated", ex);
        }
        if (ex instanceof InvalidItemStateException) {
            return new ConcurrencyFailureException("Invalid item state", ex);
        }
        if (ex instanceof InvalidQueryException) {
            return new DataRetrievalFailureException("Invalid query", ex);
        }
        if (ex instanceof InvalidSerializedDataException) {
            return new DataRetrievalFailureException("Invalid serialized data", ex);
        }
        if (ex instanceof ItemExistsException) {
            return new DataIntegrityViolationException("An item already exists", ex);
        }
        if (ex instanceof ItemNotFoundException) {
            return new DataRetrievalFailureException("Item not found", ex);
        }
        if (ex instanceof LoginException) {
            return new DataAccessResourceFailureException("Bad login", ex);
        }
        if (ex instanceof LockException) {
            return new ConcurrencyFailureException("Item is locked", ex);
        }
        if (ex instanceof MergeException) {
            return new DataIntegrityViolationException("Merge failed", ex);
        }
        if (ex instanceof NamespaceException) {
            return new InvalidDataAccessApiUsageException("Namespace not registred", ex);
        }
        if (ex instanceof NoSuchNodeTypeException) {
            return new InvalidDataAccessApiUsageException("No such node type", ex);
        }
        if (ex instanceof NoSuchWorkspaceException) {
            return new DataAccessResourceFailureException("Workspace not found", ex);
        }
        if (ex instanceof PathNotFoundException) {
            return new DataRetrievalFailureException("Path not found", ex);
        }
        if (ex instanceof ReferentialIntegrityException) {
            return new DataIntegrityViolationException("Referential integrity violated", ex);
        }
        if (ex instanceof UnsupportedRepositoryOperationException) {
            return new InvalidDataAccessApiUsageException("Unsupported operation", ex);
        }
        if (ex instanceof ValueFormatException) {
            return new InvalidDataAccessApiUsageException("Incorrect value format", ex);
        }
        if (ex instanceof VersionException) {
            return new DataIntegrityViolationException("Invalid version graph operation", ex);
        }
        // fallback
        return new JcrSystemException(ex);
    }

    /**
     * Jcr exception translator - it converts specific JSR-170 checked exceptions into unchecked Spring DA
     * exception.
     * @param ex
     * @return
     */
    public static DataAccessException translateException(IOException ex) {
        return new DataAccessResourceFailureException("I/O failure", ex);
    }

    /**
     * Callback for resource cleanup at the end of a non-JCR transaction (e.g. when participating in a
     * JtaTransactionManager transaction).
     * @see org.springframework.transaction.jta.JtaTransactionManager
     */
    private static class JcrSessionSynchronization extends TransactionSynchronizationAdapter {

        private final SessionHolder sessionHolder;

        private final SessionFactory sessionFactory;

        private boolean holderActive = true;

        /**
         * @param sessionFactory
         * @param holder
         */
        public JcrSessionSynchronization(SessionHolder holder, SessionFactory sessionFactory) {
            this.sessionFactory = sessionFactory;
            sessionHolder = holder;
        }

        public void suspend() {
            if (this.holderActive) {
                TransactionSynchronizationManager.unbindResource(this.sessionFactory);
            }
        }

        public void resume() {
            if (this.holderActive) {
                TransactionSynchronizationManager.bindResource(this.sessionFactory, this.sessionHolder);
            }
        }

        public void beforeCompletion() {
            TransactionSynchronizationManager.unbindResource(this.sessionFactory);
            this.holderActive = false;
            releaseSession(this.sessionHolder.getSession(), this.sessionFactory);
        }
    }

}
