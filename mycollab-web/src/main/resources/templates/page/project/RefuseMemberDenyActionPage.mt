<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="height:100%;">
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
    background-color: #1777AD;
    height: 70px;
    width: 100%;
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
   background-color: #FFFFFF;
   -moz-box-sizing: border-box;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    padding-left : 15px;
    padding-right: 15px;
    padding-bottom: 11px;
    font-size: 12px;
    text-align: left;
    padding-top : 8px;
    border: 1px solid rgb(169, 169, 169);
    border-radius : 3px;
    width: 800px;
    margin: 0 auto;
}

#mainContent{
     background-color: #F6F6F6;
   -moz-box-sizing: border-box;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    display: block; 
    padding: 10px 10px 8px 10px;
    border: 1px solid rgb(200, 200, 200);
    height : 200px;
}

#bottom{
    padding-left: 160px; 
    padding-right: 100px; 
    color : blue;
    font-style: bold;
    background-color: rgb(239, 239, 239);
    text-align: center;
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
<title>Refuse deny action page</title>
</head>
<body style="height: 100%; margin: 0; padding: 0; width: 100%;">
	<div id="container" style="height:100%;">
		<div id="header">
			<div class="header-mid">
				<a href="https://www.mycollab.com">
					<img src="${defaultUrls.cdn_url}logo_mycollab_2.png" alt="Logo Mycollab">
				</a>
			</div>
		</div>
		<div id="body" style="background-color: rgb(239, 239, 239); height: 100%;">
			<div id="spacing" style="height:60px; background-color: rgb(239, 239, 239);"></div>
			<div id="mainBody">
				<div id="title">
					<p><span style="color: #196893; font: 22px 'Monda', sans-serif;">You Can Not Leave This Project</span></p>
				</div>
				<hr size="1">
				<div style="vertical-align:center;padding: 8px 0px 0px 0px;">
					<span style="vertical-align:center; padding-top:20px;font: 16px 'verdana', sans-serif; color: #616161;">
						Sorry! You can not leave this project by yourself. Please request your project admin remove you out of project if you like so.
					</span>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
				</div>
				<div style="padding-left:686px;">
					<button class="v-button-bluebtn" type="button" onclick="return login();"><span style="font-family: 'verdana';font-size: 15px;">Login</span></button>
				</div>
			</div>
			<div id="bottom">
			    <p>
			    	<a javascrip="void(0);" href="https://www.mycollab.com/terms" style="text-decoration : none;"><span style="font: 11px 'Lucida Grande', sans-serif; color: #1777AD; font-weight:bold;">Terms of Service</span></a> &nbsp&nbsp&nbsp
					<span style="color: #000000">|</span>
					&nbsp&nbsp&nbsp<a javascrip="void(0);" href="https://www.mycollab.com/privacy" style="text-decoration : none;"><span style="font: 11px 'Lucida Grande', sans-serif; color: #1777AD; font-weight:bold;">Privacy Policy</span></a>&nbsp&nbsp&nbsp
					<span style="color: #000000">|</span>
					&nbsp&nbsp&nbsp<a javascrip="void(0);" href="https://www.mycollab.com" style="text-decoration : none;"><span style="font: 11px 'Lucida Grande', sans-serif; color: #1777AD; font-weight:bold;">Copyright 2014 MyCollab. All rights reserved.</span></a>
				</p>
			</div>
		</div>
	</div>
</body>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script>
	function login(){
		window.location.assign("$!projectLinkURL");
	}
</script>				
</html>
