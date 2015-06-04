<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>User denied your invitation</title>
</head>
<body>
	<table width="650" cellpadding="0" cellspacing="0" border="0" style="margin: 0px auto;">
		<tr>
			<td style="background: url('${defaultUrls.cdn_url}icons/email/border_large_top.png') no-repeat 0 0
			transparent; font-size: 11px; line-height: 11px;" height="11">&nbsp;</td>
		</tr>
		<tr>
			<td style="background: url('${defaultUrls.cdn_url}icons/email/border_large_center_orange.png') repeat-y
			0 0 transparent; text-align: center; padding-bottom: 10px;"><div style="width: 440px; display:
			inline-block; vertical-align: middle; text-align: left;"><span style="font: bold 18px Tahoma, Geneva, sans-serif; color: white;">MyCollab Invitation</span></div><div style="width: 150px; display: inline-block; vertical-align: middle;"><img src="${defaultUrls.cdn_url}icons/logo_email.png" alt="The power productivity tool for your organization" width="130" height="30" style="margin: 0px; padding: 0px;"></div>
			</td>
		</tr>
		<tr>
			<td style="background: url('${defaultUrls.cdn_url}icons/email/border_large_center.png') repeat-y 0 0
			transparent; color: #4e4e4e; font: 13px 'Lucida Sans Unicode', 'Lucida Grande', sans-serif; padding: 10px 30px 0px;">
				<div style="font-weight: bold; display: block; border-bottom: 1px solid rgb(212, 212, 212); padding-bottom: 5px; margin-bottom: 10px;">Hi there,</div>
				<div style="display: block; padding: 8px; background-color: rgb(247, 228, 221);">User "$!notification.changecomment" has denied for the invitation of project <a href="$!projectUrl" style="color: rgb(216, 121, 55); text-decoration: underline;">$!project.name</a>.</div>
			</td>
		</tr>
		#parse("templates/email/footer_ja_JP.mt")
	</table>
</body>
</html>