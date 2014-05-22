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

import java.util.HashMap;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

/**
 * This class contains the some of the item names predefined by the JCR spec 1.0 (like 'jcr', 'nt', 'mix').
 * The class is namespace aware (that's why it's not static) and will return the correct name if the namespace
 * prefixes are changed (from 'jcr' to 'foo' for example). If the cache is enabled, it will be populated in
 * lazy manner (once a certain property is looked up).
 * <p/>
 * The class can work in two modes:
 * <ol>
 * <li>Caching the names by default (default).</li>
 * <li>Detecting prefixes and computing the name on each call.</li>
 * </ol>
 * <p/>
 * <strong>Note</strong> This class was inspired by JackRabbit's JcrConstants.
 * @author Costin Leau
 * @author Sergio Bossa
 * @author Salvatore Incandela
 */
public class JcrConstants {

    /**
     * jcr:autoCreated
     */
    private static final String JCR_AUTOCREATED = "autoCreated";
    /**
     * jcr:baseVersion
     */
    private static final String JCR_BASEVERSION = "baseVersion";
    /**
     * jcr:child
     */
    private static final String JCR_CHILD = "child";
    /**
     * jcr:childNodeDefinition
     */
    private static final String JCR_CHILDNODEDEFINITION = "childNodeDefinition";
    /**
     * jcr:content
     */
    private static final String JCR_CONTENT = "content";
    /**
     * jcr:created
     */
    private static final String JCR_CREATED = "created";
    /**
     * jcr:data
     */
    private static final String JCR_DATA = "data";
    /**
     * jcr:defaultPrimaryType
     */
    private static final String JCR_DEFAULTPRIMARYTYPE = "defaultPrimaryType";
    /**
     * jcr:defaultValues
     */
    private static final String JCR_DEFAULTVALUES = "defaultValues";
    /**
     * jcr:encoding
     */
    private static final String JCR_ENCODING = "encoding";
    /**
     * jcr:frozenMixinTypes
     */
    private static final String JCR_FROZENMIXINTYPES = "frozenMixinTypes";
    /**
     * jcr:frozenNode
     */
    private static final String JCR_FROZENNODE = "frozenNode";
    /**
     * jcr:frozenPrimaryType
     */
    private static final String JCR_FROZENPRIMARYTYPE = "frozenPrimaryType";
    /**
     * jcr:frozenUuid
     */
    private static final String JCR_FROZENUUID = "frozenUuid";
    /**
     * jcr:hasOrderableChildNodes
     */
    private static final String JCR_HASORDERABLECHILDNODES = "hasOrderableChildNodes";
    /**
     * jcr:isCheckedOut
     */
    private static final String JCR_ISCHECKEDOUT = "isCheckedOut";
    /**
     * jcr:isMixin
     */
    private static final String JCR_ISMIXIN = "isMixin";
    /**
     * jcr:language
     */
    private static final String JCR_LANGUAGE = "language";
    /**
     * jcr:lastModified
     */
    private static final String JCR_LASTMODIFIED = "lastModified";
    /**
     * jcr:lockIsDeep
     */
    private static final String JCR_LOCKISDEEP = "lockIsDeep";
    /**
     * jcr:lockOwner
     */
    private static final String JCR_LOCKOWNER = "lockOwner";
    /**
     * jcr:mandatory
     */
    private static final String JCR_MANDATORY = "mandatory";
    /**
     * jcr:mergeFailed
     */
    private static final String JCR_MERGEFAILED = "mergeFailed";
    /**
     * jcr:mimeType
     */
    private static final String JCR_MIMETYPE = "mimeType";
    /**
     * jcr:mixinTypes
     */
    private static final String JCR_MIXINTYPES = "mixinTypes";
    /**
     * jcr:multiple
     */
    private static final String JCR_MULTIPLE = "multiple";
    /**
     * jcr:name
     */
    private static final String JCR_NAME = "name";
    /**
     * jcr:nodeTypeName
     */
    private static final String JCR_NODETYPENAME = "nodeTypeName";
    /**
     * jcr:onParentVersion
     */
    private static final String JCR_ONPARENTVERSION = "onParentVersion";
    /**
     * jcr:predecessors
     */
    private static final String JCR_PREDECESSORS = "predecessors";
    /**
     * jcr:primaryItemName
     */
    private static final String JCR_PRIMARYITEMNAME = "primaryItemName";
    /**
     * jcr:primaryType
     */
    private static final String JCR_PRIMARYTYPE = "primaryType";
    /**
     * jcr:propertyDefinition
     */
    private static final String JCR_PROPERTYDEFINITION = "propertyDefinition";
    /**
     * jcr:protected
     */
    private static final String JCR_PROTECTED = "protected";
    /**
     * jcr:requiredPrimaryTypes
     */
    private static final String JCR_REQUIREDPRIMARYTYPES = "requiredPrimaryTypes";
    /**
     * jcr:requiredType
     */
    private static final String JCR_REQUIREDTYPE = "requiredType";
    /**
     * jcr:rootVersion
     */
    private static final String JCR_ROOTVERSION = "rootVersion";
    /**
     * jcr:sameNameSiblings
     */
    private static final String JCR_SAMENAMESIBLINGS = "sameNameSiblings";
    /**
     * jcr:statement
     */
    private static final String JCR_STATEMENT = "statement";
    /**
     * jcr:successors
     */
    private static final String JCR_SUCCESSORS = "successors";
    /**
     * jcr:supertypes
     */
    private static final String JCR_SUPERTYPES = "supertypes";
    /**
     * jcr:system
     */
    private static final String JCR_SYSTEM = "system";
    /**
     * jcr:uuid
     */
    private static final String JCR_UUID = "uuid";
    /**
     * jcr:valueConstraints
     */
    private static final String JCR_VALUECONSTRAINTS = "valueConstraints";
    /**
     * jcr:versionHistory
     */
    private static final String JCR_VERSIONHISTORY = "versionHistory";
    /**
     * jcr:versionLabels
     */
    private static final String JCR_VERSIONLABELS = "versionLabels";
    /**
     * jcr:versionStorage
     */
    private static final String JCR_VERSIONSTORAGE = "versionStorage";
    /**
     * jcr:versionableUuid
     */
    private static final String JCR_VERSIONABLEUUID = "versionableUuid";

