<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="height:100%;">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="icon" href="https://www.mycollab.com/favicon.ico" type="image/x-icon">
<link rel="shortcut icon" href="https://www.mycollab.com/favicon.ico" type="image/x-icon">
<link rel="stylesheet" type="text/css" href="${defaultUrls.app_url}assets/css/cssVelocityPage.css">
<style media="screen" type="text/css">
#container {
    background-image: url('${defaultUrls.cdn_url}icons/footer-clouds.png');  background-repeat: no-repeat;
    background-position: bottom right;
}

</style>
<title>招待が終了しました</title>
</head>
<body>
	<div id="container">
		#parse("templates/page/pageHeader.mt")
		<div id="body">
			<div id="spacing"></div>
			<div id="mainBody">
				<div id="title">
					<h1>招待が終了しました</h1>
				</div>
				<hr size="1">
				<div>
						すみません！この招待が7日間以内に承諾できるので、招待を再送信されるようにプロジェクト管理者にてお問い合わせください。
				</div>
				<div class="right">
					<button class="v-button v-button-orangebtn" type="button" onclick="return login();"><span>ログイン</span></button>
				</div>
				#parse("templates/page/pageFooter_ja_JP.mt")
			</div>
		</div>
	</div>
</body>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script>
	function login(){
		window.location.assign("$!loginURL");
	}
</script>
</html>
