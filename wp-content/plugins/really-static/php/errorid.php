<?php
 
if($id==1)$e= __("Funktion allow_url_fopen is disabled and curl_init is also not aktivatet!<br> For more Informations check this <a href='http://sorben.org/really-static/fehler-allow_url_fopen.html'>manualpage</a><br> Really-static is automaticly deaktivated", 'reallystatic');
elseif($id==2)$e =__("Maybe you make a misstake please check <a href='http://sorben.org/really-static/fehler-quellserver.html'>manualpage</a>", 'reallystatic');
elseif($id==3)$e= __("Really-Static dont have enoth writing Rights at the destinationfolder ( $addinfo ) or the foldername may consist illigal signs. please check<a href='http://sorben.org/really-static/fehler-chmod.html'>manualpage</a>", 'reallystatic');
else $e= "No Errordescription found: '$id'";
echo $e;
RS_LOG($e);
?>