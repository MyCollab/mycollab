package com.mycollab.test.rule;

import com.mycollab.configuration.SiteConfiguration;
import org.joda.time.DateTimeZone;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.TimeZone;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
public class EssentialInitRule implements TestRule {

    @Override
    public Statement apply(Statement base, Description description) {
        SiteConfiguration.loadConfiguration();
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        DateTimeZone.setDefault(DateTimeZone.UTC);
        return base;
    }
}
