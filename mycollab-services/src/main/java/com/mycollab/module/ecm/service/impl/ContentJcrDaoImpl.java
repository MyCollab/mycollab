/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.ecm.service.impl;

import com.mycollab.core.MyCollabException;
import com.mycollab.core.UserInvalidInputException;
import com.mycollab.core.utils.FileUtils;
import com.mycollab.core.utils.MimeTypesUtil;
import com.mycollab.module.ecm.ContentException;
import com.mycollab.module.ecm.NodesUtil;
import com.mycollab.module.ecm.domain.Content;
import com.mycollab.module.ecm.domain.Folder;
import com.mycollab.module.ecm.domain.Resource;
import com.mycollab.module.ecm.service.ContentJcrDao;
import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.commons.JcrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.extensions.jcr.JcrCallback;
import org.springframework.extensions.jcr.JcrTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.jcr.*;
import javax.jcr.nodetype.NodeType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class ContentJcrDaoImpl implements ContentJcrDao {
    private static final Logger LOG = LoggerFactory.getLogger(ContentJcrDaoImpl.class);

    @Qualifier("jcrTemplate")
    @Autowired
    private JcrTemplate jcrTemplate;

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void saveContent(final Content content, final String createdUser) {
        LOG.debug("Save content {} {}", content, jcrTemplate);
        jcrTemplate.execute(new JcrCallback() {

            @Override
            public Object doInJcr(Session session) throws IOException, RepositoryException {
                Node rootNode = session.getRootNode();
                Node node = getNode(rootNode, content.getPath());
                // forward to current path
                if (node != null) {
                    if (isNodeFolder(node)) {
                        String errorStr = String.format("Resource is existed. Search node is not a folder. It has path %s and type is %s",
                                node.getPath(), node.getPrimaryNodeType().getName());
                        throw new ContentException(errorStr);
                    } else if (isNodeContent(node)) {
                        LOG.debug("Found existing resource. Override");
                        convertContentToNode(content, node, createdUser);
                        session.save();
                        return null;
                    } else {
                        String errorStr = String.format("Resource is existed. But its node type is not mycollab:content. It has path %s and type is %s",
                                node.getPath(), node.getPrimaryNodeType().getName());
                        throw new ContentException(errorStr);
                    }
                } else {

                    String path = content.getPath();
                    String[] pathStr = path.split("/");
                    Node parentNode = rootNode;
                    // create the folder node
                    for (int i = 0; i < pathStr.length - 1; i++) {
                        if (!FileUtils.isValidFileName(pathStr[i])) {
                            throw new UserInvalidInputException("Invalid file name: " + path);
                        }

                        // move to lastest node of the path
                        Node childNode = getNode(parentNode, pathStr[i]);
                        if (childNode != null) {
                            if (!isNodeFolder(childNode)) {
                                // node must is folder
                                String errorString = "Invalid path. User want to create a content has path %s but there is a content has path %s. This node has type %s";
                                throw new ContentException(String.format(errorString, path, childNode.getPath(),
                                        childNode.getPrimaryNodeType().getName()));
                            }
                        } else {
                            // add node
                            childNode = JcrUtils.getOrAddNode(parentNode, pathStr[i], "mycollab:folder");
                            childNode.setProperty("mycollab:createdUser", createdUser);
                        }
                        parentNode = childNode;
                    }

                    String nodeName = pathStr[pathStr.length - 1];
                    Node addNode = parentNode.addNode(nodeName, "{http://www.esofthead.com/mycollab}content");
                    addNode.addMixin(NodeType.MIX_LAST_MODIFIED);
                    addNode.addMixin(NodeType.MIX_TITLE);

                    convertContentToNode(content, addNode, createdUser);
                    session.save();

                }
                return null;
            }
        });
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void createFolder(final Folder folder, final String createdUser) {
        LOG.debug("Save content {} {}", folder, jcrTemplate);
        jcrTemplate.execute(new JcrCallback() {

            @Override
            public Object doInJcr(Session session) throws IOException,
                    RepositoryException {
                try {
                    String path = folder.getPath();
                    Node rootNode = session.getRootNode();
                    String[] pathStr = path.split("/");
                    Node parentNode = rootNode;
                    // create folder note
                    for (String aPathStr : pathStr) {
                        if ("".equals(aPathStr)) {
                            continue;
                        }
                        // move to lastest node of the path
                        Node childNode = getNode(parentNode, aPathStr);
                        if (childNode != null) {
                            LOG.debug("Found node with path {} in sub node ", aPathStr, parentNode.getPath());
                            if (!isNodeFolder(childNode)) {
                                // node must be the folder
                                String errorString = "Invalid path. User want to create folder has path %s but there is a content has path %s";
                                throw new ContentException(String.format(errorString, folder.getPath(), childNode.getPath()));
                            } else {
                                LOG.debug("Found folder node {}", childNode.getPath());
                            }
                        } else { // add node
                            LOG.debug("Create new folder {} of sub node {}", aPathStr, parentNode.getPath());
                            childNode = JcrUtils.getOrAddNode(parentNode, aPathStr, "mycollab:folder");
                            childNode.setProperty("mycollab:createdUser", createdUser);
                            childNode.setProperty("jcr:description", folder.getDescription());
                            session.save();
                        }

                        parentNode = childNode;
                    }

                    LOG.debug("Node path {} is existed {}", path, (getNode(rootNode, path) != null));
                } catch (Exception e) {
                    String errorString = "Error while create folder with path %s";
                    throw new MyCollabException(String.format(errorString, folder.getPath()), e);
                }
                return null;
            }
        });
    }

    private static boolean isNodeFolder(Node node) {
        try {
            return node.isNodeType("mycollab:folder");
        } catch (RepositoryException e) {
            return false;
        }
    }

    private static boolean isNodeContent(Node node) {
        try {
            return node.isNodeType("mycollab:content");
        } catch (RepositoryException e) {
            return false;
        }
    }

    private static Node getNode(Node node, String path) {
        try {
            return node.getNode(path);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Resource getResource(final String path) {
        return jcrTemplate.execute(new JcrCallback<Resource>() {
            @Override
            public Resource doInJcr(Session session) throws IOException, RepositoryException {
                Node rootNode = session.getRootNode();
                Node node = getNode(rootNode, path);

                if (node != null) {
                    if (isNodeContent(node)) {
                        return convertNodeToContent(node);
                    } else if (isNodeFolder(node)) {
                        return convertNodeToFolder(node);
                    } else {
                        throw new MyCollabException("Resource does not have type be mycollab:folder or mycollab:content. Its path is " + node.getPath());
                    }
                }
                return null;
            }
        });
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void removeResource(final String path) {
        jcrTemplate.execute(new JcrCallback() {
            @Override
            public Content doInJcr(Session session) throws IOException, RepositoryException {
                Node rootNode = session.getRootNode();
                Node node = getNode(rootNode, path);

                if (node != null) {
                    node.remove();
                    session.save();
                }
                return null;
            }
        });

    }

    @Override
    public List<Resource> getResources(final String path) {
        return jcrTemplate.execute(new JcrCallback<List<Resource>>() {

            @Override
            public List<Resource> doInJcr(Session session) throws IOException, RepositoryException {
                Node rootNode = session.getRootNode();
                Node node = getNode(rootNode, path);
                if (node != null) {
                    if (isNodeFolder(node)) {
                        List<Resource> resources = new ArrayList<>();
                        NodeIterator childNodes = node.getNodes();
                        while (childNodes.hasNext()) {
                            Node childNode = childNodes.nextNode();
                            if (isNodeFolder(childNode)) {
                                Folder subFolder = convertNodeToFolder(childNode);
                                resources.add(subFolder);
                            } else if (isNodeContent(childNode)) {
                                Content content = convertNodeToContent(childNode);
                                resources.add(content);
                            } else {
                                String errorString = "Node %s has type not mycollab:content or mycollab:folder";
                                LOG.error(String.format(errorString, childNode.getPath()));
                            }
                        }

                        return resources;
                    } else {
                        throw new ContentException("Do not support any node type except mycollab:folder. The current node has type "
                                + node.getPrimaryNodeType().getName());
                    }
                }

                LOG.debug("There is no resource in path {}", path);
                return null;
            }
        });
    }

    @Override
    public List<Content> getContents(final String path) {
        return jcrTemplate.execute(new JcrCallback<List<Content>>() {

            @Override
            public List<Content> doInJcr(Session session) throws IOException, RepositoryException {
                Node rootNode = session.getRootNode();
                Node node = getNode(rootNode, path);
                if (node != null) {
                    if (isNodeFolder(node)) {
                        List<Content> resources = new ArrayList<>();
                        NodeIterator childNodes = node.getNodes();
                        while (childNodes.hasNext()) {
                            Node childNode = childNodes.nextNode();
                            if (isNodeContent(childNode)) {
                                Content content = convertNodeToContent(childNode);
                                resources.add(content);
                            }
                        }

                        return resources;
                    } else {
                        throw new ContentException("Do not support any node type except mycollab:folder. The current node has type: "
                                + node.getPrimaryNodeType().getName() + " and its path is " + path);
                    }
                }
                return null;
            }
        });
    }

    @Override
    public List<Folder> getSubFolders(final String path) {
        return jcrTemplate.execute(new JcrCallback<List<Folder>>() {

            @Override
            public List<Folder> doInJcr(Session session) throws IOException, RepositoryException {
                Node rootNode = session.getRootNode();
                Node node = getNode(rootNode, path);
                if (node != null) {
                    if (node.isNodeType("mycollab:folder")) {
                        List<Folder> folders = new ArrayList<>();
                        NodeIterator childNodes = node.getNodes();
                        while (childNodes.hasNext()) {
                            Node childNode = (Node) childNodes.next();
                            if (isNodeFolder(childNode)) {
                                Folder subFolder = convertNodeToFolder(childNode);
                                folders.add(subFolder);
                            }

                        }
                        return folders;
                    } else {
                        throw new ContentException("Do not support any node type except mycollab:folder. The current node has type "
                                + node.getPrimaryNodeType().getName());
                    }
                }

                return null;
            }
        });
    }

    private static void convertContentToNode(Content content, Node node, String createdUser) {
        try {
            node.setProperty("jcr:title", content.getTitle());
            node.setProperty("jcr:description", content.getDescription());
            node.setProperty("mycollab:createdUser", createdUser);
            if (StringUtils.isNotBlank(content.getThumbnail())) {
                node.setProperty("mycollab:thumbnailPath", content.getThumbnail());
            }

            node.setProperty("mycollab:lastModifiedUser", createdUser);
            node.setProperty("mycollab:size", content.getSize());
        } catch (Exception e) {
            throw new MyCollabException(e);
        }
    }

    private static Content convertNodeToContent(Node node) {
        try {
            String contentPath = node.getPath();
            if (contentPath.startsWith("/")) {
                contentPath = contentPath.substring(1);
            }

            Content content = new Content(contentPath);
            content.setCreated(node.getProperty("jcr:created").getDate());
            content.setCreatedBy(NodesUtil.getString(node, "jcr:createdBy"));
            content.setTitle(NodesUtil.getString(node, "jcr:title"));
            content.setDescription(NodesUtil.getString(node, "jcr:description"));
            content.setThumbnail(NodesUtil.getString(node, "mycollab:thumbnailPath"));
            content.setMimeType(NodesUtil.getString(node, "mycollab:mimeType", MimeTypesUtil.BINARY_MIME_TYPE));
            content.setSize(node.getProperty("mycollab:size").getLong());
            content.setCreatedUser(NodesUtil.getString(node, "mycollab:createdUser"));
            content.setLastModified(node.getProperty("jcr:lastModified").getDate());
            return content;
        } catch (Exception e) {
            throw new MyCollabException(e);
        }
    }

    private static Folder convertNodeToFolder(Node node) {
        try {
            String folderPath = node.getPath();
            if (folderPath.startsWith("/")) {
                folderPath = folderPath.substring(1);
            }

            Folder folder = new Folder(folderPath);
            folder.setCreated(node.getProperty("jcr:created").getDate());
            folder.setCreatedBy(node.getProperty("jcr:createdBy").getString());
            folder.setDescription(NodesUtil.getString(node, "jcr:description"));
            folder.setCreatedUser(node.getProperty("mycollab:createdUser").getString());
            return folder;
        } catch (Exception e) {
            throw new MyCollabException(e);
        }
    }

    @Override
    public List<Resource> searchResourcesByName(final String baseFolderPath, final String resourceName) {
        return jcrTemplate.execute(new JcrCallback<List<Resource>>() {

            @Override
            public List<Resource> doInJcr(Session session) throws IOException, RepositoryException {
                return new ArrayList<>();
            }
        });
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void rename(final String oldPath, final String newPath) {
        LOG.debug("Rename content {} {}", oldPath, newPath);
        jcrTemplate.execute(new JcrCallback() {
            @Override
            public Object doInJcr(Session session) throws IOException, RepositoryException {
                Node rootNode = session.getRootNode();
                Node currentNode = getNode(rootNode, oldPath);
                if (getNode(rootNode, newPath) != null) {
                    throw new ContentException("Folder/file has already existed: " + newPath);
                }
                if (currentNode != null) {
                    currentNode.getSession().move(currentNode.getPath(), "/" + newPath);
                    currentNode.getSession().save();
                } else {
                    throw new MyCollabException("Resource path " + oldPath + " not found");
                }
                return null;
            }
        });
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void moveResource(final String oldPath, final String destinationPath) {
        jcrTemplate.execute(new JcrCallback() {

            @Override
            public Object doInJcr(Session session) throws IOException, RepositoryException {
                try {
                    int index = destinationPath.lastIndexOf("/");
                    if (index >= 0) {
                        String parentDestPath = destinationPath.substring(0, index);
                        Folder folder = new Folder(parentDestPath);
                        createFolder(folder, "");
                    }
                    session.move("/" + oldPath, "/" + destinationPath);
                    session.save();
                } catch (ItemExistsException e) {
                    throw new UserInvalidInputException("Please check duplicate file/folder before move.", e);
                } catch (Exception e) {
                    throw new MyCollabException("Illegal move source to destination.", e);
                }
                return null;
            }
        });
    }
}