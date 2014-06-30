<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="height:100%;">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="icon" href="https://www.mycollab.com/favicon.ico" type="image/x-icon">
<link rel="shortcut icon" href="https://www.mycollab.com/favicon.ico" type="image/x-icon">
<link rel="stylesheet" type="text/css" href="${defaultUrls.app_url}assets/css/cssVelocityPage.css">
<style media="screen" type="text/css">
#container {
    background-image: url('${defaultUrls.cdn_url}footer-clouds.png');  background-repeat: no-repeat;  background-position: bottom right;
}

</style>
<title>Page not found</title>
</head>
<body style="height: 100%; margin: 0; padding: 0; width: 100%;">
	<div id="container" style="height:100%;">
		#parse("templates/page/pageHeader.mt")
		<div id="body" >
			<div id="spacing" "></div>
			<div id="mainBody">
				<div id="title">
					<h1>Page not found</h>
				</div>
				<hr size="1">
				<div id="content" style="padding-top: 20px">
					<div id="content_left">
						<div id="exclamation_mark">&#33;</div>
						<div id="error_display">
							<div id="error_code">404</div>
							<div id="error_brief">Page not found, sorry</div>
						</div>
						<div class="clear"></div>
					</div>
					<div id="content_right">
						<div id="error_excuse">
							<p>The page you are looking for might have been removed, had its name changed or is temporarily unavailable. Please try the following:</p>
							<ul>
								<li>Make sure that the Web site address displayed in the address bar of your browser is spelled and formatted correctly.</li>
								<li>If you reached this page by clicking a link, contact us to alert us that the link is incorrectly formatted.</li>
								<li>Forget that this ever happened, and go browse the files :)</li>
							</ul>
						</div>
						<div id="back_to_home" style="padding-left:60px;padding-top:20px;">
							<a class="v-button-bluebtn" style="text-decoration : none;" href="https://www.mycollab.com">Go back to home page</a>
						</div>
						<div class="clear"></div>
					</div>
					<div class="clear"></div>
				</div>
				#parse("templates/page/pageFooter.mt")
			</div>
		</div>
	</div>
</body>
</html>	