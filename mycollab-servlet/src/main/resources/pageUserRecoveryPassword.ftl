<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<#import "macroPage.ftl" as lib>
<@lib.head title="Reset your password"/>
<body>
    <#include "pageLogo.ftl">
    <div id="spacing"></div>
    <div id="mainBody">
        <div id="title">
            <h1>Reset your password</h1>
        </div>
        <hr size="1">
        <table style="width: 100%" cellspacing="0" cellpadding="0">
            <tr>
                <td style="width: 50%; vertical-align:top; padding-top:20px;">Create a strong password with a
                            combination of lowercase, uppercase, and digits. Passwords are case sensitive and must be 6
                            or more characters in length.
                </td>
                <td style="vertical-align: top; width: 50%">
                    <div id="mainContent" style="height:100%;">
                        <div>
                            <form>
                                <table border="0" style="width: 100%" cellspacing="0" cellpadding="0">
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
                                            <td><input id="repassword" maxlength="45" name="password" type="password"/></td>
                                        </tr>
                                    </tbody>
                                </table>
                            </form>
                        </div>
                        <div style="padding-top: 15px; text-align: right;">
                            <button class="v-button v-button-orangebtn" type="button" onclick="return updateInfoAction();">Update</button>
                        </div>
                    </div>
                </td>
            </tr>
        </table>
        <#include "pageFooter.ftl">
    </div>
    <input type="hidden" id="username" value="${username}">
    <input type="hidden" id="loginURL" value="${loginURL}">
    <input type="hidden" id="redirectURL" value="${redirectURL}">
</body>
<script src="${defaultUrls.cdn_url}js/jquery-2.1.4.min.js"></script>
<script>
    $(document).ready(function(){
    });
    function updateInfoAction(){
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
                password : $('#password').val().trim()
                },
                success: function(data) {
                    if (data!=null) {
                        if (data.length > 0) {
                            alert(data);
                        } else {
                            alert("Your password has been changed successfully");
                            window.location.assign("${loginURL}");
                        }
                    }
                }
        });
    }
</script>
</html>