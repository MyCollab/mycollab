package db.migration;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.collections.CollectionUtils;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.esofthead.mycollab.core.utils.ImageUtil;
import com.esofthead.mycollab.core.utils.MimeTypesUtil;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.ecm.domain.Content;
import com.esofthead.mycollab.module.ecm.domain.Folder;
import com.esofthead.mycollab.module.ecm.domain.Resource;
import com.esofthead.mycollab.module.ecm.service.ContentJcrDao;
import com.esofthead.mycollab.module.ecm.service.ResourceService;
import com.esofthead.mycollab.module.file.service.RawContentService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;

public class V20141027_1__Generate_Image_Thumbnails implements
		SpringJdbcMigration {

	private static final Logger LOG = LoggerFactory
			.getLogger(V20141027_1__Generate_Image_Thumbnails.class);

	@Override
	public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
		List<Map<String, Object>> accounts = jdbcTemplate
				.queryForList("SELECT id FROM s_account ");
		for (Map<String, Object> row : accounts) {
			Integer accountId = (Integer) row.get("id");
			generateImageThumbnail(accountId, accountId + "");
		}
	}

	private void generateImageThumbnail(Integer accountId, String path) {
		ResourceService resourceService = ApplicationContextUtil
				.getSpringBean(ResourceService.class);

		RawContentService rawContentService = ApplicationContextUtil
				.getSpringBean(RawContentService.class);

		ContentJcrDao contentJcrDao = ApplicationContextUtil
				.getSpringBean(ContentJcrDao.class);

		List<Resource> resources = resourceService.getResources(path);
		if (CollectionUtils.isNotEmpty(resources)) {
			for (Resource resource : resources) {
				if (resource instanceof Content) {
					Content content = (Content) resource;
					String mimeType = MimeTypesUtil.detectMimeType(content
							.getPath());
					LOG.info("Check mimetype " + mimeType + " of content "
							+ content.getPath() + "--" + content.getThumbnail()
							+ ".");
					if (MimeTypesUtil.isImage(mimeType)) {
						try {
							BufferedImage image = ImageUtil
									.generateImageThumbnail(resourceService
											.getContentStream(resource
													.getPath()));
							String thumbnailPath = String.format(
									".thumbnail/%d/%s.%s", accountId,
									StringUtils.generateSoftUniqueId(), "png");
							content.setThumbnail(thumbnailPath);
							contentJcrDao.saveContent(content, "");

							File tmpFile = File.createTempFile("tmp", "png");
							ImageIO.write(image, "png", new FileOutputStream(
									tmpFile));

							rawContentService.saveContent(thumbnailPath,
									new FileInputStream(tmpFile));
						} catch (Exception e) {
							LOG.error(
									"Generate thumbnal is failed "
											+ resource.getPath(), e);
						}
					}
				} else if (resource instanceof Folder) {
					generateImageThumbnail(accountId, resource.getPath());
				}
			}
		}
	}
}