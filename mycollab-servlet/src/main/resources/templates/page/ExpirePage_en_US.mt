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
<title>The invitation is expired</title>
</head>
<body>
    <div id="container">
        #parse("templates/page/pageHeader.mt")
        <div id="body">
            <div id="spacing"></div>
            <div id="mainBody">
                <div id="title">
                    <h1>The invitation is expired</h1>
                </div>
                <hr size="1">
                <div>
                        You can only accept this invitation within 7 days. Please ask your Project Admin to resend the invitation.
                </div>
                <div class="right">
                    <button class="v-button v-button-orangebtn" type="button" onclick="return login();">Login</button>
                </div>
                #parse("templates/page/pageFooter_en_US.mt")
            </div>
        </div>
    </div>
</body>
<script src="${defaultUrls.cdn_url}js/jquery-2.1.4.min.js"></script>
<script>
    function login(){
        window.location.assign("$!loginURL");
    }
</script>
</html>
