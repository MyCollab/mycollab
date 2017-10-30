/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.page.service.impl;

import com.mycollab.common.i18n.WikiI18nEnum;
import com.mycollab.core.MyCollabException;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.module.ecm.ContentException;
import com.mycollab.module.ecm.NodesUtil;
import com.mycollab.module.page.domain.Folder;
import com.mycollab.module.page.domain.Page;
import com.mycollab.module.page.domain.PageResource;
import com.mycollab.module.page.domain.PageVersion;
import com.mycollab.module.page.service.PageService;
import org.apache.jackrabbit.commons.JcrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.extensions.jcr.JcrCallback;
import org.springframework.extensions.jcr.JcrTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.nodetype.NodeType;
import javax.jcr.version.Version;
import javax.jcr.version.VersionHistory;
import javax.jcr.version.VersionIterator;
import javax.jcr.version.VersionManager;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
@Repository
@Transactional
public class PageServiceImpl implements PageService {
    private static final Logger LOG = LoggerFactory.getLogger(PageServiceImpl.class);

    @Qualifier("pageJcrTemplate")
    @Autowired
    private JcrTemplate jcrTemplate;

    @Override
    public void savePage(final Page page, final String createdUser) {
        jcrTemplate.execute((JcrCallback) session -> {
            page.setCreatedTime(new GregorianCalendar());
            page.setCreatedUser(createdUser);
            page.setLastUpdatedTime(new GregorianCalendar());
            page.setLastUpdatedUser(createdUser);
            Node rootNode = session.getRootNode();
            Node node = JcrUtils.getNodeIfExists(rootNode, page.getPath());
            // forward to current path
            if (node != null) {
                if (isNodeFolder(node)) {
                    String errorStr = String.format("Resource is existed. Search node is not a folder. It has path %s and type is %s",
                            node.getPath(), node.getPrimaryNodeType().getName());
                    throw new ContentException(errorStr);
                } else if (isNodePage(node)) {
                    LOG.debug("Found existing resource. Override");
                    VersionManager vm = session.getWorkspace().getVersionManager();
                    vm.checkout("/" + page.getPath());
                    convertPageToNode(node, page, createdUser);
                    session.save();
                    vm.checkin("/" + page.getPath());
                } else {
                    String errorStr = String.format("Resource is existed. But its node type is not mycollab:content. It has path %s and type is %s",
                            node.getPath(), node.getPrimaryNodeType().getName());
                    throw new ContentException(errorStr);
                }
            } else {
                try {
                    String path = page.getPath();
                    String[] pathStr = path.split("/");
                    Node parentNode = rootNode;
                    // create folder note
                    for (int i = 0; i < pathStr.length - 1; i++) {
                        // move to lastest node of the path
                        Node childNode = JcrUtils.getNodeIfExists(parentNode, pathStr[i]);
                        if (childNode != null) {
                            if (!isNodeFolder(childNode)) {
                                // node must is folder
                                String errorString = "Invalid path. User want to create a content has path %s but there is a folder has path %s";
                                throw new ContentException(String.format(errorString, page.getPath(), childNode.getPath()));
                            }
                        } else {
                            // add node
                            childNode = parentNode.addNode(pathStr[i], "{http://www.esofthead.com/wiki}folder");
                            childNode.setProperty("wiki:createdUser", createdUser);
                            childNode.setProperty("wiki:name", pathStr[i]);
                            childNode.setProperty("wiki:description", "");
                        }
                        parentNode = childNode;
                    }

                    Node addNode = parentNode.addNode(pathStr[pathStr.length - 1], "{http://www.esofthead.com/wiki}page");
                    convertPageToNode(addNode, page, createdUser);
                    session.save();
                } catch (Exception e) {
                    LOG.error("error in convertToNode Method", e);
                    throw new MyCollabException(e);
                }
            }
            return null;
        });

    }

    @Override
    public Page getPage(final String path, final String requestedUser) {
        return jcrTemplate.execute(session -> {
            Node rootNode = session.getRootNode();
            Node node = JcrUtils.getNodeIfExists(rootNode, path);
            if (node != null) {
                if (isNodePage(node)) {
                    if (isAccessible(node, requestedUser)) {
                        return convertNodeToPage(node);
                    } else {
                        return null;
                    }
                }
            }

            return null;
        });
    }

    @Override
    public Folder getFolder(final String path) {
        return jcrTemplate.execute(session -> {
            Node rootNode = session.getRootNode();
            Node node = JcrUtils.getNodeIfExists(rootNode, path);
            if (node != null) {
                if (isNodeFolder(node)) {
                    return convertNodeToFolder(node);
                }
            }

            return null;
        });
    }

    private static boolean isNodeFolder(Node node) {
        try {
            return node.isNodeType("wiki:folder");
        } catch (RepositoryException e) {
            return false;
        }
    }

    private static boolean isNodePage(Node node) {
        try {
            return node.isNodeType("wiki:page");
        } catch (RepositoryException e) {
            return false;
        }
    }

