<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<#import "macroTextBlock.ftl" as lib>
<#include "mailHeader.ftl">
<body style="width: 600px">
<table width="600" cellpadding="0" cellspacing="0" class="wrapContent">
  <#include "mailLogo.ftl">
  <tr>
    <td style="padding: 10px 25px;">
      <div><img src="${defaultUrls.cdn_url}icons/default_user_avatar_16.png" width="16" height="16"
                style="display: inline-block; vertical-align: top;"/>
        ${formatter.formatMemberLink(siteUrl, newUser)} has joined to MyCollab workspace as
        ${formatter.formatRoleName(siteUrl, newUser)}
      </div>
    </td>
  </tr>
</table>
<#include "mailFooter2.ftl">
</body>