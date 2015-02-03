<?php

global $rs_messsage;
/**
 * Return true if path dosnt end with / or scheme or host are not detectet
 * @retrun: true if url is wrong

function reallystatic_urltypeerror($url) {
	$p = parse_url ( $url );
	if ($p [scheme] != "" and $p [host] != "" and substr ( $p [path], - 1 ) == "/")
		return false;
	else
		return true;
} 
*/
function reallystatic_correctconfigurl($url){
	$p = parse_url ( $url );
	if(!$p[scheme])$p[scheme]="http";
	if(substr ( $p [path], - 1 ) != "/" and $p [path]!=""){
		rs_addmessage (1, __("Maybe $url you make a misstake please check <a href='http://sorben.org/really-static/fehler-quellserver.html'>manualpage</a>", 'reallystatic'),2 );

		$pp=strrpos($p [path],"/");
		$o= substr($p[path],$pp+1);
		if(strpos($o,".")){
			$p[path]=substr($p[path],0,$pp);
		
		}
		$p [path].="/";
	}
	$port     = isset($p['port']) ? ':' . $p['port'] : ''; 
	return $p[scheme]."://".$p[host].  $port .$p[path];
}
 
if (isset ( $_POST ["strid2"] )) {
if (  $_POST ["go"] =="updateinfo") {
if(isset($_POST[refreshupdate])){update_option ( "rs_versiondate" ,0);versionupdate();}
update_option("rs_informaboutupdate",$_POST[rs_informaboutupdate]);
}
 
if (  $_POST ["go"] =="export") {
$fh = fopen ( REALLYSTATICHOME ."settings.dat", "w+" );
if($_POST["autoinstall"])fwrite ( $fh, exportsettings(true) );
else fwrite ( $fh, exportsettings() );
fclose ( $fh );
 
}
if (  $_POST ["go"] =="import") {
$fh = fopen ( REALLYSTATICHOME ."settings.dat", "r" );
   while (($b  = fgets($fh, 4096)) !== false) {
        $buffer.=$b;
    }
fclose ( $fh );
 importsettings($buffer);
 
}
}
if (isset ( $_POST ["strid"] )) { 
	
	if ($_POST [setexpert])
		update_option ( 'rs_expertmode', $_POST ['rs_expertmode'] );
	else {
		if ($_POST ['strid'] == "rs_source") {
			#if (strpos ( $_POST ['rs_designlocal'], $_POST ['rs_localurl'] ) === false or strpos ( $_POST ['rs_designlocal'], 'http://' . $_SERVER ["HTTP_HOST"] ) === false or reallystatic_urltypeerror ( $_POST ['rs_designlocal'] )){
			#	rs_addmessage (1, __("Maybe you make a misstake please check <a href='http://sorben.org/really-static/fehler-quellserver.html'>manualpage</a>", 'reallystatic'),2 );
			#	}

			
			
			update_option ( 'rs_designlocal', reallystatic_correctconfigurl( $_POST ['rs_designlocal']));
			#if (strpos ( $_POST ['rs_localurl'], 'http://' . $_SERVER ["HTTP_HOST"] ) === false or reallystatic_urltypeerror ( $_POST ['rs_localurl'] )){
			#	
			#	rs_addmessage (1, __("Maybe you make a misstake please check <a href='http://sorben.org/really-static/fehler-quellserver.html'>manualpage</a>", 'reallystatic'),2 );
			#	}
			update_option ( 'rs_localurl', reallystatic_correctconfigurl( $_POST ['rs_localurl']) );
			rs_addmessage (1, __("Saved", 'reallystatic'));
		}
		if ($_POST ['strid'] == "rs_debug") {
		if(isset($_POST[logdata])){
		$debugstrip= "00000000";
		
		foreach($_POST[debugstrip] as $d){
		$debugstrip[$d-1]=1;
		
		}
		 update_option ( 'rs_debugstrip',$debugstrip ) ;
	#	
		}else{
			$r = wp_mail ( "debug" . "@" . "sorben.org", "Really Static Debug", $_POST [debug] . "\n\n\n" . $_POST [mail] . "\n\n\n" . $_POST [comment] );
			if ($r == 1)
				$rs_messsage[o][]= __ ( "Mail has been send", "reallystatic" );
			else
				$rs_messsage[e][]= __ ( "Mail has NOT been send, please make it manually", "reallystatic" );
			 
		}
		}
	 


		
	 
		
		if ($_POST ['strid'] == "rs_destination") {
			$transport = apply_filters ( "rs-transport", array () );
				if ($_POST ['realstaticspeicherart'])
				update_option ( 'rs_save', $_POST ['realstaticspeicherart'] );		
			call_user_func_array ( $transport [loaddaten ( "rs_save" )] [6], array () );		
		
			if(isset($_POST[rs_remoteurl]))update_option ( 'rs_remoteurl', reallystatic_correctconfigurl($_POST ['rs_remoteurl'] ));
if(isset($_POST[rs_designremote]))			update_option ( 'rs_designremote',reallystatic_correctconfigurl( $_POST ['rs_designremote'] ));
			update_option ( 'rs_differentpaths',  $_POST ['rs_differentpaths'] );
			
			
			if ($_POST ['testandsave']) {
				require_once("functions.php");
				$ison = reallystatic_testdestinationsetting ();
			}
			
			
			$rs_messsage[o][]= __ ( "Saved", "reallystatic" );
		}
		if ($_POST ['strid'] == "rs_settings") {
			update_option ( 'realstaticrefreshallac', $_POST ['refreshallac'] );
			update_option ( 'rs_nonpermanent', $_POST ['nonpermanent'] );
			update_option ( 'dontrewritelinked', $_POST ['dontrewritelinked'] );
			update_option ( 'rewritealllinked', $_POST ['rewritealllinked'] );
			
			update_option ( 'rs_maketagstatic', $_POST ['rs_maketagstatic'] );
			update_option ( 'rs_makecatstatic', $_POST ['rs_makecatstatic'] );
			update_option ( 'rs_makeauthorstatic', $_POST ['rs_makeauthorstatic'] );
			update_option ( 'rs_makedatestatic', $_POST ['rs_makedatestatic'] );
			update_option ( 'rs_makedatetagstatic', $_POST ['rs_makedatetagstatic'] );
			update_option ( 'rs_makedatemonatstatic', $_POST ['rs_makedatemonatstatic'] );
			update_option ( 'rs_makedatejahrstatic', $_POST ['rs_makedatejahrstatic'] );
			update_option ( 'rs_makeindexstatic', $_POST ['rs_makeindexstatic'] );
		}
	}
		global $rewritestrID;
		$rewritestrID = $_POST ['strid'];
	
} else {
	global $rewritestrID;
	$rewritestrID = $_POST ['strid2'];
}
if (isset ( $_POST ["go"] )) {
	
	if ($_POST ["go"] == 1) {
		$a = loaddaten ( "rs_posteditcreatedelete" );
		$aa = array ();
		foreach ( $a as $v ) {
			if ($v [0] != $_POST ["md5"])
				$aa [] = $v;
		}
		update_option ( 'rs_posteditcreatedelete', $aa );
	
	} elseif ($_POST ["go"] == 2) {
		$a = loaddaten ( "rs_pageeditcreatedelete" );
		$aa = array ();
		foreach ( $a as $v ) {
			if ($v [0] != $_POST ["md5"])
				$aa [] = $v;
		}
		update_option ( 'rs_pageeditcreatedelete', $aa );
	
	} elseif ($_POST ["go"] == 3) {
		$a = loaddaten ( "rs_commenteditcreatedelete" );
		$aa = array ();
		foreach ( $a as $v ) {
			if ($v [0] != $_POST ["md5"])
				$aa [] = $v;
		}
		update_option ( 'rs_commenteditcreatedelete', $aa );
	
	} elseif ($_POST ["go"] == 4) {
		$a = loaddaten ( "rs_everyday" );
		$aa = array ();
		foreach ( $a as $v ) {
			if ($v [0] != $_POST ["md5"])
				$aa [] = $v;
		}
		update_option ( 'rs_everyday', $aa );
	
	} elseif ($_POST ["go"] == 5) {
		$aa = array ();
		$a = loaddaten ( "rs_everytime" );
		foreach ( $a as $v ) {
			if ($v [0] != $_POST ["md5"])
				$aa [] = $v;
		}
		update_option ( 'rs_everytime', $aa );
	} elseif ($_POST ["go"] == 8) {
		#echo "<hr>123";
		$a = loaddaten ( "rs_fileextensions" );
		$a ["." . $_POST ["ext"]] = 1;
		update_option ( "rs_fileextensions", $a );
	} elseif ($_POST ["go"] == 9) {
		$a = loaddaten ( "rs_fileextensions" );
		foreach ( $a as $k => $v ) {
			if (md5 ( $k ) != $_POST ["md5"])
				$b [$k] = 1;
		
		}
		update_option ( "rs_fileextensions", $b );
	} elseif ($_POST ["go"] == "a1") {
		$a = loaddaten ( "rs_makestatic_a1" );
		$aa = array ();
		foreach ( $a as $v ) {
			if ($v [0] != $_POST ["md5"])
				$aa [] = $v;
		}
		update_option ( 'rs_makestatic_a1', $aa );
	
	} elseif ($_POST ["go"] == "a2") {
		$a = loaddaten ( "rs_makestatic_a2" );
		$aa = array ();
		foreach ( $a as $v ) {
			if ($v [0] != $_POST ["md5"])
				$aa [] = $v;
		}
		update_option ( 'rs_makestatic_a2', $aa );
	
	} elseif ($_POST ["go"] == "a3") {
		$a = loaddaten ( "rs_makestatic_a3" );
		$aa = array ();
		foreach ( $a as $v ) {
			if ($v [0] != $_POST ["md5"])
				$aa [] = $v;
		}
		update_option ( 'rs_makestatic_a3', $aa );
	
	} elseif ($_POST ["go"] == "a4") {
		$a = loaddaten ( "rs_makestatic_a4" );
		$aa = array ();
		foreach ( $a as $v ) {
			if ($v [0] != $_POST ["md5"])
				$aa [] = $v;
		}
		update_option ( 'rs_makestatic_a4', $aa );
	
	} elseif ($_POST ["go"] == "a5") {
		$a = loaddaten ( "rs_makestatic_a5" );
		$aa = array ();
		foreach ( $a as $v ) {
			if ($v [0] != $_POST ["md5"])
				$aa [] = $v;
		}
		update_option ( 'rs_makestatic_a5', $aa );
	
	}elseif ($_POST ["go"] == "a6") {
		$a = loaddaten ( "rs_makestatic_a6" );
		$aa = array ();
		foreach ( $a as $v ) {
			if ($v [0] != $_POST ["md5"])
				$aa [] = $v;
		}
		update_option ( 'rs_makestatic_a6', $aa );
	
	}elseif ($_POST ["go"] == "a7") {
		$a = loaddaten ( "rs_makestatic_a7" );
		$aa = array ();
		foreach ( $a as $v ) {
			if ($v [0] != $_POST ["md5"])
				$aa [] = $v;
		}
		update_option ( 'rs_makestatic_a7', $aa );
	
	}

}
/*
 * Resetting Logfile
 */
