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