    /**
     * Pseudo property jcr:path used with query results
     */
    private static final String JCR_PATH = "path";
    /**
     * Pseudo property jcr:score used with query results
     */
    private static final String JCR_SCORE = "score";

    /**
     * mix:lockable
     */
    private static final String MIX_LOCKABLE = "lockable";
    /**
     * mix:referenceable
     */
    private static final String MIX_REFERENCEABLE = "referenceable";
    /**
     * mix:versionable
     */
    private static final String MIX_VERSIONABLE = "versionable";
    /**
     * nt:base
     */
    private static final String NT_BASE = "base";
    /**
     * nt:childNodeDefinition
     */
    private static final String NT_CHILDNODEDEFINITION = "childNodeDefinition";
    /**
     * nt:file
     */
    private static final String NT_FILE = "file";
    /**
     * nt:folder
     */
    private static final String NT_FOLDER = "folder";
    /**
     * nt:frozenNode
     */
    private static final String NT_FROZENNODE = "frozenNode";
    /**
     * nt:hierarchyNode
     */
    private static final String NT_HIERARCHYNODE = "hierarchyNode";
    /**
     * nt:linkedFile
     */
    private static final String NT_LINKEDFILE = "linkedFile";
    /**
     * nt:nodeType
     */
    private static final String NT_NODETYPE = "nodeType";
    /**
     * nt:propertyDefinition
     */
    private static final String NT_PROPERTYDEFINITION = "propertyDefinition";
    /**
     * nt:query
     */
    private static final String NT_QUERY = "query";
    /**
     * nt:resource
     */
    private static final String NT_RESOURCE = "resource";
    /**
     * nt:unstructured
     */
    private static final String NT_UNSTRUCTURED = "unstructured";
    /**
     * nt:version
     */
    private static final String NT_VERSION = "version";
    /**
     * nt:versionHistory
     */
    private static final String NT_VERSIONHISTORY = "versionHistory";
    /**
     * nt:versionLabels
     */
    private static final String NT_VERSIONLABELS = "versionLabels";
    /**
     * nt:versionedChild
     */
    private static final String NT_VERSIONEDCHILD = "versionedChild";

    private boolean cache = true;
    private Session session = null;

