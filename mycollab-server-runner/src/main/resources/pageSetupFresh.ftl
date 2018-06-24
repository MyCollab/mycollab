<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<#import "macroPage.ftl" as lib>
<@lib.headElements title="MyCollab Setup Assistant Page"/>
<style media="screen" type="text/css">
input {
    font-size:18px;
    height: 35px;
    width:100%;
}
label {
    display:block;
    margin-top:10px;
}
</style>
</head>
<body>
    <#include "pageLogo.ftl">
    <div id="spacing""></div>
    <div id="mainBody">
        <div id="title">
            <h1>Thank you for trial MyCollab<h1>
        </div>
        <hr size="1" style="margin: 1px 0 20px 0; ">
        <div id="title">
            <h3>Welcome to the MyCollab setup wizard. Please fill in the information below to complete the installation process.</h3>
            <h4>MyCollab is well tested on various platforms include Windows, Linux and MacOS. We have been
                spending countless hours to do the installation testing on
                as many machines as possible. If you can not install MyCollab successfully, please raise your case in
                 our <a href="http://support.mycollab.com/list/42580-general-help/" target="_blank">Support
                 page</a></h4>
        </div>
        <hr size="1" style="margin: 20px 0 1px 0; ">
        <div id="mainContent">
            <table style="width:100%">
                <tr>
                    <td style="vertical-align: top; width: 400px;"><div style="margin-top:10px;">MYCOLLAB SETUP</div></td>
                    <td style="display: inline-block; vertical-align: top; width:100%">
                        <form>
                            <table border="0" style="width:100%">
                                <tbody>
                                    <tr>
                                        <td><label for="sitename">Site name:</label></td>
                                    </tr>
                                    <tr>
                                        <td><input id="sitename" value="MyCollab"/></td>
                                    </tr>
                                    <tr>
                                        <td ><label for="serverAddress">Server address (without port value and http/https protocol):</label></td>
                                    </tr>
                                    <tr>
                                        <td><input id="serverAddress" placeholder="Example: 192.168.1.70 or myservername.com"/></td>
                                    </tr>
                                    <tr><td><h4>You can get this info from your web host. It could be an IP address or server name. You must not include the server port in this value. If you want to change the server port, please use <a href="http://community.mycollab.com/faq/#defaultport">this way</a></h4></td></tr>
                                </tbody>
                            </table>
                        </form>
                    </td>
                </tr>
            </table >
            <table style="width:100%;margin-top: 20px;">
                <tr>
                    <td style="vertical-align: top; width: 400px;"><div style="margin-top:10px;">DATABASE SETUP</div>
                        <h4>Configure your pre-created MyCollab database schema</h4>
                    </td>
                    <td style="display: inline-block; vertical-align: top; width:100%">
                        <form>
                            <table border="0" style="width:100%">
                                <tbody>
                                    <tr>
                                        <td><label for="databaseName">Database name: </label></td>
                                    </tr>
                                    <tr>
                                        <td><input id="databaseName" placeholder="Example: mycollab"/></td>
                                    </tr>
                                    <tr><td><h4>Name of MyCollab database. Database must be created before.</h4></td></tr>
                                    <tr>
                                        <td><label for="dbUserName">User name:</label></td>
                                    </tr>
                                    <tr>
                                        <td><input id="dbUserName"/></td>
                                    </tr>
                                    <tr>
                                        <td><h4>Database user name</h4></td>
                                    </tr>
                                    <tr>
                                        <td><label for="dbPassword">Password:</label></td>
                                    </tr>
                                    <tr>
                                        <td><input id="dbPassword" type="password"/></td>
                                    </tr>
                                    <tr><td><h4>Database password</h4></td></tr>
                                    <tr>
                                        <td><label for="databaseServer">Database server address:</label></td>
                                    </tr>
                                    <tr>
                                        <td><input id="databaseServer" placeholder="Example: localhost"/></td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <h4>You can get this info from your web host. If you use local Mysql, the address usually is <i>localhost:3306</i>.<h4>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </form>
                        <div class="right" style="margin-top: 10px;">
                            <button id="validate" class="v-button v-button-greenbtn" type="button" onclick="return databaseValidate();"><span style="font-size: 15px;">Check Connection</span></button>
                        </div>
                    </td>
                </tr>
            </table >
            <table style="width:100%;margin-top: 20px;">
                <tr>
                    <td style="vertical-align: top; width: 400px;"><div style="margin-top:10px;">EMAIL SETUP (Optional)<div>
                        <h4>Configure your outgoing SMTP email address to use with the software. You can configure your SMTP account later in MyCollab configuration file $\{MYCOLLAB_HOME}/conf/mycollab.properties</h4>
                    </td>
                    <td style="display: inline-block; vertical-align: top; width:100%">
                        <form>
                            <table border="0" style="width:100%">
                                <tbody>
                                    <tr>
                                        <td ><label for="smtpUserName">User name:</label></td>
                                    </tr>
                                    <tr>
                                        <td><input id="smtpUserName" placeholder="Example: mycollab@gmail.com"/></td>
                                    </tr>
                                    <tr>
                                        <td><h4>Username or account of your email service.
                                            <ul>
                                                <li>If you use Gmail, username is &lt;username&gt;@gmail.com</li>
                                                <li>If you use Outlook, username is &lt;username&gt;@outlook.com</li>
                                                <li>If you use Office365, username is &lt;username&gt;@yourdomainname</li>
                                                <li>Other email, please consult your email service provider</li>
                                            </ul>
                                            </h4>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td ><label for="smtpPassword">Password:</label></td>
                                    </tr>
                                    <tr>
                                        <td><input id="smtpPassword" type="password"/></td>
                                    </tr>
                                    <tr>
                                        <td><h4>Password of your email account<h4></td>
                                    </tr>
                                    <tr>
                                        <td><label for="smtpHost">Server name:</label></td>
                                    </tr>
                                    <tr>
                                        <td><input id="smtpHost"/></td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <h4>SMTP server address
                                                <ul>
                                                    <li>If you use Gmail, the value is smtp.gmail.com</li>
                                                    <li>If you use Outlook, the value is smtp-mail.outlook.com</li>
                                                    <li>If you use Office365, the value is smtp.office365.com</li>
                                                </ul>
                                            </h4>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><label for="smtpPort">Port:</label></td>
                                    </tr>
                                    <tr>
                                        <td><input id="smtpPort"/></td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <h4>If you use Gmail, Outlook or Office365, the port value is 587.<h4>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><label for="tls" >STARTTLS:</label><input id="tls" type="checkbox"/> <label for="ssl" >or SSL/TLS: </label><input id="ssl" type="checkbox"/></td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <h4>Some email services like Gmail, Outlook, Office365 require STARTTLS enables, other services like Yahoo may require TSL/SSL enable<h4>
                                         </td>
                                    </tr>
                                </tbody>
                            </table>
                        </form>
                        <div class="right" style="margin-top: 10px;">
                            <button id="validateEmailBtn" class="v-button v-button-greenbtn" type="button" onclick="return emailValidate();" style="width:140px"><span style="font-size: 15px;">Check Smtp</span></button>
                            <button id="setupBtn" class="v-button v-button-orangebtn" type="button" onclick="return updateInfoAction();" style="width:140px"><span style="font-size: 15px;">Setup</span></button>
                        </div>
                    </td>
                </tr>
            </table >
        </div>
        <#include "pageFooter.ftl">
    </div>
    <input type="hidden" id="postUrl" value=${postUrl}>
