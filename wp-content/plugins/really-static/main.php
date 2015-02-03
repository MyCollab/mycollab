<?php
/*
Plugin Name: Really Static
Plugin URI: http://www.php-welt.net/really-static/index.html
Description:  Make your Blog really static! Please study the <a href="http://www.php-welt.net/really-static/">quick start instuctions</a>! 
Author: Erik Sefkow
Version: 0.31
Author URI: http://erik.sefkow.net/
*/

/**
 * Make your Blog really static!
 *
 * Copyright 2008-2012 Erik Sefkow
 *
 * Really static is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * Really static is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * @author Erik Sefkow 
 * @version 0.5
 */
 
if (! defined ( "WP_CONTENT_URL" ))
	define ( "WP_CONTENT_URL", get_option ( "siteurl" ) . "/wp-content" );
if (! defined ( "WP_CONTENT_DIR" ))
	define ( "WP_CONTENT_DIR", ABSPATH . "wp-content" );
if (! defined ( 'REALLYSTATICHOME' ))
	define ( 'REALLYSTATICHOME', dirname ( __FILE__ ) . '/' );
if (! defined ( 'REALLYSTATICURLHOME' ))
	define ( 'REALLYSTATICURLHOME', plugins_url( '' , __FILE__ ) . '/' );
if (! defined ( 'REALLYSTATICBASE' ))
	define ( 'REALLYSTATICBASE', plugin_basename ( __FILE__ ) );
if (! defined ( 'REALLYSTATICMAINFILE' ))
	define ( 'REALLYSTATICMAINFILE', __FILE__);



global $wpdb;
if (! defined ( 'REALLYSTATICDATABASE' ))
	define ( 'REALLYSTATICDATABASE',$wpdb->prefix . "realstatic" );
if (! defined ( 'REALLYSTATICUPDATEAPIURL' ))
	define ( 'REALLYSTATICUPDATEAPIURL', 'http://www.php-welt.net/really-static/updateserver/index.php');

@set_time_limit ( 0);



error_reporting(E_ERROR | E_WARNING | E_PARSE);
#### PLEASE Do not change anything here!
global $rs_version, $rs_rlc;
 
$rs_version = "0.5";
$rs_rlc = 20130621;

define ( 'RSVERSION', $rs_version );
define ( 'RSSUBVERSION', $rs_rlc );
define ( 'RSVERSIONCHECK', 1 );
if (preg_match ( '#' . basename ( __FILE__ ) . '#', $_SERVER ['PHP_SELF'] )) {
	die ( 'Get it here: <a title="really static wordpress plugin" href="http://really-static-support.php-welt.net/current-development-snapshot-t5.html">really static wordpress plugin</a>' );
}
$currentLocale = get_locale ();
if (! empty ( $currentLocale )) {
	$moFile = dirname ( __FILE__ ) . "/languages/reallystatic-" . $currentLocale . ".mo";
	if (@file_exists ( $moFile ) && is_readable ( $moFile ))
		load_textdomain ( 'reallystatic', $moFile );
}
require_once ("php/ftp.php");
require_once ("php/sftp.php");
require_once ("php/local.php");

if(is_multisite()) define ( 'LOGFILE', REALLYSTATICHOME . $wpdb->blogid.'-log.html' );
else define ( 'LOGFILE', REALLYSTATICHOME . 'log.html' );
if (get_option ( 'permalink_structure' ) == "")
	define ( 'rs_nonpermanent', true );
else
	define ( 'rs_nonpermanent', false );
require ("rewrite.php");
 
/**
 * @return void
 * @param string logfiletext
 * @param int LOGTYP 1=error;2=erweiterter hinweis(lese+cachehit);3=schreiboperationen
 * @desc Write a text into Logfile
 */
function RS_LOG($line, $typ = 0,$file=LOGFILE) {
	if(get_option( 'rs_logfile')===false)return;
	#if($typ==2)return;
	if ($line === false or @filesize(LOGFILE)>(1024*1024)) {
		$fh = @fopen ( $file, "w+" );
		@fwrite ( $fh, "<pre>" );
		@fclose ( $fh );
		return;
	}
	$fh = @fopen ( $file, "a+" );

	@fwrite ( $fh, date_i18n ( "d M Y H:i:s" ) . ": " . $line . "\r\n" );
	@fclose ( $fh );
}

 
/**
 * @return void
 * @param String $d
 * @desc Just for devoloping! Zählt hoch und gibt werte aus
 */
function RS_LOGD($d="") {
	global $debugcount;
	$debugcount ++;
	RS_LOG ( "DEBUG$debugcount: " . $d );
}
function RS_LOGF($d="",$fp=""){
	global $debugcount;
	$debugcount ++;
	$myFile= REALLYSTATICHOME.$fp.time().$debugcount.".txt";
	RS_LOG("writing debuglogfile: ".$myFile);
	
	$fh = fopen($myFile, 'w+') or die("can't open file");
	fwrite($fh, $d);
	fclose($fh);
}
/**
 * @return void
 * @param Array $array
 * @desc Write a Array into Logfile
 */
function RS_LOGA($array) {
	ob_start ();
	print_R ( $array );
	$out1 = ob_get_contents ();
	ob_end_clean ();
	RS_LOG ( $out1 );
}
function multiloaddaten($name) {
	#echo get_site_option( $name )." $name<hr>";
	if (get_site_option ( $name ) !== false)
		return get_site_option ( $name );
	if ($name == "rs_donationid")
		add_site_option ( $name, "" );
	return get_site_option ( $name );
}

/**
 * @desc settingsdaten laden
 * @param string variablenname
 * @return string gespeicherter inhalt der Variable 
 */
function loaddaten($name) {
	if ($name == "localpath"){
		//RS_LOG("ABSPATH ".ABSPATH);
		return ABSPATH;
	}
	if ($name == "subpfad")
		$name = "rs_subpfad";
	if ($name == "localurl")
		$name = "rs_localurl";
	if ($name == "remotepath")
		$name = "rs_remotepath";
	if ($name == "remoteurl")
		$name = "rs_remoteurl";
	

	if ($name == "rs_posteditcreatedelete") {
		$r = get_option ( $name );
		if (count ( $r ) > 0) {
			if (! is_array ( $r [0] )) {
				foreach ( $r as $k )
					$rr [] = array ($k, "" );
				
				update_option ( $name, $rr );
				unset ( $rr );
				unset ( $r );
			}
		}
	}
	
	if ($name == "rs_pageeditcreatedelete") {
		$r = get_option ( $name );
		if (count ( $r ) > 0) {
			if (! is_array ( $r [0] )) {
				foreach ( $r as $k )
					$rr [] = array ($k, "" );
				update_option ( $name, $rr );
				unset ( $rr );
				unset ( $r );
			}
		}
	}
	if ($name == "rs_commenteditcreatedelete") {
		$r = get_option ( $name );
		if (count ( $r ) > 0) {
			if (! is_array ( $r [0] )) {
				foreach ( $r as $k )
					$rr [] = array ($k, "" );
				update_option ( $name, $rr );
				unset ( $rr );
				unset ( $r );
			}
		}
	}
	
	if ($name == "rs_everyday") {
		$r = get_option ( $name );
		if (count ( $r ) > 0) {
			if (! is_array ( $r [0] )) {
				foreach ( $r as $k )
					$rr [] = array ($k, "" );
				update_option ( $name, $rr );
				unset ( $rr );
				unset ( $r );
			}
		}
	}
	if ($name == "rs_everytime") {
		$r = get_option ( $name );
		if (count ( $r ) > 0) {
			if (! is_array ( $r [0] )) {
				foreach ( $r as $k )
					$rr [] = array ($k, "" );
				update_option ( $name, $rr );
				unset ( $rr );
				unset ( $r );
			}
		}
	}
	if($name=="rs_donationid" and multiloaddaten("rs_donationid")!="" and multiloaddaten("rs_donationid")!="-")return multiloaddaten("rs_donationid");
	return get_option ( $name );
}
 
#add_filter ( 'post_updated_messages', 'rs_pagetitlechanged' );
function rs_pagetitlechanged($messages){
		#RS_LOGA($messages);
	 
	 
	foreach($p=array("page","post") as $v)
	 $messages[$v][$_GET['message']].="<br />".__("Attention! You changed the title of this page. You may need to refresh your static files.");
 
		 
		#RS_LOGA($messages);
		return $messages;
}

add_action ( 'shutdown', 'arbeite' );
/**
 * @desc führt alle update & delete operationen durch
 * @param int doit wenn =true dann wirds sofort ausgefürt egal von wo
 * @return void
 */
function arbeite($doit=false,$silent=false) {
		global $dontwrite;
		if($dontwrite==1)return;
		
		
	global $arbeitsliste, $wpdb, $allrefresh,$eigenerools, $arbeitetrotzdem;
	if ($doit!==true &&  $arbeitetrotzdem!== true and (! is_array ( $arbeitsliste ) or substr ( $_SERVER ['PHP_SELF'], - 9 ) == "index.php"))
		return;
 
	// RS_LOG(	rs_getbo()."<=>".rs_getba());
	#RS_LOG("rs_onwork".get_option ( "rs_onwork"));
	if(really_static_demodetect() && get_option ( "rs_onwork")!=0)wp_die( __( 'Please wait! Another really-static instance is running!' ));
	update_option ( "rs_onwork", "1" );
#RS_LOG("rs_onwork".get_option ( "rs_onwork"));
			
	unset($arbeitsliste[update][""]);
	unset($arbeitsliste[delete][""]);
	RS_LOGA ( $arbeitsliste );
	#return;
	$arbeitsliste = apply_filters ( "rs-todolist", $arbeitsliste );
	
	
	RS_log ( "Verbinde", 2 );
	$pre_remoteserver = loaddaten ( "remotepath" );
	$pre_localserver = loaddaten ( "localpath" );
	$transport = apply_filters ( "rs-transport", array () );
	call_user_func_array ( $transport [loaddaten ( "rs_save" )] [0], array () );
	

	$table_name = REALLYSTATICDATABASE;
 
	//Loeschen
	if (is_array ( $arbeitsliste [delete] )) {
		foreach ( $arbeitsliste [delete] as $pusho => $get ) {
			if (! isset ( $arbeitsliste [update] [$pusho] )) {
				$push = stupidfilereplace ( $pusho, 2 );
				RS_log ( __ ( 'Deleteing File:' ) . " $push", 3 );
				call_user_func_array ( $transport [loaddaten ( "rs_save" )] [3], array ($push ) );
				rs_cachedelete($push);
			}
			unset ( $arbeitsliste[delete][$pusho] );
			if($allrefresh===true){ update_option('rs_allrefreshcache',$arbeitsliste);}			
		}
	}
 
	//UPDATEN
	if (is_array ( $arbeitsliste [update] )) {
		foreach ( $arbeitsliste [update] as $pusho => $get ) {
		
			$push=urldecode($pusho);
			 
			if(substr($push,-1)=="/")$push.="index.html";
			$get = apply_filters ( "rs-updatefile-source",$get );
			$push = apply_filters ( "rs-updatefile-destination",$push );
		 
			$push = stupidfilereplace ( $push, 2 );
		 
			$dontrefresh = false;
			RS_log ( __ ( 'get', 'reallystatic' ) . " $get", 2 );
			$content = @really_static_geturl (  ($get) );
			if ($content !== false) {
				 

				if($allrefresh=="123")$ignorecache=false;
				else $ignorecache=true;
				$dontrefresh=rs_cachetest($push,$content, $ignorecache,$get);
				if(!$dontrefresh){
			 		$geschrieben = call_user_func_array ( $transport [loaddaten ( "rs_save" )] [4], array ($push, $content ) );
					 
					RS_log ( __ ( 'writing', 'reallystatic' ) . ": " . $push . " " . strlen ( $content ) . " Byte", 3 );
					if($allrefresh===true)reallystatic_configok(__ ( "Write:", 'reallystatic' ) . " " .$push , 2);
					do_action ( "rs-write-file", "content", $push, loaddaten ( "realstaticspeicherart", 'reallystatic' ) );
					rs_cacheset($get,$content,$dontrefresh );
				}
			}
			unset ( $arbeitsliste[update][$pusho] );
			if($allrefresh===true){ update_option('rs_allrefreshcache',$arbeitsliste);}
		}
	}
				 
	#echo "###UNSET###";
	
	#print_R($arbeitsliste);
	#RS_LOG("rs_onwork".get_option ( "rs_onwork"));
		update_option ( "rs_onwork", "0" );
	#	RS_LOG("rs_onwork".get_option ( "rs_onwork"));
 
	if($allrefresh===true){
	 
	reallystatic_configok ( __ ( "Finish", 'reallystatic' ), 3 );
	 update_option('rs_allrefreshcache',array());
	}
	
	
	
	return;
}

function rs_cachedelete($ziel){
	global $wpdb;
	$table_name = REALLYSTATICDATABASE;
	$wpdb->query ( "Delete FROM $table_name where url='" .  ( $ziel ) . "'" );
	
	
}

/**
$ziel pfad bis zur datei
$content kompletter content der heruntergelanden datei
$ignorecache true= tut so als wäre datei noch nie geladen worden
$quelle quellpfad der datei

return false wenn cachehit
*/
function rs_cachetest($ziel,$content,$ignorecache,$quelle){
	global $wpdb;
		$q=substr($quelle,strlen(loaddaten("localurl")));
	#$content ="qwewqe";
	$table_name = REALLYSTATICDATABASE;
	$contentmd52 = md5 ( $content );
	$dontrefresh = false;
 
	if(!$ignorecache){
		$querystr = "SELECT datum,content  FROM 	$table_name where url='" .  ( $q ) . "'";
	 
	 
		$ss = $wpdb->get_results ( $querystr, OBJECT );
		$contentmd5 = $ss [0]->content; 
		$lastmodifieddatum = $ss [0]->datum;
		if ( $contentmd5 == $contentmd52) {
			RS_log ( __ ( 'Cachehit' ) . ": " . loaddaten ( "remotepath" ) . "$ziel @ " . date_i18n( "d.m.Y H:i:s", $lastmodifieddatum ), 2 );
			if(!$silent)reallystatic_configok( __ ( 'Cachehit' ) . ": " . loaddaten ( "remotepath" ) . "$ziel @ " . date_i18n( "d.m.Y H:i:s", $lastmodifieddatum ), 2 );
			$dontrefresh = true;
		}
	} 
	
	#RS_LOG("rs_cachetest($ziel,".md5($content)."!=$contentmd5,$ignorecache,$quelle)");

	$dontrefresh = apply_filters ( "rs-cachetest-result", $dontrefresh ,$ziel,$content,$ignorecache,$quelle);
	return $dontrefresh;
}
function rs_cacheset($quelle,$content,$dontrefresh ){
	global $wpdb;
	$table_name = REALLYSTATICDATABASE;
	if(!$dontrefresh   ){
	$quelle=substr($quelle,strlen(loaddaten("localurl")));
		$wpdb->query ( "Delete FROM 	$table_name where url='" .  ( $quelle ) . "'" );
		$wpdb->query ( "INSERT INTO `$table_name` (`url` ,`content`,`datum`)VALUES ('" .  ( $quelle ) . "', '" .  md5 ( $content ) . "','" . time () . "');" );
	}

}
add_action ( 'wp_update_comment_count', 'rs_setthisactionascomment', 550 );
/**
 * stellt sicher das erstellen von kommentar nicht als seitenedit ausgelegt wird
 * @param none
 * @return none
 */
function rs_setthisactionascomment() {
	#RS_LOG("COMMENTi");
	global $iscomment;
	$iscomment = true;
}

add_action ( 'delete_post', 'delete_poster' );
add_action ( 'deleted_post', 'delete_poster' );
/**
 * wird aufgerufen wenn ein Post geloescht wird
 * @param int Postid
 * @return void
 */
function delete_poster($id) {
	global $arbeitsliste;
	$arbeitsliste [delete] [get_page_link ( $id )] = 1;
}



/**
 * laed eine datei herunter
 * @param string URL der herunterzuladenden DAtei
 * @return string Dateiinhalt
 */
function really_static_download($url) {
	update_option ( "rs_counter", loaddaten ( "rs_counter" ) + 1 );
	$file = apply_filters ( "rs-do-download", "",$url );
	if ($file != "")
		return $file;
	if ( function_exists ( 'file_get_contents' ) and ini_get ( 'allow_url_fopen' ) == 1) {
		$file = @file_get_contents ( $url );
			
	} else {
		$curl = curl_init ( $url );
		curl_setopt ( $curl, CURLOPT_HEADER, 0 );
		curl_setopt ( $curl, CURLOPT_RETURNTRANSFER, true );
		$file = curl_exec ( $curl );
		curl_close ( $curl );
	 
	}
	if ($file === false)do_action ( "rs-error", "loading url", $url,"" );
	//	RS_log ( sprintf ( __ ( "Getting a error (timeout or 404 or 500 ) while loading: %s", 'reallystatic' ), $url ), 1 );
	do_action ( "rs-downloaded-url", $file, $url );
	return $file;
}

 
add_action("rs-info","really_static_infotologfile",10,3);
add_action("rs-error","really_static_infotologfile",10,3);
/**
 * @desc fängt meldungen ab und schreibt diese in die logfile
 * @param String $was Fehlerbezeichner
 * @param Object $info
 * @param Object $info2
 */
function really_static_infotologfile($was, $info, $info2 = "") {
 
	switch ($was) {
		case ("loading url") :
			RS_log ( sprintf ( __ ( "Getting a error (timeout, 403, 404 or 500 error ) while loading: %s", 'reallystatic' ), $info ), 1 );
			break;
		
		case ("write imgfile") :
			RS_log ( __ ( 'Writing ImageFile:', 'reallystatic' ) . " $info", 3 );
			break;
		
		case ("cachehit uploadfile") :
			RS_LOG ( "Cachehit: " . $info, 2 );
			break;
		case ("write file") :
			RS_log ( __ ( 'Writing File:', 'reallystatic' ) . " $info => $info2 " , 3 );
			break;
		case ("missing right folder create") :
		update_option ( "rs_onwork", "0" );
			RS_LOG ( "Have not enoth rigths to create Folders. tryed ($info): " . $info2 );
			break;
		case ("missing right write file") :
		update_option ( "rs_onwork", "0" );
		$t=__ ( "Really-Static dont have enoth writing Rights at the destinationfolder ( $info ) or the foldername may consist illigal signs. please check<a href='http://php-welt.net/really-static/fehler-chmod.html'>manualpage</a>", 'reallystatic' );
		rs_addmessage(true, $t,2);
			RS_log ( $t, 1 );
			break;
		case ("login error") :
			RS_log ( __ ( "Really-Static dont have enoth writing Rights at the destinationfolder ( $info ) or the foldername may consist illigal signs. please check<a href='http://php-welt.net/really-static/fehler-chmod.html'>manualpage</a>", 'reallystatic' ), 1 );
			break;	
	}

}

remove_action( 'wp_head', 'feed_links_extra'); 
remove_action('wp_head', 'wp_shortlink_wp_head', 10, 0 );
remove_action('wp_head', 'rsd_link');
remove_action('wp_head', 'wlwmanifest_link');
 

# remove_action('wp_head', 'post_comments_feed_link');
  #remove_action('wp_head', 'pingback_url');
 
 
