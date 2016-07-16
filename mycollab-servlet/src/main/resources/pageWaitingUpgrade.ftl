<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<#import "macroPage.ftl" as lib>
<@lib.head title="Upgrading MyCollab ..."/>
<body>
    <#include "pageLogo.ftl">
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