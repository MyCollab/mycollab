package db.migration;

import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author MyCollab Ltd
 * @since 5.2.4
 */
public class V20151218_1__In_Progress_As_Default_Option implements SpringJdbcMigration {
    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        List<Integer> accountIds = jdbcTemplate.queryForList("SELECT id FROM s_account", Integer.class);
        for (Integer accountId : accountIds) {
            //set default status for task
            SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("m_options");
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("type", ProjectTypeConstants.TASK);
            parameters.put("typeVal", "InProgress");
            parameters.put("sAccountId", accountId);
            parameters.put("createdtime", new GregorianCalendar().getTime());
            parameters.put("isDefault", 1);
            parameters.put("color", "fdde86");
            jdbcInsert.execute(parameters);

            List<Integer> projectIds = jdbcTemplate.queryForList("SELECT id FROM m_prj_project WHERE sAccountId=?", new Object[]{accountId}, Integer.class);
            for (Integer projectId : projectIds) {
                jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("m_options");
                parameters = new HashMap<>();
                parameters.put("type", "Project-Task");
                parameters.put("typeVal", "InProgress");
                parameters.put("sAccountId", accountId);
                parameters.put("createdtime", new GregorianCalendar().getTime());
                parameters.put("isDefault", 0);
                parameters.put("extraId", projectId);
                parameters.put("color", "fdde86");
                jdbcInsert.execute(parameters);
            }
        }
    }
}