#add_filter('post_comments_feed_link',create_function('$a','return;'));
#add_filter('comments_feed_link',create_function('$a','return;'));
#add_filter('comments_rss2_url',create_function('$a','return;'));
#add_filter('bloginfo_url',create_function('$a,$b','if($b=="comments_rss2_url")return; else return $a;'),11,2);  
 
/**
 * @desc Laed eine HTML-Datei herunter und passt sie entsprechend an
 * @param string Url der Datei
 * @return string HTML-Code 
 */
 
function really_static_geturl($url) {
	$content = really_static_download ( $url );
	if ($content === false)
		return $content;
	$content = apply_filters ( "rs-pre-rewriting-filecontent", $content, $url );
	if(preg_match('#<meta name="really-static looptest" content="testtesttest" />#is',$content)>0){
	$content=preg_replace('#<meta name="really-static looptest" content="testtesttest" />#is','',$content);
	}else rs_LOG("Anti-loop controllsum error!");
	$content=preg_replace(  "#".get_option ( 'home' ) . '\/([^/]*)\/(\d+)$#i', get_option ( 'home' ) . '/page/$2/$1',$content);
	if ($content {0} == "\n")
		$content = substr ( $content, 1 );
	if ($content {0} == "\r")
		$content = substr ( $content, 1 );
	$content = preg_replace_callback ( array ('#<link>(.*?)</link>#', '#<wfw:commentRss>(.*?)</wfw:commentRss>#', '#<comments>(.*?)</comments>#', '# RSS-Feed" href="(.*?)" />#', '# Atom-Feed" href="(.*?)" />#' ), create_function ( '$treffer', 'return str_replace(loaddaten("localurl"),loaddaten("remoteurl"),$treffer[0]);' ), $content );
	global $rs_version;
 
	$content = preg_replace ( '#<generator>http://wordpress.org/?v=(.*?)</generator>#', '<generator>http://www.php-welt.net/really-static/version.php?v=$1-RS' . $rs_version . '</generator>', $content );
	#$content = preg_replace ( '#<link rel=(\'|")EditURI(\'|")(.*?)>\n#', "", $content );
	#$content = preg_replace ( '#<link rel=(\'|")wlwmanifest(\'|")(.*?)>#', "", $content );
	#$content = preg_replace ( '#<link rel=(\'|")alternate(\'|")(.*?) hre>#', "", $content );
	$content = preg_replace ( '#<link rel=(\'|")pingback(\'|")(.*?)>\n#', "", $content );
	$content = preg_replace ( '#<link rel=(\'|")shortlink(\'|")(.*?)>\n#', "", $content );	# entfernt<link rel='shortlink' href='http://xyz.de/?p=15/' /> 
	$content = preg_replace ( '#<meta name=(\'|")generator(\'|") content=(\'|")WordPress (.*?)(\'|") />#', '<meta name="generator" content="WordPress $4 - really-static ' . $rs_version . '" />', $content );
	$content = str_replace ( loaddaten ( "rs_designlocal" ), rs_designremote(), $content );
	
	if (substr ( $content, 0, 5 ) != "<?xml") {
		 $content = preg_replace_callback ( '#<a(.*?)href=("|\')(.*?)("|\')(.*?)>(.*?)</a>#si', "urlrewirte", $content );
	} else {
		$content = preg_replace ( "#<wfw\:commentRss>(.*?)<\/wfw\:commentRss>#", "", $content );
		$content = preg_replace_callback ( '#<(link|comments)>(.*?)<\/(link|comments)>#si', "reallystatic_url_rewirte_sitemap", $content );
		$content = preg_replace_callback ( '#<(\?xml\-stylesheet)(.*?)(href)=("|\')(.*?)("|\')(\?)>#si', "reallystatic_url_rewirte_sitemap2", $content );
	} 
	$content = preg_replace_callback ( '#<(link|atom\:link|content)(.*?)(href|xml\:base)=("|\')(.*?)("|\')(.*?)>#si', "url_rewirte_metatag", $content );
 	$content = preg_replace_callback ( '#<img(.*?)src=("|\')(.*?)("|\')(.*?)>#si', "really_static_bildurlrewrite", $content );
	

	if (loaddaten ( "rewritealllinked" ) == 1) {
		$content = str_replace ( loaddaten ( "rs_localurl", 'reallystatic' ), rs_remoteurl(), $content );
		if (substr ( loaddaten ( "rs_localurl", 'reallystatic' ), - 1 ) == "/")
			$content = str_replace ( substr ( loaddaten ( "rs_localurl", 'reallystatic' ), 0, - 1 ), rs_remoteurl(), $content );
		$content = str_replace ( urlencode ( loaddaten ( "rs_localurl", 'reallystatic' ) ), urlencode ( rs_remoteurl() ), $content );
 
		if (substr ( loaddaten ( "rs_localurl", 'reallystatic' ), - 1 ) == "/")
			$content = str_replace ( urlencode ( substr ( loaddaten ( "rs_localurl", 'reallystatic' ), 0, - 1 ) ), urlencode ( rs_remoteurl() ), $content );
	
	
	} 
	$e=str_replace(loaddaten ( "rs_localurl", 'reallystatic' ),rs_remoteurl()."/",rs_remoteurl());
 	if($e!=rs_remoteurl()){
		$content=str_replace($e,rs_remoteurl(),$content);
	}
	$content = apply_filters ( "rs-post-rewriting-filecontent", $content, $url );
	$content = preg_replace ( '#<div id="site-generator">(.*?)<a href="http:(.*?)wordpress.org(.*?) (rel="generator"|title="Semantic Personal Publishing Platform")>(.*?)WordPress(.*?)</div>#sim', '<div id="site-generator"><a href="http://www.php-welt.net/really-static/">powered by really static WordPress</a></div>', $content );
	return $content;

}
/**
 * Ersetzt im geladenen sourcedokument urls so das z.b. nichtspecherbare sonderzeichen entfallen oder z.b. bla.html/2 zu bla/2 :-)
 */
function stupidfilereplace($url, $art = 1) {
	if($art==1)$url=rs_manualrewriteonurlrewrite($url,rs_remoteurl());
	return $url;
/*	#stupidfilereplaceA wird in stupidfilereplaceB umgeschrieben
	

	if (get_option ( "rs_stupidfilereplaceA" ) === false) {
		
		$a = array_merge ( ( array ) loaddaten ( 'rs_posteditcreatedelete' ), ( array ) loaddaten ( 'rs_pageeditcreatedelete' ), ( array ) loaddaten ( 'rs_commenteditcreatedelete' ), ( array ) loaddaten ( 'rs_everyday' ), ( array ) loaddaten ( 'rs_everytime' ) );
		
		$stupidfilereplaceA = array ();
		
		foreach ( $a as $k ) {
			if ($k [1] != "") {
				
				$stupidfilereplaceA [] = '@^' . str_replace ( array ("?", "." ), array ("\?", "\." ), loaddaten ( "remoteurl" ) . $k [0] ) . "$@";
				$stupidfilereplaceB [] = loaddaten ( "remoteurl" ) . $k [1];
				
				if (substr ( $k [0], - 1 ) != "/") {
					$stupidfilereplaceA [] = '@^' . str_replace ( array ("?", "." ), array ("\?", "\." ), loaddaten ( "remoteurl" ) . $k [0] . "/index.html" ) . "$@";
					$stupidfilereplaceB [] = loaddaten ( "remoteurl" ) . $k [1];
				} else {
					$stupidfilereplaceA [] = '@^' . str_replace ( array ("?", "." ), array ("\?", "\." ), loaddaten ( "remoteurl" ) . $k [0] . "index.html" ) . "$@";
					$stupidfilereplaceB [] = loaddaten ( "remoteurl" ) . $k [1];
				}
			
			}
		}
		
		//URL felher ersetzer
		//"lala"
		$stupidfilereplaceA [] = '@\%e2\%80\%93@';
		$stupidfilereplaceB [] = "-";
		$stupidfilereplaceA [] = '@\%e2\%80\%9c@';
		$stupidfilereplaceB [] = "-";
		//ï¿½
		$stupidfilereplaceA [] = '@\%c2\%b4@';
		$stupidfilereplaceB [] = "-";
		
		#multipage
		$stupidfilereplaceA [] = '@(.html)\/(.*?)@';
		$stupidfilereplaceB [] = "/$2";
		foreach ( $stupidfilereplaceA as $k => $v ) {
			$stupidfilereplaceC = str_replace ( str_replace ( array ("?", "." ), array ("\?", "\." ), loaddaten ( "remoteurl" ) ), "", $stupidfilereplaceA );
			$stupidfilereplaceD = str_replace ( loaddaten ( "remoteurl" ), "", $stupidfilereplaceB );
		
		}
		update_option ( "rs_stupidfilereplaceA", $stupidfilereplaceA );
		update_option ( "rs_stupidfilereplaceB", $stupidfilereplaceB );
		update_option ( "rs_stupidfilereplaceC", $stupidfilereplaceC );
		update_option ( "rs_stupidfilereplaceD", $stupidfilereplaceD );
	}
	if ($art == 1)
		return preg_replace ( get_option ( "rs_stupidfilereplaceA" ), get_option ( "rs_stupidfilereplaceB" ), $text );
	else
		return preg_replace ( get_option ( "rs_stupidfilereplaceC" ), get_option ( "rs_stupidfilereplaceD" ), $text );
*/
}
/*
 
function canonicalrewrite($array) {
	$path_parts = pathinfo ( $array [1] );
	if ($path_parts ["extension"] == "") {
		if (substr ( $$array [1], - 1 ) != "/")
			$array [1] .= "/";
	}
	return '<link rel="canonical" href="' . $array [1] . '" />';
}*/
function sitemaprewrite($array) {
	if (rs_nonpermanent === false) {
		// wp_link_pages Problemfix
		// nen.html/2
		// nen/page/2/index.html
		$array [1] = preg_replace ( "#.html/([0-9]+)$#i", '/page/$1/index.html', $array [1] );
	}
	
	$path_parts = pathinfo ( $array [1] );
	if ($path_parts ["extension"] == "") {
		if (substr ( $$array [1], - 1 ) != "/")
			$array [1] .= "/";
	}
	return '<loc>' . $array [1] . '</loc>';
}
/**
*	@desc Korrigiert Links zu Bildern und läd sie hoch
*	@param ARRAY mit kompletten Bild HTML-Code
*	@return String mit HTML-Code vom Bild
*/
function really_static_bildurlrewrite($array) { 
	// RS_LOGA($array);
	if (loaddaten ( "dontrewritelinked" ) == 1)
		return "<img" . $array [1] . "src=" . $array [2] . $array [3] . $array [4] . $array [5] . ">";
	//RS_LOG("AA");
	if(strpos($array [3],loaddaten ( "rs_designlocal"))!==false)return "<img" . $array [1] . "src=" . $array [2] . rs_designremote().substr($array [3],strlen(loaddaten ( "rs_designlocal"))) . $array [4] . $array [5] . ">";
	//RS_LOG("BB");
	$url = $array [3];




	$l = strlen ( loaddaten ( "localurl" ) );
	$ll = strrpos ( $url, "/" );
	if (substr ( $url, 0, $l ) != loaddaten ( "localurl" ))
		return "<img" . $array [1] . "src=" . $array [2] . $array [3] . $array [4] . $array [5] . ">";
	$aa = substr ( $url, strlen ( get_option("fileupload_url") ) );
	$a = substr ( $url, strlen ( loaddaten ( "localurl" ) ) );

	$purl=(parse_url ($url));
	if(pathinfo($purl[path], PATHINFO_EXTENSION )=="php"){
		really_static_uploadcontent( $array [3],$a);
		return "!<img" . $array [1] . "src=" . $array [2] .  loaddaten ( "remoteurl" ).$a . $array [4] . $array [5] . ">";
		return "?<img" . $array [1] . "src=" . $array [2] . $array [3] . $array [4] . $array [5] . ">";
	}
	
	if(is_multisite())  $out =rs_writefilewithlogin ( loaddaten ( "localpath" ).get_option("upload_path") . $aa, $a, true);
	else   $out =rs_writefilewithlogin ( loaddaten ( "localpath" ) . $a, $a, true );	
	if ($out === false) # cachehit
		return "<img" . $array [1] . "src=" . $array [2] . loaddaten ( "remoteurl" ).$a. $array [4] . $array [5] . ">";
	
	
	return "<img" . $array [1] . "src=" . $array [2] . loaddaten ( "remoteurl" ).$a . $array [4] . $array [5] . ">";
}
/**
 * Quelle mit kompletten localepfade
 * Ziel nur relativ vom startpunkt, also pfad vom static order in unterordner wo es hinsoll
 * wenn datumscheck return =false wenn datei schon neuste
 */
function rs_writefilewithlogin($quelle, $ziel, $datumcheck = true) {
	global $wpdb;
	$transport = apply_filters ( "rs-transport", array () );
	call_user_func_array ( $transport [loaddaten ( "rs_save" )] [0], array () );
	$q=substr($quelle,strlen(loaddaten ( "localpath" )));
	$table_name = REALLYSTATICDATABASE;
	$ss = $wpdb->get_results ( "SELECT datum  FROM 	$table_name where url='" .  ( $q ) . "'", OBJECT );
	$fs = @filemtime ( $quelle );
	if($fs===false){
		do_action ( "rs-error", "localfile not found", $quelle,"" );
		return -1;
	}
	if ($datumcheck == true && $ss [0]->datum == $fs){
		do_action ( "rs-info", "cachehit", $quelle,$fs );
		return false;
	}

	
	$wpdb->query ( "Delete FROM $table_name where url='" .  ( $q) . "'" );
	$wpdb->query ( "INSERT INTO `" . $wpdb->prefix . "realstatic` (`url` ,`content`,`datum`)VALUES ('" .  ( $q ) . "','" .  md5_file ( $quelle ) . "', '$fs');" );
	global $internalrun;
	if ($internalrun == true)
		reallystatic_configok ( $quelle, 2 );
	 
	call_user_func_array ( $transport [loaddaten ( "rs_save" )] [2], array ($ziel, $quelle ) );
	 
	do_action ( "rs-info", "write imgfile", $ziel );
	
	do_action ( "rs-write-file", "img", loaddaten ( "remotepath" ) . $ziel, loaddaten ( "realstaticspeicherart", 'reallystatic' ) );
	
	return true;
}
/**
 * passt ein Links in einem Sitemap an
 * @param array url
 * @return string url
 */
function reallystatic_url_rewirte_sitemap($array) {
	$url = str_replace ( loaddaten ( "localurl" ), loaddaten ( "remoteurl" ), $array [2] );
	if (strpos ( $url, loaddaten ( "remoteurl" ) ) !== false) {
		$url = stupidfilereplace ( $url );
		$url = nonpermanent ( $url );
	}
	return "<" . $array [1] . ">" . $url . "</" . $array [3] . ">";
}
/**
 * passt ein Links in einem XML-Stylesheet an
 * @param array xml-url
 * @return string xml-url
 */
function reallystatic_url_rewirte_sitemap2($array) {
	
	$url = loaddaten ( "remoteurl" ) .substr($array [5],strlen( get_option ( 'home' ) . "/"));
	if (strpos ( $url, loaddaten ( "remoteurl" ) ) !== false) {
		$url = stupidfilereplace ( $url );
		$url = nonpermanent ( $url );
	}
	return "<" . $array [1] . "" . $array [2] . "" . $array [3] . "=" . $array [4] . $url . $array [6] . "" . $array [7] . "" . $array [8] . ">";
}
/**
 * passt ein Links in Metatags an
 * @param array url
 * @return string url
 */
function url_rewirte_metatag($array) {
	$url = $array [5];
	if($url==@reallystatic_rewrite(get_bloginfo('comments_rss2_url'),1))return""; # entfernt ?feed=comments-rss2
	if (strpos ( $url, loaddaten ( "remoteurl" ) ) !== false) {
		
		$url = stupidfilereplace ( $url );
		$url = nonpermanent ( $url );
	}
	return "<" . $array [1] . $array [2] . $array [3] . "=" . $array [4] . $url . $array [6] . $array [7] . ">";
}
/*
* @desc ersetzt im Textgefundene Links; sollte eigentlich ausgemustert werden, komme sonst aber nicht an alles
* @param Array $array zerlegter HTML-link
* @returm String kompletter Link
*/
function urlrewirte($array) {
  
	$url = $array [3];
	
	if (rs_nonpermanent === false) {
		$url = preg_replace ( "#/index.html/([0-9]+)$#i", '/$1/index.html', $url );
		$url = preg_replace ( "#.html/([0-9]+)$#i", '/$1/index.html', $url );
	}
	 
	if (strpos ( $url, get_option ( 'siteurl' ) . "/" ) !== false) {
			
		$exts = loaddaten ( "rs_fileextensions" );
		$ext = strrchr ( strtolower ( $url ), '.' );
		if (loaddaten ( "dontrewritelinked" ) != 1 && 1 == $exts [$ext]) {	
			$url=rs_manualrewriteonurlrewrite($url,rs_remoteurl());
			$ll = substr ( $url, strlen ( get_option("fileupload_url") ) );
			$l = substr ( $url, strlen ( loaddaten ( "localurl" ) ) );
			if(is_multisite())really_static_uploadfile ( loaddaten ( "localpath" ).get_option("upload_path") . $ll, $l );
			else really_static_uploadfile ( loaddaten ( "localpath" ) . $l, $l );
			$url = loaddaten ( "remoteurl" ) . $l;
		} else {
			if (loaddaten ( "dontrewritelinked" ) == 1 && 1 == $exts [$ext])
				$url = $array [3];
			$url = stupidfilereplace ( $url );
			$url = nonpermanent ( $url );
		}
	}
	global $seitenlinktransport;
	$seitenlinktransport = "";
	#if (strpos ( $url, "page=" ) !== false)
	#	$url = reallystatic_urlrewrite ( $url, 1 );
if(substr($url,0,strlen(rs_remoteurl()))!=rs_remoteurl() and substr($url,0,strlen(loaddaten ( "localurl" ) ))==loaddaten ( "localurl" ) ){
	$url=rs_remoteurl() . substr($url,strlen(loaddaten ( "localurl" ) ));
}else if($url== substr(loaddaten ("localurl"), 0,-1))$url=rs_remoteurl(); 
	
	
	return "<a" . $array [1] . "href=" . $array [2] . ($url) . $array [4] . $array [5] . ">" . $array [6] . "</a>";
}
/**
 * 
 * @desc Lad eine datei auf den ziel Server
 * @param String $local lokaler Dateipfad
 * @param String $remote entfernter Dateipfad
 * @return boolean je nach Erfolg
 */
function really_static_uploadfile($local, $remote) {
	if(really_static_rememberdonework($local,false,true)!==false)return;
	$fs = @filemtime ( $local );
 	if($fs===false){
 		do_action ( "rs-info", "error file not accessable", $local,$remote );
 	}
	global $wpdb;
	$table_name = REALLYSTATICDATABASE;
	$ss = $wpdb->get_results ( "SELECT datum  FROM 	$table_name where url='" .  ( $local ) . "'", OBJECT );

	if ($ss [0]->datum == $fs) {
		 
		do_action ( "rs-info", "cachehit uploadfile", $local,$remote );
		return false;
	}
	$transport = apply_filters ( "rs-transport", array () );
	 
	call_user_func_array ( $transport [loaddaten ( "rs_save" )] [0], array () );
	
	do_action ( "rs-info", "write file", $local,$remote );
 
	call_user_func_array ( $transport [loaddaten ( "rs_save" )] [2], array ($remote, $local ) );
 
	do_action ( "rs-write-file", "any", $local, loaddaten ( "realstaticspeicherart", 'reallystatic' ) );
 
	$wpdb->query ( "Delete FROM $table_name where url='" .  ( $local ) . "'" );
	$wpdb->query ( "INSERT INTO `" . $wpdb->prefix . "realstatic` (`url` ,`datum`)VALUES ('" .  ( $local ) . "', '$fs');" );
 
	return true;
}



