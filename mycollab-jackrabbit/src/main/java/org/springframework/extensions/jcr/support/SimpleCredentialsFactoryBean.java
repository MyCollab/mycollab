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
package org.springframework.extensions.jcr.support;

import javax.jcr.SimpleCredentials;

import org.springframework.beans.factory.FactoryBean;

/**
 * Simple factory for instances of <code>javax.jcr.SimpleCredentials</code>.
 * Allows for setup of SimpleCredentials with a password of type <code>java.lang.String</code>
 * instead of <code>char[]</code>.
 * 
 * @author Mirko Zeibig
 * @since 30.11.2012
 */
public class SimpleCredentialsFactoryBean implements FactoryBean<SimpleCredentials>{

    private String userID;
    private String password;
    private SimpleCredentials simpleCredentials;

    @Override
    public SimpleCredentials getObject() throws Exception {
        if (simpleCredentials == null) {
            simpleCredentials = new SimpleCredentials(userID, password.toCharArray());
        }
        return simpleCredentials;
    }

    @Override
    public Class<?> getObjectType() {
        return SimpleCredentials.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }


    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
