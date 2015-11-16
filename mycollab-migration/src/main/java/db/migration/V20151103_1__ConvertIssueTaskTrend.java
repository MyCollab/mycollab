package db.migration;

import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.joda.time.LocalDate;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author MyCollab Ltd
 * @since 5.2.2
 */
public class V20151103_1__ConvertIssueTaskTrend implements SpringJdbcMigration {
    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        jdbcTemplate.update("delete from s_timeline_tracking");
        jdbcTemplate.execute("ALTER TABLE `s_timeline_tracking` ADD COLUMN `flag` TINYINT(2) NOT NULL DEFAULT 1;");
        createTimelineOfBugs(jdbcTemplate);
        createTimelineOfTasks(jdbcTemplate);
    }

    private void createTimelineOfBugs(JdbcTemplate jdbcTemplate) {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT id, lastUpdatedTime, projectid, status, " +
                "sAccountId FROM m_tracker_bug ORDER BY lastUpdatedTime ASC");
        Map<Entry, Map<String, Object>> newEntries = new HashMap<>();
        for (Map<String, Object> row : rows) {
            Long id = (Long) row.get("id");
            LocalDate lastUpdatedTime = new LocalDate(row.get("lastUpdatedTime"));
            Entry entry = new Entry(id, lastUpdatedTime);
            newEntries.put(entry, row);
        }

        Collection<Map<String, Object>> entries = newEntries.values();
        for (Map<String, Object> row : entries) {
            Long id = (Long) row.get("id");
            LocalDate lastUpdatedTime = new LocalDate(row.get("lastUpdatedTime"));
            String status = (String) row.get("status");
            Long projectid = (Long) row.get("projectid");
            Integer sAccountId = (Integer) row.get("sAccountId");

            jdbcTemplate.update("insert s_timeline_tracking(type, typeid, fieldval, extratypeid, sAccountId, forDay, " +
                            "fieldgroup, flag) values (?,?,?,?,?,?,?, ?)",
                    ProjectTypeConstants.BUG, id, status, projectid, sAccountId, lastUpdatedTime.toDate(), "status", 1);
        }
    }

    private void createTimelineOfTasks(JdbcTemplate jdbcTemplate) {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT id, lastUpdatedTime, projectid, status, " +
                "sAccountId FROM m_prj_task ORDER BY lastUpdatedTime ASC");
        Map<Entry, Map<String, Object>> newEntries = new HashMap<>();
        for (Map<String, Object> row : rows) {
            Long id = (Long) row.get("id");
            LocalDate lastUpdatedTime = new LocalDate(row.get("lastUpdatedTime"));
            Entry entry = new Entry(id, lastUpdatedTime);
            newEntries.put(entry, row);
        }

        Collection<Map<String, Object>> entries = newEntries.values();
        for (Map<String, Object> row : entries) {
            Long id = (Long) row.get("id");
            LocalDate lastUpdatedTime = new LocalDate(row.get("lastUpdatedTime"));
            String status = (String) row.get("status");
            Long projectid = (Long) row.get("projectid");
            Integer sAccountId = (Integer) row.get("sAccountId");

            jdbcTemplate.update("insert s_timeline_tracking(type, typeid, fieldval, extratypeid, sAccountId, forDay, " +
                            "fieldgroup, flag) values (?,?,?,?,?,?,?,?)",
                    ProjectTypeConstants.TASK, id, status, projectid, sAccountId, lastUpdatedTime.toDate(), "status", 1);
        }
    }

    private static class Entry {
        LocalDate lastUpdatedTime;
        Long typeId;

        private Entry(Long typeId, LocalDate lastUpdatedTime) {
            this.typeId = typeId;
            this.lastUpdatedTime = lastUpdatedTime;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Entry)) return false;

            Entry entry = (Entry) o;

            if (!lastUpdatedTime.equals(entry.lastUpdatedTime)) return false;
            return typeId.equals(entry.typeId);

        }

        @Override
        public int hashCode() {
            int result = lastUpdatedTime.hashCode();
            result = 31 * result + typeId.hashCode();
            return result;
        }
    }
}
