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
<title>Member deny invitation feedback page</title>
</head>
<body style="height: 100%; margin: 0; padding: 0; width: 100%;">
	<div id="container" style="height:100%;">
		#parse("templates/page/pageHeader.mt")
		<div id="body" >
			<div id="spacing" "></div>
			<div id="mainBody">
				<div id="title">
					<h1>Please Feedback To Inviter</h1>
				</div>
				<hr size="1">
				<div >
					<h3>
					Oops! We are sorry because you do not want to join the ${projectName} project. Could you please drop some lines to tell reason to the inviter?
					</h3>
					<div style="display: block; padding: 8px 8px 8px 8px;">
                    <textarea id="message" rows="8" cols="90" style="width:750px;">
                    </textarea>
                	</div>
                    <div style="display: block; padding-left: 588px;">
                        <button class="v-button v-button-orangebtn" type="button" onclick="return sendEmailFeedBack();"><span style="font-family: 'verdana';font-size: 15px;">Send</span></button>&nbsp&nbsp
                        <button class="v-button v-button-blankbtn" type="button" onclick="return skip();"><span style="font-family: 'verdana';font-size: 15px;">Skip</span></button>
                    </div>
                    <div id="requireMsg" style="display: none; padding: 12px 8px 8px 20px;">
                        <p><span style="color:red; font-style:italic">
                            (*) Reason
                        </span></p>
                    </div>
				</div>
				#parse("templates/page/pageFooter_en_US.mt")
			</div>			
		</div>
	</div>
	<input type="hidden" id="inviterEmail" value="$!inviterEmail">
    <input type="hidden" id="url" value="$!redirectURL">
    <input type="hidden" id="toEmail" value="$!toEmail">
    <input type="hidden" id="toName" value="$!toName">
    <input type="hidden" id="inviterName" value="$!inviterName">
    <input type="hidden" id="sAccountId" value="$!sAccountId">
    <input type="hidden" id="projectId" value="$!projectId">
    <input type="hidden" id="projectRoleId" value="$!projectRoleId">
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
                    toName : $('#toName').val().trim() , inviterName: $('#inviterName').val().trim(), sAccountId: $('#sAccountId').val().trim(),
                    projectId : $('#projectId').val().trim() , projectRoleId : $('#projectRoleId').val().trim() },
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