    @Override
    public List<PageVersion> getPageVersions(final String path) {
        return jcrTemplate.execute(session -> {
            Node rootNode = session.getRootNode();
            Node node = JcrUtils.getNodeIfExists(rootNode, path);
            if (node != null) {
                VersionManager vm = session.getWorkspace().getVersionManager();
                VersionHistory history = vm.getVersionHistory("/" + path);
                List<PageVersion> versions = new ArrayList<>();
                for (VersionIterator it = history.getAllVersions(); it.hasNext(); ) {
                    Version version = (Version) it.next();
                    if (!"jcr:rootVersion".equals(version.getName())) {
                        versions.add(convertNodeToPageVersion(version));
                    }
                }
                return versions;
            } else {
                return null;
            }
        });
    }

    @Override
    public Page restorePage(final String path, final String versionName) {
        return jcrTemplate.execute(session -> {
            Node rootNode = session.getRootNode();
            Node node = JcrUtils.getNodeIfExists(rootNode, path);
            if (node != null) {
                VersionManager vm = session.getWorkspace().getVersionManager();
                try {
                    vm.restore("/" + path, versionName, true);
                    node = JcrUtils.getNodeIfExists(rootNode, path);
                    return convertNodeToPage(node);
                } catch (Exception e) {
                    LOG.error("Error when restore document {} to version {}", path, versionName, e);
                }
            }
            return null;
        });

    }

    private PageVersion convertNodeToPageVersion(Version node) {
        try {
            PageVersion version = new PageVersion();
            version.setName(node.getName());
            version.setIndex(node.getIndex());
            version.setCreatedTime(node.getCreated());
            return version;
        } catch (Exception e) {
            LOG.error("Error while get detail node version");
            throw new MyCollabException(e);
        }
    }