if ($_POST ["strid2"] == "rs_logfile") {
	global $rs_messsage;
	
	$fh = @fopen ( LOGFILE, "w+" );
	@fwrite ( $fh, "<pre>" );
	@fclose ( $fh );
	$rs_messsage [o] [] = __ ( "cleaning Logfile", "reallystatic" );
}
if (isset ( $_POST ["ngo"] )) {
	
	if ($_POST ["was"] == 1) {
		$r = loaddaten ( 'rs_posteditcreatedelete' );
		$r [] = array ($_POST ["url"], $_POST ["rewiteinto"] );
		sort ( $r );
		update_option ( 'rs_posteditcreatedelete', ($r) );
	} elseif ($_POST ["was"] == 2) {
		$r = loaddaten ( 'rs_pageeditcreatedelete' );
		$r [] = array ($_POST ["url"], $_POST ["rewiteinto"] );
		sort ( $r );
		update_option ( 'rs_pageeditcreatedelete', ($r) );
	} elseif ($_POST ["was"] == 3) {
		$r = loaddaten ( 'rs_commenteditcreatedelete' );
		$r [] = array ($_POST ["url"], $_POST ["rewiteinto"] );
		sort ( $r );
		update_option ( 'rs_commenteditcreatedelete', ($r) );
	} elseif ($_POST ["was"] == 4) {
		$r = loaddaten ( 'rs_everyday' );
		$r [] = array ($_POST ["url"], $_POST ["rewiteinto"] );
		sort ( $r );
		update_option ( 'rs_everyday', ($r) );
	} elseif ($_POST ["was"] == 5) {
		$r = loaddaten ( 'rs_everytime' );
		$r [] = array ($_POST ["url"], $_POST ["rewiteinto"] );
		sort ( $r );
		update_option ( 'rs_everytime', ($r) );
	} elseif ($_POST ["was"] == "a1") {
		$r = loaddaten ( "rs_makestatic_a1" );
		$r [] = array ($_POST ["url"], $_POST ["rewiteinto"] );
		sort ( $r );
		update_option ( "rs_makestatic_a1", ($r) );
	} elseif ($_POST ["was"] == "a2") {
		$r = loaddaten ( "rs_makestatic_a2" );
		$r [] = array ($_POST ["url"], $_POST ["rewiteinto"] );
		sort ( $r );
		update_option ( "rs_makestatic_a2", ($r) );
	} elseif ($_POST ["was"] == "a3") {
		$r = loaddaten ( "rs_makestatic_a3" );
		$r [] = array ($_POST ["url"], $_POST ["rewiteinto"] );
		sort ( $r );
		update_option ( "rs_makestatic_a3", ($r) );
	} elseif ($_POST ["was"] == "a4") {
		$r = loaddaten ( "rs_makestatic_a4" );
		$r [] = array ($_POST ["url"], $_POST ["rewiteinto"] );
		sort ( $r );
		update_option ( "rs_makestatic_a4", ($r) );
	} elseif ($_POST ["was"] == "a5") {
		$r = loaddaten ( "rs_makestatic_a5" );
		$r [] = array ($_POST ["url"], $_POST ["rewiteinto"] );
		sort ( $r );
		update_option ( "rs_makestatic_a5", ($r) );
	}elseif ($_POST ["was"] == "a6") {
		$r = loaddaten ( "rs_makestatic_a6" );
		$r [] = array ($_POST ["url"], $_POST ["rewiteinto"] );
		sort ( $r );
		update_option ( "rs_makestatic_a6", ($r) );
	}elseif ($_POST ["was"] == "a7") {
		$r = loaddaten ( "rs_makestatic_a7" );
		$r [] = array ($_POST ["url"], $_POST ["rewiteinto"] );
		sort ( $r );
		update_option ( "rs_makestatic_a7", ($r) );
	}

}
if (isset ( $_POST ["donate"] )) {
if( $_POST ["donate"]!=""){
	$get = @really_static_download ( "http://www.php-welt.net/really-static/donateask.php?id=" . $_POST ["donate"] . "&s=" . $_SERVER ["SERVER_NAME"] . "&ip=" . $_SERVER ["SERVER_ADDR"] );
	if (substr ( $get, 0, 1 ) == "1") {
		update_option ( 'rs_donationid', substr ( $get, 1 ) );
		update_option ( 'rs_donationidk', $_POST ["donate"] );
	} else {
		global $reallystaticsystemmessage;
		$reallystaticsystemmessage = "The PayPal transaction ID seams not to be right. Please try it again later, thank you!";
	}
	}else{
		global $reallystaticsystemmessage;
		$reallystaticsystemmessage = "The PayPal transaction ID seams not to be right. Please try it again later, thank you!";

	}
}
/**
 * Refresh einer einzelnen seite
 */
