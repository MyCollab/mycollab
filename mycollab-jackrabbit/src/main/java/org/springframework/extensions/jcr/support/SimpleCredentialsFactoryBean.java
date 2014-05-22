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
