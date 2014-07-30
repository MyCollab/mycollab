<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Project Invitation</title>
</head>
<body style="background-color: rgb(235, 236, 237); color: #4e4e4e; padding: 20px 0px;">
	#macro( linkBlock $webLink )
		<div style="width: 100%; padding: 20px 15px; background-color: rgb(237, 248, 255);">
			<a href="$webLink" style="color: rgb(76, 131, 182); font-size: 12px; text-decoration: underline; width: 100%; display: inline-block; word-wrap: break-word; white-space: normal; word-break: break-all;">$webLink</a>
		</div>
	#end
	
	#macro( messageBlock $messageContent )
        <div style="padding: 20px 15px; background-color: rgb(237, 248, 255); position: relative; color: rgb(71, 87, 116); text-align: left; word-wrap: break-word; white-space: normal; word-break: break-all;">
            <div style="color: rgb(167, 221, 249); font-size: 35px; line-height: 10px; text-align: left;">&ldquo;</div>
            <div style="padding:0px 20px; font-size: 12px; line-height: 1.6em;">$messageContent</div>
            <div style="color: rgb(167, 221, 249); font-size: 35px; line-height: 10px; text-align: right;">&bdquo;</div>
        </div>
    #end
	
	<table width="700" cellpadding="0" cellspacing="0" border="0" style="margin: 20px auto; background-color: rgb(255, 255, 255);">
       <tr>
       		<td>
       			<div style="padding: 10px 50px; background-color: rgb(106, 201, 228);">
       				<img src="${defaultUrls.cdn_url}logo-email.png" alt="esofthead-logo" width="130" height="30" style="margin: 0px; padding: 0px;">
       			</div>
       		</td>			
		</tr>
        <tr>
            <td style="font: 12px 'Lucida Sans Unicode', 'Lucida Grande', sans-serif; padding: 10px 50px;">
            	<div><img src="${defaultUrls.cdn_url}default_user_avatar_16.png" width="16" height="16" style="display: inline-block; vertical-align: top;"/>$inviteUser has よりあなたは、オンラインコラボレーションツールでのプロジェクト "$!member.projectName" のチームに招待されました。</div>
				<p>承認される場合は、下記アドレスをクリックしてください。</p>
				#linkBlock( $!urlAccept )
				<p>拒否される場合、または心あたりのない場合は、下記アドレスをクリックしてください。</p>
				#linkBlock( $!urlDeny )
			</td>
		</tr>
		<tr>
		<td>
        #messageBlock( $inviteMessage )
        </td>
        </tr>
		#parse("templates/email/footer_ja_JP.mt")
	</table>
</body>
</html>