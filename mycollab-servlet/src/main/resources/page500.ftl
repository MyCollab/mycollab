<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<#import "macroPage.ftl" as lib>
<@lib.head title="Error"/>
<body>
    <#include "pageLogo.ftl">
    <div id="spacing"></div>
    <div id="mainBody">
        <div id="title">
            <h1>Error</h1>
        </div>
        <hr size="1">
        <div id="content" style="padding-top: 20px">
            <div id="content_left">
                <div id="exclamation_mark">&#33;</div>
                <div id="error_display">
                    <div id="error_code">500</div>
                    <div id="error_brief">Something's wrong</div>
                </div>
                <div class="clear"></div>
            </div>
            <div id="content_right">
                <div id="error_excuse">
                    An unexpected error has occurred. We apologize for the inconvenience. Our team has been notified and will investigate the issue right away.
                </div>
                <div id="back_to_home" style="padding-left:30px;padding-top:20px;">
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
