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
    width: 680px;
	height: 530px;
    margin: 0px auto;
}

#mainContent{
    display: block; 
    padding: 10px 30px 8px 30px;
}
.v-button-bluebtn{
	background: url('${defaultUrls.cdn_url}grad-dark-bottom2.png') repeat-x left bottom
		#2599c8;
	border: 1px solid #093768;
	color: #FFFFFF;
	text-shadow: 1px 1px 0px #1570cd;
	border-radius: 3px;
	padding: 6px 8px 6px 8px;
	width: 82px;
}
.v-button-bluebtn:hover {
	background: url('${defaultUrls.cdn_url}grad-dark-bottom2.png') repeat-x left bottom
		#1377b3;
	border: 1px solid #093768;
	cursor: pointer;
}
</style>
<title>Your trial is about to end</title>
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
						<span>Thank you for choosing MyCollab!<span> <br>
						<span>You are just one click away from completing your account registration: <span> <br><br>
						<div style="text-align: center;">
							<a style="text-decoration:none;" href="$!linkConfirm" class="v-button-bluebtn"/><span style="font-family: 'verdana';font-size: 17px; ">Confirm your e-mail</span></a>
						</div> 
						<br>
						<span>By clicking this link, you agree to the 
						<a style="text-decoration : none;" href="https://www.mycollab.com/terms"><span style="font-size:14px; color: #4B80C6; text-decoration: none;">Terms of Service</span></a> and the 
						<a style="text-decoration : none;" href="https://www.mycollab.com/privacy"><span style="font-size:14px; color: #4B80C6;">Privacy Policy</span></a> <span> <br>
					</div>
					<div id="contentBody" style="padding-top: 15px;">
						<span style="font: 14px">
						If clicking on the link does not work, just copy and paste the following address into your browser:
						</span> &nbsp
						<a style="text-decoration : none;" href="https://www.mycollab.com"/> https://www.mycollab.com</a> <br> <br>
						If you are still having problems, simply forward this e-mail <a href="mailto:support@mycollab.com" style="text-decoration : none;"><span style="color:#5587BA">support@mycollab.com</span></a>, and we will be happy to help you. <br><br>
						
						<span style="font-weight: bold;">Have a productive day!</span>
						</span>
						<div style="padding-top:20px;">
							<hr size="1">
						</div>
					</div>
					<div id="contentFooter" style="padding-top:10px;">
						Best regards, <br>
						<span style="font-weight: bold;">MyCollab's Customer Support Team </span><br>
						(+84) 862-924-513 <br>
						<a href="mailto:support@mycollab.com" style="text-decoration : none;"><span style="font-weight: bold; color:#709AC5">support@mycollab.com </span></a>
					</div>
				</div>
			</td>
		</tr>
		<tr>
			<td style="background: url('${defaultUrls.cdn_url}border_large_bottom_new.png') no-repeat 0 0 transparent; line-height: 7px; font-size: 7px;" height="7">&nbsp;</td>
		</tr>
	</table>
	<input type="hidden" id="linkConfirm" value="$!linkConfirm"/>
</body> 
</html>	