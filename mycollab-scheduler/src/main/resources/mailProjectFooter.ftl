<#import "macroTextBlock.ftl" as lib>
<p style="color: ${styles.meta_color}; font:${styles.small_font}; text-align: center;">
This email has been sent to you as part of your project "${projectName!}" membership. To change your project email
preferences at any time, please visit the <@lib.hyperLink displayName="Project Notification Setting" webLink=projectNotificationUrl!/>
page for your project.
</p>
<div style="color: ${styles.meta_color}; font:${styles.small_font}; display:inline-flex; padding: 0px 30px;">
  Copyright by <a href="https://www.mycollab.com">MyCollab ${current_year?c}</a>. All rights reserved. Connect with us:
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