    @Override
    public Page getPageByVersion(final String path, final String versionName) {
        return jcrTemplate.execute(session -> {
            Node rootNode = session.getRootNode();
            Node node = JcrUtils.getNodeIfExists(rootNode, path);
            if (node != null) {
                VersionManager vm = session.getWorkspace().getVersionManager();
                VersionHistory history = vm.getVersionHistory("/" + path);
                Version version = history.getVersion(versionName);
                if (version != null) {
                    Node frozenNode = version.getFrozenNode();
                    return convertNodeToPage(frozenNode);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        });
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void removeResource(final String path) {
        jcrTemplate.execute(session -> {
            Node rootNode = session.getRootNode();
            if ("".equals(path) || "/".equals(path)) {
                NodeIterator nodes = rootNode.getNodes();
                while (nodes.hasNext()) {
                    Node node = nodes.nextNode();
                    if (isNodeFolder(node) || isNodePage(node)) {
                        node.remove();
                    }
                }
                session.save();
            } else {
                Node node = JcrUtils.getNodeIfExists(rootNode, path);
                if (node != null && (isNodeFolder(node) || isNodePage(node))) {
                    node.remove();
                    session.save();
                }
            }

            return null;
        });
    }

    @Override
    public List<Page> getPages(final String path, final String requestedUser) {
        return jcrTemplate.execute(session -> {
            Node rootNode = session.getRootNode();
            Node node = JcrUtils.getNodeIfExists(rootNode, path);
            if (node != null) {
                if (isNodeFolder(node)) {
                    List<Page> pages = new ArrayList<>();
                    NodeIterator childNodes = node.getNodes();
                    while (childNodes.hasNext()) {
                        Node childNode = childNodes.nextNode();
                        if (isNodePage(childNode)) {
                            if (isAccessible(childNode, requestedUser)) {
                                Page page = convertNodeToPage(childNode);
                                pages.add(page);
                            }
                        }
                    }

                    return pages;
                } else {
                    throw new ContentException(String.format("Do not support any node type except mycollab:folder. The current node has type: %s and its path is %s",
                            node.getPrimaryNodeType().getName(), path));
                }
            }
            return new ArrayList<Page>();
        });
    }

    @Override
    public List<PageResource> getResources(final String path, final String requestedUser) {
        return jcrTemplate.execute(session -> {
            Node rootNode = session.getRootNode();
            Node node = JcrUtils.getNodeIfExists(rootNode, path);
            if (node != null) {
                if (isNodeFolder(node)) {
                    List<PageResource> resources = new ArrayList<>();
                    NodeIterator childNodes = node.getNodes();
                    while (childNodes.hasNext()) {
                        Node childNode = childNodes.nextNode();
                        if (isNodeFolder(childNode)) {
                            Folder subFolder = convertNodeToFolder(childNode);
                            resources.add(subFolder);
                        } else if (isNodePage(childNode)) {
                            if (isAccessible(childNode, requestedUser)) {
                                Page page = convertNodeToPage(childNode);
                                resources.add(page);
                            }
                        } else {
                            String errorString = "Node %s has type not mycollab:content or mycollab:folder";
                            LOG.error(String.format(errorString, childNode.getPath()));
                        }
                    }

                    return resources;
                } else {
                    throw new ContentException(
                            "Do not support any node type except mycollab:folder. The current node has type "
                                    + node.getPrimaryNodeType().getName());
                }
            }

            LOG.debug("There is no resource in path {}", path);
            return new ArrayList<PageResource>();
        });
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void createFolder(final Folder folder, final String createdUser) {
        jcrTemplate.execute(session -> {
            try {
                Node rootNode = session.getRootNode();
                String folderPath = folder.getPath();
                String[] pathStr = folderPath.split("/");
                Node parentNode = rootNode;
                // create folder note
                for (int i = 0; i < pathStr.length; i++) {
                    if ("".equals(pathStr[i])) {
                        continue;
                    }
                    // move to lastest node of the path
                    Node childNode = JcrUtils.getNodeIfExists(parentNode, pathStr[i]);
                    if (childNode != null) {
                        LOG.debug("Found node with path {} in sub node ", pathStr[i], parentNode.getPath());
                        if (!isNodeFolder(childNode)) {
                            // node must be the folder
                            String errorString = "Invalid path. User want to create folder has path %s but there is a content has path %s";
                            throw new ContentException(String.format(errorString, folderPath, childNode.getPath()));
                        } else {
                            LOG.debug("Found folder node {}", childNode.getPath());

                            if (i == pathStr.length - 1) {
                                childNode.setProperty("wiki:createdUser", createdUser);
                                childNode.setProperty("wiki:description", StringUtils.getStrOptionalNullValue(folder.getDescription()));
                                childNode.setProperty("wiki:name", folder.getName());
                                session.save();
                            }
                        }
                    } else { // add node
                        LOG.debug("Create new folder {} of sub node {}", pathStr[i], parentNode.getPath());
                        childNode = parentNode.addNode(pathStr[i], "{http://www.esofthead.com/wiki}folder");
                        childNode.setProperty("wiki:createdUser", createdUser);
                        childNode.setProperty("wiki:description", StringUtils.getStrOptionalNullValue(folder.getDescription()));
                        childNode.setProperty("wiki:name", folder.getName());
                        session.save();
                    }

                    parentNode = childNode;
                }

                LOG.debug("Node path {} is existed {}", folderPath,
                        (JcrUtils.getNodeIfExists(rootNode, folderPath) != null));
            } catch (Exception e) {
                String errorString = "Error while create folder with path %s";
                throw new MyCollabException(String.format(errorString, folder.getPath()), e);
            }
            return null;
        });
    }

    private static Node convertPageToNode(Node node, Page page, String createdUser) {
        try {
            node.addMixin(NodeType.MIX_VERSIONABLE);

            node.setProperty("wiki:subject", page.getSubject());
            node.setProperty("wiki:content", page.getContent());
            node.setProperty("wiki:status", page.getStatus());
            node.setProperty("wiki:category", page.getCategory());
            node.setProperty("wiki:isLock", page.isLock());
            node.setProperty("wiki:createdUser", createdUser);
            return node;
        } catch (Exception e) {
            throw new MyCollabException(e);
        }
    }

    private static boolean isAccessible(Node node, String requestedUser) {
        String status = NodesUtil.getString(node, "wiki:status");
        String createdUser = NodesUtil.getString(node, "wiki:createdUser");
        return !WikiI18nEnum.status_private.name().equals(status) || (requestedUser.equals(createdUser));

    }

    private Page convertNodeToPage(Node node) {
        try {
            Page page = new Page();
            String contentPath = node.getPath();
            if (contentPath.startsWith("/")) {
                contentPath = contentPath.substring(1);
            }
            page.setPath(contentPath);
            page.setSubject(NodesUtil.getString(node, "wiki:subject"));
            page.setContent(NodesUtil.getString(node, "wiki:content"));
            page.setLock(node.getProperty("wiki:isLock").getBoolean());
            page.setStatus(NodesUtil.getString(node, "wiki:status"));
            page.setCategory(NodesUtil.getString(node, "wiki:category"));
            page.setCreatedTime(node.getProperty("jcr:created").getDate());
            page.setCreatedUser(NodesUtil.getString(node, "wiki:createdUser"));
            page.setNew(false);
            page.setLastUpdatedTime(page.getCreatedTime());
            page.setLastUpdatedUser(page.getCreatedUser());
            return page;
        } catch (Exception e) {
            throw new MyCollabException(e);
        }
    }

    private Folder convertNodeToFolder(Node node) {
        try {
            Folder folder = new Folder();
            folder.setCreatedTime(node.getProperty("jcr:created").getDate());
            folder.setCreatedUser(node.getProperty("wiki:createdUser").getString());
            if (node.hasProperty("wiki:description")) {
                folder.setDescription(node.getProperty("wiki:description").getString());
            } else {
                folder.setDescription("");
            }

            folder.setName(node.getProperty("wiki:name").getString());

            String folderPath = node.getPath();
            if (folderPath.startsWith("/")) {
                folderPath = folderPath.substring(1);
            }
            folder.setPath(folderPath);
            return folder;
        } catch (Exception e) {
            throw new MyCollabException(e);
        }
    }
}
