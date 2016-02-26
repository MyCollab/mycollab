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
package com.esofthead.mycollab.spring;

import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.persistence.VelocityDriverDeclare;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author MyCollab Ltd.
 * @since 4.6.0
 */
@Configuration
@Profile("production")
@Import(DataSourceConfiguration.class)
@DependsOn("dbMigration")
public class MyBatisConfiguration {
    @Autowired
    DataSourceConfiguration dbConfig;

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dbConfig.dataSource());
        sqlSessionFactory.setTypeAliasesPackage("com.esofthead.mycollab.common.domain.criteria;" +
                "com.esofthead.mycollab.module.crm.domain.criteria;" +
                "com.esofthead.mycollab.module.ecm.domain.criteria;" +
                "com.esofthead.mycollab.module.file.domain.criteria;" +
                "com.esofthead.mycollab.module.project.domain.criteria;" +
                "com.esofthead.mycollab.module.tracker.domain.criteria;" +
                "com.esofthead.mycollab.module.user.domain.criteria;" +
                "com.esofthead.mycollab.ondemand.module.support.domain.criteria");
        sqlSessionFactory.setTypeAliasesSuperType(SearchCriteria.class);
        sqlSessionFactory.setTypeAliases(new Class[]{VelocityDriverDeclare.class});
        sqlSessionFactory.setTypeHandlersPackage("com.esofthead.mybatis.plugin.ext");
        sqlSessionFactory.setMapperLocations(buildBatchMapperResources(
                "classpath:sqlMap/common/*Mapper*.xml",
                "classpath:sqlMap/user/*Mapper*.xml",
                "classpath:sqlMap/form/*Mapper*.xml",
                "classpath:sqlMap/ecm/*Mapper*.xml",
                "classpath:sqlMap/crm/*Mapper*.xml",
                "classpath:sqlMap/project/*Mapper*.xml",
                "classpath:sqlMap/tracker/*Mapper*.xml",
                "classpath:sqlMap/support/*Mapper*.xml"));

        return sqlSessionFactory.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlMapClient() throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory());
    }

    private Resource[] buildMapperResources(String resourcePath) throws IOException {
        try {
            ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
            Resource[] mappingLocations = patternResolver.getResources(resourcePath);
            return mappingLocations;
        } catch (FileNotFoundException e) {
            return new Resource[0];
        }
    }

    private Resource[] buildBatchMapperResources(String... resourcesPath) throws IOException {
        ArrayList<Resource> resources = new ArrayList<>();
        for (String resourcePath : resourcesPath) {
            CollectionUtils.addAll(resources, buildMapperResources(resourcePath));
        }
        return resources.toArray(new Resource[0]);
    }
}
