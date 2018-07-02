#=====================================================
# You can visit link http://community.mycollab.com/configuration/
# to get all configuration fieldBuilder and their meanings
#=====================================================

#=====================================================
#    SITE CONFIGURATION
#=====================================================
app.siteName=${sitename}
app.notifyEmail=${mailNotify}

server.address=${serveraddress}
server.apiUrl=http://api.mycollab.com/api/
server.storageSystem=file
server.siteUrl=http://%s:%d
server.resourceDownloadUrl=http://%s:%d/file/
server.cdnUrl=http://%s:%d/assets/

#=====================================================
#    DATABASE CONFIGURATION
#=====================================================
spring.datasource.driverClassName=com.mysql.jdbc.Driver
spring.datasource.url=${dbUrl}
spring.datasource.username=${dbUser}
spring.datasource.password=${dbPassword}

#=====================================================
#    MAIL CONFIGURATION
#
# SMTP Mail setting to use in 
#=====================================================
mail.smtphost=${smtpAddress}
mail.port=${smtpPort}
mail.username=${smtpUserName}
mail.password=${smtpPassword}
mail.startTls=${smtpTLSEnable}
mail.ssl=${smtpSSLEnable}

#=====================================================
#    ERROR REPORTING
# This email is used to receive any error causes during 
# MyCollab running. It just collects java stack trace not
# any end user sensitive data. In case you are serious not
# want to send report automatically to our team, you can
# leave this field to empty
#=====================================================
error.sendTo=error@mycollab.com