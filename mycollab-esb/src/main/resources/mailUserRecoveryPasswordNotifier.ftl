<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<#import "macroTextBlock.ftl" as lib>
<#include "mailHeader.ftl">
<body>
<table width="600" cellpadding="0" cellspacing="0" border="0" style="margin: 20px 0px;">
    <#include "mailLogo.ftl">
    <tr>
        <td style="padding: 10px 30px;">
            <br>
            <div style="display: block; border-bottom: 1px solid rgb(212, 212, 212); padding-bottom: 5px; margin-bottom: 10px;">
                Hi <b>${username}</b>,
            </div>
            <span style="text-align: left;">We has received a password change request for this email. If you made this request, then please click on link below:</span><br>

            <@lib.actionLink displayName="Change password" webLink=urlRecoveryPassword!/>

            <p style="text-align: left; word-wrap: break-word; white-space: normal; word-break: normal;">If you did not
                ask to change your password, then please ignore this email. Another user may have entered your username
                by mistake. No change will be made to your account.</p>
        </td>
    </tr>
    <#include "mailFooter.ftl">
</table>
</body>
</html>