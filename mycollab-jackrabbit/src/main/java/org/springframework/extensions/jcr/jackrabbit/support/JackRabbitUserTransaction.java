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
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

/**
 * JackRabbit User transaction (based on the XA Resource returned by JackRabbit).
 * <p/>
 * Inspired from JackRabbit test suite. Internal {@link javax.transaction.UserTransaction} implementation.
 */
public class JackRabbitUserTransaction implements UserTransaction {

    private static byte counter = 0;

    private final XAResource xares;

    private Xid xid;

    private int status = Status.STATUS_NO_TRANSACTION;

    /**
     * Create a new instance of this class. Takes a session as parameter.
     * @param session session. If session is not of type {@link XAResource}, an
     *            <code>IllegalArgumentException</code> is thrown
     */
    public JackRabbitUserTransaction(Session session) {
        //        if (session instanceof XASession) {
        //            xares = ((XASession) session).getXAResource();
        if (session instanceof XAResource) {
            xares = (XAResource) session;
        } else {
            throw new IllegalArgumentException("Session not of type XASession");
        }
    }

    /**
     * @see javax.transaction.UserTransaction#begin
     */
    @Override
    public void begin() throws NotSupportedException, SystemException {
        if (status != Status.STATUS_NO_TRANSACTION) {
            throw new IllegalStateException("Transaction already active");
        }

        try {
            xid = new XidImpl(counter++);
            xares.start(xid, XAResource.TMNOFLAGS);
            status = Status.STATUS_ACTIVE;

        } catch (XAException e) {
            final SystemException systemException = new SystemException("Unable to commit transaction: " + "XA_ERR="
                    + e.errorCode);
            systemException.initCause(e);
            throw systemException;
        }
    }

    /**
     * @see javax.transaction.UserTransaction#commit
     */
    @Override
    public void commit() throws IllegalStateException, RollbackException, SecurityException, SystemException {

        if (status != Status.STATUS_ACTIVE) {
            throw new IllegalStateException("Transaction not active");
        }

        try {
            xares.end(xid, XAResource.TMSUCCESS);

            status = Status.STATUS_PREPARING;
            xares.prepare(xid);
            status = Status.STATUS_PREPARED;

            status = Status.STATUS_COMMITTING;
            xares.commit(xid, false);
            status = Status.STATUS_COMMITTED;

        } catch (XAException e) {

            if (e.errorCode >= XAException.XA_RBBASE && e.errorCode <= XAException.XA_RBEND) {
                RollbackException rollbackException = new RollbackException(e.toString());
                rollbackException.initCause(e);
                throw rollbackException;
            }

            final SystemException systemException = new SystemException("Unable to commit transaction: " + "XA_ERR="
                    + e.errorCode);
            systemException.initCause(e);
            throw systemException;
        }
    }

    /**
     * @see javax.transaction.UserTransaction#getStatus
     */
    @Override
    public int getStatus() throws SystemException {
        return status;
    }

    /**
     * @see javax.transaction.UserTransaction#rollback
     */
    @Override
    public void rollback() throws IllegalStateException, SecurityException, SystemException {

        if (status != Status.STATUS_ACTIVE && status != Status.STATUS_MARKED_ROLLBACK) {

            throw new IllegalStateException("Transaction not active");
        }

        try {
            xares.end(xid, XAResource.TMFAIL);

            status = Status.STATUS_ROLLING_BACK;
            xares.rollback(xid);
            status = Status.STATUS_ROLLEDBACK;

        } catch (XAException e) {
            final SystemException systemException = new SystemException("Unable to commit transaction: " + "XA_ERR="
                    + e.errorCode);
            systemException.initCause(e);
            throw systemException;
        }
    }

    /**
     * @see javax.transaction.UserTransaction#setRollbackOnly()
     */
    @Override
    public void setRollbackOnly() throws IllegalStateException, SystemException {
        if (status != Status.STATUS_ACTIVE) {
            throw new IllegalStateException("Transaction not active");
        }
        status = Status.STATUS_MARKED_ROLLBACK;
    }

    /**
     * @see javax.transaction.UserTransaction#setTransactionTimeout
     */
    @Override
    public void setTransactionTimeout(int seconds) throws SystemException {
    }

    /**
     * Internal {@link Xid} implementation.
     */
    class XidImpl implements Xid {

        /** Global transaction id */
        private final byte[] globalTxId;

        /**
         * Create a new instance of this class. Takes a global transaction number as parameter
         * @param globalTxNumber global transaction number
         */
        public XidImpl(byte globalTxNumber) {
            this.globalTxId = new byte[] { globalTxNumber };
        }

        /**
         * @see javax.transaction.xa.Xid#getFormatId()
         */
        @Override
        public int getFormatId() {
            return 0;
        }

        /**
         * @see javax.transaction.xa.Xid#getBranchQualifier()
         */
        @Override
        public byte[] getBranchQualifier() {
            return new byte[0];
        }

        /**
         * @see javax.transaction.xa.Xid#getGlobalTransactionId()
         */
        @Override
        public byte[] getGlobalTransactionId() {
            return globalTxId;
        }
    }

    /**
     * @return Returns the counter.
     */
    public byte getCounter() {
        return counter;
    }

    /**
     * @param counter The counter to set.
     */
    public void setCounter(byte counter) {
        JackRabbitUserTransaction.counter = counter;
    }
}
