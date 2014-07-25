<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="icon" href="http://www.mycollab.com/favicon.ico" type="image/x-icon">
<link rel="shortcut icon" href="http://www.mycollab.com/favicon.ico" type="image/x-icon">
</head>
<body style="background-color: rgb(218, 223, 225); color: #4e4e4e; font: 16px Georgia, serif; padding: 20px 0px;">
	#macro( confirmLink $webLink $displayText )
		<a href="$webLink" style="color: rgb(90, 151, 226); font-size: 16px; text-decoration: none;">$displayText</a>
	#end
	
	<table width="800" cellpadding="0" cellspacing="0" border="0" style="margin: 20px auto; background-color: rgb(255, 255, 255);">
       <tr>
       		<td style="text-align: center; padding: 50px 0px 0px;">
       			<img src="${defaultUrls.cdn_url}logo-email-big.png" alt="esofthead-logo" width="218" height="50" style="margin: 0px; padding: 0px;">
       		</td>			
		</tr>
        <tr>
            <td style="padding: 20px 30px; text-align: center;">
				<p style="font-size: 22px; padding-top: 10px;"><b><i>試用期間が終了しました。お支払い方法を選んで下さい。</i></b><p>
				<hr>
				<div id="contentBody" style="text-align: left;">
					<p><b>$!userName</b>様、</p>
					<p>
					MyCollabのご試用、どうも有難う御座いました。 試用期間が <b>$!expireDay</b> に無効となります。プロジェクトやタスクを管理するため、MyCollabを引き続きご使用の場合は、アカウントをアクセスし、アカウントの請求情報にて、有効なお支払い方法を選んでください。そうすれば、あなたやあなたの利用者に対するサービスが中断されません。 <br><br>
					
					システム内にプロジェクトを維持しない場合でも、個人的にMyCollabを無料に使用することができます。個人的に使用するため、何もする必要がありません。試用の終了時に、アカウントが変換されます。<br/><br/>
					
					アカウントを
					#confirmLink( $!link $!link )
					にてアクセスして下さい。
					<br><br>
					
					<span style="font-weight: bold;">どうも有り難う御座います！</span>
					</p>
				</div>
				<hr>
				<div id="contentFooter" style="padding-top:10px; text-align: left;">
					何卒よろしくお願いいたします。<br>
					<span style="font-weight: bold;">MyCollab's Customer Support Team </span><br>
					(+84) 862-924-513 <br>
					#confirmLink( "mailto:support@mycollab.com" "support@mycollab.com" )
				</div>
			</td>
		</tr>
		#parse("templates/email/footer_ja_JP.mt")
	</table>
</body>
</html>	
