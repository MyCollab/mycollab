<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Recovery password</title>
</head>
<body>
	<table width="650" cellpadding="0" cellspacing="0" border="0" style="margin: 0px auto;">
		<tr>
			<td style="background: url('${defaultUrls.cdn_url}border_large_top_new.png') no-repeat 0 0 transparent; font-size: 11px; line-height: 11px;" height="11">&nbsp;</td>
		</tr>
		<tr>
			<td style="background: url('${defaultUrls.cdn_url}border_large_center_orange.png') repeat-y 0 0 transparent; text-align: center; padding-bottom: 10px;"><div style="width: 440px; display: inline-block; vertical-align: middle; text-align: left;"><span style="font: bold 18px Tahoma, Geneva, sans-serif; color: white;">Recovery password</span></div><div style="width: 150px; display: inline-block; vertical-align: middle;"><img src="${defaultUrls.cdn_url}logo_new.png" alt="esofthead-logo" width="150" height="45" style="margin: 0px; padding: 0px;"></div>
			</td>
		</tr>
		<tr>
			<td style="background: url('${defaultUrls.cdn_url}border_large_center_new.png') repeat-y 0 0 transparent; color: #4e4e4e; font: 13px 'Lucida Sans Unicode', 'Lucida Grande', sans-serif; padding: 10px 30px 0px;">
				<div style="font-weight: bold; display: block; border-bottom: 1px solid rgb(212, 212, 212); padding-bottom: 5px; margin-bottom: 10px;">Hi $!username,</div>
				<span style="text-align: left; font-size:12px; word-wrap: break-word; white-space: normal; word-break: break-all;">[MyCollab] We has received a password change request for this email. </span><br>
				<span style="text-align: left; font-size:12px; word-wrap: break-word; white-space: normal; word-break: break-all;">If you made this request, then please click on link below: </span> <br><br>
				
				<span style="text-align: left; word-wrap: break-word; white-space: normal; word-break: break-all;"><a href="$!urlRecoveryPassword" style="color: #4283c4; font: 10px 'Lucida Sans Unicode', 'Lucida Grande', sans-serif; text-decoration: underline;">$!urlRecoveryPassword</a></span><br><br>
				
				<span style="text-align: left; font-size:12px;word-wrap: break-word; white-space: normal; word-break: break-all;">If you did not ask to change your password, then please ignore this email. Another user may have entered your username by mistake. No change will be made to your account.</span>
				<br>
				<br>
            </td>
        </tr>
        #parse("templates/email/footer.mt")
    </table>
</body>
</html>