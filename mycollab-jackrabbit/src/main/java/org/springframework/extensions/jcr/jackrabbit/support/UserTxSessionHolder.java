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
import javax.transaction.UserTransaction;
import javax.transaction.xa.XAResource;

import org.springframework.extensions.jcr.SessionHolder;

/**
 * Extension of Session Holder which includes a UserTransaction which handles the XASession returned by the
 * JackRabbit repository implementation.
 * @see org.springframework.extensions.jcr.jackrabbit.support.JackRabbitUserTransaction
 * @author Costin Leau
 * @author Sergio Bossa
 * @author Salvatore Incandela
 */
public class UserTxSessionHolder extends SessionHolder {

    private UserTransaction transaction;

    /**
     * @param session
     */
    public UserTxSessionHolder(Session session) {
        super(session);
    }

    /**
     * @return Returns the transaction.
     */
    public UserTransaction getTransaction() {
        return transaction;
    }

    /**
     * @see org.springframework.extensions.jcr.SessionHolder#setSession(javax.jcr.Session)
     */
    @Override
    public void setSession(Session session) {
        /*
         * if (!(session instanceof XASession)) throw new IllegalArgumentException( "Session not of type
         * XASession; actual type is " + session.getClass());
         */

        // when using JCA we have another session type
        //        if (session instanceof XASession) {
        //            transaction = new JackRabbitUserTransaction(session);
        //        }
        if (session instanceof XAResource) {
            transaction = new JackRabbitUserTransaction(session);
        }
        super.setSession(session);
    }

    /**
     * @see org.springframework.transaction.support.ResourceHolderSupport#clear()
     */
    @Override
    public void clear() {
        super.clear();
        transaction = null;
    }

}