/**
 * 
 * @desc Lad content auf den ziel Server
 * @param String $source downloadurl
 * @param String $remote entfernter Dateipfad
 * @return boolean je nach Erfolg
 */
function really_static_uploadcontent($source, $desination) {
rs_log(" really_static_uploadcontent($source, $desination) {");
 	global $wpdb;
	$content=really_static_download($source);
	$dontrefresh=rs_cachetest($desination,$content,false,$source);
	if(!$dontrefresh){
		$transport = apply_filters ( "rs-transport", array () );
		call_user_func_array ( $transport [loaddaten ( "rs_save" )] [0], array () );
		#	do_action ( "rs-info", "write file", $local,$remote );
		
			$remote = substr ( $source, strlen ( loaddaten ( "localurl" ) ) );
		
		rs_log("-->($remote,  ) ");
		
		call_user_func_array ( $transport [loaddaten ( "rs_save" )] [4], array ($remote, $content ) );
		#	do_action ( "rs-write-file", "any", $local, loaddaten ( "realstaticspeicherart", 'reallystatic' ) );	 	
		rs_cacheset($source,$content,$dontrefresh );
	}
	return true;
}









function really_static_rememberdonework($key,$value=false,$askandset=false){
	global $really_static_rememberdonework;


	if($value===false){//abfrage
		if(isset($really_static_rememberdonework[md5($key)])) return $really_static_rememberdonework[md5($key)];
		else {
		if($askandset===true)	$really_static_rememberdonework[md5($key)]=1;
		  return false;
		}
	}else{//speichern
	
		$really_static_rememberdonework[md5($key)]=$value;
	
	
	}


}

/*
* Erneuern einer einzelnen seite
* Hauptfunktion 2
*/
function getnpush($get, $push, $allrefresh = false) {
	global $notagain, $wpdb;
	if (loaddaten ( "dontrewritelinked" ) != 1) {
		$push = str_replace ( loaddaten ( "remoteurl" ), "", stupidfilereplace ( loaddaten ( "remoteurl" ) . $push ) );
		$push = nonpermanent ( $push );
	}
	$path_parts = pathinfo ( $push );
	
	if ($path_parts ["extension"] == "") {
		if (substr ( $push, - 1 ) != "/")
			$push .= "/index.html";
		else
			$push .= "index.html";
	}
	
	$table_name = REALLYSTATICDATABASE;
	if ($allrefresh !== false) {
		//timeout hile
		$querystr = "SELECT datum,content  FROM 	$table_name where url='" .  ( 	substr($get,strlen(loaddaten("localurl"))) ) . "'";
		$ss = $wpdb->get_results ( $querystr, OBJECT );
		$contentmd5 = $ss [0]->content;
		$lastmodifieddatum = $ss [0]->datum;
		if ($allrefresh === true and $lastmodifieddatum > 0) {
			return;
		}
	}
	
	#global $arbeitsliste;
	#$arbeitsliste [update] [$push] = $get;
	rs_arbeitsliste_create_add($push,$get);
}


/**
 * @desc Entfernt moegliche urlvorsetze --> realer pfad wordpresspfad ohne domain davor
 */
function cleanupurl($url) {
	return str_replace ( get_option ( 'home' ) . "/", "", $url );
	return str_replace ( array (get_option ( 'siteurl' ) . "/", get_option ( 'siteurl' ), loaddaten ( "localurl" ), loaddaten ( "remoteurl" ) ), array ("", "", "", "" ), $url );
}
function rs_arbeitsliste_create_add($get,$push,$firstrun=true){
	
	$get=str_replace("//","/",$get);
	$get=rs_manualrewriteonurlrewrite($get,"");
	if($firstrun)$get=rs_manualrewriteaddtodo($get);
	global $arbeitsliste;
	$arbeitsliste [update] [$get] = $push;
}

add_filter('rs-todolist-add-indexlink',"rsindexlink",999,2);
function rsindexlink($url,$replace){
	if(ereg("%indexurl%",$url))$url=str_replace("%indexurl%",$replace,$url);
	return $url;
}
add_filter('rs-todolist-add-taglink',"rstaglink",999,2);
function rstaglink($url,$replace){
	if(ereg("%tagurl%",$url))$url=str_replace("%tagurl%",$replace,$url);
	return $url;
}
add_filter('rs-todolist-add-catlink',"rscatlink",999,2);
function rscatlink($url,$replace){
	if(ereg("%caturl%",$url))$url=str_replace("%caturl%",$replace,$url);
	return $url;
}

add_filter('rs-todolist-add-authorlink',"rsauthorlink",999,2);
function rsauthorlink($url,$replace){
	if(ereg("%authorurl%",$url))$url=str_replace("%authorurl%",$replace,$url);
	return $url;
}
add_filter('rs-todolist-add-datedaylink',"rsdatedaylink",999,2);
function rsdatedaylink($url,$replace){
	if(ereg("%dateurl%",$url))$url=str_replace("%dateurl%",$replace,$url);
	return $url;
}
add_filter('rs-todolist-add-datemonthlink',"rsdatemonthlink",999,2);
function rsdatemonthlink($url,$replace){
	if(ereg("%dateurl%",$url))$url=str_replace("%dateurl%",$replace,$url);
	return $url;
}
add_filter('rs-todolist-add-dateyearlink',"rsdateyearlink",999,2);
function rsdateyearlink($url,$replace){
	if(ereg("%dateurl%",$url))$url=str_replace("%dateurl%",$replace,$url);
	return $url;
}
add_filter('rs-todolist-add-commentlink',"rscommentlink",999,2);
function rscommentlink($url,$replace){
	if(ereg("%dcommenturl%",$url))$url=str_replace("%commenturl%",$replace,$url);
	return $url;
}


add_filter('trash_comment', "sdfsdcc");
add_filter('spam_comment', "sdfsdcc"); 
add_filter('untrash_comment', "sdfsdcc");
add_filter('unspam_comment', "sdfsdcc");

/**
 * sammelt vorabinformationen
 */
function sdfsdcc($cid) {
 
	$c = get_comment ( $cid );
	global $rs_commentpostionsinfo;
	$rs_commentpostionsinfo = rs_commentpageinfo ( $c->comment_post_ID, $c->comment_ID );
}



 

add_filter('comment_save_pre', "sdfsda"); #wp_update_comment holt id aus 
add_filter('preprocess_comment', "sdfsdb"); #erstell
add_filter('get_comment', "sdfsdc");
function sdfsda($a){

	global $rs_cid,$rs_commentpostionsinfo;  $c=get_comment($rs_cid);
	#RS_LOGA($c);
	$rs_commentpostionsinfo=rs_commentpageinfo($c->comment_post_ID,$c->comment_ID);

	return $a;
}
function sdfsdb($c){
 
global $rs_commentpostionsinfo;
$rs_commentpostionsinfo=rs_commentpageinfo($c[comment_post_ID]);return $c;}
function sdfsdc($a){global $rs_cid; $rs_cid=$a->comment_ID;return $a;}


add_action('pre_post_update', "sdfsd");


function sdfsd($id,$return=false){
	$gp=get_post ( $id );
	if($return==false)global $oldpagepost;
	foreach(wp_get_post_categories($id) as $v)$oldpagepost[cat]=get_category_parentids($oldpagepost[cat],$v,$gp );
	foreach(wp_get_post_tags($id) as $v)$oldpagepost[tag]=get_tag_parentids($oldpagepost[tag],$v->term_id,$gp );
	$oldpagepost[date]=$gp->post_date;
	$oldpagepost[author][art]=$gp->post_author;
	$oldpagepost[author][gesamt]=sdfsdfs($gp->post_author,0);
	$oldpagepost[author][page]=sdfsdfs($gp->post_author, strtotime($oldpagepost[date]));
	
	$oldpagepost[page][postgesamt]=main_count_post();
	$oldpagepost[page][post]=main_count_post_until($oldpagepost[date]);
	$oldpagepost[page][date_day_gesamt]=rs_post_on_page( date_i18n( "Y-m-d 00:00:00", strtotime($oldpagepost[date])), date_i18n("Y-m-d 23:59:59", strtotime($oldpagepost[date])),"post_type='post' and post_status = 'publish'");
	$oldpagepost[page][date_month_gesamt]=rs_post_on_page( date_i18n( "Y-m-1 00:00:00", strtotime($oldpagepost[date])), date_i18n("Y-m-". date_i18n( "t", $oldpagepost[date] )." 23:59:59", strtotime($oldpagepost[date])),"post_type='post' and post_status = 'publish'");
	$oldpagepost[page][date_year_gesamt]=rs_post_on_page( date_i18n( "Y-1-1 00:00:00", strtotime($oldpagepost[date])), date_i18n("Y-12-31 23:59:59", strtotime($oldpagepost[date])),"post_type='post' and post_status = 'publish'");
	
	$oldpagepost[page][date_day]=1+$oldpagepost[page][date_day_gesamt]-rs_post_on_page( date_i18n( "Y-m-d H:i:s", strtotime($oldpagepost[date])), date_i18n("Y-m-d 23:59:59", strtotime($oldpagepost[date])),"post_type='post' and post_status = 'publish'");
	$oldpagepost[page][date_month]=1+$oldpagepost[page][date_month_gesamt]-rs_post_on_page( date_i18n( "Y-m-1 H:i:s", strtotime($oldpagepost[date])), date_i18n("Y-m-". date_i18n( "t", $oldpagepost[date] )." 23:59:59", strtotime($oldpagepost[date])),"post_type='post' and post_status = 'publish'");
	$oldpagepost[page][date_year]=1+$oldpagepost[page][date_year_gesamt]-rs_post_on_page( date_i18n( "Y-1-1 H:i:s", strtotime($oldpagepost[date])), date_i18n("Y-12-31 23:59:59", strtotime($oldpagepost[date])),"post_type='post' and post_status = 'publish'");
	if($return==true)return $oldpagepost;
}

 

function rs_posteditdiff($postid){
	global $oldpagepost;
	$nd=sdfsd($postid,true);
	$do[cat]=array_diff_key ($oldpagepost[cat][page],$nd[cat][page]);
	$do[tag]=array_merge(array_keys(array_diff_key ($oldpagepost[tag][page],$nd[tag][page])),array_keys(array_diff_key ($nd[tag][page],$oldpagepost[tag][page])));
	$do[author_post]=$nd[author];
	$do[author_pre]=$oldpagepost[author];
	if($oldpagepost[date]!=$nd[date])$do[date]=array($oldpagepost[date],$nd[date]);
	else $do[date]=array("",$nd[date]);
	$do[pre_page]=$oldpagepost[page];
	$do[post_page]=$nd[page];
	$do[cat_pre]=$oldpagepost[cat];
	$do[cat_post]=$nd[cat];
	$do[tag_pre]=$oldpagepost[tag];
	$do[tag_post]=$nd[tag];
	return $do;
}
function rs_post_on_page($oben,$unten,$subbedingung="1"){
	global $wpdb;
	$q="SELECT count(ID) as outa FROM " . $wpdb->prefix . "posts where post_status = 'publish' AND post_date>='" .$oben. "' and post_date<='$unten' and $subbedingung";
	 //RS_LOG("rs_post_on_page:".$wpdb->get_var( $wpdb->prepare($q) ) ."       via: $q");
	 
	  return $wpdb->get_var( @$wpdb->prepare($q) );
}
/**
postid
commentid
*/
function rs_commentpageinfo($id,$cid=-1){
	global $post;
	#RS_LOG( "$id,$cid");
	#RS_LOGA( $post);
	if($id=="")$id=$post->ID;
	
//	RS_LOG("last_changed".	wp_cache_get('last_changed', 'comment'));
	wp_cache_set('last_changed',mt_rand(0,time()-99) , 'comment'); // bekomme immer aktuelle werte
	//wp_cache_flush();
//	RS_LOG("last_changed".	wp_cache_get('last_changed', 'comment'));
	$args = array(
			'author_email' => '',
			'ID' => '',
			'karma' => '',
			'number' => '',
			'offset' => '',
			'orderby' => '',
			'order' => get_option("comment_order"),
			'parent' => '0',
			'post_id' => $id,
			'post_author' => '',
			'post_name' => '',
			'post_parent' => '',
			'post_status' => '',
			'post_type' => '',
			'status' => '',
			'type' => '',
			'user_id' => '',
			'search' => '',
			'count' => false
	);
#	$a[gesamt]=floor(count(get_comments( $args ))/ get_option ( "comments_per_page" ));
	$a[gesamt]=count(get_comments( $args ));
	#RS_LOG(" rs_commentpageinfo($id,$cid) = ".$a[gesamt]);
	
	
	if($cid==-1)return $a;
	
 
	
		$args = array(
			'author_email' => '',
			'ID' => '',
			'karma' => '',
			'number' => '',
			'offset' => '',
			'orderby' => '',
			'order' => 'DESC',
			'parent' => '',
			'post_id' => $id,
			'post_author' => '',
			'post_name' => '',
			'post_parent' => '',
			'post_status' => '',
			'post_type' => '',
			'status' => '',
			'type' => '',
			'user_id' => '',
			'search' => '',
			'count' => false
	);
	$i=0;
	#RS_LOGA(get_comments( $args ));
	foreach(get_comments( $args ) as $k=>$v){
		if($v->comment_parent==0)$i++;
		if($v->comment_ID==$cid)break;
	
	}
	$a[page]= $i ;
	return $a;
}

// Wird aufgerufen wenn ein Post editiert wird
add_action ( 'publish_to_publish', create_function ( '$array', ' rs_setba(2);	RS_log ("edit POST ".$array->ID);renewrealstaic($array->ID); ' ) );

// Post wurde veroeffentlicht
add_action ( 'private_to_published', create_function ( '$array', ' rs_setba(1); 	RS_log ("publ POST ".$array);	renewrealstaic($array, 123,"postcreate");rs_refreshallcomments($array->ID);' ) );

