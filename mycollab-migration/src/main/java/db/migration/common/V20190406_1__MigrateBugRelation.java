package db.migration.common;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class V20190406_1__MigrateBugRelation extends BaseJavaMigration {

    public void migrate(Context context) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(new SingleConnectionDataSource(context.getConnection(), true));
        SimpleJdbcInsert insertOp = new SimpleJdbcInsert(jdbcTemplate).withTableName("m_prj_ticket_relation");

        List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT * FROM m_tracker_related_bug");
        rows.forEach(row -> {
            Map<String, Object> parameters = new HashMap<>(6);
            parameters.put("ticketId", row.get("bugid"));
            parameters.put("ticketType", "Project-Bug");
            parameters.put("type", "Project-Bug");
            parameters.put("typeId", row.get("relatedid"));
            parameters.put("rel", row.get("relatetype"));
            parameters.put("comment", row.get("comment"));
            insertOp.execute(parameters);
        });
        jdbcTemplate.execute("DROP TABLE `m_tracker_related_bug`;");
    }
}
