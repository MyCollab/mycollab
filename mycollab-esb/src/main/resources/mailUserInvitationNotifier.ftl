<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<#import "macroTextBlock.ftl" as lib>
<#include "mailHeader.ftl">
<body>
    <table width="600" cellpadding="0" cellspacing="0" border="0" style="margin: 20px 0px;">
        <#include "mailLogo.ftl">
        <tr>
            <td style="padding: 10px 30px;">
                <div>Hello ${invitee.displayName}, <br>
                    You have a new account at <@lib.hyperLink displayName=siteName webLink=siteUrl/><br>
                    Account details: <br>
                    &nbsp;&nbsp;&nbsp;&nbsp;Email: <a href="mailto:${invitee.email}">${invitee.email}</a><br>
                    <#if password?has_content>
                        &nbsp;&nbsp;&nbsp;&nbsp;Password: ${password}
                    <#else>
                        &nbsp;&nbsp;&nbsp;&nbsp;Password: &lt;&lt;Sent in the previous message&gt;&gt;
                    </#if>
                </div>
                <br>
                <@lib.actionLink displayName="Go" webLink=siteUrl/><br>
            </td>
        </tr>
    <#include "mailFooter.ftl">
    </table>
</body>
</html>