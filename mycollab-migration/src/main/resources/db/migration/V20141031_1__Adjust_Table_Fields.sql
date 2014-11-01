DELETE FROM s_activitystream WHERE type LIKE '%Project%' AND module="Crm";
UPDATE m_tracker_bug SET status='ReOpened' WHERE status='Reopened';
