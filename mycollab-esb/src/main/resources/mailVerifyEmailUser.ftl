<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<#import "macroTextBlock.ftl" as lib>
<#include "mailHeader.ftl">
<body style="width: 600px">
<table width="600" cellpadding="0" cellspacing="0" class="wrapContent">
    <#include "mailLogo.ftl">
    <tr>
        <td style="padding: 0px 25px;">
            <p style="font-size: 22px;"><b>Email Verification</b>
            <p>Click the link below to verify your email:</p>
            <div style="background-color: #1F9DFE; tex-align: center; padding: 3px 0px; width: 330px; margin: 0px auto;">
                <div style="width: 100%; text-align: center; padding: 10px 0px;">
                    <a style="text-decoration:none;" href="${linkConfirm}"/><span
                        style="font-size: 22px; color: #FFFFFF;">Confirm your e-mail &gt; </span></a>
                </div>
            </div>
        </td>
    </tr>
</table>
<#include "mailFooter2.ftl">
</body>
</html>    