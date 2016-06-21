
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="height: 100%;">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="icon" href="${defaultUrls.cdn_url}favicon.ico" type="image/x-icon">
<link rel="shortcut icon" href="${defaultUrls.cdn_url}favicon.ico" type="image/x-icon">
<link rel="stylesheet" type="text/css" href="${defaultUrls.cdn_url}css/main.css">
<style media="screen" type="text/css">
#container {
    background-image: url('${defaultUrls.cdn_url}icons/footer_clouds.png');  background-repeat: no-repeat;
    background-position: bottom right;
}
#mainContent {
padding: 10px 0 8px 0px;
}
input {
    font-size:20px;
    line-height:35px;
    width:100%;
    border: 1px solid rgb(169, 169, 169);
}
label {
    display:block;
    margin-top:10px;
}

h3 {
    padding-right:20px;
}

#body {
    letter-spacing: 0.5px;
}
</style>
<title>MyCollab is upgrading ...</title>
</head>
<body style="height: 100%; margin: 0; padding: 0; width: 100%;">
    <div id="container">
        <#include "pageHeader.ftl">
        <div id="body">
            <div id="spacing""></div>
            <div id="mainBody">
                <div id="title">
                    <h1 style="font-size:40px; margin-bottom:15px;">MyCollab is upgrading ...<h1>
                </div>
                <hr size="1" style="margin: 1px 0 20px 0; ">
                <div id="title" style="display:flex">
                    <h3>The upgrade process may take several minutes. Please be patient ... <h3>
                    <img src="${defaultUrls.cdn_url}icons/lazy-load-icon.gif" alt="Pulpit rock" style="height:18px;">
                </div>
                <#include "pageFooter.ftl">
            </div>
        </div>
    </div>
    <input type="hidden" id="postUrl" value=${postUrl}>
</body>
<script src="/assets/js/jquery-2.1.4.min.js"></script>
<script type="text/javascript">
    $(document).ready(function () {
        checkUpgradeReady();
    });
    function checkUpgradeReady(){
      var urlPost = "/it/upgrade_status";
      $.ajax({cache:false, type: 'GET', url: urlPost,
        success: function(data) {
          if(data!=null){
            if(data == "Still upgrading") {
              setTimeout(checkUpgradeReady, 5000);
            } else {
              alert("MyCollab is upgraded successfully. Redirect to the app?");
              window.location.assign("/");
            }
          }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
          setTimeout(checkUpgradeReady, 5000);
        }
      });
    }
</script>
</html>