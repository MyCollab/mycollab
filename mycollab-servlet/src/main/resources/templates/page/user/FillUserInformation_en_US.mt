<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="height: 100%;">
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
<title>User accept the invitation page</title>
</head>
<body style="height: 100%; margin: 0; padding: 0; width: 100%;">
	<div id="container">
		#parse("templates/page/pageHeader.mt")
		<div id="body">
			<div id="spacing""></div>
			<div id="mainBody">
				<div id="title">
					<h1>Welcome <span style="font-style:italic; font-size:20px;">$!username</span><h1>
				</div>
				<hr size="1" style="margin: 1px 0 1px 0; ">
				<hr size="1" style="margin: 1px 0 1px 0; ">
				<div>
					<table >
					 	<tr>
					 		<td style="vertical-align: top; width: 400px;"><h3 >Thank you for accepting the invitation!<br> Please enter your password<h3></td>
					 		<td style="display: inline-block; vertical-align: top;">
								<div id="mainContent">
					  				 <div>
										<form>
										<table border="0" style="width:100%">
										<tbody>
										<tr>
											<td "><label for="password"><h3 style="margin-top:0">New Password:<h3></label></td>
										</tr>
										<tr>
											<td><input id="password" maxlength="45" name="password" type="password" style="width:100%;height:35px;border: 1px solid rgb(169, 169, 169);"/></td>
										</tr>
										<tr>
											<td><label for="password"><h3>Confirm New Password:<h3></label></td>
										</tr>
										<tr>
											<td><input id="repassword"  name="password" type="password" style="width:100%;height:35px;border: 1px solid rgb(169, 169, 169);"/></td>
										</tr>
										</tbody></table>
										</form>
									</div>
									<div style="padding-top: 15px; padding-left:250px;">
										<button class="v-button v-button-orangebtn" type="button" onclick="return updateInfoAction();"><span style="font-family: 'Georgia';font-size: 15px;">Update & Go</span></button>
									</div>
								</div>
							</td>
					 	</tr>
					</table>
				</div>
				
				#parse("templates/page/pageFooter_en_US.mt")
			</div>
		</div>
	</div>
	<input type="hidden" id="username" value="$!username">
	<input type="hidden" id="accountId" value="$!accountId">
	<input type="hidden" id="redirectURL" value="$!redirectURL">
</body>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script>
	$(document).ready(function(){
	});
	function updateInfoAction(){
		$('#requireMsg').html("").hide();
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
		      			password : $('#password').val().trim(), accountId : $('#accountId').val() ,
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