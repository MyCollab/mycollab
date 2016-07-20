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
package com.mycollab.form.service.impl;

import com.mycollab.core.MyCollabException;
import com.mycollab.core.cache.CacheKey;
import com.mycollab.core.utils.JsonDeSerializer;
import com.mycollab.form.dao.FormSectionFieldMapper;
import com.mycollab.form.dao.FormSectionMapper;
import com.mycollab.form.dao.FormSectionMapperExt;
import com.mycollab.form.domain.FormSection;
import com.mycollab.form.domain.FormSectionExample;
import com.mycollab.form.domain.FormSectionField;
import com.mycollab.form.domain.SimpleFormSection;
import com.mycollab.form.service.MasterFormService;
import com.mycollab.form.view.builder.type.AbstractDynaField;
import com.mycollab.form.view.builder.type.DynaForm;
import com.mycollab.form.view.builder.type.DynaSection;
import com.mycollab.form.view.builder.type.DynaSection.LayoutType;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MasterFormServiceImpl implements MasterFormService {

    private static final Logger LOG = LoggerFactory.getLogger(MasterFormServiceImpl.class);
    private static final String TYPE_PACKAGE = "com.mycollab.form.view.builder.type.";

    @Autowired
    private FormSectionMapper formSectionMapper;

    @Autowired
    private FormSectionMapperExt formSectionMapperExt;

    @Autowired
    private FormSectionFieldMapper formSectionFieldMapper;

    @SuppressWarnings({"unchecked"})
    @Override
    public DynaForm findCustomForm(@CacheKey Integer sAccountId, String moduleName) {
        List<SimpleFormSection> sections = formSectionMapperExt.findSections(sAccountId, moduleName);

        if (CollectionUtils.isEmpty(sections)) {
            return null;
        } else {
            DynaForm form = new DynaForm();

            for (SimpleFormSection section : sections) {
                DynaSection dySection = new DynaSection();
                dySection.setLayoutType(LayoutType.from(section.getLayouttype()));

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
                        AbstractDynaField dynaField = (AbstractDynaField) JsonDeSerializer.fromJson(field.getFieldformat(), clsType);
                        dynaField.setDisplayName(field.getDisplayname());
                        dynaField.setFieldIndex(field.getFieldindex());
                        dynaField.setFieldName(field.getFieldname());
                        dynaField.setMandatory(field.getIsmandatory());
                        dynaField.setRequired(field.getIsrequired());
                        dynaField.setCustom(field.getIscustom());

                        dySection.fields(dynaField);
                    }
                }

                form.sections(dySection);
            }

            return form;
        }
    }

    @Override
    public void saveCustomForm(@CacheKey Integer sAccountId, String moduleName, DynaForm form) {
        LOG.debug("Save form section");

        int sectionCount = form.getSectionCount();

        if (sectionCount > 0) {
            LOG.debug("Remove existing form section of module {}", moduleName);
            FormSectionExample ex = new FormSectionExample();
            ex.createCriteria().andSaccountidEqualTo(sAccountId).andModuleEqualTo(moduleName);
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

            LOG.debug("Save section name {} of module {} of account {} successfully, Return id is {}",
                    section.getHeader(), moduleName, sAccountId, sectionId);

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

                LOG.debug("Save field {} with name {}", new Object[]{field.getDisplayName(), field.getFieldName()});
                formSectionFieldMapper.insertAndReturnKey(dbField);
            }
        }
    }

}
