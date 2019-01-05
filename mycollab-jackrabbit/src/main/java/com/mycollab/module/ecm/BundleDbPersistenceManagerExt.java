/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.ecm;

import com.mycollab.module.ecm.DbUtil;
import com.mycollab.spring.AppContextUtil;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.jackrabbit.core.cache.Cache;
import org.apache.jackrabbit.core.cache.CacheAccessListener;
import org.apache.jackrabbit.core.cluster.UpdateEventChannel;
import org.apache.jackrabbit.core.id.NodeId;
import org.apache.jackrabbit.core.id.PropertyId;
import org.apache.jackrabbit.core.persistence.CachingPersistenceManager;
import org.apache.jackrabbit.core.persistence.IterablePersistenceManager;
import org.apache.jackrabbit.core.persistence.PMContext;
import org.apache.jackrabbit.core.persistence.PersistenceManager;
import org.apache.jackrabbit.core.persistence.check.ConsistencyCheckListener;
import org.apache.jackrabbit.core.persistence.check.ConsistencyChecker;
import org.apache.jackrabbit.core.persistence.check.ConsistencyReport;
import org.apache.jackrabbit.core.persistence.pool.BundleDbPersistenceManager;
import org.apache.jackrabbit.core.persistence.pool.H2PersistenceManager;
import org.apache.jackrabbit.core.persistence.pool.MySqlPersistenceManager;
import org.apache.jackrabbit.core.persistence.pool.PostgreSQLPersistenceManager;
import org.apache.jackrabbit.core.persistence.util.NodeInfo;
import org.apache.jackrabbit.core.state.*;
import org.apache.jackrabbit.core.util.db.ConnectionFactory;
import org.apache.jackrabbit.core.util.db.DatabaseAware;

import javax.jcr.RepositoryException;
import java.util.List;
import java.util.Map;