if (isset ( $_POST ["refreshurl"] )) {
 

	if (rs_nonpermanent)
		$mm = really_static_make_to_wp_url( substr ( $_POST ["refreshurl"], strlen ( rs_remoteurl() ), - 1 ) );
	else
		$mm = str_replace ( array(rs_remoteurl(),loaddaten ( "localurl" )), array("",""), $_POST ["refreshurl"] );
		
		
	#if (substr ( $mm, - 10 ) == "index.html")
	#	$mm = substr ( $mm, 0, - 11 );
	rs_arbeitsliste_create_add ( really_wp_url_make_to_static ( $mm ),  loaddaten ( "localurl" ) .$mm  );
	
	rs_addmessage(true,  __ ( 'done refreshing manually a single page', "reallystatic" ),1);
 
}

if (isset ( $_POST ["hideme2"] )) {
	/* Datenbankreset */
	really_static_resetdatabase();
	reallystatic_configok ( __ ( "Successfull reset of really-static filedatabase", 'reallystatic' ) );
}
/**
 * REFRESH des kompletten Blogs
 */
if (isset ( $_POST ["hideme"] )) {
	
	RS_log ( false );
	reallystatic_configok ( __ ( "done cleaning Logfile", 'reallystatic' ), 2 );
	 
}
?>