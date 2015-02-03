<?php
global $rewritestrID;
 
if (isset ( $_POST ["multidonate"] )) {
if( $_POST ["multidonate"]!=""){
	$get = @really_static_download ( "http://www.php-welt.net/really-static/donateask.php?id=" . $_POST ["multidonate"] . "&s=" . $_SERVER ["SERVER_NAME"] . "&ip=" . $_SERVER ["SERVER_ADDR"] );
 
	if (substr ( $get, 0, 1 ) == "1") {
		update_site_option ( 'rs_donationid', substr ( $get, 1 ) );
	} else {
		global $reallystaticsystemmessage;
		$reallystaticsystemmessage = "The PayPal transaction ID seams not to be right. Please try it again later, thank you!";
	}
	}else{
		global $reallystaticsystemmessage;
		$reallystaticsystemmessage = "The PayPal transaction ID seams not to be right. Please try it again later, thank you!";

	}
	$rewritestrID = $_POST ['strid2'];
 }


$ll=WPLANG;
if($ll=="" or $ll{2}!="_")$ll="en_US";
function reallystatic_settingssubmitbutton($titel,$akt=1,$onclick=""){
if($akt==1)$farbe="button-primary";
else $farbe="button-secondary";
if($onclick!="") $onclick=' onclick="'.$onclick.'"';
return '<span class="submit"><input'.$onclick.' type="submit" name="submit" id="submit" class="'.$farbe.'" value="'.$titel.'"  /></span>';

}

$menu=array();
     


#####################
echo '<link href="' . REALLYSTATICURLHOME . 'sonstiges/admin.css" rel="stylesheet" type="text/css" />';
echo "\n";
echo '<script type="text/javascript" src="' . REALLYSTATICURLHOME . 'sonstiges/admin.js"></script>';
echo "\n";


echo'<h1 class="reallystatic">'.__("Really Static Settings", 'reallystatic').'</h1>';
global $rs_messsage;

if(is_array($rs_messsage[e]))foreach ($rs_messsage[e] as $v)reallystatic_configerror ( 0,$v );#errormessages
if(is_array($rs_messsage[o]))foreach ($rs_messsage[o] as $v)reallystatic_configok ( $v, 1 ); # okmessages
if(is_array($rs_messsage[m]))foreach ($rs_messsage[m] as $v)reallystatic_configok ( $v, 1 ); # js messages
unset($rs_messsage);

echo '	<form method="post" id="rstatic_option-form">

<script type="text/javascript">
//
     var url =document.URL;
	 var u=url.split("/");
	 url=u[u.length-1];
	 u=url.split("#");
	 if(u[1]!="")var strID="#"+u[1];
	 else var strID="#rs_main";
	if(strID=="#undefined")  strID="#rs_main";
	';
#if(multiloaddaten("rs_donationid")=="")echo 'strID="#rs_start";';
if($rewritestrID!="")echo 'strID="#'.$rewritestrID.'";';

echo '
  //Javascript f�r das Tab Men�
  $(function () {
    var rstatic_panels = $("div.tabs > div");
	
    $("div.tabs ul.tabNavigation li").filter(strID).addClass("rstatic_active");
	
    rstatic_panels.hide().filter(strID).show();
 
    $("div.tabs ul.tabNavigation a").click(function () {
      rstatic_panels.hide();
      rstatic_panels.filter(this.hash).show();
 document.expert.strid.value=this.hash;
	   
     $("div.tabs ul.tabNavigation li").removeClass("rstatic_active");
	  
   $(this).parent().addClass("rstatic_active");
 
   //   $(this).addClass("rstatic_active");
      return false;
    }).filter(strID).click();
  });

    function toggleVisibility(id) {
       var e = document.getElementById(id);
       if(e.style.display == "inline")
          e.style.display = "none";
       else
          e.style.display = "inline";
    }
	function showexpert(a){
 
	if(a.checked==true){ 
		 document.getElementById("rs_settings").style.display="inline";
		document.getElementById("rs_advanced").style.display="inline";
		document.getElementById("rs_reset").style.display="inline";
	}else{ 
		 document.getElementById("rs_settings").style.display="none";
		document.getElementById("rs_advanced").style.display="none";
		document.getElementById("rs_reset").style.display="none";
	}
	
	
	}
	function hideshowupload(a){
	';
$dest=apply_filters ( "rs-adminmenu-transport",array());
foreach ($dest as $v)echo 'if(a.id=="fp'.$v[name].'")document.getElementById("shower'.$v[name].'").style.display="inline";
else document.getElementById("shower'.$v[name].'").style.display="none";';
echo '
			
	
	}
	
</script>
 




 <div class="tabs">
	   <ul class="tabNavigation" id="rstatic_tabs">';
     
