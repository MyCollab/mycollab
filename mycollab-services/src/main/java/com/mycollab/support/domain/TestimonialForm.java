package com.mycollab.support.domain;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @author MyCollab Ltd
 * @since 5.0.6
 */
public class TestimonialForm {
    @NotNull(message = "Name must be not null")
    @Length(max = 100, message = "Field value is too long")
    private String displayname;

    @Length(max = 100, message = "Field value is too long")
    private String jobrole;

    @Length(max = 100, message = "Field value is too long")
    private String company;

    @Length(max = 100, message = "Field value is too long")
    private String website;

    @NotNull(message = "Testimonial must be not null")
    private String testimonial;

    @NotNull(message = "Email must be not null")
    private String email;

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getJobrole() {
        return jobrole;
    }

    public void setJobrole(String jobrole) {
        this.jobrole = jobrole;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getTestimonial() {
        return testimonial;
    }

    public void setTestimonial(String testimonial) {
        this.testimonial = testimonial;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
