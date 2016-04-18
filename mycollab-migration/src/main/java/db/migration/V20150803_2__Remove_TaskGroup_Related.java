package db.migration;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

/**
 * @author MyCollab Ltd
 * @since 5.1.1
 */
public class V20150803_2__Remove_TaskGroup_Related implements SpringJdbcMigration {
    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT id, projectid, sAccountId, milestoneId " +
                "FROM m_prj_task_list");
        for (Map<String, Object> row : rows) {
            Long id = (Long) row.get("id");
            Integer milestoneId = (Integer) row.get("milestoneId");

            jdbcTemplate.update("UPDATE m_prj_task SET milestoneId=? WHERE tasklistid=?", milestoneId, id);

        }

        jdbcTemplate.execute("ALTER TABLE `m_prj_task` DROP COLUMN `tasklistid`, DROP INDEX `FK_m_prj_task_2` ;");
        jdbcTemplate.execute("DROP TABLE `m_prj_task_list`;");
    }
}
