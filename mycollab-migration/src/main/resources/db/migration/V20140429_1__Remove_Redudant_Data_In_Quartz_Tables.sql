DELETE FROM QRTZ_CRON_TRIGGERS WHERE TRIGGER_NAME IN ('sendAccountBillingEmailTrigger', 'sendingCountUserLoginByDateTrigger');
DELETE FROM QRTZ_TRIGGERS WHERE TRIGGER_NAME IN ('sendAccountBillingEmailTrigger', 'sendingCountUserLoginByDateTrigger');
DELETE FROM QRTZ_JOB_DETAILS WHERE JOB_NAME IN ('sendAccountBillingEmailJob', 'sendCountUserLoginByDateJob');