package com.mycollab.form.dao;

import com.mycollab.form.domain.SimpleFormSection;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FormSectionMapperExt {
    List<SimpleFormSection> findSections(@Param("accountId") Integer sAccountId, @Param("moduleName") String moduleName);
}