$menu[]=array("id"=>"rs_main","name"=>__('Startseite', 'reallystatic'),"content"=>
 'Willkommen zur Really-Static Mulitpage konfiguration. Momentan gibt es hier keine Einstellungen, sondern nur die möglichkeit einen Globalgültigen Freischaltcode einzugeben ;-)<br>
 Alle wichtigen einstellungen werden in den Blogs selber festgelegt. Allerdings, ist es angedacht das hier Presets definiert werden können.');
 


     
$menu=apply_filters ( "rs-adminmenu-show",$menu);
$text="";

for($i = 0; $i < count($menu); $i++){
	if($menu[$i][name]!="")echo '<li id="'.$menu[$i][id].'"><a href="#'.$menu[$i][id].'">'.$menu[$i][name].'</a></li>';
	else echo '<li id="'.$menu[$i][id].'"></li>';
	$text.= '<div class="rstatic_panel" id="'.$menu[$i][id].'">'.$menu[$i][content].'</div>';	
}


echo '<li id="rs_donate" class="last"><a href="#rs_donate">';
if(multiloaddaten("rs_donationid")!="-" and multiloaddaten("rs_donationid")!="")echo __('About', 'reallystatic');
else{
if(multiloaddaten("rs_donationid")=="" or multiloaddaten("rs_donationid")=="-")echo __('<font color="red">please</font> ', 'reallystatic');
 echo ''.__('Donate', 'reallystatic').'';
 }
echo '</a></li></ul>';
echo '<div class="rstatic_panel" id="rs_donate">';
global $reallystaticsystemmessage;
if(multiloaddaten("rs_donationid")==""|| multiloaddaten("rs_donationid")=="-")echo '<form method="post"><input type="hidden" name="strid2" value="rs_donate" /><font color="red">'.$reallystaticsystemmessage.'</font>
  My plugins for Wordpress are "donationware". I develop, release, and maintain them for free, and you can use them for free, but I hope you find them worthy of a donation of thanks or encouragement. Registration costs one cent (a PayPal fee). If you choose to make a payment of $0.01 (or whatever minimum PayPal allows for your currency), PayPal takes that as a fee and I receive nothing, and that`s perfectly acceptable.
  <br><br>
  Your PayPal transaction ID is a fully valid registration code: <input name="multidonate" type="text" /><input name="Submit1" type="submit" value="Submit" /></form>
  <form action="https://www.paypal.com/cgi-bin/webscr" method="post">
<input type="hidden" name="cmd" value="_s-xclick">
<input type="hidden" name="hosted_button_id" value="9157614">
<input type="image" src="https://www.paypal.com/'.$ll.'/i/btn/btn_donate_LG.gif" border="0" name="submit" alt="Jetzt einfach, schnell und sicher online bezahlen � mit PayPal.">
<img alt="" border="0" src="https://www.paypal.com/'.$ll.'/i/scr/pixel.gif" width="1" height="1">
</form>

  '.versionupdate();
  elseif(multiloaddaten("rs_donationid")!="-")echo "<h2>License</h2>".multiloaddaten("rs_donationid");
else{
	echo "Thank you, for supporting really static!";
}
echo "<h2>Languagepack</h2>".__("This Languagepack is written by <a href='http://erik.sefkow.net'>Erik Sefkow</a> please <a href='http://erik.sefkow.net/donate.html'>donate me</a>", 'reallystatic');
echo "<h2>Really-Static Plugins</h2>";
#apply_filters ( "rs-aboutyourplugin");
echo '</div>';
#echo str_replace("</form>",' <input type="hidden" id="rs_expertmodehidden" name="rs_expertmode" value="'.multiloaddaten ( "rs_expertmode").'"></form>',$text);
echo $text;
/*echo '<form method="post" name="expert"><input type="hidden" name="setexpert" value="1" /><input type="hidden" name="strid" value="'.$_POST[strid2].'" /><input type="checkbox" name="rs_expertmode" onclick="document.expert.submit();" id="showexpertid" value="1" '.ison(multiloaddaten ( "rs_expertmode"),3,'checked ','',1).'><label for="showexpertid">show Expertsettings</label></form></div>
<script type="text/javascript">showexpert(document.getElementById("showexpertid"));hideshowupload(document.getElementById("fp'.multiloaddaten ( "rs_save").'"));</script>';

*/

/*if(multiloaddaten("rs_donationid")=="")echo '<br><br><center><!-- Facebook Badge START --><a href="http://www.facebook.com/pages/really-static-Wordpress-Plugin/177723978808" title="really static Wordpress Plugin" target="_TOP"><img src="http://badge.facebook.com/badge/177723978808.4655.159374760.png" width="360" height="101" style="border: 0px;" /></a><!-- Facebook Badge END --></center>';*/

?>