</body>
<script src="/assets/js/jquery-2.1.4.min.js"></script>
<script>
function databaseValidate(){
    if ($('#databaseName').val() == "") {
        alert("Database schema name must be not null");
        return;
    }

    if ($('#dbUserName').val() == "") {
        alert("Database username must be not null");
        return;
    }

    if ($('#dbPassword').val() == "") {
        alert("Database user password must be not null");
        return;
    }

    if ($('#databaseServer').val() == "") {
        alert("Database server address must be not null");
        return;
    }

    var urlValidate = "/validate";
    $('#validate').html('<img src="${defaultUrls.cdn_url}icons/lazy-load-icon.gif" alt="Pulpit rock" style="height:18px;" >');
    $.ajax({
          type: 'GET',
          url: urlValidate,
          data : {
                    databaseName : $('#databaseName').val().trim(),
                    dbUserName : $('#dbUserName').val().trim(),
                    dbPassword : $('#dbPassword').val().trim(),
                    databaseServer : $('#databaseServer').val().trim()
                },
          success: function(data) {
             if (data!=null) {
                if (data.length > 0) {
                    alert(data);
                    $('#validate').html('<span>Check Connection</span>');
                } else {
                    alert("Database connection is OK");
                    $('#validate').html('<span>Check Connection</span>');
                }
             }
          }
    });
}

