package db.migration;

import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author MyCollab Ltd
 * @since 5.1.1
 */
public class V20150729_1__Default_Values_Option_Table implements SpringJdbcMigration {
    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        jdbcTemplate.execute("ALTER TABLE `m_options` ADD COLUMN `refOption` INT(11) NULL");
        List<Integer> accountIds = jdbcTemplate.queryForList("SELECT id FROM s_account", Integer.class);
        for (Integer accountId : accountIds) {
            //set default status for task
            SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("m_options");
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("type", ProjectTypeConstants.TASK);
            parameters.put("typeVal", "Open");
            parameters.put("sAccountId", accountId);
            parameters.put("createdtime", new GregorianCalendar().getTime());
            parameters.put("isDefault", 1);
            jdbcInsert.execute(parameters);

            parameters.put("typeVal", "Closed");
            jdbcInsert.execute(parameters);

            parameters.put("typeVal", "Pending");
            jdbcInsert.execute(parameters);


            // Set default status for milestone
            parameters.put("type", ProjectTypeConstants.MILESTONE);
            parameters.put("typeVal", OptionI18nEnum.MilestoneStatus.Future.name());
            jdbcInsert.execute(parameters);

            parameters.put("typeVal", OptionI18nEnum.MilestoneStatus.InProgress.name());
            jdbcInsert.execute(parameters);

            parameters.put("typeVal", OptionI18nEnum.MilestoneStatus.Closed.name());
            jdbcInsert.execute(parameters);

            //get all default options
            List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT id, type, typeVal, orderIndex, sAccountId, createdtime, createdUser, " +
                    "extraId, isDefault, refOption, description FROM m_options WHERE sAccountId = ? AND " +
                    "isDefault='1'", new Object[]{accountId});

            List<Integer> projectIds = jdbcTemplate.queryForList("SELECT id FROM m_prj_project WHERE sAccountId=?", new Object[]{accountId}, Integer.class);
            for (Integer projectId : projectIds) {
                if (rows != null && rows.size() > 0) {
                    for (Map<String, Object> row : rows) {
                        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("m_options");
                        parameters = new HashMap<>();
                        parameters.put("type", row.get("type"));
                        parameters.put("typeVal", row.get("typeVal"));
                        parameters.put("sAccountId", accountId);
                        parameters.put("createdtime", new GregorianCalendar().getTime());
                        parameters.put("isDefault", 0);
                        parameters.put("extraId", projectId);
                        jdbcInsert.execute(parameters);
                    }
                }
            }
        }
    }
}
