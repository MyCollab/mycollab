<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="height:100%;">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="icon" href="https://www.mycollab.com/favicon.ico" type="image/x-icon">
<link rel="shortcut icon" href="https://www.mycollab.com/favicon.ico" type="image/x-icon">
<link rel="stylesheet" type="text/css" href="${defaultUrls.cdn_url}css/cssVelocityPage.css">
<style media="screen" type="text/css">
#container {
    background-image: url('${defaultUrls.cdn_url}icons/footer-clouds.png');  background-repeat: no-repeat;
    background-position: bottom right;
}

</style>
<title>ページが見つかりません</title>
</head>
<body style="height: 100%; margin: 0; padding: 0; width: 100%;">
	<div id="container" style="height:100%;">
		#parse("templates/page/pageHeader.mt")
		<div id="body" >
			<div id="spacing"></div>
			<div id="mainBody">
				<div id="title">
					<h1>ページが見つかりません</h1>
				</div>
				<hr size="1">
				<div id="content" style="padding-top: 20px">
					<div id="content_left">
						<div id="exclamation_mark">&#33;</div>
						<div id="error_display">
							<div id="error_code">404</div>
							<div id="error_brief">ページが見つかりません</div>
						</div>
						<div class="clear"></div>
					</div>
					<div id="content_right">
						<div id="error_excuse">
							<p>お探しのページが見つかりません。悪いリンクか、又は間違ったアドレスのいずれかの問題です。何か問題が発生したと判断したら、<a class="v-button-bluebtn" style="text-decoration : none;" href="mailto:support@mycollab.com">ご連絡ください。<a/></p>
						</div>
						<div id="back_to_home" style="padding-left:30px;">
							<a class="v-button v-button-orangebtn" style="text-decoration : none;" href="https://www.mycollab.com">ホームページへ戻る</a>
						</div>
						<div class="clear"></div>
					</div>
					<div class="clear"></div>
				</div>
				#parse("templates/page/pageFooter_ja_JP.mt")
			</div>
		</div>
	</div>
</body>
</html>	