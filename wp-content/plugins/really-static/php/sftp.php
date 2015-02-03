<?php
 
add_filter("rs-aboutyourplugin",create_function('$b,$a','$n="SFTP-Snapin (v0.9)"; if($a==1)echo "<b>$n:</b> programmed by Jim Wigginton and Erik Sefkow<br>";else return  $b.$n.", ";'),10,2);




add_filter("rs-transport","sftp");
function sftp($transportfuntions){
	$transportfuntions[sftp]=array(
	"rs_sftp_connect",
	"rs_sftp_disconnect",
	"rs_sftp_writefile",
	"rs_sftp_deletefile",
	"rs_sftp_writecontent",
	"rs_sftp_isconnected",
	"rs_sftp_saveoptions",
	
	
	"rs_sftp_remoteurl",
	"rs_sftp_remotetemplateurl",
	"rs_sftp_settingstoarray",
	"rs_sftp_arraytosettings",

	"rs_sftp_install",
	"rs_sftp_uninstall",
	"rs_sftp_upgrade"

	
	
	
	
	
	
	
	);
	return $transportfuntions;
} 
add_filter("rs-adminmenu-savealltransportsettings","rs_sftp_saveoptions");
function rs_sftp_saveoptions() {
	global $rs_messsage;
		if(isset($_POST["rs_sftp_remoteurl"]))update_option ( 'rs_sftp_remoteurl',$_POST["rs_sftp_remoteurl"]);
	if(isset($_POST["rs_sftp_remotetemplateurl"]))update_option ( 'rs_sftp_remotetemplateurl',$_POST["rs_sftp_remotetemplateurl"]);
 
	update_option ( 'rs_sftpserver', $_POST ['rs_sftpserver'] );
	update_option ( 'rs_sftpuser', $_POST ['rs_sftpuser'] );
	update_option ( 'rs_sftppasswort', $_POST ['rs_sftppasswort'] );
	update_option ( 'rs_sftpport', $_POST ['rs_sftpport'] );
	update_option ( 'rs_remotepathsftp', $_POST ['rs_remotepathsftp'] );
	if (substr ( $_POST ['rs_remotepathsftp'], - 1 ) != "/")
		$rs_messsage [e] [] = __ ( "You may forgot a / at the end of the path!", "reallystatic" );
	
}
add_filter("rs-adminmenu-transport","sftpadmin");
function sftpadmin($array){
if(get_option("rs_differentpaths")==true)$sub='<tr><td valign="top" width="400px">
	'.__('Domainprefix for your cached files', 'reallystatic').'</td><td>:<input name="rs_sftp_remoteurl" size="50" type="text" value="' . 	get_option('rs_sftp_remoteurl') . '"	/> <a style="cursor:pointer;"  onclick="toggleVisibility(\'remoteurl\');" >[?]</a>  <div style="max-width:500px; text-align:left; display:none" id="remoteurl">( where your visitors find your blog )</div></td></tr>'
. '<tr><td valign="top" >'.__('Url to the templatefolder', 'reallystatic').'</td><td>:<input name="rs_sftp_remotetemplateurl" size="50" type="text" value="' . get_option ( "rs_sftp_remotetemplateurl" ) . '"	/> <a style="cursor:pointer;"  onclick="toggleVisibility(\'designurl\');" >[?]</a>  <div style="max-width:500px; text-align:left; display:none" id="designurl">( for example: '. loaddaten ( "rs_designsftp", 'reallystatic' ).' )</div></td></tr>'
;




	$array[]=array(name=>"sftp",title=>__('work with sftp', 'reallystatic'),main=>'<div style="margin-left:50px;"><table border="0" width="100%"><tr><td width="350px">'.__('SFTP-Server IP', 'reallystatic').':'.__('Port', 'reallystatic').'</td><td>:<input name="rs_sftpserver" size="50" type="text" value="' . loaddaten ( "rs_sftpserver" , 'reallystatic') . '"	/>:<input name="rs_sftpport" size="5" type="text" value="' . loaddaten ( "rs_sftpport" , 'reallystatic') . '"	/></td></tr>
<tr><td>'.__('SFTP-login User', 'reallystatic').'</td><td>:<input name="rs_sftpuser" size="50" type="text" value="' . loaddaten ( "rs_sftpuser", 'reallystatic' ) . '"	/></td></tr>
<tr><td>'.__('SFTP-login Password', 'reallystatic').'</td><td>:<input name="rs_sftppasswort" size="50" type="password" value="' . loaddaten ( "rs_sftppasswort" , 'reallystatic') . '"	/></td></tr><tr><td valign="top">'.__('path from SFTP-Root to cachedfiles', 'reallystatic').'</td><td>:<input name="rs_remotepathsftp" size="50" type="text" value="' . loaddaten ( "rs_remotepathsftp" , 'reallystatic') . '"	/> <a style="cursor:pointer;"  onclick="toggleVisibility(\'internalftppfad2\');" >[?]</a>
	<div style="max-width:500px; text-align:left; display:none" id="internalftppfad2">('.__('the path inside your FTP account e.g. "/path/".If it should saved to maindirectory write "/" ', 'reallystatic').')</div></td></tr>'.$sub.'</table></div><br>');
	return $array;
	
}
 
 
 
/*
* 
*/
function rs_sftp_isconnected(){
	global $rs_sftp_isconnectet;
	if($rs_sftp_isconnectet===true)return true;
	else return false;
}
function rs_sftp_connect(){
 
	global $rs_sftp_isconnectet,$sftp;
	if($rs_sftp_isconnectet!==true){
		include('sftp/SFTP.php');
		$sftp = new Net_SFTP(get_option ( 'rs_sftpserver'),get_option ( 'rs_sftpport'));
		 
		if (!$sftp->login(get_option ( 'rs_sftpuser'), get_option ( 'rs_sftppasswort'))) {
			do_action ( "rs-error", "login error", "SFTP" ,"");
			exit('Login Failed');
		}
		$rs_sftp_isconnectet=true;
	 }
	 return $sftp;
}
function rs_sftp_disconnect(){

}
function rs_sftp_writefile($ziel, $quelle){

	$sftp=rs_sftp_connect();
	$ziel=get_option ( 'rs_remotepathsftp').$ziel;
	$handle = fopen ($quelle, "r");
	while (!feof($handle)) {
		$content .= fgets($handle, 4096);
	}	
	fclose ($handle);
	if($sftp->put($ziel, $content)===false){
	$dir=rs_sftp_recursivemkdir($ziel);
	if($sftp->put($ziel, $content)===false){
		do_action ( "rs-error", "missing right folder create", $dir,$ziel );
		echo "Have not enoth rigths to create Folders. tryed ($dir): ".$ziel;
		exit;
	}
	}
	 
}

function rs_sftp_readfile($datei){
$sftp=rs_sftp_connect();
$tmp="temp".time()."asde.tmp";
return $sftp->get($datei);
 
}

function rs_sftp_deletefile($file){
	$sftp=rs_sftp_connect();
	$sftp->delete($file);
}
function rs_sftp_writecontent($ziel,$content){
	$sftp=rs_sftp_connect();
	$ziel=get_option ( 'rs_remotepathsftp').$ziel;
	 $ziel=str_replace("//","/",$ziel);
	 
		if($sftp->put($ziel, $content)===false){
	 
	$dir=rs_sftp_recursivemkdir($ziel);
	 
	if($sftp->put($ziel, $content)===false){
			 	do_action ( "rs-error", "missing right folder create", $ziel ,"" );
		echo "Have not enoth rigths to create Folders. tryed ($dir): ".$ziel;
		exit;
	}
	}
 
	
	
}
 function rs_sftp_recursivemkdir($ziel){
	 global $sftp;
	$dir=split("/", $ziel);
	##
	unset($dir[count($dir)-1]);
	$dir=implode("/",$dir);
	$ddir=$dir;
	do{
		do{
		#echo "$dir<hr>";
			$fh =@$sftp->mkdir($dir);
			$okdir=$dir;
			$dir=split("/",$dir);
			unset($dir[count($dir)-1]);
			$dir=implode("/",$dir);

	 }while($dir!="" and $fh===false);
	 if($fh===false){

	 	do_action ( "rs-error", "missing right write file", $ziel,"" );
	 	die(str_replace("%folder%","$ziel",__("Im no able to create the directory %folder%! Please check writings rights!", 'reallystatic')));
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
function rs_sftp_error($text,$type){


}



//////////

function rs_sftp_remoteurl(){
	if(get_option("rs_differentpaths")==true){
		return loaddaten ( "rs_sftp_remoteurl");
	}else return loaddaten ( "rs_remoteurl" );

}

function rs_sftp_remotetemplateurl(){
	if(get_option("rs_differentpaths")==true){
		return loaddaten ( "rs_sftp_remotetemplateurl");
	}else return loaddaten ( "rs_designremote");

}
 



function rs_sftp_settingstoarray(){
	return array(
	"rs_remotepathsftp"=>get_option('rs_remotepathsftp'),
	"rs_sftpserver"=>get_option('rs_sftpserver'),
	"rs_sftpuser"=>get_option('rs_sftpuser'),
	"rs_sftppasswort"=>get_option('rs_sftppasswort'),
	"rs_sftpport"=>get_option('rs_sftpport'),
	"rs_sftp_remoteurl"=>get_option('rs_sftp_remoteurl'),
	"rs_sftp_remotetemplateurl"=>get_option('rs_sftp_remotetemplateurl'),
	
	);

}
function rs_sftp_arraytosettings($array){
	update_option('rs_remotepathsftp',$array["rs_remotepathsftp"]);
	update_option('rs_sftpserver',$array["rs_sftpserver"]);
	update_option('rs_sftpuser',$array["rs_sftpuser"]);
	update_option('rs_sftppasswort',$array["rs_sftppasswort"]);
	update_option('rs_sftpport',$array["rs_sftpport"]);
	update_option('rs_sftp_remoteurl',$array["rs_sftp_remoteurl"]);
	update_option('rs_sftp_remotetemplateurl',$array["rs_sftp_remotetemplateurl"]);

}






function rs_sftp_install(){
	add_option('rs_remotepathsftp', "/", '', 'yes' );
	add_option('rs_sftpserver', "", '', 'yes' );
	add_option('rs_sftpuser', "", '', 'yes' );
	add_option('rs_sftppasswort', "", '', 'yes' );
	add_option('rs_sftpport', "22", '', 'yes' );
	add_option('rs_sftp_remoteurl',"", '', 'yes' );
	add_option('rs_sftp_remotetemplateurl',"", '', 'yes' );
}
function rs_sftp_uninstall(){
	delete_option('rs_remotepathsftp');
	delete_option('rs_sftpserver');
	delete_option('rs_sftpuser');
	delete_option('rs_sftppasswort');
	delete_option('rs_sftpport');
	delete_option('rs_sftp_remoteurl');
	delete_option('rs_sftp_remotetemplateurl');
}
function rs_sftp_upgrade($aktuelleversion){
	$testversion=0.520130216;
	if($aktuelleversion < $testversion){
	   require_once(ABSPATH . 'wp-admin/includes/upgrade.php');
		add_option('rs_sftp_remoteurl',"", '', 'yes' );
		add_option('rs_sftp_remotetemplateurl',"", '', 'yes' );
	   RS_LOG("sftp Really-static updatet to ".$testversion);
	}

}
		 

?>