// Post wurde gelöscht
add_action ( 'publish_to_trash', create_function ( '$array', '  rs_setba(3); 	RS_log ("del POST ");renewrealstaic($array->ID, 123,"postdelete");

		rs_refreshallcomments($array->ID);
		
		
		
		' ) );
/**
	wird aufgerufen wenn post gelöscht wird
*/
function rs_refreshallcomments($postid){
 
	$args = array(
			"author_email" => "",
			"ID" => "",
			"karma" => "",
			"number" => "",
			"offset" => "",
			"orderby" => "",
			"order" =>"",
			"parent" => "0",
			"post_id" => $postid,
			"post_author" => "",
			"post_name" => "",
			"post_parent" => "",
			"post_status" => "",
			"post_type" => "",
			"status" => "",
			"type" => "",
			"user_id" => "",
			"search" => "",
			"count" => false
	);
 
	foreach(get_comments( $args ) as $v)reallystatic_editkommentar($v->comment_ID,true);
}

/**
 * @desc Die Hauptfunktion die alle subseiten mit statisiert
 *
 * @param int $id
 * @param int $allrefresher
 * @param string $operation
 * @param array $subarbeitsliste ...enthält arbeitszeugs v: d(elete)/r(efrsh)
 * @return void
 */

function renewrealstaic($id, $allrefresher = 123, $operation = "", $subarbeitsliste = "") {
	global $post;
	$post = get_post ( $id );
	global $iscomment, $allrefresh, $homeurllaenge;
	$homeurllaenge = strlen ( get_option ( 'home' ) . "/" );
	$allrefresh = $allrefresher;
	global $wpdb, $notagain, $wp_rewrite;
	//test ob es ein Entwurf ist
	$table_name = $wpdb->prefix . "posts";
	//Eintraege pro post
	$querystr = " SELECT post_status  FROM $table_name where id='" . $id . "'";
	$post_status = $wpdb->get_results ( $querystr, OBJECT );
	$post_status = $post_status [0]->post_status;
	
	if ($post_status == "draft" or wp_is_post_autosave ( $id ))
		return;
		
	if($post->post_type	=="post" and rs_getbo()!=3)rs_setbo(1);
	elseif($post->post_type	=="page"  and rs_getbo()!=3)rs_setbo(2);
		
		
		
		
		
	global $arbeitsliste;
	$notagain [$id] = 1;
	global $publishingpost;
	if ($_POST ["originalaction"] == "post")
		$publishingpost = true;
	$table_name = $wpdb->prefix . "options";
	//Eintraege pro post
	$querystr = " SELECT option_value  FROM $table_name where option_name='posts_per_page'";
	$pageposts = $wpdb->get_results ( $querystr, OBJECT );
	$pageposts = $pageposts [0]->option_value;
	$table_name = $wpdb->prefix . "posts";
	//Eintraege pro post
	$querystr = "SELECT post_date  FROM $table_name where ID='$id'";
	$erstell = $wpdb->get_results ( $querystr, OBJECT );
	$erstell = $erstell [0]->post_date; //wann wurde post erstellt
	
	
	
 
	
	
	$posteditdiff = @rs_posteditdiff ( $id );
		

	if ($operation != "komentaredit" && ! ($post->post_type == "page")) {
		if ($operation != "komentarerstellt" or loaddaten ( "realstaticrefreshallac" )) {
			index_refresh ( $erstell,$pageposts,$homeurllaenge,$posteditdiff );
		}
	}	

	
 

	
	if ($operation != "komentaredit" or isset ( $subarbeitsliste ["seiteselber"] )) {
		#	RS_LOG("Seite selber");
		seiteselberrefresh($id,$operation,$homeurllaenge,$subarbeitsliste,$post_status);


	}
 
	if (is_array ( $subarbeitsliste ))
		unset ( $subarbeitsliste ["seiteselber"] );
	
		#RS_LOG("subcommentarseiten");
	

	if (is_array ( $subarbeitsliste ))comment_refresh($id,$homeurllaenge,$subarbeitsliste);
	if ($post->post_type == "page")
		return;
	if ($operation == "komentaredit")
		return;
	if ($operation == "komentarerstellt" and ! loaddaten ( "realstaticrefreshallac" ))
		return;
	
	author_refresh($id,$posteditdiff,$erstell, $pageposts, $operation, $homeurllaenge,$authorid);
	
	categorry_refresh($posteditdiff,$homeurllaenge,$pageposts,$operation);
	
	tag_refresh( $posteditdiff,$erstell, $pageposts, $k, $operation, $homeurllaenge,$pageposts);
	
	date_refresh($posteditdiff, $erstell, $operation, $homeurllaenge, $pageposts);
	
	
	



}
/**
 * weitere Statische dateien hinzu
 */
function reallystaticsinglepagehook($url) {
	
	global $arbeitsliste;
	rs_arbeitsliste_create_add($url,loaddaten ( "localurl" ) . $url);
 

}
/**
 * weitere Statische dateien loeschen
 */
function reallystaticsinglepagedeletehook($url) {
	global $arbeitsliste;
	$arbeitsliste [delete] [$url] = 1;
}
/**
 * Loeschen durchfuehren
 */
function reallystaticdeletepage($url) {
	global $wpdb;
	
	global $arbeitsliste;
	
	$table_name = REALLYSTATICDATABASE;
	if ($url != "") {
		$arbeitsliste [delete] [loaddaten ( "remotepath" ) . $url] = 1;
	} else {
		global $reallystaticsinglepagedelete;
		if (isset ( $reallystaticsinglepagedelete )) {
			foreach ( $reallystaticsinglepagedelete as $v ) {
				$arbeitsliste [delete] [loaddaten ( "remotepath" ) . $v] = 1;
			}
		}
	
	}

}

/**
 * Fuer das AdminMenue
 * @param void
 * @return void
 */
add_action ( 'admin_menu', 'really_static_mainmenu', 22 );
function really_static_mainmenu() {
	if (function_exists ( 'add_submenu_page' ))
		add_submenu_page ( 'options-general.php', 'Really Static', 'Really Static', 10, REALLYSTATICMAINFILE, 'really_static_settings' );
	else
		add_options_page ( 'Really Static', 'Really Static', 8, REALLYSTATICMAINFILE, 'really_static_settings' );
}

add_filter('plugin_row_meta', "rs_aditionallinks", 10, 2);
function rs_aditionallinks($links, $file) {
	if ($file == basename(dirname(__FILE__)) . '/' . basename(__FILE__)) {
		$links[] = '<a href="options-general.php?page=' . REALLYSTATICBASE .'&menu=123">goto the 1-2-3 quicksetup again</a>';
		$links[] = '<a href="http://really-static-support.php-welt.net/">' . __('Support', 'really_static') . '</a>';
	}
	return $links;
}

function really_static_multimainmenu() {
	add_submenu_page( 'settings.php', 'Really Static', 'Really Static', 'manage_options', REALLYSTATICMAINFILE, 'really_static_multisettings' );
}
add_action('network_admin_menu', 'really_static_multimainmenu');
function really_static_multisettings() {
	/*if(get_site_option( 'testtest' )===false)add_site_option( 'testtest', 0 );
	update_site_option( 'testtest',get_site_option( 'testtest' ) +1);
	echo "!!".get_site_option( 'testtest' );*/
	require_once ("php/multiadmin.php");
}
/**
 * Setingsmenue
 * @param void
 * @return void
 */
function really_static_settings() {
	require_once("php/functions.php");
	$base = plugin_basename ( __FILE__ );
	if (is_array ( $_POST ))
		require_once ("php/configupdate.php");
	if ($_GET ["menu"] == "123")
		require_once ("php/123.php");
	else {
		$h = "";
		$reallystaticfile = filemtime ( __FILE__ );
		require_once ("php/admin.php");
		 
	}
}

/**
 * Zeigt ein Problemmeldung an
 * @param int Formatierungsart
 * @param string Textliche Problemmeldung
 * @return void
 */
function reallystatic_configerror($id, $addinfo = "") {
	if ($id == 0) {
		echo '<div id="front-page-warning" class="updated" style="background-color:#FF9999;border-color:#990000;">
	<p>' . $addinfo . '</p>
</div>';
	} else {
		echo '<div id="front-page-warning" class="updated fade-ff0000">
	<p>';
		reallystatic_errorinfo ( $id, $addinfo );
		echo '</p>
</div>';
	}
}
/**
 * Zeigt ein OK ab
 * @param string
 * @param int Formatierungsart
 * @return void
 */
function reallystatic_configok($text, $typ = 1) {
 
	if ($typ == 1)
		echo '<div id="message" class="updated" style="background: #FFA; border: 1px #AA3 solid;"><p>' . $text . '</p></div>';
	elseif ($typ == 3) {
		 echo '<script type="text/javascript">doingout("<b>Ready</b>");  document.getElementById("tabs").style.display = "block";	 document.getElementById("refreshallinfo").style.display = "none";	  </script> ';
		# $(".tabs div").show();  $(".tabNavigation div").show();
		
	 
	} else {
		global $showokinit;
		if ($showokinit != 2) {
			#if ($_POST ["pos"] == "3")
			#	echo "<h2>Generating Files</h2>" . __ ( "Really-Staic is now generating, static files out of your Blog. Please wait until really-static is ready." );
					$showokinit = 2;
			echo '<div id="okworking"  class="updated fade">please wait</div><script type="text/javascript">	function doingout(text){	document.getElementById(\'okworking\').innerHTML = text;	}	 	</script>';
		}
		echo (str_pad('<script type="text/javascript">document.getElementById(\'refreshallinfog\').innerHTML = "'.date_i18n("H:i:s").'";  doingout("<b>Working on:</b> ' . $text . '");</script>',2048," "))."\n";#' . $text . "<br>";
	}
	@ob_flush ();
	flush ();

}

function reallystatic_errorinfo($id, $addinfo = "") {
	
	require ("php/errorid.php");
}
/**
 * a = entscheider
 * t=1 = nur true false; t=2 rest
 * dann b
 * sonst c
 */
function ison($a, $t, $b, $c = "", $d = "") {
	global $ison;
	if ($t == 1) {
		if ($a === true) {
			$ison ++;
			return $b;
		} else {
			$ison --;
			return $c;
		}
	} elseif ($t == 2) {
		if ($a == true) {
			$ison ++;
			return $b;
		} else {
			$ison --;
			return $c;
		}
	} elseif ($t == 3) {
		if ($a == $d) {
			$ison ++;
			return $b;
		} else {
			$ison --;
			return $c;
		}
	}
}



/**
 * 
 * @desc wird aufgerufen fuer Kategorien
 * @param unknown_type $operation  art des postedits
 * @param unknown_type $erstell timestamp
 * @param unknown_type $pageposts post pro seite
 * @param unknown_type $category
 * @param unknown_type $allrefresh
 * @param unknown_type $muddicat
 * @param unknown_type $url
 */
function really_static_categoryrefresh($category,$homeurllaenge, $seite , $seitemax ,$art) {
	
	#RS_LOG("really_static_categoryrefresh $category, $seite , $seitemax ,$art");
	global $publishingpost, $rscatnewpage, $arbeitsliste, $homeurllaenge;
	for($seiter = $seite; $seiter <= $seitemax; $seiter ++) {
		if ($seiter > 1) {
			if (rs_nonpermanent == true)
				$seitee = "&paged=$seiter";
			else
				$seitee = "/page/$seiter";
		} else
			$seitee = "";
		foreach ( loaddaten ( "rs_makestatic_a3" ) as $value ) {
			$url = $value [1];
			if ($url == "")
				$url = $value [0];
			global $seitenlinktransport;
			$seitenlinktransport = $seitee;
			$templink = apply_filters ( "rs-todolist-add-catlink", $url, substr ( get_category_link ( $category ), $homeurllaenge ) );
			if($art=="update"){
				$templink = apply_filters ( "rs-todolist-add", $templink );
				if ($templink !== false)
					rs_arbeitsliste_create_add ( reallystatic_rewrite ( $templink, 1 ), loaddaten ( "localurl" ) . really_static_make_to_wp_url ( $templink ) );
			}else{
				$templink = apply_filters ( "rs-todolist-delete", $templink );
				if ($templink !== false)
					rs_arbeitsliste_delete_add ( reallystatic_rewrite ( $templink, 1 ), loaddaten ( "localurl" ) . really_static_make_to_wp_url ( $templink ) );				
				
			}
		}
	}
}
/**
 * @desc Teste ob es eine neue Version von Really-Static gibt
 * @return boolean true wenn neue version
 */
function testfornewversion() { 
	$rz = get_option ( "rs_versionsize" );
	$rd = get_option ( "rs_versiondate" );
	#$rd = get_option ( "rs_versiondate" );
	if ((time () - $rd) > (86400 * RSVERSIONCHECK)) {
	 $data = new stdClass;
  $data->checked=true;
 
$data=rs_check_for_plugin_update($data);
if(isset($data->response)) {  delete_site_transient('update_plugins');}
 
	
	
		$out = @get_headers ( "http://downloads.wordpress.org/plugin/really-static.zip", 1 );
		update_option ( "rs_versiondate", time () );
		update_option ( "rs_versionsize", $out ["Content-Length"]);
	 
		if ($rz!="" and $out ["Content-Length"] != $rz)
			return true;
	}
	return false;
}
function versionupdate(){

 
if(testfornewversion()==true){ update_option ( "rs_newversion", "ja" ); delete_site_transient('update_plugins');}
 


}

 

add_filter('site_transient_update_plugins', 'rs_transient_update_plugins');
add_filter('update_plugin_complete_actions', 'rs_hasbeenupdatecheck',10,2);

function rs_hasbeenupdatecheck($eee,$p){
 
if($p==REALLYSTATICBASE)update_option ( "rs_newversion" ,"nein");
return $eee;
}

function rs_transient_update_plugins($a){
 if(get_option ( "rs_newversion" )=="ja" && get_option("rs_informaboutupdate")==1){
	@$t->id=7007;
	$t->slug="really-static";
	$t->new_version=RSVERSION+0.01;
	$t->url="http://wordpress.org/extend/plugins/really-static/";
	$t->package="http://downloads.wordpress.org/plugin/really-static.zip";
	$a->response[REALLYSTATICBASE]=$t;
	#RS_LOGA($a);
}
#RS_LOG("##".get_option("rs_informaboutupdate"));
#RS_LOG("##".get_option ( "rs_newversion" ));
#RS_LOG("------------");
return $a;
}




/**
 * @desc Behandelt alle Links mit ? und formt sie um
 * @param string $url URL die bearbeitet werden soll
 * @return string ueberarbeite URL
 */
function nonpermanent($url) {
	return $url;
	if ($url == "")
		return "index.html";
	if (rs_nonpermanent != true) {
		
		if (substr ( $url, - 1 ) != "/" && strpos ( str_replace ( loaddaten ( "remoteurl" ), "", $url ), "." ) === false) {
			if (strpos ( $url, "#" ) === false)
				return $url . "/";
			else
				return $url;
		} else
			return $url;
	}
	#	RS_LOGD($url);
	$url = preg_replace ( "#\&amp;cpage=(\d+)#", "", $url );
	if (strpos ( $url, "?" ) !== false) {
		$url = str_replace ( "&#038;", "/", $url );
		$url = str_replace ( "&", "/", $url );
		if (strpos ( $url, "#" ) !== false)
			$url = str_replace ( "#", "/index.html#", str_replace ( "?", "", $url ) );
		else
			$url = str_replace ( "?", "", $url ) . "/";
	}
	$url = preg_replace ( "#" . loaddaten ( "remoteurl" ) . "wp-trackback.phpp\=(\d+)#", loaddaten ( "localurl" ) . "wp-trackback.php?p=$1", $url );
	if (substr ( $url, - 2 ) == "//")
		$url = substr ( $url, 0, - 1 );
	if (substr ( $url, - 1 ) == "/")
		$url = $url . "index.html";
	
		#RS_LOGD("OUT:".$url); 
	return $url;
}

// Fuegt Links ins PLUGIN Menue von Wordpress
add_filter ( 'plugin_row_meta', create_function ( '$links, $file', '$base = plugin_basename ( __FILE__ );	if ($file == $base) {		$links [] = "<a href=\"options-general.php?page=" . $base . "\">" . __ ( "Settings" ) . "</a>";		$links [] = "<a href=\"http://blog.phpwelt.net/tag/really-static/\">" . __ ( "Support" ) . "</a>";		$links [] = "<a href=\"http://www.php-welt.net/really-static/index.html#donate\">" . __ ( "Donate" ) . "</a>";	}	return $links;' ), 10, 2 );

add_filter ( 'extra_plugin_headers', 'rs_apply_filters_plugin' );
function rs_apply_filters_plugin($a){
$a["RSInfo"]="RS Info";
return $a;
}
add_action ( 'edit_form_after_editor', 'wp_default_scripts2' );
/**
 * @desc Erinnert den Benutzer doch etwas zu s p e n d e n bei der nachrichteneingabe
 * @param object
 * @return object
 */
function wp_default_scripts2() {
 

	if ((loaddaten ( "rs_donationid" ) == "-" || loaddaten ( "rs_donationid" ) == "") and loaddaten ( "rs_counter" ) > 2000) {
	echo '<div style="padding:10px;margin:5px;border-width: 2px; border-style: dashed; border-color: red; ">You are using really-static for a long long time. <a target="_blank" href="http://really-static-support.php-welt.net/why-donate--t8.html">Please donate</a></div>';
 	}
	
 
}
/**
 * @desc Fuegt ein index.html an Ordnernamen an
 * @param string $url URL die bearbeitet werden soll
 * @return string
 */
function urlcorrect($url) {
	if (rs_nonpermanent != true) {
		$path_parts = pathinfo ( $url );
		if ($path_parts ["extension"] == "") {
			
			if (substr ( $url, - 1 ) != "/")
				$url .= "/index.html";
			else
				$url .= "index.html";
		}
	}
	return $url;
}

add_action ( 'delete_attachment', create_function ( '$a', 'global $dontwrite;$dontwrite=1; RS_log("delteattach:".$a);' ) );
add_action ( 'edit_attachment', create_function ( '$a', 'RS_log("editattach:".$a);' ) );

 



############
#
#
# COMMENTAR START
#
#
###############
#add_action ( 'trash_comment', 'reallystatic_deletekommentar' );

add_action ( 'comment_post', 'reallystatic_newkommentar', 999, 2 );
add_action ( 'edit_comment', 'reallystatic_editkommentar' );
//add_action ( 'transition_comment_status', 'reallystatic_kommentarjob',10,3 );
#add_action ( 'trashed_comment', 'reallystatic_editkommentar' );
#add_action ( 'spammed_comment', 'reallystatic_editkommentar' );
add_action ( 'transition_comment_status', 'rs_transition_comment_status',10,3 );
#add_action ( 'untrashed_comment', 'reallystatic_editkommentar' );
#add_action ( 'unspammed_comment', 'reallystatic_editkommentar' );
 


function rs_transition_comment_status($n,$o,$c){

 
	
	if(($o=="unapproved" && $n=="approved") || ($o=="approved" && $n=="unapproved")||($o=="approved" && $n=="spam")||($o=="spam" && $n=="approved")
		||  ($o=="approved" && $n=="trash")||($o=="trash" && $n=="approved")){
		global $post;
		
		if(!$post)$post=get_post($c->comment_post_ID);
		reallystatic_editkommentar($c->comment_ID);
	}
	
	#RS_LOG("$o ======>>>> $n");
}
function commentseite($gesamt,$page,$proseite){
	
	 if(get_option('default_comments_page')=="newest")$o= (floor(($gesamt-$page)/$proseite))+1;
	 else $o= floor(($gesamt-$page)/$proseite)+1;
	 
	#RS_LOG(" commentseite($gesamt,$page,$proseite) = $o");
	return $o;
}


/**
 * 
 * @desc Wird nach einer Komentaredition aufgerufen
 * @param int $id ID des Kommentars
 * @return void
 */
function reallystatic_editkommentar($id,$zwangsgrill=false) {
	global $rs_commentpostionsinfo;
 
	 
	if($zwangsgrill==false){
	if(!(rs_getbo()==3 and 	rs_getba()==1)){
		rs_setbo(3);
		rs_setba(2);
	}
	
	
	
	#RS_LOG ( "reallystatic_editkommentar" );
	$comment = get_comment ( $id );
	if($comment->comment_approved!="1" and $comment->comment_approved!="approved"){
		rs_setbo(3);
		rs_setba(3);
	}
	
	
	# RS_LOGA ( $comment );
	#RS_LOGA ( rs_commentpageinfo ( $comment->comment_post_ID, $comment->comment_ID ) );
	
	$vorher = $rs_commentpostionsinfo;
	$nachher = rs_commentpageinfo ( $comment->comment_post_ID, $comment->comment_ID );
	#RS_LOGA($vorher);
 
	
	if ($vorher [gesamt] < $nachher [gesamt]) {
	#	RS_LOG ( "commentar add" .get_option('default_comments_page'));
		if(get_option('default_comments_page')=="newest") $bis= commentseite ( $nachher [gesamt],1, get_option ( "comments_per_page" ));
				else  $bis= commentseite ( $nachher [gesamt], 1, get_option ( "comments_per_page" ));
				$von=commentseite ( $nachher [gesamt], $nachher [page], get_option ( "comments_per_page" ) );
					if(get_option('default_comments_page')=="newest"){
						if (ceil ( $vorher [gesamt] / get_option ( "comments_per_page" ) ) != ceil ( $nachher [gesamt] / get_option ( "comments_per_page" ) )) {
							$von=1;
						}
						
					}
				
				
		for($i = $von; $i <= $bis; $i ++) {
			 #RS_LOG("###########$i");
				$arbeiter [$i] = "r";
		}
	 
		
	} else if ($vorher [gesamt] == $nachher [gesamt]) {
		RS_LOG ( "commentar edit" );
		if ($vorher [page] == $nachher [page]) {
		#	RS_LOG ( "text edit" );
			$arbeiter [commentseite ( $nachher [gesamt], $nachher [page], get_option ( "comments_per_page" ) )] = "r";
		} else {
		#	RS_LOG ( "datums edit" );
			$arbeiter [commentseite ( $vorher [gesamt], $vorher [page], get_option ( "comments_per_page" ) )] = "r";
			$arbeiter [commentseite ( $nachher [gesamt], $nachher [page], get_option ( "comments_per_page" ) )] = "r";
		}
	} else {
	#	RS_LOG ( "commentar grill" .get_option('default_comments_page'));
		
		if (ceil ( $vorher [gesamt] / get_option ( "comments_per_page" ) ) != ceil ( $nachher [gesamt] / get_option ( "comments_per_page" ) )) {
			
			if(get_option('default_comments_page')=="newest"){
				$arbeiter [commentseite ( $vorher [gesamt],1, get_option ( "comments_per_page" ) )] = "d";
			}
			else $arbeiter [commentseite ( $vorher [gesamt], 1, get_option ( "comments_per_page" ) )] = "d";
		}
		$von =commentseite ( $nachher [gesamt], $nachher [page], get_option ( "comments_per_page" ) );
		$bis=commentseite ( $nachher [gesamt], $nachher [gesamt], get_option ( "comments_per_page" ) );
		if(get_option('default_comments_page')=="newest"){
			
			if (ceil ( $vorher [gesamt] / get_option ( "comments_per_page" ) ) != ceil ( $nachher [gesamt] / get_option ( "comments_per_page" ) )) {
				$bis=commentseite ( $nachher [gesamt], 1, get_option ( "comments_per_page" ) );
			}
			
		}
		for($i = $von; $i <= $bis; $i ++) {
		#	RS_LOG("###$von => $bis = $i");
			if (! $arbeiter [$i])
				$arbeiter [$i] = "r";
		}
		

	}
		}else{
		//RS_LOG("###".rs_commentpageinfo ( $comment->comment_post_ID, $comment->comment_ID ));
		$oo=rs_commentpageinfo ( $comment->comment_post_ID, $comment->comment_ID );
	for($i = 1; $i <= ceil($oo[gesamt]/ get_option ( "comments_per_page" )); $i ++) {
			#	RS_LOG("###$von => $bis = $i");
			 
				$arbeiter [$i] = "d";
		}
	}
	
	
	if( get_option("default_comments_page")=="oldest" && $arbeiter [1]){
		$arbeiter  ["seiteselber"] = "1";
		unset($arbeiter [1]);
	}else	if(get_option('default_comments_page')=="newest" && ($arbeiter [ceil($nachher [gesamt]/ get_option ( "comments_per_page" ) )]or $nachher [gesamt]==0)){
		$arbeiter  ["seiteselber"] = "1";
		if($nachher [gesamt]==0)unset($arbeiter [1]);
			else unset($arbeiter [ceil($nachher [gesamt]/ get_option ( "comments_per_page" ) )]);
	}
	if($zwangsgrill==true)unset(	$arbeiter  ["seiteselber"]);
	//
	#RS_LOGA ( $arbeiter );

	renewrealstaic ( $comment->comment_post_ID, 123, "komentaredit", $arbeiter );
	return;
	
 
}


/**
 * komentar wird gepostet und benutzer wird weiter geleitet
 * @param int $id Kommmentarid
 * @param int $status ==0 wenn in spam; ==1 wenn approved  
 * @return void
 */
function reallystatic_newkommentar($id, $status = 0) {
	if($status==0)return;
	RS_LOG("reallystatic_newkommentar");
		rs_setbo(3);
	rs_setba(1);
	
	reallystatic_editkommentar($id);
	return;
}

############
#
#
# COMMENTAR END
#
#
###############
/**
 * @param url: url ebend
 * @param typ: =1 hin =2 zurück
 * @param von: was soll erstezt werden
 * @param nach: womit wird es ersetzt
 */
function reallystatic_rewrite($url, $typ, $von = "", $nach = "") {
	//$url=str_replace("//","/",$url);
	$url = apply_filters ( "rs-pre-urlrewriter", $url, $typ, $von, $nach );
	if ($typ == 1) {
		if ($nach != "")
			$url = str_replace ( $von, $nach, $url );
		$url = really_wp_url_make_to_static ( $url );
	} elseif ($typ == 2) {
	}
	$url = apply_filters ( "rs-post-urlrewriter", $url, $typ, $von, $nach );
	return $url;
}

/**
 * @desc resetting internal filecache database
 * @param none
 * @return none
 */
function really_static_resetdatabase(){
	do_action ( "rs-database-reset-start");
	global $wpdb;
	$table_name = REALLYSTATICDATABASE;
	$wpdb->query ( "  Delete FROM $table_name" );
	do_action ( "rs-database-reset-done");
}
/**
 * @desc rebuild the entire Blog
 * @param boolean silent
 * @return none
 */
function really_static_rebuildentireblog($silent =false) {
	global $internalrun, $test, $showokinit, $arbeitsliste;
	$internalrun = true;

	if(get_option('rs_allrefreshcache')==array()){
		global $wpdb;
		RS_LOG ( "everyday" );
		$a = loaddaten ( 'rs_everyday' );
		if(!$silent) reallystatic_configok ( "->Everyday", 2 );
		if (is_array ( $a )) {
			foreach ( $a as $v ) {
			rs_arbeitsliste_create_add(urlcorrect ( $v [0] ),loaddaten ( "localurl" ) . $v [0]);
			 
				RS_LOG ( $arbeitsliste [update] [urlcorrect ( $v [0] )] );
			}
		}
		RS_LOG ( "posteditcreatedelete" );
		$a = loaddaten ( 'rs_posteditcreatedelete' );
		if(!$silent) reallystatic_configok ( "->posteditcreatedelete:", 2 );
		if (is_array ( $a )) {
			foreach ( $a as $v ) {
				$v [0] = str_replace ( "%postname%", str_replace ( array (loaddaten ( "localurl" ), loaddaten ( "remoteurl" ) ), array ("", "" ), get_permalink ( $id ) ), $v [0] );
				#	getnpush ( loaddaten ( "localurl" ) . $v [0], $v [0], true );
				if ($v [0] == "" or substr ( $v [0], - 1 ) == "/")
					$v [0] .= "index.html";
			 rs_arbeitsliste_create_add(urlcorrect ( $v [0] ),loaddaten ( "localurl" ) . $v [0]);
				#$arbeitsliste [update] [urlcorrect ( $v [0] )] = loaddaten ( "localurl" ) . $v [0];
				#RS_LOG ( $arbeitsliste [update] [urlcorrect ( $v [0] )] );
			}
		}
		RS_LOG ( "refresh posts" );
		$table_name = REALLYSTATICDATABASE;
		if(!$silent) reallystatic_configok ( "->main", 2 );
		$lastposts = get_posts ( 'numberposts=-1' );
	 
		foreach ( $lastposts as $post ) {
	 
		#	$post->ID=1405;#################
			$querystr = "SELECT datum  FROM 	$table_name where url='" .  ( get_page_link ( $post->ID ) ) . "'";
			$ss = $wpdb->get_results ( $querystr, OBJECT );
			if ( $ss [0]->datum > 0) {
				if(!$silent) reallystatic_configok(__ ( "Skiping existing:", 'reallystatic' ) . " " . get_page_link ( $post->ID ) , 2);
			} else {
				
				$allowedtypes = array ('comment' => '', 'pingback' => 'pingback', 'trackback' => 'trackback' );
				$comtypewhere = ('all' != $args ['type'] && isset ( $allowedtypes [$args ['type']] )) ? " AND comment_type = '" . $allowedtypes [$args ['type']] . "'" : '';
				$allcoms = ($wpdb->get_var ( $wpdb->prepare ( "SELECT COUNT(comment_ID) FROM $wpdb->comments WHERE comment_post_ID = %d AND comment_parent = 0 AND comment_approved = '1' " . $comtypewhere, $post->ID ) ) - 1) / get_option ( "comments_per_page" );
				$arbeiter = Array ();
				
				if ((ceil ( $allcoms ) == $allcoms)) {
					for($i = 1; $i <= $allcoms; $i ++)
						$arbeiter [($i)] = "r";
				}
				reallystatic_configok(__ ( "Scaning:", 'reallystatic' ) . " " . get_page_link ( $post->ID ) , 2);
				 renewrealstaic ( $post->ID, true, "", $arbeiter );
			 
				# exit;#############
			#RS_LOG( "renewrealstaic ( ".$post->ID.", true );");
			}
		
		}
		 
		//Statische seitem
		RS_LOG ( "refresg static pages" );
	 
		$lastposts = get_pages ( 'numberposts=-1' );
		 
		foreach ( $lastposts as $post ) {
			
			foreach ( loaddaten ( 'rs_posteditcreatedelete' ) as $v ) {
				
			//	$t = str_replace ( "%postname%", cleanupurl ( get_page_link ( $post->ID ) ), $v [0] );
				
				$querystr = "SELECT datum  FROM 	$table_name where url='" .  ( get_page_link ( $post->ID ) ) . "'";
				$ss = $wpdb->get_results ( $querystr, OBJECT );
				if ($ss [0]->datum > 0) {
					if(!$silent)  reallystatic_configok( __ ( "Skiping existing:", 'reallystatic' ) . " " . get_page_link ( $post->ID ) , 2 );
				} else {
					
					$allowedtypes = array ('comment' => '', 'pingback' => 'pingback', 'trackback' => 'trackback' );
					$comtypewhere = ('all' != $args ['type'] && isset ( $allowedtypes [$args ['type']] )) ? " AND comment_type = '" . $allowedtypes [$args ['type']] . "'" : '';
					$allcoms = ($wpdb->get_var ( $wpdb->prepare ( "SELECT COUNT(comment_ID) FROM $wpdb->comments WHERE comment_post_ID = %d AND comment_parent = 0 AND comment_approved = '1' " . $comtypewhere, $post->ID ) ) - 1) / get_option ( "comments_per_page" );
					$arbeiter = Array ();
					
					if ((ceil ( $allcoms ) == $allcoms)) {
						for($i = 1; $i <= $allcoms; $i ++)
							$arbeiter [($i)] = "r";
					}
					renewrealstaic ( $post->ID, true, "", $arbeiter );
					#RS_LOG ( "renewrealstaic ( " . $post->ID . ", true );" );
				}
			}
		}
		update_option('rs_allrefreshcache',$arbeitsliste);
	}else{
		RS_LOG ( "use filelist from previous abortet refresh!" );
		$arbeitsliste=get_option('rs_allrefreshcache');
		 
	}
	global $allrefresh;
	$allrefresh = true;

}




/**
true wenn abfrage durch RS, geht auch nur im demomodus
*/
function really_static_selfdetect(){
##RS_LOG("HOMECHANGE".get_option ( 'home' ) ."!=". get_option ( 'siteurl' ));
#RS_LOG ("really_static_demodetect: ".really_static_demodetect());
#RS_LOG("rs_onwork:".get_option ( "rs_onwork"));
 if(!really_static_demodetect())return true;# kein demomode
if(	get_option ( "rs_onwork")==1)return true;
return false;
/*
if($_SERVER ['SERVER_ADDR']!=$_SERVER ['REMOTE_ADDR'])return false;
return true;*/
} 

/*
demo wenn home = site dann true
*/
function really_static_demodetect(){
	if(get_option ( 'home' ) != get_option ( 'siteurl' ))return 0;
	return 1;
}


function gt($a,$b){
	if($a>$b)return $a;
	else return $b;
	
}
function lt($a,$b){
	 
	if($a<$b)return $a;
	else return $b;
}
function rs_dayupdate($e,$erstell,$operation,$homeurllaenge,$pageposts,$von=false,$bis=false,$art="update"){
rs_setgo(41);
	#RS_LOG("rs_dayupdate $von => $bis $art          $e,$erstell");
	
	global $wpdb;

	
	if($von===false){
		//Tag
		$oben = date_i18n( "Y-m-d 23:59:59", ($e) );
		$unten = $erstell;
		if ($operation == "postcreate") {
			#tag =  seitenanzahl
			$querystr = "SELECT count(ID) as outa FROM " . $wpdb->prefix . "posts where post_status = 'publish' AND post_date>'" . date_i18n( "Y-m-d 0:0:0", ($e) ) . "' and post_date<='$oben'";
			$bis = $wpdb->get_results ( $querystr, OBJECT );
			if (($bis [0]->outa % $pageposts) > 0) {
				$von = 1;
				$bis = 1;
			} else {
				$bis = floor ( $bis [0]->outa / $pageposts );
				$von = 1;
			}
		} else {
			#tag = seite auf der der post erstellt wurde
			$querystr = "SELECT count(ID) as outa FROM " . $wpdb->prefix . "posts where post_type='post' AND post_status = 'publish' AND post_date>'$unten' and post_date<='$oben'";
			$bis = $wpdb->get_results ( $querystr, OBJECT );
			$bis = 1 + floor ( $bis [0]->outa / $pageposts );
			$von = $bis;
		}
	}
		for($tag = $von; $tag <= $bis; $tag ++) {
			if ($tag > 1) {
				if (rs_nonpermanent == true)
					$text = "&paged=$tag";
				else
					$text = "/page/$tag";
			} else
				$text = "";
			foreach ( loaddaten ( "rs_makestatic_a5" ) as $value ) {
				$url = $value [1];
				if ($url == "")
					$url = $value [0];
				global $seitenlinktransport;
				$seitenlinktransport = $text;
				$templink = apply_filters ( "rs-todolist-add-datedaylink",$url, substr ( get_day_link ( date_i18n( "Y", $e ), date_i18n( "m", $e ), date_i18n( "d", $e ) ), $homeurllaenge ) ,$e);
	
				#$templink = str_replace ( "%dateurl%", substr ( get_day_link ( date_i18n( "Y", $e ), date_i18n( "m", $e ), date_i18n( "d", $e ) ), $homeurllaenge ), $url );
				if($art=="update"){
					$templink = apply_filters ( "rs-todolist-add", $templink );
					if ($templink !== false)
						rs_arbeitsliste_create_add(reallystatic_rewrite ( $templink, 1 ), loaddaten ( "localurl" ) .really_static_make_to_wp_url (  $templink ));
				}else{
					$templink = apply_filters ( "rs-todolist-delete", $templink );
					if ($templink !== false)
						rs_arbeitsliste_delete_add(reallystatic_rewrite ( $templink, 1 ), loaddaten ( "localurl" ) .really_static_make_to_wp_url (  $templink ));
						
				}
			}}
	 
}
function rs_monthupdate($e,$erstell,$operation,$homeurllaenge,$pageposts,$von=false,$bis=false,$art="update"){
rs_setgo(42);
	#RS_LOG("rs_monthupdate $von => $bis $art");
	global $wpdb;
 
		//Monat
	if($von===false){
		$t = date_i18n( "t", $e );
		$oben = date_i18n( "Y-m-$t 23:59:59", ($e) );
		if ($operation == "postcreate") {
			$querystr = "SELECT count(ID) as outa FROM " . $wpdb->prefix . "posts where post_status = 'publish' AND post_date>'" . date_i18n( "Y-m-1 0:0:0", ($e) ) . "' and post_date<='$oben'";
			$bis = $wpdb->get_results ( $querystr, OBJECT );
			if (($bis [0]->outa % $pageposts) > 0) {
				$von = 1;
				$bis = 1;
			} else {
				$bis = floor ( $bis [0]->outa / $pageposts );
				$von = 1;
			}
		} else {
			$querystr = "SELECT count(ID) as outa FROM " . $wpdb->prefix . "posts where post_type='post' AND post_status = 'publish' AND post_date>'$unten' and post_date<='$oben'";
			$bis = $wpdb->get_results ( $querystr, OBJECT );
			$bis = 1 + floor ( $bis [0]->outa / $pageposts );
			$von = $bis;
		}}
		for($monat = $von; $monat <= $bis; $monat ++) {
			if ($monat > 1) {
				if (rs_nonpermanent == true)
					$text = "&paged=$monat";
				else
					$text = "/page/$monat";
			} else
				$text = "";
			foreach ( loaddaten ( "rs_makestatic_a5" ) as $value ) {
				$url = $value [1];
				if ($url == "")
					$url = $value [0];
				global $seitenlinktransport;
				$seitenlinktransport = $text;
	
				#					$templink = str_replace ( "%dateurl%", substr ( get_month_link ( date_i18n( "Y", $e ), date_i18n( "m", $e ) ), $homeurllaenge ), $url );
				$templink = apply_filters ( "rs-todolist-add-datemonthlink",$url, substr ( get_month_link ( date_i18n( "Y", $e ), date_i18n( "m", $e ) ), $homeurllaenge ) ,$e);
if($art=="update"){
				$templink = apply_filters ( "rs-todolist-add", $templink );
				
				if ($templink !== false)
					rs_arbeitsliste_create_add(reallystatic_rewrite ( $templink, 1 ),loaddaten ( "localurl" ) .  really_static_make_to_wp_url ( $templink ));
}else{
	$templink = apply_filters ( "rs-todolist-delete", $templink );
	
	if ($templink !== false)
		rs_arbeitsliste_delete_add(reallystatic_rewrite ( $templink, 1 ),loaddaten ( "localurl" ) .  really_static_make_to_wp_url ( $templink ));
	
}
	
			}}
		 
}

function rs_yearupdate($e, $erstell, $operation, $homeurllaenge, $pageposts, $von = false, $bis = false, $art = "update") {
rs_setgo(43);
	global $wpdb;
	#RS_LOG("rs_yearupdate $von => $bis $art");
	
	// Jahr
	if ($von === false) {
		$oben = date_i18n( "Y-12-31 23:59:59", ($e) );
		if ($operation == "postcreate") {
			$querystr = "SELECT count(ID) as outa FROM " . $wpdb->prefix . "posts where post_status = 'publish' AND post_date>'" . date_i18n( "Y-1-1 0:0:0", ($e) ) . "' and post_date<='$oben'";
			$bis = $wpdb->get_results ( $querystr, OBJECT );
			$bis = $wpdb->get_results ( $querystr, OBJECT );
			if (($bis [0]->outa % $pageposts) > 0) {
				$von = 1;
				$bis = 1;
			} else {
				$von = 1;
				$bis = floor ( $bis [0]->outa / $pageposts );				
			}
		} else {
			$querystr = "SELECT count(ID) as outa FROM " . $wpdb->prefix . "posts where post_type='post' AND post_status = 'publish' AND post_date>'$unten' and post_date<='$oben'";
			$bis = $wpdb->get_results ( $querystr, OBJECT );
			$bis = 1 + floor ( $bis [0]->outa / $pageposts );
			$von = $bis;
		}
	}
 
	for($jahr = $von; $jahr <= $bis; $jahr ++) {
		if ($jahr > 1) {
			if (rs_nonpermanent == true)
				$text = "&paged=$jahr";
			else
				$text = "/page/$jahr";
		} else
			$text = "";
		foreach ( loaddaten ( "rs_makestatic_a5" ) as $value ) {
			$url = $value [1];
			if ($url == "")
				$url = $value [0];
			global $seitenlinktransport;
			$seitenlinktransport = $text;
			
			// templink = str_replace ( "%dateurl%", substr ( get_year_link (
			// date_i18n( "Y", $e ) ), $homeurllaenge ), $url );
			$templink = apply_filters ( "rs-todolist-add-dateyearlink", $url, substr ( get_year_link ( date_i18n( "Y", $e ) ), $homeurllaenge ), $e );
			// S_LOG("DDD:".get_month_link ( date_i18n( "Y", $e ), date_i18n( "m", $e
			// ))." ".$templink);
			if ($art == "update") {
				$templink = apply_filters ( "rs-todolist-add", $templink );
				if ($templink !== false)
					rs_arbeitsliste_create_add ( reallystatic_rewrite ( $templink, 1 ), loaddaten ( "localurl" ) . really_static_make_to_wp_url ( $templink ) );
			} else {
				
				$templink = apply_filters ( "rs-todolist-delete", $templink );
				if ($templink !== false)
					rs_arbeitsliste_delete_add ( reallystatic_rewrite ( $templink, 1 ), loaddaten ( "localurl" ) . really_static_make_to_wp_url ( $templink ) );
			}
		}
	}
}
function rs_tagupdate($erstell,$pageposts, $tagoty,$operation,$homeurllaenge,$seite=false,$seitemax=false,$art="update",$post=false){
	rs_setgo(2);
	#RS_LOG("rs_tagupdate $seite,$seitemax $art");
	if($seite===false){
		$a=get_tag_parentids(array(),$tagoty->term_id,$post);
		
		
	$seite = (ceil ( $a[page][$tagoty->term_id] / $pageposts ));
	#$seite = (ceil ( getinnewer ( $erstell, $pageposts, $tagoty->term_id, 'post_tag' ) / $pageposts ));
	// RS_LOG("TAG BEGIN: $seite $operation ");
	if ($operation != "postcreate") {
	
		if ($seite > 1) {
			if (rs_nonpermanent == true)
				$seitee = "&paged=$seite";
			else
				$seitee = "/page/$seite";
		} else
			$seitee = "";
		
		
		$seitemax=$seite;
	 
	} else {

		$seitemax = (ceil (  $a[gesamt][$tagoty->term_id] / $pageposts ));
		if (($seitemax % $pageposts) == 0) {
			$seitemax = 1;
		} else {
			$seitemax = ceil ( $seitemax / $pageposts );
		}
	}
	}
		for($seiter = $seite; $seiter <= $seitemax; $seiter ++) {
			if ($seiter > 1) {
				if (rs_nonpermanent == true)
					$seitee = "&paged=$seiter";
				else
					$seitee = "/page/$seiter";
			} else
				$seitee = "";
			foreach ( loaddaten ( "rs_makestatic_a2" ) as $value ) {
				$url = $value [1];
				if ($url == "")
					$url = $value [0];
				global $seitenlinktransport;
				$seitenlinktransport = $seitee;
				$m = apply_filters ( "rs-todolist-add-taglink", $url,substr (  get_tag_link ( $tagoty ), $homeurllaenge ) );
				
				if($art=="update"){
				$url2 = apply_filters ( "rs-todolist-add", $m );
				if ($url2 !== false)
					rs_arbeitsliste_create_add ( reallystatic_rewrite ( $url2, 1 ), loaddaten ( "localurl" ) . really_static_make_to_wp_url ( $url2 ) );
					}else{
					$url2 = apply_filters ( "rs-todolist-delete", $m );
					rs_arbeitsliste_delete_add ( reallystatic_rewrite ( $url2, 1 ), loaddaten ( "localurl" ) . really_static_make_to_wp_url ( $url2 ) );
					}
					
					
			}
		}
	
}
function rs_arbeitsliste_delete_add($get,$push,$firstrun=true){
	global $arbeitsliste;
	if($firstrun)$get=rs_manualrewriteaddtodo($get);
	$arbeitsliste [delete] [$get] = $push;
}

/**
 * returns array with sum of post inside cat and position of post inside category
 */
function get_category_parentids($a, $id, $post = false) {
	$p = get_posts ( array (
			'numberposts'     => -1,
			'category' => $id 
	) );
	$a [gesamt] [$id] = count ( $p );
	$i = 1;
		 
	foreach ( $p as $v ) {
		 
		if ($v->ID == $post->ID)
			break;
		$i ++;
	}

 
	$a [page][$id] = $i;
	$parent = &get_category ( $id );
	if ($parent->parent != 0)
		$a = get_category_parentids ( $a, $parent->parent, $post );
	 
	return $a;
}
function get_tag_parentids($a, $id, $post = false) {
	$p=query_posts( array('tag_id'=>$id,'posts_per_page' => -1 ));
	 
	$a [gesamt] [$id] = count ( $p );
	$i = 1;
	foreach ( $p as $v ) {
 		if ($v->ID == $post->ID)
			break;
		$i ++;
	}
	
	
	if($i<=$a [gesamt] [$id] )$a [page][$id] = $i;
	$parent = &get_tag ( $id );
	if ($parent->parent != 0)
		$a = get_tag_parentids ( $a, $parent->parent, $post );
	 
	return $a;
}



function sdfsdfs($author,$timestamp){
	global $wpdb;
	if($timestamp>0) $s=" and post_date>='". date_i18n( "Y-m-d H:i:s", ($timestamp) )."'";
	$querystr = "SELECT count(ID) as outa FROM " . $wpdb->prefix . "posts where post_author='" . $author . "' AND post_status = 'publish' $s";
	#RS_LOG($querystr);
	$bis = $wpdb->get_results ( $querystr, OBJECT );
	return $bis [0]->outa ;
}


function rs_authorupdate($id, $erstell, $pageposts, $operation, $homeurllaenge, $authorid = false, $von = false, $bis = false, $art = "update") {
	rs_setgo(3);
	#RS_LOG ( "rs_authorupdate  $von => $bis" );
	global $wpdb;
	if ($von === false) {
		$e = strtotime ( $erstell );
		$oben = date_i18n( "Y-m-d 23:59:59", ($e) );
		$unten = $erstell;
		$querystr = "SELECT post_author as outo FROM " . $wpdb->prefix . "posts	WHERE ID='$id'";
		$authorid2 = $wpdb->get_results ( $querystr, OBJECT );
		$authorid = $authorid2 [0]->outo;
		if ($operation == "postcreate") {
			// ag = seitenanzahl
			$querystr = "SELECT count(ID) as outa FROM " . $wpdb->prefix . "posts where post_author='" . $authorid . "' AND post_status = 'publish'  and post_date<='$oben'";
			$bis = $wpdb->get_results ( $querystr, OBJECT );
			
			if (($bis [0]->outa % $pageposts) > 0) {
				$von = 1;
				$bis = 1;
			} else {
				$bis = floor ( $bis [0]->outa / $pageposts );
				$von = 1;
			}
		} else {
			// ag = seite auf der der post erstellt wurde
			$querystr = "SELECT count(ID) as outa FROM " . $wpdb->prefix . "posts where post_author='" . $authorid . "' AND post_status = 'publish' AND post_date>'$unten' and post_type='post'  "; // nd
			                                                                                                                                                                                        // post_date<='$oben'
			$bis = $wpdb->get_results ( $querystr, OBJECT );
			$bis = 1 + floor ( $bis [0]->outa / $pageposts );
			$von = $bis;
		}
	}
	for($seite = $von; $seite <= $bis; $seite ++) {
		if ($seite > 1) {
			if (rs_nonpermanent == true)
				$text = "&paged=$seite";
			else
				$text = "/page/$seite";
		} else
			$text = "";
		foreach ( loaddaten ( "rs_makestatic_a4" ) as $value ) {
			$url = $value [1];
			if ($url == "")
				$url = $value [0];
			global $seitenlinktransport;
			$seitenlinktransport = $text;
			
			
			$templink = apply_filters ( "rs-todolist-add-authorlink", $url, substr ( get_author_posts_url ( $authorid, '' ), $homeurllaenge ) );
			if ($art == "update") {
				$url = apply_filters ( "rs-todolist-add", $templink );
				if ($url !== false)
					rs_arbeitsliste_create_add ( reallystatic_rewrite ( $url, 1 ), loaddaten ( "localurl" ) . really_static_make_to_wp_url ( $url ) );
			} else {
				$url = apply_filters ( "rs-todolist-delete", $templink );
				if ($url !== false)
					rs_arbeitsliste_delete_add ( reallystatic_rewrite ( $url, 1 ), loaddaten ( "localurl" ) . really_static_make_to_wp_url ( $url ) );
			}
		}
	}
}









/**
Erkennt welche Kategorieseiten aktualisiert werden muss

*/
function categorry_refresh($posteditdiff, $homeurllaenge, $pageposts, $operation) {
	rs_setgo(5);
	if (loaddaten ( "rs_makecatstatic" ) == 1 and is_array ( loaddaten ( "rs_makestatic_a3" ) )) {
	if(isset($posteditdiff [cat_pre] [page]))$catids=$posteditdiff [cat_pre] [page];
	else if(isset($posteditdiff [cat_post] [page]))$catids=$posteditdiff [cat_post] [page];
	else $catids=($posteditdiff [cat_pre] [page]+$posteditdiff [cat_post] [page]);
	foreach( $catids  as $cid=>$mist){
		if(!isset($posteditdiff [cat_pre] [page] [$cid])){
			#erstellt
			#RS_LOG("Kate $cid erstellt");
			really_static_categoryrefresh ( $cid, $homeurllaenge, ceil ( $posteditdiff [cat_post] [page] [$cid] / $pageposts ), ceil ( $posteditdiff [cat_post] [gesamt] [$cid] / $pageposts ), "update" );

		}else if(!isset($posteditdiff [cat_post] [page] [$cid])){
			#gelöscht
			#RS_LOG("Kate $cid gegrillt");
			really_static_categoryrefresh ( $cid, $homeurllaenge, ceil ( $posteditdiff [cat_pre] [page] [$cid] / $pageposts ), ceil ( $posteditdiff [cat_pre] [gesamt] [$cid] / $pageposts ), "update" );

		}else{
			#ist und bleibt
			#RS_LOG("Kate $cid bearbeitet");
			if(isset($posteditdiff [cat_pre] [page] [$cid])==isset($posteditdiff [cat_post] [page] [$cid])){
				#RS_LOG("Kate $cid bleibt auf selber seite");
				really_static_categoryrefresh ( $cid, $homeurllaenge, ceil ( $posteditdiff [cat_post] [page] [$cid] / $pageposts ), ceil ( $posteditdiff [cat_post] [page] [$cid] / $pageposts ), "update" );

			}else{
				#RS_LOG("Kate $cid verschoben");
				if($posteditdiff [cat_pre] [page] [$cid] <$posteditdiff [cat_post] [page] [$cid] )	really_static_categoryrefresh ( $cid, $homeurllaenge, ceil ( $posteditdiff [cat_pre] [page] [$cid] / $pageposts ), ceil ( $posteditdiff [cat_post] [page] [$cid] / $pageposts ), "update" );
				else	really_static_categoryrefresh ( $cid, $homeurllaenge, ceil ( $posteditdiff [cat_post] [page] [$cid] / $pageposts ), ceil ( $posteditdiff [cat_pre] [page] [$cid] / $pageposts ), "update" );

			}
			
			
			
		}


	}
/*
 
 RS_LOG("Kate ALLLT");
 
 
 

			#RS_LOG("categorry_refresh");
		
		if ($operation == "postcreate") {
		#	RS_LOG ( "erstell aus cat $k" );
			foreach ( $posteditdiff [cat_post] [page] as $k => $v ) {
				#RS_LOG ( "UPDATE SEITE " . ceil ( $v / $pageposts ) . " bis " . ceil ( $posteditdiff [cat_post] [gesamt] [$k] / $pageposts ) . " @ $k" );
				really_static_categoryrefresh ( $k, $homeurllaenge, ceil ( $v / $pageposts ), ceil ( $posteditdiff [cat_post] [gesamt] [$k] / $pageposts ), "update" );
			}
		} elseif ($posteditdiff [date] [0] != "" && $posteditdiff [date] [1] != "") {
			#RS_LOG ( "innerhalb von cat verschoben" );
			foreach ( $posteditdiff [cat_pre] [page] as $k => $v ) {
			#	 RS_LOG ( "verschiebe aus cat $v nach ". $posteditdiff [cat_post][page][$k] );
				#RS_LOG ( "UPDATE SEITE " . ceil ( $posteditdiff [cat_pre] [page] [$k] / $pageposts ) . " UND " . ceil ( $posteditdiff [cat_post] [page] [$k] / $pageposts ) . " @ $k" );
				really_static_categoryrefresh ( $k, $homeurllaenge, ceil ( $posteditdiff [cat_pre] [page] [$k] / $pageposts ), ceil ( $posteditdiff [cat_pre] [page] [$k] / $pageposts ), "update" );
				really_static_categoryrefresh ( $k, $homeurllaenge, ceil ( $posteditdiff [cat_post] [page] [$k] / $pageposts ), ceil ( $posteditdiff [cat_post] [page] [$k] / $pageposts ), "update" );
			}
		} else {
			foreach ( $posteditdiff [cat_post][gesamt] as $k => $v ) {
				if (!$posteditdiff [cat_pre] [gesamt] [$k]) {
					#RS_LOG ( "Lösche aus cat $k" );
					
					if (ceil ( $posteditdiff [cat_pre] [gesamt] [$k] / $pageposts ) != ceil ( $posteditdiff [cat_post] [gesamt] [$k] / $pageposts )) {
						#RS_LOG ( "DELETE SEITE " . ceil ( $posteditdiff [cat_pre] [gesamt] [$k] / $pageposts ) . " @ $k" );
						really_static_categoryrefresh ( $k, $homeurllaenge, ceil ( $posteditdiff [cat_pre] [gesamt] [$k] / $pageposts ), ceil ( $posteditdiff [cat_pre] [gesamt] [$k] / $pageposts ), "delete" );
					}
					if (ceil ( $posteditdiff [cat_post] [gesamt] [$k] / $pageposts ) > 0){
						really_static_categoryrefresh ( $k, $homeurllaenge,ceil ( $posteditdiff [cat_pre] [page] [$k] / $pageposts ), ceil ( $posteditdiff [cat_post] [gesamt] [$k] / $pageposts ) , "update" );
						#RS_LOG ( "UPDATE SEITE " . ceil ( $posteditdiff [cat_pre] [page] [$k] / $pageposts ) . " bis " . ceil ( $posteditdiff [cat_post] [gesamt] [$k] / $pageposts ) . " @ $k" );
						
					}
				} else {
				#	RS_LOG ( "erstell aus cat $k" );
					 
					#RS_LOG ( "UPDATE SEITE " . ceil ( $posteditdiff [cat_post] [page] [$k] / $pageposts ) . " bis " . ceil ( $posteditdiff [cat_post] [gesamt] [$k] / $pageposts ) . " @ $k" );
					really_static_categoryrefresh ( $k, $homeurllaenge, ceil ( $posteditdiff [cat_post] [page] [$k] / $pageposts ), ceil ( $posteditdiff [cat_post] [gesamt] [$k] / $pageposts ), "update" );
				}
			}
		}*/
	}
}

function author_refresh($id,$posteditdiff,$erstell, $pageposts, $operation, $homeurllaenge,$authorid){
		rs_setgo(3);
	global $wpdb;
	if (loaddaten ( "rs_makeauthorstatic" ) == 1 and is_array ( loaddaten ( "rs_makestatic_a4" ) )) {
		$querystr = "SELECT post_author as outo FROM " . $wpdb->prefix . "posts	WHERE ID='$id'";
		$authorid2 = $wpdb->get_results ( $querystr, OBJECT );
		$authorid= $authorid2 [0]->outo ;
	
	if($posteditdiff[author_pre][gesamt]> $posteditdiff[author_post][gesamt]){
		#gelöscht
		if(floor($posteditdiff[author_pre][gesamt]/$pageposts)!= floor($posteditdiff[author_pre][gesamt]-1/$pageposts)){
			#beim alten ist es nun euine sache weniger
			rs_authorupdate($id,$erstell, $pageposts, $operation, $homeurllaenge,$authorid, ceil($posteditdiff[author_pre][gesamt]/$pageposts),ceil($posteditdiff[author_pre][gesamt]/$pageposts),  "delete");
			rs_authorupdate($id,$erstell, $pageposts, $operation, $homeurllaenge,$authorid, ceil($posteditdiff[author_pre][page]/$pageposts),ceil($posteditdiff[author_pre][gesamt]/$pageposts)-1,  "update");
		}else{
			rs_authorupdate($id,$erstell, $pageposts, $operation, $homeurllaenge,$authorid, ceil($posteditdiff[author_pre][page]/$pageposts),ceil($posteditdiff[author_pre][gesamt]/$pageposts),  "update");
		}
	}else	if($posteditdiff[author_pre][gesamt]< $posteditdiff[author_post][gesamt]){
		#erstell
		rs_authorupdate($id,$erstell, $pageposts, $operation, $homeurllaenge,$authorid, ceil($posteditdiff[author_post][page]/$pageposts),ceil($posteditdiff[author_post][gesamt]/$pageposts),  "update");
	}elseif($posteditdiff[author_pre][art]==$posteditdiff[author_post][art] ){
			#autorgleich
		 if($posteditdiff[author_pre][page] != $posteditdiff[author_post][page]){
				#verschoben
				RS_LOG("author verschoben");
				rs_authorupdate($id,$erstell, $pageposts, $operation, $homeurllaenge,$authorid, floor($posteditdiff[author_pre][page]/$pageposts),floor($posteditdiff[author_pre][page]/$pageposts),  "update");
				rs_authorupdate($id,$erstell, $pageposts, $operation, $homeurllaenge,$authorid, floor($posteditdiff[author_post][page]/$pageposts),floor($posteditdiff[author_post][page]/$pageposts),  "update");
	
			}else{
				#tue nix
				RS_LOG("author tue nix, nur seite selber");
				rs_authorupdate($id,$erstell, $pageposts, $operation, $homeurllaenge,$authorid, floor($posteditdiff[author_post][page]/$pageposts),floor($posteditdiff[author_post][page]/$pageposts),  "update");
			}
		}else{
			#autor geandert, refresh bei neuem author
			RS_LOG("author geandert");
			rs_authorupdate($id,$erstell, $pageposts, $operation, $homeurllaenge,$authorid, floor($posteditdiff[author_post][page]/$pageposts),floor($posteditdiff[author_post][gesamt]/$pageposts),  "update");
			if(floor($posteditdiff[author_pre][gesamt]/$pageposts)!= floor($posteditdiff[author_pre][gesamt]-1/$pageposts)){
				#beim alten ist es nun euine sache weniger
				rs_authorupdate($id,$erstell, $pageposts, $operation, $homeurllaenge,$authorid, floor($posteditdiff[author_pre][gesamt]/$pageposts),floor($posteditdiff[author_pre][gesamt]/$pageposts),  "delete");
				rs_authorupdate($id,$erstell, $pageposts, $operation, $homeurllaenge,$authorid, floor($posteditdiff[author_pre][page]/$pageposts),floor($posteditdiff[author_pre][gesamt]/$pageposts)-1,  "update");
			}else{
				rs_authorupdate($id,$erstell, $pageposts, $operation, $homeurllaenge,$authorid, floor($posteditdiff[author_pre][page]/$pageposts),floor($posteditdiff[author_pre][gesamt]/$pageposts),  "update");
			}
		}
	
			
		#rs_authorupdate($erstell, $pageposts, $operation, $homeurllaenge, $von = false, $bis = false, $art = "update");
	}
	
}

function date_refresh($posteditdiff, $erstell, $operation, $homeurllaenge, $pageposts) {
#	RS_LOGA($posteditdiff);
	// date
	if (loaddaten ( "rs_makedatestatic" ) == 1 and is_array ( loaddaten ( "rs_makestatic_a5" ) )) {
		#RS_LOG("date_refresh");
		if (loaddaten ( "rs_makedatetagstatic" ) == 1) {
			rs_setgo(41);
			if ($operation == "postcreate") {
			#	RS_LOG ( "ERSTELL" );
				$a = ceil ( $posteditdiff [post_page] [date_day] / $pageposts );
				$b = ceil ( $posteditdiff [post_page] [date_day_gesamt] / $pageposts );
				
				rs_dayupdate ( strtotime ( $erstell ), $erstell, $operation, $homeurllaenge, $pageposts, $a, $b, "update" );
			} elseif ($posteditdiff [date] [0] == "" && $operation != "postdelete") {
			#	RS_LOG ( "DATUM BLIEB GLEICH" );
				$a = ceil ( $posteditdiff [post_page] [date_day_gesamt] / $pageposts )-ceil ( $posteditdiff [post_page] [date_day] / $pageposts );
			 
				rs_dayupdate ( strtotime ( $erstell ), $erstell, $operation, $homeurllaenge, $pageposts, $a, $a, "update" );
			} else {
				if (substr ( $posteditdiff [date] [0], 0, 10 ) == substr ( $posteditdiff [date] [1], 0, 10 )) {
					#RS_LOG ( "EDIT TAG BLIEB GLEICH, uhrzeit anders" );
					$a = floor ( lt ( $posteditdiff [pre_page] [date_day], $posteditdiff [post_page] [date_day] ) / $pageposts );
					$b = floor ( gt ( $posteditdiff [pre_page] [date_day], $posteditdiff [post_page] [date_day] ) / $pageposts );
					rs_dayupdate ( strtotime ( $erstell ), $erstell, $operation, $homeurllaenge, $pageposts, $a, $b, "update" );
				} else {
				#	RS_LOG ( "EDIT TAG geändert" );
					$oo = $posteditdiff [pre_page] [date_day_gesamt] / $pageposts;
					rs_dayupdate ( strtotime ( $posteditdiff [date] [1] ), $posteditdiff [date] [1], $operation, $homeurllaenge, $pageposts, ceil ( $posteditdiff [post_page] [date_day] / $pageposts ), ceil ( $posteditdiff [post_page] [date_day_gesamt] / $pageposts ), "update" );
					if (ceil ( $oo ) != ($oo)) {
						if ($operation == "postdelete")
							rs_dayupdate ( strtotime ( $posteditdiff [date] [1] ), $posteditdiff [date] [1], $operation, $homeurllaenge, $pageposts, ceil ( $oo ), ceil ( $oo ), "delete" );
						else
							rs_dayupdate ( strtotime ( $posteditdiff [date] [0] ), $posteditdiff [date] [0], $operation, $homeurllaenge, $pageposts, ceil ( $oo ), ceil ( $oo ), "delete" );
						$oo --;
					}
					if ($operation == "postdelete")
						rs_dayupdate ( strtotime ( $posteditdiff [date] [1] ), $posteditdiff [date] [1], $operation, $homeurllaenge, $pageposts, ceil ( $posteditdiff [pre_page] [date_day] / $pageposts ), ceil ( $oo ), "update" );
					else
						rs_dayupdate ( strtotime ( $posteditdiff [date] [0] ), $posteditdiff [date] [0], $operation, $homeurllaenge, $pageposts, ceil ( $posteditdiff [pre_page] [date_day] / $pageposts ), ceil ( $oo ), "update" );
				}
			}
		}
		// ##########
		if (loaddaten ( "rs_makedatemonatstatic" ) == 1) {
		rs_setgo(42);
			if ($operation == "postcreate") {
				#RS_LOG ( "Monat ERSTELL" );
				$a = ceil ( $posteditdiff [post_page] [date_month] / $pageposts );
				$b = ceil ( $posteditdiff [post_page] [date_month_gesamt] / $pageposts );
				rs_monthupdate ( strtotime ( $erstell ), $erstell, $operation, $homeurllaenge, $pageposts, $a, $b, "update" );
			} elseif ($posteditdiff [date] [0] == "" && $operation != "postdelete") {
			#	RS_LOG ( "Monat DATUM BLIEB GLEICH" );
				$a = ceil ( $posteditdiff [post_page] [date_month_gesamt] / $pageposts )-ceil ( $posteditdiff [post_page] [date_month] / $pageposts );
				
				rs_monthupdate ( strtotime ( $erstell ), $erstell, $operation, $homeurllaenge, $pageposts, $a, $a, "update" );
			} else {
				if (substr ( $posteditdiff [date] [0], 0, 10 ) == substr ( $posteditdiff [date] [1], 0, 10 )) {
					
					$a = floor ( lt ( $posteditdiff [pre_page] [date_month], $posteditdiff [post_page] [date_month] ) / $pageposts );
					$b = floor ( gt ( $posteditdiff [pre_page] [date_month], $posteditdiff [post_page] [date_month] ) / $pageposts );
					#RS_LOG ( "Monat EDIT TAG BLIEB GLEICH, uhrzeit anders $a $b" );
					rs_monthupdate ( strtotime ( $erstell ), $erstell, $operation, $homeurllaenge, $pageposts, $a, $b, "update" );
				} else {
				#	RS_LOG ( "Monat EDIT TAG geändert" );
					// neues up
					rs_monthupdate ( strtotime ( $posteditdiff [date] [1] ), $posteditdiff [date] [1], $operation, $homeurllaenge, $pageposts, ceil ( $posteditdiff [post_page] [date_month] / $pageposts ), ceil ( $posteditdiff [post_page] [date_month_gesamt] / $pageposts ), "update" );
					// vorheriges up
					$oo = $posteditdiff [pre_page] [date_month_gesamt] / $pageposts;
					if (floor ( $oo ) != ($oo)) {
						if ($operation == "postdelete")
							rs_monthupdate ( strtotime ( $posteditdiff [date] [1] ), $posteditdiff [date] [1], $operation, $homeurllaenge, $pageposts, ceil ( $oo ), ceil ( $oo ), "delete" );
						else
							rs_monthupdate ( strtotime ( $posteditdiff [date] [0] ), $posteditdiff [date] [0], $operation, $homeurllaenge, $pageposts, ceil ( $oo ), ceil ( $oo ), "delete" );
						$oo --;
					}
					if ($oo >= 0) {
						if ($operation == "postdelete")
							rs_monthupdate ( strtotime ( $posteditdiff [date] [1] ), $posteditdiff [date] [1], $operation, $homeurllaenge, $pageposts, ceil ( $posteditdiff [pre_page] [date_month] / $pageposts ), ceil ( $oo ), "update" );
						else
							rs_monthupdate ( strtotime ( $posteditdiff [date] [0] ), $posteditdiff [date] [0], $operation, $homeurllaenge, $pageposts, ceil ( $posteditdiff [pre_page] [date_month] / $pageposts ), ceil ( $oo ), "update" );
					}
				}
			}
		}
			// ################
			#RS_LOGA($posteditdiff);
		if (loaddaten ( "rs_makedatejahrstatic" ) == 1) {
		rs_setgo(43);
			if ($operation == "postcreate") {
			#	RS_LOG ( "ERSTELL" );
				$a = ceil ( $posteditdiff [post_page] [date_year] / $pageposts );
				$b = ceil ( $posteditdiff [post_page] [date_year_gesamt] / $pageposts );
				rs_yearupdate ( strtotime ( $erstell ), $erstell, $operation, $homeurllaenge, $pageposts, $a, $b, "update" );
			} elseif ($posteditdiff [date] [0] == "" && $operation != "postdelete") {
			
				$a =  ceil ( $posteditdiff [post_page] [date_year_gesamt] / $pageposts )- ceil ( $posteditdiff [post_page] [date_year] / $pageposts );
			 
			#	RS_LOG ( "DATUM BLIEB GLEICH $a " );
				
				rs_yearupdate ( strtotime ( $erstell ), $erstell, $operation, $homeurllaenge, $pageposts, $a, $a, "update" );
			} else {
				if (substr ( $posteditdiff [date] [0], 0, 10 ) == substr ( $posteditdiff [date] [1], 0, 10 )) {
				#	RS_LOG ( "EDIT TAG BLIEB GLEICH, uhrzeit anders" );
					$a = floor ( lt ( $posteditdiff [pre_page] [date_year], $posteditdiff [post_page] [date_year] ) / $pageposts );
					$b = floor ( gt ( $posteditdiff [pre_page] [date_year], $posteditdiff [post_page] [date_year] ) / $pageposts );
					rs_yearupdate ( strtotime ( $erstell ), $erstell, $operation, $homeurllaenge, $pageposts, $a, $b, "update" );
				} else {
					#RS_LOG ( "EDIT TAG geändert" );
					$oo = $posteditdiff [pre_page] [date_year_gesamt] / $pageposts;
					rs_yearupdate ( strtotime ( $posteditdiff [date] [1] ), $posteditdiff [date] [1], $operation, $homeurllaenge, $pageposts, ceil ( $posteditdiff [post_page] [date_year] / $pageposts ), ceil ( $posteditdiff [post_page] [date_year_gesamt] / $pageposts ), "update" );
					if (floor ( $oo ) != ($oo)) {
						if ($operation == "postdelete")
							rs_yearupdate ( strtotime ( $posteditdiff [date] [1] ), $posteditdiff [date] [1], $operation, $homeurllaenge, $pageposts, ceil ( $oo ), ceil ( $oo ), "delete" );
						else
							rs_yearupdate ( strtotime ( $posteditdiff [date] [0] ), $posteditdiff [date] [0], $operation, $homeurllaenge, $pageposts, ceil ( $oo ), ceil ( $oo ), "delete" );
						$oo --;
					}
					if ($operation == "postdelete")
						rs_yearupdate ( strtotime ( $posteditdiff [date] [1] ), $posteditdiff [date] [1], $operation, $homeurllaenge, $pageposts, ceil ( $posteditdiff [pre_page] [date_year] / $pageposts ), ceil ( $oo ), "update" );
					else
						rs_yearupdate ( strtotime ( $posteditdiff [date] [0] ), $posteditdiff [date] [0], $operation, $homeurllaenge, $pageposts, ceil ( $posteditdiff [pre_page] [date_year] / $pageposts ), ceil ( $oo ), "update" );
				}
			}
		}
		// ###########
	}
}


function tag_refresh($posteditdiff, $erstell, $pageposts, $k, $operation, $homeurllaenge, $pageposts) {
	rs_setgo(2);
	
	// Tags
	if (loaddaten ( "rs_maketagstatic" ) == 1 and is_array ( loaddaten ( "rs_makestatic_a2" ) )
	and is_array($posteditdiff [tag_post] [gesamt])) {
	#	RS_LOG("tag_refresh");
	 

		
		foreach (  $posteditdiff [tag_post] [gesamt] as $v => $vvvvv) {
			
			$s = $posteditdiff [tag_pre] [page] [$v]; // grill
			if ($s == "")
				$s = $posteditdiff[tag_post] [page] [$v]; // hinzu
			
			$ges = $posteditdiff [tag_pre] [gesamt] [$v];
			if ($ges == "")
				$ges = $posteditdiff[tag_post] [gesamt] [$v];
			
			if ($posteditdiff[tag_pre] [page] [$v]&& $posteditdiff [tag_post] [page] [$v]) {
				# RS_LOG("tag blieb");
				 #refresh nur die seite
				 rs_tagupdate ( $erstell, $pageposts, $v, $operation, $homeurllaenge, ceil ( $s / $pageposts ), ceil ( $s / $pageposts ), "update" );
				 	
			} elseif ($posteditdiff [tag_pre] [page] [$v]) {
				#RS_LOG("tag wurde entfernt $ges $pageposts");
				if (ceil ( $ges / $pageposts ) != ceil ( ($ges - 1) / $pageposts )) {
					#RS_LOG("letzte seite weggefallen".$s." ".$ges." ".$pageposts);
					rs_tagupdate ( $erstell, $pageposts, $v, $operation, $homeurllaenge, ceil ( $ges / $pageposts ), ceil ( $ges / $pageposts ), "delete" );
				}
				rs_tagupdate ( $erstell, $pageposts, $v, $operation, $homeurllaenge, ceil ( $posteditdiff [tag_pre] [page] [$v] / $pageposts ), ceil (  ($posteditdiff [tag_pre] [gesamt] [$v]-1) / $pageposts ), "update" );
			} else {
				#RS_LOG("tag wurde hinzugefuegt @ $v $s $ges $pageposts ");
				// ".ceil($s/$pageposts)."#".ceil($ges/$pageposts));
				rs_tagupdate ( $erstell, $pageposts, $v, $operation, $homeurllaenge, ceil ( $s / $pageposts ), ceil ( $ges / $pageposts ), "update" );
			}
		}
	/*	if (count ( $posteditdiff [tag] ) == 0) {
			// efresh nur die seite
			RS_LOG ( "tag nur refresh" );
			if($operation=="postdelete") $aaa="delete";
				else $aaa="update";
			foreach ( $posteditdiff [tag_post] [page] as $k => $v ) 
				
				
			  rs_tagupdate ( $erstell, $pageposts, $k, $operation, $homeurllaenge, ceil ( $v / $pageposts ), ceil ( $v / $pageposts ), "update" );
		}*/
		
		
		
	}
}

function index_refresh($erstell, $pageposts, $homeurllaenge, $posteditdiff) {
rs_setgo(1);
	if (loaddaten ( "rs_makeindexstatic" ) == 1 and is_array ( loaddaten ( "rs_makestatic_a1" ) )) {
		#RS_LOGA($posteditdiff);
		if ($posteditdiff [pre_page] [postgesamt] == $posteditdiff [post_page] [postgesamt]) {
			#RS_LOG("index edit");
			if ($posteditdiff [pre_page] [post] == $posteditdiff [post_page] [post]) {
				#RS_LOG("index nix passiert");
				$a=floor($posteditdiff [pre_page] [post]/$pageposts);
				#floor($posteditdiff [pre_page] [post]/$pageposts)
				index_update($erstell,$pageposts,$homeurllaenge,$a,$a,"update");
				
			} elseif ($posteditdiff [pre_page] [post] != $posteditdiff [post_page] [post]) {
				#RS_LOG("index verschoben");
				index_update($erstell,$pageposts,$homeurllaenge,floor($posteditdiff [pre_page] [post]/$pageposts),floor($posteditdiff [pre_page] [post]/$pageposts),"update");
				index_update($erstell,$pageposts,$homeurllaenge,floor($posteditdiff [post_page] [post]/$pageposts),floor($posteditdiff [post_page] [post]/$pageposts),"update");
				
				
			}
		} elseif ($posteditdiff [pre_page] [postgesamt] > $posteditdiff [post_page] [postgesamt]) {
		 
			
			
			
			$bis=ceil($posteditdiff [pre_page] [postgesamt]/$pageposts);
			$bis2=ceil($posteditdiff [post_page] [postgesamt]/$pageposts);
		#	RS_LOG("index gelöscht $bis > $bis2");
			
			
		if($bis>$bis2){
			#letze seite weg
			index_update($erstell,$pageposts,$homeurllaenge,$bis,$bis,"delete");
			$bis--;
		}
		index_update($erstell,$pageposts,$homeurllaenge,floor($posteditdiff [post_page] [post]/$pageposts),$bis,"update");
		
			
			
		} elseif ($posteditdiff [pre_page] [postgesamt] < $posteditdiff [post_page] [postgesamt]) {
		#RS_LOG("index add");
			index_update($erstell,$pageposts,$homeurllaenge,ceil($posteditdiff [post_page] [post]/$pageposts),ceil($posteditdiff [post_page] [postgesamt]/$pageposts),"update");
				
		}
	}
}

function index_update($erstell,$pageposts,$homeurllaenge,$von=false,$bis=false,$art="update") {
	global $wpdb;
	#RS_LOG("index_update $von $bis $art");
		if($von===false){
		if ($operation == "postcreate") {
			$querystr = "SELECT count(ID) as outo FROM " . $wpdb->prefix . "posts	WHERE post_type='post' and post_status = 'publish' ";
			$normaleseiten = $wpdb->get_results ( $querystr, OBJECT );
			$bis = ceil ( $normaleseiten [0]->outo / $pageposts );
			$von = 1;
		} else {
			$querystr = "SELECT count(ID) as outo FROM " . $wpdb->prefix . "posts	WHERE post_type='post' and post_status = 'publish' AND post_date>'$erstell'";
			$normaleseiten = $wpdb->get_results ( $querystr, OBJECT );
			$von = $bis = ceil ( $normaleseiten [0]->outo / $pageposts );
		}
		}
		for($normaleseiten = $von; $normaleseiten <= $bis; $normaleseiten ++) {
			
			if ($normaleseiten > 1) {
				if (rs_nonpermanent == true)
					$text = "?paged=$normaleseiten";
				else
					$text = "page/$normaleseiten";
			} else
				$text = "";
			foreach ( loaddaten ( "rs_makestatic_a1" ) as $value ) {
				$url = $value [1];
				if ($url == "")
					$url = $value [0];
				global $seitenlinktransport;
				$seitenlinktransport = $text;
				
				if ($text == "")
					$normaleseiten2 = "index.html";
				else
					$normaleseiten2 = "";
				if (strpos ( $normaleseiten2, get_option ( 'home' ) . "/" ) === false)
					$normaleseiten2 = get_option ( 'home' ) . "/" . $normaleseiten2;
					
					// S_LOG($normaleseiten2 );
				$normaleseiten2 = (really_static_rewrite1 ( $normaleseiten2 ));
				// S_LOG($normaleseiten2 );
				// templink = get_option ( 'home' ) . "/" . str_replace (
				// "%indexurl%", substr ( $normaleseiten2, $homeurllaenge ),
				// $url );
				// templink = substr ( $templink, $homeurllaenge );
				$templink = apply_filters ( "rs-todolist-add-indexlink", $url, substr ( $normaleseiten2, $homeurllaenge ) );
				

				if($art=="update"){
				$templink = apply_filters ( "rs-todolist-add", $templink );
				if ($templink !== false)
					rs_arbeitsliste_create_add ( reallystatic_rewrite ( $templink, 1 ), loaddaten ( "localurl" ) . really_static_make_to_wp_url ( $templink ) );
				}else{
					$templink = apply_filters ( "rs-todolist-delete", $templink );
					if ($templink !== false)
						rs_arbeitsliste_delete_add ( reallystatic_rewrite ( $templink, 1 ), loaddaten ( "localurl" ) . really_static_make_to_wp_url ( $templink ) );
				}

			}
			
			// $arbeitsliste[update][nonpermanent(urlcorrect(str_replace (
		// array("%indexurl%","//"), array($normaleseiten,"/"), $url
		// )))]=loaddaten ( "localurl" ) . str_replace (
		// array("%indexurl%","//"), array($normaleseiten,"/"), $url );
			global $seitenlinktransport;
			$seitenlinktransport="";
		}
 
}
function comment_refresh($id,$homeurllaenge, $subarbeitsliste) {
rs_setgo(7);
	global $wpdb;
	if (get_option ( 'page_comments' )) {
		$allowedtypes = array (
				'comment' => '',
				'pingback' => 'pingback',
				'trackback' => 'trackback' 
		);
		$comtypewhere = ('all' != $args ['type'] && isset ( $allowedtypes [$args ['type']] )) ? " AND comment_type = '" . $allowedtypes [$args ['type']] . "'" : '';
		
		$seitenanzahl = ceil ( $wpdb->get_var ( $wpdb->prepare ( "SELECT COUNT(comment_ID) FROM $wpdb->comments WHERE comment_post_ID = %d AND comment_parent = 0 AND comment_approved = '1' " . $comtypewhere, $id ) ) ) / get_option ( "comments_per_page" );
		
		if (! is_array ( $subarbeitsliste )) {
			for($i = 1; $i <= ($seitenanzahl); $i ++) {
				if (! (($i == $seitenanzahl && 'newest' == get_option ( 'default_comments_page' )) or ($i == 1 && 'newest' != get_option ( 'default_comments_page' ))))
					$subarbeitsliste [$i] = "r";
			}
		}
		if (is_array ( $subarbeitsliste )) {
			foreach ( $subarbeitsliste as $i => $akt ) {
				
				$templink = substr ( get_comments_pagenum_link ( $i ), $homeurllaenge );
				if(strpos($templink,"#")!==false)$templink=substr($templink,0,strpos($templink,"#"));
				#RS_LOG("templink $templink");
				$url = apply_filters ( "rs-todolist-add", $templink );
				if ($url !== false) {
					foreach ( loaddaten ( "rs_makestatic_a6" ) as $value ) {
						$url = str_replace ( '%commenturl%', $url, $value [0] );
						if ($akt == "r")
							rs_arbeitsliste_create_add (reallystatic_rewrite ( $url, 1 ), loaddaten ( "localurl" ) . really_static_make_to_wp_url ( $url ));
						elseif ($akt == "d")
							rs_arbeitsliste_delete_add (reallystatic_rewrite ( $url, 1 ), loaddaten ( "localurl" ) . really_static_make_to_wp_url ( $url ));
					}
				}
			}
		}
	}
}
function seiteselberrefresh($id, $operation, $homeurllaenge, $subarbeitsliste,$post_status) {
rs_setgo(6);
	global $wpdb;
	$a = loaddaten ( 'rs_posteditcreatedelete' );
	if (is_array ( $a )) {
		
		$querystr = "SELECT post_content as outo FROM " . $wpdb->prefix . "posts	WHERE ID='$id'";
		$normaleseiten = $wpdb->get_results ( $querystr, OBJECT );
		$pagecontent = $normaleseiten [0]->outo;
		foreach ( $a as $v ) {
			if ($operation != "komentarerstellt" or ereg ( "%postname%", $v [0] )) {#Quickfix, das beim kommentar erstellen, nur wichtiges
				if (! isset ( $subarbeitsliste ["seiteselber"] ) or ereg ( "%postname%", $v [0] )) {
					// nset ( $sourcefile );
					$normaleseiten = apply_filters ( "rs-pagecount", 1 + substr_count ( $pagecontent, "<!--nextpage-->" ), $pagecontent, $qq );
					if (strpos ( $v [0], "%postname%" ) !== false) {
						$normaleseiten2 = $normaleseiten;
					} else
						$normaleseiten2 = 1;
						
					#RS_LOG("SEITE SELBER: for($seite = 1; $seite <= $normaleseiten2; $seite ++) {");
					for($seite = 1; $seite <= $normaleseiten2; $seite ++) {
						if ($seite > 1) {
							if (rs_nonpermanent == true)
								$text = "&page=$seite";
							else
								$text = "/page/$seite";
						} else
							$text = "";
				global $seitenlinktransport;
				$seitenlinktransport = $text;
						if (ereg ( "%postname%", $v [0] ))
							$qq = get_option ( 'home' ) . "/" . str_Replace ( "%postname%", substr ( get_permalink ( $id ), $homeurllaenge ), $v [0] );
						else
							$qq = get_option ( 'home' ) . "/" . $v [0];
						
						$templink = substr ( $qq, $homeurllaenge );
						

						if ($post_status == "trash") {
							$url = apply_filters ( "rs-todolist-delete", $templink );
							if ($url !== false)
								rs_arbeitsliste_delete_add ( reallystatic_rewrite ( $url, 1, $v [0], $v [1] ), 1 );
						} else {
							$url = apply_filters ( "rs-todolist-add", $templink );
							
							if ($url !== false)
								rs_arbeitsliste_create_add ( reallystatic_rewrite ( $url, 1, $v [0], $v [1] ), loaddaten ( "localurl" ) . really_static_make_to_wp_url ( $url ) );
						}
					}
				}
			}
		}
		global $seitenlinktransport;
		$seitenlinktransport="";
	}
}

function main_count_post(){
	global $wpdb;
	$querystr = "SELECT count(ID) as outo FROM " . $wpdb->prefix . "posts	WHERE post_type='post' and post_status = 'publish' ";
	$normaleseiten = $wpdb->get_results ( $querystr, OBJECT );
	return  $normaleseiten [0]->outo;
 
	
}
function main_count_post_until($timestamp){
	global $wpdb;
	$querystr = "SELECT count(ID) as outo FROM " . $wpdb->prefix . "posts	WHERE post_type='post' and post_status = 'publish' AND post_date>'$timestamp'";
	$normaleseiten = $wpdb->get_results ( $querystr, OBJECT );
	return $normaleseiten [0]->outo ;

}

/**
 * Cronjob: stundlich
 *
 * @since 0.3
 * @param none
 * @return bool everytime true
 */
function reallystatic_hourcronjob() {
 	//RS_LOG("reallystatic_hourcronjob");
 	$rulecron=get_option( 'rs_rulecron');
 	foreach($rulecron as $k=>$v){
 		if ( $v[ts]==0){
 			if($v[tw]==0){#nur aktuelle seite
 	
 				rs_arbeitsliste_create_add (rs_datereplace($v[tm],$v[dc]), loaddaten ( "localurl" ) .rs_datereplace($v[tm],$v[dc]));
 	
 			}else{#alle seiten
 				rs_manualrewriteaddtodopregmatch("r",rs_datereplace($v[tm],$v[dc]));
 			}
 				
 		}
 	}
 	
	return true;
}
add_action ( 'reallystatic_hourlyevent', 'reallystatic_hourcronjob' );
/**
 * Cronjob: Taeglich
 *
 * @since 0.3
 * @param none
 * @return bool everytime true
 */
function reallystatic_daycronjob() {
versionupdate();
	 //	RS_LOG("reallystatic_daycronjob");
	 	$rulecron=get_option( 'rs_rulecron');
	 	foreach($rulecron as $k=>$v){
	 		if ( $v[ts]==1){
	 			if($v[tw]==0){#nur aktuelle seite
	 	
	 				rs_arbeitsliste_create_add (rs_datereplace($v[tm],$v[dc]), loaddaten ( "localurl" ) .rs_datereplace($v[tm],$v[dc]));
	 	
	 			}else{#alle seiten
	 				rs_manualrewriteaddtodopregmatch("r",rs_datereplace($v[tm],$v[dc]));
	 			}
	 				
	 		}
	 	}
	 	
	return true;
}
add_action ( 'reallystatic_daylyevent', 'reallystatic_daycronjob' );
/**
 * Cronjob: Monatlich
 *
 * @since 0.3
 * @param none
 * @return bool everytime true
 */
function reallystatic_monthcronjob() {
	if( date_i18n("d")!=1)return;
	$rulecron=get_option( 'rs_rulecron');
	foreach($rulecron as $k=>$v){
		if ( $v[ts]==2){
			if($v[tw]==0){#nur aktuelle seite
				
				rs_arbeitsliste_create_add (rs_datereplace($v[tm],$v[dc]), loaddaten ( "localurl" ) .rs_datereplace($v[tm],$v[dc]));
				
			}else{#alle seiten
				rs_manualrewriteaddtodopregmatch("r",rs_datereplace($v[tm],$v[dc]));
			}
			
		}
	}
	
 	RS_LOG("reallystatic_monthcronjob");
	return true;
}
add_action ( 'reallystatic_monthlyevent', 'reallystatic_monthcronjob' );

/**
 * was r = refresh, d=delete
 * preg 
 */
function rs_manualrewriteaddtodopregmatch($was,$preg){ 
	global $wpdb,$table_name;
 	$res = $wpdb->get_results("SELECT url FROM ".REALLYSTATICDATABASE);
 	foreach ($res as $row) {
		if(preg_match($preg,$row->url)){
			if($was=="r")rs_arbeitsliste_create_add( $row->url, loaddaten ( "localurl" ) . $row->url,false);
			else rs_arbeitsliste_delete_add( $row->url, loaddaten ( "localurl" ) . $row->url,false);
		}
	}
	
	
}
 





function rs_filter_cron_schedules( $schedules ) {
	// add a 'weekly' schedule to the existing set
	$schedules['monthly'] = array(
			'interval' => 86400,
			'display' => __('Once a month')
	);
	return $schedules;
}
add_filter( 'cron_schedules',  'rs_filter_cron_schedules'  );


########## NO docmentation, because outsourced
add_action('admin_init', 'rs_upgrade' );
function rs_upgrade()
{
	if(get_option ( 'rs_firstTime')!= RSVERSION . RSSUBVERSION){
		RS_LOG("rs_upgrade");
		require_once("php/functions.php");
		require_once("sonstiges/wppluginintegration.php");
		rs_upgrade_real();
	}
}
register_activation_hook ( __FILE__, 'rs_activation' );
function rs_activation() {
	 
	
	RS_LOG("rs_activation");
	require_once("sonstiges/wppluginintegration.php");
	rs_activation_real();
}

register_deactivation_hook ( __FILE__, 'rs_deactivation' );
function rs_deactivation() {
	RS_LOG("rs_deactivation");
	require_once("sonstiges/wppluginintegration.php");
	rs_deactivation_real();
}



add_filter('get_sample_permalink_html','rs_showstaticlinkoneditor',10,2);
function rs_showstaticlinkoneditor($tt,$id){
	if(!really_static_demodetect())return $tt;
	$p=get_post($id);
 
	return $tt."<span id='view-post-btn'><a href='".really_wp_url_make_to_static(str_replace(loaddaten ( "localurl" ),loaddaten ( "remoteurl" ),$p->guid))."' class='button' target='_blank'>View static page</a></span>";
}
 


add_action( 'admin_notices' , 'rs_showinfohints' );
function rs_showinfohints() {
 	$t = get_option ( 'rs_showokmessage' );
	if ( $t==false or count ( $t ) == 0)
		return;
	#RS_LOG("##########################");
	#RS_LOG($t);
	#RS_LOGA($t);
	
	foreach ( $t as $k => $v ) {
		
		if ($v [0] == 1)
			echo '<div class="updated"> <p>' . $v [1] . '</p>  </div>';
		else
			echo '<div class="error"> <p>' . $v [1] . '</p>  </div>';
		
		if ($v [2] == "" or $_GET [$v [2]] ==$v[3])
			unset ( $t [$k] );
	}
	update_option ( 'rs_showokmessage', $t );
}
/*
 * 1=info
 * 2=error
 * 3= fatal error => wp_die
 */
function rs_addmessage($shownow,$text,$art=1,$getname="",$getvalue=""){
	 
	if($art==3)wp_die("<h1>fatal error</h1>".$text.'<br><br><a href="javascript:history.back()">back to previous page</a>',"fatal error");
	if($shownow){
		if ($art == 1)
			echo '<div class="updated"> <p>' .$text . '</p>  </div>';
		else
			echo '<div class="error"> <p>' . $text . '</p>  </div>';
		return;
	}
	$m=get_option( 'rs_showokmessage');
	$m[]=array($art,$text,$getname,$getvalue);
	update_option( 'rs_showokmessage',$m);
}


/**
liefert Nummer des bearbeiteten objektes:
1=Post
2=Page
3=Komentar
*/
function rs_getbo(){
	global $rs_bo;
	return $rs_bo;
}
/**
setfunktion von rs_getbo, siehe beschreibung dort
*/
function rs_setbo($art){
	global $rs_bo;
	$rs_bo=$art;
}
/**
int int
*/
function rs_matchbo($zutestenderbo){
	if($zutestenderbo==0)return true;
	$aktuellerbo=rs_getbo();
	if($aktuellerba==$zutestenderba)return true;
	if($aktuellerbo==1 and $zutestenderbo==12)return true;
	if($aktuellerbo==2 and $zutestenderbo==12)return true;
	return false;
}

/**
	<option value="0">bearbeitet</option>
	<option value="1">erstellt</option>
	<option  value="2">editiert</option>
	<option  value="3">gelöscht</option>
*/
function rs_getba(){
	global $rs_ba;
	return $rs_ba;
}
function rs_setba($art){
	global $rs_ba;
	$rs_ba=$art;
}

function rs_matchba($zutestenderba){
	if($zutestenderba==0)return true;
	$aktuellerba=rs_getba();
	if($aktuellerba==$zutestenderba)return true;
	return false;
}
/**
	<option value="0">irgendwas</option>
	<option value="1">indexpage</option>
	<option value="2">tagpage</option>
	<option value="3">authorpage</option>
	<option value="40">irgendeine Datumsseite</option>	
	<option value="41">Tag-Datum</option>
	<option value="42">Monat-Datum</option>
	<option value="43">Jahr-Datum</option>
*/
function rs_getgo(){
	global $rs_go;
	return $rs_go;
}
function rs_setgo($art){
	global $rs_go;
	$rs_go=$art;
}
function rs_matchgo($zutestendergo){


	if($zutestendergo==0)return true;
	$aktuellergo=rs_getgo();
	rs_LOG("rs_matchgo($zutestendergo  $aktuellergo)  ");

	
	if($aktuellergo==$zutestendergo)return true;
	if($zutestendergo==40 and ($aktuellergo==41 or $aktuellergo==42 or $aktuellergo==43))return true;
	return false;
}

function rs_manualrewriteaddtodo($url){
	#$rule[]=array("bo"=>0,"ba"=>0,"go"=>3,"pm"=>0,"pt"=>"",
	#"dn"=>1,"to"=>'feed/');
	$rule=get_option( 'rs_rule');
	// RS_LOGA($rule);
	#$rule[]=array("bo"=>0,"ba"=>0,"go"=>3,"pm"=>0,"pt"=>"",
	#"dn"=>1,"to"=>'de/$1');

#	$rule[]=array("bo"=>0,"ba"=>1,"go"=>0,"pm"=>0,"pt"=>"",
	#"dn"=>1,"to"=>'sitemap.xml');
	
 
 
if(count($rule)==0  || !is_array($rule))return $url;
	 foreach($rule as $r){
		if(rs_matchbo($r[bo]) and rs_matchba($r[ba])and rs_matchgo($r[go])){//RS_LOG("A");
			if($r[pm]==0 or preg_match($r[pt],$url)){//RS_LOG("b $url");
				$r[to]=rs_datereplace(str_replace('$1',$url,$r[to]),$r[da]);

				if($r[dn]==0){#erneuere auch
					 rs_arbeitsliste_create_add($r[to], loaddaten ( "localurl" ) .$r[to],false);
				 
				}elseif ($r[dn]==1){#erneuere passendes aus cache
					rs_manualrewriteaddtodopregmatch("r",$r[to]);
				}elseif($r[dn]==2){#lösche auch
					 rs_arbeitsliste_delete_add($r[to], loaddaten ( "localurl" ) .$r[to],false);
					 }elseif ($r[dn]==3){#lösche passendes aus cache
					 	rs_manualrewriteaddtodopregmatch("d",$r[to]);
				 
					
				}
			
			
		
			}
		}
	}
	return $url;
}
function rs_manualrewriteonurlrewrite($url,$pre){
	#RS_LOG("rs_manualrewriteonurlrewrite($url,$pre)");
	#$rule[]=array("pm"=>"#^feed/$#","rz"=>"feed.xml");
	$rule=get_option( 'rs_rulerw');
	if(count($rule)==0 || !is_array($rule))return $url;
	$url=substr(rawurldecode($url),strlen($pre)); # wandelt codierte zeichenkennten in url um zu normal
	 
	foreach($rule as $r){
	 
		$url=preg_replace($r["pm"],rs_datereplace($r["rz"],$r["db"]),$url);

	}
 
	
	$url= $pre.str_replace(array("%2F","%23","%3F","%3D"),array("/","#","?","="),rawurlencode($url));
	#RS_LOG("rs_manualrewriteonurlrewrite($url)");
	return $url;
}


/**
return true when linux
*/
function rs_hostlinux(){
if (strtoupper(substr(PHP_OS, 0, 3)) === 'WIN')return false;
return true; 
}
/**
return / when linux
*/
function rs_de($art=1){
if(rs_hostlinux())return "/";
if($art==1)return "\\\\";
else return "\\";
}
 function rs_designremote(){
 $transport = apply_filters ( "rs-transport", array () );
	return call_user_func_array ( $transport [loaddaten ( "rs_save" )] [8], array ("") );
 
 }
 function rs_remoteurl(){
 $transport = apply_filters ( "rs-transport", array () );
 return @call_user_func_array ( $transport [loaddaten ( "rs_save" )] [7], array ("") );

 
 }
function rs_fragehelp($text){return '<a style="cursor:pointer;"  onclick="toggleVisibility(\'internallocalpfad\');" >[?]</a>  <div style="max-width:500px; text-align:left; display:none" id="internallocalpfad">('.$text.')</div>';}
function rs_options(){return array('rs_fileextensions' ,'rs_expertmode','rs_stupidfilereplaceA','rs_stupidfilereplaceB','rs_stupidfilereplaceC','rs_stupidfilereplaceD','rs_counter','rs_firstTime','rs_makestatic_a1','rs_makestatic_a2','rs_makestatic_a3','rs_makestatic_a4','rs_makestatic_a5','rs_makestatic_a6','rs_makestatic_a7'
,'rs_posteditcreatedelete','rs_urlrewriteinto','rs_lokalerspeicherpfad','rs_nonpermanent','rs_localpath','rs_subpfad','rs_remoteurl','rs_differentpaths','rs_save','rs_designlocal','rs_designremote','rs_everytime','rs_pageeditcreatedelete','rs_commenteditcreatedelete'
,'rs_everyday','rs_rulerw','rs_rule','rs_rulecron','rs_donationid','rs_donationidk','rs_maketagstatic','rs_makecatstatic','rs_makeauthorstatic','rs_makedatestatic','rs_makedatetagstatic','rs_makedatemonatstatic'
,'rs_makedatejahrstatic','rs_makeindexstatic','rs_hide_adminpannel','rs_showokmessage','rs_onwork','rs_ftpsaveroutine','rs_allrefreshcache','rs_versionsize','rs_versiondate','rs_informaboutupdate','rs_debugstrip');}


add_action( 'wp_head', 'rs_looptest' );
function rs_looptest(){
echo '<meta name="really-static looptest" content="testtesttest" />';
}
 
 
 
function rs_getinstalledplugins(){
	$avp = get_plugins();
	foreach($avp as $k=>$v) {

		if(strtolower(substr($v[Name],0,13))=="really-static"){
			$a[]=array("s"=>$k,"n"=>substr(strtolower($v[Name]),14),"v"=>$v[Version]);
		}
	}
	return $a;
}
 


 


function rs_check_for_plugin_update($checked_data) {
	global   $wp_version;
	
	//Comment out these two lines during testing.
	 if (empty($checked_data->checked))
	 	return $checked_data;
	foreach(rs_getinstalledplugins() as $rsip){
	$args = array(		'slug' =>  $rsip[n],		'version' => $rsip[v],		'reqsource' => get_bloginfo('url'),		'key' =>loaddaten ( "rs_donationidk" )	);
	$request_string = array('body' => array('action' => 'basic_check', 	'request' => serialize($args), 'api-key' => md5(get_bloginfo('url'))),'user-agent' => 'WordPress/' . $wp_version . '; ' . get_bloginfo('url'));
	$raw_response = wp_remote_post(REALLYSTATICUPDATEAPIURL, $request_string);
	
	if (!is_wp_error($raw_response) && ($raw_response['response']['code'] == 200))
		$response = unserialize($raw_response['body']);
 	if (is_object($response) && !empty($response)){
	  $response->package=str_replace("%key%",loaddaten ( "rs_donationidk" ),$response->package);
 		$checked_data->response[ $rsip[s]] = $response;
	} 
	}
	return $checked_data;
}
// Take over the update check
add_filter('pre_set_site_transient_update_plugins', 'rs_check_for_plugin_update');



function rs_plugin_api_call($def, $action, $args) {
	global $wp_version;
	$ok=false;
		foreach(rs_getinstalledplugins() as $rsip){
		
		
	if (!isset($args->slug) || ($args->slug != $rsip[n]))
		continue;
	$ok=true;
	$args->version = $rsip[v];
	$args->key = loaddaten ( "rs_donationidk" );
	$args->reqsource = get_bloginfo('url')	;
	$request_string = array('body' => array('action' => $action, 'request' => serialize($args),	'api-key' => md5(get_bloginfo('url'))),'user-agent' => 'WordPress/' . $wp_version . '; ' . get_bloginfo('url'));
	$request = wp_remote_post(REALLYSTATICUPDATEAPIURL, $request_string);
	if (is_wp_error($request)) {
 return false;	
 } else {
		$res = unserialize($request['body']);
		
		if ($res === false)	 return false;
	}
	
	}
	if($ok)return $res;
	else return false;
}
 
 add_filter('plugins_api', 'rs_plugin_api_call', 10, 3);
 
/*
testet string auf vorkommen von date variablen
*/
function rs_testfordate($s){
	$teste=array("d","j","m","n","Y","y","g","G","h","H","i","s");
	foreach($teste as $v) if(preg_match('#\$'.$v.'#is',$s))return true;
	return false;
}
function rs_datereplace($s,$repl=true){
if($repl!==true)return $s;
	$teste=array("d","j","m","n","Y","y","g","G","h","H","i","s");
	foreach($teste as $v) $s=preg_replace('#\$'.$v.'#is', date_i18n($v),$s);
	return $s;
}


 
?>