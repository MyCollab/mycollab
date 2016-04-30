UPDATE m_tracker_bug SET status='Resolved' WHERE id > 0 AND status='WontFix';
UPDATE m_tracker_bug SET status='Open' WHERE id > 0 AND status='InProgress';
UPDATE m_tracker_bug SET resolution='None' WHERE id > 0 AND resolution='Newissue';
UPDATE m_tracker_bug SET resolution='None' WHERE id > 0 AND resolution='WaitforVerification';
UPDATE m_tracker_bug SET resolution='None' WHERE id > 0 AND resolution='ReOpen';
UPDATE m_tracker_bug SET resolution='Duplicate' WHERE id > 0 AND resolution='Duplicated';