/**
 * Customize db persistence of jackrabbit
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class BundleDbPersistenceManagerExt implements PersistenceManager, CachingPersistenceManager, IterablePersistenceManager, CacheAccessListener, ConsistencyChecker, DatabaseAware {

    private BundleDbPersistenceManager wrapManager;

    public BundleDbPersistenceManagerExt() {
        HikariDataSource ds = AppContextUtil.getSpringBean(HikariDataSource.class);
        String schemaType = DbUtil.getSchemaType(ds.getDriverClassName());

        if (schemaType.equals("postgresql")) {
            wrapManager = new PostgreSQLPersistenceManager();
        } else if (schemaType.equals("mysql")) {
            wrapManager = new MySqlPersistenceManager();
        } else if (schemaType.equals("h2")) {
            wrapManager = new H2PersistenceManager();
        }

        wrapManager.setDriver(ds.getDriverClassName());
        wrapManager.setUser(ds.getUsername());
        wrapManager.setPassword(ds.getPassword());
        wrapManager.setUrl(ds.getJdbcUrl());

        wrapManager.setDatabaseType(schemaType);
    }

    /**
     * {@inheritDoc}
     */
    public void init(PMContext context) throws Exception {
        wrapManager.init(context);
    }

    @Override
    public void cacheAccessed(long accessCount) {
        wrapManager.cacheAccessed(accessCount);
    }

    @Override
    public void disposeCache(Cache cache) {
        wrapManager.disposeCache(cache);
    }

    @Override
    public void onExternalUpdate(ChangeLog changes) {
        wrapManager.onExternalUpdate(changes);
    }

    @Override
    public List<NodeId> getAllNodeIds(NodeId after, int maxCount) throws ItemStateException, RepositoryException {
        return wrapManager.getAllNodeIds(after, maxCount);
    }

    @Override
    public Map<NodeId, NodeInfo> getAllNodeInfos(NodeId after, int maxCount) throws ItemStateException, RepositoryException {
        return wrapManager.getAllNodeInfos(after, maxCount);
    }

    @Override
    public void close() throws Exception {
        wrapManager.close();
    }

    @Override
    public NodeState createNew(NodeId id) {
        return wrapManager.createNew(id);
    }

    @Override
    public PropertyState createNew(PropertyId id) {
        return wrapManager.createNew(id);
    }

    @Override
    public NodeState load(NodeId id) throws NoSuchItemStateException, ItemStateException {
        return wrapManager.load(id);
    }

    @Override
    public PropertyState load(PropertyId id) throws NoSuchItemStateException, ItemStateException {
        return wrapManager.load(id);
    }

    @Override
    public NodeReferences loadReferencesTo(NodeId id) throws NoSuchItemStateException, ItemStateException {
        return wrapManager.loadReferencesTo(id);
    }

    @Override
    public boolean exists(NodeId id) throws ItemStateException {
        return wrapManager.exists(id);
    }

    @Override
    public boolean exists(PropertyId id) throws ItemStateException {
        return wrapManager.exists(id);
    }

    @Override
    public boolean existsReferencesTo(NodeId targetId) throws ItemStateException {
        return wrapManager.exists(targetId);
    }

    @Override
    public void store(ChangeLog changeLog) throws ItemStateException {
        wrapManager.store(changeLog);
    }

    @Override
    public void checkConsistency(String[] uuids, boolean recursive, boolean fix) {
        wrapManager.checkConsistency(uuids, recursive, fix);
    }

    @Override
    public void setEventChannel(UpdateEventChannel eventChannel) {
        wrapManager.setEventChannel(eventChannel);
    }

    @Override
    public ConsistencyReport check(String[] uuids, boolean recursive, boolean fix, String lostNFoundId, ConsistencyCheckListener listener) throws RepositoryException {
        return wrapManager.check(uuids, recursive, fix, lostNFoundId, listener);
    }

    @Override
    public void setConnectionFactory(ConnectionFactory connectionFactory) {
        wrapManager.setConnectionFactory(connectionFactory);
    }

    /**
     * Returns the configured JDBC connection url.
     * @return the configured JDBC connection url.
     */
    public String getUrl() {
        return wrapManager.getUrl();
    }

    /**
     * Sets the JDBC connection URL.
     * The connection can be created using a JNDI Data Source as well.
     * To do that, the driver class name must reference a javax.naming.Context class
     * (for example javax.naming.InitialContext), and the URL must be the JNDI URL
     * (for example java:comp/env/jdbc/Test).
     *
     * @param url the url to set.
     */
    public void setUrl(String url) {
        wrapManager.setUrl(url);
    }

    /**
     * Returns the configured user that is used to establish JDBC connections.
     * @return the JDBC user.
     */
    public String getUser() {
        return wrapManager.getUser();
    }

    /**
     * Sets the user name that will be used to establish JDBC connections.
     * @param user the user name.
     */
    public void setUser(String user) {
        wrapManager.setUser(user);
    }

    /**
     * Returns the configured password that is used to establish JDBC connections.
     * @return the password.
     */
    public String getPassword() {
        return wrapManager.getPassword();
    }

    /**
     * Sets the password that will be used to establish JDBC connections.
     * @param password the password for the connection
     */
    public void setPassword(String password) {
        wrapManager.setPassword(password);
    }

    /**
     * Returns the class name of the JDBC driver.
     * @return the class name of the JDBC driver.
     */
    public String getDriver() {
        return wrapManager.getDriver();
    }

    /**
     * Sets the class name of the JDBC driver. The driver class will be loaded
     * during {@link #init(PMContext) init} in order to assure the existence.
     * If no driver is specified, the default driver for the database is used.
     *
     * @param driver the class name of the driver
     */
    public void setDriver(String driver) {
        wrapManager.setDriver(driver);
    }

    /**
     * Returns the configured schema object prefix.
     * @return the configured schema object prefix.
     */
    public String getSchemaObjectPrefix() {
        return wrapManager.getSchemaObjectPrefix();
    }

    /**
     * Sets the schema object prefix. This string is used to prefix all schema
     * objects, like tables and indexes. this is useful, if several persistence
     * managers use the same database.
     *
     * @param schemaObjectPrefix the prefix for schema objects.
     */
    public void setSchemaObjectPrefix(String schemaObjectPrefix) {
        wrapManager.setSchemaObjectPrefix(schemaObjectPrefix);
    }
    /**
     * Returns the configured database type name.
     * @return the database type name.
     */
    public String getDatabaseType() {
        return wrapManager.getDatabaseType();
    }

    public String getDataSourceName() {
        return wrapManager.getDataSourceName();
    }

    public void setDataSourceName(String dataSourceName) {
        wrapManager.setDataSourceName(dataSourceName);
    }
}
