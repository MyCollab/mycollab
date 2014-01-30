<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="height:100%;">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="icon" href="https://www.mycollab.com/favicon.ico" type="image/x-icon">
<link rel="shortcut icon" href="https://www.mycollab.com/favicon.ico" type="image/x-icon">
<style media="screen" type="text/css">
.container {
}
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
<title>User deny invitation feedback page</title>
</head>
<body style="height: 100%; margin: 0; padding: 0; width: 100%;">
	<div id="container" style="height:100%;">
		<div id="header">
			<div class="header-mid">
				<a href="https://www.mycollab.com">
					<img src="${defaultUrls.cdn_url}logo_mycollab_2.png" alt="Logo MyCollab">
				</a>
			</div>
		</div>
		<div id="body" style="background-color: rgb(239, 239, 239); width: 100%; height:100%;">
			<div id="spacing" style="height:60px; background-color: rgb(239, 239, 239);"></div>
			<div id="mainBody">
				<div id="title">
					<p><span style="color: #196893; font: 22px 'Monda', sans-serif; padding-left: 8px;">Please Feedback To Inviter</span></p>
				</div>
				<hr size="1" style="padding-left:8px; width:755px;">
				<div>
					<p style="padding: 0px 8px 0px 8px;"><span style="vertical-align:top; padding-top:12px;font: 16px 'verdana', sans-serif; color: #616161;">Oops! We are sorry because you do not want to join MyCollab. Could you please drop some lines to tell reason to the inviter?
					</span></p>
					<div style="display: block; padding: 8px 8px 8px 8px;">
                    <textarea id="message" rows="8" cols="90" style="width:750px;">
                    </textarea>
                	</div>
                    <div style="display: block; padding-left: 588px;">
                        <button class="v-button-bluebtn" type="button" onclick="return sendEmailFeedBack();"><span style="font-family: 'verdana';font-size: 15px; ">Send</span></button>&nbsp&nbsp
                        <button class="v-button-bluebtn" type="button" onclick="return skip();"><span style="font-family: 'verdana';font-size: 14px; ">Skip</span></button>
                    </div>
                    <div id="requireMsg" style="display: none; padding: 12px 8px 8px 20px;">
                        <p><span style="color:red; font-style:italic">
                            (*) Reason
                        </span></p>
                    </div>
				</div>
			</div>
			<div id="bottom">
			    <p>
			    	<a javascrip="void(0);" href="https://www.mycollab.com/terms" style="text-decoration : none;"><span style="font: 11px 'Lucida Grande', sans-serif; color: #1777AD; font-weight:bold;">Terms of Service</span></a> &nbsp&nbsp&nbsp
					<span style="color: #000000">|</span>
					&nbsp&nbsp&nbsp<a javascrip="void(0);" href="https://www.mycollab.com/privacy" style="text-decoration : none;"><span style="font: 11px 'Lucida Grande', sans-serif; color: #1777AD; font-weight:bold;">Privacy Policy</span></a>&nbsp&nbsp&nbsp
					<span style="color: #000000">|</span>
					&nbsp&nbsp&nbsp<a javascrip="void(0);" href="https://www.mycollab.com" style="text-decoration : none;"><span style="font: 11px 'Lucida Grande', sans-serif; color: #1777AD; font-weight:bold;">Copyright 2013 MyCollab. All rights reserved.</span></a>
				</p>
			</div>
		</div>
	</div>
	<input type="hidden" id="inviterEmail" value="$!inviterEmail">
    <input type="hidden" id="url" value="$!redirectURL">
    <input type="hidden" id="toEmail" value="$!toEmail">
    <input type="hidden" id="toName" value="$!toName">
    <input type="hidden" id="inviterName" value="$!inviterName">
</body>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script>
    $(document).ready(function(){
        $('#message').val("");
        $('#message').focus();
    });
    function sendEmailFeedBack(){
        $('#requireMsg').hide();
        if($('#message').val().trim() == ""){
            $('#requireMsg').show();
            return;
        }   
        var url = encodeURI($('#url').val().trim());
             $.ajax({
                  type: 'POST',
                  url: url,
                  data : {inviterEmail : $('#inviterEmail').val().trim() ,toEmail : $('#toEmail').val().trim(), message : $('#message').val().trim(),
                    toName : $('#toName').val().trim() , inviterName: $('#inviterName').val().trim()},
                  complete: function(data){
                     alert('Send email successfully');
                     window.location.assign("http://www.mycollab.com/");
                  }
                });
    }
    function skip(){
        window.location.assign("http://www.mycollab.com/");
    }
</script>               
</html>