    // JCR related namespaces
    protected static final String JCR_NS = "http://www.jcp.org/jcr/1.0";
    protected static final String NT_NS = "http://www.jcp.org/jcr/nt/1.0";
    protected static final String MIX_NS = "http://www.jcp.org/jcr/mix/1.0";

    /**
     * Cache for jcr items.
     */
    protected final Map<Integer, String> jcrCacheMap = new HashMap<Integer, String>();

    /**
     * Cache for nt and mix items. (to avoid String classes and to balance the maps).
     */
    protected final Map<Integer, String> ntCacheMap = new HashMap<Integer, String>();

    /**
     * Constructor.
     * @param cache true to cache resolved names, false otherwise.
     */
    public JcrConstants(Session session, boolean cache) {
        this.cache = cache;
        this.session = session;
    }

    public JcrConstants(Session session) {
        this(session, true);
    }

    /**
     * Resolve name.
     * @param namespace
     * @param property
     * @return
     */
    protected String resolveName(String namespace, String property) {
        // search cache
        if (cache) {
            Map<Integer, String> map;

            // jcr namespace
            if (JCR_NS.hashCode() == namespace.hashCode())
                map = jcrCacheMap;
            // mix and nt namespace
            else
                map = ntCacheMap;

            String result = map.get(property.hashCode());
            // cache miss
            if (result == null)
                result = computeName(namespace, property);
            map.put(property.hashCode(), result);

            return result;
        }
        // dynamic resolve
        return computeName(namespace, property);
    }

    /**
     * Computes the actual name.
     * @param namespace
     * @param property
     * @return
     */
    protected String computeName(String namespace, String property) {
        try {
            StringBuffer buffer = new StringBuffer();
            buffer.append(session.getNamespacePrefix(namespace));
            buffer.append(':');
            buffer.append(property);
            return buffer.toString();
        } catch (RepositoryException e) {
            throw SessionFactoryUtils.translateException(e);
        }
    }

