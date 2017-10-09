#=====================================================
# You can visit link https://community.mycollab.com/configuration/
# to get all configuration fieldBuilder and their meanings
#=====================================================

#=====================================================
#    SITE CONFIGURATION
#=====================================================
site.name=${sitename}
server.address=${serveraddress}


#=====================================================
#    DATABASE CONFIGURATION
#=====================================================
db.driverClassName=com.mysql.jdbc.Driver
db.url=${dbUrl}
db.username=${dbUser}
db.password=${dbPassword}

#=====================================================
#    MAIL CONFIGURATION
#
# SMTP Mail setting to use in 
#=====================================================
mail.smtphost=${smtpAddress}
mail.port=${smtpPort}
mail.username=${smtpUserName}
mail.password=${smtpPassword}
mail.isTLS=${smtpTLSEnable}
mail.isSSL=${smtpSSLEnable}
mail.notify=${mailNotify}

#=====================================================
#    ERROR REPORTING
# This email is used to receive any error causes during 
# MyCollab running. It just collects java stack trace not
# any end user sensitive data. In case you are serious not
# want to send report automatically to our team, you can
# leave this field to empty
#=====================================================
error.sendTo=error@mycollab.com