function emailValidate() {
    if ($('#smtpHost').val() == "") {
        alert("Smtp server address must be not null");
        return;
    }

    if ($('#smtpPort').val() == "") {
        alert("Smtp server port must be not null");
        return;
    }

    var tlsStatus = "";
    if ($('#tls').is(":checked"))
    {
        tlsStatus = "true";
    }
    else
    {
        tlsStatus = "false";
    }

    var sslStatus = "";
    if ($('#ssl').is(":checked"))
    {
        sslStatus = "true";
    }
    else
    {
        sslStatus = "false";
    }

    var urlValidate = "/emailValidate";
    $('#validateEmailBtn').html('<img src="${defaultUrls.cdn_url}icons/lazy-load-icon.gif" alt="Pulpit rock" style="height:18px;" >');
    $.ajax({
          type: 'GET',
          url: urlValidate,
          data : {
                    smtpUserName : $('#smtpUserName').val().trim(),
                    smtpPassword : $('#smtpPassword').val().trim(),
                    smtpHost : $('#smtpHost').val().trim(),
                    smtpPort : $('#smtpPort').val().trim(),
                    tls : tlsStatus,
                    ssl : sslStatus
                },
          success: function(data){
             if(data!=null){
                if(data.length > 0){
                    alert(data);
                    $('#validateEmailBtn').html('<span>Check Smtp</span>');
                }else{
                    alert("Your SMTP setting is OK");
                    $('#validateEmailBtn').html('<span>Check Smtp</span>');
                }
             }
          }
    });
}

function updateInfoAction(){
    $('#requireMsg').html("").hide();
    if ($('#sitename').val() == "") {
        alert("Site name must be not null");
        return;
    }

    if ($('#serverAddress').val() == "") {
        alert("Server address must be not null");
        return;
    }

    if ($('#databaseName').val() == "") {
        alert("Database name must be not null");
        return;
    }

    if ($('#dbUserName').val() == "") {
        alert("Database username must be not null");
        return;
    }

    if ($('#dbPassword').val() == "") {
        alert("Database user password must be not null");
        return;
    }

    if ($('#databaseServer').val() == ""){
        alert("Database server address must be not null");
        return;
    }
    $('#setupBtn').html('<img src="${defaultUrls.cdn_url}icons/lazy-load-icon.gif" alt="Pulpit rock" style="height:18px;"><span style="font-size: 15px">&nbsp;&nbsp;Setting up...</span>');
    $('#setupBtn').after('<p><h3 style=\"color:orange\">Please be patient! It may takes a few minutes to set up MyCollab depends on your servers performance. Whenever the install process is completed, the browser redirects automatically to the application home page</h3></p>');
    var urlPost = "/install";

    var tlsStatus = "";
    if ($('#tls').is(":checked"))
    {
        tlsStatus = "true";
    }
    else
    {
        tlsStatus = "false";
    }

    var sslStatus = "";
    if ($('#ssl').is(":checked"))
    {
        sslStatus = "true";
    }
    else
    {
        sslStatus = "false";
    }

     $.ajax({
          type: 'POST',
          url: urlPost,
          data : {
                    sitename : $('#sitename').val().trim(),
                    serverAddress : $('#serverAddress').val().trim(),
                    databaseName : $('#databaseName').val().trim(),
                    dbUserName : $('#dbUserName').val().trim(),
                    dbPassword : $('#dbPassword').val().trim(),
                    databaseServer : $('#databaseServer').val().trim(),
                    smtpUserName : $('#smtpUserName').val().trim(),
                    smtpPassword : $('#smtpPassword').val().trim(),
                    smtpHost : $('#smtpHost').val().trim(),
                    smtpPort : $('#smtpPort').val().trim(),
                    tls : tlsStatus,
                    ssl: sslStatus
                },
          success: function(res) {
             setTimeout(function(){checkServerStarted();}, 10000)
          }
    });
}

function checkServerStarted() {
  $.get("/checkStarted", function(checkRes) {
    if (checkRes === "Started") {
        window.location.assign(location.protocol + "//" + document.getElementById("serverAddress").value + ((location.port != "")? (":" + location.port) : ""));
    } else {
        setTimeout(function(){checkServerStarted();}, 5000);
    }
  }).fail(function(data) {
      setTimeout(function(){checkServerStarted();}, 5000);
  });
}
</script>
</html>