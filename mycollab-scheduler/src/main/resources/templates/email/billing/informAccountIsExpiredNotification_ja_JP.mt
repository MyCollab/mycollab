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
				<p style="font-size: 22px; padding-top: 10px;"><b><i>試用期間が終了しました！</i></b><p>
				<hr size="1">
				<div id="contentBody" style="text-align: left;">
					<p><b>$!userName</b> 様</p>
					<p>
					MyCollabの無料トライアルが終了することになりました。プロジェクトやタスクを管理するため、MyCollabを引き続きご使用の場合は、アカウントの請求情報にて、お支払い方法を選んでください。 <br/>
					
					アカウントを  
					#confirmLink( $!link $!link )
					にてアクセスして下さい。
					<br/>
					
					有料版にアップグレードしない場合でも、個人的のためMyCollabを無料に使用することができます。個人的に使用するため、何もする必要がありません。あなたのアカウントが変換されます。アカウントを完全にキャンセルする場合は、ログインし、アカウントセクションにて設定して下さい。<br/>
					
					<p><b>どうも有り難う御座います！</b></p>
					</p>
					<div style="padding-top:10px;">
						<hr size="1">
					</div>
				</div>
				<div id="contentFooter" style="padding-top:10px; text-align: left;">
					<span>ご質問は？</span>
					#confirmLink( "mailto:support@mycollab.com" "ヘルプへ" )
				</div>
			</td>
		</tr>
		#parse("templates/email/footer_ja_JP.mt")
	</table>
</body>
</html>	