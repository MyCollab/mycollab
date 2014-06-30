<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="height:100%;">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="icon" href="https://www.mycollab.com/favicon.ico" type="image/x-icon">
<link rel="shortcut icon" href="https://www.mycollab.com/favicon.ico" type="image/x-icon">
<link rel="stylesheet" type="text/css" href="${defaultUrls.app_url}assets/css/cssVelocityPage.css">
<style>

#container {
    background-image: url('${defaultUrls.cdn_url}footer-clouds.png');  background-repeat: no-repeat;  background-position: bottom right;
}
</style>
<title>User has denied</title>
</head>
<body style="height: 100%; margin: 0; padding: 0; width: 100%;">
	<div id="container" style="height:100%;">
		#parse("templates/page/pageHeader.mt")
		<div id="body">
			<div id="spacing"></div>
			<div id="mainBody">
				<div id="title">
					<h1>User has denied</h1>
				<hr size="1">
				<div >
					<h3>
						Oops! Sorry, Maybe you has denied the invitation. We're very glad to show you what things which My Collab Online Tools can help your business. Welcome and take a look. 
					</h3>
				</div>
				<div style="text-align:right;">
					<button class="v-button v-button-orangebtn" type="button" onclick="return login();"><span style="font-family: 'verdana';font-size: 15px;">Take a look</span></button>
				</div>
				#parse("templates/page/pageFooter.mt")
		</div>
	</div>
</body>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script>
	function login(){
		window.location.assign("https://www.mycollab.com");
	}
</script>				
</html>
