package com.mycollab.servlet;

import com.mycollab.configuration.SiteConfiguration;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.joda.time.LocalDate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class SetupServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);

        Configuration configuration = SiteConfiguration.freemarkerConfiguration();
        Map<String, Object> context = new HashMap<>();

        String postUrl = "/install";
        context.put("postUrl", postUrl);

        Map<String, String> defaultUrls = new HashMap<>();
        defaultUrls.put("cdn_url", "/assets/");
        defaultUrls.put("app_url", "/");
        defaultUrls.put("facebook_url", "https://www.facebook.com/mycollab2");
        defaultUrls.put("google_url", "https://plus.google.com/u/0/b/112053350736358775306/+Mycollab/about/p/pub");
        defaultUrls.put("twitter_url", "https://twitter.com/mycollabdotcom");
        context.put("defaultUrls", defaultUrls);
        context.put("current_year", new LocalDate().getYear());

        StringWriter writer = new StringWriter();
        Template template = configuration.getTemplate("pageSetupFresh.ftl");
        try {
            template.process(context, writer);
        } catch (TemplateException e) {
            throw new IOException(e);
        }

        PrintWriter out = response.getWriter();
        out.print(writer.toString());
    }
}
