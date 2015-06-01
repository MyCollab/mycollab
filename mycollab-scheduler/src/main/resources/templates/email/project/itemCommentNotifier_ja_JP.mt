<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>New comment created</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css">
</head>
<body style="background-color: rgb(235, 236, 237); font: 13px Arial, 'Times New Roman', sans-serif; color: #4e4e4e; padding: 20px 0px;">
	#macro( hyperLink $displayName $webLink )
		<a href="$webLink" style="color: rgb(36, 127, 211); text-decoration: none; white-space: normal;">$displayName</a>
	#end
	
	#macro( messageBlock $messageContent )
		<div style="padding: 20px 15px; background-color: rgb(237, 248, 255); position: relative; color: rgb(71, 87, 116); text-align: left; word-wrap: break-word; white-space: normal; word-break: break-all;">
			<div style="color: #A7DDF9; font-size: 35px; line-height: 10px; text-align: left;">&ldquo;</div>
			<div style="padding:0px 20px; font-size: 12px; line-height: 1.6em;">$messageContent</div>
			<div style="color: #A7DDF9; font-size: 35px; line-height: 10px; text-align: right;">&bdquo;</div>
		</div>
	#end
	
    <table width="800" cellpadding="0" cellspacing="0" border="0" style="font: 13px Arial, 'Times New Roman', sans-serif; color: #4e4e4e; margin: 20px auto; background-color: rgb(255, 255, 255);">
       <tr>
       		<td>
       			<div style="padding: 10px 30px; background-color: rgb(106, 201, 228);">
       				<img src="${defaultUrls.cdn_url}icons/logo-email.png" alt="esofthead-logo" width="130" height="30"
       				style="margin: 0px; padding: 0px;">
       			</div>
       		</td>			
		</tr>
        <tr>
            <td style="color: #4e4e4e; padding: 10px 30px;">
				<p>$actionHeading</p>
				<p>
				#foreach( $title in $titles )
					#if( $foreach.count > 1 )
						<span style="color: rgb(36, 127, 211);">&#9474;</span>
					#end
					#hyperLink( $title.displayName $title.webLink )
				#end
				</p>
				<p><b>
				#hyperLink( $summary $summaryLink )
				</b></p>
				#messageBlock( $!comment.changecomment )
			</td>
		</tr>
		#parse("templates/email/footer_ja_JP.mt")
	</table>
</body>
</html>