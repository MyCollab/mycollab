<?php
/*
 *
*/
add_filter("rs-aboutyourplugin",create_function('$b,$a','$n="Localfile-Snapin (v1.00 final)"; if($a==1)echo "<b>$n:</b> programmed by Erik Sefkow<br>";else return $b.$n.", ";'),10,2);


add_filter("rs-transport","rs_local");
/*
0   verbinden
1	abmelden
2	dateischreiben
3	datei löschen
4	dateiinhalt schreiben
5	test auf verbundenheit
6	einstellungenspeichern
7	Domainprefix for your cached files
8	Url to the templatefolder
9	einstellungen zu array
10	array zu einstellungen
11	install
12	uninstall
13	upgrade
 *
*/
function rs_local($transportfuntions){
	$transportfuntions[local]=array(
			"rs_local_connect",
			"rs_local_disconnect",
			"rs_local_writefile",
			"rs_local_deletefile",
			"rs_local_writecontent",
			"rs_local_isconnected",
			"rs_local_saveoptions",
			
			"rs_local_remoteurl",
			"rs_local_remotetemplateurl",
			"rs_local_settingstoarray",
			"rs_local_arraytosettings",
			
			"rs_local_install",
			"rs_local_uninstall",
			"rs_local_upgrade"
			);
	return $transportfuntions;
}
add_filter("rs-adminmenu-transport","localadmin");
function localadmin($array){
if(get_option("rs_differentpaths")==true)$sub='<tr><td valign="top" width="400px">
	'.__('Domainprefix for your cached files', 'reallystatic').'</td><td>:<input name="rs_local_remoteurl" size="50" type="text" value="' . 	get_option('rs_local_remoteurl') . '"	/> <a style="cursor:pointer;"  onclick="toggleVisibility(\'remoteurl\');" >[?]</a>  <div style="max-width:500px; text-align:left; display:none" id="remoteurl">( where your visitors find your blog )</div></td></tr>'
. '<tr><td valign="top" >'.__('Url to the templatefolder', 'reallystatic').'</td><td>:<input name="rs_local_remotetemplateurl" size="50" type="text" value="' . get_option ( "rs_local_remotetemplateurl" ) . '"	/> <a style="cursor:pointer;"  onclick="toggleVisibility(\'designurl\');" >[?]</a>  <div style="max-width:500px; text-align:left; display:none" id="designurl">( for example: '. loaddaten ( "rs_designlocal", 'reallystatic' ).' )</div></td></tr>'
;


	$array[]=array(name=>"local",title=>__('work with local filesystem', 'reallystatic'),main=>'<div style="margin-left:50px;"><table border="0" width="100%"><tr><td valign="top" width="350px">'.__('internal filepath from to cachedfiles', 'reallystatic').'</td><td>:<input name="rs_lokalerspeicherpfad" size="50" type="text" value="' . loaddaten ( "rs_lokalerspeicherpfad" , 'reallystatic') . '"	/> '.rs_fragehelp(__('the path inside your system e.g. "/www/html/".If it should saved to maindirectory write "/" ', 'reallystatic')).'</td></tr>'.$sub.'</table></div><br>');
	return $array;

}
#if($_POST ['strid'] == "rs_destination" and $_POST['realstaticspeicherart']=="local"){
add_filter("rs-adminmenu-savealltransportsettings","rs_local_saveoptions");