    /**
     * Creates the actual cache.
     */
    protected void createCache() {
        // String hashcode is used as key for fast look-ups.

        //
        // madness coming
        //

        // JCR
        jcrCacheMap.put(JCR_AUTOCREATED.hashCode(), computeName(JCR_NS, JCR_AUTOCREATED));
        jcrCacheMap.put(JCR_BASEVERSION.hashCode(), computeName(JCR_NS, JCR_BASEVERSION));
        jcrCacheMap.put(JCR_CHILD.hashCode(), computeName(JCR_NS, JCR_CHILD));
        jcrCacheMap.put(JCR_CHILDNODEDEFINITION.hashCode(), computeName(JCR_NS, JCR_CHILDNODEDEFINITION));
        jcrCacheMap.put(JCR_CONTENT.hashCode(), computeName(JCR_NS, JCR_CONTENT));
        jcrCacheMap.put(JCR_CREATED.hashCode(), computeName(JCR_NS, JCR_CREATED));
        jcrCacheMap.put(JCR_DATA.hashCode(), computeName(JCR_NS, JCR_DATA));
        jcrCacheMap.put(JCR_DEFAULTPRIMARYTYPE.hashCode(), computeName(JCR_NS, JCR_DEFAULTPRIMARYTYPE));
        jcrCacheMap.put(JCR_DEFAULTVALUES.hashCode(), computeName(JCR_NS, JCR_DEFAULTVALUES));
        jcrCacheMap.put(JCR_ENCODING.hashCode(), computeName(JCR_NS, JCR_ENCODING));
        jcrCacheMap.put(JCR_FROZENMIXINTYPES.hashCode(), computeName(JCR_NS, JCR_FROZENMIXINTYPES));
        jcrCacheMap.put(JCR_FROZENNODE.hashCode(), computeName(JCR_NS, JCR_FROZENNODE));
        jcrCacheMap.put(JCR_FROZENPRIMARYTYPE.hashCode(), computeName(JCR_NS, JCR_FROZENPRIMARYTYPE));
        jcrCacheMap.put(JCR_FROZENUUID.hashCode(), computeName(JCR_NS, JCR_FROZENUUID));
        jcrCacheMap.put(JCR_HASORDERABLECHILDNODES.hashCode(), computeName(JCR_NS, JCR_HASORDERABLECHILDNODES));
        jcrCacheMap.put(JCR_ISCHECKEDOUT.hashCode(), computeName(JCR_NS, JCR_ISCHECKEDOUT));
        jcrCacheMap.put(JCR_ISMIXIN.hashCode(), computeName(JCR_NS, JCR_ISMIXIN));
        jcrCacheMap.put(JCR_LANGUAGE.hashCode(), computeName(JCR_NS, JCR_LANGUAGE));
        jcrCacheMap.put(JCR_LASTMODIFIED.hashCode(), computeName(JCR_NS, JCR_LASTMODIFIED));
        jcrCacheMap.put(JCR_LOCKISDEEP.hashCode(), computeName(JCR_NS, JCR_LOCKISDEEP));
        jcrCacheMap.put(JCR_LOCKOWNER.hashCode(), computeName(JCR_NS, JCR_LOCKOWNER));
        jcrCacheMap.put(JCR_MANDATORY.hashCode(), computeName(JCR_NS, JCR_MANDATORY));
        jcrCacheMap.put(JCR_MERGEFAILED.hashCode(), computeName(JCR_NS, JCR_MERGEFAILED));
        jcrCacheMap.put(JCR_MIMETYPE.hashCode(), computeName(JCR_NS, JCR_MIMETYPE));
        jcrCacheMap.put(JCR_MIXINTYPES.hashCode(), computeName(JCR_NS, JCR_MIXINTYPES));
        jcrCacheMap.put(JCR_MULTIPLE.hashCode(), computeName(JCR_NS, JCR_MULTIPLE));
        jcrCacheMap.put(JCR_NAME.hashCode(), computeName(JCR_NS, JCR_NAME));
        jcrCacheMap.put(JCR_NODETYPENAME.hashCode(), computeName(JCR_NS, JCR_NODETYPENAME));
        jcrCacheMap.put(JCR_ONPARENTVERSION.hashCode(), computeName(JCR_NS, JCR_ONPARENTVERSION));
        jcrCacheMap.put(JCR_PATH.hashCode(), computeName(JCR_NS, JCR_PATH));
        jcrCacheMap.put(JCR_PREDECESSORS.hashCode(), computeName(JCR_NS, JCR_PREDECESSORS));
        jcrCacheMap.put(JCR_PRIMARYITEMNAME.hashCode(), computeName(JCR_NS, JCR_PRIMARYITEMNAME));
        jcrCacheMap.put(JCR_PRIMARYTYPE.hashCode(), computeName(JCR_NS, JCR_PRIMARYTYPE));
        jcrCacheMap.put(JCR_PRIMARYITEMNAME.hashCode(), computeName(JCR_NS, JCR_PRIMARYITEMNAME));
        jcrCacheMap.put(JCR_PROPERTYDEFINITION.hashCode(), computeName(JCR_NS, JCR_PROPERTYDEFINITION));
        jcrCacheMap.put(JCR_PROTECTED.hashCode(), computeName(JCR_NS, JCR_PROTECTED));
        jcrCacheMap.put(JCR_REQUIREDPRIMARYTYPES.hashCode(), computeName(JCR_NS, JCR_REQUIREDPRIMARYTYPES));
        jcrCacheMap.put(JCR_REQUIREDTYPE.hashCode(), computeName(JCR_NS, JCR_REQUIREDTYPE));
        jcrCacheMap.put(JCR_ROOTVERSION.hashCode(), computeName(JCR_NS, JCR_ROOTVERSION));
        jcrCacheMap.put(JCR_SAMENAMESIBLINGS.hashCode(), computeName(JCR_NS, JCR_SAMENAMESIBLINGS));
        jcrCacheMap.put(JCR_SCORE.hashCode(), computeName(JCR_NS, JCR_SCORE));
        jcrCacheMap.put(JCR_STATEMENT.hashCode(), computeName(JCR_NS, JCR_STATEMENT));
        jcrCacheMap.put(JCR_SUCCESSORS.hashCode(), computeName(JCR_NS, JCR_SUCCESSORS));
        jcrCacheMap.put(JCR_SUPERTYPES.hashCode(), computeName(JCR_NS, JCR_SUPERTYPES));
        jcrCacheMap.put(JCR_SYSTEM.hashCode(), computeName(JCR_NS, JCR_SYSTEM));
        jcrCacheMap.put(JCR_UUID.hashCode(), computeName(JCR_NS, JCR_UUID));
        jcrCacheMap.put(JCR_VALUECONSTRAINTS.hashCode(), computeName(JCR_NS, JCR_VALUECONSTRAINTS));
        jcrCacheMap.put(JCR_VERSIONABLEUUID.hashCode(), computeName(JCR_NS, JCR_VERSIONABLEUUID));
        jcrCacheMap.put(JCR_VERSIONHISTORY.hashCode(), computeName(JCR_NS, JCR_VERSIONHISTORY));
        jcrCacheMap.put(JCR_VERSIONLABELS.hashCode(), computeName(JCR_NS, JCR_VERSIONLABELS));
        jcrCacheMap.put(JCR_VERSIONSTORAGE.hashCode(), computeName(JCR_NS, JCR_VERSIONSTORAGE));

        // MIX
        jcrCacheMap.put(MIX_LOCKABLE.hashCode(), computeName(MIX_NS, MIX_LOCKABLE));
        jcrCacheMap.put(MIX_REFERENCEABLE.hashCode(), computeName(MIX_NS, MIX_REFERENCEABLE));
        jcrCacheMap.put(MIX_VERSIONABLE.hashCode(), computeName(MIX_NS, MIX_VERSIONABLE));

        // NT
        jcrCacheMap.put(NT_BASE.hashCode(), computeName(NT_NS, NT_BASE));
        jcrCacheMap.put(NT_CHILDNODEDEFINITION.hashCode(), computeName(NT_NS, NT_CHILDNODEDEFINITION));
        jcrCacheMap.put(NT_FILE.hashCode(), computeName(NT_NS, NT_FILE));
        jcrCacheMap.put(NT_FOLDER.hashCode(), computeName(NT_NS, NT_FOLDER));
        jcrCacheMap.put(NT_FROZENNODE.hashCode(), computeName(NT_NS, NT_FROZENNODE));
        jcrCacheMap.put(NT_HIERARCHYNODE.hashCode(), computeName(NT_NS, NT_HIERARCHYNODE));
        jcrCacheMap.put(NT_LINKEDFILE.hashCode(), computeName(NT_NS, NT_LINKEDFILE));
        jcrCacheMap.put(NT_NODETYPE.hashCode(), computeName(NT_NS, NT_NODETYPE));
        jcrCacheMap.put(NT_PROPERTYDEFINITION.hashCode(), computeName(NT_NS, NT_PROPERTYDEFINITION));
        jcrCacheMap.put(NT_QUERY.hashCode(), computeName(NT_NS, NT_QUERY));
        jcrCacheMap.put(NT_RESOURCE.hashCode(), computeName(NT_NS, NT_RESOURCE));
        jcrCacheMap.put(NT_UNSTRUCTURED.hashCode(), computeName(NT_NS, NT_UNSTRUCTURED));
        jcrCacheMap.put(NT_VERSION.hashCode(), computeName(NT_NS, NT_VERSION));
        jcrCacheMap.put(NT_VERSIONEDCHILD.hashCode(), computeName(NT_NS, NT_VERSIONEDCHILD));
        jcrCacheMap.put(NT_VERSIONHISTORY.hashCode(), computeName(NT_NS, NT_VERSIONHISTORY));
        jcrCacheMap.put(NT_VERSIONLABELS.hashCode(), computeName(NT_NS, NT_VERSIONLABELS));
    }

