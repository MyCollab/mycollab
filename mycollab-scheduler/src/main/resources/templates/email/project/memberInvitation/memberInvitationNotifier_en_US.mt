<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Project Invitation</title>
</head>
<body style="background-color: ${styles.background}; font: ${styles.font}; padding: 0px;">
	#macro( linkBlock $webLink )
		<div style="width: 100%; padding: 20px 15px; background-color: rgb(237, 248, 255); box-sizing: border-box;">
			<a href="$webLink" style="color: ${styles.link_color}; font-size: 12px; width: 100%; display: inline-block; word-wrap: break-word; white-space: normal; word-break: break-all;">$webLink</a>
		</div>
	#end
	
	#macro( messageBlock $messageContent )
        <div style="padding: 20px 15px; background-color: rgb(237, 248, 255); position: relative; color: rgb(71, 87, 116); text-align: left; word-wrap: break-word; white-space: normal;">
            <div style="color: rgb(167, 221, 249); font-size: 35px; line-height: 10px; text-align: left;">&ldquo;</div>
            <div style="padding:0px 20px; font-size: 12px; line-height: 1.6em;">$messageContent</div>
            <div style="color: rgb(167, 221, 249); font-size: 35px; line-height: 10px; text-align: right;">&bdquo;</div>
        </div>
    #end
	
	<table width="700" cellpadding="0" cellspacing="0" border="0" style="margin: 20px auto;">
       <tr>
       		<td>
       			<div style="padding: 0px 25px;">
       				<img src="${defaultUrls.cdn_url}icons/logo.png" alt="The power productivity tool for your organization" width="130" height="30"
       				style="margin: 0px; padding: 0px;">
       			</div>
       		</td>			
		</tr>
        <tr>
            <td style="padding: 10px 25px;">
            	<div><img src="${defaultUrls.cdn_url}icons/default_user_avatar_16.png" width="16" height="16"
            	style="display: inline-block; vertical-align: top;"/>$inviteUser has <b>invited</b> you to join the project "$!member.projectName".</div>
				<p>Please, accept the invitation at:</p>
				#linkBlock( $!urlAccept )
				<p>or decline it and cancel further reminders at:</p>
				#linkBlock( $!urlDeny )
			</td>
		</tr>
		<tr>
		<td style="padding: 10px 25px;">
		#messageBlock( $inviteMessage )
		</td>
		</tr>
		#parse("templates/email/footer_en_US.mt")
	</table>
</body>
</html>