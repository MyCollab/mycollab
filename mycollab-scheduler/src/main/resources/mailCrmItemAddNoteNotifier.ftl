<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<#import "macroTextBlock.ftl" as lib>
<#include "mailHeader.ftl">
<body>
    <table width="600" cellpadding="0" cellspacing="0" border="0" class="wrapContent">
        <#include "mailLogo.ftl">
        <tr>
            <td style="padding: 10px 30px;">
                <p>${actionHeading}</p>
                <p><b><@lib.hyperLink displayName=summary webLink=summaryLink/></b></p>
                <@lib.block content=comment.changecomment!/>
            </td>
        </tr>
        <#include "mailFooter.ftl">
    </table>
</body>
</html>