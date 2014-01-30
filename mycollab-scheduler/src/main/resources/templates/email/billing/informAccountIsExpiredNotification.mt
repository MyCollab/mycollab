<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="icon" href="https://www.mycollab.com/favicon.ico" type="image/x-icon">
<link rel="shortcut icon" href="https://www.mycollab.com/favicon.ico" type="image/x-icon">
<style media="screen" type="text/css">
@import "https://fonts.googleapis.com/css?family=Monda:400,700";
@font-face {
    font-family: "verdana";
    font-style: normal;
    font-weight: bold;
    src: local("?"), url("https://s3.amazonaws.com/mycollab_assets/fonts/verdana.eot?#iefix") format("embedded-opentype"), url("https://s3.amazonaws.com/mycollab_assets/fonts/verdana.svg#verdana") format("svg"), url("https://s3.amazonaws.com/mycollab_assets/fonts/verdana.woff") format("woff"), url("https://s3.amazonaws.com/mycollab_assets/fonts/verdana.ttf") format("truetype");
}

#header {
    background-color: #646464;
    height: 70px;
}
.header-mid {
    display: block;
    padding-left: 50px;
    padding-top: 10px;
}
.header-mid .a {
    color: #FFFFFF;
    text-decoration: none;
}

.body-style {
    background-color: #F9F9F9;
    float: right;
    margin-bottom: 20px;
    padding-left: 30px;
    width: 840px;
}
#mainBody{
    font-size: 14px;
    text-align: left;
    border: 1px solid rgb(169, 169, 169);
    border-radius : 3px;
    width: 600px;
	height: 450px;
    margin: 0px auto;
}

#mainContent{
    display: block; 
    padding: 10px 30px 8px 30px;
}
</style>
<title>Your trial has ended</title>
</head>
<body>
	<table width="650" cellpadding="0" cellspacing="0" border="0" style="margin: 0px auto;">
		<tr>
			<td style="background: url('${defaultUrls.cdn_url}border_black_top.png') no-repeat 0 0/ 650px transparent; font-size: 11px; line-height: 11px;" height="6"></td>
		</tr>
		<tr>
			<td style="background: url('${defaultUrls.cdn_url}border_black_center.png') repeat-y 0 0/ 650px transparent; text-align: left; padding-bottom: 10px;">
				<div style="width: 150px; display: inline-block; padding-left: 20px;">
					<img src="${defaultUrls.cdn_url}logo_mycollab_2.png" alt="MyCollab-logo" width="250" height="50" style="margin: 0px; padding: 0px;">
				</div>
			</td>
		</tr>
		<tr>
			<td style="background: url('${defaultUrls.cdn_url}border_large_center_white.png') repeat-y 0 0 transparent; color: #4e4e4e; font: 13px 'Lucida Sans Unicode', 'Lucida Grande', sans-serif; padding: 10px 30px 0px;">
				<div id="mainContent">
			<div id="contentTitle">
				<p><span style="color: #196893; font: 22px 'Monda', sans-serif;"> Oh no, your trial has ended!</span></p>
				<hr size="1">
			</div>
			<div id="contentBody">
				<p><span style="font-weight:bold"> Hi $!userName</span>,</p>
				<span style="font: 14px">
				Your free trial of MyCollab has just ended. If you wish to continue using MyCollab to manage projects and tasks, you may enter a payment method in your account under Billing Info. <br><br>
				
				To access your account, visit  <a href="$!link" style="text-decoration : none;"><span style="font-weight: bold; color:#709AC5">$!link </span></a> <br><br>
				
				If you decide not to upgrade to a paid plan, you may still use MyCollab free for personal stuff. You don't need to do anything if you want to use it personally. We'll simply convert your account for you. If you wish to cancel your account entirely, please login and go to your Account section.<br></br>
				
				<span style="font-weight: bold;">Thank you!</span>
				</span>
				<div style="padding-top:10px;">
					<hr size="1">
				</div>
			</div>
			<div id="contentFooter" style="padding-top:10px;">
				<span>Question?</span>
				<a href="mailto:support@mycollab.com"><span style="font-weight: bold; color:#709AC5">Get help</span></a>
			</div>
		</div>
			</td>
		</tr>
		<tr>
			<td style="background: url('${defaultUrls.cdn_url}border_large_bottom_new.png') no-repeat 0 0 transparent; line-height: 7px; font-size: 7px;" height="7">&nbsp;</td>
		</tr>
	</table>
</body>
</html>	