    public String getJCR_AUTOCREATED() {
        return resolveName(JCR_NS, JCR_AUTOCREATED);
    }

    /**
     * @return Returns the JCR_BASEVERSION.
     */
    public String getJCR_BASEVERSION() {
        return resolveName(JCR_NS, JCR_BASEVERSION);
    }

    /**
     * @return Returns the JCR_CHILD.
     */
    public String getJCR_CHILD() {
        return resolveName(JCR_NS, JCR_CHILD);
    }

    /**
     * @return Returns the JCR_CHILDNODEDEFINITION.
     */
    public String getJCR_CHILDNODEDEFINITION() {
        return resolveName(JCR_NS, JCR_CHILDNODEDEFINITION);
    }

    /**
     * @return Returns the JCR_CONTENT.
     */
    public String getJCR_CONTENT() {
        return resolveName(JCR_NS, JCR_CONTENT);
    }

    /**
     * @return Returns the JCR_CREATED.
     */
    public String getJCR_CREATED() {
        return resolveName(JCR_NS, JCR_CREATED);
    }

    /**
     * @return Returns the JCR_DATA.
     */
    public String getJCR_DATA() {
        return resolveName(JCR_NS, JCR_DATA);
    }

    /**
     * @return Returns the JCR_DEFAULTPRIMARYTYPE.
     */
    public String getJCR_DEFAULTPRIMARYTYPE() {
        return resolveName(JCR_NS, JCR_DEFAULTPRIMARYTYPE);
    }

