<#import "macroTextBlock.ftl" as lib>
<#if unsubscribeUrl?has_content>
<p style="color: ${styles.meta_color}; font:${styles.small_font}; text-align: center;">
This account email has been sent to you as part of your MyCollab membership.
You can unsubscribe from these emails. <@lib.hyperLink displayName="Unsubscribe from our emails" webLink=unsubscribeUrl/>
</p>
</#if>
<div style="color: ${styles.meta_color}; font:${styles.small_font}; display:inline-flex; padding: 0px 30px;">
    Copyright by <a href="https://www.mycollab.com">&nbsp;©&nbsp; MyCollab ${current_year?c}</a>. All rights reserved.&nbsp;Connect with us:&nbsp;
    <a style="display: inline-block; vertical-align: middle;" href="${defaultUrls.facebook_url}">
        <img src="${defaultUrls.cdn_url}icons/email/footer-facebook.png">
    </a>&nbsp;
    <a style="display: inline-block; vertical-align: middle;" href="${defaultUrls.google_url}">
        <img src="${defaultUrls.cdn_url}icons/email/footer-google.png">
    </a>&nbsp;
    <a style="display: inline-block; vertical-align: middle;" href="${defaultUrls.twitter_url}">
        <img src="${defaultUrls.cdn_url}icons/email/footer-twitter.png">
    </a>
</div>