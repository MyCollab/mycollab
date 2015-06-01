<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Recovery password</title>
</head>
<body style="background-color: rgb(235, 236, 237); font: 13px Arial, 'Times New Roman', sans-serif; color: #4e4e4e; padding: 20px 0px;">
	#macro( linkBlock $webLink )
		<div style="padding: 20px 15px; background-color: rgb(237, 248, 255);">
			<a href="$webLink" style="color: rgb(76, 131, 182); font-size: 12px; text-decoration: underline; width: 100%; display: inline-block; word-wrap: break-word; white-space: normal; word-break: break-all;">$webLink</a>
		</div>
	#end
	
	<table width="800" cellpadding="0" cellspacing="0" border="0" style="margin: 20px auto; background-color: rgb(255, 255, 255);">
       <tr>
       		<td>
       			<div style="padding: 10px 30px; background-color: rgb(106, 201, 228);">
       				<img src="${defaultUrls.cdn_url}icons/logo-email.png" alt="esofthead-logo" width="130" height="30"
       				style="margin: 0px; padding: 0px;">
       			</div>
       		</td>			
		</tr>
        <tr>
            <td style="padding: 10px 30px;">
            	<br>
				<div style="display: block; border-bottom: 1px solid rgb(212, 212, 212); padding-bottom: 5px; margin-bottom: 10px;"><b>$!username</b>様、</div>
				<span style="text-align: left; word-wrap: break-word; white-space: normal; word-break: break-all;">パスワード変更要求のため、このEメールを受けました。 </span><br>
				<span style="text-align: left; word-wrap: break-word; white-space: normal; word-break: break-all;">パスワード変更手続きをおこなう際は、下記のリンクをクリックしてください。 </span> <br><br>
				
				#linkBlock( $!urlRecoveryPassword )
				
				<p style="text-align: left; word-wrap: break-word; white-space: normal; word-break: normal;">パスワード変更手続きを行わない際は、このメールを無視してください。別のユーザーが誤ってユーザー名を入力した可能性があります。あなたのアカウントに何も変更しないとなります。</p>
				<br>
            </td>
        </tr>
        #parse("templates/email/footer_ja_JP.mt")
    </table>
</body>
</html>