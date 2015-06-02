<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="icon" href="https://www.mycollab.com/favicon.ico" type="image/x-icon">
<link rel="shortcut icon" href="https://www.mycollab.com/favicon.ico" type="image/x-icon">
</head>
<body style="background-color: rgb(218, 223, 225); color: #4e4e4e; font: 16px Georgia, serif; padding: 20px 0px;">
	#macro( confirmLink $webLink $displayText )
		<a href="$webLink" style="color: rgb(90, 151, 226); font-size: 16px; text-decoration: none;">$displayText</a>
	#end
	
	#macro( linkBlock $webLink )
		<div style="padding: 20px 15px; background-color: rgb(237, 248, 255);">
			<a href="$webLink" style="color: rgb(76, 131, 182); font-size: 16px; text-decoration: underline; width: 100%; display: inline-block; word-wrap: break-word; white-space: normal; word-break: break-all;">$webLink</a>
		</div>
	#end
	
	<table width="760" cellpadding="0" cellspacing="0" border="0" style="margin: 20px auto; background-color: rgb(255, 255, 255);">
       <tr>
       		<td style="text-align: center; padding: 50px 0px 0px;">
       			<img src="${defaultUrls.cdn_url}icons/logo-email.png" alt="esofthead-logo" width="130" height="30" style="margin: 0px; padding: 0px;">
       		</td>			
		</tr>
        <tr>
            <td style="padding: 10px 50px; text-align: center;">
						<p style="font-size: 22px;"><b><i>MyCollab をお選びいただき、ありがとうございます！</i></b><p>
						<p>You are just one click away from completing your account registration: <p>
						<div style="background-color: rgb(32, 36, 35); tex-align: center; padding: 3px 0px; width: 330px; margin: 0px auto;">
							<div style="width: 100%; padding: 10px 0px; border-color: rgb(99, 102, 101); border-width: 1px 0px; border-style: solid;">
								<a style="text-decoration:none;" href="$!linkConfirm"/><span style="font-size: 22px; text-transform: uppercase; color: rgb(255, 255, 255);">Eメールをご確認下さい。</span></a>
							</div>
						</div> 
						<br/>
						<p style="text-align: left;">何時でも、#confirmLink ($siteUrl $siteUrl) にてアカウントがアクセスできます。（今後のご参考のため、ページをブックマークしてください。）<br/>
						メールアドレス
						#set ($mailToUrl = "mailto:" + $user.Email)
						#confirmLink ($mailToUrl $user.Email)
						 と登録したパスワードでログインしてください。</p>
						<p style="text-align: left;">こちらのリンクをクリックすると、 
						#confirmLink ("https://www.mycollab.com/terms" "Terms of Service")
						 と 
						#confirmLink ("https://www.mycollab.com/privacy" "Privacy Policy")
						に同意されたものとみなされます。
						</p>
						<p style="text-align: left;">
						クリックすると、リンクが動作できない場合は、下記のアドレスをコピーして、ブラウザにてアクセスして下さい。
						</p>
						#linkBlock ($!linkConfirm)
						<p style="text-align: left;">
						何か問題が御座いましたら、こちらのEメールを 
						#confirmLink ("mailto:support@mycollab.com" "support@mycollab.com")
						に転送してください。問題を解決すべくサポート致します。 <br><br>
						
						<span style="font-weight: bold;">生産的な一日を過ごして下さい！</span>
						</p>
						<p style="text-align: left;">あなたのビジネスの売上を成長させてため、Mycollabのご使用をお楽しみください！試用中でも各計画を切り替えることができるとなります。</p>
					</div>
					<div id="contentFooter" style="padding:10px 0px 50px; text-align: left;">
						何卒よろしくお願いいたします。<br>
						<span style="font-weight: bold;">MyCollab's Customer Support Team </span><br>
						(+84) 862-924-513 <br>
						<a href="mailto:support@mycollab.com" style="text-decoration : none;"><span style="font-weight: bold; color:#709AC5">support@mycollab.com </span></a>
					</div>
				</div>
			</td>
		</tr>
		#parse("templates/email/footer_ja_JP.mt")
	</table>
</body> 
</html>	