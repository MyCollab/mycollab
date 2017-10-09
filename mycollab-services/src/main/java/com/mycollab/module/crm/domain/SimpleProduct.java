package com.mycollab.module.crm.domain;

public class SimpleProduct extends Product {
    private String accountname;

    private String contactname;

    public String getAccountname() {
        return accountname;
    }

    public void setAccountname(String accountname) {
        this.accountname = accountname;
    }

    public String getContactname() {
        return contactname;
    }

    public void setContactname(String contactname) {
        this.contactname = contactname;
    }
}
