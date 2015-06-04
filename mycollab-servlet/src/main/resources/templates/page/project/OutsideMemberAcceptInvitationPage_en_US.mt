<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="height:100%;">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="icon" href="${defaultUrls.cdn_url}favicon.ico" type="image/x-icon">
<link rel="shortcut icon" href="${defaultUrls.cdn_url}favicon.ico" type="image/x-icon">
<link rel="stylesheet" type="text/css" href="${defaultUrls.cdn_url}css/cssVelocityPage.css">
<style media="screen" type="text/css">
#container {
    background-image: url('${defaultUrls.cdn_url}icons/footer_clouds.png');  background-repeat: no-repeat;
    background-position: bottom right;
}
</style>
<title>Accept invitation page</title>
</head>
<body style="height: 100%; margin: 0; padding: 0; width: 100%;">
    <div id="container" style="height:100%;">
        #parse("templates/page/pageHeader.mt")
        <div id="body" >
            <div id="spacing"></div>
            <div id="mainBody">
                <div id="title">
                    <h1>Thank you for joining to the project '$!projectName'</h1>
                </div>
                <hr size="1">
                <div>
                    <table style="width: 100%" cellspacing="0" cellpadding="0">
                        <tr>
                            <td style="width: 50%; vertical-align:top; padding-top:12px;">Your user name is '$!email'. Please enter your password</td>
                            <td style="width: 50%; vertical-align: top;">
                                <div id="mainContent">
                                     <div>
                                        <form>
                                        <table border="0" style="width: 100%" cellspacing="0" cellpadding="0">
                                        <tbody>
                                        <tr>
                                            <td><h3>New Password:</h3></td>
                                        </tr>
                                        <tr>
                                            <td><input id="password" maxlength="45" name="password" type="password"/></td>
                                        </tr>
                                        <tr>
                                            <td style="height:20px;"></td>
                                        </tr>
                                        <tr>
                                            <td><h3>Confirm New Password:</h3></td>
                                        </tr>
                                        <tr>
                                            <td><input id="repassword" maxlength="45" name="password" type="password"/></td>
                                        </tr>
                                        </tbody></table>
                                        </form>
                                    </div>
                                    <div style="padding-top: 15px; text-align: right;">
                                        <button class="v-button v-button-orangebtn" type="button" onclick="return createAccount();">Save</button>&nbsp&nbsp&nbsp
                                        <button class="v-button v-button-graybtn" type="button" onclick="return cancel();">Cancel</button>
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
    <input type="hidden" id="handelCreateAccountURL" value="$!handelCreateAccountURL">
    <input type="hidden" id="projectId" value="$!projectId">
    <input type="hidden" id="sAccountId" value="$!sAccountId">
    <input type="hidden" id="email" value="$!email">
    <input type="hidden" id="roleId" value="$!roleId">
</body>
<script src="${defaultUrls.cdn_url}js/jquery-1.10.2.min.js"></script>
<script>
    $(document).ready(function(){
    });
    function createAccount(){
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
        var url = encodeURI($('#handelCreateAccountURL').val());
         $.ajax({
              type: 'POST',
              url: url,
              data : { email : $('#email').val().trim(),
                        password : $('#password').val().trim(),
                        roleId : $('#roleId').val().trim(),
                        projectId : $('#projectId').val().trim(),
                        sAccountId : $('#sAccountId').val()},
              success: function(data){
                 if(data!=null){
                    if(data.length > 0){
                        alert(data);
                    }else{
                        alert("Your password has been set successfully.");
                        window.location.assign("$!projectLinkURL");
                    }
                 }
              }
        });
    }
    function cancel(){
        window.location.assign("$!projectLinkURL");
    }
</script>				
</html>