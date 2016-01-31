<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Recovery password</title>
<style>
a {
  color: $styles.link_color;
}
</style>
</head>
<body style="background-color: ${styles.background}; font: ${styles.font}; color: #4e4e4e; padding: 0px 0px;">
    #macro( linkBlock $webLink $displayName)
        <table style="width: auto; border-collapse: collapse; margin: 10px auto">
            <tbody>
                <tr>
                    <td>
                        <div style="border: 1px solid ${styles.border_color}; border-radius: 3px">
                            <table style="width: auto; border-collapse: collapse">
                                <tr>
                                    <td style="font: 14px/1.4285714 Arial, sans-serif; padding: 4px 10px; background-color: ${styles.action_color}">
                                        <a href="$webLink" style="color: white; text-decoration: none; font-weight: bold">$displayName</a>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </td>
                </tr>
            </tbody>
        </table>
    #end
    
    <table width="600" cellpadding="0" cellspacing="0" border="0" style="margin: 20px auto;">
        #parse("templates/email/logo.mt")
        <tr>
            <td style="padding: 10px 30px;">
                <br>
                <div style="display: block; border-bottom: 1px solid rgb(212, 212, 212); padding-bottom: 5px; margin-bottom: 10px;">Hi <b>$!username</b>,</div>
                <span style="text-align: left; word-wrap: break-word; white-space: normal; word-break: break-all;">We has received a password change request for this email. If you made this request, then please click on link below:</span><br>
                
                #linkBlock( $!urlRecoveryPassword "Change password")
                
                <p style="text-align: left; word-wrap: break-word; white-space: normal; word-break: normal;">If you did not ask to change your password, then please ignore this email. Another user may have entered your username by mistake. No change will be made to your account.</p>
                <br>
            </td>
        </tr>
        #parse("templates/email/footer_en_US.mt")
    </table>
</body>
</html>