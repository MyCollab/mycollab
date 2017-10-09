package com.mycollab.community.shell.view.components;

import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Text;
import com.mycollab.html.DivLessFormatter;
import com.mycollab.module.user.domain.SimpleUser;
import com.mycollab.module.user.service.UserService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

/**
 * @author MyCollab Ltd
 * @since 5.1.3
 */
public class AdRequestWindow extends MWindow {
    public AdRequestWindow(final SimpleUser user) {
        super("No one has ever become poor by giving -  Anne Frank, diary of Anne Frank");
        this.withModal(true).withResizable(false).withWidth("800px").withCenter();

        MVerticalLayout content = new MVerticalLayout();

        Label message = new Label("Hey <b>" + UserUIContext.getUser().getDisplayName() + "</b>, you've been " +
                "using MyCollab for a while now, and we hope you are happy with it. We invested a lot of time and " +
                "money developing MyCollab. If you like it, please write a few words on twitter, blog or " +
                "our testimonial form. Your kindness helps this software be continued.",
                ContentMode.HTML);

        Label tweetUs = new Label(new Div().appendChild(new Text("&nbsp;&nbsp;" + FontAwesome.TWITTER.getHtml()),
                DivLessFormatter.EMPTY_SPACE,
                new A("https://twitter.com/intent/tweet?text=I am using MyCollab to manage all project activities, " +
                        "accounts and it works great @mycollabdotcom&source=webclient", "_blank")
                        .appendText("Share on Twitter")).setStyle("color:#006dac").write(), ContentMode.HTML);

        Label linkedIn = new Label(new Div().appendChild(new Text("&nbsp;&nbsp;" + FontAwesome.LINKEDIN_SQUARE.getHtml()),
                DivLessFormatter.EMPTY_SPACE,
                new A("https://www.linkedin.com/cws/share?url=https%3A%2F%2Fwww.mycollab.com&original_referer=https%3A%2F%2Fwww.mycollab.com&token=&isFramed=false&lang=en_US", "_blank")
                        .appendText("Share on LinkedIn")).setStyle("color:#006dac").write(), ContentMode.HTML);

        Label testimonialAd = new Label("A chance to get a free license of the premium MyCollab software for 10 users" +
                ". If you execute one of the following:");
        Label rateSourceforge = new Label(new Div().appendChild(new Text("&nbsp;&nbsp;" + FontAwesome.FLAG_CHECKERED.getHtml()),
                DivLessFormatter.EMPTY_SPACE, new A("https://community.mycollab.com/docs/developing-mycollab/translating/", "_blank")
                        .appendText("Localize MyCollab to your language at least 20% of the phrases")).setStyle
                ("color:#006dac").write(), ContentMode.HTML);
        MButton testimonialBtn = new MButton("Write a testimonial which is selected to post on our website",
                clickEvent -> {
                    close();
                    turnOffAdd(user);
                    UI.getCurrent().addWindow(new TestimonialWindow());
                }).withIcon(FontAwesome.KEYBOARD_O).withStyleName(WebThemes.BUTTON_LINK);

        MButton ignoreBtn = new MButton("No, thanks", clickEvent -> {
            close();
            turnOffAdd(user);
        }).withStyleName(WebThemes.BUTTON_OPTION);

        MButton loveBtn = new MButton("I did", clickEvent -> {
            close();
            NotificationUtil.showNotification("We appreciate your kindness action", "Thank you for your time");
            turnOffAdd(user);
        }).withIcon(FontAwesome.HEART).withStyleName(WebThemes.BUTTON_ACTION);

        MHorizontalLayout btnControls = new MHorizontalLayout(ignoreBtn, loveBtn);
        content.with(message, rateSourceforge, tweetUs, linkedIn, testimonialAd, rateSourceforge, new
                        MHorizontalLayout(ELabel.html("&nbsp;&nbsp;"), testimonialBtn).withSpacing(false),
                new Label("Sincerely,"), ELabel.html(new A("https://hainguyen.mycollab.com/about-me/", "_blank").appendText("Hai Nguyen").write()), btnControls)
                .withAlign(btnControls, Alignment.MIDDLE_RIGHT);
        this.setContent(content);
    }

    private void turnOffAdd(SimpleUser user) {
        user.setRequestad(true);
        UserService userService = AppContextUtil.getSpringBean(UserService.class);
        userService.updateSelectiveWithSession(user, UserUIContext.getUsername());
    }
}