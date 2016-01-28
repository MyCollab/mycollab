<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="icon" href="${defaultUrls.cdn_url}favicon.ico" type="image/x-icon">
<link rel="shortcut icon" href="${defaultUrls.cdn_url}favicon.ico" type="image/x-icon">
</head>
<body style="background-color: ${styles.background}; font: ${styles.font}; color: #4e4e4e; padding: 0px;">
    #macro( confirmLink $webLink $displayText )
        <a href="$webLink" style="${styles.link_color};">$displayText</a>
    #end
    
    #macro( linkBlock $webLink )
        <div style="padding: 20px 15px; background-color: rgb(237, 248, 255);">
            <a href="$webLink" style="color: ${styles.link_color}; width: 100%; display: inline-block; word-wrap: break-word; white-space: normal; word-break: break-all;">$webLink</a>
        </div>
    #end
    
    <table width="760" cellpadding="0" cellspacing="0" border="0" style="margin: 20px auto;">
       <tr>
               <td style="text-align: center; padding: 50px 0px 0px;">
                   <img src="${defaultUrls.cdn_url}icons/logo_email.png" alt="The power productivity tool for your organization" width="130" height="30" style="margin: 0px; padding: 0px;">
               </td>            
        </tr>
        <tr>
            <td style="padding: 0px 25px; text-align: center;">
                        <p style="font-size: 22px;"><b><i>Thank you for choosing MyCollab!</i></b><p>
                        <p>You are just one click away from completing your account registration: <p>
                        <div style="background-color: rgb(32, 36, 35); tex-align: center; padding: 3px 0px; width: 330px; margin: 0px auto;">
                            <div style="width: 100%; padding: 10px 0px; border-color: rgb(99, 102, 101); border-width: 1px 0px; border-style: solid;">
                                <a style="text-decoration:none;" href="$!linkConfirm"/><span style="font-size: 22px; text-transform: uppercase; color: rgb(255, 255, 255);">Confirm your e-mail</span></a>
                            </div>
                        </div> 
                        <br>
                        <p style="text-align: left;">Access your account anytime from 
                        #confirmLink ($siteUrl $siteUrl)
                         (maybe bookmark this page for future reference).<br>
                        Login with your email address 
                        #set ($mailToUrl = "mailto:" + $user.Email)
                        #confirmLink ($mailToUrl $user.Email)
                         and the password you created.</p>
                        <p style="text-align: left;">By clicking this link, you agree to the 
                        #confirmLink ("https://www.mycollab.com/terms" "Terms of Service")
                         and the 
                        #confirmLink ("https://www.mycollab.com/privacy" "Privacy Policy")
                        </p>
                        <p style="text-align: left;">
                        If clicking on the link does not work, just copy and paste the following address into your browser:
                        </p>
                        #linkBlock ($!linkConfirm)
                        <p style="text-align: left;">
                        If you are still having problems, simply forward this e-mail to
                        #confirmLink ("mailto:support@mycollab.com" "support@mycollab.com")
                        , and we will be happy to help you. <br><br>
                        
                        <span style="font-weight: bold;">Have a productive day!</span>
                        </p>
                        <p style="text-align: left;">P/S: Hope you enjoy using MyCollab to grow the sales in your
                        business, and remember you can switch between plans during the trial!</p>
                    </div>
                    <div id="contentFooter" style="padding:10px 0px 50px; text-align: left;">
                        Best regards, <br>
                        <span style="font-weight: bold;">MyCollab's Customer Support Team </span><br>
                        (+84) 862-924-513 <br>
                        <a href="mailto:support@mycollab.com" style="text-decoration : none;"><span
                        style="font-weight: bold; color: ${styles.link_color};">support@mycollab.com </span></a>
                    </div>
                </div>
            </td>
        </tr>
        #parse("templates/email/footer_en_US.mt")
    </table>
</body> 
</html>    