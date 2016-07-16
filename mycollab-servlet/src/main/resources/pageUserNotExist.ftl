<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<#import "macroPage.ftl" as lib>
<@lib.head title="User not found"/>
<body>
    <#include "pageLogo.ftl">
    <div id="spacing"></div>
    <div id="mainBody">
        <div id="title">
            <h1>User is not existed</h1>
        </div>
        <hr size="1">
        <div>The user ${username} is not existed in our database.</div>
        <div style="text-align:right;">
            <button class="v-button v-button-orangebtn" type="button" onclick="return login();">Login</button>
        </div>
        <#include "pageFooter.ftl">
    </div>
</body>
<script src="${defaultUrls.cdn_url}js/jquery-2.1.4.min.js"></script>
<script>
    function login(){
        window.location.assign("${loginURL}");
    }
</script>
</html>