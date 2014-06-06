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

import org.springframework.transaction.support.ResourceHolderSupport;

/**
 * Holder object for Jcr Session.
 * @author Guillaume Bort <guillaume.bort@zenexity.fr>
 * @author Costin Leau
 * @author Sergio Bossa
 * @author Salvatore Incandela
 */
public class SessionHolder extends ResourceHolderSupport {

    private Session session;

    public SessionHolder(Session session) {
        setSession(session);
    }

    protected void setSession(Session session) {
        this.session = session;
    }

    public Session getSession() {
        return session;
    }

    /**
     * @see org.springframework.transaction.support.ResourceHolderSupport#clear()
     */
    public void clear() {
        super.clear();
        session = null;
    }
}
