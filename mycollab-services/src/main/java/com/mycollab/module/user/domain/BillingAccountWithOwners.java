package com.mycollab.module.user.domain;

import java.util.List;

public class BillingAccountWithOwners extends SimpleBillingAccount {
	private static final long serialVersionUID = 1L;
	private List<SimpleUser> owners;

	public List<SimpleUser> getOwners() {
		return owners;
	}

	public void setOwners(List<SimpleUser> owners) {
		this.owners = owners;
	}
}
