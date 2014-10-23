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
package com.esofthead.mycollab.form.service.ibatis;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.cache.CacheKey;
import com.esofthead.mycollab.core.utils.JsonDeSerializer;
import com.esofthead.mycollab.form.dao.FormSectionFieldMapper;
import com.esofthead.mycollab.form.dao.FormSectionMapper;
import com.esofthead.mycollab.form.dao.FormSectionMapperExt;
import com.esofthead.mycollab.form.domain.FormSection;
import com.esofthead.mycollab.form.domain.FormSectionExample;
import com.esofthead.mycollab.form.domain.FormSectionField;
import com.esofthead.mycollab.form.domain.SimpleFormSection;
import com.esofthead.mycollab.form.service.MasterFormService;
import com.esofthead.mycollab.form.view.builder.type.AbstractDynaField;
import com.esofthead.mycollab.form.view.builder.type.DynaForm;
import com.esofthead.mycollab.form.view.builder.type.DynaSection;
import com.esofthead.mycollab.form.view.builder.type.DynaSection.LayoutType;

@Service
public class MasterFormServiceImpl implements MasterFormService {

	private static Logger log = LoggerFactory
			.getLogger(MasterFormServiceImpl.class);

	private static final String TYPE_PACKAGE = "com.esofthead.mycollab.form.view.builder.type.";

	@Autowired
	private FormSectionMapper formSectionMapper;

	@Autowired
	private FormSectionMapperExt formSectionMapperExt;

	@Autowired
	private FormSectionFieldMapper formSectionFieldMapper;

	@SuppressWarnings({ "unchecked" })
	@Override
	public DynaForm findCustomForm(@CacheKey Integer sAccountId,
			String moduleName) {
		List<SimpleFormSection> sections = formSectionMapperExt.findSections(
				sAccountId, moduleName);

		if (CollectionUtils.isEmpty(sections)) {
			return null;
		} else {
			DynaForm form = new DynaForm();

			for (SimpleFormSection section : sections) {
				DynaSection dySection = new DynaSection();

				dySection
						.setLayoutType(LayoutType.from(section.getLayouttype()));

				dySection.setHeader(section.getName());
				dySection.setOrderIndex(section.getLayoutindex());
				dySection.setDeletedSection(section.getIsdeletesection());

				List<FormSectionField> fields = section.getFields();
				if (CollectionUtils.isNotEmpty(fields)) {
					for (FormSectionField field : fields) {
						String fieldtype = TYPE_PACKAGE + field.getFieldtype();
						Class clsType;
						try {
							clsType = Class.forName(fieldtype);
						} catch (ClassNotFoundException e) {
							throw new MyCollabException(e);
						}
						AbstractDynaField dynaField = (AbstractDynaField) JsonDeSerializer
								.fromJson(field.getFieldformat(), clsType);
						dynaField.setDisplayName(field.getDisplayname());
						dynaField.setFieldIndex(field.getFieldindex());
						dynaField.setFieldName(field.getFieldname());
						dynaField.setMandatory(field.getIsmandatory());
						dynaField.setRequired(field.getIsrequired());
						dynaField.setCustom(field.getIscustom());

						dySection.addField(dynaField);
					}
				}

				form.addSection(dySection);
			}

			return form;
		}
	}

	@Override
	public void saveCustomForm(@CacheKey Integer sAccountId, String moduleName,
			DynaForm form) {
		log.debug("Save form section");

		int sectionCount = form.getSectionCount();

		if (sectionCount > 0) {
			log.debug("Remove existing form section of module {}", moduleName);
			FormSectionExample ex = new FormSectionExample();
			ex.createCriteria().andSaccountidEqualTo(sAccountId)
					.andModuleEqualTo(moduleName);
			formSectionMapper.deleteByExample(ex);
		}

		for (int i = 0; i < sectionCount; i++) {
			DynaSection section = form.getSection(i);

			FormSection formSection = new FormSection();
			formSection.setModule(moduleName);
			formSection.setLayoutindex(section.getOrderIndex());
			formSection.setLayouttype(LayoutType.to(section.getLayoutType()));
			formSection.setName(section.getHeader());
			formSection.setIsdeletesection(section.isDeletedSection());
			formSection.setSaccountid(sAccountId);

			formSectionMapper.insertAndReturnKey(formSection);
			Integer sectionId = formSection.getId();

			log.debug(
					"Save section name {} of module {} of account {} successfully, Return id is {}",
					new Object[] { section.getHeader(), moduleName, sAccountId,
							sectionId });

			int fieldCount = section.getFieldCount();
			for (int j = 0; j < fieldCount; j++) {
				AbstractDynaField field = section.getField(j);

				FormSectionField dbField = new FormSectionField();
				dbField.setSectionid(sectionId);
				dbField.setDisplayname(field.getDisplayName());
				dbField.setFieldformat(JsonDeSerializer.toJson(field));
				dbField.setFieldindex(field.getFieldIndex());
				dbField.setFieldname(field.getFieldName());
				dbField.setFieldtype(field.getClass().getSimpleName());
				dbField.setIsmandatory(field.isMandatory());
				dbField.setIsrequired(field.isRequired());
				dbField.setIscustom(field.isCustom());

				log.debug(
						"Save field {} with name {}",
						new Object[] { field.getDisplayName(),
								field.getFieldName() });
				formSectionFieldMapper.insertAndReturnKey(dbField);
			}
		}
	}

}
