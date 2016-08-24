ALTER TABLE `s_billing_plan`
ADD COLUMN `bankTransferPath` VARCHAR(400) NULL;
ALTER TABLE `s_billing_plan`
ADD COLUMN `yearlyShoppingUrl` VARCHAR(400) NULL;
ALTER TABLE `s_account`
ADD COLUMN `paymentMethod` VARCHAR(45) NULL;
