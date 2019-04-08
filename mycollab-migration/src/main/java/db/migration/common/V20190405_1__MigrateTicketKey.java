package db.migration.common;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class V20190405_1__MigrateTicketKey extends BaseJavaMigration {
    public void migrate(Context context) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(new SingleConnectionDataSource(context.getConnection(), true));
        SimpleJdbcInsert insertOp = new SimpleJdbcInsert(jdbcTemplate).withTableName("m_prj_ticket_key");
        List<Integer> projectIds = jdbcTemplate.queryForList("SELECT id FROM m_prj_project", Integer.class);
        for (Integer projectId : projectIds) {
            AtomicInteger key = new AtomicInteger(1);
            List<Integer> bugIds = jdbcTemplate.queryForList("SELECT id FROM m_prj_bug WHERE projectId=?",
                    new Object[]{projectId}, Integer.class);

            bugIds.forEach(bugId -> {
                Map<String, Object> parameters = new HashMap<>(3);
                parameters.put("projectId", projectId);
                parameters.put("ticketId", bugId);
                parameters.put("ticketType", "Project-Bug");
                parameters.put("ticketKey", key.getAndIncrement());
                insertOp.execute(parameters);
            });

            List<Integer> taskIds = jdbcTemplate.queryForList("SELECT id FROM m_prj_task WHERE projectId=?",
                    new Object[]{projectId}, Integer.class);
            taskIds.forEach(taskId -> {
                Map<String, Object> parameters = new HashMap<>(3);
                parameters.put("projectId", projectId);
                parameters.put("ticketId", taskId);
                parameters.put("ticketType", "Project-Task");
                parameters.put("ticketKey", key.getAndIncrement());
                insertOp.execute(parameters);
            });
        }
    }
}
