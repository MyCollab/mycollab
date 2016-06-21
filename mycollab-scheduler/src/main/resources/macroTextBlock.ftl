<#macro block content>
    <div style="padding: 20px 15px; background-color: rgb(237, 248, 255); position: relative; color: rgb(71, 87, 116); text-align: left; word-wrap: break-word; white-space: normal; word-break: break-all;">
        <div style="color: #A7DDF9; font-size: 35px; line-height: 10px; text-align: left;">&ldquo;</div>
        <div style="padding:0px 20px; font-size: 12px; line-height: 1.6em;">${content}</div>
        <div style="color: #A7DDF9; font-size: 35px; line-height: 10px; text-align: right;">&bdquo;</div>
    </div>
</#macro>

<#macro hyperLink displayName webLink>
    <a href="${webLink}" style="white-space: normal;">${displayName}</a>
</#macro>

<#macro actionLink displayName webLink>
    <a href="${webLink}" class="actionBtn">${displayName}</a>
</#macro>
