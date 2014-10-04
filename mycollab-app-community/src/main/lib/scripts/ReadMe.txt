The following are scripts implementing diverse "extra" functionality for YAJSW.
For more details please check the script file and the project documentation.


Groovy scripts executed as trigger actions (eg if a text matches application console output, or on state changes):

sendMail.gv
trayMessage.gv
trayColor.gv
snmpTrap.gv

Groovy scripts executed as conditions (eg script checks condition cyclically and starts/stops application accordingly)

timeCondition.gv
fileCondition.gv
commandCondition.gv

Groovy script executed on MSCS cluster change

cluster.gv

Groovy script for calculating the delay on application restart

linearRestartDelay.gv

Groovy script executed within the application process before the application is started (generally to map network forlders before a service is started)

mapNetworkDrive.gv

Groovy script for monitoring the application

maxStartup.gv
maxDuration.gv

