<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<#import "macroPage.ftl" as lib>
<@lib.head title="Page not found"/>
<body>
    <#include "pageLogo.ftl">
    <div id="spacing"></div>
    <div id="mainBody">
        <div id="title">
            <h1>Page not found</h1>
        </div>
        <hr size="1">
        <div id="content" style="padding-top: 20px">
            <div id="content_left">
                <div id="exclamation_mark">&#33;</div>
                <div id="error_display">
                    <div id="error_code">404</div>
                    <div id="error_brief">Page not found</div>
                </div>
                <div class="clear"></div>
            </div>
            <div id="content_right">
                <div id="error_excuse">
                    <p>Well, this is embarrassing. We can't find the page you asked for.Bad link? Mistyped address? We're not sure. If you think something is wrong, <a class="v-button-bluebtn" style="text-decoration : none;" href="mailto:support@mycollab.com">contact us</a></p>
                </div>
                <div id="back_to_home" style="padding-left:30px;">
                    <a class="v-button v-button-orangebtn" style="text-decoration : none;" href="https://www.mycollab.com">Return to the home page</a>
                </div>
                <div class="clear"></div>
            </div>
            <div class="clear"></div>
        </div>
        <#include "pageFooter.ftl">
    </div>
</body>
</html>