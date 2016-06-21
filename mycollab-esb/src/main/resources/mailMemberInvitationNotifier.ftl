<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<#import "macroTextBlock.ftl" as lib>
<#include "mailHeader.ftl">
<body">
<table width="600" cellpadding="0" cellspacing="0" border="0" style="margin: 20px auto;">
    <#include "mailLogo.ftl">
    <tr>
        <td style="padding: 10px 25px;">
            <div><img src="${defaultUrls.cdn_url}icons/default_user_avatar_16.png" width="16" height="16"
                      style="display: inline-block; vertical-align: top;"/><b>${inviteUser}</b> invited you to join the
                project <b>"${project.name}!"</b>.
            </div>
        </td>
    </tr>
    <tr>
        <td style="padding: 0px 25px;">
            <@lib.block content=inviteMessage!/>
            <#if password?has_content>
            Account details: <br>
            &nbsp;&nbsp;&nbsp;&nbsp;Email: <a href="mail:${inviteeEmail}">${inviteeEmail}</a><br>
            &nbsp;&nbsp;&nbsp;&nbsp;Password: ${password}
            </#if>
            <br>
            <@lib.actionLink displayName="Go" webLink=urlAccept/><br>
        </td>
    </tr>
    <#include "mailFooter.ftl">
</table>
</body>
</html>