    /**
     * @return Returns the JCR_DEFAULTVALUES.
     */
    public String getJCR_DEFAULTVALUES() {
        return resolveName(JCR_NS, JCR_DEFAULTVALUES);
    }

    /**
     * @return Returns the JCR_ENCODING.
     */
    public String getJCR_ENCODING() {
        return resolveName(JCR_NS, JCR_ENCODING);
    }

    /**
     * @return Returns the JCR_FROZENMIXINTYPES.
     */
    public String getJCR_FROZENMIXINTYPES() {
        return resolveName(JCR_NS, JCR_FROZENMIXINTYPES);
    }

    /**
     * @return Returns the JCR_FROZENNODE.
     */
    public String getJCR_FROZENNODE() {
        return resolveName(JCR_NS, JCR_FROZENNODE);
    }

    /**
     * @return Returns the JCR_FROZENPRIMARYTYPE.
     */
    public String getJCR_FROZENPRIMARYTYPE() {
        return resolveName(JCR_NS, JCR_FROZENPRIMARYTYPE);
    }

    /**
     * @return Returns the JCR_FROZENUUID.
     */
    public String getJCR_FROZENUUID() {
        return resolveName(JCR_NS, JCR_FROZENUUID);
    }

    /**
     * @return Returns the JCR_HASORDERABLECHILDNODES.
     */
    public String getJCR_HASORDERABLECHILDNODES() {
        return resolveName(JCR_NS, JCR_HASORDERABLECHILDNODES);
    }

    /**
     * @return Returns the JCR_ISCHECKEDOUT.
     */
    public String getJCR_ISCHECKEDOUT() {
        return resolveName(JCR_NS, JCR_ISCHECKEDOUT);
    }

    /**
     * @return Returns the JCR_ISMIXIN.
     */
    public String getJCR_ISMIXIN() {
        return resolveName(JCR_NS, JCR_ISMIXIN);
    }

    /**
     * @return Returns the JCR_LANGUAGE.
     */
    public String getJCR_LANGUAGE() {
        return resolveName(JCR_NS, JCR_LANGUAGE);
    }

    /**
     * @return Returns the JCR_LASTMODIFIED.
     */
    public String getJCR_LASTMODIFIED() {
        return resolveName(JCR_NS, JCR_LASTMODIFIED);
    }

    /**
     * @return Returns the JCR_LOCKISDEEP.
     */
    public String getJCR_LOCKISDEEP() {
        return resolveName(JCR_NS, JCR_LOCKISDEEP);
    }

    /**
     * @return Returns the JCR_LOCKOWNER.
     */
    public String getJCR_LOCKOWNER() {
        return resolveName(JCR_NS, JCR_LOCKOWNER);
    }

    /**
     * @return Returns the JCR_MANDATORY.
     */
    public String getJCR_MANDATORY() {
        return resolveName(JCR_NS, JCR_MANDATORY);
    }

    /**
     * @return Returns the JCR_MERGEFAILED.
     */
    public String getJCR_MERGEFAILED() {
        return resolveName(JCR_NS, JCR_MERGEFAILED);
    }

    /**
     * @return Returns the JCR_MIMETYPE.
     */
    public String getJCR_MIMETYPE() {
        return resolveName(JCR_NS, JCR_MIMETYPE);
    }

    /**
     * @return Returns the JCR_MIXINTYPES.
     */
    public String getJCR_MIXINTYPES() {
        return resolveName(JCR_NS, JCR_MIXINTYPES);
    }

    /**
     * @return Returns the JCR_MULTIPLE.
     */
    public String getJCR_MULTIPLE() {
        return resolveName(JCR_NS, JCR_MULTIPLE);
    }

