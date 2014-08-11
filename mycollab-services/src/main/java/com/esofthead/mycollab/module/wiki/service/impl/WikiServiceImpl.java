package com.esofthead.mycollab.module.wiki.service.impl;

import java.io.IOException;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.nodetype.NodeType;

import org.apache.jackrabbit.commons.JcrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.extensions.jcr.JcrCallback;
import org.springframework.extensions.jcr.JcrTemplate;
import org.springframework.stereotype.Service;

import com.esofthead.mycollab.module.ecm.ContentException;
import com.esofthead.mycollab.module.wiki.domain.Page;
import com.esofthead.mycollab.module.wiki.service.WikiService;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.4.0
 *
 */
@Service
public class WikiServiceImpl implements WikiService {
	private static Logger log = LoggerFactory.getLogger(WikiServiceImpl.class);

	@Autowired
	private JcrTemplate pageJcrTemplate;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void savePage(final Page page, final String createdUser) {
		pageJcrTemplate.execute(new JcrCallback() {

			@Override
			public Object doInJcr(Session session) throws IOException,
					RepositoryException {
				Node rootNode = session.getRootNode();
				Node node = getNode(rootNode, page.getPath());
				// forward to current path
				if (node != null) {
					if (isNodeFolder(node)) {
						String errorStr = String
								.format("Resource is existed. Search node is not a folder. It has path %s and type is %s",
										node.getPath(), node
												.getPrimaryNodeType().getName());
						throw new ContentException(errorStr);
					} else if (isNodePage(node)) {
						log.debug("Found existing resource. Override");

					} else {
						String errorStr = String
								.format("Resource is existed. But its node type is not mycollab:content. It has path %s and type is %s",
										node.getPath(), node
												.getPrimaryNodeType().getName());
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
							Node childNode = getNode(parentNode, pathStr[i]);
							if (childNode != null) {
								if (!isNodeFolder(childNode)) {
									// node must is folder
									String errorString = "Invalid path. User want to create a content has path %s but there is a folder has path %s";
									throw new ContentException(String.format(
											errorString, page.getPath(),
											childNode.getPath()));
								}
							} else {
								// add node
								childNode = JcrUtils.getOrAddNode(parentNode,
										pathStr[i], "wiki:folder");
								childNode.setProperty("wiki:createdUser",
										createdUser);
							}
							parentNode = childNode;
						}

						Node addNode = parentNode.addNode(
								pathStr[pathStr.length - 1],
								"{http://www.esofthead.com/wiki}page");
						addNode.addMixin(NodeType.MIX_LAST_MODIFIED);
						addNode.addMixin(NodeType.MIX_VERSIONABLE);

						addNode.setProperty("wiki:subject", page.getSubject());
						addNode.setProperty("wiki:content", page.getContent());
						addNode.setProperty("status", page.getStatus());
						addNode.setProperty("wiki:createdUser", createdUser);
						addNode.setProperty("jcr:lastModifiedBy", createdUser);
						session.save();
					} catch (Exception e) {
						log.error("error in convertToNode Method", e);
					}
				}
				return null;
			}
		});

	}

	private static Node getNode(Node node, String path) {
		try {
			return node.getNode(path);
		} catch (Exception e) {
			return null;
		}
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
}
