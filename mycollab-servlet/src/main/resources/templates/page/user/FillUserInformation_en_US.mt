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
<title>Create New Password</title>
</head>
<body style="height: 100%; margin: 0; padding: 0; width: 100%;">
	<div id="container">
		#parse("templates/page/pageHeader.mt")
		<div id="body">
			<div id="spacing"></div>
			<div id="mainBody">
				<div id="title">
					<h1>Welcome <span style="font-style:italic; font-size:20px;">$!username</span>
					</h1>
				</div>
				<hr size="1">
				<div>
					<table style="width: 100%" cellspacing="0" cellpadding="0" >
					 	<tr>
					 		<td style="vertical-align: top; width: 50%; padding-top: 10px;">Thank you for accepting the invitation! While you are new to MyCollab, please enter your password:</td>
					 		<td style="vertical-align: top; width: 50%;">
								<div id="mainContent">
					  				 <div>
										<form>
										<table border="0" style="width:100%" cellspacing="0" cellpadding="0">
										<tbody>
										<tr>
											<td><label for="password">Password:</label></td>
										</tr>
										<tr>
											<td><input id="password" maxlength="45" name="password" type="password"/></td>
										</tr>
										<tr>
                                            <td style="height:10px;"></td>
                                        </tr>
										<tr>
											<td><label for="password">Verify Password:</label></td>
										</tr>
										<tr>
											<td><input id="repassword"  name="password" type="password"/></td>
										</tr>
										</tbody></table>
										</form>
									</div>
									<div style="padding-top: 15px; text-align: right;">
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
			alert("Password is required");
			return;
		}
		if($('#repassword').val()==""){
			alert("Verify password is required");
			return;
		}
		if($('#password').val() != $('#repassword').val()){
			alert("Password don't match");
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
		      	 		alert("Your password has been set successfully");
		      	 		window.location.assign("$!loginURL");
		      	 	}
		      	 }
		      }
		});
	}
</script>				
</html>