function rs_local_saveoptions(){
	global $rs_messsage;
	if(isset($_POST["rs_local_remoteurl"]))update_option ( 'rs_local_remoteurl',$_POST["rs_local_remoteurl"]);
	if(isset($_POST["rs_local_remotetemplateurl"]))update_option ( 'rs_local_remotetemplateurl',$_POST["rs_local_remotetemplateurl"]);
	
	update_option ( 'rs_lokalerspeicherpfad', str_replace("\\\\","\\",$_POST ['rs_lokalerspeicherpfad'] ));
	$e=substr ( $_POST ['rs_lokalerspeicherpfad'], - 1 );
	if ($e != rs_de(2))$rs_messsage[e][]= str_replace("/",rs_de(2),__("You may forgot a / at the end of the path!", "reallystatic" ));
 
}
function rs_local_isconnected(){
	return true;
}
function rs_local_connect(){
	global $rs_isconnectet;
	$rs_isconnectet=true;
}
function rs_local_disconnect(){
	global $rs_isconnectet;
	$rs_isconnectet=false;
}
function rs_local_writefile($ziel, $quelle){
	  if(!rs_hostlinux()){$ziel=str_replace(array("\\\\","/"),array("\\","\\"),$ziel);$quelle=str_replace(array("\\\\","/"),array("\\","\\"),$quelle);}
	$ziel= get_option ( "rs_lokalerspeicherpfad").$ziel;
	# RS_LOG(" @copy($quelle,$ziel);");
	$fh=@copy($quelle,$ziel);
#  RS_LOG("111");
	if($fh===false){ # RS_LOG("false");
		$dir=rs_local_recursivemkdir($ziel);
	
	$fh=@copy($quelle,$ziel);
	if($fh===false){
		do_action ( "rs-error", "missing right folder create", $dir,$ziel );
		echo "Have not enoth rigths to create Folders. tryed ($dir): ".$ziel;
		exit;
	}}
	return $ziel;

}
function rs_local_deletefile($ziel){
	if(!rs_hostlinux())$ziel=str_replace("/","\\\\",$ziel);
	unlink(get_option ( "rs_lokalerspeicherpfad" ).$ziel);

}
function rs_local_writecontent($ziel, $content) {
  if(!rs_hostlinux()){$ziel=str_replace(array("\\\\","/"),array("\\","\\"),$ziel);}

	$ziel = get_option ( "rs_lokalerspeicherpfad" ) . $ziel;
	$fh = @fopen ( $ziel, 'w+' );
	if ($fh === false) {
		$dir = rs_local_recursivemkdir ( $ziel );
		if($dir===false)return false;
		$fh = @fopen ( $ziel, 'w+' );
		if ($fh === false) {
			do_action ( "rs-error", "missing right folder create", $dir,$ziel );
			
			#RS_LOG ( "Have not enoth rigths to create Folders. tryed ($dir): " . $ziel );
			exit ();
		}
	}
	fwrite ( $fh, $content );
	fclose ( $fh );
	return $ziel;
}
function rs_local_recursivemkdir($ziel){

	$dir=split(rs_de(), $ziel);

	##
	unset($dir[count($dir)-1]);
	$dir=implode(rs_de(2),$dir);
	$ddir=$dir;
	
 
	
	do{
		do{
			#RS_LOG( "$dir<hr>");
		 
			$fh =@mkdir($dir);
			$okdir=$dir;
			$dir=split(rs_de(),$dir);
			unset($dir[count($dir)-1]);
			$dir=implode(rs_de(2),$dir);

	 }while($dir!="" and $fh===false);
	 if($fh===false){
	 	do_action ( "rs-error", "missing right write file", $ziel,"" );
		return false;
	 	#RS_LOG(reallystatic_configerror(3,$ziel));
	  
	 }
	 $dir=$ddir;
	}while($okdir!=$dir);
	##
	return $dir;

}
/**
 * text= errortex
 * type 1=just debug 2=error-> halt
 */
function rs_local_error($text,$type){


}




//////////

function rs_local_remoteurl(){
	if(get_option("rs_differentpaths")==true){
		return loaddaten ( "rs_local_remoteurl");
	}else return loaddaten ( "rs_remoteurl" );

}

function rs_local_remotetemplateurl(){
	if(get_option("rs_differentpaths")==true){
		return loaddaten ( "rs_local_remotetemplateurl");
	}else return loaddaten ( "rs_designremote");

}
function rs_local_settingstoarray(){
	return array(
	"rs_localurl"=>get_option('rs_localurl'),
	"rs_local_remoteurl"=>get_option('rs_local_remoteurl'),
	"rs_local_remotetemplateurl"=>get_option('rs_local_remotetemplateurl'),
	
	);

}
function rs_local_arraytosettings($array){
		update_option('rs_localurl',$array["rs_localurl"]);
		update_option('rs_local_remoteurl',$array["rs_local_remoteurl"]);
		update_option('rs_local_remotetemplateurl',$array["rs_local_remotetemplateurl"]);
}
function rs_local_install(){
	add_option('rs_localurl', get_option('home')."/", '', 'yes' );
	add_option('rs_local_remoteurl',"", '', 'yes' );
	add_option('rs_local_remotetemplateurl',"", '', 'yes' );
}
function rs_local_uninstall(){
	delete_option('rs_localurl');
	delete_option('rs_local_remoteurl');
	delete_option('rs_local_remotetemplateurl');
}
function rs_local_upgrade($aktuelleversion){
	$testversion=0.520130215;
	if($aktuelleversion < $testversion){
	   require_once(ABSPATH . 'wp-admin/includes/upgrade.php');
		add_option('rs_local_remoteurl',"", '', 'yes' );
		add_option('rs_local_remotetemplateurl',"", '', 'yes' );
	   RS_LOG("local Really-static updatet to ".$testversion);
	}

}
		 



?>