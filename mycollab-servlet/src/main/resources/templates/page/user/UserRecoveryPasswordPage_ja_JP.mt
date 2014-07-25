<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="height:100%;">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="icon" href="https://www.mycollab.com/favicon.ico" type="image/x-icon">
<link rel="shortcut icon" href="https://www.mycollab.com/favicon.ico" type="image/x-icon">
<link rel="stylesheet" type="text/css" href="https://s3.amazonaws.com/mycollab_assets/fonts/verdana.svg#verdana">
<link rel="stylesheet" type="text/css" href="https://s3.amazonaws.com/mycollab_assets/fonts/verdana.svg#verdana">

<link rel="stylesheet" type="text/css" href="${defaultUrls.app_url}assets/css/cssVelocityPage.css">
<style media="screen" type="text/css">
#container {
    background-image: url('${defaultUrls.cdn_url}footer-clouds.png');  background-repeat: no-repeat;  background-position: bottom right;
}

</style>
<title>User recovery password page</title>
</head>
<body style="height: 100%; margin: 0; padding: 0; width: 100%;">
	<div id="container" style="height:100%;">
		#parse("templates/page/pageHeader.mt")
		<div id="body">
			<div id="spacing"></div>
			<div id="mainBody">
				<div id="title">
					<h1>Choose Your New Password
					</h1>
				</div>
				<hr size="1">
				<div >
					<table>
					 	<tr>
					 		<td style="width: 350px; vertical-align:top; padding-top:20px;">Please enter a new password!</td>
					 		<td style="display: inline-block; vertical-align: top;">
								<div id="mainContent" style="height:100%;">
					  				 <div>
										<form>
										<table border="0">
										<tbody>
										<tr>
											<td style="padding-top:8px;"><label for="password">New Password:</label></td>
										</tr>
										<tr>
											<td><input id="password" maxlength="45" name="password" type="password"/></td>
										</tr>
										<tr>
											<td style="height:10px;"></td>
										</tr>
										<tr>
											<td><label for="password">Confirm New Password:</label></td>
										</tr>
										<tr>
											<td><input id="repassword" maxlength="45" name="password" type="password"/></td>
										</tr>
										</tbody></table>
										</form>
									</div>
									<div style="padding-top: 15px; text-align: right;">
										<button class="v-button v-button-orangebtn" type="button" onclick="return updateInfoAction();"><span style="font-family: 'verdana';font-size: 15px;">Reset</span></button>
									</div>
								</div>
							</td>
					 	</tr>
					</table>
				</div>
				#parse("templates/page/pageFooter_ja_JP.mt")
			</div>
			
		</div>
	</div>
	<input type="hidden" id="username" value="$!username">
	<input type="hidden" id="loginURL" value="$!loginURL">
	<input type="hidden" id="redirectURL" value="$!redirectURL">
</body>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script>
	$(document).ready(function(){
	});
	function updateInfoAction(){
		if ($('#password').val() == ""){
			alert("Please enter password");
			return;
		}
		if($('#repassword').val()==""){
			alert("Please retype password");
			return;
		}
		if($('#password').val() != $('#repassword').val()){
			alert("You enter password mismatch with retype password, please re-enter");
			return;
		}
		var url = encodeURI($('#redirectURL').val());
		 $.ajax({
		      type: 'POST',
		      url: url,
		      data : {
		      			username : $('#username').val().trim(), 
		      			password : $('#password').val().trim()
		      		},
		      success: function(data){
		      	 if(data!=null){
		      	 	if(data.length > 0){
		      	 		alert(data);
		      	 	}else{
		      	 		alert("Your account has been updated.");
		      	 		window.location.assign("$!loginURL");
		      	 	}
		      	 }
		      }
		});
	}
</script>				
</html>