    /**
     * @return Returns the JCR_NAME.
     */
    public String getJCR_NAME() {
        return resolveName(JCR_NS, JCR_NAME);
    }

    /**
     * @return Returns the JCR_NODETYPENAME.
     */
    public String getJCR_NODETYPENAME() {
        return resolveName(JCR_NS, JCR_NODETYPENAME);
    }

    /**
     * @return Returns the JCR_ONPARENTVERSION.
     */
    public String getJCR_ONPARENTVERSION() {
        return resolveName(JCR_NS, JCR_ONPARENTVERSION);
    }

    /**
     * @return Returns the JCR_PATH.
     */
    public String getJCR_PATH() {
        return resolveName(JCR_NS, JCR_PATH);
    }

    /**
     * @return Returns the JCR_PREDECESSORS.
     */
    public String getJCR_PREDECESSORS() {
        return resolveName(JCR_NS, JCR_PREDECESSORS);
    }

    /**
     * @return Returns the JCR_PRIMARYITEMNAME.
     */
    public String getJCR_PRIMARYITEMNAME() {
        return resolveName(JCR_NS, JCR_PRIMARYITEMNAME);
    }

    /**
     * @return Returns the JCR_PRIMARYTYPE.
     */
    public String getJCR_PRIMARYTYPE() {
        return resolveName(JCR_NS, JCR_PRIMARYTYPE);
    }

    /**
     * @return Returns the JCR_PROPERTYDEFINITION.
     */
    public String getJCR_PROPERTYDEFINITION() {
        return resolveName(JCR_NS, JCR_PROPERTYDEFINITION);
    }

    /**
     * @return Returns the JCR_PROTECTED.
     */
    public String getJCR_PROTECTED() {
        return resolveName(JCR_NS, JCR_PROTECTED);
    }

    /**
     * @return Returns the JCR_REQUIREDPRIMARYTYPES.
     */
    public String getJCR_REQUIREDPRIMARYTYPES() {
        return resolveName(JCR_NS, JCR_REQUIREDPRIMARYTYPES);
    }

    /**
     * @return Returns the JCR_REQUIREDTYPE.
     */
    public String getJCR_REQUIREDTYPE() {
        return resolveName(JCR_NS, JCR_REQUIREDTYPE);
    }

    /**
     * @return Returns the JCR_ROOTVERSION.
     */
    public String getJCR_ROOTVERSION() {
        return resolveName(JCR_NS, JCR_ROOTVERSION);
    }

    /**
     * @return Returns the JCR_SAMENAMESIBLINGS.
     */
    public String getJCR_SAMENAMESIBLINGS() {
        return resolveName(JCR_NS, JCR_SAMENAMESIBLINGS);
    }

    /**
     * @return Returns the JCR_SCORE.
     */
    public String getJCR_SCORE() {
        return resolveName(JCR_NS, JCR_SCORE);
    }

    /**
     * @return Returns the JCR_STATEMENT.
     */
    public String getJCR_STATEMENT() {
        return resolveName(JCR_NS, JCR_STATEMENT);
    }

    /**
     * @return Returns the JCR_SUCCESSORS.
     */
    public String getJCR_SUCCESSORS() {
        return resolveName(JCR_NS, JCR_SUCCESSORS);
    }

    /**
     * @return Returns the JCR_SUPERTYPES.
     */
    public String getJCR_SUPERTYPES() {
        return resolveName(JCR_NS, JCR_SUPERTYPES);
    }

    /**
     * @return Returns the JCR_SYSTEM.
     */
    public String getJCR_SYSTEM() {
        return resolveName(JCR_NS, JCR_SYSTEM);
    }

    /**
     * @return Returns the JCR_UUID.
     */
    public String getJCR_UUID() {
        return resolveName(JCR_NS, JCR_UUID);
    }

    /**
     * @return Returns the JCR_VALUECONSTRAINTS.
     */
    public String getJCR_VALUECONSTRAINTS() {
        return resolveName(JCR_NS, JCR_VALUECONSTRAINTS);
    }

    /**
     * @return Returns the JCR_VERSIONABLEUUID.
     */
    public String getJCR_VERSIONABLEUUID() {
        return resolveName(JCR_NS, JCR_VERSIONABLEUUID);
    }

