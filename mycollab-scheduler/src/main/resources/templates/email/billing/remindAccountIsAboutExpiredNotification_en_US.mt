<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="icon" href="${defaultUrls.cdn_url}favicon.ico" type="image/x-icon">
<link rel="shortcut icon" href="${defaultUrls.cdn_url}favicon.ico" type="image/x-icon">
</head>
<body style="background-color: rgb(218, 223, 225); color: #4e4e4e; font: 16px Georgia, serif; padding: 20px 0px;">
	#macro( confirmLink $webLink $displayText )
		<a href="$webLink" style="color: rgb(90, 151, 226); font-size: 16px; text-decoration: none;">$displayText</a>
	#end
	
	<table width="800" cellpadding="0" cellspacing="0" border="0" style="margin: 20px auto; background-color: rgb(255, 255, 255);">
       <tr>
       		<td style="text-align: center; padding: 50px 0px 0px;">
       			<img src="${defaultUrls.cdn_url}icons/logo_email.png" alt="The power productivity tool for your organization" width="130" height="30" style="margin: 0px; padding: 0px;">
       		</td>			
		</tr>
        <tr>
            <td style="padding: 20px 30px; text-align: center;">
				<p style="font-size: 22px; padding-top: 10px;"><b><i>Your trial is about to end. Please enter a payment method.</i></b><p>
				<hr>
				<div id="contentBody" style="text-align: left;">
					<p>Hi <b>$!userName</b>,</p>
					<p>
					We want to thank you again for trying MyCollab. Your trial is set to expires on <b>$!expireDay</b> and we wanted to remind you that in order to keep using MyCollab for project management, please visit your account and enter a valid form of payment under the Billing Info section. That way, there will be no interruption of service for you or your users. <br><br>
					
					You may still use MyCollab free for personal stuff if you decide not to keep projects in the system. You don't need to do anything if you want to use it personally. At the end of the trial, we'll simply convert your account for you.<br><br>
					
					To access your account, visit  
					#confirmLink( $!link $!link )
					<br><br>
					
					<span style="font-weight: bold;">Thank you!</span>
					</p>
				</div>
				<hr>
				<div id="contentFooter" style="padding-top:10px; text-align: left;">
					Best regards, <br>
					<span style="font-weight: bold;">MyCollab's Customer Support Team </span><br>
					(+84) 862-924-513 <br>
					#confirmLink( "mailto:support@mycollab.com" "support@mycollab.com" )
				</div>
			</td>
		</tr>
		#parse("templates/email/footer_en_US.mt")
	</table>
</body>
</html>	