    /**
     * @return Returns the JCR_VERSIONHISTORY.
     */
    public String getJCR_VERSIONHISTORY() {
        return resolveName(JCR_NS, JCR_VERSIONHISTORY);
    }

    /**
     * @return Returns the JCR_VERSIONLABELS.
     */
    public String getJCR_VERSIONLABELS() {
        return resolveName(JCR_NS, JCR_VERSIONLABELS);
    }

    /**
     * @return Returns the JCR_VERSIONSTORAGE.
     */
    public String getJCR_VERSIONSTORAGE() {
        return resolveName(JCR_NS, JCR_VERSIONSTORAGE);
    }

    /**
     * @return Returns the MIX_LOCKABLE.
     */
    public String getMIX_LOCKABLE() {
        return resolveName(MIX_NS, MIX_LOCKABLE);
    }

    /**
     * @return Returns the MIX_REFERENCEABLE.
     */
    public String getMIX_REFERENCEABLE() {
        return resolveName(MIX_NS, MIX_REFERENCEABLE);
    }

    /**
     * @return Returns the MIX_VERSIONABLE.
     */
    public String getMIX_VERSIONABLE() {
        return resolveName(MIX_NS, MIX_VERSIONABLE);
    }

    /**
     * @return Returns the NT_BASE.
     */
    public String getNT_BASE() {
        return resolveName(NT_NS, NT_BASE);
    }

    /**
     * @return Returns the NT_CHILDNODEDEFINITION.
     */
    public String getNT_CHILDNODEDEFINITION() {
        return resolveName(NT_NS, NT_CHILDNODEDEFINITION);
    }

    /**
     * @return Returns the NT_FILE.
     */
    public String getNT_FILE() {
        return resolveName(NT_NS, NT_FILE);
    }

    /**
     * @return Returns the NT_FOLDER.
     */
    public String getNT_FOLDER() {
        return resolveName(NT_NS, NT_FOLDER);
    }

    /**
     * @return Returns the NT_FROZENNODE.
     */
    public String getNT_FROZENNODE() {
        return resolveName(NT_NS, NT_FROZENNODE);
    }

    /**
     * @return Returns the NT_HIERARCHYNODE.
     */
    public String getNT_HIERARCHYNODE() {
        return resolveName(NT_NS, NT_HIERARCHYNODE);
    }

    /**
     * @return Returns the NT_LINKEDFILE.
     */
    public String getNT_LINKEDFILE() {
        return resolveName(NT_NS, NT_LINKEDFILE);
    }

    /**
     * @return Returns the NT_NODETYPE.
     */
    public String getNT_NODETYPE() {
        return resolveName(NT_NS, NT_NODETYPE);
    }

    /**
     * @return Returns the NT_PROPERTYDEFINITION.
     */
    public String getNT_PROPERTYDEFINITION() {
        return resolveName(NT_NS, NT_PROPERTYDEFINITION);
    }

    /**
     * @return Returns the NT_QUERY.
     */
    public String getNT_QUERY() {
        return resolveName(NT_NS, NT_QUERY);
    }

    /**
     * @return Returns the NT_RESOURCE.
     */
    public String getNT_RESOURCE() {
        return resolveName(NT_NS, NT_RESOURCE);
    }

    /**
     * @return Returns the NT_UNSTRUCTURED.
     */
    public String getNT_UNSTRUCTURED() {
        return resolveName(NT_NS, NT_UNSTRUCTURED);
    }

    /**
     * @return Returns the NT_VERSION.
     */
    public String getNT_VERSION() {
        return resolveName(NT_NS, NT_VERSION);
    }

    /**
     * @return Returns the NT_VERSIONEDCHILD.
     */
    public String getNT_VERSIONEDCHILD() {
        return resolveName(NT_NS, NT_VERSIONEDCHILD);
    }

    /**
     * @return Returns the NT_VERSIONHISTORY.
     */
    public String getNT_VERSIONHISTORY() {
        return resolveName(NT_NS, NT_VERSIONHISTORY);
    }

    /**
     * @return Returns the NT_VERSIONLABELS.
     */
    public String getNT_VERSIONLABELS() {
        return resolveName(NT_NS, NT_VERSIONLABELS);
    }
}
